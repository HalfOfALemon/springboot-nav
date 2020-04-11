package com.anitano.springbootnavigation.service;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;

import java.util.List;

/**
 * @ClassName: CategoryService
 * @Author: 杨11352
 * @Date: 2020/3/31 20:11
 */
public interface CategoryService {
    /**获得分类的列表*/
    List<WebsiteCategory> getList();
    WebsiteCategory get(Integer id);
    WebsiteCategory update(WebsiteCategory websiteCategory);
}
