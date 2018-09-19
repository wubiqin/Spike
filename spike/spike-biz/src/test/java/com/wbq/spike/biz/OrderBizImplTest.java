package com.wbq.spike.biz;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wbq.spike.api.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 19 九月 2018
 *  
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class OrderBizImplTest {

    @Resource
    private OrderService orderService;

    private ExecutorService executorService;

    @Test
    public void createOrder() throws InterruptedException {
        init();
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Inner());
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("worker running");
        }
    }

    @Test
    public void createOrderOptimistic() throws InterruptedException {
        init();
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Inner());
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("worker running");
        }
    }

    @Test
    public void createOrderOptimisticByRedis() throws InterruptedException {

    }



    private class Inner implements Runnable {

        @Override
        public void run() {
            orderService.createOrderOptimistic(1);
        }
    }

    private void init() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("wbq-thread-%d").build();
        executorService = new ThreadPoolExecutor(50, 50, 0L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(200), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}