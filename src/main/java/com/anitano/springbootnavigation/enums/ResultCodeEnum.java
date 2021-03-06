package com.anitano.springbootnavigation.enums;

import lombok.Getter;

/**
 * @ClassName: ResultEnum
 * @Author: 杨11352
 * @Date: 2019/10/31 20:47
 */
public enum ResultCodeEnum {
    /* 成功状态码 */
    SUCCESS(0, "成功"),

    /* 令牌失效 */
    TOKEN_INVALID(401,"令牌失效"),

    SERVER_ERROR(400, "服务器错误"),

    /*参数错误 10001-19999 */

    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    USER_EMAIL_HAS_EXISTED(20005, "邮箱已存在"),
    Cert_HAS_EXISTED(20006, "认证已存在"),
    USER_RIGHTS_ERROR(20007, "用户权限错误"),
    /*管理员*/
    ADMIN_NOT_LOGGED_IN(21001, "管理员未登录"),

    /* 业务错误：30001-39999 */
    CREATE_FAIL(30001, "创建失败"),
    UPDATE_FAIL(30001, "创建失败"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /* 数据错误：50001-599999 */
    RESULE_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),
    INVALID_FILE_TYPE(50004,"数据类型错误"),
    DELETE_OLD_DATA_FAILED(50005,"删除旧数据失败"),
    UPDATE_DATA_FAILED(50006,"更新数据失败"),
    DELETE_DATA_FAILED(50007,"不允许删除该数据"),


    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "只有标签 Owner ,才具备删除权限"),
    PERMISSION_NO_PHONE_ACCESS(70002,"此认证标签已有认证，不可以进行操作");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCodeEnum item : ResultCodeEnum.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCodeEnum item : ResultCodeEnum.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
