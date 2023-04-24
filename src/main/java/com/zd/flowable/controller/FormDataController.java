package com.zd.flowable.controller;

import com.alibaba.fastjson.JSONArray;
import com.zd.flowable.common.PageResult;
import com.zd.flowable.common.RestResult;
import com.zd.flowable.entity.FormData;
import com.zd.flowable.model.*;
import com.zd.flowable.service.FormCommonService;
import com.zd.flowable.service.FormDataService;
import com.zd.flowable.service.FormHangService;
import com.zd.flowable.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private static final String INDEX_NAME = "form_data_index";

    @Autowired
    private FormDataService formDataService;
    @Autowired
    private FormHangService formHangService;
    @Autowired
    private FormCommonService formCommonService;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final Logger log = LoggerFactory.getLogger(FormDataController.class);

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
    public Result uploadFile(@RequestPart("file") MultipartFile file, @RequestPart @Validated FormDataProperty formDataProperty) {
        return formDataService.uploadFile(file, formDataProperty);
    }

    /**
     * 删除
     *
     * @param formDataId 表单数据编号
     * @return
     */
    @DeleteMapping("")
    public Result delFormData(@RequestParam("formDataId") String formDataId) {

        var formData = formDataService.findFormDataById(formDataId);
        var filePath = formData.getResult().getFilePath();

        if (StringUtils.isNotBlank(filePath)) {
            DelFileUtil.delFolder(System.getProperty(Constant.DIR) + filePath.trim());
        }

        formHangService.delHangByFormDataId(formDataId);
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
    public Result delFile(@PathVariable String formDataId) {

        var value = formDataService.findFormDataById(formDataId);
        var entity = value.getResult();

        if (entity != null && StringUtils.isNotBlank(entity.getFilePath())) {
            DelFileUtil.delFolder(System.getProperty(Constant.DIR) + entity.getFilePath().trim());
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

        var sqlCount = new StringBuilder("SELECT COUNT(1) FROM( " + sql + " GROUP BY id ) eq");

        var totalCount = formCommonService.countForm(sqlCount.toString(), params);

        log.info("总数量：{}", totalCount);

        if (totalCount > 0) {

            var start = searchParam.getPageIndex() == 0 ? 0
                    : (searchParam.getPageIndex() - 1) * searchParam.getPageSize();

            sql.append(" GROUP BY id ORDER BY id DESC LIMIT :start,:pageSize ");
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
    public RestResult<FormData> findFormDataById(@PathVariable String id) {
        return formDataService.findFormDataById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 表单数据编号
     * @return
     */
    @PostMapping("/delBatch")
    public Result delBatch(@RequestBody String ids) {
        return formDataService.delBatchFormData(ids);
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param response
     */
    @GetMapping("/download")
    public void downloadExcel(@RequestParam("fileName") String fileName, @RequestParam("filePath") String filePath, HttpServletResponse response) {
        try {
            FileDownload.fileDownload(response, PathUtil.getProjectPath() + filePath, fileName + filePath.substring(58));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加es
     *
     * @return
     */
    @GetMapping("/add-es")
    public Result addDataToEs() throws IOException {

        if (HighLevelClientUtils.indexExists(restHighLevelClient, INDEX_NAME)) {
            log.info("createIndex:{} 已存在", INDEX_NAME);
        } else {
            boolean index = HighLevelClientUtils.createIndex(restHighLevelClient, INDEX_NAME);

            if (!index) {
                log.info("createIndex:{} 失败", INDEX_NAME);
                return Result.error(ResultCodeEnum.CREATE_INDEX_ERROR);
            }
        }

        var list = formDataService.queryAll();

        if (list.size() > 0) {
            HighLevelClientUtils.updateDocs(restHighLevelClient, INDEX_NAME, JSONArray.toJSONString(list), Constant.ID);
            return Result.ok();
        }

        return Result.error(ResultCodeEnum.ERROR);
    }

    /**
     * 查询es
     * value：抛出指定异常才会重试
     * include：和value一样，默认为空，当exclude也为空时，默认所有异常
     * exclude：指定不处理的异常
     * maxAttempts：最大重试次数，默认3次
     * backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；
     * multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
     * @param searchParam
     * @return
     */
    @PostMapping("/query-es")
    @Retryable(value = Exception.class, backoff = @Backoff(delay = 2000,multiplier = 1.5))
    public Result queryEsData(@RequestBody FormDataSearchParam searchParam) throws IOException {
        var pageNo = searchParam.getPageIndex();
        var pageSize = searchParam.getPageSize();
        var startNum = (pageNo - 1) * pageSize;
        var title = searchParam.getTitle();

        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 全局模糊查询
        if (StringUtils.isNotBlank(title)) {
            WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery(FormDataEs.TITLE, "*" + title + "*");
            boolQuery.must(queryBuilder);
        } else {
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            boolQuery.must(matchAllQueryBuilder);
        }

        searchBuilder.query(boolQuery);
        searchBuilder.from(startNum);
        searchBuilder.size(pageSize);
        searchBuilder.sort(FormDataEs.ID, SortOrder.DESC);

        return Result.ok().data(Constant.RESULT, HighLevelClientUtils.search(restHighLevelClient, INDEX_NAME, searchBuilder));
    }


}
