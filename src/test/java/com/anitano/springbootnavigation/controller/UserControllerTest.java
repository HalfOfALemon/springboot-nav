package com.anitano.springbootnavigation.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: UserControllerTest
 * @Author: 杨11352
 * @Date: 2020/3/24 23:09
 */
class UserControllerTest {
Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    void getUser() {
        RedisTemplate<String,Object> redisTemplate =new RedisTemplate<>();
        Object key1 = redisTemplate.opsForValue().get("key1");
        logger.info(key1.toString());
    }
}