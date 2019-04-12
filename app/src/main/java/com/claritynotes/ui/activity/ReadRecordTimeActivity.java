package com.claritynotes.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.database.helper.ReadRecordTimeItemHelper;
import com.claritynotes.model.ReadRecordDetailItem;
import com.claritynotes.model.ReadRecordTimeItem;
import com.claritynotes.model.event.ReadRecordTimeEvent;
import com.claritynotes.ui.utils.BookRecordUtil;
import com.claritynotes.utils.DateUtils;
import com.claritynotes.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 简言 on 2019/4/8  17:32.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.activity
 * Description :
 */
public class ReadRecordTimeActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    @BindView(R.id.read_record_time_rv)
    RecyclerView readRecordTimeRv;
    @BindView(R.id.main_ll_content)
    LinearLayout llContent;
    @BindView(R.id.main_tv_date)
    TextView mainTvDate;
    @BindView(R.id.main_tv_content)
    TextView mainTvContent;

    //默认 一进入 在onCreate中获取数据 当eventBus 回调回来数据之后进行必要的更新
    private List<ReadRecordTimeItem> readRecordTimeItems;

    private String bookKey;

    private String names;
    private String title;

    private int position = 0;

    private int count = 0;

    private ReadRecordTimeAdapter readRecordTimeAdapter;
    private String recordTime;


    @StyleRes
    @Override
    public int getActivityTheme() {
        // Make sure we don't use the one set to the Config, since we want a non-toolbar-actionbar for this activity
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark : R.style.AppTheme;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_record_time);
        ButterKnife.bind(this);

        registerEventBus(this);

        //根据这个 key 从数据库中获取
        bookKey = Objects.requireNonNull(getIntent().getExtras()).getString("bookKey");

        //获取数据
        readRecordTimeItems = BookRecordUtil.getReadRecordTimeItems(bookKey);
        //readRecordTimeItems = n0ew ArrayList<>();

        readRecordTimeAdapter = new ReadRecordTimeAdapter();
        readRecordTimeRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        readRecordTimeRv.setItemAnimator(new DefaultItemAnimator());
        readRecordTimeRv.setAdapter(readRecordTimeAdapter);

        mainTvDate.setText(Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(0, 10));

        //mainTvContent.setText("今天，你已经更新了 " + count + " 条数据");

        //点击进行 一个新的添加
        llContent.setOnClickListener(v -> {


            count = readRecordTimeItems.size();

            mainTvContent.setText("今天，你已经更新了 " + count + " 条数据");
            //跳转到详情页面
            Intent intent = new Intent(ReadRecordTimeActivity.this, ReadRecordDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShow", false);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在这里进行数据的存储，
        if (bookKey == null) bookKey = names != null ? names : "0";

        //bookKey = names 说明这是一个新建的页
        if (!bookKey.equals("0")) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getStringNames(ReadRecordTimeEvent event) {

        if (!event.isEdit()) { //添加状态

            if (readRecordTimeItems.get(0).getTitle().equals("title")) {

                readRecordTimeItems.clear();
            }

            //UIUtils.showToast("添加状态" + event.getNames());
            ReadRecordTimeItem item = new ReadRecordTimeItem(event.getNames(), event.getTitle(), Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(0, 10), event.getRecordTime());
            readRecordTimeItems.add(0, item);
        } else {
            //编辑状态
            //UIUtils.showToast("编辑状态");
            readRecordTimeItems.get(position).setNames(event.getNames());
            readRecordTimeItems.get(position).setTitle(event.getTitle());
        }

        //当编辑页面将改动的数据传回来后，进行保存
        if (ReadRecordTimeItemHelper.deleteAll(bookKey)) {

            ReadRecordTimeItemHelper.save(bookKey, readRecordTimeItems);
        }
        readRecordTimeAdapter.notifyDataSetChanged();
    }

    class ReadRecordTimeAdapter extends RecyclerView.Adapter<ReadRecordTimeAdapter.TimeViewHolder> {


        @NonNull
        @Override
        public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ReadRecordTimeActivity.this).inflate(R.layout.adapter_read_time, viewGroup, false);
            return new TimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeViewHolder timeViewHolder, int i) {

            //从数据库中获取最新数据的时间，和当前新增项目返回值的时间进行对比
            if (i > 0 && readRecordTimeItems.get(i).getRecordMonthDay().equals(readRecordTimeItems.get(i - 1).getRecordMonthDay())) {
                //就将显示时间控件隐藏
                timeViewHolder.llCircleTime.setVisibility(View.GONE);
            } else {
                //否则，就将控件显示
                timeViewHolder.mainTvDate.setText(readRecordTimeItems.get(i).getRecordMonthDay());
            }

            timeViewHolder.mainTvContent.setText(readRecordTimeItems.get(i).getNames());

            timeViewHolder.mainTvTitle.setText(readRecordTimeItems.get(i).getTitle());

            timeViewHolder.adapterRecordTime.setText(readRecordTimeItems.get(i).getRecordTime());

            //点击 进行 编辑
            timeViewHolder.mainIvEdit.setOnClickListener(v -> {

                if (readRecordTimeItems.get(0).getTitle().equals("title")) {

                    UIUtils.showToast("请点击上面进行添加");
                    return;
                }

                position = i;
                //跳转到详情页面
                Intent intent = new Intent(ReadRecordTimeActivity.this, ReadRecordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("contentKey", readRecordTimeItems.get(i).getTitle());
                bundle.putString("oldTitle", readRecordTimeItems.get(i).getTitle());
                bundle.putString("oldNames", readRecordTimeItems.get(i).getNames());
                bundle.putBoolean("isShow", true);  //进入 进行编辑 第一次进入 一定是没有展示的
                intent.putExtras(bundle);
                startActivity(intent);
            });

            timeViewHolder.editDelete.setOnClickListener(v -> {

                if (readRecordTimeItems.get(i).getNames().equals("请点击上面添加数据")){

                    UIUtils.showToast("示例不可删除");
                    return;
                }

                editDataDialog(i, readRecordTimeItems);
            });
        }

        @Override
        public int getItemCount() {
            return readRecordTimeItems.size();
        }

        class TimeViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.main_iv_circle)
            ImageView mainIvCircle;
            @BindView(R.id.main_tv_date)
            TextView mainTvDate;
            @BindView(R.id.main_tv_title)
            TextView mainTvTitle;
            @BindView(R.id.main_ll_title)
            LinearLayout mainLlTitle;
            @BindView(R.id.main_tv_content)
            TextView mainTvContent;
            @BindView(R.id.main_iv_edit)
            ImageView mainIvEdit;
            @BindView(R.id.item_rl_edit)
            RelativeLayout itemRlEdit;
            @BindView(R.id.item_ll_control)
            LinearLayout itemLlControl;
            @BindView(R.id.item_ll)
            LinearLayout itemLl;
            @BindView(R.id.ll_circle_time)
            LinearLayout llCircleTime;
            @BindView(R.id.adapter_read_record_time)
            TextView adapterRecordTime;
            @BindView(R.id.edit_image_delete)
            ImageButton editDelete;

            public TimeViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void editDataDialog(int positin, List<ReadRecordTimeItem> inReadRecordDetailItems) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去的和未来的。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {


                    readRecordTimeItems = BookRecordUtil.deleteReadRecordTimeItems(positin, bookKey, inReadRecordDetailItems.get(positin).getTitle(), inReadRecordDetailItems);
                    readRecordTimeAdapter.notifyDataSetChanged();
                }).show();

        //设置窗口参数
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }


}
