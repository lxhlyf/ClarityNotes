package com.claritynotes.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 简言 on 2019/4/2  21:20.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description : 具体的句子， 对应于 SentenceActivity
 */
public class SentenceItem implements Parcelable {

    private String sentence;
    private String bookSource;
    private String recordTime;

    public SentenceItem(String sentence, String bookSource, String recordTime) {
        this.sentence = sentence;
        this.bookSource = bookSource;
        this.recordTime = recordTime;
    }

    protected SentenceItem(Parcel in) {
        sentence = in.readString();
        bookSource = in.readString();
        recordTime = in.readString();
    }

    public static final Creator<SentenceItem> CREATOR = new Creator<SentenceItem>() {
        @Override
        public SentenceItem createFromParcel(Parcel in) {
            return new SentenceItem(in);
        }

        @Override
        public SentenceItem[] newArray(int size) {
            return new SentenceItem[size];
        }
    };

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getBookSource() {
        return bookSource;
    }

    public void setBookSource(String bookSource) {
        this.bookSource = bookSource;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sentence);
        dest.writeString(bookSource);
        dest.writeString(recordTime);
    }
}
