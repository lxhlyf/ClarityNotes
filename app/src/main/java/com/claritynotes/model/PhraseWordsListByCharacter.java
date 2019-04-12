package com.claritynotes.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 简言 on 2019/3/30  13:55.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description : 具体的一条短语， 对应于 PhraseWordsListDetailActivity
 */
public class PhraseWordsListByCharacter implements Parcelable {

    private String wordOrPhrase;
    private String yinbiao;
    private String translate;

    private String exampleSentence;

    private String recordTime;

    public PhraseWordsListByCharacter(String wordOrPhrase, String yinbiao, String translate, String exampleSentence, String recordTime) {
        this.wordOrPhrase = wordOrPhrase;
        this.yinbiao = yinbiao;
        this.translate = translate;
        this.exampleSentence = exampleSentence;
        this.recordTime = recordTime;
    }

    protected PhraseWordsListByCharacter(Parcel in) {
        wordOrPhrase = in.readString();
        yinbiao = in.readString();
        translate = in.readString();
        exampleSentence = in.readString();
        recordTime = in.readString();
    }

    public static final Creator<PhraseWordsListByCharacter> CREATOR = new Creator<PhraseWordsListByCharacter>() {
        @Override
        public PhraseWordsListByCharacter createFromParcel(Parcel in) {
            return new PhraseWordsListByCharacter(in);
        }

        @Override
        public PhraseWordsListByCharacter[] newArray(int size) {
            return new PhraseWordsListByCharacter[size];
        }
    };

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getWordOrPhrase() {
        return wordOrPhrase;
    }

    public void setWordOrPhrase(String wordOrPhrase) {
        this.wordOrPhrase = wordOrPhrase;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public String getYinbiao() {
        return yinbiao;
    }

    public void setYinbiao(String yinbiao) {
        this.yinbiao = yinbiao;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wordOrPhrase);
        dest.writeString(yinbiao);
        dest.writeString(translate);
        dest.writeString(exampleSentence);
        dest.writeString(recordTime);
    }
}
