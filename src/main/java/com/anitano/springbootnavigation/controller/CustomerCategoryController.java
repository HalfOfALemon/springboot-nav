package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.exception.NavException;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: CustomerCategoryController
 * @Author: 杨11352
 * @Date: 2020/4/24 15:28
 */
@RestController
@RequestMapping("customer")
public class CustomerCategoryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    /**
     * 登录用户获取分类
     * @return
     */
    @GetMapping("category/list/{username}")
    public ResultVO list(@PathVariable("username")String username){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、获取用户自定义信息
        List categoryList = redisTemplate.opsForList().range("category:"+user.getId(), 0, -1);
        return ResultVOUtil.success(categoryList);
    }

    /**
     * 根据分类索引获取分类信息
     * @param index
     * @param username
     * @return
     */
    @GetMapping("category/{index}/{username}")
    public ResultVO get(@PathVariable("index")Integer index,@PathVariable("username")String username){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、根据索引和用户id获取信息
        Object category = redisTemplate.opsForList().index("category:" + user.getId(), index);
        return ResultVOUtil.success(category);
    }

    /**
     * 添加一个分类
     * @param username
     * @param websiteCategory
     * @return
     */
    @PostMapping("category/{username}")
    public ResultVO add(@PathVariable("username")String username,@RequestBody WebsiteCategory websiteCategory){
        //判断分类是否为空
        if(StringUtils.isEmpty(websiteCategory.getCategoryName())){
            logger.info("[添加分类]分类名为空");
            ResultVOUtil.error(ResultCodeEnum.PARAM_IS_BLANK);
        }
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、添加用户信息
        //设置分类值
        WebsiteCategory lastCategory = (WebsiteCategory)redisTemplate.opsForList().index("category:" + user.getId(), -1);
        if(lastCategory != null){
            websiteCategory.setCategoryId(lastCategory.getCategoryId()+1);
            websiteCategory.setCategoryType(lastCategory.getCategoryType()+1);
        }else {
            websiteCategory.setCategoryId(1);
            websiteCategory.setCategoryType(1);
        }
        redisTemplate.opsForList().rightPush("category:" + user.getId(),websiteCategory);
        return ResultVOUtil.success();
    }

    /**
     * 删除分类信息
     * @param username
     * @param index
     * @return
     */
    @DeleteMapping("category/{username}/{index}")
    public ResultVO delete(@PathVariable("username")String username,@PathVariable("index")Integer index){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、删除分类信息
        //先获取索引的值,再根据这个内容，删除list里边相同的内容
        WebsiteCategory category = (WebsiteCategory)redisTemplate.opsForList().index("category:" + user.getId(), index);
        Long remove = redisTemplate.opsForList().remove("category:" + user.getId(), -1, category);
        //3、删除分类值下的网站信息
        if(remove<1){
            logger.info("[删除分类]数据不存在");
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }

        String key = new StringBuilder().append("webinfo:").append(user.getId()).append(":").append(category.getCategoryType()).toString();
        logger.info("=============={}",key);
        redisTemplate.delete(key);
        return ResultVOUtil.success();
    }

    /**
     * 更新分类信息
     * @param username
     * @param websiteCategory
     * @return
     */
    @PutMapping("category/{username}")
    public ResultVO update(@PathVariable("username")String username,@RequestBody WebsiteCategory websiteCategory){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、更新数据
        String key = new StringBuilder().append("category:").append(user.getId()).toString();
        //先获取数据库里边的值
        WebsiteCategory resultCategory = (WebsiteCategory)redisTemplate.opsForList().index(key, websiteCategory.getCategoryId());
        if(resultCategory == null){
            logger.info("[更新分类]普通用户更新分类失败,数据不存在");
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
        resultCategory.setCategoryName(websiteCategory.getCategoryName());
        //再更新数据
        try {
            redisTemplate.opsForList().set(key,websiteCategory.getCategoryId(),resultCategory);
        } catch (Exception e) {
            logger.info("[更新分类]普通用户更新分类失败");
            return ResultVOUtil.error(ResultCodeEnum.UPDATE_DATA_FAILED);
        }
        return ResultVOUtil.success();
    }

}
