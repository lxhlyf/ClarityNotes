package com.claritynotes.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.claritynotes.model.SentenceAuthorSource;
import com.claritynotes.model.SentenceItem;
import com.claritynotes.ui.activity.SentenceListActivity;
import com.claritynotes.ui.utils.BookRecordUtil;
import com.claritynotes.ui.utils.CharacterUtils;
import com.claritynotes.utils.UIUtils;

import java.util.ArrayList;
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
public class SentenceFragment extends BaseFragment {

    private String str = this.getClass().getSimpleName();

    @BindView(R.id.sentence_rv)
    RecyclerView recyclerView;

    private SentenceAdapter sentenceAdapter;

    private List<SentenceAuthorSource> sentenceAuthorSources;


    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_sentence;
    }

    @Override
    protected void initView(View rootView) {

        sentenceAuthorSources = CharacterUtils.getSentenceAuthorSource();

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sentenceAdapter = new SentenceAdapter();
        recyclerView.setAdapter(sentenceAdapter);
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sentence_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_sentence_item:

                //弹出对话框进行数据设置
                editDataDialog(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void editDataDialog(boolean isEdit) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.dialog_sentence_fragment_add, null);
        EditText mInputAuthor = view.findViewById(R.id.dialog_edit_author);
        EditText mInputSource = view.findViewById(R.id.dialog_edit_source);
        EditText mInputContent = view.findViewById(R.id.dialog_edit_content);

        if (itemPosition != -1){

            mInputAuthor.setText(sentenceAuthorSources.get(itemPosition).getAuthor());
            mInputSource.setText(sentenceAuthorSources.get(itemPosition).getSource());
            mInputContent.setText(sentenceAuthorSources.get(itemPosition).getContent());
        }

        AlertDialog dialog1 = showDialog("请输新增条目:", view).setPositiveButton("确定", (dialog, which) -> {

            //set arguments
            upDateSentenceItem(mInputAuthor.getText().toString(), mInputSource.getText().toString(), mInputContent.getText().toString(), isEdit);
        }).show();

        setWindowDialogParam(dialog1);
    }
    private void upDateSentenceItem(String author, String source, String content, boolean isEdit) {

        if (isEdit && author.equals("名句的作者")){
            UIUtils.showToast("请点击右上角添加新的条目");
            return;
        }
        CharacterUtils.updateSentenceAuthorSource(author, source, content,sentenceAuthorSources, isEdit, itemPosition);
        sentenceAdapter.notifyDataSetChanged();

        itemPosition = -1;
    }


    class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.MyViewHolder>{
        @NonNull
        @Override
        public SentenceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_sentence_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SentenceAdapter.MyViewHolder holder, int i) {

            if (TextUtils.isEmpty(sentenceAuthorSources.get(i).getSource()))
                sentenceAuthorSources.remove(i);

            if (TextUtils.isEmpty(sentenceAuthorSources.get(i).getAuthor()))
                holder.sentenceAuthor.setVisibility(View.GONE);
            holder.sentenceAuthor.setText(sentenceAuthorSources.get(i).getAuthor());
            holder.sentenceSource.setText(sentenceAuthorSources.get(i).getSource());

            if (TextUtils.isEmpty(sentenceAuthorSources.get(i).getContent()))
                holder.sentenceContent.setVisibility(View.GONE);
            holder.sentenceContent.setText(sentenceAuthorSources.get(i).getContent());

            if (TextUtils.isEmpty(sentenceAuthorSources.get(i).getRecordTime()))
                holder.recordTime.setVisibility(View.GONE);
            holder.recordTime.setText(sentenceAuthorSources.get(i).getRecordTime());

            holder.itemView.setOnClickListener(v -> {

                if (sentenceAuthorSources.get(i).getAuthor().equals("名句的作者")){

                    UIUtils.showToast("请点击右上角添加新的数据");
                    return;
                }

                    Intent intent = new Intent(mActivity, SentenceListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("source", sentenceAuthorSources.get(i).getSource());
                    //bundle.putParcelableArrayList("sentenceItemList", sentenceItemList);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
            });

            holder.itemView.setOnLongClickListener(v -> {

                if (sentenceAuthorSources.get(i).getAuthor().equals("名句的作者")){

                    UIUtils.showToast("请点击右上角添加新的数据");
                    return true;
                }

                itemPosition = i;
                editDataDialog(true);
                return true;
            });

            holder.editDelete.setOnClickListener(v -> {

                if (sentenceAuthorSources.get(i).getAuthor().equals("名句的作者")){

                    UIUtils.showToast("示例不可删除");
                    return;
                }

                editDataDialog(i);
            });
        }

        @Override
        public int getItemCount() {
            return sentenceAuthorSources.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id._sentence_author)
            TextView sentenceAuthor;
            @BindView(R.id.sentence_source)
            TextView sentenceSource;
            @BindView(R.id.sentence_content)
            TextView sentenceContent;
            @BindView(R.id.adapter_sentence_item_record)
            TextView recordTime;
            @BindView(R.id.edit_image_delete)
            ImageButton editDelete;

            public MyViewHolder(@NonNull View itemView) {
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


                    sentenceAuthorSources = CharacterUtils.deleteSentenceAuthorSources(positin, sentenceAuthorSources, sentenceAuthorSources.get(positin).getSource());
                    sentenceAdapter.notifyDataSetChanged();
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
