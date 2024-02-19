package top.ulug.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by fddi on 18-3-24.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 保存文件到本地
     *
     * @param file     文件
     * @param savePath 保存路径
     * @param name     名称
     */
    public static void saveFile(MultipartFile file, String savePath, String name) throws Exception {
        if (file == null || StringUtils.isEmpty(savePath) ||
                StringUtils.isEmpty(name)) {
            throw new Exception("参数为空");
        }
        DataInputStream inputStream = new DataInputStream(file.getInputStream());
        File path = new File(savePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File localFile = new File(savePath, name);
        FileOutputStream outputStream = new FileOutputStream(localFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * 保存远程文件到本地
     *
     * @param url      文件url
     * @param savePath 保存路径
     * @param name     名称
     * @throws Exception
     */
    public static void saveRemoteFile(String url, String savePath, String name) throws Exception {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(savePath) ||
                StringUtils.isEmpty(name))
            throw new Exception("参数为空");
        URL openUrl = new URL(url);
        DataInputStream inputStream = new DataInputStream(openUrl.openStream());
        File file = new File(savePath);
        if (!file.exists()) file.mkdirs();
        File iFile = new File(savePath, name);
        FileOutputStream outputStream = new FileOutputStream(iFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * 解压zip文件
     *
     * @param sourceFile zip
     * @param targetDir  解压路径
     * @throws Exception
     */
    public static void unZip(File sourceFile, String targetDir) throws Exception {
        long start = System.currentTimeMillis();
        if (!sourceFile.exists()) {
            throw new Exception("参数为空");
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(sourceFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = targetDir + "/" + entry.getName();
                    createDirIfNotExist(dirPath);
                } else {
                    File targetFile = new File(targetDir + "/" + entry.getName());
                    createFileIfNotExist(targetFile);
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        is = zipFile.getInputStream(entry);
                        fos = new FileOutputStream(targetFile);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    } finally {
                        try {
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            LOG.info("解压完成，耗时：" + (System.currentTimeMillis() - start) + " ms");
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createDirIfNotExist(String path) {
        File file = new File(path);
        createDirIfNotExist(file);
    }

    public static void createDirIfNotExist(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createFileIfNotExist(File file) throws Exception {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

    public static void createParentDirIfNotExist(File file) {
        createDirIfNotExist(file.getParentFile());
    }

    /***
     * 删除文件夹
     *
     * @param folderPath 路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 路径
     * @return result
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
