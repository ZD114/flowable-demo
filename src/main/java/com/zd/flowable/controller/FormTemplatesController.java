package com.zd.flowable.controller;

import com.zd.flowable.model.FormTemplateProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 自定义表单模板
 *
 * @author zhangda
 * @date: 2023/2/6
 **/
@RestController
@RequestMapping("/form-template")
public class FormTemplatesController {

    @Autowired
    private FormTemplateService formTemplateService;

    /**
     * 保存表单模板
     *
     * @param formTemplateProperty
     * @return
     */
    @PostMapping("")
    public Result addTemplate(@RequestBody FormTemplateProperty formTemplateProperty) {
        return formTemplateService.addTemplate(formTemplateProperty);
    }

    /**
     * 删除表单模板
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delTemplate(@PathVariable Long id) {
        return formTemplateService.delTemplate(id);
    }
}
