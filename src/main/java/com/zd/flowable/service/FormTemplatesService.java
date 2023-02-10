package com.zd.flowable.service;

import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormTemplatesProperty;
import com.zd.flowable.model.Result;

import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/6
 **/
public interface FormTemplatesService {

    Result addTemplate(FormTemplatesProperty templateProperty);

    Result delTemplate(Long id);

    Result updateTemplate(FormTemplatesProperty templateProperty);

    Integer countTemplate(String sql, Map<String, Object> params);

    List<FormTemplatesProperty> searchPageList(String sql, Map<String, Object> params);

    RestResult<FormTemplates> findTemplateById(Long id);

    Result delTemplateBatch(List<Long> ids);

    List<FormTemplates> queryAll();
}
