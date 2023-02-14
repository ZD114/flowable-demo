package com.zd.flowable.service.impl;

import com.zd.flowable.entity.FormData;
import com.zd.flowable.model.FormDataProperty;
import com.zd.flowable.model.FormMyProperty;
import com.zd.flowable.model.Result;
import com.zd.flowable.service.FormDataService;
import com.zd.flowable.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangda
 * @date: 2023/2/10
 **/
@Service
public class FormDataServiceImpl implements FormDataService {

    @Resource
    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Override
    public Result addFormData(FormDataProperty formDataProperty) {
        var entity = new FormData();
        var now = LocalDateTime.now();

        BeanUtils.copyProperties(formDataProperty, entity, Constant.FORM_DATA_ID);

        entity.setFormDataId(UuidUtil.get32UUID());
        entity.setUpdateTime(now);
        entity.setCreateTime(now);

        nameJdbcTemplate.update(JdbcUtility.getInsertSql(entity, true, Constant.FORM_DATA_ID),
                JdbcUtility.getSqlParameterSource(entity, Constant.FORM_DATA_ID));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Result uploadFile(MultipartFile file, FormDataProperty formDataProperty) {

        String pathDate = LocalDateTime.now().toString(), fileName = "";

        String filePath = PathUtil.getProjectPath() + Constant.FILE_PATH + pathDate;    //文件上传路径

        fileName = FileUpload.fileUp(file, filePath, UuidUtil.get32UUID());                //执行上传

        var entity = new FormData();
        BeanUtils.copyProperties(formDataProperty, entity);

        entity.setUpdateTime(LocalDateTime.now());
        entity.setFilePath(Constant.FILE_PATH + pathDate + "/" + fileName);

        nameJdbcTemplate.update(JdbcUtility.getUpdateSql(entity), JdbcUtility.getSqlParameterSource(entity));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Result delFormData(String formDataId) {
        var param = new HashMap<String, Object>();

        param.put("id", formDataId);

        nameJdbcTemplate.update("DELETE FROM form_data WHERE form_data_id = :id", param);

        return Result.ok();
    }

    @Override
    public FormData findFormDataById(String formDataId) {
        var param = new HashMap<String, Object>();

        param.put("id", formDataId);

        return nameJdbcTemplate.queryForObject("select * from form_data where form_data_id = :id", param, FormData.class);
    }

    @Override
    public Result updateFormData(FormDataProperty formDataProperty) {

        var entity = new FormData();
        BeanUtils.copyProperties(formDataProperty, entity);

        entity.setUpdateTime(LocalDateTime.now());

        nameJdbcTemplate.update(JdbcUtility.getUpdateSql(entity), JdbcUtility.getSqlParameterSource(entity));

        return Result.ok().data(Constant.RESULT, entity);
    }

    @Override
    public Integer countFormData(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public List<FormData> searchPageList(String sql, Map<String, Object> params) {
        return nameJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(FormData.class));
    }

    @Override
    public Result delBatchFormData(String ids) {
        List<FormData> list = new ArrayList<>();
        var realIds = ids.split(",");

        for (int i = 0; i < realIds.length; i++) {
            FormData fd = new FormData();
            fd.setFormDataId(realIds[i]);
            list.add(fd);
        }

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list);

        // 批量删除挂靠记录和数据
        nameJdbcTemplate.batchUpdate("DELETE FROM form_hang WHERE form_data_id = :formDataId", batch);
        nameJdbcTemplate.batchUpdate("DELETE FROM form_data WHERE form_data_id = :formDataId", batch);

        return Result.ok();
    }

    @Override
    public List<FormData> queryAll() {
        return nameJdbcTemplate.query("select * from form_data", new BeanPropertyRowMapper<>(FormData.class));
    }

    @Override
    public Result updateByFormMyId(FormMyProperty formMyProperty) {
        var param = new HashMap<String, Object>();

        param.put("isImage", formMyProperty.getIsImage());
        param.put("isFile", formMyProperty.getIsFile());
        param.put("isFwb", formMyProperty.getIsFwb());
        param.put("isLc", formMyProperty.getIsLc());
        param.put("dataPrivate", formMyProperty.getDataPrivate());
        param.put("updateTime", LocalDateTime.now());
        param.put("formMyId", formMyProperty.getFormMyId());

        var sql = new StringBuilder("update form_data set is_image = :isImage, is_file = :isFile, ");
        sql.append("is_fwb = :isFwb, is_lc = :isLc, data_private = :dataPrivate, update_time = updateTime ");
        sql.append("where my_form_id = :formMyId");

        nameJdbcTemplate.update(sql.toString(), param);

        return Result.ok();
    }
}
