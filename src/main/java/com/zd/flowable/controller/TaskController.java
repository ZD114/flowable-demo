package com.zd.flowable.controller;

import com.zd.flowable.common.PageResult;
import com.zd.flowable.entity.TaskDetail;
import com.zd.flowable.model.Result;
import com.zd.flowable.model.ResultCodeEnum;
import com.zd.flowable.model.TaskProperty;
import com.zd.flowable.model.TaskSearchParam;
import com.zd.flowable.service.FormCommonService;
import com.zd.flowable.service.TaskSpecialService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.form.model.FormField;
import org.flowable.form.model.SimpleFormModel;
import org.flowable.task.api.Task;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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
    private TaskService taskService;

    @Autowired
    private FormCommonService formCommonService;

    @Autowired
    private TaskSpecialService taskSpecialService;

    /**
     * 根据任务编号完成任务
     *
     * @param taskProperty
     * @return
     */
    @PostMapping("")
    public Result completeTask(@RequestBody TaskProperty taskProperty) {
        var taskId = taskProperty.getTaskId();//任务编号

        var assignee = taskProperty.getUserId();// 用户

        log.info("任务编号：{}", taskId);

        if (StringUtils.isBlank(taskId)) {
            throw new BadRequestException(EXCEPTION_MSG2);
        }
        if (StringUtils.isBlank(assignee)) { // 用户不存在
            throw new BadRequestException(EXCEPTION_MSG3);
        }

        var mapVariables = taskProperty.getMapVariables();//参数对象
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
     * @param taskProperty ACT_RU_TASK ：ID_
     *                     ACT_FO_FORM_DEFINITION ：ID_
     * @return
     */
    @PostMapping("/form")
    public Result completeForm(@RequestBody TaskProperty taskProperty) {
        var taskId = taskProperty.getTaskId();
        var formDefinitionId = taskProperty.getFormDefinitionId();
        var mapVariables = taskProperty.getMapVariables();//参数对象

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
        var taskFormModel = taskService.getTaskFormModel(taskId);
        var formModel = (SimpleFormModel) taskFormModel.getFormModel();

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

    /**
     * 候选人归还任务
     *
     * @param taskProperty
     * @return
     */
    @PostMapping("/unClaimTask")
    public Result unClaimTaskUser(@RequestBody TaskProperty taskProperty) {
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
                .taskAssignee(userId)
                .singleResult();

        if (task != null) {
            // 拾取对应的任务
            taskService.unclaim(task.getId());
            log.info("任务归还成功，归还人：{}", userId);
        }

        return Result.ok();
    }

    /**
     * 代办任务列表
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/list")
    public PageResult<TaskDetail> searchPage(@RequestBody TaskSearchParam searchParam) {
        var pageResult = new PageResult<TaskDetail>();

        var params = new HashMap<String, Object>();

        var sql = new StringBuilder("SELECT * FROM ACT_RU_TASK WHERE 1=1 ");

        if (StringUtils.isNotBlank(searchParam.getProcessInstanceId())) {
            sql.append(" AND PROC_DEF_ID_ = :processInstanceId ");
            params.put("processInstanceId", searchParam.getProcessInstanceId());

        }

        if (StringUtils.isNotBlank(searchParam.getUserName())) {
            sql.append(" AND ASSIGNEE_ like :userName ");
            params.put("userName", "%" + searchParam.getUserName() + "%");

        }

        if (StringUtils.isNotBlank(searchParam.getLastStart())) {
            sql.append(" and CREATE_TIME_ >= :createTime");
            params.put("createTime", searchParam.getLastStart());

        }

        if (StringUtils.isNotBlank(searchParam.getLastEnd())) {
            sql.append(" and CREATE_TIME_ <= :createTime");
            params.put("createTime", searchParam.getLastEnd());

        }

        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY ID_ ) eq");

        var totalCount = formCommonService.countForm(sqlCount.toString(), params);

        log.info("总数量：{}", totalCount);

        if (totalCount > 0) {
            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY ID_ ORDER BY ID_ DESC LIMIT :start,:pageSize ");
            params.put("start", start);
            params.put("pageSize", searchParam.getPageSize());

            var resources = taskSpecialService.searchPageList(sql.toString(), params);

            pageResult.setData(resources);
        }

        pageResult.setTotal(totalCount);
        pageResult.setTotalPages((long) Math.ceil(totalCount / (double) searchParam.getPageSize()));

        return pageResult;
    }

}
