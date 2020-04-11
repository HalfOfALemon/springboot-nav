package com.anitano.springbootnavigation.repository;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: UserRepository
 * @Author: 杨11352
 * @Date: 2020/3/24 16:47
 */
public interface UserRepository extends JpaRepository<WebsiteUser,Integer> {
    WebsiteUser findByUsername(String username);
    /**用户名模糊查询*/
    Page<WebsiteUser> findByUsernameLike(Pageable pageable,String query);
}
