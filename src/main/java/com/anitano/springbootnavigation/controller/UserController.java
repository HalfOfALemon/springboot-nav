package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.converter.WebsiteUserToUserDTOconverter;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.UserDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.repository.UserRepository;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.MD5Util;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
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
public class UserController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("user/login")
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
     * 检查token是否正确
     * @return
     */
    @GetMapping("user/token")
    public ResultVO token(@RequestParam(value = "username") String username , @RequestParam(value = "token") String token){
        String resultToken = userService.checkToken(username,token);
        if(resultToken !=null ){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.TOKEN_INVALID);
    }
    /**
     * 根据一个id，获取用户信息
     * @param id
     * @return
     */
    @GetMapping("user/{id}")
    public ResultVO<UserDTO> user(@PathVariable(value = "id")Integer id){
        WebsiteUser websiteUser = userService.getUser(id);
        UserDTO userDTO = WebsiteUserToUserDTOconverter.convert(websiteUser);
        return ResultVOUtil.success(userDTO);
    }
    /*===========================管理员===================================*/
    /**
     * 获取用户列表
     * @param page 页码
     * @param size 每页大小
     * @param query 是否带查询条件
     * @return
     */
    @GetMapping("/users")
    public ResultVO<UserDTO> users(@RequestParam(value = "page",defaultValue = "1")Integer page,@RequestParam(value = "size",defaultValue = "5")Integer size,
                                   @RequestParam(value = "query")String query){
        //使用pageable的子类pagerequest构建一个对象
        PageRequest pageRequest = PageRequest.of(page-1,size);
        Page<UserDTO> userDTOS = userService.getUsers(pageRequest,query);
        return ResultVOUtil.success(userDTOS);
    }

    /**
     * 添加一个新用户
     * @param websiteUser
     * @return
     */
    @PostMapping("/users")
    public ResultVO users(@RequestBody WebsiteUser websiteUser){
        //验证用户是否存在
        WebsiteUser resultUser = userService.getUser(websiteUser.getUsername());
        if(resultUser != null){
            return ResultVOUtil.error(ResultCodeEnum.USER_HAS_EXISTED);
        }
        //添加用户信息
        websiteUser.setCreateTime(new Date());
        websiteUser.setUpdateTime(new Date());
        websiteUser.setRole(1);
        websiteUser.setState(true);
        //对密码进行MD5加密
        websiteUser.setPassword(MD5Util.MD5EncodeUtf8(websiteUser.getPassword()));
        //保存用户
        resultUser = userService.userSave(websiteUser);
        if(resultUser!=null){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.CREATE_FAIL);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("user/{id}")
    public ResultVO delete(@PathVariable(value = "id")Integer id){
        //查询用户是否存在
        WebsiteUser websiteUser = userService.getUser(id);
        if(websiteUser == null){
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        userRepository.delete(websiteUser);
        return ResultVOUtil.success();
    }

    /**
     * 修改用户状态
     * @param id
     * @param state
     * @return
     */
    @PutMapping("user/{id}/state/{state}")
    public ResultVO userStateChange(@PathVariable(value = "id")Integer id,@PathVariable(value = "state")Boolean state){
        WebsiteUser websiteUser = userService.getUser(id);
        if(websiteUser == null){
            ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        websiteUser.setState(state);
        userService.userSave(websiteUser);
        return ResultVOUtil.success();
    }

    /**
     * 修改用户信息
     * @param id
     * @param userDTO
     * @return
     */
    @PutMapping("user/{id}")
    public ResultVO user(@PathVariable(value = "id")Integer id,@RequestBody UserDTO userDTO){
        //查询用户是否存在
        WebsiteUser resultUser = userService.getUser(id);
        if(resultUser == null){
            ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //复制信息过去
        BeanUtils.copyProperties(userDTO,resultUser);
        //修改用户信息
        WebsiteUser resultSave = userService.userSave(resultUser);
        if(resultSave != null){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.DATA_IS_WRONG);
    }
}
