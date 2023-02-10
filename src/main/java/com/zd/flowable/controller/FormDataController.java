package com.zd.flowable.controller;

import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormDataService;
import com.zd.flowable.utils.DelFileUtil;
import com.zd.flowable.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 表单数据操作
 *
 * @author zhangda
 * @date: 2023/2/10
 **/
@RestController
@RequestMapping("/form-data")
public class FormDataController {

    @Autowired
    private FormDataService formDataService;

    /**
     * 增加
     *
     * @param formDataProperty
     * @return
     */
    @PostMapping("")
    public Result addFormData(@RequestBody FormDataProperty formDataProperty) {
        return formDataService.addFormData(formDataProperty);
    }

    /**
     * 上传附件
     *
     * @param file             上传的附件
     * @param formDataProperty
     * @return
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, FormDataProperty formDataProperty) {
        return formDataService.uploadFile(file, formDataProperty);
    }

    /**
     * 删除
     *
     * @param formDataId 表单数据编号
     * @param filePath   附件路径
     * @return
     */
    @DeleteMapping("")
    public Result delFormData(@RequestParam("formDataId") Long formDataId, @RequestParam("filePath") String filePath) {

        if (StringUtils.isNotBlank(filePath)) {
            DelFileUtil.delFolder(PathUtil.getProjectPath() + filePath.trim());
        }

        formDataService.delFormData(formDataId);

        return null;
    }

    /**
     * 删除附件
     *
     * @param formDataId 表单数据编号
     * @return
     */
    @DeleteMapping("/delFile/{formDataId}")
    public Result delFile(@PathVariable Long formDataId) {

        var entity = formDataService.findFormDatById(formDataId);

        if (entity != null && StringUtils.isNotBlank(entity.getFilePath())) {
            DelFileUtil.delFolder(PathUtil.getProjectPath() + entity.getFilePath().trim());
            entity.setFilePath("");

            FormDataProperty formDataProperty = new FormDataProperty();
            BeanUtils.copyProperties(entity, formDataProperty);

            formDataService.updateFormData(formDataProperty);
        }

        return Result.ok();
    }

    /**
     * 更新
     *
     * @param formDataProperty
     * @return
     */
    @PutMapping("")
    public Result updateFormData(@RequestBody FormDataProperty formDataProperty) {
        return formDataService.updateFormData(formDataProperty);
    }
}
