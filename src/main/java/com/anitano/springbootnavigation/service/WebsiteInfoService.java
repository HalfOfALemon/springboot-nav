package com.anitano.springbootnavigation.service;

import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @ClassName: WebsiteInfoService
 * @Author: 杨11352
 * @Date: 2020/3/31 14:29
 */
public interface WebsiteInfoService {
    /**根据获取列表*/
    Page<WebsiteInfoDTO> getList(Pageable pageable, String query);
    /**根据分类id获取列表*/
    List<WebsiteInfoDTO> getList(Integer categoryType);
    /**根据id获取信息*/
    WebsiteInfoDTO get(String websiteId);
    /**添加网站*/
    WebsiteInfo add(WebsiteInfo websiteInfo);
    /**根据id删除网站信息*/
    WebsiteInfo delete(String id);
    /**更新网站信息*/
    WebsiteInfo update(WebsiteInfoDTO websiteInfoDTO);
    /**将网站信息保存到redis*/
    ResultVO addRedisInfo(Integer userId);
}
