package com.anitano.springbootnavigation.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @ClassName: WebsiteInfo
 * @Author: 杨11352
 * @Date: 2020/3/31 12:40
 */
@Entity /**Entity将POJO映射到数据库*/
@Data
@DynamicUpdate
public class WebsiteInfo {
    @Id
    private String websiteId;

    /** 名字*/
    private String websiteName;

    /** url地址*/
    private String websiteUrl;

    /** 排序*/
    private Double websiteSort;

    /** 描述（可为空）*/
    private String websiteDescription;

    /** 网站小图 （可为空）*/
    private String websiteIcon;

    /** 类目编号 */
    private Integer categoryType;

    private Date createTime;
    private Date updateTime;
}
