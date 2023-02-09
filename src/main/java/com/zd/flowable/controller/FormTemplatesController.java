package com.zd.flowable.controller;

import com.zd.flowable.common.PageResult;
import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormTemplateProperty;
import com.zd.flowable.model.FormTemplateSearchParam;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormTemplateService;
import net.logstash.logback.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 自定义表单模板
 *
 * @author zhangda
 * @date: 2023/2/6
 **/
@RestController
@RequestMapping("/form-template")
public class FormTemplatesController {

    @Autowired
    private FormTemplateService formTemplateService;

    /**
     * 保存表单模板
     *
     * @param formTemplateProperty
     * @return
     */
    @PostMapping("")
    public Result addTemplate(@RequestBody FormTemplateProperty formTemplateProperty) {
        return formTemplateService.addTemplate(formTemplateProperty);
    }

    /**
     * 删除表单模板
     *
     * @param id 模板编号
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delTemplate(@PathVariable Long id) {
        return formTemplateService.delTemplate(id);
    }

    /**
     * 更新表单模板
     *
     * @param formTemplateProperty
     * @return
     */
    @PutMapping("")
    public Result updateTemplate(@RequestBody FormTemplateProperty formTemplateProperty) {
        return formTemplateService.updateTemplate(formTemplateProperty);
    }

    /**
     * 表单模板列表
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/list")
    public PageResult<FormTemplateProperty> searchPage(FormTemplateSearchParam searchParam) {

        var pageResult = new PageResult<FormTemplateProperty>();

        var params = new HashMap<String, Object>();

        var sql = new StringBuilder("SELECT * FROM form_templates WHERE 1=1 ");

        if (!StringUtils.isEmpty(searchParam.getTitle())) {
            sql.append(" AND title like :title");
            params.put("title", "%" + searchParam.getTitle() + "%");

        }

        if (!StringUtils.isEmpty(searchParam.getType())) {
            sql.append(" AND type := type");
            params.put("type", searchParam.getType());

        }

        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY form_templates_id ) eq");

        var totalCount = formTemplateService.countTemplate(sqlCount.toString(), params);

        if (totalCount > 0) {
            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY form_templates_id ORDER BY form_templates_id DESC LIMIT :start,:pageSize ");
            params.put("start", start);
            params.put("pageSize", searchParam.getPageSize());

            var resources = formTemplateService.searchPageList(sql.toString(), params);

            pageResult.setData(resources);
        }

        pageResult.setTotal(totalCount);
        pageResult.setTotalPages((long) Math.ceil(totalCount / (double) searchParam.getPageSize()));

        return pageResult;
    }

    /**
     * 根据编号查询模板信息
     *
     * @param id 模板编号
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<FormTemplates> findTemplateById(@PathVariable Long id) {
        return formTemplateService.findTemplateById(id);
    }
}
