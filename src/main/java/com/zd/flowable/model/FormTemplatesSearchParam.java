package com.zd.flowable.model;

import com.zd.flowable.common.PageParam;

/**
 * @author zhangda
 * @date: 2023/2/9
 **/
public class FormTemplatesSearchParam extends PageParam {

    /**
     * 模板名称
     */
    private String title;

    /**
     * 模板类型
     */
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
