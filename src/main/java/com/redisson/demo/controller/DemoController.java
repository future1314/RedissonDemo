package com.redisson.demo.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by D.K on 2017/7/15.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;////Integer ???不可以吗
    @Autowired
    private RedissonClient redissonClient;

    @ResponseBody
    @RequestMapping("/lock3")
    public String lock3() {
        int serverId = 3;
//        Long counter = redisTemplate.opsForValue().increment("COUNTER", 1);

        RLock lock = redissonClient.getLock("TEST");
        try {
            lock.lock();
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] locked and begun...");
            Thread.sleep(2000); // 2 sec
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] ended successfully...");
        } catch (Exception ex) {
            logger.error("Error occurred");
        } finally {
            lock.unlock();
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] unlocked...");
        }

        return "lock-" + Thread.currentThread().getName() + "[" + serverId +"]";
    }

    @ResponseBody
    @RequestMapping("/lock2")
    public String lock2() {
        int serverId = 2;
//        Long counter = redisTemplate.opsForValue().increment("COUNTER", 1);
        RLock lock = redissonClient.getLock("TEST");
        try {
            lock.lock();
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] locked and begun...");
            Thread.sleep(15000); // 15 sec
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] ended successfully...");
        } catch (Exception ex) {
            logger.error("Error occurred");
        } finally {
            lock.unlock();
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] unlocked...");
        }

        return "lock-" + Thread.currentThread().getName() + "[" + serverId +"]";
    }

    @ResponseBody
    @RequestMapping("/lock")
    public String lock(@RequestParam("sid") String serverId) {

        RLock lock = redissonClient.getLock("TEST");
        try {
            lock.lock();
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] locked and begun...");
            Thread.sleep(5000); // 5 sec
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] ended successfully...");
        } catch (Exception ex) {
            logger.error("Error occurred");
        } finally {
            lock.unlock();
            logger.info("Request Thread - " + Thread.currentThread().getName() + "[" + serverId +"] unlocked...");
        }

        return "lock-" + Thread.currentThread().getName() + "[" + serverId +"]";
    }

    @ResponseBody
    @RequestMapping("/lockfor")
    public String lockThread() {
        //int serverId = 2;
        //redisTemplate.opsForValue().set("COUNTER","0");
        for (int serverId = 0; serverId <10 ; serverId++) {
            new Thread(()->{
                RLock lock = redissonClient.getLock("TEST");
                try {
                    lock.lock();
                    logger.error("Request Thread - " + Thread.currentThread().getName() + "[........] locked and begun...");
                    Thread.sleep(15000); // 15 sec
                    logger.error("Request Thread - " + Thread.currentThread().getName() + "[........] ended successfully...");
                } catch (Exception ex) {
                    logger.error("-----Error occurred");
                } finally {
                    lock.unlock();
                    logger.error("Request Thread - " + Thread.currentThread().getName() + "[........] unlocked---");
                }
                Long counter = redisTemplate.opsForValue().increment("COUNTER-", 1);
                logger.error("Request Thread - " + Thread.currentThread().getName() + "[... "+counter+" ....] <<<>>>>---");
            }).start();
        }
        return "locked....";// + Thread.currentThread().getName() + "[" + serverId +"]";
    }
}
