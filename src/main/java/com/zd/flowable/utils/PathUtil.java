package com.zd.flowable.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @description: 路径工具类
 * @auther: zd
 * @date: 2022/10/10
 **/
public class PathUtil {

    private PathUtil() {
    }

    /**
     * 获取Projectpath
     *
     * @return
     */
    public static String getProjectPath() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            throw new NullPointerException();
        }

        HttpServletRequest request = null;
        String result = "";
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();

            if (ra != null) {
                request = ((ServletRequestAttributes) ra).getRequest();
            }

            if (request != null) {
                result = request.getServletContext().getRealPath("/").replace("%20", " ").replace("file:/", "").trim();
            }

            return result;
        } catch (NullPointerException e) {
            return result;
        }
    }

    /**
     * 获取Classpath
     *
     * @return
     */
    public static String getClasspath() {
        String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))).replace("file:/", "").replace("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;

        }
        //path = "H:\\";  //当项目以jar、war包运行时，路径改成实际硬盘位置
        return path;
    }

}
