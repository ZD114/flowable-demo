package com.zd.flowable.model;

import com.zd.flowable.common.PageParam;

/**
 * 任务列表查询参数
 *
 * @author zhangda
 * @date: 2023/4/24
 **/
public class TaskSearchParam extends PageParam {
    private String userName;//当前办理人

    private String processInstanceId;//过程实例编号

    private String lastStart;//开始时间

    private String lastEnd;//结束时间

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getLastStart() {
        return lastStart;
    }

    public void setLastStart(String lastStart) {
        this.lastStart = lastStart;
    }

    public String getLastEnd() {
        return lastEnd;
    }

    public void setLastEnd(String lastEnd) {
        this.lastEnd = lastEnd;
    }
}
