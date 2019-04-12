package com.claritynotes.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.utils.PreferanceUtil;
import com.claritynotes.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 简言 on 2019/2/4.
 * 努力吧 ！ 少年 ！
 */

public abstract class BaseFragment extends LazyLoadFragment {

    private View rootView;
    protected Activity mActivity;
    protected Context context;

    protected int itemPosition = -1;

    private Unbinder unbinder;

    protected String ateKey;
    protected String light_theme = "light_theme";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
        this.context = getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);
            //ButterKnife IOC 等注入工具在这里进行注册
            unbinder = ButterKnife.bind(this, rootView);

            //获取主题
            ateKey = PreferanceUtil.getInstance(mActivity).getATEKey();

            // StateView 在这里注册

            initTitle(rootView);
            initView(rootView);
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    public void initTitle(View rootView) {

    }


    protected abstract int provideContentViewId();

    /**
     * 可以在这里进行UI操作
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onFragmentVisibleChange(boolean isFragmentVisible) {
    }

    @Override
    public void onFragmentFirstVisible() {
        loadData();
    }

    protected void loadData() {

    }

    protected AlertDialog.Builder showDialog(String message, View view) {

        //谈一个弹框，提示用户输入新增加的词根
        return new AlertDialog.Builder(mActivity).setMessage(message)
                .setView(view)
                .setNegativeButton("取消", (dialog, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                    itemPosition = -1;
                }).setCancelable(false);

        //设置窗口参数
        //setWindowDialogParam();
    }

    protected void setWindowDialogParam(AlertDialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
