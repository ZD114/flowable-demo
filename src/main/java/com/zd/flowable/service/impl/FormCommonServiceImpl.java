package com.zd.flowable.service.impl;

import com.zd.flowable.service.FormCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 公共表单服务
 * @author zhangda
 * @date: 2023/2/21
 **/
@Service
public class FormCommonServiceImpl implements FormCommonService {

    @Autowired
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Integer countForm(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.queryForObject(sql, params, Integer.class);
    }
}
