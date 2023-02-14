package com.zd.flowable.service;

import com.zd.flowable.entity.FormMy;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;

import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/14
 **/
public interface FormMyService {

    Result addFormMy(FormMyProperty formMyProperty);

    Result delFormMy(String id);

    Result updateFormMy(FormMyProperty formMyProperty);

    Integer countFormMy(String sql, Map<String, Object> params);

    List<FormMy> searchPageList(String sql, Map<String, Object> params);

    FormMy findFormMyById(String id);
}
