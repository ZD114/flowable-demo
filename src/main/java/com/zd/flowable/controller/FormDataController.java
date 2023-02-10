package com.zd.flowable.controller;

import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 增加
     *
     * @param formDataProperty
     * @return
     */
    @PostMapping("")
    public Result addFormData(@RequestBody FormDataProperty formDataProperty) {
        return formDataService.addFormData(formDataProperty);
    }

    /**
     * 上传附件
     *
     * @param file
     * @param formDataProperty
     * @return
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, FormDataProperty formDataProperty) {
        return formDataService.uploadFile(file, formDataProperty);
    }
}
