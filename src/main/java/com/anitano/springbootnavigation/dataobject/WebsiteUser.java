package com.anitano.springbootnavigation.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: websiteUser
 * @Author: 杨11352
 * @Date: 2020/3/24 16:40
 */
/**Entity将POJO映射到数据库*/
@Entity
@Data
@DynamicUpdate
public class WebsiteUser{
    /**用户ID*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**用户名称*/
    private String username;
    /**用户密码*/
    private String password;
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
    /**token*/
    private String token;
    /**用户状态，1正常，0禁用*/
    private Boolean state;
    private Date updateTime;
    private Date createTime;

    public WebsiteUser(Integer id,String username,String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public WebsiteUser(){

    }
}
