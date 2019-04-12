package com.claritynotes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 简言 on 2019/4/1  19:48.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description : 对应于 PhraseFragment
 */
public class PhraseTranslationItem {

    private String translation;

    private String recordTime;

   /* private ArrayList<PhraseWordsListByCharacter> phraseWordsList = new ArrayList<>();

    public ArrayList<PhraseWordsListByCharacter> getPhraseWordsList() {
        return phraseWordsList;
    }

    public void setPhraseWordsList(PhraseWordsListByCharacter phraseTranslationItem) {
        this.phraseWordsList.add(phraseTranslationItem);
    }*/

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
