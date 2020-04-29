package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import com.anitano.springbootnavigation.service.CategoryService;
import com.anitano.springbootnavigation.service.WebsiteInfoService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: UserControllerTest
 * @Author: 杨11352
 * @Date: 2020/3/24 23:09
 */
@SpringBootTest
class UserControllerTest {
Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WebsiteInfoService websiteInfoService;
    @Test
    void getUser() {
        RedisTemplate<String,Object> redisTemplate =new RedisTemplate<>();
        Object key1 = redisTemplate.opsForValue().get("key1");
        logger.info(key1.toString());
    }

    @Test
    void redisCategory(){
        //设置分类
        List<WebsiteCategory> websiteCategoryList = categoryService.getList();
        redisTemplate.opsForList().rightPushAll("category:1", websiteCategoryList);
        /*获取分类*/
        List list = redisTemplate.opsForList().range("category:1", 0, -1);
        System.out.println(list);
        /*保存网站信息*/
    }
    @Test
    void redisWebinfo(){
        List<WebsiteCategory> websiteCategoryList = categoryService.getList();
        for(WebsiteCategory category :websiteCategoryList){
            /*获取网站信息*/
            List<WebsiteInfoDTO> websiteInfoDTOList = websiteInfoService.getList(category.getCategoryType());
            redisTemplate.opsForList().rightPushAll("webinfo:1:"+category.getCategoryType(), websiteInfoDTOList);
            System.out.println(redisTemplate.opsForList().range("webinfo:1:"+category.getCategoryType(),0,-1));
        }
    }
}