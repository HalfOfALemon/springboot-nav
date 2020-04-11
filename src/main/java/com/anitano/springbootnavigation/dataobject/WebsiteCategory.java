package com.anitano.springbootnavigation.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName: WebsiteCategory
 * @Author: 杨11352
 * @Date: 2020/3/31 20:03
 */
@Entity //Entity将POJO映射到数据库
@Data
public class WebsiteCategory {
    @Id
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;
}
