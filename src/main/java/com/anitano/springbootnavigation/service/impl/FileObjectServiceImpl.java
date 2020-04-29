package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.config.UploadProperties;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.exception.NavException;
import com.anitano.springbootnavigation.service.FileObjectService;
import com.anitano.springbootnavigation.service.WebsiteInfoService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @ClassName: FileObjectServiceImpl
 * @Author: 杨11352
 * @Date: 2020/4/13 17:22
 */
@Service
public class FileObjectServiceImpl implements FileObjectService {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private UploadProperties properties;
    @Autowired
    private WebsiteInfoService websiteInfoService;

    /**
     * 上传的方法
     * @param file
     * @return
     */
    private String BaseUploadImage(MultipartFile file){
        try {
            //校验文件类型
            if(!properties.getAllowTypes().contains(file.getContentType())){
                throw  new NavException(ResultCodeEnum.INVALID_FILE_TYPE);
            }
            //校验内容,如果不是图片就返回null
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null){
                throw new NavException(ResultCodeEnum.INVALID_FILE_TYPE);
            }
            //上传到FastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            //四个参数 文件流、文件大小、 文件类型、第四个可以为空
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回路径
            logger.info("返回路径：{}",storePath.getFullPath());
            return properties.getBaseUrl()+storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[上传文件] 上传文件失败",e);
            throw  new NavException(ResultCodeEnum.INVALID_FILE_TYPE);
        }
    }
    /**上传文件*/
    @Override
    public String uploadImage(MultipartFile file) {
        return BaseUploadImage(file);
    }

    /**
     * 上传图标
     * @param websiteId
     * @param file
     * @return
     */
    @Override
    public String uploadIcon(String websiteId, MultipartFile file) {
        //1、查询信息存不存在
        WebsiteInfoDTO websiteInfoDTO = websiteInfoService.get(websiteId);
        if(websiteInfoDTO == null){
            logger.info("[修改图标]该网站信息不存在");
            return null;
        }
        //2、删除旧图标
        if(StringUtils.isNotEmpty(websiteInfoDTO.getWebsiteIcon())){
            deleteFile(websiteInfoDTO.getWebsiteIcon());
        }
        //3、上传图标
        String url = BaseUploadImage(file);
        //4、将保存的图片地址保存到数据库
        websiteInfoDTO.setWebsiteIcon(url);
        websiteInfoService.update(websiteInfoDTO);
        return url;
    }

    /**
     * 删除文件
     * @param fullUrl 完整的地址 例如：http://fastdfs.anitano.cn/group1/M00/00/00/ajbIXV6VsfSAH-9pAAQHRPqcmLs011.png
     * @return
     */
    @Override
    public ResultCodeEnum  deleteFile(String fullUrl){
        //判断这个地址是不是自己服务器的
        int index = fullUrl.indexOf(properties.getBaseUrl());
        int length = properties.getBaseUrl().length();
        if(index == -1 || fullUrl.length() <length){
            return ResultCodeEnum.DATA_IS_WRONG;
        }
        String filePath = fullUrl.substring(length);
        try {
            fastFileStorageClient.deleteFile(filePath);
        } catch (Exception e) {
            logger.info("[删除旧图片失败]地址：{}，原因：{}",filePath,e.getMessage());
            return ResultCodeEnum.DELETE_OLD_DATA_FAILED;
        }
        return ResultCodeEnum.SUCCESS;
    }

}
