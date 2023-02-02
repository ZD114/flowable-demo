package com.zd.flowable.model;

import java.io.Serializable;

/**
 * @author zhangda
 * @date: 2023/2/2
 **/
public class DeploymentProperty implements Serializable {
    private static final long serialVersionUID = 1L;

    private String modelId; //模型ID
    private String tenantId; //租户ID
    private String name; //部署名称
    private String xmlPath; //xml路径
    private String pngPath; //png路径
    private String zipPath; //zip路径

    private String deploymentId; //部署编号
    private String formName; //表单名称

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getPngPath() {
        return pngPath;
    }

    public void setPngPath(String pngPath) {
        this.pngPath = pngPath;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
