package com.zd.flowable.model;

/**
 * @author zhangda
 * @date: 2023/2/2
 **/
public class DeploymentProperty {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 部署名称
     */
    private String name;

    /**
     * xml路径
     */
    private String xmlPath;

    /**
     * png路径
     */
    private String pngPath;

    /**
     * zip路径
     */
    private String zipPath;

    /**
     * 部署编号
     */
    private String deploymentId;

    /**
     * 表单名称
     */
    private String formName;

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
