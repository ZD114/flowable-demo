package com.zd.flowable.entity;

import com.alibaba.excel.annotation.ExcelProperty;

import java.time.LocalDateTime;

/**
 * 模板实体类
 *
 * @author zhangda
 * @date: 2023/2/6
 **/
public class FormTemplates {
    /**
     * 模板编号
     */
    @ExcelProperty(value = "模板编号", index = 0)
    private String id;

    /**
     * 模板名称
     */
    @ExcelProperty(value = "模板名称", index = 1)
    private String title;

    /**
     * 模板内容
     */
    @ExcelProperty(value = "模板内容", index = 2)
    private String htmlContent;

    /**
     * 模板类型
     */
    @ExcelProperty(value = "模板类型", index = 3)
    private String type;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 4)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", index = 5)
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
