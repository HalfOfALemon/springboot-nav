package com.anitano.springbootnavigation.converter;

import com.anitano.springbootnavigation.dataobject.WebsiteInfo;
import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.UserDTO;
import com.anitano.springbootnavigation.dto.WebsiteInfoDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: WebsiteInfoToWebsiteInfoDTOconverter
 * @Author: 杨11352
 * @Date: 2020/3/31 14:43
 */
public class WebsiteInfoToWebsiteInfoDTOconverter {
    public static WebsiteInfoDTO convert(WebsiteInfo websiteInfo){
        WebsiteInfoDTO websiteInfoDTO = new WebsiteInfoDTO();
        BeanUtils.copyProperties(websiteInfo,websiteInfoDTO);
        return websiteInfoDTO;
    }

    public static List<WebsiteInfoDTO> convert(List<WebsiteInfo> websiteInfoList) {
        return websiteInfoList.stream().map(
                e -> convert(e)
        ).collect(Collectors.toList());
    }

}
