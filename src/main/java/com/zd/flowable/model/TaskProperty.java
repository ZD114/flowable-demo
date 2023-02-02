package com.zd.flowable.model;

import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/2
 **/
public class TaskProperty {
    private Map<String, Object> mapVariables; //map参数名
    private String taskId;//任务编号
    private String processInstanceId;//执行编号
    private String userId;//候选用户编号
    private String formDefinitionId; //表单定义编号

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Map<String, Object> getMapVariables() {
        return mapVariables;
    }

    public void setMapVariables(Map<String, Object> mapVariables) {
        this.mapVariables = mapVariables;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }
}
