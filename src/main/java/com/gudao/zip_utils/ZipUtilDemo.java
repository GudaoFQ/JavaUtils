package com.gudao.zip_utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Author : GuDao
 * 2020-11-17
 */

@RestController
@RequestMapping("/test")
public class ZipUtilDemo {

    @GetMapping("zip")
    public void downLoadZip(HttpServletResponse response) throws Exception {
        //需要读取文件的完整路径 TODO 需要指定为自己的
        String filePath ="E:\\Deptstop\\Java并发体系知识导图笔记.xmind";
        //创建ByteArrayOutputStream 存储压缩流，后期用于读出压缩文件流内容
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        /**
         * 创建ZipOutputStream 用于生成压缩文件流
         * 这里为什么用ByteArrayOutputStream对象构造ZipOutputStream
         * 翻看ZipOutputStream构造函数的源码，你会发现
         * ZipOutputStream(OutputStream out) out参数的注释为
         * out参数的注释为 the actual output stream
         * 也就是说out对象是压缩文件的实际输出流
         */
        ZipOutputStream zos = new ZipOutputStream(bos);
        //创建压缩文件内容实例，这里我就创建一个，
        // 如果大家需要创建多个可多次putNextEntry TODO 需要修改为自己的文件
        zos.putNextEntry(new ZipEntry("Java并发体系知识导图笔记.xmind"));
        //读取文件内容，并将文件内容放到压缩流中
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
        }
        //关闭流
        fis.close();
        zos.close(); //切记要先关闭流，不然后续无法获取压缩流内容
        //获取压缩字节流，到这里就明白为什么使用ByteArrayOutputStream构造ZipOutputStream了吧
        byte[] zipBytes = bos.toByteArray();
        //下面就是下载的那一套了
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-msdownload");
        // TODO 为压缩文件命名
        String filename = new String("压缩文件下载.zip".getBytes(),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment;filename=" + filename);
        /**
         * 由于需要向响应体中放入字节流 所以要放入response.getOutputStream();
         * 千万不要习惯性的使用response.getWriter()哦
         */
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(zipBytes);
        servletOutputStream.close();
    }
}
