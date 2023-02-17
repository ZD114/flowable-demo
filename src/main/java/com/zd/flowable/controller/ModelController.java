package com.zd.flowable.controller;

import com.zd.flowable.model.Result;
import org.flowable.ui.common.service.exception.ConflictingRequestException;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 模型管理
 *
 * @author zhangda
 * @date: 2023/2/16
 **/
@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;
    @Autowired
    protected FlowableModelQueryService modelQueryService;

    /**
     * 获取模型基本信息
     *
     * @param modelId 模型编号id
     * @return
     */
    @GetMapping("/{modelId}")
    public ModelRepresentation getModel(@PathVariable String modelId) {
        return modelService.getModelRepresentation(modelId);
    }

    /**
     * 删除模型
     *
     * @param modelId 模型编号
     * @return
     */
    @DeleteMapping("/{modelId}")
    public Result deleteModel(@PathVariable String modelId) {
        modelService.deleteModel(modelId);

        return Result.ok();
    }

    /**
     * 导入模型文件
     *
     * @param request
     * @param file    文件
     * @return
     */
    @PostMapping("/import")
    public ModelRepresentation importProcessModel(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        return modelQueryService.importProcessModel(request, file);
    }

    /**
     * 创建模型
     *
     * @param modelRepresentation
     * @return
     */
    @PostMapping("/{userId}")
    public ModelRepresentation createModel(@RequestBody ModelRepresentation modelRepresentation, @PathVariable String userId) {
        modelRepresentation.setKey(modelRepresentation.getKey().replace(" ", ""));

        checkForDuplicateKey(modelRepresentation);

        var json = modelService.createModelJson(modelRepresentation);
        var newModel = modelService.createModel(modelRepresentation, json, userId);

        return new ModelRepresentation(newModel);
    }

    public void checkForDuplicateKey(ModelRepresentation modelRepresentation) {
        var modelKeyInfo = modelService.validateModelKey(null, modelRepresentation.getModelType(), modelRepresentation.getKey());

        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new ConflictingRequestException("Provided model key already exists: " + modelRepresentation.getKey());
        }

    }
}
