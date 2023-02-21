package com.zd.flowable.service;

import com.zd.flowable.entity.FormHang;
import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @auther: zd
 * @date: 2023/2/13
 **/
public interface FormHangService {

    Result addFormHang(FormHangProperty formHangProperty);

    Result delFormHang(String id);

    Result delHangByFormDataId(String formDataId);

    Result updateFormHang(FormHangProperty formHangProperty);

    List<FormHang> searchPageList(String sql, Map<String, Object> params);

    FormHang findFormHangById(String id);

    Result delBatch(List<String> ids);

}
