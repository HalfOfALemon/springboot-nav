package com.anitano.springbootnavigation;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringbootNavigationApplicationTests {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate2;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void testRedis() {
        //测试保存对象
        String key = "WebsiteUser:1";
        redisTemplate.opsForValue().set(key, new WebsiteUser(1,"yang","12345"));
        //WebsiteUser user = (WebsiteUser) redisTemplate.opsForValue().get(key);
        //logger.info("uesr: "+user.toString());
    }
    @Test
    void testRedis2() {
        //测试获取
        String key = "WebsiteUser:4";
        WebsiteUser user = (WebsiteUser) redisTemplate2.opsForValue().get(key);
        logger.info("uesr: "+user.toString());
    }
    @Test
    void testRedis3() {
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations ops = redisTemplate.opsForValue();
        //ops.set("k1", "v1");
        //Object k1 = ops.get("key1");
        Object k1 = ops.get("k1");
        logger.info("====================="+k1);
    }
}
