package com.zd.flowable.entity;

import java.time.LocalDateTime;

/**
 * @author zhangda
 * @date: 2023/4/24
 **/
public class TaskDetail {
    private String id_;

    private Integer rev_;

    private String executionId_;

    private String procInstId_;

    private String procDefId_;

    private String name_;

    private String taskDefKey_;

    private String assignee_;

    private LocalDateTime createTime_;

    private String tenantId_;

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public Integer getRev_() {
        return rev_;
    }

    public void setRev_(Integer rev_) {
        this.rev_ = rev_;
    }

    public String getExecutionId_() {
        return executionId_;
    }

    public void setExecutionId_(String executionId_) {
        this.executionId_ = executionId_;
    }

    public String getProcInstId_() {
        return procInstId_;
    }

    public void setProcInstId_(String procInstId_) {
        this.procInstId_ = procInstId_;
    }

    public String getProcDefId_() {
        return procDefId_;
    }

    public void setProcDefId_(String procDefId_) {
        this.procDefId_ = procDefId_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getTaskDefKey_() {
        return taskDefKey_;
    }

    public void setTaskDefKey_(String taskDefKey_) {
        this.taskDefKey_ = taskDefKey_;
    }

    public String getAssignee_() {
        return assignee_;
    }

    public void setAssignee_(String assignee_) {
        this.assignee_ = assignee_;
    }

    public LocalDateTime getCreateTime_() {
        return createTime_;
    }

    public void setCreateTime_(LocalDateTime createTime_) {
        this.createTime_ = createTime_;
    }

    public String getTenantId_() {
        return tenantId_;
    }

    public void setTenantId_(String tenantId_) {
        this.tenantId_ = tenantId_;
    }
}
