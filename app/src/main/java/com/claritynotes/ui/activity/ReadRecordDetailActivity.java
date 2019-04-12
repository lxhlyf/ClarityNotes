package com.claritynotes.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.button.MaterialButton;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.daily.DailyDataUtils;
import com.claritynotes.daily.data.DailyItemDetail;
import com.claritynotes.database.helper.ReadRecordDetailItemHelper;
import com.claritynotes.model.ReadRecordDetailItem;
import com.claritynotes.model.event.ReadRecordTimeEvent;
import com.claritynotes.ui.utils.BookRecordUtil;
import com.claritynotes.ui.utils.CharacterUtils;
import com.claritynotes.utils.UIUtils;
import com.claritynotes.widgets.LinedEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 简言 on 2019/4/8  19:27.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.activity
 * Description :
 */
public class ReadRecordDetailActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    @BindView(R.id.read_record_detail_rv)
    RecyclerView readRecordDetailRv;
    @BindView(R.id.iv_detail_headportrait)
    CircleImageView ivDetailHeadportrait;
    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.iv_detail_plus)
    RelativeLayout ivDetailPlus;
    @BindView(R.id.layout_main_top)
    RelativeLayout layoutMainTop;
    @BindView(R.id.edit_diary_et_chapter)
    LinedEditText editChapter;


    private List<ReadRecordDetailItem> readRecordDetailItems;

    private String names;
    private boolean isShow;
    private String title;
    private int itemPosition = -1;

    private ReadRecordDetailAdapter readRecordDetailAdapter;

    private String recordTime;

    private String contentKey = null;
    private String oldTitle;
    private String oldNames;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_read_record_detail);
        ButterKnife.bind(this);

        isShow = Objects.requireNonNull(getIntent().getExtras()).getBoolean("isShow");

        if (isShow){ //当数据已经在 time页中 进行了展示

            contentKey = getIntent().getExtras().getString("contentKey");
            //UIUtils.showToast(contentKey);
            oldTitle = getIntent().getExtras().getString("oldTitle");
            oldNames = getIntent().getExtras().getString("oldNames");
            editChapter.setText(oldTitle);
        }

        //get data  //当以添加模式的方式进入的时候，是不能从数据库中获取的
        readRecordDetailItems = BookRecordUtil.getReadRecordDetailItems(contentKey);

        readRecordDetailAdapter = new ReadRecordDetailAdapter();
        readRecordDetailRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        readRecordDetailRv.setItemAnimator(new DefaultItemAnimator());
        readRecordDetailRv.setAdapter(readRecordDetailAdapter);

        //add item's onClick
        ivDetailPlus.setOnClickListener(v -> {

            if (TextUtils.isEmpty(editChapter.getText())){

                UIUtils.showToast("请先添加一个章节");
                return;
            }

            editDataDialog(false);
        });
    }

    class ReadRecordDetailAdapter extends RecyclerView.Adapter<ReadRecordDetailAdapter.ReadRecordDetailViewHolder> {

        @NonNull
        @Override
        public ReadRecordDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ReadRecordDetailActivity.this).inflate(R.layout.dapter_read_detal_item, viewGroup, false);
            return new ReadRecordDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReadRecordDetailViewHolder holder, int i) {

            holder.readDetailItemName.setText(readRecordDetailItems.get(i).getName());

            holder.readDetailItemGander.setText(readRecordDetailItems.get(i).getGender());

            holder.recordDetailItemCharacterContent.setText(readRecordDetailItems.get(i).getCharacter());

            holder.recordDetailItemThingContent.setText(readRecordDetailItems.get(i).getThings());

            holder.itemView.setOnClickListener(v -> {

                if (readRecordDetailItems.get(i).getName().equals("刘一峰")){

                    UIUtils.showToast("请点击右上角添加新的条目");
                    return;
                }

                itemPosition = i;
                editDataDialog(true);
            });

            holder.editDelete.setOnClickListener(v -> {

                if (readRecordDetailItems.get(i).getName().equals("刘一峰")){

                    UIUtils.showToast("示例不可删除");
                    return;
                }

                editDataDialog(i,readRecordDetailItems);

            });
        }

        @Override
        public int getItemCount() {
            return readRecordDetailItems.size();
        }

        public class ReadRecordDetailViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.read_detail_item_name)
            TextView readDetailItemName;
            @BindView(R.id.read_detail_item_gander)
            TextView readDetailItemGander;
            @BindView(R.id.record_detail_item_character_content)
            TextView recordDetailItemCharacterContent;
            @BindView(R.id.record_detail_item_thing_content)
            TextView recordDetailItemThingContent;
            @BindView(R.id.edit_image_delete)
            ImageButton editDelete;

            public ReadRecordDetailViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!editChapter.getText().equals(oldTitle)){

            if (ReadRecordDetailItemHelper.deleteAll(oldTitle)) {

                ReadRecordDetailItemHelper.save(editChapter.getText().toString(), readRecordDetailItems);
            }
        }
    }


    @Override
    public void onBackPressed() {

        //从数据库中获取标题， 判断标题是否被进行了修改， 如果被进行了修改， 要做相应的处理
        title = Objects.requireNonNull(editChapter.getText()).toString();
        if (readRecordDetailItems.size() > 0) {
            names = readRecordDetailItems.get(0).getName()+" ";
            for (int i = 1; i < readRecordDetailItems.size(); i++) {

                names += readRecordDetailItems.get(i).getName() + " ";
            }
        }
        if (!isShow || !title.equals(oldTitle) || !oldNames.equals(names)) {



            if (TextUtils.isEmpty(names)){

                super.onBackPressed();
                return;
            }

            title = Objects.requireNonNull(editChapter.getText()).toString();
            //获取 名字
            ReadRecordTimeEvent event = new ReadRecordTimeEvent();
            event.setNames(names);
            event.setTitle(title);
            if (!isShow){

                event.setRecordTime(readRecordDetailItems.get(0).getRecordTime()); //只有新添加的才会走这里，所以拿的永远是最上层的那一个
            }
            event.setEdit(isShow); //isShow = true //编辑状态  isShow = false //添加章台
            EventBus.getDefault().postSticky(event);
        }
        super.onBackPressed();
    }

    private void editDataDialog(boolean isEdit) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_chapter_list_add, null);

        EditText mInputName = view.findViewById(R.id.dialog_edit_fingurn_name);
        EditText mInputGander = view.findViewById(R.id.dialog_edit_book_gander);
        EditText mInputCharacter = view.findViewById(R.id.dialog_edit_figurn_character);
        EditText mInputFiguenThings = view.findViewById(R.id.dialog_edit_record_things);

        if (itemPosition != -1) {

            mInputName.setText(readRecordDetailItems.get(itemPosition).getName());
            mInputGander.setText(readRecordDetailItems.get(itemPosition).getGender());
            mInputCharacter.setText(readRecordDetailItems.get(itemPosition).getCharacter());
            mInputFiguenThings.setText(readRecordDetailItems.get(itemPosition).getThings());
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
                    upDate(mInputName.getText().toString(), mInputGander.getText().toString(), mInputCharacter.getText().toString(), mInputFiguenThings.getText().toString(), isEdit);
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

    private void upDate(String name, String gander, String character, String things, boolean isEdit) {


        String newTitle = Objects.requireNonNull(editChapter.getText()).toString();
        readRecordDetailItems = BookRecordUtil.updataRecordDetailItems(newTitle, oldTitle, name, gander, character, things, readRecordDetailItems, isEdit, itemPosition);
        readRecordDetailRv.scrollToPosition(0);
        readRecordDetailAdapter.notifyDataSetChanged();

        itemPosition = -1;
    }

    private void editDataDialog(int positin, List<ReadRecordDetailItem> inReadRecordDetailItems) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去的和未来的。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {

                    if (TextUtils.isEmpty(contentKey)){
                        return;
                    }

                    readRecordDetailItems = BookRecordUtil.deleteReadRecordDetailItems(contentKey, positin, inReadRecordDetailItems);
                    readRecordDetailAdapter.notifyDataSetChanged();
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
