package com.zd.flowable.service;

import com.zd.flowable.entity.TaskDetail;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @auther: zd
 * @date: 2023/4/24
 **/
public interface TaskSpecialService {
    List<TaskDetail> searchPageList(String sql, Map<String, Object> params);
}
