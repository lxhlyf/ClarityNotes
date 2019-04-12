package com.claritynotes.daily.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.daily.DailyDataUtils;
import com.claritynotes.daily.data.DailyContent;
import com.claritynotes.database.helper.DailyItemContentHelper;
import com.claritynotes.model.event.DailyEvent;
import com.claritynotes.utils.DateUtils;
import com.claritynotes.widgets.LinedEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 简言 on 2019/4/8  15:39.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.daily.activity
 * Description :
 */
public class DailyEditActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    @BindView(R.id.edit_diary_et_title)
    EditText editTitle;
    @BindView(R.id.iv_main_headportrait)
    CircleImageView ivMainHeadportrait;
    @BindView(R.id.tv_main_name)
    TextView tvMainName;
    @BindView(R.id.layout_main_top)
    RelativeLayout layoutMainTop;
    @BindView(R.id.add_diary_et_content)
    LinedEditText addDiaryEtContent;

    private String briefy; //传回去用作内容的概要进行显示

    private String contentKay;

    private DailyContent dailyContent; //从数据中获取的

    private DailyContent content;

    private boolean isSave;

    @Override
    public int getActivityTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark : R.style.AppTheme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_daily_edit);
        ButterKnife.bind(this);

        isSave = Objects.requireNonNull(getIntent().getExtras()).getBoolean("isSave");

        contentKay = Objects.requireNonNull(getIntent().getExtras()).getString("dailyDetail");

        //如果是从 编辑来的
        if (!TextUtils.isEmpty(contentKay)){  //如果是从编辑来的 ，就获取了数据库

            //通过这个 Key 去数据库中查找对应的文章
            dailyContent = DailyDataUtils.getDailyEditData(contentKay);

            //去展示数据
            assert dailyContent != null;
            editTitle.setText(dailyContent.getTitle());
            addDiaryEtContent.setText(dailyContent.getContent());
        }else {

            dailyContent = new DailyContent(null, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //将新编辑的的 内容 保存到数据库中
        content = new DailyContent(editTitle.getText().toString(), Objects.requireNonNull(addDiaryEtContent.getText()).toString(), DateUtils.toDateStr(new Date()));

        if (TextUtils.isEmpty(contentKay)){ //是null 说明是来自 新增 所以 直接进行保存就可以了

                contentKay =  briefy;
                DailyItemContentHelper.save(contentKay, content);

        }else { //说明这是来自编辑

            if (!content.getContent().equals(dailyContent.getContent()) || !content.getTitle().equals(dailyContent.getTitle())){

                //无论是title还是content不一样都需要重新进行保存

                if (DailyItemContentHelper.deleteAll(contentKay)){  //删掉旧数据，存储新数据

                    contentKay = getContentNewKey(); //将旧的key换成新的key 进行保存
                    DailyItemContentHelper.save(contentKay, content);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {

        //获得title
        getContentNewKey();

        // 从数据库中获取

        /**
         * isSave 在上层view 已经进行了显示
         * 2.title 不是空
         * 3.内容不是空
         *
         * 只要有一个满足就应该回调
         */
        if (!isSave || !TextUtils.equals(editTitle.getText(), contentKay)) { //没有被保存 过的 时候 才去保存
            //当条目显示之后， 就不再进行显示了
            DailyEvent event = new DailyEvent();
            event.setDailyBrify(briefy);
            event.setEdit(isSave);
            EventBus.getDefault().post(event);
        }
        super.onBackPressed();
    }

    private String getContentNewKey() {
        if(!TextUtils.isEmpty(editTitle.getText().toString())){
            briefy = editTitle.getText().toString();
        }else {

            int count;
            int length = Objects.requireNonNull(addDiaryEtContent.getText()).toString().length();
            if (length < 25){
                count = length;
            }else{
                count = 25;
            }
            briefy = addDiaryEtContent.getText().toString().substring(0, count);
        }

        return briefy;
    }
}
