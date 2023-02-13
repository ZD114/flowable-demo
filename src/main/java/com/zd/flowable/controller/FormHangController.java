package com.zd.flowable.controller;

import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 挂靠表单
 *
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
     *
     * @param formHangProperty
     * @return
     */
    @PostMapping("")
    public Result addFormHang(@RequestBody FormHangProperty formHangProperty) {
        return formHangService.addFormHang(formHangProperty);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("id")
    public Result delFormHang(@PathVariable String id) {
        return formHangService.delFormHang(id);
    }
}
