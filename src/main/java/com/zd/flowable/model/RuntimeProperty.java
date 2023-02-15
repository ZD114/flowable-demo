package com.zd.flowable.model;

import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/2
 **/
public class RuntimeProperty {
    /**
     * map参数名
     */
    private Map<String, Object> mapVariables;

    /**
     * 流程定义实例Id
     */
    private String processDefinitionId;

    /**
     * 业务key
     */
    private String businessKey;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 部署编号
     */
    private String deploymentId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 流程实例编号
     */
    private String processInstanceId;

    /**
     * 当前节点
     */
    private String nodeId;

    /**
     * 目的节点
     */
    private String toNodeId;

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

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(String toNodeId) {
        this.toNodeId = toNodeId;
    }
}
