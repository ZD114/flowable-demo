package com.zd.flowable.controller;

import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 挂靠表单
 * @author zhangda
 * @date: 2023/2/13
 **/
@RestController
@RequestMapping("form-hang")
public class FormHangController {

    @Autowired
    private FormHangService formHangService;

    /**
     * 保存
     * @param formHangProperty
     * @return
     */
    @PostMapping("")
    public Result addFormHang(@RequestBody FormHangProperty formHangProperty) {
        return formHangService.addFormHang(formHangProperty);
    }
}
