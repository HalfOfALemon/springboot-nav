package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserController
 * @Author: 杨11352
 * @Date: 2020/3/24 16:59
 */
@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResultVO<WebsiteUser> login(@RequestBody WebsiteUser user){
        //1、查询用户是存在，密码是否正确
        WebsiteUser resultUser = userService.login(user.getUsername(),user.getPassword());
        if(resultUser !=null){
            //2、生成token
            String loginToken = UUID.randomUUID().toString().replace("-", "");
            //3、保存到redis,设置过期时间
            redisTemplate.opsForValue().set("token:"+resultUser.getUsername(),loginToken,6, TimeUnit.DAYS);
            //4、返回给前端
            Map<String,String> map = new HashMap();
            map.put("username",resultUser.getUsername());
            map.put("token",loginToken);
            return ResultVOUtil.success(map);
        }
        return ResultVOUtil.error(ResultCodeEnum.USER_LOGIN_ERROR);
    }

    /**
     * 检查token是否存在
     * @return
     */
    @GetMapping("token")
    public ResultVO token(@RequestParam(value = "username") String username , @RequestParam(value = "token") String token){
        String resultToken = userService.checkToken(username);
        if(resultToken !=null){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.TOKEN_INVALID);
    }

}
