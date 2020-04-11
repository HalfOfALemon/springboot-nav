package com.anitano.springbootnavigation.dto;

import lombok.Data;

/**
 * @ClassName: WebsiteInfoDTO
 * @Author: 杨11352
 * @Date: 2020/3/31 14:36
 */
@Data
public class WebsiteInfoDTO {
    private String websiteId;
    /** 名字*/
    private String websiteName;

    /** url地址*/
    private String websiteUrl;

    /** 排序*/
    private Double websiteSort;

    /** 描述*/
    private String websiteDescription;
    /** 网站小图 */
    private String websiteIcon;
    /** 类目编号 */
    private Integer categoryType;
}
