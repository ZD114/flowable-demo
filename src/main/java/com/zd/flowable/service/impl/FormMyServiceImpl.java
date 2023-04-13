package com.zd.flowable.service.impl;

import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormMy;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormMyService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import com.zd.flowable.utils.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

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

    @Autowired
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addFormMy(FormMyProperty formMyProperty) {
        var entity = new FormMy();
        var now = LocalDateTime.now();

        BeanUtils.copyProperties(formMyProperty, entity, Constant.ID);

        entity.setId(UuidUtil.get32UUID());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.ID),
                JdbcUtility.getSqlParameterSource(entity, Constant.ID));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Result delFormMy(String id) {
        var param = new HashMap<String, Object>();

        param.put(Constant.ID, id);

        nameJdbcTemplate.update("delete from form_my where id = :id", param);

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
    public List<FormMy> searchPageList(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(FormMy.class));
    }

    @Override
    public RestResult<FormMy> findFormMyById(String id) {
        var param = new HashMap<String, Object>();

        param.put(Constant.ID, id);

        var formMy = nameJdbcTemplate.query("select * from form_my where id = :id", param, new BeanPropertyRowMapper<>(FormMy.class));

        if (formMy.size() == 0) {
            return new RestResult<>(true, "200", "", null);
        }

        return new RestResult<>(true, "200", "", formMy.get(0));
    }

    @Override
    public Result delBatchFormMy(List<String> ids) {
        var list = new ArrayList<FormMy>();

        for (int i = 0; i < ids.size(); i++) {
            var fm = new FormMy();
            fm.setId(ids.get(i));
            list.add(fm);
        }

        var batch = SqlParameterSourceUtils.createBatch(list);

        nameJdbcTemplate.batchUpdate("DELETE FROM form_my WHERE id = :id", batch);

        return Result.ok();
    }

    @Override
    public List<FormMy> queryAll() {
        return nameJdbcTemplate.query("select * from form_my", new BeanPropertyRowMapper<>(FormMy.class));
    }
}
