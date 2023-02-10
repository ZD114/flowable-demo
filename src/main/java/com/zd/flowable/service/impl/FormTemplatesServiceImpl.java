package com.zd.flowable.service.impl;

import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormTemplatesProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormTemplatesService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/6
 **/
@Service
public class FormTemplatesServiceImpl implements FormTemplatesService {

    @Resource
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addTemplate(FormTemplatesProperty templateProperty) {
        var keyHolder = new GeneratedKeyHolder();
        var entity = new FormTemplates();
        var now = LocalDateTime.now();

        BeanUtils.copyProperties(templateProperty, entity, Constant.FORM_TEMPLATES_ID);

        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.FORM_TEMPLATES_ID),
                JdbcUtility.getSqlParameterSource(entity, Constant.FORM_TEMPLATES_ID), keyHolder);

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
    public Result updateTemplate(FormTemplatesProperty templateProperty) {
        var entity = new FormTemplates();

        BeanUtils.copyProperties(templateProperty, entity);
        entity.setUpdateTime(LocalDateTime.now());

        nameJdbcTemplate.update(JdbcUtility.getUpdateSql(entity), JdbcUtility.getSqlParameterSource(entity));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Integer countTemplate(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public List<FormTemplates> searchPageList(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(FormTemplates.class));
    }

    @Override
    public RestResult<FormTemplates> findTemplateById(Long id) {
        var param = new HashMap<String, Object>();

        param.put("id", id);

        var formTemplate = nameJdbcTemplate.query("select * from form_templates where form_templates_id = :id", param, new BeanPropertyRowMapper<>(FormTemplates.class));

        return new RestResult<>(true, "200", "", formTemplate.get(0));
    }

    @Override
    public Result delTemplateBatch(List<Long> ids) {
        List<FormTemplates> list = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            FormTemplates ft = new FormTemplates();
            ft.setFormTemplatesId(ids.get(i));
            list.add(ft);
        }

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(ids.toArray());

        nameJdbcTemplate.batchUpdate("DELETE FROM form_templates WHERE form_templates_id = :formTemplatesId", batch);

        return Result.ok();
    }

    @Override
    public List<FormTemplates> queryAll() {
        return nameJdbcTemplate.query("select * from form_templates", new BeanPropertyRowMapper<>(FormTemplates.class));
    }
}
