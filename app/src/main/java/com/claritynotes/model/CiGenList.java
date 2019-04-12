package com.claritynotes.model;

import java.util.ArrayList;

/**
 * Created by 简言 on 2019/3/31  16:27.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description :
 */
public class CiGenList {

    private String ciGen;
    private String translation;

    private String recordTime;

    //private ArrayList<PhraseWordsListByCharacter> phraseWordsList = new ArrayList<>();

    public CiGenList(String ciGen, String translation, String recordTime) {

        this.ciGen = ciGen;
        this.translation = translation;
        this.recordTime = recordTime;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

//    public ArrayList<PhraseWordsListByCharacter> getPhraseWordsList() {
//        return phraseWordsList;
//    }
//
//    public void setPhraseWordsList(PhraseWordsListByCharacter phraseWordsListByCharacter) {
//        this.phraseWordsList.add(phraseWordsListByCharacter);
//    }

    public String getCiGen() {
        return ciGen;
    }

    public void setCiGen(String ciGen) {
        this.ciGen = ciGen;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
