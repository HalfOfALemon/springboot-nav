package com.anitano.springbootnavigation.repository;

import com.anitano.springbootnavigation.dataobject.WebsiteCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName: CategoryRepository
 * @Author: 杨11352
 * @Date: 2020/3/31 20:10
 */
public interface CategoryRepository extends JpaRepository<WebsiteCategory,Integer> {
}
