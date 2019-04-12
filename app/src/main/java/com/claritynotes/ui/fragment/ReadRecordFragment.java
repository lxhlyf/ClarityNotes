package com.claritynotes.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseFragment;
import com.claritynotes.model.ReadBookItem;
import com.claritynotes.model.ReadRecordTimeItem;
import com.claritynotes.ui.activity.ReadRecordTimeActivity;
import com.claritynotes.ui.utils.BookRecordUtil;
import com.claritynotes.ui.utils.CharacterUtils;
import com.claritynotes.utils.UIUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 简言 on 2019/3/29  16:48.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.fragment
 * Description :
 */
public class ReadRecordFragment extends BaseFragment {


    private String str = this.getClass().getSimpleName();

    @BindView(R.id.read_record_rv)
    RecyclerView recyclerView;

    private ReadRecordAdapter adapter;

    private List<ReadBookItem> readBookItems;

    private ReadRecordAdapter readRecordAdapter;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_reading_writing;
    }

    @Override
    protected void initView(View rootView) {

        //获取数据
        readBookItems = BookRecordUtil.getReadBookItem();

        readRecordAdapter = new ReadRecordAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(readRecordAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);  //while activity is created , the menu will be not recreated .
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.read_record_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_record:

                editDataDialog(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editDataDialog(boolean isEdit) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.dialog_record_fragment, null);
        EditText mInputBookName = view.findViewById(R.id.dialog_edit_book_name);
        EditText mInputBookAuthor = view.findViewById(R.id.dialog_edit_book_author);

        if (itemPosition != -1) { //修改的时候进这里

            mInputBookName.setText(readBookItems.get(itemPosition).getBookName());
            mInputBookAuthor.setText(readBookItems.get(itemPosition).getAuthor());
        }

        AlertDialog dialog = showDialog("请输入你新的短语:", view).setPositiveButton("确定", (dialog1, which) -> {

            //可以在这里将输入的词根 存入到数据中去，在Fragment中 查找并进行显示
            upData(mInputBookName, mInputBookAuthor, isEdit);
        }).show();

        setWindowDialogParam(dialog);
    }

    private void upData(EditText mInputBookName, EditText mInputBookAuthor, boolean isEdit) {

        readBookItems = BookRecordUtil.updataReadBookItem(mInputBookName.getText().toString(), mInputBookAuthor.getText().toString(), readBookItems, isEdit, itemPosition);
        readRecordAdapter.notifyDataSetChanged();
        itemPosition = -1;
    }


    class ReadRecordAdapter extends RecyclerView.Adapter<ReadRecordAdapter.ReadViewHolder> {

        @NonNull
        @Override
        public ReadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_read_item, viewGroup, false);
            return new ReadViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReadViewHolder readViewHolder, int i) {

            readViewHolder.bookAuthor.setText(readBookItems.get(i).getAuthor());

            readViewHolder.bookName.setText(readBookItems.get(i).getBookName());

            readViewHolder.ItemRecordTime.setText(readBookItems.get(i).getRecordTime());

            readViewHolder.editBth.setOnClickListener(v -> {

                if (readBookItems.get(0).getAuthor().equals("刘一峰")){

                    UIUtils.showToast("请点击右上角添加新的条目");
                    return;
                }

                //跳转到编辑页面
                Intent intent = new Intent(mActivity, ReadRecordTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bookKey", readBookItems.get(i).getBookName()+readBookItems.get(i).getAuthor()); //bookKey = bookName + author
                intent.putExtras(bundle);
                startActivity(intent);
            });

            readViewHolder.itemView.setOnLongClickListener(v -> {

                if (readBookItems.get(0).getAuthor().equals("刘一峰")) {

                    UIUtils.showToast("请右上角添加新的书籍");
                    return true;
                }

                itemPosition = i;
                editDataDialog(true);
                return true;
            });

            readViewHolder.editDelete.setOnClickListener(v -> {

                if (readBookItems.get(0).getAuthor().equals("刘一峰")) {

                    UIUtils.showToast("请右上角添加新的书籍");
                    return;
                }

                editDataDialog(i);
            });
        }

        @Override
        public int getItemCount() {
            return readBookItems.size();
        }

        public class ReadViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.book_name)
            TextView bookName;
            @BindView(R.id.book_author)
            TextView bookAuthor;
            @BindView(R.id.adapter_sentence_item_record)
            TextView ItemRecordTime;
            @BindView(R.id.edit_image_btn)
            ImageButton editBth;
            @BindView(R.id.edit_image_delete)
            ImageButton editDelete;

            public ReadViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void editDataDialog(int positin) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去的和未来的。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {


                    readBookItems = BookRecordUtil.deleteReadBookItems(positin, readBookItems);
                    readRecordAdapter.notifyDataSetChanged();
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
}
