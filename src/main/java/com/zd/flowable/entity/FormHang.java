package com.zd.flowable.entity;

import java.time.LocalDateTime;

/**
 * @author zhangda
 * @date: 2023/2/13
 **/
public class FormHang {

    /**
     * 挂靠编号
     */
    private String formHangId;

    /**
     * 流程实例编号
     */
    private String processInstanceId;

    /**
     * 表单数据编号
     */
    private String formDataId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 挂靠人
     */
    private String hangName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getFormHangId() {
        return formHangId;
    }

    public void setFormHangId(String formHangId) {
        this.formHangId = formHangId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getHangName() {
        return hangName;
    }

    public void setHangName(String hangName) {
        this.hangName = hangName;
    }
}
