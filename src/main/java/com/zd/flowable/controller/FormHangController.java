package com.zd.flowable.controller;

import com.zd.flowable.common.PageResult;
import com.zd.flowable.entity.FormHang;
import com.zd.flowable.model.FormHangProperty;
import com.zd.flowable.model.FormHangSearchParam;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormHangService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 挂靠表单
 *
 * @author zhangda
 * @date: 2023/2/13
 **/
@RestController
@RequestMapping("form-hang")
public class FormHangController {

    @Autowired
    private FormHangService formHangService;

    private static final Logger log = LoggerFactory.getLogger(FormHangController.class);

    /**
     * 保存
     *
     * @param formHangProperty
     * @return
     */
    @PostMapping("")
    public Result addFormHang(@RequestBody FormHangProperty formHangProperty) {
        return formHangService.addFormHang(formHangProperty);
    }

    /**
     * 删除
     *
     * @param id 表单挂靠编号
     * @return
     */
    @DeleteMapping("id")
    public Result delFormHang(@PathVariable String id) {
        return formHangService.delFormHang(id);
    }

    /**
     * 更新
     *
     * @param formHangProperty
     * @return
     */
    @PutMapping("")
    public Result updateFormHang(@RequestBody FormHangProperty formHangProperty) {
        return formHangService.updateFormHang(formHangProperty);
    }

    /**
     * 列表
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/list")
    public PageResult<FormHang> searchPage(@RequestBody FormHangSearchParam searchParam) {
        var pageResult = new PageResult<FormHang>();

        var params = new HashMap<String, Object>();

        var sql = new StringBuilder("SELECT * FROM form_hang WHERE 1=1 ");

        if (StringUtils.isNotBlank(searchParam.getUserName())) {
            sql.append(" AND user_name like :userName ");
            params.put("userName", "%" + searchParam.getUserName() + "%");

        }

        if (StringUtils.isNotBlank(searchParam.getHangName())) {
            sql.append(" AND hang_name like :hangName ");
            params.put("hangName", "%" + searchParam.getHangName() + "%");

        }

        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY form_hang_id ) eq");

        var totalCount = formHangService.countFormHang(sqlCount.toString(), params);

        log.info("挂靠总数量：{}", totalCount);

        if (totalCount > 0) {

            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY form_templates_id ORDER BY form_templates_id DESC LIMIT :start,:pageSize ");
            params.put("start", start);
            params.put("pageSize", searchParam.getPageSize());

            var resources = formHangService.searchPageList(sql.toString(), params);

            pageResult.setData(resources);
        }

        pageResult.setTotal(totalCount);
        pageResult.setTotalPages((long) Math.ceil(totalCount / (double) searchParam.getPageSize()));

        return pageResult;
    }

    /**
     * 根据挂靠编号查询
     *
     * @param id 挂靠编号
     * @return
     */
    @GetMapping("/{id}")
    public FormHang findFormHangById(@PathVariable String id) {
        return formHangService.findFormHangById(id);
    }
}
