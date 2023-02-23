package com.zd.flowable.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * excel处理类
 *
 * @author zhangda
 * @date: 2023/2/9
 **/
public class EasyExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(EasyExcelUtil.class);

    private EasyExcelUtil() {}

    /**
     * excel导出
     *
     * @param response
     * @param fileName
     * @param list
     */
    public static void
            downloadExcel(HttpServletResponse response, String fileName, List<?> list, Class<?> object) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(Constant.UTF8);

        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, Constant.UTF8) + ".xlsx");
            EasyExcelFactory.write(response.getOutputStream(), object).sheet(fileName).doWrite(list);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * excel上传
     *
     * @param file
     * @param object
     * @return
     * @throws IOException
     */
    public static String uploadExcel(MultipartFile file, Class<?> object) throws IOException {
        // 获取上传的文件流
        InputStream inputStream = file.getInputStream();

        // 读取Excel
        EasyExcelFactory.read(inputStream, object,
                new AnalysisEventListener() {
                    @Override
                    public void invoke(Object o, AnalysisContext analysisContext) {
                        log.info("解析数据为:{}", o);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        log.info("解析完成...");
                    }
                }
        ).sheet().doRead();

        return "上传Excel文件成功";
    }

    /**
     * 导出多个sheet到多个excel文件，并压缩到一个zip文件
     *
     * @param zipFilename 下载时压缩包名称
     * @param response    请求返回流
     * @param header      excel实体类类型
     * @param splitTimes  次数分界，excel数据导入多少次后创建下一个excel文件
     * @param supplier    供给侧函数式接口，提供get方法，该方法可自定义
     * @return void
     */
    public static <T> void exportZip(String zipFilename, HttpServletResponse response, Class<T> header, int splitTimes, Supplier<List<T>> supplier) {
        if (zipFilename == null || zipFilename.isEmpty()) {
            zipFilename = "export";
        } else if (zipFilename.toLowerCase(Locale.ROOT).endsWith(".zip")) {
            zipFilename = zipFilename.substring(0, zipFilename.length() - 3);
        }
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            String downFileName = URLEncoder.encode(zipFilename, Constant.UTF8);
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + downFileName + ".zip");
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");
            //开始存入
            try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
                List<T> exportData = null;
                int count = 0;
                int fileIndex = 1;

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ExcelWriterBuilder builder = EasyExcelFactory.write(outputStream).autoCloseStream(false)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
                ExcelWriter excelWriter = builder.build();

                zipOut.putNextEntry(new ZipEntry(String.format("%s-%d.xls", zipFilename, fileIndex)));
                WriteSheet writeSheet = EasyExcelFactory.writerSheet(zipFilename).head(header).build();
                exportData = supplier.get();

                if (exportData == null) {
                    throw new NullPointerException("导出数据为空");
                }

                while (exportData != null) {
                    count += 1;
                    excelWriter.write(exportData, writeSheet);
                    exportData = supplier.get();
                    //如果到了分割点，则证明该excel文件已经到达存储临界值，直接保存该文件
                    if (count % splitTimes == 0) {
                        fileIndex += 1;
                        excelWriter.finish();
                        outputStream.writeTo(zipOut);
                        zipOut.closeEntry();

                        //如果导出数据不为空则证明存在下一个excel文件，则在压缩包中新增一个excel文件
                        if (exportData != null) {
                            outputStream = new ByteArrayOutputStream();
                            builder = EasyExcelFactory.write(outputStream).autoCloseStream(false)
                                    // 自动适配
                                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
                            excelWriter = builder.build();
                            zipOut.putNextEntry(new ZipEntry(String.format("%s-%d.xls", zipFilename, fileIndex)));
                            writeSheet = EasyExcelFactory.writerSheet(zipFilename).head(header).build();
                        }
                    }
                }

                //count % splitTimes == 0 代表本次导出刚好导出整数个文件，且每个文件中都有规定大小的数据
                if (count % splitTimes == 0) {
                    return;
                }

                excelWriter.finish();
                outputStream.writeTo(zipOut);
                zipOut.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("导出excel异常", e);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("导出excel异常");
        }
    }

}
