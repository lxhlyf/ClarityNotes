package com.claritynotes.daily.data;

/**
 * Created by 简言 on 2019/4/8  15:26.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.daily.data
 * Description :
 */
public class DailyItemDetail {

    private String content;  //就是title

    private String recordTiem;

    private String yearMonthDay;


    public DailyItemDetail(String content, String recordTiem, String yearMonthDay) {
        this.content = content;
        this.recordTiem = recordTiem;
        this.yearMonthDay = yearMonthDay;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public void setYearMonthDay(String yearMonthDay) {
        this.yearMonthDay = yearMonthDay;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecordTiem() {
        return recordTiem;
    }

    public void setRecordTiem(String recordTiem) {
        this.recordTiem = recordTiem;
    }
}