package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.converter.WebsiteUserToUserDTOconverter;
import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.UserDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.repository.UserRepository;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.service.WebsiteInfoService;
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

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: UserController
 * @Author: 杨11352
 * @Date: 2020/3/24 16:59
 */
@RestController
public class UserController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebsiteInfoService websiteInfoService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户登录
     * @param user 用户信息
     * @return 返回用户信息
     */
    @PostMapping("user/login")
    public ResultVO<WebsiteUser> login(@RequestBody WebsiteUser user){
        //1、查询用户是存在，密码是否正确
        return userService.login(user);
    }
    /**
     * 检查token是否正确
     * @return 返回
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
     * 检查用户权限
     * @param username
     * @param token
     * @return
     */
    @GetMapping("user/role")
    public ResultVO role(@RequestParam(value = "username") String username , @RequestParam(value = "token") String token){
        String resultToken = userService.checkToken(username,token);
        if(resultToken !=null){
            WebsiteUser user = userService.getUser(username);
            if(user != null && user.getRole() == 0){
                return ResultVOUtil.success();
            }
        }
        return ResultVOUtil.error(ResultCodeEnum.USER_RIGHTS_ERROR);
    }
    /**
     * 根据一个id，获取用户信息
     * @param id 用户id
     * @return 返回用户dto
     */
    @GetMapping("user/{id}")
    public ResultVO<UserDTO> user(@PathVariable(value = "id")Integer id){
        WebsiteUser websiteUser = userService.getUser(id);
        UserDTO userDTO = WebsiteUserToUserDTOconverter.convert(websiteUser);
        return ResultVOUtil.success(userDTO);
    }

    /**
     * 普通用户注册
     * @param websiteUser
     * @return
     */
    @PostMapping("user/register")
    public ResultVO register(@RequestBody WebsiteUser websiteUser){
        //1、注册用户
        ResultVO resultVO = userService.registerUser(websiteUser);
        if(resultVO.getCode() == 0){
            //2、在redis新建用户信息
            websiteInfoService.addRedisInfo(Integer.valueOf(resultVO.getData().toString()));
        }
        return resultVO;
    }
    /*===========================管理员===================================*/
    /**
     * 获取用户列表
     * @param page 页码
     * @param size 每页大小
     * @param query 是否带查询条件
     * @return
     */
    @GetMapping("admin/users")
    public ResultVO<UserDTO> users(@RequestParam(value = "page",defaultValue = "1")Integer page,@RequestParam(value = "size",defaultValue = "5")Integer size,
                                   @RequestParam(value = "query")String query){
        //使用pageable的子类pagerequest构建一个对象
        PageRequest pageRequest = PageRequest.of(page-1,size);
        Page<UserDTO> userDTOS = userService.getUsers(pageRequest,query);
        return ResultVOUtil.success(userDTOS);
    }

    /**
     * 管理员添加一个新用户，不会在redis创建一些基本信息，只有在用户注册页面注册才会产生基本信息
     * @param websiteUser
     * @return
     */
    @PostMapping("admin/users")
    public ResultVO users(@RequestBody WebsiteUser websiteUser){
        return userService.registerUser(websiteUser);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("admin/user/{id}")
    public ResultVO delete(@PathVariable(value = "id")Integer id){
        //1、查询用户是否存在
        WebsiteUser websiteUser = userService.getUser(id);
        if(websiteUser == null){
            logger.info("[删除用户]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        if(websiteUser.getId() == 1){
            //不允许操作最基础的用户
            return ResultVOUtil.error(ResultCodeEnum.PERMISSION_NO_PHONE_ACCESS);
        }
        // 2、删除用户
        userRepository.delete(websiteUser);
        //3、先获取分类，再根据分类值删除网站信息
        ArrayList<WebsiteCategory> categoryList = (ArrayList<WebsiteCategory>)redisTemplate.opsForList().range("category:" + websiteUser.getId(), 0, -1);
        for(WebsiteCategory category:categoryList){
            StringBuilder key = new StringBuilder().append("webinfo:").append(websiteUser.getId()).append(":").append(category.getCategoryType());
            redisTemplate.delete(key.toString());
        }
        //删除分类
        redisTemplate.delete("category:"+websiteUser.getId());
        //删除主题
        redisTemplate.delete("theme:"+websiteUser.getId());
        //删除登录token
        redisTemplate.delete("token:"+websiteUser.getId());
        return ResultVOUtil.success();
    }

    /**
     * 修改用户状态
     * @param id
     * @param state
     * @return
     */
    @PutMapping("admin/user/{id}/state/{state}")
    public ResultVO userStateChange(@PathVariable(value = "id")Integer id,@PathVariable(value = "state")Boolean state){
        WebsiteUser websiteUser = userService.getUser(id);
        if(websiteUser == null){
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        if(websiteUser.getId() == 1){
            //不允许操作最基础的用户
            return ResultVOUtil.error(ResultCodeEnum.PERMISSION_NO_PHONE_ACCESS);
        }
        websiteUser.setState(state);
        userService.userSave(websiteUser);
        return ResultVOUtil.success();
    }
    /**
     * 修改用户是否为管理员
     * @param id
     * @return
     */
    @PutMapping("admin/user/{id}/role")
    public ResultVO userRoleChange(@PathVariable(value = "id")Integer id){
        WebsiteUser websiteUser = userService.getUser(id);
        if(websiteUser == null){
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        if(websiteUser.getId() == 1){
            //不允许操作最基础的用户
            return ResultVOUtil.error(ResultCodeEnum.PERMISSION_NO_PHONE_ACCESS);
        }
        Integer role = websiteUser.getRole() == 0 ? 1 : 0;
        websiteUser.setRole(role);
        userService.userSave(websiteUser);
        return ResultVOUtil.success();
    }

    /**
     * 修改用户信息
     * @param id
     * @param userDTO
     * @return
     */
    @PutMapping("admin/user/{id}")
    public ResultVO user(@PathVariable(value = "id")Integer id,@RequestBody UserDTO userDTO){
        //1、查询用户是否存在
        WebsiteUser resultUser = userService.getUser(id);
        if(resultUser == null){
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、复制信息过去
        BeanUtils.copyProperties(userDTO,resultUser);
        //3、修改用户信息
        WebsiteUser resultSave = userService.userSave(resultUser);
        if(resultSave != null){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.DATA_IS_WRONG);
    }

}
