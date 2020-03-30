package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.converter.WebsiteUserToUserDTOconverter;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.UserDTO;
import com.anitano.springbootnavigation.repository.UserRepository;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.MD5Util;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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
    public WebsiteUser getUser(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public WebsiteUser getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public WebsiteUser login(String username,String password) {
        password = MD5Util.MD5EncodeUtf8(password);
        WebsiteUser resultUser = userRepository.findByUsername(StringUtils.trimWhitespace(username));
        if(resultUser != null && password.equals(resultUser.getPassword())){
            return resultUser;
        }
        return null;
    }

    @Override
    public String checkToken(String username,String token) {
        String ResultToken = (String)redisTemplate.opsForValue().get("token:" + username);
        if(ResultToken == null || !ResultToken.equals(token)){
            return null;
        }
        return token;
    }

    @Override
    public Page<UserDTO> getUsers(Pageable pageable,String query) {
        Page<WebsiteUser> websiteUserPage ;
        PageImpl<UserDTO> userDTOPage;
        /*判断带不带查询条件*/
        if(StringUtils.isEmpty(query)){
            //1、查询条件为空，查询所有
            websiteUserPage = userRepository.findAll(pageable);
            //2、本来获得到Page就可以返回出去，但是还要将数据装换成DTO，所以重新组装成PageImpl，Page一个实现类
            List<UserDTO> userDTOList = WebsiteUserToUserDTOconverter.convert(websiteUserPage.getContent());
            //3、所有page的子类PageImpl,构造一个page
            userDTOPage = new PageImpl<>(userDTOList,pageable,websiteUserPage.getTotalElements());
        }else {
            //查询到的用户数据List 直接组装成Page
            List<WebsiteUser> websiteUserList = userRepository.findByUsernameLike("%"+query+"%");
            List<UserDTO> userDTOList = WebsiteUserToUserDTOconverter.convert(websiteUserList);
            userDTOPage = new PageImpl<>(userDTOList,pageable,websiteUserList.size());
        }
        return userDTOPage;
    }

    @Override
    public WebsiteUser userSave(WebsiteUser websiteUser) {
        return userRepository.save(websiteUser);
    }
}
