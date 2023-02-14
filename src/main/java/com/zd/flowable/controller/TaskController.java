package com.zd.flowable.controller;

import com.zd.flowable.model.Result;
import com.zd.flowable.model.ResultCodeEnum;
import com.zd.flowable.model.TaskProperty;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.form.api.FormInfo;
import org.flowable.form.model.FormField;
import org.flowable.form.model.SimpleFormModel;
import org.flowable.task.api.Task;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务管理
 *
 * @author zhangda
 * @date: 2023/2/2
 **/
@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private static final String EXCEPTION_MSG2 = "processInstanceId are required parameters";
    private static final String EXCEPTION_MSG3 = "userId are required parameters";

    @Autowired
    protected TaskService taskService;

    /**
     * 根据任务编号完成任务
     *
     * @param taskProperty
     * @return
     */
    @PostMapping("")
    public Result completeTask(@RequestBody TaskProperty taskProperty) {
        String taskId = taskProperty.getTaskId();//任务编号

        String assignee = taskProperty.getUserId();// 用户

        log.info("任务编号：{}", taskId);

        if (StringUtils.isBlank(taskId)) {
            throw new BadRequestException(EXCEPTION_MSG2);
        }
        if (StringUtils.isBlank(assignee)) { // 用户不存在
            throw new BadRequestException(EXCEPTION_MSG3);
        }

        Map<String, Object> mapVariables = taskProperty.getMapVariables();//参数对象
        if (mapVariables == null) {//不带参启动
            taskService.complete(taskId);
        } else {
            taskService.complete(taskId, mapVariables);
        }

        return Result.ok();
    }

    /**
     * 查询我的任务
     *
     * @param userName 办理人
     * @return 返回任务列表
     */
    @GetMapping("/{userName}")
    public List<Task> findMyPersonalTask(@PathVariable String userName) {
        return taskService.createTaskQuery()
                .taskAssignee(userName)  // 指定办理人
                .list();
    }

    /**
     * 完成表单
     *
     * @param taskProperty
     * @return
     */
    @PostMapping("/form")
    public Result completeForm(@RequestBody TaskProperty taskProperty) {
        String taskId = taskProperty.getTaskId();
        String formDefinitionId = taskProperty.getFormDefinitionId();
        Map<String, Object> mapVariables = taskProperty.getMapVariables();//参数对象

        taskService.completeTaskWithForm(taskId, formDefinitionId, "", mapVariables);

        return Result.ok().data(mapVariables);
    }

    /**
     * 获取任务表单数据
     *
     * @param taskId 任务编号
     * @return
     */
    @GetMapping("/form/{taskId}")
    public List<FormField> taskFormDataGet(@PathVariable String taskId) {
        FormInfo taskFormModel = taskService.getTaskFormModel(taskId);
        SimpleFormModel formModel = (SimpleFormModel) taskFormModel.getFormModel();

        return formModel.getFields();
    }

    /**
     * 查询拾取代办任务列表
     *
     * @param taskProperty
     * @return
     */
    @PostMapping("/candidate/list")
    public List<Task> claimTaskList(@RequestBody TaskProperty taskProperty) {
        var processInstanceId = taskProperty.getProcessInstanceId();//执行编号
        var userId = taskProperty.getUserId();//候选用户编号

        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateUser(userId)
                .list();
    }

    /**
     * 候选人拾取任务
     *
     * @param taskProperty
     * @return
     */
    @PostMapping("/claimTask")
    public Result claimTaskUser(@RequestBody TaskProperty taskProperty) {
        var processInstanceId = taskProperty.getProcessInstanceId();//执行编号
        var userId = taskProperty.getUserId();//候选用户编号

        if (StringUtils.isBlank(processInstanceId)) {
            return Result.error(ResultCodeEnum.NULL_ARGUMENT_ERROR);
        }
        if (StringUtils.isBlank(userId)) {
            return Result.error(ResultCodeEnum.NULL_ARGUMENT_ERROR);
        }

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateUser(userId)
                .singleResult();

        if (task != null) {
            // 拾取对应的任务
            taskService.claim(task.getId(), userId);
            log.info("任务拾取成功，拾取人：{}", userId);
        }

        return Result.ok();
    }
}
