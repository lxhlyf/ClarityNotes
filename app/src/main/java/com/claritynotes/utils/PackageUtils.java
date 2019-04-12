package com.claritynotes.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.claritynotes.app.MyApp;

/**
 * 描述：应用包相关的工具类
 *
 * @author CoderPig on 2018/02/28 18:01.
 */

public class PackageUtils {

    public static int packageCode() {
        PackageManager manager = MyApp.getContext().getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(MyApp.getContext().getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String packageName() {
        //根据全局上下文拿到包管理器
        PackageManager manager = MyApp.getContext().getPackageManager();
        String name = null;
        try {
            // 获取App versionName
            PackageInfo info = manager.getPackageInfo(MyApp.getContext().getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
}
