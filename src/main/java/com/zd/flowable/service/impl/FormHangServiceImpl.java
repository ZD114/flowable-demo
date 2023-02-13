package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormHang;
import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormHangService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import com.zd.flowable.utils.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/13
 **/
@Service
public class FormHangServiceImpl implements FormHangService {

    @Resource
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addFormHang(FormHangProperty formHangProperty) {
        var formDataIds = formHangProperty.getFormDataId().split(",");

        for (String formDataId : formDataIds) {
            var entity = new FormHang();
            var now = LocalDateTime.now();

            BeanUtils.copyProperties(formDataId, entity, Constant.FORM_HANG_ID);

            entity.setFormHangId(UuidUtil.get32UUID());
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.FORM_HANG_ID),
                    JdbcUtility.getSqlParameterSource(entity, Constant.FORM_HANG_ID));
        }
        return null;
    }

    @Override
    public Result delFormHang(String id) {
        var param = new HashMap<String, Object>();

        param.put("id", id);

        nameJdbcTemplate.update("delete from form_hang where form_hang_id = :id", param);

        return Result.ok();
    }

    @Override
    public Result delHangByFormDataId(String formDataId) {
        var param = new HashMap<String, Object>();

        param.put("id", formDataId);

        nameJdbcTemplate.update("delete from form_hang where form_data_id = :id", param);

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
    public Integer countFormHang(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.queryForObject(sql, params, Integer.class);
    }
}
