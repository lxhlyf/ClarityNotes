package com.claritynotes.ui.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.app.MyApp;
import com.claritynotes.base.BaseFragment;
import com.claritynotes.model.CharactorList;

import com.claritynotes.model.event.WordsRefreshEvent;
import com.claritynotes.ui.adapter.WordsAdapter;
import com.claritynotes.ui.utils.CharacterUtils;
import com.claritynotes.utils.UIUtils;
import com.claritynotes.widgets.MyRecyclerView;
import com.gavin.com.library.PowerfulStickyDecoration;

import com.gavin.com.library.listener.PowerGroupListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * Created by 简言 on 2019/3/29  16:48.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.fragment
 * Description :
 */
public class WordsFragment extends BaseFragment {

    private String str = this.getClass().getSimpleName();
    @BindView(R.id.fragment_words_rv)
    MyRecyclerView recyclerView;

    private WordsAdapter wordsAdapter;
    private List<CharactorList> dataList = new ArrayList<>();
    private PowerfulStickyDecoration decoration;
    private PowerfulStickyDecoration.Builder decorationBuilder;
    private LinearLayoutManager linearLayoutManager;
    private boolean isExpended;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_words;
    }

    @Override
    protected void initView(View rootView) {

        dataList = CharacterUtils.getCiGenByCList();

        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //------------- PowerfulStickyDecoration 使用部分  ----------------
        decorationBuilder = PowerfulStickyDecoration.Builder
                .init(new PowerGroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //获取组名，用于判断是否是同一组
                        if (dataList.size() > position) {
                            return dataList.get(position).getCharactor();
                        }
                        return null;
                    }

                    @Override
                    public View getGroupView(int position) {
                        //获取自定定义的组View
                        if (dataList.size() > position) {
                            final View view = getLayoutInflater().inflate(R.layout.charactor_group, null, false);
                            ((TextView) view.findViewById(R.id.tv)).setText(dataList.get(position).getCharactor());
                            ImageView imageView = (ImageView) view.findViewById(R.id.iv);
                            return view;
                        } else {
                            return null;
                        }
                    }
                })
                .setCacheEnable(true)
                .setGroupHeight(UIUtils.dip2px(mActivity, 40))
                .setOnClickListener((position, id) -> {

                    if (dataList.get(position).getCiGenLists() == null || dataList.get(position).getCiGenLists().size() <= 0) {

                        UIUtils.showToast(dataList.get(position).getCharactor() + ":还没有添加数据!");
                        return;
                    }

                    if (dataList.size() > position) {
                        //修改数据
                        changeExpandedState(position);
                        CharactorList charactorList = dataList.get(position);
                        //修改悬浮窗
                        //final View view = getLayoutInflater().inflate(R.layout.charactor_group, null, false);
                        View view = LayoutInflater.from(mActivity).inflate(R.layout.charactor_group, null, false);
                        //设置对应的字母
                        ((TextView) view.findViewById(R.id.tv)).setText(dataList.get(position).getCharactor());
                        //左边的图标
                        ImageView imageView = view.findViewById(R.id.iv);
                        //设置左边图标
                        //imageView.setImageResource(dataList.get(position).getIcon());
                        //设置右边图标
                        ImageView ivExpanded = view.findViewById(R.id.iv_expanded);
                        if (isExpended) {
                            //设置为闭合， 默认为闭合的
                            isExpended = false;
                            ivExpanded.setImageResource(R.drawable.ic_decoration_dismiss);
                        } else {
                            //设置为展开
                            isExpended = true;
                            ivExpanded.setImageResource(R.drawable.ic_decoratin_expend);
                        }

                        float rotation = charactorList.isExpanded() ? 180f : 360f;
                        imageView.setRotation(rotation);
                        //修改数据后，刷新指定的悬浮窗
                        decoration.notifyRedraw(recyclerView, view, position);
                        wordsAdapter.notifyChangeItem(position);
                    }
                });

        showBackgroundByTheme();

        decoration = decorationBuilder.build();

        recyclerView.addItemDecoration(decoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        wordsAdapter = new WordsAdapter(getActivity(), dataList);
        recyclerView.setAdapter(wordsAdapter);
    }

    private void showBackgroundByTheme() {

        if (ateKey.equals(light_theme)) {

            //set decoration's background
            decorationBuilder.setGroupBackground(getResources().getColor(R.color.radio_bg_group));
        }
    }

    /**
     * 修改数据
     *
     * @param position
     */
    private void changeExpandedState(int position) {
        if (dataList.size() > position) {
            CharactorList charactorList = dataList.get(position);
            charactorList.setExpanded(!charactorList.isExpanded());

            //当我们将 isExpanded 这个值改变之后，应该更新数据库中的值
            CharacterUtils.updataCharactorList(charactorList.isExpanded(), dataList);
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
        //this line has to be set , otherwise the menu can't show;
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.words_fragment, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 设置 Setting的监听器
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_ci_gen:
                ciGenPostion = -1;
                editDataDialog(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus(this);
    }

    private int ciGenPostion = -1;
    private int characterPosition;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(WordsRefreshEvent result) {


        if (result.isDelete()) { //是删除

            editDataDialog(result.getCharacterPosition(), result.getCiGenPostion());
        } else if (result.isNotify()) {

            wordsAdapter.notifyChangeItem(result.getCharacterPosition());
        } else {//是编辑

            ciGenPostion = result.getCiGenPostion();
            characterPosition = result.getCharacterPosition();
            //只能在这里绑定数据。
            editDataDialog(true);
        }
    }

    private void editDataDialog(int characterPosition, int ciGenPostion) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去的和未来的。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {


                    dataList = CharacterUtils.deleteCiGenItem(characterPosition,ciGenPostion, dataList);
                    wordsAdapter.notifyChangeAll();
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

    private void editDataDialog(boolean isEdit) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.dialog_cig_gen_add, null);
        EditText mInputAdress = view.findViewById(R.id.dialog_edit_text);
        EditText mInputTranslation = view.findViewById(R.id.dialog_edit_word_translation);

        if (ciGenPostion != -1) {

            mInputAdress.setText(dataList.get(characterPosition).getCiGenLists().get(ciGenPostion).getCiGen());
            mInputTranslation.setText(dataList.get(characterPosition).getCiGenLists().get(ciGenPostion).getTranslation());
        }

        AlertDialog dialog = showDialog("请输入你新的词根:", view).setPositiveButton("确定", (dialog1, which) -> {

            //可以在这里将输入的词根 存入到数据中去，在Fragment中 查找并进行显示
            if (TextUtils.isEmpty(mInputAdress.getText().toString()) || TextUtils.isEmpty(mInputTranslation.getText().toString())) {

                UIUtils.showToast("请填写完整，方便日后复习");
                ciGenPostion = -1;
                return;
            }
            updateData(mInputAdress, mInputTranslation, isEdit);
        }).show();

        setWindowDialogParam(dialog);
    }

    private void updateData(EditText inputAdress, EditText mInputTranslation, boolean isEdit) {
        if (isEdit && mInputTranslation.getText().toString().equals("请输入词根的含义")) {

            UIUtils.showToast("请添加数据");
            return;
        }
        dataList = CharacterUtils.upDateCiGenItem(inputAdress.getText().toString(), mInputTranslation.getText().toString(), dataList, isEdit, ciGenPostion, characterPosition);
        wordsAdapter.notifyChangeAll();
        ciGenPostion = -1;
    }
}