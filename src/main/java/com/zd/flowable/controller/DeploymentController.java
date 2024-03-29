package com.zd.flowable.controller;

import com.zd.flowable.model.DeploymentProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * 部署管理
 *
 * @author zhangda
 * @date: 2023/2/2
 **/
@RestController
@RequestMapping("/deployment")
public class DeploymentController {

    private static final Logger log = LoggerFactory.getLogger(DeploymentController.class);

    @Autowired
    private ModelService modelService;            //模型服务

    @Autowired
    private RepositoryService repositoryService; //管理流程定义  与流程定义和部署对象相关的Service

    @Autowired
    private FormRepositoryService formRepositoryService; //表单服务

    /**
     * 根据模型编号部署流程
     *
     * @param deploymentProperty
     * @return
     */
    @PostMapping("/modelId")
    public Result deploymentById(@RequestBody DeploymentProperty deploymentProperty) {
        var modelId = deploymentProperty.getModelId();

        var model = modelService.getModel(modelId);
        var bpmnModel = modelService.getBpmnModel(model);

        var deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addBpmnModel(model.getKey() + ".bpmn", bpmnModel).deploy();
        var processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        log.info("Found process definition: {}", deployment.getId());

        return Result.ok().data("processDefinition", processDefinition.getId());    //实例ID
    }

    /**
     * 部署流程(从Classpath)
     *
     * @param deploymentProperty
     * @return
     */
    @PostMapping("/classpath")
    public Result deploymentProcessDefinitionFromClasspath(@RequestBody DeploymentProperty deploymentProperty) {
        var name = deploymentProperty.getName();
        var xmlPath = deploymentProperty.getXmlPath();
        var pngPath = deploymentProperty.getPngPath();

        var deploymentBuilder = repositoryService.createDeployment();        //创建部署对象
        deploymentBuilder.name(name);                        //部署名称

        if (StringUtils.isNotBlank(xmlPath)) {
            deploymentBuilder.addClasspathResource(xmlPath);    //从文件中读取xml资源
        }
        if (StringUtils.isNotBlank(pngPath)) {
            deploymentBuilder.addClasspathResource(pngPath);    //从文件中读取png资源
        }

        var deployment = deploymentBuilder.deploy();    //完成部署

        return Result.ok().data(Constant.DEPLOYMENT_ID, deployment.getId());  //部署ID
    }

    /**
     * 部署流程定义(从zip压缩包)
     *
     * @param deploymentProperty
     * @return
     * @throws Exception
     */
    @PostMapping("/zip")
    public Result deploymentProcessDefinitionFromZip(@RequestBody DeploymentProperty deploymentProperty) throws IOException {
        var name = deploymentProperty.getName();
        var zipPath = deploymentProperty.getZipPath();
        var tenantId = deploymentProperty.getTenantId();

        var outfile = new File(zipPath);
        var inputStream = new FileInputStream(outfile);
        var ipInputStream = new ZipInputStream(inputStream);

        var deploymentBuilder = repositoryService.createDeployment();        //创建部署对象

        deploymentBuilder.name(name);                        //部署名称
        deploymentBuilder.tenantId(tenantId); //部署的租户
        deploymentBuilder.addZipInputStream(ipInputStream);

        var deployment = deploymentBuilder.deploy();    //完成部署

        ipInputStream.close();
        inputStream.close();

        return Result.ok().data(Constant.DEPLOYMENT_ID, deployment.getId());   //部署ID
    }

    /**
     * 外置表单部署流程
     *
     * @param deploymentProperty
     * @return
     */
    @PostMapping("/form")
    public Result deploymentForm(@RequestBody DeploymentProperty deploymentProperty) {
        var deployment = formRepositoryService.createDeployment()
                .addClasspathResource(deploymentProperty.getFormName())
                .name(deploymentProperty.getName())
                .parentDeploymentId(deploymentProperty.getDeploymentId())
                .deploy();
        return Result.ok().data(Constant.DEPLOYMENT_ID, deployment.getId());
    }
}
