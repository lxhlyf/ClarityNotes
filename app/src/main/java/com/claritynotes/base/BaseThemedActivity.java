package com.claritynotes.base;

import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.appthemeengine.ATEActivity;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Aidan Follestad (afollestad)
 */
public class BaseThemedActivity extends ATEActivity {

    protected String light_theme = "light_theme";

    @Nullable
    @Override
    public final String getATEKey() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                "dark_theme" : "light_theme";
    }

    //判断是否注册了EventBus
    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    //注册EventBus
    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    //取消注册EventBus
    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }
}
