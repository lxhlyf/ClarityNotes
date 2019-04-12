package com.claritynotes.model;

/**
 * Created by 简言 on 2019/4/8  17:06.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description :
 */
public class ReadBookItem {

    private String bookName;
    private String author;
    private String recordTime;

    public ReadBookItem(String bookName, String author, String recordTime) {
        this.bookName = bookName;
        this.author = author;
        this.recordTime = recordTime;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
