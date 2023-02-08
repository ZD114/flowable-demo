package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormTemplateProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormTemplateService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author zhangda
 * @date: 2023/2/6
 **/
@Service
public class FormTemplateServiceImpl implements FormTemplateService {

    @Resource
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addTemplate(FormTemplateProperty templateProperty) {
        var keyHolder = new GeneratedKeyHolder();
        var entity = new FormTemplates();
        var now = LocalDateTime.now();

        BeanUtils.copyProperties(templateProperty, entity, "formTemplatesId");

        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, "formTemplatesId"),
                JdbcUtility.getSqlParameterSource(entity, "formTemplatesId"), keyHolder);

        entity.setFormTemplatesId(keyHolder.getKey().longValue());

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Result delTemplate(Long id) {

        var param = new HashMap<String, Object>();

        param.put("id", id);

        nameJdbcTemplate.update("delete from form_templates where form_templates_id = :id", param);

        return Result.ok();
    }

    @Override
    public Result updateTemplate(FormTemplateProperty templateProperty) {
        var entity = new FormTemplates();

        BeanUtils.copyProperties(templateProperty, entity);
        entity.setUpdateTime(LocalDateTime.now());

        nameJdbcTemplate.update(JdbcUtility.getUpdateSql(entity), JdbcUtility.getSqlParameterSource(entity));

        return Result.ok().data(Constant.RESULT, entity);
    }
}
