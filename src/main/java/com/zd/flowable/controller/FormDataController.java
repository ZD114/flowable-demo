package com.zd.flowable.controller;

import com.zd.flowable.common.PageResult;
import com.zd.flowable.entity.FormData;
import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.FormDataSearchParam;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormDataService;
import com.zd.flowable.utils.DelFileUtil;
import com.zd.flowable.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

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

    private static final Logger log = LoggerFactory.getLogger(FormDataService.class);

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

        var entity = formDataService.findFormDataById(formDataId);

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

    /**
     * 列表
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/list")
    public PageResult<FormData> searchPage(@RequestBody FormDataSearchParam searchParam) {
        var pageResult = new PageResult<FormData>();

        var params = new HashMap<String, Object>();

        var sql = new StringBuilder("SELECT * FROM form_data WHERE 1=1 ");

        if (StringUtils.isNotBlank(searchParam.getTitle())) {
            sql.append(" AND title like :title ");
            params.put("title", "%" + searchParam.getTitle() + "%");

        }

        if (StringUtils.isNotBlank(searchParam.getUserName())) {
            sql.append(" AND user_name like :userName ");
            params.put("userName", "%" + searchParam.getUserName() + "%");

        }

        if (searchParam.getIsEdit() > 0) {
            sql.append(" AND is_edit := isEdit ");
            params.put("isEdit", searchParam.getIsEdit());

        }

        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY form_data_id ) eq");

        var totalCount = formDataService.countFormData(sqlCount.toString(), params);

        log.info("总数量：{}", totalCount);

        if (totalCount > 0) {

            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY form_templates_id ORDER BY form_templates_id DESC LIMIT :start,:pageSize ");
            params.put("start", start);
            params.put("pageSize", searchParam.getPageSize());

            var resources = formDataService.searchPageList(sql.toString(), params);

            pageResult.setData(resources);
        }

        pageResult.setTotal(totalCount);
        pageResult.setTotalPages((long) Math.ceil(totalCount / (double) searchParam.getPageSize()));

        return pageResult;
    }

    /**
     * 根据表单数据编号查询对象
     *
     * @param id 表单数据编号
     * @return
     */
    @GetMapping("/{id}")
    public FormData findFormDataById(@PathVariable Long id) {
        return formDataService.findFormDataById(id);
    }
}
