package com.zd.flowable.controller.common;

import com.zd.flowable.model.Result;
import com.zd.flowable.model.ResultCodeEnum;
import com.zd.flowable.utils.HighLevelClientUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ES通用处理
 *
 * @author zhangda
 * @date: 2023/4/19
 **/
@RestController
@RequestMapping("/es-common")
public class ESController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 删除索引
     *
     * @param index 索引名称
     * @return
     */
    @DeleteMapping("/{index}")
    public Result delEsIndex(@PathVariable String index) {
        var flag = HighLevelClientUtils.deleteIndex(restHighLevelClient, index);
        if (flag){
            return Result.ok();
        }

        return Result.error(ResultCodeEnum.DATA_PROCESS_ERROR);
    }
}
