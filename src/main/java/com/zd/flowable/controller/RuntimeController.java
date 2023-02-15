package com.zd.flowable.controller;

import com.zd.flowable.model.Result;
import com.zd.flowable.model.ResultCodeEnum;
import com.zd.flowable.model.RuntimeProperty;
import com.zd.flowable.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private RepositoryService repositoryService; //管理流程定义  与流程定义和部署对象相关的Service

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
        String businessKey = runtimeProperty.getBusinessKey();//业务key

        if (StringUtils.isBlank(processDefinitionId)) { // 若定义ID不存在
            throw new BadRequestException(EXCEPTION_MSG);
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceWithForm(processDefinitionId, "", mapVariables, businessKey);

        return Result.ok().data("processInstanceId", processInstance.getId());
    }

    /**
     * 预览XML
     *
     * @param runtimeProperty
     * @return
     * @throws IOException
     */
    @PostMapping("/xml")
    public Result getViewXml(@RequestBody RuntimeProperty runtimeProperty) throws IOException {
        String deploymentId = runtimeProperty.getDeploymentId();
        String fileName = runtimeProperty.getFileName();

        createXmlAndPng(deploymentId);

        return Result.ok().data("code", FileUtil.readFileAllContent(URLDecoder.decode(fileName, "UTF-8")));
    }

    /**
     * 获取预览PNG
     *
     * @param runtimeProperty
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/png")
    public Result getViewPng(@RequestBody RuntimeProperty runtimeProperty) {
        String deploymentId = runtimeProperty.getDeploymentId();
        String fileName = runtimeProperty.getFileName();

        Map<String, Object> map = new HashMap<>();

        try {
            createXmlAndPng(deploymentId);//生成XML和PNG

            String newFileName = URLDecoder.decode(fileName, "UTF-8");
            String imgSrcPath = PathUtil.getProjectPath() + newFileName;

            map.put("imgSrc", "data:image/jpeg;base64," + Base64Utils.getImageStr(imgSrcPath)); //解决图片src中文乱码，把图片转成base64格式显示(这样就不用修改tomcat的配置了)

        } catch (IOException e) {
            return Result.error(ResultCodeEnum.ERROR);
        }

        return Result.ok().data(map);
    }

    /**
     * 根据流程定义的部署ID生成XML和PNG
     *
     * @param deploymentId 部署ID
     * @throws IOException
     */
    protected void createXmlAndPng(String deploymentId) throws IOException {
        DelFileUtil.delFolder(PathUtil.getProjectPath());            //生成先清空之前生成的文件
        List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
        for (String name : names) {
            if (name.indexOf("zip") != -1) continue;
            InputStream in = repositoryService.getResourceAsStream(deploymentId, name);
            FileUpload.copyFile(in, PathUtil.getProjectPath(), name);            //把文件上传到文件目录里面
            in.close();
        }
    }

    /**
     * 激活流程
     *
     * @param processInstanceId 实例编号
     */
    @PutMapping("/actives/{processInstanceId}")
    public Result activateProcessDefinitionById(@PathVariable String processInstanceId) {
        repositoryService.activateProcessDefinitionById(processInstanceId, true, null);

        return Result.ok();
    }

    /**
     * 挂起流程
     *
     * @param processInstanceId 实例编号
     */
    @PutMapping("/suspends/{processInstanceId}")
    public Result suspendProcessDefinitionById(@PathVariable String processInstanceId) {
        repositoryService.suspendProcessDefinitionById(processInstanceId, true, null);

        return Result.ok();
    }

    /**
     * 节点跳转
     *
     * @param runtimeProperty
     * @return
     */
    @PostMapping("/jump")
    public Result moveActivityIdTo(@RequestBody RuntimeProperty runtimeProperty) {
        var processInstanceId = runtimeProperty.getProcessInstanceId();
        var nodeId = runtimeProperty.getNodeId();
        var toNodeId = runtimeProperty.getToNodeId();

        if (StringUtils.isBlank(processInstanceId)) {
            return Result.error(ResultCodeEnum.NULL_ARGUMENT_ERROR);
        }

        if (StringUtils.isBlank(nodeId)) {
            return Result.error(ResultCodeEnum.NULL_ARGUMENT_ERROR);
        }

        if (StringUtils.isBlank(toNodeId)) {
            return Result.error(ResultCodeEnum.NULL_ARGUMENT_ERROR);
        }

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(processInstanceId)
                .moveActivityIdTo(nodeId, toNodeId)
                .changeState();

        return Result.ok();
    }
}
