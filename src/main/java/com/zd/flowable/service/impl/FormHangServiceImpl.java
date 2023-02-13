package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormHang;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormHangService;
import com.zd.flowable.utils.Constant;
import com.zd.flowable.utils.JdbcUtility;
import com.zd.flowable.utils.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
}
