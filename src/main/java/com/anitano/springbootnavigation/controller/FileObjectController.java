package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.service.FileObjectService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: FileObjectController
 * @Author: 杨11352
 * @Date: 2020/4/13 15:42
 */
@RestController
public class FileObjectController {
    @Autowired
    private FileObjectService fileObjectService;

    @PostMapping("upload/image")
    public ResultVO upload(@RequestParam("file") MultipartFile file){
        String imageUrl = fileObjectService.uploadImage(file);
        return ResultVOUtil.success(imageUrl);
    }

    @PostMapping("upload/icon/{websiteId}")
    public ResultVO uploadIcon(@PathVariable(value = "websiteId")String websiteId, @RequestParam("file") MultipartFile file){
        String imageUrl = fileObjectService.uploadIcon(websiteId,file);
        if(imageUrl == null){
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
        return ResultVOUtil.success(imageUrl);
    }
}
