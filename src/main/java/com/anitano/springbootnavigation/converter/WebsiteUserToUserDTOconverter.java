package com.anitano.springbootnavigation.converter;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.dto.UserDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: WebsiteUserToUserDTOconverter
 * @Author: 杨11352
 * @Date: 2020/3/27 22:37
 */
public class WebsiteUserToUserDTOconverter {
    public static UserDTO convert(WebsiteUser websiteUser){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(websiteUser,userDTO);
        return userDTO;
    }

    public static List<UserDTO> convert(List<WebsiteUser> websiteUserList) {
        return websiteUserList.stream().map(
                e -> convert(e)
        ).collect(Collectors.toList());
    }
}
