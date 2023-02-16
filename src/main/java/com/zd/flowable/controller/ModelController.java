package com.zd.flowable.controller;

import com.zd.flowable.model.Result;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param modelId 模型编号
     * @return
     */
    @DeleteMapping("/{modelId}")
    public Result deleteModel(@PathVariable String modelId) {
        modelService.deleteModel(modelId);

        return Result.ok();
    }

}
