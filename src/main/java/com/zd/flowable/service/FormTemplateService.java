package com.zd.flowable.service;

import com.zd.flowable.model.FormTemplateProperty;
import com.zd.flowable.model.Result;

/**
 * @author zhangda
 * @date: 2023/2/6
 **/
public interface FormTemplateService {

    Result addTemplate(FormTemplateProperty templateProperty);
}
