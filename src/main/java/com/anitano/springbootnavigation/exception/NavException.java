package com.anitano.springbootnavigation.exception;


import com.anitano.springbootnavigation.enums.ResultCodeEnum;

/**
 * @ClassName: SellException
 * @Author: 杨11352
 * @Date: 2019/10/31 20:39
 */
public class NavException extends RuntimeException {
    private Integer code;

    public NavException(ResultCodeEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public NavException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
