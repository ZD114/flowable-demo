package com.zd.flowable.model;

import com.zd.flowable.common.PageParam;

/**
 * @author zhangda
 * @date: 2023/2/14
 **/
public class FormMySearchParam extends PageParam {
    /**
     * 表单名称
     */
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
