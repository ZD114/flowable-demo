package com.zd.flowable.service;

import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;

/**
 * @description:
 * @auther: zd
 * @date: 2023/2/13
 **/
public interface FormHangService {

    Result addFormHang(FormHangProperty formHangProperty);

    Result delFormHang(String id);

    Result delHangByFormDataId(String formDataId);

}
