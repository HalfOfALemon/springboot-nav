package com.anitano.springbootnavigation.utils;

import java.util.Random;

/**
 * @ClassName: KeyUtil
 * @Author: 杨11352
 * @Date: 2019/10/31 21:09
 */
public class KeyUtil {
    /**
     * 根据时间的毫秒数 + 一个随机数 , 要记得加上 线程锁
     *
     * @return
     */
    public static synchronized String getUniqueKey() {
        Random random = new Random();
        int number = random.nextInt(90000) + 10000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
