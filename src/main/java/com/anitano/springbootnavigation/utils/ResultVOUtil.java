package com.anitano.springbootnavigation.utils;


import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.vo.ResultVO;

/**
 * @Author: 杨11352
 * @Date: 2019/10/27 17:26
 */
public class ResultVOUtil {
    /**
     * 成功返回
     *
     * @param object
     * @return
     */
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        setResultCode(resultVO,ResultCodeEnum.SUCCESS);
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(msg);
        return resultVO;
    }

    public static ResultVO error(ResultCodeEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        setResultCode(resultVO,resultEnum);
        return resultVO;
    }
    public static void setResultCode(ResultVO resultVO,ResultCodeEnum code) {
        resultVO.setMessage(code.getMessage());
        resultVO.setCode(code.getCode());
    }
}
