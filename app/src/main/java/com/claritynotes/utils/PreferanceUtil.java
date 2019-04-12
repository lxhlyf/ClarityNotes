package com.claritynotes.utils;

import android.content.Context;
import android.content.IntentFilter;
import android.preference.PreferenceManager;

/**
 * Created by 简言 on 2019/4/2  9:49.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.utils
 * Description :
 */
public class PreferanceUtil {

    private Context context;

    private static PreferanceUtil instance;

    private PreferanceUtil(Context context){

        this.context = context;
    }

    public static PreferanceUtil getInstance(Context context){

        if (instance == null){

            synchronized (PreferanceUtil.class){
                if (instance == null){

                    instance = new PreferanceUtil(context);
                }
            }
        }
        return instance;
    }


    public final String getATEKey() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false) ?
                "dark_theme" : "light_theme";
    }
}
