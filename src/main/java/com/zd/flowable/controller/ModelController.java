package com.zd.flowable.controller;

import com.zd.flowable.model.Result;
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
}
