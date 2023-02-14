package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormMy;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormMyService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import com.zd.flowable.utils.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;

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
}
