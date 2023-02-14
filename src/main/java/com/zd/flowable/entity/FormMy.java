package com.zd.flowable.entity;

import java.time.LocalDateTime;

/**
 * @author zhangda
 * @date: 2023/2/14
 **/
public class FormMy {

    /**
     * 我的表单编号
     */
    private String formMyId;

    /**
     * 表单名称
     */
    private String title;

    /**
     * 表单内容
     */
    private String htmlContent;

    /**
     * 表单类型
     */
    private String type;

    /**
     * 创建者
     */
    private String userName;

    /**
     * 是否私有
     */
    private int isPrivate;

    /**
     * 是否上传图片（0否，1是）
     */
    private int isImage;

    /**
     * 是否上传附件（0否，1是）
     */
    private int isFile;

    /**
     * 数据私有
     */
    private String dataPrivate;

    /**
     * 表单模板编号
     */
    private String formTemplatesId;

    private int isLc;

    private int isFwb;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public String getFormMyId() {
        return formMyId;
    }

    public void setFormMyId(String formMyId) {
        this.formMyId = formMyId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    public int getIsImage() {
        return isImage;
    }

    public void setIsImage(int isImage) {
        this.isImage = isImage;
    }

    public int getIsFile() {
        return isFile;
    }

    public void setIsFile(int isFile) {
        this.isFile = isFile;
    }

    public String getDataPrivate() {
        return dataPrivate;
    }

    public void setDataPrivate(String dataPrivate) {
        this.dataPrivate = dataPrivate;
    }

    public String getFormTemplatesId() {
        return formTemplatesId;
    }

    public void setFormTemplatesId(String formTemplatesId) {
        this.formTemplatesId = formTemplatesId;
    }

    public int getIsLc() {
        return isLc;
    }

    public void setIsLc(int isLc) {
        this.isLc = isLc;
    }

    public int getIsFwb() {
        return isFwb;
    }

    public void setIsFwb(int isFwb) {
        this.isFwb = isFwb;
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
