package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import com.anitano.springbootnavigation.repository.CategoryRepository;
import com.anitano.springbootnavigation.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: CategoryServiceImpl
 * @Author: 杨11352
 * @Date: 2020/3/31 20:12
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<WebsiteCategory> getList() {
        return categoryRepository.findAll();
    }

    @Override
    public WebsiteCategory get(Integer id) {
        WebsiteCategory websiteCategory = categoryRepository.findById(id).get();
        return websiteCategory;
    }

    @Override
    public WebsiteCategory update(WebsiteCategory websiteCategory) {
        return categoryRepository.save(websiteCategory);
    }
}
