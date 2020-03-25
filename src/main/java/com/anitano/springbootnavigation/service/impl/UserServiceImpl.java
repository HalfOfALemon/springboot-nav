package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.repository.UserRepository;
import com.anitano.springbootnavigation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @ClassName: UserServiceImpl
 * @Author: 杨11352
 * @Date: 2020/3/24 16:53
 */
@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public WebsiteUser getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public WebsiteUser login(String username,String password) {
        WebsiteUser resultUser = userRepository.findByUsername(StringUtils.trimWhitespace(username));
        if(resultUser != null && password.equals(resultUser.getPassword())){
            return resultUser;
        }
        return null;
    }

    @Override
    public String checkToken(String username) {
        String token = (String)redisTemplate.opsForValue().get("token:" + username);
        return token == null ? null:token;
    }
}
