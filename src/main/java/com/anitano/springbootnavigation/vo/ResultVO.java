package com.anitano.springbootnavigation.vo;

import lombok.Data;

/**
 * @Author: 杨11352
 * @Date: 2019/10/27 11:25
 */
@Data
public class ResultVO<T> {
    /**
     * 错误提示码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 具体内容
     */
    private T data;
}
