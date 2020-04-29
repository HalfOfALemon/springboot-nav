package com.anitano.springbootnavigation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: UploadProperties
 * @Author: 杨11352
 * @Date: 2020/4/13 18:52
 */
@Data
@Component
@ConfigurationProperties(prefix = "th.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}
