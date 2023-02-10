package com.zd.flowable.controller;

import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表单数据操作
 *
 * @author zhangda
 * @date: 2023/2/10
 **/
@RestController
@RequestMapping("/form-data")
public class FormDataController {

    @Autowired
    private FormDataService formDataService;

    @PostMapping("")
    public Result addFormData(@RequestBody FormDataProperty formDataProperty) {
        return formDataService.addFormData(formDataProperty);
    }
}
