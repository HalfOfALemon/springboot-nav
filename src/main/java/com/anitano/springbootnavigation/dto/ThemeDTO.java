package com.anitano.springbootnavigation.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ThemeDTO
 * @Author: 杨11352
 * @Date: 2020/4/26 19:42
 */
@Data
public class ThemeDTO implements Serializable {
    /*搜索框*/
    private String searchInput;
    /*分类*/
    private String category;
    /*网址信息*/
    private String webinfo;
    /*背景css*/
    private String background;

    /*默认构造器用于序列化*/
    public ThemeDTO(){

    }
    public ThemeDTO(String searchInput,String category,String webinfo,String background){
        this.searchInput = searchInput;
        this.category = category;
        this.webinfo = webinfo;
    }
}
