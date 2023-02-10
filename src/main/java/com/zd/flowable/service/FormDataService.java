package com.zd.flowable.service;

import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;

/**
 * @description:
 * @auther: zd
 * @date: 2023/2/10
 **/
public interface FormDataService {

    Result addFormData(FormDataProperty formDataProperty);
}
