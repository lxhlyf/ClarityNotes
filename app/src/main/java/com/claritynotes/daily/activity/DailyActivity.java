package com.claritynotes.daily.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.daily.DailyDataUtils;
import com.claritynotes.daily.data.DailyItem;
import com.claritynotes.daily.data.DailyItemDetail;
import com.claritynotes.model.event.DailyEvent;
import com.claritynotes.utils.DateUtils;
import com.claritynotes.utils.UIUtils;
import com.claritynotes.widgets.DividerItemDecoration;
import com.claritynotes.widgets.MyEditDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aidan Follestad (afollestad)
 */
public class DailyActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    @BindView(R.id.iv_main_plus)
    RelativeLayout ivPlus;
    @BindView(R.id.et_main_search)
    EditText etSearch;
    @BindView(R.id.iv_main_set)
    ImageView ivSet;
    @BindView(R.id.rv_main)
    RecyclerView rvItem;
    @BindView(R.id.tv_main_name)
    TextView tvName;


    private MyEditDialog dialogEdit;

    private DailyAdapter dailyAdapter;

    private List<DailyItem> dailyItems;

    private DailyItemAdapter dailyItemAdapter;

    private int mDailyPosition;
    private int mDetailPosition;

    @StyleRes
    @Override
    public int getActivityTheme() {
        // Make sure we don't use the one set to the Config, since we want a non-toolbar-actionbar for this activity
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark : R.style.AppTheme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.bind(this);

        registerEventBus(this);

        //获取数据
        dailyItems = DailyDataUtils.getDailyData();

        //dailyItemDetails = DailyDataUtils.getDailyItemsDetail();

        dailyAdapter = new DailyAdapter();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvItem.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvItem.setItemAnimator(new DefaultItemAnimator());
        rvItem.setAdapter(dailyAdapter);

        //新增加一条日记的点击事件
        ivPlus.setOnClickListener(v -> {

            Intent intent = new Intent(this, DailyEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("dailyDetail", null);
            bundle.putBoolean("isSave", false);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {


        @NonNull
        @Override
        public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(DailyActivity.this).inflate(R.layout.adapter_daily, viewGroup, false);
            return new DailyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DailyViewHolder dailyViewHolder, int position) {


            dailyViewHolder.recordTimeMonth.setText(dailyItems.get(position).getRecordMonthDay());

            dailyItemAdapter = new DailyItemAdapter(dailyItems.get(position).getDailyItemDetails(), position);

            dailyViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(DailyActivity.this, LinearLayoutManager.VERTICAL, false));
            dailyViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            dailyViewHolder.recyclerView.setAdapter(dailyItemAdapter);

            dailyItemAdapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return dailyItems.size();
        }

        class DailyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.daily_record_time)
            TextView recordTimeMonth;
            @BindView(R.id.adapter_daily_rv)
            RecyclerView recyclerView;

            public DailyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDailyString(DailyEvent event) {

        dailyItems = DailyDataUtils.updateDailyItems(event.getDailyBrify(), dailyItems, event.isEdit(), mDailyPosition, mDetailPosition);
        dailyAdapter.notifyDataSetChanged(); //放的是时间
        dailyItemAdapter.notifyDataSetChanged(); //放的是具体的item
    }

    public class DailyItemAdapter extends RecyclerView.Adapter<DailyItemAdapter.DailyItemViewHolder> {

        private List<DailyItemDetail> dailyItemDetails;
        private int position;

        public DailyItemAdapter(List<DailyItemDetail> dailyItemDetails, int position) {

            this.dailyItemDetails = dailyItemDetails;
            this.position = position;
        }

        @NonNull
        @Override
        public DailyItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(DailyActivity.this).inflate(R.layout.adapter_daily_item, viewGroup, false);
            return new DailyItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DailyItemAdapter.DailyItemViewHolder dailyItemViewHolder, int i) {

            dailyItemViewHolder.itemRecord.setText(dailyItemDetails.get(i).getRecordTiem());

            dailyItemViewHolder.itemContent.setText(dailyItemDetails.get(i).getContent());

            dailyItemViewHolder.itemView.setOnClickListener(v -> {

                mDailyPosition = position; //具体时间Item的位置

                mDetailPosition = i;

                if (dailyItemDetails.get(i).getContent().equals("今日还没有书写!!")) return;
                Intent intent = new Intent(DailyActivity.this, DailyEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("dailyDetail", dailyItemDetails.get(i).getContent());   //key
                bundle.putBoolean("isSave", true);
                intent.putExtras(bundle);
                startActivity(intent);
            });

            //长按删除
            dailyItemViewHolder.itemView.setOnLongClickListener(v -> {

                if (dailyItemDetails.get(i).getContent().equals("今日还没有书写!!")) return true;

                Log.e("daily", ""+dailyItemDetails.size());

                //这里的数据 ，没有成功的传过去， 是一个空的集合
                //1.
                //2. 具体Item 的position
                //3. 时间item的position
                editDataDialog(true, i, position, dailyItemDetails);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return dailyItemDetails.size();
        }

        public class DailyItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.adapter_daily_item_record)
            TextView itemRecord;
            @BindView(R.id.daily_item_content)
            TextView itemContent;

            public DailyItemViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void editDataDialog(boolean isEdit, int dailyDetailPosition, int dailyPositin, List<DailyItemDetail> dailyItemDetails) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去和为来。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {

                    //条目删掉还不行，还要将对应的数据库删掉
                    /**
                     *  i 对应的概略条目的 位置
                     *  dailyItemPosition 对应的时间条目的位置
                     */
                    if (dailyItemDetails.size() > 0)
                        dailyItems = DailyDataUtils.deleteDailyItem(dailyItemDetails.get(dailyDetailPosition).getContent(), dailyItems, dailyDetailPosition, dailyPositin);

                    //将当前条目删除 掉
                    //dailyItemDetails.remove(dailyDetailPosition);
                    dailyItemAdapter.notifyDataSetChanged();

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }
}
