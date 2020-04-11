package com.anitano.springbootnavigation.service.impl;

import com.anitano.springbootnavigation.converter.WebsiteInfoToWebsiteInfoDTOconverter;
import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.exception.NavException;
import com.anitano.springbootnavigation.repository.WebsiteInfoRepository;
import com.anitano.springbootnavigation.service.WebsiteInfoService;
import com.anitano.springbootnavigation.utils.KeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: WebsiteInfoServiceImpl
 * @Author: 杨11352
 * @Date: 2020/3/31 14:29
 */
@Service
public class WebsiteInfoServiceImpl implements WebsiteInfoService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    WebsiteInfoRepository websiteInfoRepository;

    @Override
    public Page<WebsiteInfoDTO> getList(Pageable pageable, String query) {
        Page<WebsiteInfo> websiteInfoPage;
        if(StringUtils.isEmpty(query)){
            //1、不带查询条件，根据分页查询全部
            websiteInfoPage = websiteInfoRepository.findAll(pageable);
        }else {
            //带查询条件，根据查询条件查询
            websiteInfoPage = websiteInfoRepository.findByWebsiteNameLike(pageable,"%"+query+"%");
        }
        // 2、组装一个page类
        List<WebsiteInfoDTO> websiteInfoDTOList = WebsiteInfoToWebsiteInfoDTOconverter.convert(websiteInfoPage.getContent());
        return new PageImpl<>(websiteInfoDTOList,pageable,websiteInfoPage.getTotalElements());
    }

    @Override
    public List<WebsiteInfoDTO> getList(Integer categoryType) {
        List<WebsiteInfo> websiteInfoList = websiteInfoRepository.findByCategoryTypeOrderByWebsiteSortAsc(categoryType);
        List<WebsiteInfoDTO> websiteInfoDTOList = WebsiteInfoToWebsiteInfoDTOconverter.convert(websiteInfoList);
        return websiteInfoDTOList;
    }

    @Override
    public WebsiteInfoDTO get(String websiteId) {
        WebsiteInfo websiteInfo = websiteInfoRepository.findByWebsiteId(websiteId);
        if(websiteInfo == null){
            return null;
        }
        return WebsiteInfoToWebsiteInfoDTOconverter.convert(websiteInfo);
    }

    @Override
    public WebsiteInfo add(WebsiteInfo websiteInfo) {
        if(StringUtils.isEmpty(websiteInfo.getCategoryType()) || StringUtils.isEmpty(websiteInfo.getWebsiteName())){
            throw new NavException(ResultCodeEnum.DATA_IS_WRONG);
        }
        //设置id
        String id = websiteInfoRepository.findFirstByOrderByWebsiteIdDesc().getWebsiteId();
        websiteInfo.setWebsiteId(Integer.parseInt(id)+1+"");
        //设置创建时间
        websiteInfo.setCreateTime(new Date());
        websiteInfo.setUpdateTime(new Date());
        /*设置排序*/
        //获取排序最后的值
        WebsiteInfo websiteIdDesc = websiteInfoRepository.findFirstByCategoryTypeOrderByWebsiteIdDesc(websiteInfo.getCategoryType());
        Double websiteSort;
        if(websiteIdDesc == null){
            //该分类下没有信息
            websiteSort = 1.0;
        }else {
            websiteSort = websiteIdDesc.getWebsiteSort();
        }
        websiteInfo.setWebsiteSort(websiteSort);
        logger.info("-------{},{}",websiteInfo,websiteIdDesc);
        return websiteInfoRepository.save(websiteInfo);
    }

    @Override
    public WebsiteInfo delete(String id) {
        WebsiteInfo websiteInfo = websiteInfoRepository.findByWebsiteId(id);
        if(websiteInfo == null){
            return null;
        }
        websiteInfoRepository.delete(websiteInfo);
        return websiteInfo;
    }

    @Override
    public WebsiteInfo update(WebsiteInfoDTO websiteInfoDTO) {
        WebsiteInfo websiteInfo = websiteInfoRepository.findByWebsiteId(websiteInfoDTO.getWebsiteId());
        if(websiteInfo != null){
            BeanUtils.copyProperties(websiteInfoDTO,websiteInfo);
            return websiteInfoRepository.save(websiteInfo);
        }
        return null;
    }
}
