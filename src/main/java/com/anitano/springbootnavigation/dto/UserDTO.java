package com.anitano.springbootnavigation.dto;

import lombok.Data;

import javax.persistence.Id;

/**
 * @ClassName: User
 * @Author: 杨11352
 * @Date: 2020/3/27 22:26
 */
@Data
public class UserDTO {
    @Id
    private Integer id;
    /**用户名称*/
    private String username;
    /**邮箱*/
    private String email;
    /**手机*/
    private String phone;
    /**问题*/
    private String question;
    /**答案*/
    private String answer;
    /**用户权限*/
    private Integer role;
    /**用户状态，1正常，0禁用*/
    private Boolean state;
    public UserDTO(){}
}
