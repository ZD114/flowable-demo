package com.zd.flowable.controller;

import com.zd.flowable.model.Result;
import com.zd.flowable.model.RuntimeProperty;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 运行过程管理
 *
 * @author zhangda
 * @date: 2023/2/2
 **/
@RestController
@RequestMapping("/runtime")
public class RuntimeController {

    private static final Logger log = LoggerFactory.getLogger(RuntimeController.class);
    private static final String EXCEPTION_MSG = "processInstanceKey are required parameters";

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 启动流程
     *
     * @param runtimeProperty
     * @return
     */
    @PostMapping("")
    public Result startProcess(@RequestBody RuntimeProperty runtimeProperty) {
        ProcessInstance processInstance = null;//流程实例对象
        String processDefinitionId = runtimeProperty.getProcessDefinitionId();//过程实例ID参数
        Map<String, Object> mapVariables = runtimeProperty.getMapVariables();//map参数
        String businessKey = runtimeProperty.getBusinessKey();//业务key

        if (StringUtils.isBlank(processDefinitionId)) { // 若定义ID不存在
            throw new BadRequestException("过程定义编号必须！");
        }

        log.info("业务key: {},定义key: {}", businessKey, processDefinitionId);

        String userName = runtimeProperty.getUserName();

        if (StringUtils.isBlank(userName)) {
            userName = "";
        }

        if (mapVariables == null && StringUtils.isBlank(businessKey)) {//只用id启动
            Authentication.setAuthenticatedUserId(userName);//设置流程发起人
            processInstance = runtimeService.startProcessInstanceById(processDefinitionId);
            Authentication.setAuthenticatedUserId(null);//这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
        }

        if (mapVariables != null && StringUtils.isBlank(businessKey)) {//若map参数不为空，businessKey为空
            Authentication.setAuthenticatedUserId(userName);//设置流程发起人
            processInstance = runtimeService.startProcessInstanceById(processDefinitionId, mapVariables);    //map存储变量 用流程定义的KEY启动，会自动选择KEY相同的流程定义中最新版本的那个(KEY为模型中的流程唯一标识)
            Authentication.setAuthenticatedUserId(null);//这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
        }

        if (mapVariables != null && StringUtils.isNotBlank(businessKey)) { //若map参数和businessKey都不为空
            processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey, mapVariables);
        }

        if (mapVariables == null && StringUtils.isNotBlank(businessKey)) { //若map参数为空，businessKey不为空
            processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey);
        }

        if (processInstance == null) {
            throw new NullPointerException();
        }

        return Result.ok().data("过程实例编号: ", processInstance.getId());
    }

    /**
     * 表单启动流程
     *
     * @param runtimeProperty
     * @return
     */
    @PostMapping("/form")
    public Result formStart(@RequestBody RuntimeProperty runtimeProperty) {
        String processDefinitionId = runtimeProperty.getProcessDefinitionId();//过程实例ID参数
        Map<String, Object> mapVariables = runtimeProperty.getMapVariables();//map参数

        if (StringUtils.isBlank(processDefinitionId)) { // 若定义ID不存在
            throw new BadRequestException(EXCEPTION_MSG);
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceWithForm(processDefinitionId, "", mapVariables, "");

        return Result.ok().data("processInstanceId", processInstance.getId());
    }
}
