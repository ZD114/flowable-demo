package com.zd.flowable.utils;

/**
 * @description: 常量类
 * @auther: zd
 * @date: 2022/10/12
 **/
public class Constant {

    private Constant() {
    }

    public static final String SUCCESS = "success"; // 返回成功标志
    public static final String ERROR = "error"; //返回失败标志
    public static final String FILEPATH = "static/"; //工作流生成XML和PNG目录
    public static final String RESULT = "result";

    public static final String DEPLOYMENT_ID = "deploymentId"; //部署编号

    public static final String FILE_PATH = "uploadFiles/file/";					//文件上传路径

    /***************************************表单常量*****************************************/
    public static final String FORM_TEMPLATES_ID = "formTemplatesId"; //表单模板编号
    public static final String FORM_DATA_ID = "formDataId"; //表单数据编号
    public static final String FORM_HANG_ID = "formHangId"; //表单挂靠编号
    public static final String FORM_MY_ID = "formMyId"; //我的表单编号

}
