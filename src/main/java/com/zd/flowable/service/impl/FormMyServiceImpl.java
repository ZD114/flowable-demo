package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormHang;
import com.zd.flowable.entity.FormMy;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormMyService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import com.zd.flowable.utils.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/14
 **/
@Service
public class FormMyServiceImpl implements FormMyService {

    @Resource
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addFormMy(FormMyProperty formMyProperty) {
        var entity = new FormMy();
        var now = LocalDateTime.now();

        BeanUtils.copyProperties(formMyProperty, entity, Constant.FORM_MY_ID);

        entity.setFormMyId(UuidUtil.get32UUID());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.FORM_MY_ID),
                JdbcUtility.getSqlParameterSource(entity, Constant.FORM_MY_ID));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Result delFormMy(String id) {
        var param = new HashMap<String, Object>();

        param.put("id", id);

        nameJdbcTemplate.update("delete from form_my where form_my_id = :id", param);

        return Result.ok();
    }

    @Override
    public Result updateFormMy(FormMyProperty formMyProperty) {
        var entity = new FormMy();

        BeanUtils.copyProperties(formMyProperty, entity);
        entity.setUpdateTime(LocalDateTime.now());

        nameJdbcTemplate.update(JdbcUtility.getUpdateSql(entity), JdbcUtility.getSqlParameterSource(entity));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Integer countFormMy(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public List<FormMy> searchPageList(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(FormMy.class));
    }

    @Override
    public FormMy findFormMyById(String id) {
        var param = new HashMap<String, Object>();

        param.put("id", id);

        return nameJdbcTemplate.queryForObject("select * from form_my where form_my_id = :id", param, FormMy.class);
    }

    @Override
    public Result delBatchFormMy(List<String> ids) {
        List<FormMy> list = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            FormMy fm = new FormMy();
            fm.setFormMyId(ids.get(i));
            list.add(fm);
        }

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list);

        nameJdbcTemplate.batchUpdate("DELETE FROM form_my WHERE form_my_id = :formMyId", batch);

        return Result.ok();
    }

    @Override
    public List<FormMy> queryAll() {
        return nameJdbcTemplate.query("select * from form_my", new BeanPropertyRowMapper<>(FormMy.class));
    }
}