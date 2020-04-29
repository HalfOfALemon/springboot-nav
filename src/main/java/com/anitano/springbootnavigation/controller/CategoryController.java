package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.service.CategoryService;
import com.anitano.springbootnavigation.service.UserService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: CategoryController
 * @Author: 杨11352
 * @Date: 2020/3/31 20:07
 */
@RestController
@RequestMapping("")
public class CategoryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryService categoryService;

    /**
     *未登录获取分类列表
     * @return
     */
    @GetMapping("/category/list")
    public ResultVO list(){
        List<WebsiteCategory> list = categoryService.getList();
        if(list != null){
            return ResultVOUtil.success(list);
        }
        logger.info("获取分类信息失败");
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }

    /**
     * 根据ID 获取一个分类信息
     * @param id id值
     * @return
     */
    @GetMapping("/category/{id}")
    public ResultVO get(@PathVariable(value = "id")Integer id){
        WebsiteCategory websiteCategory = categoryService.get(id);
        if(websiteCategory ==null){
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
        return ResultVOUtil.success(websiteCategory);
    }
/*======================管理员功能==========================*/
    /**
     * 修改分类
     * @param id
     * @param websiteCategory
     * @return
     */
    @PutMapping("/admin/category/{id}")
    public ResultVO update(@PathVariable(value = "id")Integer id,@RequestBody WebsiteCategory websiteCategory){
        WebsiteCategory resultCategory = categoryService.get(id);
        if(resultCategory == null){
            return  ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
        WebsiteCategory result = categoryService.update(websiteCategory);
        if(result != null){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.UPDATE_FAIL);
    }

}
