package com.claritynotes.ui;

import android.view.View;
import android.widget.TextView;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by 简言 on 2019/3/30  10:41.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui
 * Description :
 */
public class WordsListFragment extends BaseFragment {

    @BindView(R.id.text_words_list)
    TextView textView;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_words_list;
    }

    @Override
    protected void initView(View rootView) {

        textView.setText(this.getClass().getSimpleName());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
