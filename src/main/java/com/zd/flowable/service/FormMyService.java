package com.zd.flowable.service;

import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;

/**
 * @author zhangda
 * @date: 2023/2/14
 **/
public interface FormMyService {

    Result addFormMy(FormMyProperty formMyProperty);

    Result delFormMy(String id);
}
