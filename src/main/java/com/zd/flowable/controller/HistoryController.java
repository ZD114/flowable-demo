package com.zd.flowable.controller;

import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.ui.modeler.domain.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author zhangda
 * @date: 2023/2/15
 **/
public class HistoryController {

    @Autowired
    private HistoryService historyService;        // 历史管理(执行完的数据的管理)
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService; //管理流程定义  与流程定义和部署对象相关的Service
    @Autowired
    private ProcessEngine processEngine;        // 流程引擎对象

    /**
     * 流程是否完成功能
     *
     * @param processInstanceId 流程实例编号
     * @return
     */
    @GetMapping("/{processInstanceId}")
    public boolean isFinished(@PathVariable String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() > 0;
    }

    /**
     * 根据流程执行ID获取流程图资源json数据
     *
     * @param processInstanceId 流程实例编号
     * @param userName          用户
     * @return
     */
    public InputStream getModelJSON2(String processInstanceId, String userName) {
        var processDefinitionId = "";

        if (isFinished(processInstanceId)) {            // 如果流程已经结束，则得到结束节点
            var pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        } else {                                    // 如果流程没有结束，则取当前活动节点
            /* 根据流程实例ID获得当前处于活动状态的ActivityId合集 */
            var pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        var highLightedFlow = new StringBuffer();
        /* 获得活动的节点对象 */
        var highLightedActivityList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        var highLightedActivities = new ArrayList<String>();                // 节点对象ID

        for (HistoricActivityInstance tempActivity : highLightedActivityList) {
            var activityId = tempActivity.getActivityId();
            highLightedActivities.add(activityId);
            highLightedFlow.append(activityId).append(",");
        }

        var bpmnModel = repositoryService.getBpmnModel(processDefinitionId); // 获取流程图
        var modelData = new Model();
        var processDefinition = repositoryService.getProcessDefinition(processDefinitionId);

        modelData.setKey(processDefinition.getKey());            // 唯一标识
        modelData.setName(processDefinition.getName());        // 名称
        modelData.setVersion(processDefinition.getVersion());    // 版本
        modelData.setModelType(0);
        modelData.setCreatedBy(userName);    // 创建人，当前用户名
        modelData.setLastUpdatedBy(userName); // 最后更新人，当前用户名
        modelData.setLastUpdated(new Date());

        var engConf = processEngine.getProcessEngineConfiguration();

        /* 获得活动的连线对象 */
        var highLightedFlowInstances = runtimeService.createActivityInstanceQuery().activityType(BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW).processInstanceId(processInstanceId).list();
        var flows = new ArrayList<String>();                // 连线ID

        for (ActivityInstance ai : highLightedFlowInstances) {
            flows.add(ai.getActivityId());
        }

        var diagramGenerator = engConf.getProcessDiagramGenerator();
        var inr = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, flows,
                engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(),
                engConf.getClassLoader(), 1.0, true);

        return inr;
    }
}
