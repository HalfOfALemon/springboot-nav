package com.anitano.springbootnavigation.controller;

/**
 * @ClassName: UserFeedbackController
 * @Author: 杨11352
 * @Date: 2020/4/28 17:43
 */

import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserFeedbackController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("feedback")
    public ResultVO add(@RequestBody Map<String,String> map){
        redisTemplate.opsForList().rightPush("feedback",map);
        return ResultVOUtil.success();
    }

    @GetMapping("admin/feedback")
    public ResultVO get(){
        List feedback = redisTemplate.opsForList().range("feedback", 0, -1);
        if(feedback !=null){
            return ResultVOUtil.success(feedback);
        }
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }
}
