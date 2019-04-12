package com.claritynotes.model;

import java.util.ArrayList;

/**
 * Created by 简言 on 2019/4/2  20:46.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description :  句子的作者和来源， 对应于 SentenceFragment
 */
public class SentenceAuthorSource {

    private String author;
    private String source;
    private String content;

    private String recordTime;

    //private ArrayList<SentenceItem> sentenceItemList = new ArrayList<>();

    public SentenceAuthorSource(String author, String source, String content, String recordTime) {
        this.author = author;
        this.source = source;
        this.content = content;
        this.recordTime = recordTime;
    }

  /*  public ArrayList<SentenceItem> getSentenceItemList() {
        return sentenceItemList;
    }

    public void setSentenceItemList(SentenceItem sentenceItem) {
        this.sentenceItemList.add(sentenceItem);
    }*/

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
