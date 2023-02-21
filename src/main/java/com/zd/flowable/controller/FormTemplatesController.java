package com.zd.flowable.controller;

import com.zd.flowable.common.PageResult;
import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.FormTemplatesProperty;
import com.zd.flowable.model.FormTemplatesSearchParam;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormCommonService;
import com.zd.flowable.service.FormTemplatesService;
import com.zd.flowable.utils.EasyExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    private FormTemplatesService formTemplateService;
    @Autowired
    private FormCommonService formCommonService;

    /**
     * 保存表单模板
     *
     * @param formTemplateProperty
     * @return
     */
    @PostMapping("")
    public Result addTemplate(@RequestBody FormTemplatesProperty formTemplateProperty) {
        return formTemplateService.addTemplate(formTemplateProperty);
    }

    /**
     * 删除表单模板
     *
     * @param id 模板编号
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delTemplate(@PathVariable String id) {
        return formTemplateService.delTemplate(id);
    }

    /**
     * 更新表单模板
     *
     * @param formTemplateProperty
     * @return
     */
    @PutMapping("")
    public Result updateTemplate(@RequestBody FormTemplatesProperty formTemplateProperty) {
        return formTemplateService.updateTemplate(formTemplateProperty);
    }

    /**
     * 表单模板列表
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/list")
    public PageResult<FormTemplates> searchPage(FormTemplatesSearchParam searchParam) {

        var pageResult = new PageResult<FormTemplates>();

        var params = new HashMap<String, Object>();

        var sql = new StringBuilder("SELECT * FROM form_templates WHERE 1=1 ");

        if (StringUtils.isNotBlank(searchParam.getTitle())) {
            sql.append(" AND title like :title");
            params.put("title", "%" + searchParam.getTitle() + "%");

        }

        if (StringUtils.isNotBlank(searchParam.getType())) {
            sql.append(" AND type := type");
            params.put("type", searchParam.getType());

        }

        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY id ) eq");

        var totalCount = formCommonService.countForm(sqlCount.toString(), params);

        if (totalCount > 0) {
            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY id ORDER BY id DESC LIMIT :start,:pageSize ");
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
    public RestResult<FormTemplates> findTemplateById(@PathVariable String id) {
        return formTemplateService.findTemplateById(id);
    }

    /**
     * 批量删除模板
     *
     * @param id 模板编号列表
     * @return
     */
    @PostMapping("/delBatch")
    public Result delTemplateBatch(@RequestBody String id) {
        return formTemplateService.delTemplateBatch(id);
    }

    /**
     * 导出excel
     *
     * @param fileName 文件名称
     * @param response
     */
    @GetMapping("/download")
    public void downloadExcel(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        EasyExcelUtil.downloadExcel(response, fileName, formTemplateService.queryAll());
    }
}
