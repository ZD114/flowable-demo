package com.zd.flowable.service;

import com.zd.flowable.entity.FormData;
import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @auther: zd
 * @date: 2023/2/10
 **/
public interface FormDataService {

    Result addFormData(FormDataProperty formDataProperty);

    Result uploadFile(MultipartFile file, FormDataProperty formDataProperty);

    Result delFormData(Long formDataId);

    FormData findFormDataById(Long formDataId);

    Result updateFormData(FormDataProperty formDataProperty);

    Integer countFormData(String sql, Map<String, Object> params);

    List<FormData> searchPageList(String sql, Map<String, Object> params);
}
