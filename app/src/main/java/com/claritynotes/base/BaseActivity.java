package com.claritynotes.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.model.event.WordsRefreshEvent;
import com.claritynotes.ui.activity.MainActivity;
import com.claritynotes.ui.fragment.WordsFragment;
import com.claritynotes.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 简言 on 2019/3/17  18:53.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.allbelonglxh.base
 * Description :
 */
@SuppressLint("Registered")
public abstract class BaseActivity extends BaseThemedActivity {

    private Unbinder unbinder;

    private Activity currentActivity;

    private static long mPreTime = 0;

    private LinkedList mActiviitys = new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(ProviderView());
        unbinder = ButterKnife.bind(this);

        synchronized (this) {
            mActiviitys.add(this);
        }

        initView();

        initData();

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentActivity = null;
    }

    protected abstract void initView();

    protected abstract int ProviderView();

    protected void initData() {
    }

    protected void initListener() {
    }

    /**
     * 设置Setting 这个菜单项
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);  //toolBar 上的Menu setting

        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_view_example));
//        searchView.setIconifiedByDefault(false);
//        searchItem.expandActionView();

        return true;
    }

    /**
     * 设置 Setting的监听器
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.search:

                //这里进行一些搜索的操作
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * 统一退出控制
     */
    @Override
    public void onBackPressed() {
        if (currentActivity instanceof MainActivity) {
            //如果是主页面
            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2秒
                Toast.makeText(this, "再按一次，退出应用", Toast.LENGTH_SHORT).show();
                mPreTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        unbinder.unbind();

        synchronized (this) {
            mActiviitys.remove(this);
        }
        super.onDestroy();
    }
}
