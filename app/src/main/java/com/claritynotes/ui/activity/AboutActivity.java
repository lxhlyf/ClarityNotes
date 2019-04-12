package com.claritynotes.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.ui.fragment.AboutFragment;

import java.util.Objects;

/**
 * 描述：关于的Activity
 *
 * @author jay on 2018/1/12 14:01
 */

public class AboutActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    private Toolbar toolbar;

    @StyleRes
    @Override
    public int getActivityTheme() {
        // Make sure we don't use the one set to the Config, since we want a non-toolbar-actionbar for this activity
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark_ActionBar : R.style.AppTheme_ActionBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Slidr.attach(this);
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.cly_root, AboutFragment.newInstance()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
