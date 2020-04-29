package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.converter.WebsiteUserToUserDTOconverter;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.UserDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.repository.UserRepository;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.AttributeUtil;
import com.anitano.springbootnavigation.utils.MD5Util;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserServiceImpl
 * @Author: 杨11352
 * @Date: 2020/3/24 16:53
 */
@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(getClass());
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
    public ResultVO login(WebsiteUser user) {
        //1、判断登录方式
        boolean isEmail = AttributeUtil.isEmail(user.getEmail());
        WebsiteUser resultUser;
        //是邮箱登录
        if(isEmail){
            resultUser = userRepository.findByUsername(StringUtils.trimWhitespace(user.getEmail()));
        }else {
            //是用户名登录
            resultUser = userRepository.findByUsername(StringUtils.trimWhitespace(user.getUsername()));
        }
        //2、验证密码
        String password = MD5Util.MD5EncodeUtf8(user.getPassword());
        if(resultUser != null && password.equals(resultUser.getPassword())){
            //3、判断用户存存在，保存token
            //生成token
            String loginToken = UUID.randomUUID().toString().replace("-", "");
            //保存到redis,设置过期时间
            redisTemplate.opsForValue().set("token:"+resultUser.getId(),loginToken,30, TimeUnit.DAYS);
            //返回给前端
            Map<String,String> map = new HashMap();
            map.put("username",resultUser.getUsername());
            map.put("token",loginToken);
            return ResultVOUtil.success(map);
        }
        logger.info("[普通用户登录]登录失败");
        return ResultVOUtil.error(ResultCodeEnum.USER_LOGIN_ERROR);
    }

    @Override
    public String checkToken(String username,String token) {
        WebsiteUser user = userRepository.findByUsername(username);
        String ResultToken = (String)redisTemplate.opsForValue().get("token:" + user.getId());
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
        }else {
            //查询到的用户数据List 直接组装成Page
            websiteUserPage= userRepository.findByUsernameLike(pageable,"%"+query+"%");
        }
        //2、本来获 得到Page就可以返回出去，但是还要将数据装换成DTO，所以重新组装成PageImpl，Page一个实现类
        List<UserDTO> userDTOList = WebsiteUserToUserDTOconverter.convert(websiteUserPage.getContent());
        //3、所有page的子类PageImpl,构造一个page
        userDTOPage = new PageImpl<>(userDTOList,pageable,websiteUserPage.getTotalElements());
        return userDTOPage;
    }

    @Override
    public WebsiteUser userSave(WebsiteUser websiteUser) {
        return userRepository.save(websiteUser);
    }

    @Override
    public ResultVO registerUser(WebsiteUser websiteUser) {
        //验证用户是否存在
        WebsiteUser resultUser = userRepository.findByUsername(websiteUser.getUsername());
        if(resultUser != null){
            logger.info("[注册用户]用户已存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_HAS_EXISTED);
        }
        //验证邮箱是否存在
        WebsiteUser resultEmail = userRepository.findByEmail(websiteUser.getEmail());
        if(resultEmail != null){
            logger.info("[注册用户]邮箱已存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_EMAIL_HAS_EXISTED);
        }
        //添加用户信息
        websiteUser.setCreateTime(new Date());
        websiteUser.setUpdateTime(new Date());
        websiteUser.setRole(1);
        websiteUser.setState(true);
        //对密码进行MD5加密
        websiteUser.setPassword(MD5Util.MD5EncodeUtf8(websiteUser.getPassword()));
        //保存用户
        //保存用户
        WebsiteUser resultSave = userRepository.save(websiteUser);
        if(resultSave!=null){
            return ResultVOUtil.success(resultSave.getId());
        }
        return ResultVOUtil.error(ResultCodeEnum.CREATE_FAIL);
    }
}
