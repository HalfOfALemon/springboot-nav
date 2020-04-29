package com.anitano.springbootnavigation.service;

import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: FileObjectService
 * @Author: 杨11352
 * @Date: 2020/4/13 17:22
 */
public interface FileObjectService {
    String uploadImage(MultipartFile file);
    String uploadIcon(String websiteId,MultipartFile file);
    ResultCodeEnum deleteFile(String fullUrl);
}
