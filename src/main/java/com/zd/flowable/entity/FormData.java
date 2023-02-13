package com.zd.flowable.entity;

import java.time.LocalDateTime;

/**
 * @author zhangda
 * @date: 2023/2/10
 **/
public class FormData {
    /**
     * 表单数据编号
     */
    private String formDataId;

    /**
     * 表单标签
     */
    private String title;

    /**
     * 表单内容
     */
    private String htmlContent;

    /**
     * 是否允许修改（0不允许，1允许）
     */
    private int isEdit;

    /**
     * 创建者
     */
    private String userName;

    /**
     * 修改者
     */
    private String editName;

    /**
     * 图片
     */
    private String fileImage;

    /**
     * 附件
     */
    private String filePath;

    /**
     * 是否上传图片（0否，1是）
     */
    private int isImage;

    /**
     * 是否上传附件（0否，1是）
     */
    private int isFile;

    /**
     * 我的表单编号
     */
    private String myFormId;

    /**
     * 数据私有
     */
    private String dataPrivate;

    private int isLc;

    private int isFwb;

    private String fwbContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
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

    public int getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEditName() {
        return editName;
    }

    public void setEditName(String editName) {
        this.editName = editName;
    }

    public String getFileImage() {
        return fileImage;
    }

    public void setFileImage(String fileImage) {
        this.fileImage = fileImage;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getMyFormId() {
        return myFormId;
    }

    public void setMyFormId(String myFormId) {
        this.myFormId = myFormId;
    }

    public String getDataPrivate() {
        return dataPrivate;
    }

    public void setDataPrivate(String dataPrivate) {
        this.dataPrivate = dataPrivate;
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

    public String getFwbContent() {
        return fwbContent;
    }

    public void setFwbContent(String fwbContent) {
        this.fwbContent = fwbContent;
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
