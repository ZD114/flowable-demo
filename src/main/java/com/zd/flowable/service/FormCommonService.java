package com.zd.flowable.service;

import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/21
 **/
public interface FormCommonService {

    Integer countForm(String sql, Map<String, Object> params);
}
