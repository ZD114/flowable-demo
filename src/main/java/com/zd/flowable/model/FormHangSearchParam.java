package com.zd.flowable.model;

import com.zd.flowable.common.PageParam;

/**
 * @author zhangda
 * @date: 2023/2/13
 **/
public class FormHangSearchParam extends PageParam {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 挂靠人
     */
    private String hangName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHangName() {
        return hangName;
    }

    public void setHangName(String hangName) {
        this.hangName = hangName;
    }
}
