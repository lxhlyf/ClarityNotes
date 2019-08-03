package com.claritynotes.ui.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseActivity;
import com.claritynotes.collapsingtb.CollapsingToolbarActivity;
import com.claritynotes.dialogs.AboutDialog;
import com.claritynotes.misc.MiscActivity;
import com.claritynotes.daily.activity.DailyActivity;
import com.claritynotes.ui.fragment.PhraseFragment;
import com.claritynotes.ui.fragment.ReadRecordFragment;
import com.claritynotes.ui.fragment.SentenceFragment;
import com.claritynotes.ui.fragment.WordsFragment;
import com.claritynotes.ui.utils.FragmentManagerHelper;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;

    private String ateKey;

    private WordsFragment wordsFragment;
    private PhraseFragment phraseFragment;
    private SentenceFragment sentenceFragment;
    private ReadRecordFragment readRecordFragment;

    private FragmentManagerHelper fragmentManagerHelper;


    @BindView(R.id.main_rg)
    RadioGroup radioGroup;

    @Override
    protected int ProviderView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        final Toolbar toolbar = findViewById(R.id.appbar_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        //获取主题
        ateKey = getATEKey();
        //setBgByTheme();

        //默认选中 words
        fragmentManagerHelper = new FragmentManagerHelper(this, getSupportFragmentManager(), R.id.main_tab_fl);
        if (wordsFragment == null) {
            wordsFragment = new WordsFragment();
        }
        fragmentManagerHelper.switchFragment(wordsFragment);

        mDrawer = findViewById(R.id.drawer_layout);
        mDrawer.addDrawerListener(new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close));

        final NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(this);
    }

    private void setBgByTheme() {

        if (ateKey.equals(light_theme)) {
            //设置bottomBar的Background
            radioGroup.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLightDefault));
        }
    }

    private boolean isWordsRBTwice = true;

    @Override
    protected void initListener() {
        super.initListener();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId) {
                case R.id.words_rb:
                    //只能进来一次
                    fragmentManagerHelper.switchFragment(wordsFragment);
                    break;
                case R.id.phrase_rb:
                    if (phraseFragment == null) {
                        phraseFragment = new PhraseFragment();
                    }
                    fragmentManagerHelper.switchFragment(phraseFragment);
                    break;
                case R.id.sentence_rb:
                    if (sentenceFragment == null) {
                        sentenceFragment = new SentenceFragment();
                    }
                    fragmentManagerHelper.switchFragment(sentenceFragment);
                    break;
                case R.id.write_read_rb:
                    if (readRecordFragment == null) {
                        readRecordFragment = new ReadRecordFragment();
                    }
                    fragmentManagerHelper.switchFragment(readRecordFragment);
                    break;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //registerEventBus(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterEventBus(this);
    }

    //作用就是调用父类的
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置 NavigationView 侧滑栏
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawer.closeDrawers();
        final int mItemId = item.getItemId();
        mDrawer.postDelayed(() -> {
            switch (mItemId) {
                case R.id.drawer_daily_every:
                    startActivity(new Intent(MainActivity.this, DailyActivity.class));
                    break;
//                    case R.id.drawer_book_read:
//                        startActivity(new Intent(MainActivity.this, TabSampleActivity.class));
//                        break;
//                    case R.id.drawer_wallet_record:
//                        startActivity(new Intent(MainActivity.this, CollapsingToolbarActivity.class));
//                        break;
//                    case R.id.drawer_cure_heart:
//                        startActivity(new Intent(MainActivity.this, WidgetActivity.class));
//                        break;
//                    case R.id.drawer_music:
//                        startActivity(new Intent(MainActivity.this, MiscActivity.class));
                    //break;
                case R.id.drawer_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;
                case R.id.drawer_about:
                    //AboutDialog.show(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
        }, 75);
        return true;
    }

    @Override
    protected void onDestroy() {
        fragmentManagerHelper.clearFragment();
        super.onDestroy();
    }
}