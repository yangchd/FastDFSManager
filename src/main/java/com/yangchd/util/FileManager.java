package com.yangchd.util;

import com.yangchd.util.pool.ConnectionPool;
import com.yangchd.util.pool.FastDFSLogger;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author yangchd
 * @date 2017/8/23
 * 文件管理工具
 */
public class FileManager implements FileManagerConfig {

    private static final long serialVersionUID = 1L;
    private static ConnectionPool pool;

    static {
        try {
            pool = new ConnectionPool(10, 30, 200);
        } catch (Exception e) {
            FastDFSLogger.error("FastDFS初始化异常",e);
        }
    }

    public static Map<String, Object> upload(FastDFSFile file, NameValuePair[] valuePairs) throws Exception {
        Map<String, Object> rMap = new HashMap<String, Object>(4);
        String[] uploadResults;
        String logId;
        if(null != file.getFilename() && !"".equals(file.getFilename())){
            logId = file.getFilename();
        }else{
            logId = UUID.randomUUID().toString();
        }
        TrackerServer trackerServer = pool.checkout(logId);
        StorageServer storageServer = null;
        StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
        try {
            uploadResults = client1.upload_file(file.getContent(), file.getExt(), valuePairs);
            String groupName = uploadResults[0];
            String remoteFileName = uploadResults[1];
            String fileAbsolutePath = PROTOCOL
                    + TRACKER_NGNIX_ADDR
                    + SEPARATOR + groupName
                    + SEPARATOR + remoteFileName;
            rMap.put("group", groupName);
            rMap.put("filename", remoteFileName);
            rMap.put("filepath", fileAbsolutePath);
        } catch (Exception e) {
            FastDFSLogger.error("FastDFS文件上传异常：",e);
            throw new Exception(e);
        } finally {
            pool.checkin(trackerServer, logId);
        }
        return rMap;
    }

    public static int delete(String groupName, String filename) throws Exception {
        String logId;
        if(null != filename && !"".equals(filename)){
            logId = filename;
        }else{
            logId = UUID.randomUUID().toString();
        }
        TrackerServer trackerServer = pool.checkout(logId);
        StorageServer storageServer = null;
        StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
        int result;
        try {
            result = client1.delete_file(groupName, filename);
        } catch (Exception e) {
            FastDFSLogger.error("文件删除异常：" + e.getMessage());
            throw new Exception(e);
        } finally {
            pool.checkin(trackerServer, logId);
        }
        return result;
    }

    /**
     * 直接获取文件流
     */
    static byte[] getFileBytes(String groupName, String remoteFileName) throws Exception {
        byte[] content;
        String logId;
        if(null != remoteFileName && !"".equals(remoteFileName)){
            logId = remoteFileName;
        }else{
            logId = UUID.randomUUID().toString();
        }
        TrackerServer trackerServer = pool.checkout(logId);
        StorageServer storageServer = null;
        StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
        try {
            content = client1.download_file(groupName, remoteFileName);
        } catch (Exception e) {
            FastDFSLogger.error("文件下载异常：" + e.getMessage());
            throw new Exception(e);
        } finally {
            pool.checkin(trackerServer, logId);
        }
        return content;
    }


    /**
     * 文件上次参数获取函数
     *
     * @param filename 文件名
     * @param length   文件长度
     * @param ext      文件后缀
     * @param author   作者
     */
    public static NameValuePair[] getValuePairs(String filename, String length, String ext, String author) {
        NameValuePair[] meta_list = new NameValuePair[4];
        meta_list[0] = new NameValuePair("fileName", filename);
        meta_list[1] = new NameValuePair("fileLength", length);
        meta_list[2] = new NameValuePair("fileExt", ext);
        meta_list[3] = new NameValuePair("fileAuthor", author);
        return meta_list;
    }

    /**
     * 根据File获取文件流
     */
    static byte[] getBytesByFile(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fin.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
            fin.close();
            bos.close();
        } catch (Exception e) {
            FastDFSLogger.error("文件流转化异常：" + e.getMessage());
        }
        return buffer;
    }

    /**
     * 获取文件后缀名
     */
    static String getFileExt(File file) {
        String filename = file.getName();
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

}