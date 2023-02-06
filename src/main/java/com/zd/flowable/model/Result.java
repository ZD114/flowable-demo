package com.zd.flowable.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/1/31
 **/
public class Result {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 返回的状态码
     */
    private Integer code;

    /**
     * 返回的消息
     */
    private String message;

    /**
     * 返回的数据
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 外部不可new Result(). 构造方法私有化。
     */
    private Result() {
    }

    /**
     * 成功返回
     *
     * @return 返回成功结果
     */
    public static Result ok() {
        Result result = new Result();
        result.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return result;
    }

    /**
     * 设置返回结果
     *
     * @param resultCodeEnum 结果枚举
     * @return result 返回给前端的结果
     */
    public static Result error(ResultCodeEnum resultCodeEnum) {
        Result result = new Result();
        result.setSuccess(resultCodeEnum.getSuccess());
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * 设置success的状态
     *
     * @param success 值为true或false
     * @return result 返回给前端的数据
     */
    public Result success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    /**
     * 设置message的内容
     *
     * @param message 消息内容
     * @return result 返回给前端的数据
     */
    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 设置code内容
     *
     * @param code 返回码
     * @return result 返回给前端的数据
     */
    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 单值设置数据
     *
     * @param key   键
     * @param value 值
     * @return result 返回给前端的数据
     */
    public Result data(String key, Object value) {

        this.data.put(key, value);
        return this;
    }

    /**
     * 多值设置数据
     *
     * @param map 集合
     * @return result 返回给前端的数据
     */
    public Result data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
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

    public Map<String, Object> getData() {
        return data;
    }

}
