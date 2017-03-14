package com.ly.common.utils.duplicate;

import com.lianjia.fn.common.shiro.RedisManager;
import com.lianjia.fn.duplicate.SimpleDistributedLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yangxiaochen
 * @date 2016/10/27 13:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class SimpleLockTest {

    Logger logger = LogManager.getLogger(SimpleLockTest.class);

    @Autowired
    RedisManager redisManager;
    @Autowired
    SimpleDistributedLock simpleDistributedLock;


    @Test
    @Ignore
    public void test() throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("t1 locking kkk");
                simpleDistributedLock.lock("kkk");
                logger.info("t1 locked kkk");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }

                simpleDistributedLock.unlock("kkk");
                logger.info("t1 unlocked kkk");
            }
        }).start();
        Thread.sleep(2000);
        logger.info("main locking kkk");
        simpleDistributedLock.lock("kkk");
        logger.info("main locked kkk");

        simpleDistributedLock.unlock("kkk");
        logger.info("main unlock kkk");
    }
}
