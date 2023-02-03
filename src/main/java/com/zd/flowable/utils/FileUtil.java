package com.zd.flowable.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description: 文件处理类
 * @auther: zd
 * @date: 2022/10/13
 **/
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil(){}

    /**
     * 读取Projectpath某文件里的全部内容
     * @param fileP  文件路径
     */
    public static String readFileAllContent(String fileP) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        String encoding = "utf-8";
        File file = new File(PathUtil.getProjectpath() + fileP);//文件路径(先在本地)
        if (file.isFile() && file.exists()) { 		// 判断文件是否存在
            try(InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read)){
                //异常处理块
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    fileContent.append(lineTxt);
                    fileContent.append("\n");
                }
            }catch (NullPointerException e){
                logger.error("读取文件内容出错",e);
            }
        }else{
            logger.info("找不到指定的文件,查看此路径是否正确:{}", fileP);
        }
        return fileContent.toString();
    }

    /**
     * 读取到字节数组2
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray2(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        try(FileInputStream fs = new FileInputStream(f);
            FileChannel channel = fs.getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                logger.info("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
