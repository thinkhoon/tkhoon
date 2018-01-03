package com.tkhoon.framework.helper;

import com.tkhoon.framework.FrameworkConstant;
import com.tkhoon.framework.bean.Multipart;
import com.tkhoon.framework.exception.UploadException;
import com.tkhoon.framework.util.FileUtil;
import com.tkhoon.framework.util.StreamUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadHelper {

    private static final Logger logger = LoggerFactory.getLogger(UploadHelper.class);

    // 获取上传限制
    private static final int uploadLimit = ConfigHelper.getNumberProperty(FrameworkConstant.APP_UPLOAD_LIMIT);

    // 定义一个 FileUpload 对象（用于解析所上传的文件）
    private static ServletFileUpload fileUpload;

    public static void init(ServletContext servletContext) {
        // 获取一个临时目录（使用 Tomcat 的 work 目录）
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        // 创建 FileUpload 对象
        fileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        // 设置上传限制
        if (uploadLimit != 0) {
            fileUpload.setFileSizeMax(uploadLimit * 1024 * 1024); // 单位为 M
            if (logger.isDebugEnabled()) {
                logger.debug("[Smart] limit of uploading: {}M", uploadLimit);
            }
        }
    }

    public static boolean isMultipart(HttpServletRequest request) {
        // 判断上传文件的内容是否为 multipart 类型
        return ServletFileUpload.isMultipartContent(request);
    }

    public static List<Object> createMultipartParamList(HttpServletRequest request) throws Exception {
        // 定义参数列表
        List<Object> paramList = new ArrayList<Object>();
        // 创建两个对象，分别对应 普通字段 与 文件字段
        Map<String, String> fieldMap = new HashMap<String, String>();
        List<Multipart> multipartList = new ArrayList<Multipart>();
        // 获取并遍历表单项
        List<FileItem> fileItemList;
        try {
            fileItemList = fileUpload.parseRequest(request);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            // 异常转换（抛出自定义异常）
            throw new UploadException(e);
        }
        for (FileItem fileItem : fileItemList) {
            // 分两种情况处理表单项
            String fieldName = fileItem.getFieldName();
            if (fileItem.isFormField()) {
                // 处理普通字段
                String fieldValue = fileItem.getString(FrameworkConstant.DEFAULT_CHARSET);
                fieldMap.put(fieldName, fieldValue);
            } else {
                // 处理文件字段
                String originalFileName = FileUtil.getRealFileName(fileItem.getName());
                String uploadedFileName = FileUtil.getEncodedFileName(originalFileName);
                String contentType = fileItem.getContentType();
                long fileSize = fileItem.getSize();
                InputStream inputSteam = fileItem.getInputStream();
                // 创建 Multipart 对象，并将其添加到 multipartList 中
                Multipart multipart = new Multipart(uploadedFileName, contentType, fileSize, inputSteam);
                multipartList.add(multipart);
                // 将所上传文件的文件名存入 fieldMap 中
                fieldMap.put(fieldName, uploadedFileName);
            }
        }
        // 初始化参数列表
        paramList.add(fieldMap);
        if (multipartList.size() > 1) {
            paramList.add(multipartList);
        } else if (multipartList.size() == 1) {
            paramList.add(multipartList.get(0));
        } else {
            paramList.add(null);
        }
        // 返回参数列表
        return paramList;
    }

    public static void uploadFile(String basePath, Multipart multipart) {
        try {
            // 创建文件路径（绝对路径）
            String filePath = basePath + multipart.getFileName();
            FileUtil.createFile(filePath);
            // 执行流复制操作
            InputStream inputStream = new BufferedInputStream(multipart.getInputStream());
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            StreamUtil.copyStream(inputStream, outputStream);
        } catch (Exception e) {
            logger.error("上传文件出错！", e);
            throw new RuntimeException(e);
        }
    }
}
