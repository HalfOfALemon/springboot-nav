package com.anitano.springbootnavigation.service;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserService
 * @Author: 杨11352
 * @Date: 2020/3/24 16:52
 */
public interface UserService {
    /**根据用户名获取某个用户*/
    WebsiteUser getUser(String username);
    /**用户登录*/
    WebsiteUser login(String username,String password);
    /**检查token是否存在*/
    String checkToken(String username);
}
