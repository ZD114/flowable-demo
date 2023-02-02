package com.zd.flowable.model;

import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/2
 **/
public class RuntimeProperty {
    private Map<String,Object> mapVariables; //map参数名
    private String processDefinitionId; //流程定义实例Id
    private String businessKey; //业务key
    private String userName; //用户名

    public Map<String, Object> getMapVariables() {
        return mapVariables;
    }

    public void setMapVariables(Map<String, Object> mapVariables) {
        this.mapVariables = mapVariables;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}