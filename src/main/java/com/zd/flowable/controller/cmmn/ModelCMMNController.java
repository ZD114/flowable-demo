package com.zd.flowable.controller.cmmn;

import org.flowable.ui.modeler.rest.app.AbstractModelCmmnResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 案例模型管理
 *
 * @author zhangda
 * @date: 2023/4/12
 **/
@RestController
@RequestMapping("/cmmn")
public class ModelCMMNController extends AbstractModelCmmnResource {

    /**
     * 获取cmmn的xml文件
     *
     * @param caseModelId
     * @param response
     * @throws IOException
     */
    @GetMapping("/{caseModelId}")
    public void getProcessModelCmmnXml(@PathVariable String caseModelId, HttpServletResponse response) throws IOException {
        super.getCaseModelCmmnXml(response, caseModelId);
    }
}
