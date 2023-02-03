package com.zd.flowable.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @description: 删除文件工具类
 * @auther: zd
 * @date: 2022/10/10
 **/
public class DelFileUtil {

    private DelFileUtil() {
    }

    /**
     * 只删除此路径的最末路径下所有文件和文件夹
     *
     * @param folderPath 文件路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);    // 删除完里面所有内容
            Files.delete(Path.of(folderPath)); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     */
    public static boolean delAllFile(String path) throws IOException {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        String realPath = "";//真实路径
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                realPath = path + tempList[i];
                temp = new File(realPath);
            } else {
                realPath = path + File.separator + tempList[i];
                temp = new File(realPath);
            }
            if (temp.isFile()) {
                Files.delete(Path.of(realPath));
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);    // 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);    // 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

}
