package com.zd.flowable.model;

/**
 * @author zhangda
 * @date: 2023/1/31
 **/
public enum ResultCodeEnum {
    SUCCESS(true, 200, "成功"),

    NULL_ARGUMENT_ERROR(false,400,"前端传来的参数为空"),

    DATA_SEARCH_ERROR(false,400,"前端系统参数查找出现错误"),

    ILLEGAL_STATUS_ERROR(false,500,"当前需求单状态出现错误"),

    DATA_PROCESS_ERROR(false,500,"后台数据处理出现错误"),

    ERROR(false,500,"后台其他异常"),

    UNKNOWN_REASON(false, 20001, "未知错误"),

    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),

    JSON_PARSE_ERROR(false, 21002, "json解析异常"),

    PARAM_ERROR(false, 21003, "参数不正确"),

    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),

    FILE_DELETE_ERROR(false, 21005, "文件刪除错误"),

    EXCEL_DATA_IMPORT_ERROR(false, 21006, "Excel数据导入错误"),

    URL_ENCODE_ERROR(false, 23001, "URL编码失败"),

    ILLEGAL_CALLBACK_REQUEST_ERROR(false, 23002, "非法回调请求"),

    FETCH_ACCESSTOKEN_FAILD(false, 23003, "获取accessToken失败"),

    FETCH_USERINFO_ERROR(false, 23004, "获取用户信息失败"),

    LOGIN_ERROR(false, 23005, "登录失败"),

    GATEWAY_ERROR(false, 26000, "服务不能访问"),

    CODE_ERROR(false, 28000, "验证码错误"),

    LOGIN_PHONE_ERROR(false, 28009, "手机号码不正确"),

    LOGIN_MOBILE_ERROR(false, 28001, "账号不正确"),

    LOGIN_PASSWORD_ERROR(false, 28008, "密码不正确"),

    LOGIN_DISABLED_ERROR(false, 28002, "该用户已被禁用"),

    REGISTER_MOBLE_ERROR(false, 28003, "手机号已被注册"),

    LOGIN_AUTH(false, 28004, "需要登录"),

    LOGIN_ACL(false, 28005, "没有权限"),

    SMS_SEND_ERROR(false, 28006, "短信发送失败"),

    SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL(false, 28007, "短信发送过于频繁");

    private Boolean success;

    private Integer code;

    private String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
