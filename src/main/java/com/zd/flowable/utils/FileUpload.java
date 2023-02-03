package com.zd.flowable.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: 文件上传工具类
 * @auther: zd
 * @date: 2022/10/10
 **/
public class FileUpload {

    private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);
    private FileUpload(){}

    /**上传文件
     * @param file 			//文件对象
     * @param filePath		//上传路径
     * @param fileName		//文件名
     * @return  文件名
     */
    public static String fileUp(MultipartFile file, String filePath, String fileName){
        String extName = ""; // 扩展名格式：
        String realFileName = file.getOriginalFilename();
        if(realFileName == null){
            throw new NullPointerException("file name is null");
        }
        try {
            if (realFileName.lastIndexOf(".") >= 0){
                extName = realFileName.substring(realFileName.lastIndexOf("."));
            }
            String realName = copyFile(file.getInputStream(), filePath, fileName+extName).replace("-", "");
            logger.info("文件名：{}", realName);
        } catch (IOException e) {
            logger.error("file not exists",e);
        }
        return fileName+extName;
    }

    /**
     * 写文件到当前目录的upload目录中
     * @param in
     * @param dir
     * @param realName
     * @return
     * @throws IOException
     */
    public static String copyFile(InputStream in, String dir, String realName)
            throws IOException {
        File file = mkdirsmy(dir,realName);
        FileUtils.copyInputStreamToFile(in, file);
        in.close();
        return realName;
    }


    /**判断路径是否存在，否：创建此路径
     * @param dir  文件路径
     * @param realName  文件名
     * @throws IOException
     */
    public static File mkdirsmy(String dir, String realName) throws IOException{
        File file = new File(dir, realName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            boolean msg = file.createNewFile();
            logger.info("创建新文件是否成功：{}", msg);
        }
        return file;
    }


    /**下载网络图片上传到服务器上
     * @param httpUrl 图片网络地址
     * @param filePath	图片保存路径
     * @param myFileName  图片文件名(null时用网络图片原名)
     * @return	返回图片名称
     */
    public static String getHtmlPicture(String httpUrl, String filePath , String myFileName) throws IOException {

        URL url; //定义URL对象url
        String fileName = null == myFileName?httpUrl.substring(httpUrl.lastIndexOf("/")).replace("/", ""):myFileName; //图片文件名(null时用网络图片原名)
        url = new URL(httpUrl);	//初始化url对象
        File myFile = mkdirsmy(filePath,fileName);
        try(BufferedInputStream in = new BufferedInputStream(url.openStream());
                FileOutputStream file = new FileOutputStream(myFile)) { //初始化in对象，也就是获得url字节流
            int t;
            while ((t = in.read()) != -1) {
                file.write(t);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}
