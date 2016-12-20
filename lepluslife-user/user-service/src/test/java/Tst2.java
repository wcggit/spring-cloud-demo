import com.jfk.UserApplication;
import com.jfk.mapper.UserMapper;
import com.jfk.service.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/11/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserApplication.class)
@WebAppConfiguration
@IntegrationTest
public class Tst2 {

  @Inject
  private UserService userService;

  @Inject
  private UserMapper userMapper;

  @Test
  public void optimisticTest() {
    int threads = 100;
    CountDownLatch end = new CountDownLatch(2);
    Date date = new Date();
    AtomicInteger atomicInteger = new AtomicInteger(0);
    new Thread(() -> {
      for (int i = 1; i <= 100; i++) {
        while (userService.userManage() == 0) {

        }
      }
      end.countDown();
    }).start();
    new Thread(() -> {
      for (int i = 1; i <= 100; i++) {
        while (userService.userManage() == 0) {
        }
      }
      end.countDown();
    }).start();
    try {
      end.await();
      System.out.println(System.currentTimeMillis() - date.getTime());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void pessimisticTest() {
    CountDownLatch end = new CountDownLatch(2);
    Date date = new Date();
    new Thread(() -> {
      for (int i = 1; i <= 100; i++) {
        userService.pessimisticTest();
      }
      end.countDown();
    }).start();
    new Thread(() -> {
      for (int i = 1; i <= 100; i++) {
        userService.pessimisticTest();
      }
      end.countDown();
    }).start();
    try {
      end.await();
      System.out.println(System.currentTimeMillis() - date.getTime());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
    public void optimisticTest2() {
    int threads = 2;
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    for (int i = 1; i <= threads; i++) {
      executor.execute(new Thread(() -> {
//        for (int m = 1; m <= 100; m++) {
          while (userService.userManage() == 0) {
//          }
        }
      }));
    }
    Date date = new Date();
    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    System.out.println(System.currentTimeMillis() - date.getTime());
  }

  @Test
  public void pessimisticTest2() {
    int threads = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    for (int i = 1; i <= threads; i++) {
      executor.execute(new Thread(() -> {
        for (int m = 1; m <= 100; m++) {
        userService.pessimisticTest();
        }
      }));
    }
    Date date = new Date();
    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    System.out.println(System.currentTimeMillis() - date.getTime());
  }
}
