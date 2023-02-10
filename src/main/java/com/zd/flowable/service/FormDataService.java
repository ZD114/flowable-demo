package com.zd.flowable.service;

import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @auther: zd
 * @date: 2023/2/10
 **/
public interface FormDataService {

    Result addFormData(FormDataProperty formDataProperty);

    Result uploadFile(MultipartFile file, FormDataProperty formDataProperty);
}
