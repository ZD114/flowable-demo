package com.zd.flowable.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description: 文件下载
 * @auther: zd
 * @date: 2023/02/13
 **/
public class FileDownload {

    private static final Logger log = LoggerFactory.getLogger(FileDownload.class);

    /**
     * 下载文件
     *
     * @param response
     * @param filePath //文件完整路径(包括文件名和扩展名)
     * @param fileName //下载后看到的文件名
     * @return 文件名
     */
    public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception {
        byte[] data = FileUtil.toByteArray2(filePath);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
        response.flushBuffer();
    }

    /**
     * 下载
     *
     * @param downloadList
     */
    static void download(Vector<String> downloadList) {
        // 线程池
        ExecutorService pool = null;
        HttpURLConnection connection = null;
        //循环下载
        try {
            for (int i = 0; i < downloadList.size(); i++) {
                pool = Executors.newCachedThreadPool();
                final String url = downloadList.get(i);
                String filename = getFilename(downloadList.get(i));

                log.info("正在下载第{}个文件，地址：{}", (i + 1), url);

                Future<HttpURLConnection> future = pool.submit(new Callable<HttpURLConnection>() {
                    @Override
                    public HttpURLConnection call() throws Exception {
                        HttpURLConnection connection = null;
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setConnectTimeout(10000);//连接超时时间
                        connection.setReadTimeout(10000);// 读取超时时间
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        return connection;
                    }
                });
                connection = future.get();

                log.info("下载完成.响应码: {}", connection.getResponseCode());

                // 写入文件
                writeFile(new BufferedInputStream(connection.getInputStream()), URLDecoder.decode(filename, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection)
                connection.disconnect();
            if (null != pool)
                pool.shutdown();
        }
    }

    /**
     * 通过截取URL地址获得文件名
     * 注意：还有一种下载地址是没有文件后缀的，这个需要通过响应头中的
     * Content-Disposition字段 获得filename，一般格式为："attachment; filename=\xxx.exe\"
     *
     * @param url
     * @return
     */
    static String getFilename(String url) {
        return ("".equals(url) || null == url) ? "" : url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    /**
     * 写入文件
     *
     * @param bufferedInputStream
     * @param filename            文件名
     */
    static void writeFile(BufferedInputStream bufferedInputStream, String filename) {
        //创建本地文件
        File destFileFile = new File("D:\\TestDownloads\\" + filename);
        if (!destFileFile.getParentFile().exists()) {
            destFileFile.getParentFile().mkdir();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destFileFile);
            byte[] b = new byte[1024];
            int len = 0;

            // 写入文件
            while ((len = bufferedInputStream.read(b, 0, b.length)) != -1) {

                fileOutputStream.write(b, 0, len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if (null != bufferedInputStream)
                    bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
