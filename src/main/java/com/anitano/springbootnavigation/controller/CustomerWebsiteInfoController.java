package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: CustomerWebsiteInfoController
 * @Author: 杨11352
 * @Date: 2020/4/25 20:51
 */
@RequestMapping("customer")
@RestController
public class CustomerWebsiteInfoController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    /**
     * 根据分类id获取储存在redis里边的网站信息
     * @param categoryType
     * @param username
     * @return
     */
    @GetMapping("/navs/category-type/{categoryType}")
    public ResultVO customerListByCategoryType(@PathVariable(value = "categoryType")Integer categoryType,@RequestParam("username")String username) {
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if (user != null) {
            StringBuilder key = new StringBuilder();
            key.append("webinfo:").append(user.getId()).append(":").append(categoryType);
            List webInfoList = redisTemplate.opsForList().range(key.toString(), 0, -1);
            return ResultVOUtil.success(webInfoList);
        }
        logger.info("[自定义用户获取网站信息]用户未登录");
        return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
    }

    /**
     * 根据分类和索引获取一个网址信息
     * @param categoryType
     * @param index
     * @param username
     * @return
     */
    @GetMapping("/navs/{categoryType}/{index}/{username}")
    public ResultVO get(@PathVariable(value = "categoryType")Integer categoryType,@PathVariable(value = "index")Integer index,@PathVariable("username")String username){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if (user != null) {
            String key = new StringBuilder().append("webinfo:").append(user.getId()).append(":").append(categoryType).toString();
            WebsiteInfoDTO websiteInfoDTO = (WebsiteInfoDTO)redisTemplate.opsForList().index(key, index);
            if(websiteInfoDTO !=null){
                return ResultVOUtil.success(websiteInfoDTO);
            }else {
                logger.info("[自定义用户获取网站信息]网址信息不存在");
                return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
            }
        }
        logger.info("[自定义用户获取网站信息]用户未登录");
        return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
    }

    /**
     * 编辑一个网址信息
     * @param categoryType
     * @param index
     * @param username
     * @param websiteInfoDTO
     * @return
     */
    @PutMapping("/navs/{categoryType}/{index}/{username}")
    public ResultVO update(@PathVariable(value = "categoryType")Integer categoryType,
                           @PathVariable(value = "index")Integer index,
                           @PathVariable("username")String username,
                           @RequestBody WebsiteInfoDTO websiteInfoDTO){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if (user != null) {
            //2、先获取数据
            String key = new StringBuilder().append("webinfo:").append(user.getId()).append(":").append(categoryType).toString();
            WebsiteInfoDTO resultWebsiteInfoDTO = (WebsiteInfoDTO)redisTemplate.opsForList().index(key, index);
            if(websiteInfoDTO !=null){
                //3、更新数据
                BeanUtils.copyProperties(websiteInfoDTO,resultWebsiteInfoDTO);
                try {
                    redisTemplate.opsForList().set(key,index,resultWebsiteInfoDTO);
                } catch (Exception e) {
                    logger.info("[更新分类]普通用户更新分类失败");
                    return ResultVOUtil.error(ResultCodeEnum.UPDATE_DATA_FAILED);
                }
                return ResultVOUtil.success();
            }else {
                logger.info("[自定义用户获取网站信息]网址信息不存在");
                return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
            }
        }
        logger.info("[自定义用户获取网站信息]用户未登录");
        return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
    }

    @DeleteMapping("/navs/{categoryType}/{index}/{username}")
    public ResultVO delete(@PathVariable(value = "categoryType")Integer categoryType,@PathVariable(value = "index")Integer index,@PathVariable("username")String username){
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if (user != null) {
            //2、获取要删除的对象
            String key = new StringBuilder().append("webinfo:").append(user.getId()).append(":").append(categoryType).toString();
            WebsiteInfoDTO websiteInfoDTO = (WebsiteInfoDTO)redisTemplate.opsForList().index(key, index);
            if(websiteInfoDTO !=null){
                //3、根据获取到的对象，删除一个网址
                redisTemplate.opsForList().remove(key,-1,websiteInfoDTO);
                return ResultVOUtil.success();
            }else {
                logger.info("[自定义用户获取网站信息]网址信息不存在");
                return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
            }
        }
        logger.info("[自定义用户获取网站信息]用户未登录");
        return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
    }

    /**
     * 添加一个网址
     * @param username
     * @param websiteInfoDTO
     * @return
     */
    @PostMapping("/navs/{username}")
    public ResultVO add(@PathVariable("username")String username, @RequestBody WebsiteInfoDTO websiteInfoDTO){
        //判断分类是否为空
        if(StringUtils.isEmpty(websiteInfoDTO)){
            logger.info("[添加网站信息]网站信息为空");
            ResultVOUtil.error(ResultCodeEnum.PARAM_IS_BLANK);
        }
        //1、根据用户名获取用户id
        WebsiteUser user = userService.getUser(username);
        if(user == null){
            logger.info("[登录获取分类失败]用户不存在");
            return ResultVOUtil.error(ResultCodeEnum.USER_NOT_EXIST);
        }
        //2、添加网站信息
        //设置分类值
        String key = new StringBuilder().append("webinfo:").append(user.getId()).append(":").append(websiteInfoDTO.getCategoryType()).toString();
        websiteInfoDTO.setWebsiteId(key);
        //储存到redis
        redisTemplate.opsForList().rightPush(key,websiteInfoDTO);
        return ResultVOUtil.success();
    }
}
