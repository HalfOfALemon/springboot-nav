package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.exception.NavException;
import com.anitano.springbootnavigation.repository.WebsiteInfoRepository;
import com.anitano.springbootnavigation.service.WebsiteInfoService;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: WebsiteInfoController
 * @Author: 杨11352
 * @Date: 2020/3/31 12:47
 */
@RequestMapping("/navs")
@RestController
public class WebsiteInfoController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    WebsiteInfoService websiteInfoService;

    /**
     * 获取全部导航信息
     * @param page 第几页
     * @param size 每页大小
     * @param query 查询条件
     * @return 返回
     */
    @GetMapping
    public  ResultVO list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "size",defaultValue = "10")Integer size,
                          @RequestParam(value = "query")String query){
        PageRequest pageRequest = PageRequest.of(page-1,size);
        Page<WebsiteInfoDTO> websiteInfoDTOPage = websiteInfoService.getList(pageRequest, query);
        if(websiteInfoDTOPage != null){
            return ResultVOUtil.success(websiteInfoDTOPage);
        }
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }
    /**
     * 根据分类Type，获取列表
     * @param categoryType 分类id
     */
    @GetMapping("/category-type/{categoryType}")
    public ResultVO listByCategoryType(@PathVariable(value = "categoryType")Integer categoryType){
        List<WebsiteInfoDTO> websiteInfoDTOList = websiteInfoService.getList(categoryType);
        if(websiteInfoDTOList != null ){
            return ResultVOUtil.success(websiteInfoDTOList);
        }
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }
    @GetMapping("/{id}")
    public ResultVO get(@PathVariable(value = "id")String websiteId){
        WebsiteInfoDTO websiteInfoDTO = websiteInfoService.get(websiteId);
        if(websiteInfoDTO == null){
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
        return ResultVOUtil.success(websiteInfoDTO);
    }

    /**
     * 添加导航网站
     * @param websiteInfo
     * @return
     */
    @PostMapping("")
    public ResultVO add(@RequestBody WebsiteInfo websiteInfo){
        try {
            websiteInfoService.add(websiteInfo);
        } catch (NavException e) {
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 删除一个导航网站
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResultVO delete(@PathVariable(value = "id")String id){
        WebsiteInfo websiteInfo = websiteInfoService.delete(id);
        if(websiteInfo == null){
            return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
        }
        return ResultVOUtil.success();
    }
    @PutMapping("")
    public ResultVO update(@RequestBody WebsiteInfoDTO websiteInfoDTO){
        WebsiteInfo websiteInfo = websiteInfoService.update(websiteInfoDTO);
        if(websiteInfo != null){
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }
}
