package com.anitano.springbootnavigation.repository;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: UserRepository
 * @Author: 杨11352
 * @Date: 2020/3/24 16:47
 */
public interface UserRepository extends JpaRepository<WebsiteUser,Integer> {
    WebsiteUser findByUsername(String username);
}
