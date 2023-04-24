package com.zd.flowable.service.impl;

import com.zd.flowable.entity.TaskDetail;
import com.zd.flowable.service.TaskSpecialService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/4/24
 **/
@Service
public class TaskSpecialServiceImpl implements TaskSpecialService {

    @Autowired
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public List<TaskDetail> searchPageList(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(TaskDetail.class));
    }
}
