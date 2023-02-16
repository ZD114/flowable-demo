package com.zd.flowable.controller;

import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
