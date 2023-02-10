package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormData;
import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.model.ResultCodeEnum;
import com.zd.flowable.service.FormDataService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author zhangda
 * @date: 2023/2/10
 **/
@Service
public class FormDataServiceImpl implements FormDataService {

    @Resource
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addFormData(FormDataProperty formDataProperty) {
        var keyData = new GeneratedKeyHolder();
        var entity = new FormData();
        var now = LocalDateTime.now();

        BeanUtils.copyProperties(formDataProperty, entity, Constant.FORM_DATA_ID);

        entity.setUpdateTime(now);
        entity.setCreateTime(now);

        nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.FORM_DATA_ID),
                JdbcUtility.getSqlParameterSource(entity, Constant.FORM_DATA_ID), keyData);

        entity.setFormDataId(keyData.getKey().longValue());

        return Result.ok().data(Constant.RESULT, entity);
    }
}
