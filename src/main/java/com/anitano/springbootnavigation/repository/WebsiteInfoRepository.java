package com.anitano.springbootnavigation.repository;

import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName: WebsiteInfoRepository
 * @Author: 杨11352
 * @Date: 2020/3/31 14:27
 */
public interface WebsiteInfoRepository extends JpaRepository<WebsiteInfo,String> {
    /**根据id查询*/
    WebsiteInfo findByWebsiteId(String id);
    /**查询这个分类下的内容*/
    List<WebsiteInfo> findByCategoryType(Integer categoryId);
    /**模糊查询*/
    Page<WebsiteInfo> findByWebsiteNameLike(Pageable pageable,String query);
    /**获取某个分类下排序的最后一个*/
    WebsiteInfo findFirstByCategoryTypeOrderByWebsiteIdDesc(Integer categoryType);
    /**获取整个数据库排序的最后一个*/
    WebsiteInfo findFirstByOrderByWebsiteIdDesc();
}
