package com.zd.flowable.controller;

import com.zd.flowable.common.PageResult;
import com.zd.flowable.entity.FormMy;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.FormMySearchParam;
import com.zd.flowable.model.Result;
import com.zd.flowable.model.ResultCodeEnum;
import com.zd.flowable.service.FormDataService;
import com.zd.flowable.service.FormMyService;
import com.zd.flowable.service.FormTemplatesService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 我的表单管理
 *
 * @author zhangda
 * @date: 2023/2/14
 **/
@RestController
@RequestMapping("/form-my")
public class FormMyController {

    @Autowired
    private FormMyService formMyService;
    @Autowired
    private FormTemplatesService formTemplatesService;
    @Autowired
    private FormDataService formDataService;

    private static final Logger log = LoggerFactory.getLogger(FormMyController.class);

    /**
     * 保存我的表单
     *
     * @param formMyProperty
     * @return
     */
    @PostMapping("")
    public Result addFormMy(@RequestBody FormMyProperty formMyProperty) {

        if (StringUtils.isNotBlank(formMyProperty.getFormTemplatesId())) {
            var formTemplates = formTemplatesService.findTemplateById(formMyProperty.getFormTemplatesId());
            formMyProperty.setHtmlContent(formTemplates.getResult().getHtmlContent());
        }

        return formMyService.addFormMy(formMyProperty);
    }

    /**
     * 删除
     *
     * @param id 我的表单编号
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delFormMy(@PathVariable String id) {
        return formMyService.delFormMy(id);
    }

    /**
     * 更新
     *
     * @param formMyProperty
     * @return
     */
    @PutMapping("")
    public Result updateFormMy(@RequestBody FormMyProperty formMyProperty) {

        if (StringUtils.isNotBlank(formMyProperty.getFormTemplatesId())) {
            var formTemplates = formTemplatesService.findTemplateById(formMyProperty.getFormTemplatesId());
            formMyProperty.setHtmlContent(formTemplates.getResult().getHtmlContent());
        }

        // 更新表单数据表中规则
        var result = formDataService.updateByFormMyId(formMyProperty);

        if (!result.getSuccess()) {
            return Result.error(ResultCodeEnum.ERROR);
        }

        return formMyService.updateFormMy(formMyProperty);
    }

    /**
     * 列表
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/list")
    public PageResult<FormMy> searchPage(FormMySearchParam searchParam) {

        var pageResult = new PageResult<FormMy>();

        var params = new HashMap<String, Object>();

        var sql = new StringBuilder("SELECT * FROM form_my WHERE 1=1 ");

        if (StringUtils.isNotBlank(searchParam.getTitle())) {
            sql.append(" AND title like :title");
            params.put("title", "%" + searchParam.getTitle() + "%");

        }

        if (StringUtils.isNotBlank(searchParam.getUserName())) {
            sql.append(" AND user_name like :userName");
            params.put("userName", "%" + searchParam.getUserName() + "%");
        }

        if (searchParam.getIsFile() > 0) {
            sql.append(" AND is_file := isFile ");
            params.put("isFile", searchParam.getIsFile());

        }

        if (searchParam.getIsImage() > 0) {
            sql.append(" AND is_image := isImage ");
            params.put("isImage", searchParam.getIsImage());

        }

        if (searchParam.getIsPrivate() > 0) {
            sql.append(" AND is_private := isPrivate ");
            params.put("isPrivate", searchParam.getIsPrivate());

        }
        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY form_my_id ) eq");

        var totalCount = formMyService.countFormMy(sqlCount.toString(), params);

        log.info("我的表单总数量：{}", totalCount);

        if (totalCount > 0) {

            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY form_templates_id ORDER BY form_templates_id DESC LIMIT :start,:pageSize ");
            params.put("start", start);
            params.put("pageSize", searchParam.getPageSize());

            var resources = formMyService.searchPageList(sql.toString(), params);

            pageResult.setData(resources);
        }

        pageResult.setTotal(totalCount);
        pageResult.setTotalPages((long) Math.ceil(totalCount / (double) searchParam.getPageSize()));

        return pageResult;
    }

    /**
     * 根据编号查询我的表单
     *
     * @param id 我的表单编号
     * @return
     */
    @GetMapping("/{id}")
    public FormMy findFormMyById(@PathVariable String id) {
        return formMyService.findFormMyById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 我的表单编号列表
     * @return
     */
    @PostMapping("/delBatch")
    public Result delBatch(@RequestBody List<String> ids) {
        return formMyService.delBatchFormMy(ids);
    }
}
