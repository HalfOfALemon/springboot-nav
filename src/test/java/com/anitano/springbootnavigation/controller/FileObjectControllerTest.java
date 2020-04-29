package com.anitano.springbootnavigation.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: FileObjectControllerTest
 * @Author: 杨11352
 * @Date: 2020/4/13 15:43
 */
@SpringBootTest
class FileObjectControllerTest {
    Logger logger = LoggerFactory.getLogger(getClass());
/*    @Test
    public void test01(){
        System.out.println("测试：");
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            NameValuePair nvp[] = null;
            //上传到文件系统
            String fileId = client.upload_file1("E:\\javaRoad\\token.png", "png",
                    nvp);
            logger.info("测试上传文件：{}",fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
/*    @Test
    void testDownload() {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = client.download_file1("group1/M00/00/00/ajbIXV6UGYeAatFvAAKR-l5cGKY321.png");
            FileOutputStream fos = new FileOutputStream(new File("E:\\javaRoad\\tokenDownload.png"));
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
/*    @Test
    public void getToken() throws Exception {
        int ts = (int) Instant.now().getEpochSecond();
        String token = ProtoCommon.getToken("M00/00/00/ajbIXV6UGYeAatFvAAKR-l5cGKY321.png", ts, "FastDFS1478963250");
        StringBuilder sb = new StringBuilder();
        sb.append("?token=").append(token);
        sb.append("&ts=").append(ts);
        System.out.println(sb.toString());
    }*/
}