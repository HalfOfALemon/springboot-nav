package com.anitano.springbootnavigation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: AttributeUtil
 * @Author: 杨11352
 * @Date: 2020/4/20 14:48
 */
public class AttributeUtil {
    public static boolean isEmail(String string) {
        if (string == null) {
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
