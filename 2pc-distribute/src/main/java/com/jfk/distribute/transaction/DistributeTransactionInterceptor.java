package com.jfk.distribute.transaction;



import com.jfk.distribute.entities.TransactionException;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.distribute.entities.TransactionResult;
import com.jfk.distribute.util.UUIDUtils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.EnsurePath;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class DistributeTransactionInterceptor {

  private static Logger logger = Logger.getLogger(DistributeTransactionInterceptor.class);

  @Autowired
  private ZookeeperClient zookeeperClient;

  @Pointcut("@annotation (com.jfk.distribute.annotation.DistributeTransaction)")
  private void authorityPointcut() {
  }

  /**
   * 切面方式对启用分布式事务的方法管理机制
   */
  @Around("authorityPointcut()")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    Object obj = null;
    //获取方法参数中是否有TransactionLock 对象参数
    Object[] args = pjp.getArgs();
    TransactionLock lock = null;
    if (args.length > 0) {
      for (Object o : args) {
        if (o instanceof TransactionLock) {
          lock = (TransactionLock) o;
        }
      }
    }
    String method = pjp.getSignature().getName();
    String target = pjp.getTarget().toString();
    if (lock != null) {
      //有TransactionLock 加入zk事物控制
      logger.debug("<-- distribut transaction starting -->");
      CuratorFramework client = zookeeperClient.getClient();
      PathChildrenCache cache = null;
      TransactionResult result = new TransactionResult();
      String path = lock.getParent() + UUIDUtils.generatorTransactionPath();
      EnsurePath ensurePath = client.newNamespaceAwareEnsurePath(path);
      ensurePath.ensure(client.getZookeeperClient());
      try {
        try {
          cache = pathChildrenCache(client, lock.getParent(), true, result, lock.getCount());
          cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
          // 执行方法体
          obj = pjp.proceed(args);
          client.setData().forPath(path, "0".getBytes());
        } catch (Exception e) {
          logger.debug(
              "distribut transaction for method[" + target + "." + method + "] exception:-->", e);
          //正常设置为0，异常设置为-1，超时设置为-2
          try{
            client.setData().forPath(path, "-1".getBytes()); //如果调用者超时，导致产生hystrix 发送作用删除当前ensurePath 会报错 KeeperException$NoNodeException
          }catch (Exception ex){
          }
          throw new TransactionException(
              "distribut transaction for methed[" + target + "." + method + "] exception:-->" + e
                  .getMessage());
        }
        //线程超时没有返回，抛出异常，事务回滚
        long count = lock.getTimeout();
        while (!result.isEnd()) {
          count -= 200;
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
          }
          if (count <= 0) {
            logger.debug(
                "<-- distribut transaction timeout 调用超时没有返回，抛出异常，事务回滚-->");
            client.setData().forPath(path, "-2".getBytes());
            result.setEnd(true);
            //这里需要抛出异常，让事务回滚
            throw new TransactionException(
                "<-- distribut transaction timeout rollback!调用超时没有返回，抛出异常，事务回滚 -->");
          }
        }
        if (!result.hasResult()) {
          logger.debug("<-- distribut transaction exception rollback! 这通常意味着方法在执行中报错 -->");
          throw new TransactionException(
              "<-- distribut transaction exception rollback! 这通常意味着方法在执行中报错 -->");
        }
      } catch (Exception e) {
        throw new TransactionException(e);
      } finally {
        if (cache != null) {
          CloseableUtils.closeQuietly(cache);
          logger.debug("distribut transaction closed PathChildrenCacheListener -->");
        }
      }
      logger.debug("distribut transaction end -->");
    } else {

      obj = pjp.proceed(args);
      logger.debug(" nothing distribut transaction -->");

    }
    return obj;
  }

  /**
   * 异常通知
   */
  @AfterThrowing(throwing = "e", pointcut = "authorityPointcut()")
  public void doThrow(JoinPoint jp, Throwable e) {
    String method = jp.getSignature().getName();
    String target = jp.getTarget().toString();
    throw new TransactionException("方法[" + target + ":" + method + "]执行中报错==", e);
  }

  /**
   * 获取watcher实例
   *
   * @param path             事务监听节点
   * @param result           事务结果
   * @param transactionCount 参与本次事务的服务数量
   */
  public static PathChildrenCache pathChildrenCache(CuratorFramework client, final String path,
                                                    Boolean cacheData,
                                                    final TransactionResult result,
                                                    final int transactionCount) throws Exception {
    final PathChildrenCache cached = new PathChildrenCache(client, path, cacheData);
    cached.getListenable().addListener(new PathChildrenCacheListener() {
      @Override
      public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
          throws Exception {
        PathChildrenCacheEvent.Type eventType = event.getType();
        switch (eventType) {
          case CONNECTION_RECONNECTED:
            cached.rebuild();
            break;
          case CONNECTION_LOST:
            logger.debug("Connection error,waiting -->");
            break;
          default:
            System.out.println(eventType.name());
            List<String> list = client.getChildren().forPath(path);
            logger.debug(
                "zk notify event " + event.getType().name() + " finish count is " + list.size()
                + "-->");
            if (list.size() == transactionCount) {
              List<String> datas = new ArrayList<String>(5);
              for (String str : list) {
                String data = new String(client.getData().forPath(path + "/" + str));
                System.out.println(data);
                logger.debug("zk notify event " + event.getType().name() + " finish data is " + data
                             + "-->");
                datas.add(data);
                if (StringUtils.isEmpty(data)) {
                  result.setEnd(false);
                  return;
                }
              }
              result.setResult(true);
              result.setEnd(true);
              for (String data : datas) {
                if ("-1".equals(data) || "-2".equals(data)) {
                  result.setResult(false);
                  break;
                }
              }
            }
        }
      }
    });
    return cached;
  }

  public ZookeeperClient getZookeeperClient() {
    return zookeeperClient;
  }

  public void setZookeeperClient(ZookeeperClient zookeeperClient) {
    this.zookeeperClient = zookeeperClient;
  }

}
