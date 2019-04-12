package com.claritynotes.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.model.SentenceItem;
import com.claritynotes.ui.utils.CharacterUtils;
import com.claritynotes.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 简言 on 2019/4/2  21:12.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.activity
 * Description :
 */
public class SentenceListActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private ArrayList<SentenceItem> sentenceItems;
    private SentenceItemAdapter sentenceItemAdapter;
    private int itemPosition = -1;

    private String source;

    @StyleRes
    @Override
    public int getActivityTheme() {
        // Make sure we don't use the one set to the Config, since we want a non-toolbar-actionbar for this activity
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark_ActionBar : R.style.AppTheme_ActionBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);
        ButterKnife.bind(this);

        source = Objects.requireNonNull(getIntent().getExtras()).getString("source");

        //UIUtils.showToast(source);
        initView();
    }

    private void initView() {

        // 从数据库中获取数据
        sentenceItems = CharacterUtils.getSentenceItems(source);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sentenceItemAdapter = new SentenceItemAdapter();
        recyclerView.setAdapter(sentenceItemAdapter);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class SentenceItemAdapter extends RecyclerView.Adapter<SentenceItemAdapter.MyViewHolder> {
        @NonNull
        @Override
        public SentenceItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(SentenceListActivity.this).inflate(R.layout.adapter_sentence_list_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SentenceItemAdapter.MyViewHolder holder, int i) {

            if (TextUtils.isEmpty(sentenceItems.get(i).getSentence())){

                sentenceItems.remove(i);
                return;
            }
            holder.sentence.setText(sentenceItems.get(i).getSentence());

            if(TextUtils.isEmpty(sentenceItems.get(i).getBookSource()))
                holder.bookSource.setVisibility(View.GONE);

            if (TextUtils.isEmpty(sentenceItems.get(i).getBookSource()))
                holder.bookSource.setVisibility(View.GONE);
            holder.bookSource.setText(sentenceItems.get(i).getBookSource());

            if (TextUtils.isEmpty(sentenceItems.get(i).getRecordTime()))
                holder.recordTime.setVisibility(View.GONE);
            holder.recordTime.setText(sentenceItems.get(i).getRecordTime());

            holder.itemView.setOnClickListener(v -> {

                itemPosition = i;
                editDataDialog(true);
            });

            holder.editButton.setOnClickListener(v -> {

                if (sentenceItems.get(i).getBookSource().equals("刘一峰")){
                    UIUtils.showToast("示例不可删除");
                    return;
                }

                editDataDialog(i);
            });
        }

        @Override
        public int getItemCount() {
            return sentenceItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.adapter_list_sentence)
            TextView sentence;
            @BindView(R.id.adapter_list_book_source)
            TextView bookSource;
            @BindView(R.id.adapter_record_time)
            TextView recordTime;
            @BindView(R.id.edit_image_delete)
            ImageButton editButton;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void editDataDialog(int positin) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去的和未来的。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {


                    sentenceItems = CharacterUtils.deleteSentenceItems(positin, source, sentenceItems);
                    sentenceItemAdapter.notifyDataSetChanged();
                }).show();

        //设置窗口参数
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.words_list_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //添加 新的单词
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
            case R.id.add_words_list:

                editDataDialog(false);
                break;
        }
        return true;
    }

    private void editDataDialog(boolean isEdit) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_sentence_list_add, null);

        EditText mInputSentence = view.findViewById(R.id.dialog_edit_sentence);
        EditText mInputBookSource = view.findViewById(R.id.dialog_edit_book_source);
        //EditText mInputRecordTime = view.findViewById(R.id.dialog_edit_record_time);

        if (itemPosition != -1){

            mInputSentence.setText(sentenceItems.get(itemPosition).getSentence());
            mInputBookSource.setText(sentenceItems.get(itemPosition).getBookSource());
            //mInputRecordTime.setText(sentenceItems.get(itemPosition).getRecordTime());
        }

        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setMessage("请输入你新的好句:")
                .setView(view)
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                    itemPosition = -1;
                }).setPositiveButton("确定", (dialog12, which) -> {

                    //可以在这里将输入的词根 存入到数据中去，在Fragment中 查找并进行显示
                    upDate(mInputSentence.getText().toString(), mInputBookSource.getText().toString(), isEdit);
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

    private void upDate(String sentence, String bookSource, boolean isEdit) {
        if (isEdit && bookSource.equals("刘一峰")){
            UIUtils.showToast("点击右上角添加新的条目");
            return;
        }
        sentenceItems = CharacterUtils.upDataSentenceItems(source, sentence, bookSource, sentenceItems, isEdit, itemPosition);
        recyclerView.scrollToPosition(0);
        sentenceItemAdapter.notifyDataSetChanged();

        itemPosition = -1;
    }
}
