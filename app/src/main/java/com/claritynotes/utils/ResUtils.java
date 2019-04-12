package com.claritynotes.utils;


import com.claritynotes.app.MyApp;

/**
 * 描述：获取文件资源工具类
 *
 * @author CoderPig on 2018/02/14 11:07.
 */

public class ResUtils {
    /* 获取文件资源 */
    public static String getString(int strId) {
        return MyApp.getContext().getResources().getString(strId);
    }
}
