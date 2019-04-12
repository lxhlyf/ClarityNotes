package com.claritynotes.model;

/**
 * Created by 简言 on 2019/4/8  17:50.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.activity
 * Description :
 */
public class ReadRecordTimeItem {

   private String names;

   private String title;

   private String recordTime;

   private String recordMonthDay;

    public ReadRecordTimeItem(String names, String title, String recordMonthDay, String recordTime) {
        this.names = names;
        this.title = title;
        this.recordMonthDay = recordMonthDay;
        this.recordTime = recordTime;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecordMonthDay() {
        return recordMonthDay;
    }

    public void setRecordMonthDay(String recordMonthDay) {
        this.recordMonthDay = recordMonthDay;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
