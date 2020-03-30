package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: UserServiceImplTest
 * @Author: 杨11352
 * @Date: 2020/3/25 16:26
 */
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Test
    void checkToken() {
        String yang = userService.checkToken("yang2","123");
        System.out.println(yang);
    }
}