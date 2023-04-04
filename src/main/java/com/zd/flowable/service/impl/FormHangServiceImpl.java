package com.zd.flowable.service.impl;

import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormHang;
import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormHangService;
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
 * @date: 2023/2/13
 **/
@Service
public class FormHangServiceImpl implements FormHangService {

    @Autowired
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addFormHang(FormHangProperty formHangProperty) {
        var formDataIds = formHangProperty.getFormDataId().split(",");

        var dataList = new ArrayList<FormHang>();

        for (String formDataId : formDataIds) {
            var entity = new FormHang();
            var now = LocalDateTime.now();

            formHangProperty.setFormDataId(formDataId);

            BeanUtils.copyProperties(formHangProperty, entity, Constant.ID);

            entity.setId(UuidUtil.get32UUID());
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.ID),
                    JdbcUtility.getSqlParameterSource(entity, Constant.ID));
        }
        return Result.ok().data(Constant.RESULT, dataList);
    }

    @Override
    public Result delFormHang(String id) {
        var param = new HashMap<String, Object>();

        param.put("id", id);

        nameJdbcTemplate.update("delete from form_hang where id = :id", param);

        return Result.ok();
    }

    @Override
    public Result delHangByFormDataId(String formDataId) {
        var param = new HashMap<String, Object>();

        param.put("id", formDataId);

        nameJdbcTemplate.update("delete from form_hang where id = :id", param);

        return Result.ok();
    }

    @Override
    public Result updateFormHang(FormHangProperty formHangProperty) {
        var entity = new FormHang();
        BeanUtils.copyProperties(formHangProperty, entity);

        entity.setUpdateTime(LocalDateTime.now());

        nameJdbcTemplate.update(JdbcUtility.getUpdateSql(entity), JdbcUtility.getSqlParameterSource(entity));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public List<FormHang> searchPageList(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(FormHang.class));
    }

    @Override
    public RestResult<FormHang> findFormHangById(String id) {
        var param = new HashMap<String, Object>();

        param.put("id", id);

        var formHang = nameJdbcTemplate.query("select * from form_hang where id = :id", param, new BeanPropertyRowMapper<>(FormHang.class));

        if (formHang.size() == 0) {
            return new RestResult<>(true, "200", "", null);
        }

        return new RestResult<>(true, "200", "", formHang.get(0));
    }

    @Override
    public Result delBatch(List<String> ids) {
        var list = new ArrayList<FormHang>();

        for (int i = 0; i < ids.size(); i++) {
            var fh = new FormHang();
            fh.setId(ids.get(i));
            list.add(fh);
        }

        var batch = SqlParameterSourceUtils.createBatch(list);

        nameJdbcTemplate.batchUpdate("DELETE FROM form_hang WHERE id = :id", batch);

        return Result.ok();
    }
}
