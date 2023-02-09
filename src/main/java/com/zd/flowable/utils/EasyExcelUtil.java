package com.zd.flowable.utils;

import com.alibaba.excel.EasyExcel;
import com.zd.flowable.entity.FormTemplates;
import com.zd.flowable.model.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * excel处理类
 *
 * @author zhangda
 * @date: 2023/2/9
 **/
public class EasyExcelUtil {

    public static void downloadExcel(HttpServletResponse response, String fileName, List<?> list) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), FormTemplates.class).sheet(fileName).doWrite(list);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
