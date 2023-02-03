package com.zd.flowable.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 图片和base64转换
 * @auther: zd
 * @date: 2022/10/13
 **/
public class Base64Utils {

    private Base64Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 将输入流转为base64字符串
     *
     * @param in
     * @return
     */
    public static String getImageBase64(InputStream in) {
        byte[] data = null;
        try {
            data = new byte[in.available()];

            while (in.read(data) > 0) {
                break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }

    /**
     * 根据路径转换base64
     *
     * @param imgSrcPath
     * @return
     */
    public static String getImageStr(String imgSrcPath) throws IOException {
        byte[] data = null;
        try (InputStream in = new FileInputStream(imgSrcPath)) {
            data = new byte[in.available()];

            while (in.read(data) > 0) {
                break;
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串
    }
}
