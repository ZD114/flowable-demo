package com.zd.flowable.model;

import com.zd.flowable.common.PageParam;

/**
 * @author zhangda
 * @date: 2023/2/10
 **/
public class FormDataSearchParam extends PageParam {
    /**
     * 表单标签
     */
    private String title;

    /**
     * 是否允许修改（0不允许，1允许）
     */
    private int isEdit;

    /**
     * 创建者
     */
    private String userName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
