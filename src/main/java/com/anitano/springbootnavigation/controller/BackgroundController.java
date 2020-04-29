package com.anitano.springbootnavigation.controller;

import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.utils.ResultVOUtil;
import com.anitano.springbootnavigation.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserBackground
 * @Author: 杨11352
 * @Date: 2020/4/27 20:10
 */
@RestController
public class BackgroundController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/backgrounds")
    public ResultVO getList(){
        List background = redisTemplate.opsForList().range("background", 0, -1);
        if(background != null){
            return ResultVOUtil.success(background);
        }
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }
    @GetMapping("/background/{index}")
    public ResultVO get(@PathVariable("index")Integer index){
        Object background = redisTemplate.opsForList().index("background", index);
        if(background != null){
            return ResultVOUtil.success(background.toString());
        }
        return ResultVOUtil.error(ResultCodeEnum.RESULE_DATA_NONE);
    }
    /**
     * 管理员添加css基本样式
     * @param background
     * @return
     */
    @PostMapping("admin/background")
    public ResultVO add(@RequestBody Map<String,String> background){
        logger.info("[添加背景background]{}",background);
        Long length = redisTemplate.opsForList().leftPush("background", background.get("background"));
        if(length>0){
            logger.info("[添加背景]",length);
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(ResultCodeEnum.UPDATE_FAIL);
        }
    }
}
