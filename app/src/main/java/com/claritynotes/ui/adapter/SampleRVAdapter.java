package com.claritynotes.ui.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.model.PhraseWordsListByCharacter;
import com.claritynotes.model.event.PhraseRefreshEvent;
import com.claritynotes.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aidan Follestad (afollestad)
 */
public class SampleRVAdapter extends RecyclerView.Adapter<SampleRVAdapter.SampleVH> {

    private List<PhraseWordsListByCharacter> wordOrPhrases;

    public SampleRVAdapter(List<PhraseWordsListByCharacter> wordOrPhrases) {

        this.wordOrPhrases = wordOrPhrases;
    }

    @Override
    public SampleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_rv, parent, false);
        return new SampleVH(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(SampleVH holder, int position) {

        if (TextUtils.isEmpty(wordOrPhrases.get(position).getWordOrPhrase()))
            wordOrPhrases.remove(position);
        holder.wordPhrase.setText(wordOrPhrases.get(position).getWordOrPhrase());

        if (TextUtils.isEmpty(wordOrPhrases.get(position).getYinbiao()))
            holder.yinBiao.setVisibility(View.GONE);
        holder.yinBiao.setText(wordOrPhrases.get(position).getYinbiao());

        if (TextUtils.isEmpty(wordOrPhrases.get(position).getTranslate()))
            holder.translationText.setVisibility(View.GONE);
        holder.translationText.setText(wordOrPhrases.get(position).getTranslate());

        if (TextUtils.isEmpty(wordOrPhrases.get(position).getExampleSentence()))
            holder.exampleSentence.setVisibility(View.GONE);
        holder.exampleSentence.setText(wordOrPhrases.get(position).getExampleSentence());


        Log.e("simple_record", ""+wordOrPhrases.get(position).getRecordTime());
        if (TextUtils.isEmpty(wordOrPhrases.get(position).getRecordTime()))
            holder.listItemRecordTime.setVisibility(View.GONE);
        holder.listItemRecordTime.setText(wordOrPhrases.get(position).getRecordTime());

        holder.itemView.setOnClickListener(v -> {

            PhraseRefreshEvent event = new PhraseRefreshEvent();
            event.setPostion(position);
            event.setDelete(false);
            EventBus.getDefault().post(event);
        });

        holder.editDelete.setOnClickListener(v -> {

            if (wordOrPhrases.get(position).getWordOrPhrase().equals("请输入单词") ||
                    wordOrPhrases.get(position).getWordOrPhrase().equals("请输入短语")){
                UIUtils.showToast("示例不可删除");
                return;
            }

            PhraseRefreshEvent event = new PhraseRefreshEvent();
            event.setPostion(position);
            event.setDelete(true);
            EventBus.getDefault().post(event);
        });
    }

    @Override
    public int getItemCount() {
        return wordOrPhrases.size();
    }

    public static class SampleVH extends RecyclerView.ViewHolder {

        @BindView(R.id.phrase_or_words)
        TextView wordPhrase;
        @BindView(R.id.yin_biao)
        TextView yinBiao;
        @BindView(R.id.translation)
        TextView translationText;
        @BindView(R.id.example_sentence)
        TextView exampleSentence;
        @BindView(R.id.list_item_record_time)
        TextView listItemRecordTime;
        @BindView(R.id.edit_image_delete)
        ImageButton editDelete;

        public SampleVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}