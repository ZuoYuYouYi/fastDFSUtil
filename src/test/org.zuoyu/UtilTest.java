package org.zuoyu;

import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.zuoyu.core.FastDFSUntil;

import java.io.*;
import java.util.Date;

/**
 * @author zuoyu
 * @program fastdfsbase
 * @description 测试
 * @create 2018-08-16 15:03
 **/
public class UtilTest {

    private FastDFSUntil fastDFSUntil = null;

    @Before
    public void before(){
        fastDFSUntil = FastDFSUntil.getFastDFSUntil();
    }
    /**
     * 文件上传实例
     */
    @Test
    public void testOne(){
        File file = new File("/home/zuoyu/Images/Screenshot_2018-07-27_13-53-58.png");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), "Screenshot_2018-07-27_13-53-58.png", "png", fileInputStream);
            String path = fastDFSUntil.uploadFile(multipartFile);
            System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件下载实例
     */
    @Test
    public void testTwo(){
        try {
            byte[] content = fastDFSUntil.downloadFile("group1", "M00/00/00/fwAAAVt1MNmEP881AAAAAHeiNMk483.png");
            FileOutputStream fileOutputStream = new FileOutputStream("/home/zuoyu/Downloads/" + new Date() + fastDFSUntil.getExtension("M00/00/00/fwAAAVt1MNmEP881AAAAAHeiNMk483.png"));
            IOUtils.write(content, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件附加信息实例
     */
    @Test
    public void testThree(){
        try {
            NameValuePair[] nameValuePairs = fastDFSUntil.getFileMate("group1", "M00/00/00/fwAAAVt2fCWEWBx2AAAAAL2LQWw113.png");
            for (NameValuePair nameValuePair : nameValuePairs){
                System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件信息实例
     */
    @Test
    public void testFour(){
        try {
            FileInfo fileInfo = fastDFSUntil.getFileInfo("group1", "M00/00/00/fwAAAVt2fCWEWBx2AAAAAL2LQWw113.png");
            System.out.println(fileInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
