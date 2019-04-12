package com.claritynotes.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.model.CharactorList;
import com.claritynotes.model.CiGenList;
import com.claritynotes.model.PhraseWordsListByCharacter;
import com.claritynotes.model.event.WordsRefreshEvent;
import com.claritynotes.ui.activity.PhraseWordsListDetailActivity;
import com.claritynotes.utils.UIUtils;
import com.claritynotes.widgets.SlideItemLayout;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 简言 on 2019/3/31  8:48.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.adapter
 * Description :
 */
public class CiGenItemAdapter extends RecyclerView.Adapter<CiGenItemAdapter.MyViewHolder> {

    private final Context mContext;
    private final CharactorList mCharactorList;

    private int position;

    protected int viewHeight;

    public CiGenItemAdapter(Context context, CharactorList charactorList, int position) {

        this.mContext = context;
        this.mCharactorList = charactorList;
        this.position = position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_conplex_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        //this.viewHeight = holder.itemView.getHeight();

        if (TextUtils.isEmpty(mCharactorList.getCiGenLists().get(i).getCiGen())) {
            return;
        }
        holder.ciGenText.setText(mCharactorList.getCiGenLists().get(i).getCiGen());

        if (TextUtils.isEmpty(mCharactorList.getCiGenLists().get(i).getTranslation()))
            holder.ciGenTranslation.setVisibility(View.GONE);
        holder.ciGenTranslation.setText(mCharactorList.getCiGenLists().get(i).getTranslation());

        if (TextUtils.isEmpty(mCharactorList.getCiGenLists().get(i).getRecordTime()))
            holder.recordTime.setVisibility(View.GONE);
        holder.recordTime.setText(mCharactorList.getCiGenLists().get(i).getRecordTime());

        holder.itemView.setOnClickListener(v -> {

            //ArrayList<PhraseWordsListByCharacter> phraseWordsList = mCharactorList.getCiGenLists().get(i).getPhraseWordsList();

            //跳转到单词详情页上去
            Intent intent = new Intent(mContext, PhraseWordsListDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", "单词列表");
            //bundle.putString("character", mCharactorList.getCharactor());
            bundle.putString("ciGen", mCharactorList.getCiGenLists().get(i).getCiGen());
            //bundle.putParcelableArrayList("phraseOrWords", phraseWordsList);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            WordsRefreshEvent event = new WordsRefreshEvent();
            event.setCiGenPostion(i);
            event.setCharacterPosition(position);
            event.setDelete(false);
            EventBus.getDefault().post(event);
            return true;
        });

        //删除按钮的点击事件
        holder.menuTv.setOnClickListener(v -> {

            if (mCharactorList.getCiGenLists().get(i).getTranslation().equals("请输入词根的含义")){
                UIUtils.showToast("示例条目不可以删除");
                return;
            }

            WordsRefreshEvent event = new WordsRefreshEvent();
            event.setCiGenPostion(i);
            event.setCharacterPosition(position);
            event.setDelete(true);
            EventBus.getDefault().post(event);
            holder.slideItemLayout.closeMenu(); //关闭菜单，并且更新
        });

        holder.slideItemLayout.setOnStateChangeListener(new MyOnStateChangeListenter());
    }

    /**
     * 匿名内部类不知道，为什么不行
     */
    private SlideItemLayout slideLayout; //保持可变性
    class MyOnStateChangeListenter implements SlideItemLayout.OnStateChangeListener {

        @Override
        public void onClose(SlideItemLayout layout) {
            if(slideLayout ==layout){
                slideLayout = null;
            }

            WordsRefreshEvent event = new WordsRefreshEvent();
            event.setCharacterPosition(position);
            event.setNotify(true);
            EventBus.getDefault().post(event);
        }

        @Override
        public void onDown(SlideItemLayout layout) {
            if(slideLayout != null && slideLayout!=layout){
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onOpen(SlideItemLayout layout) {
            slideLayout = layout;
        }
    }

    @Override
    public int getItemCount() {
        return mCharactorList.getCiGenLists().size() > 0 ? mCharactorList.getCiGenLists().size() : 0;
    }

    public void removeItem(int j) {

        mCharactorList.getCiGenLists().remove(j);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ci_gen_text)
        TextView ciGenText;
        @BindView(R.id.ci_gen_translate)
        TextView ciGenTranslation;
        @BindView(R.id.adapter_ci_gen_record)
        TextView recordTime;

        @BindView(R.id.item_menu)
        TextView menuTv;
        @BindView(R.id.slide_layout)
        SlideItemLayout slideItemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
