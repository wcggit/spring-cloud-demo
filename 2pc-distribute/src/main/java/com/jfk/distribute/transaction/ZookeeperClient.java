package com.jfk.distribute.transaction;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ZookeeperClient implements ApplicationContextAware, InitializingBean, DisposableBean {

  @Value("${server.zkAddress}")
  private  String zkConnection;

  private static CuratorFramework client = null;

  private static ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

  private static ApplicationContext applicationContext;

  public ZookeeperClient() {
  }


  /**
   * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) {
    checkApplicationContext();
    return (T) applicationContext.getBean(name);
  }

  /**
   * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(Class<T> clazz) {
    checkApplicationContext();
    return (T) applicationContext.getBeansOfType(clazz);
  }

  private static void checkApplicationContext() {
    Assert.notNull(applicationContext, "applicaitonContext未注入ZookeeperClient");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    client = CuratorFrameworkFactory.newClient(zkConnection, retryPolicy);
    client.start();
  }

  @Override
  public void destroy() throws Exception {
    if (client != null) {
      CloseableUtils.closeQuietly(client);
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    ZookeeperClient.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public ZookeeperClient(String zkConnection) {
    this.zkConnection = zkConnection;
  }

  public CuratorFramework getClient() {
    if (client == null) {
      client = CuratorFrameworkFactory.newClient(zkConnection, retryPolicy);
      client.start();
    }
    return client;
  }


}