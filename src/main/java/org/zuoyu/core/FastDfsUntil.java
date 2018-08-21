package org.zuoyu.core;

import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

/**
 *  fastDFS的工具类.
 *
 * @author zuoyu
 *
 * @program fastdfsbase
 *
 * @create 2018-08-16 14:10
 **/
public class FastDfsUntil {

  private static final FastDfsUntil FASTDFSUNTIL = new FastDfsUntil();
  /**
   * 配置文件.
   */
  private static final String FDFS_CLIENT =
      System.getProperty("user.dir") + "/src/main/resources/fdfs_client.conf";

  /**
   * 存储客户端.
   */
  private StorageClient1 storageClient;

  /**
   * 跟踪客户端.
   */
  private static TrackerClient trackerClient;

  /**
   * 跟踪服务.
   */
  private TrackerServer trackerServer;

  static {
    try {

      ClientGlobal.init(FDFS_CLIENT);
      trackerClient = new TrackerClient();
    } catch (IOException | MyException e) {
      e.printStackTrace();
    }
  }

  private FastDfsUntil() {
  }

  /**
   * 打开跟踪服务.
   */
  private void openTrackerServer() throws IOException {
    trackerServer = trackerClient.getConnection();
    storageClient = new StorageClient1(trackerServer, null);
  }

  /**
   * 获取fastDFS工具类.
   *
   * @return FastDFSUntil - fastDFS工具类
   */
  public static FastDfsUntil getFastDfsUntil() {
    return FASTDFSUNTIL;
  }

  /**
   * 文件上传.
   *
   * @param multipartFile - 要上传的文件
   * @return String - 上传的路径
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
      openTrackerServer();
      path = storageClient.upload_appender_file1(multipartFile.getBytes(), ext, nameValuePairs);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      trackerServer.close();
    }
    return path;
  }

  /**
   * 文件下载.
   *
   * @param groupName - 组名
   * @param remoteFileName - 文件定位名
   * @return byte[] - 文件内容
   */
  public byte[] downloadFile(String groupName, String remoteFileName) throws Exception {
    byte[] content = null;
    try {
      openTrackerServer();
      content = storageClient.download_file(groupName, remoteFileName);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      trackerServer.close();
    }
    return content;
  }

  /**
   * 获取附加文件信息.
   *
   * @param groupName - 组名
   * @param remoteFileName - 文件定位名
   * @return NameValuePair[] - 文件附加信息
   */
  public NameValuePair[] getFileMate(String groupName, String remoteFileName) {
    NameValuePair[] nameValuePairs = null;
    try {
      openTrackerServer();
      nameValuePairs = storageClient.get_metadata(groupName, remoteFileName);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        trackerServer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return nameValuePairs;
  }

  /**
   * 获取文件信息对象.
   *
   * @param groupName - 组名
   * @param remoteFileName - 文件定位名
   */
  public FileInfo getFileInfo(String groupName, String remoteFileName) throws Exception {
    FileInfo fileInfo = null;
    try {
      openTrackerServer();
      fileInfo = storageClient.get_file_info(groupName, remoteFileName);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      trackerServer.close();
    }
    return fileInfo;
  }

  /**
   * 根据返回的路径获取对应组名称.
   *
   * @param path - 路径
   * @return String - 对应组名称
   */
  public String getGroup(String path) {
    return path.split("/")[0];
  }

  /**
   * 根据返回的路径获取对应的文件定位名.
   *
   * @param path - 路径
   * @return String - 对应的文件定位名
   */
  public String getRemoteFileName(String path) {
    return path.substring(path.indexOf("/") + 1);
  }

  /**
   * 根据文件定位信息或文件名获取文件类型.
   *
   * @param fileName - 文件定位信息或文件名
   * @return String - 文件类型
   */
  public String getExtension(String fileName) {
    return FilenameUtils.getExtension(fileName);
  }
}
