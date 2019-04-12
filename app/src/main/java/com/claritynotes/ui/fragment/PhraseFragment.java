package com.claritynotes.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseFragment;
import com.claritynotes.model.PhraseTranslationItem;
import com.claritynotes.model.PhraseWordsListByCharacter;
import com.claritynotes.ui.activity.PhraseWordsListDetailActivity;
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
public class PhraseFragment extends BaseFragment {

    private String str = this.getClass().getSimpleName();

    @BindView(R.id.container_fragment_phrase)
    LinearLayout container;
    @BindView(R.id.fragment_phrase_rv)
    RecyclerView recyclerView;

    private List<PhraseTranslationItem> phraseTranslationItems;

    private PhraseAdapter phraseAdapter;


    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_phrase;
    }

    @Override
    protected void initView(View rootView) {

        //change view's background by theme
        setBackgroundByTheme();

        phraseTranslationItems = CharacterUtils.getPhraseTranslationItems();

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        phraseAdapter = new PhraseAdapter();
        recyclerView.setAdapter(phraseAdapter);
    }

    private void setBackgroundByTheme() {

        if (ateKey.equals(light_theme)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                container.setBackground(getResources().getDrawable(R.drawable.cyq2));
            }
        }
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

        inflater.inflate(R.menu.phrase_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_phrase:

                editDataDialog(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editDataDialog(boolean isEdit) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.dialog_phrase_fragment, null);
        EditText mInputAdress = view.findViewById(R.id.dialog_edit_text);

        if (itemPosition != -1) {

            mInputAdress.setText(phraseTranslationItems.get(itemPosition).getTranslation());
        }

        AlertDialog dialog = showDialog("请输入你新的短语:", view).setPositiveButton("确定", (dialog1, which) -> {

            //可以在这里将输入的词根 存入到数据中去，在Fragment中 查找并进行显示
            upData(mInputAdress, isEdit);
        }).show();

        setWindowDialogParam(dialog);
    }

    private void upData(EditText mInputAdress, boolean isEdit) {

        if (isEdit && mInputAdress.getText().toString().equals("请输入短语的意思")) {

            UIUtils.showToast("请右上角添加新的短语");
            return;
        }
        phraseTranslationItems = CharacterUtils.upDatePhraseTranslationItem(mInputAdress.getText().toString(), phraseTranslationItems, isEdit, itemPosition);
        phraseAdapter.notifyDataSetChanged();
        itemPosition = -1;
    }

    class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.MyViewHolder> {

        @NonNull
        @Override
        public PhraseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_phrase_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhraseAdapter.MyViewHolder holder, int i) {

            if (TextUtils.isEmpty(phraseTranslationItems.get(i).getTranslation())) {

                phraseTranslationItems.remove(i);
                return;
            }

            holder.meanPhrase.setText(phraseTranslationItems.get(i).getTranslation());

            if (TextUtils.isEmpty(phraseTranslationItems.get(i).getRecordTime()))
                holder.recordTime.setVisibility(View.GONE);
            holder.recordTime.setText(phraseTranslationItems.get(i).getRecordTime());

            holder.itemView.setOnClickListener(v -> {

                //跳转到短语列表中去

                Intent intent = new Intent(mActivity, PhraseWordsListDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "短语列表");
                //bundle.putParcelableArrayList("phraseOrWords", phraseWordsList);
                bundle.putString("phraseMean", phraseTranslationItems.get(i).getTranslation());
                intent.putExtras(bundle);
                mActivity.startActivity(intent);

            });

            holder.itemView.setOnLongClickListener(v -> {

                itemPosition = i;
                //Log.e("itemPosition", ""+itemPosition);
                editDataDialog(true);
                return true;
            });

            holder.editButton.setOnClickListener(v -> {

                if (phraseTranslationItems.get(i).getTranslation().equals("请输入短语的意思")) {
                    UIUtils.showToast("示例不可删除");
                    return;
                }

                editDataDialog(i);
            });
        }

        @Override
        public int getItemCount() {
            return phraseTranslationItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.mean_phrase)
            TextView meanPhrase;
            @BindView(R.id.record_time)
            TextView recordTime;
            @BindView(R.id.adapter_phrase_item_cv)
            CardView apiCv;
            @BindView(R.id.edit_image_delete)
            ImageButton editButton;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                if (ateKey == light_theme) {
                    apiCv.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                } else {

                }
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


                    phraseTranslationItems = CharacterUtils.delePhraseTransition(positin, phraseTranslationItems.get(positin).getTranslation(), phraseTranslationItems);
                    phraseAdapter.notifyDataSetChanged();
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
