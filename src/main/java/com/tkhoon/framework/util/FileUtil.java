package com.tkhoon.framework.util;

import com.tkhoon.framework.FrameworkConstant;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    // 创建目录
    public static File createDir(String dirPath) {
        File dir;
        try {
            dir = new File(dirPath);
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir);
            }
        } catch (Exception e) {
            logger.error("创建目录出错！", e);
            throw new RuntimeException(e);
        }
        return dir;
    }

    // 创建文件
    public static File createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                FileUtils.forceMkdir(parentDir);
            }
        } catch (Exception e) {
            logger.error("创建文件出错！", e);
            throw new RuntimeException(e);
        }
        return file;
    }

    // 复制目录（不会复制空目录）
    public static void copyDir(String srcPath, String destPath) {
        try {
            File srcDir = new File(srcPath);
            File destDir = new File(destPath);
            if (srcDir.exists() && srcDir.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(srcDir, destDir);
            }
        } catch (Exception e) {
            logger.error("复制目录出错！", e);
            throw new RuntimeException(e);
        }
    }

    // 复制文件
    public static void copyFile(String srcPath, String destPath) {
        try {
            File srcFile = new File(srcPath);
            File destDir = new File(destPath);
            if (srcFile.exists() && srcFile.isFile()) {
                FileUtils.copyFileToDirectory(srcFile, destDir);
            }
        } catch (Exception e) {
            logger.error("复制文件出错！", e);
            throw new RuntimeException(e);
        }
    }

    // 删除目录
    public static void deleteDir(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                FileUtils.deleteDirectory(dir);
            }
        } catch (Exception e) {
            logger.error("删除目录出错！", e);
            throw new RuntimeException(e);
        }
    }

    // 删除文件
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                FileUtils.forceDelete(file);
            }
        } catch (Exception e) {
            logger.error("删除文件出错！", e);
            throw new RuntimeException(e);
        }
    }

    // 重命名文件
    public static void renameFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            File newFile = new File(destPath);
            boolean result = srcFile.renameTo(newFile);
            if (!result) {
                throw new RuntimeException("重命名文件出错！" + newFile);
            }
        }
    }

    // 将字符串写入文件
    public static void writeFile(String filePath, String fileContent) {
        OutputStream os = null;
        Writer w = null;
        try {
            FileUtil.createFile(filePath);
            os = new BufferedOutputStream(new FileOutputStream(filePath));
            w = new OutputStreamWriter(os, FrameworkConstant.DEFAULT_CHARSET);
            w.write(fileContent);
            w.flush();
        } catch (Exception e) {
            logger.error("写入文件出错！", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (w != null) {
                    w.close();
                }
            } catch (Exception e) {
                logger.error("释放资源出错！", e);
            }
        }
    }

    // 获取真实文件名（去掉文件路径）
    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    // 获取编码后的文件名（将文件名进行 Base64 编码）
    public static String getEncodedFileName(String fileName) {
        String prefix = FilenameUtils.getBaseName(fileName);
        String suffix = FilenameUtils.getExtension(fileName);
        return CodecUtil.encodeBase64(prefix) + "." + suffix;
    }

    // 获取解码后的文件名（将文件名进行 Base64 解码）
    public static String getDecodedFileName(String fileName) {
        String prefix = FilenameUtils.getBaseName(fileName);
        String suffix = FilenameUtils.getExtension(fileName);
        return CodecUtil.decodeBase64(prefix) + "." + suffix;
    }

    // 判断文件是否存在
    public static boolean checkFileExists(String filePath) {
        return new File(filePath).exists();
    }
}
