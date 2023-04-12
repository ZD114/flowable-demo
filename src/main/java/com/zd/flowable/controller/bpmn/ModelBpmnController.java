package com.zd.flowable.controller.bpmn;

import com.zd.flowable.model.Result;
import org.flowable.ui.modeler.rest.app.AbstractModelBpmnResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 模型bpmn管理类
 *
 * @author zhangda
 * @date: 2023/4/12
 **/
@RestController
@RequestMapping("/bpmn")
public class ModelBpmnController extends AbstractModelBpmnResource {

    /**
     * 获取bpmn的xml
     *
     * @param modelId 模型编号
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/{modelId}")
    public void getProcessModelBpmn20Xml(@PathVariable String modelId, HttpServletResponse response) throws IOException {
        super.getProcessModelBpmn20Xml(response, modelId);
    }

}
