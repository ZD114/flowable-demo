package com.zd.flowable.model;

import java.time.LocalDateTime;

/**
 * 模板参数类
 *
 * @author zhangda
 * @date: 2023/2/6
 **/
public class FormTemplatesProperty {
    /**
     * 模板编号
     */
    private String formTemplatesId;

    /**
     * 模板名称
     */
    private String title;

    /**
     * 模板内容
     */
    private String htmlContent;

    /**
     * 模板类型
     */
    private String type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public String getFormTemplatesId() {
        return formTemplatesId;
    }

    public void setFormTemplatesId(String formTemplatesId) {
        this.formTemplatesId = formTemplatesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
