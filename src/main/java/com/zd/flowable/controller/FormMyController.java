package com.zd.flowable.controller;

import com.alibaba.excel.util.StringUtils;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormMyService;
import com.zd.flowable.service.FormTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 我的表单管理
 *
 * @author zhangda
 * @date: 2023/2/14
 **/
@RestController
@RequestMapping("/form-my")
public class FormMyController {

    @Autowired
    private FormMyService formMyService;
    @Autowired
    private FormTemplatesService formTemplatesService;

    /**
     * 保存我的表单
     *
     * @param formMyProperty
     * @return
     */
    @PostMapping("")
    public Result addFormMy(@RequestBody FormMyProperty formMyProperty) {

        if (StringUtils.isNotBlank(formMyProperty.getFormTemplatesId())) {
            var formTemplates = formTemplatesService.findTemplateById(formMyProperty.getFormTemplatesId());
            formMyProperty.setHtmlContent(formTemplates.getResult().getHtmlContent());
        }

        return formMyService.addFormMy(formMyProperty);
    }
}
