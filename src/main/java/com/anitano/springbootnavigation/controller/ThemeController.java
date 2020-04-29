package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.ThemeDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: ThemeController 获取用户主题
 * @Author: 杨11352
 * @Date: 2020/4/26 19:14
 */
@RestController
public class ThemeController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    /**
     * 获取一个管理员设置的默认主题信息
     * @return
     */
    @GetMapping("/theme")
    public ResultVO setAdmin(){
        //1、获取默认的主题
        Boolean isTheme = redisTemplate.hasKey("theme:admin");
        if(isTheme){
            ThemeDTO themeDTO=(ThemeDTO) redisTemplate.opsForValue().get("theme:admin");
            return ResultVOUtil.success(themeDTO);
        }else {
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
    }
    /**
     * 获取一个用户主题信息
     * @param username
     * @return
     */
    @GetMapping("customer/theme/{username}")
    public ResultVO set(@PathVariable("username")String username){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、获取用户自定义主题
        Boolean isTheme = redisTemplate.hasKey("theme:" + user.getId());
        if(isTheme){
            ThemeDTO themeDTO=(ThemeDTO) redisTemplate.opsForValue().get("theme:" + user.getId());
            return ResultVOUtil.success(themeDTO);
        }else {
            //主题不存在，创建一个新主题
            ThemeDTO themeDTO = new ThemeDTO("SearchInput02","Category02","Webinfo01","background-image: linear-gradient(to top, rgb(223, 233, 243) 0%, white 100%);");
            redisTemplate.opsForValue().set("theme:"+user.getId(),themeDTO);
            //重新获取
            return ResultVOUtil.success(redisTemplate.opsForValue().get("theme:" + user.getId()));
        }
    }

    /**
     * 添加一个用户主题信息
     * @param username
     * @param themeDTO
     * @return
     */
    @PostMapping("customer/theme/{username}")
    public ResultVO add(@PathVariable("username")String username, @RequestBody ThemeDTO themeDTO){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、添加用户自定义主题
        redisTemplate.opsForValue().set("theme:" + user.getId(),themeDTO);
        return ResultVOUtil.success();
    }

    /**
     * 修改用户主题
     * @param username
     * @param themeDTO
     * @return
     */
    @PutMapping("customer/theme/{username}")
    public ResultVO update(@PathVariable("username")String username, @RequestBody ThemeDTO themeDTO){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、获取用户自定义主题
        ThemeDTO resultThemeDTO = (ThemeDTO)redisTemplate.opsForValue().get("theme:" + user.getId());
        BeanUtils.copyProperties(themeDTO,resultThemeDTO);
        //3、更新数据
        redisTemplate.opsForValue().set("theme:" + user.getId(),themeDTO);
        return ResultVOUtil.success();
    }
/*========================管理员================================*/
    /**
     * 修改管理员主题
     * @param themeDTO
     * @return
     */
    @PutMapping("admin/theme")
    public ResultVO updateAdmin(@RequestBody ThemeDTO themeDTO){
        //1、获取用户自定义主题
        ThemeDTO resultThemeDTO = (ThemeDTO)redisTemplate.opsForValue().get("theme:admin");
        BeanUtils.copyProperties(themeDTO,resultThemeDTO);
        //2、更新数据
        redisTemplate.opsForValue().set("theme:admin",themeDTO);
        return ResultVOUtil.success();
    }

}
