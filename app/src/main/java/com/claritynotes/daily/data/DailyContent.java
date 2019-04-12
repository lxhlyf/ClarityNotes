package com.claritynotes.daily.data;

/**
 * Created by 简言 on 2019/4/9  14:19.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.daily.activity
 * Description :
 */
public class DailyContent {

    private String title;

    private String content;

    private String recordTime;

    public DailyContent(String title, String content, String recordTime) {
        this.title = title;
        this.content = content;
        this.recordTime = recordTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
