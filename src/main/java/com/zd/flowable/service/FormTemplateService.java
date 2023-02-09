package com.zd.flowable.service;

import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormTemplateProperty;
import com.zd.flowable.model.Result;

import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/6
 **/
public interface FormTemplateService {

    Result addTemplate(FormTemplateProperty templateProperty);

    Result delTemplate(Long id);

    Result updateTemplate(FormTemplateProperty templateProperty);

    Integer countTemplate(String sql, Map<String, Object> params);

    List<FormTemplateProperty> searchPageList(String sql, Map<String, Object> params);

    RestResult<FormTemplates> findTemplateById(Long id);
}
