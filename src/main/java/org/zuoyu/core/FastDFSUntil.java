package org.zuoyu.core;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author zuoyu
 * @program fastdfsbase
 * @description fastDFS的工具类
 * @create 2018-08-16 14:10
 **/
public class FastDFSUntil {
    private static final FastDFSUntil FASTDFSUNTIL = new FastDFSUntil();
    /**
     * 配置文件
     */
    private static final String FDFS_CLIENT = System.getProperty("user.dir") + "/src/main/resources/fdfs_client.conf";
    /**
     * 核心对象
     */
    private static StorageClient1 storageClient;

    static {
        try {

            ClientGlobal.init(FDFS_CLIENT);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            storageClient = new StorageClient1(trackerServer, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    private FastDFSUntil() {
    }

    /**
     * 获取fastDFS工具类
     *
     * @return FastDFSUntil - fastDFS工具类
     */
    public static FastDFSUntil getFastDFSUntil() {
        return FASTDFSUNTIL;
    }

    /**
     * 文件上传
     *
     * @param multipartFile - 要上传的文件
     * @return String - 上传的路径
     * @throws Exception
     */
    public String uploadFile(MultipartFile multipartFile) throws Exception {

        //扩展名
        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        NameValuePair[] nameValuePairs = new NameValuePair[3];
        nameValuePairs[0] = new NameValuePair("fileName", multipartFile.getName());
        nameValuePairs[1] = new NameValuePair("fileExt", ext);
        nameValuePairs[2] = new NameValuePair("fileSize", String.valueOf(multipartFile.getSize()));
        String path = null;
        try {
            path = storageClient.upload_appender_file1(multipartFile.getBytes(), ext, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 文件下载
     *
     * @param groupName      - 组名
     * @param remoteFileName - 文件定位名
     * @return byte[] - 文件内容
     * @throws Exception
     */
    public byte[] downloadFile(String groupName, String remoteFileName) throws Exception {
        byte[] content = null;
        try {
            content = storageClient.download_file(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 获取附加文件信息
     *
     * @param groupName      - 组名
     * @param remoteFileName - 文件定位名
     * @return NameValuePair[] - 文件附加信息
     * @throws Exception
     */
    public NameValuePair[] getFileMate(String groupName, String remoteFileName) throws Exception {
        NameValuePair[] nameValuePairs = null;
        try {
            nameValuePairs = storageClient.get_metadata(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameValuePairs;
    }

    /**
     * 获取文件信息对象
     *
     * @param groupName      - 组名
     * @param remoteFileName - 文件定位名
     * @return
     * @throws Exception
     */
    public FileInfo getFileInfo(String groupName, String remoteFileName) throws Exception {
        FileInfo fileInfo = null;
        try {
            fileInfo = storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    /**
     * 根据返回的路径获取对应组名称
     *
     * @param path - 路径
     * @return String - 对应组名称
     */
    public String getGroup(String path) {
        return path.split("/")[0];
    }

    /**
     * 根据返回的路径获取对应的文件定位名
     *
     * @param path - 路径
     * @return String - 对应的文件定位名
     */
    public String getRemoteFileName(String path) {
        return path.substring(path.indexOf("/") + 1);
    }

    /**
     * 根据文件定位信息或文件名获取文件类型
     *
     * @param fileName - 文件定位信息或文件名
     * @return String - 文件类型
     */
    public String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }
}
