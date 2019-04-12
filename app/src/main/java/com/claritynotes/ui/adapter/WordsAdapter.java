package com.claritynotes.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.model.CharactorList;
import com.claritynotes.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 简言 on 2019/3/29  17:28.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.adapter
 * Description :
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.MyViewHolder> {

    private Context context;
    private List<CharactorList> characters;
    private int position;
    private int positionFWFragment;

    private CiGenItemAdapter ciGenItemAdapter;
    public WordsAdapter(Context context, List<CharactorList> characters) {

        this.context = context;
        this.characters = characters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_words_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        if (characters.size() > i) {
            CharactorList charactorList = characters.get(i);
            if (charactorList.isExpanded()) {

                ciGenItemAdapter =new CiGenItemAdapter(context, characters.get(i), i);

                for (int j=0; j<charactorList.getCiGenLists().size(); j++){

                    if (TextUtils.isEmpty(charactorList.getCiGenLists().get(j).getCiGen())){

                        ciGenItemAdapter.removeItem(j);
                    }
                }

                ViewGroup.LayoutParams layoutParams = holder.mLlBg.getLayoutParams();
                //this parameter have to be defined
                layoutParams.height = (UIUtils.dip2px(context,70 * (ciGenItemAdapter.getItemCount())));
                holder.mLlBg.setLayoutParams(layoutParams);

                ciGenItemAdapter.notifyDataSetChanged();

                //这里可以定义一个RecyclerView , 陈列数据。
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                holder.recyclerView.setAdapter(ciGenItemAdapter);
            } else {

                //Log.e("else_wordsAdapter", "" + charactorList.isExpanded());
                ViewGroup.LayoutParams layoutParams = holder.mLlBg.getLayoutParams();
                layoutParams.height = UIUtils.dip2px(context, 0);
                holder.mLlBg.setLayoutParams(layoutParams);
            }
        }
    }

    public void notifyChangeItem(int position) {
        positionFWFragment = position;
        notifyItemChanged(position);
    }

    public void notifyChangeAll() {

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ll_bg)
        LinearLayout mLlBg;

        @BindView(R.id.words_adapter_rv)
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickAdapterListener onItemClickAdapterListener;

    public interface OnItemClickAdapterListener {

        void onClick(int containerFrameLayout, int position);
    }

    public void setOnItemClickAdapterListener(OnItemClickAdapterListener listener) {

        this.onItemClickAdapterListener = listener;
    }
}
