package com.claritynotes.model.event;

/**
 * Created by 简言 on 2019/4/9  7:04.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model.event
 * Description :
 */
public class DailyEvent{

    private String dailyBrify;

    private String recordTime;

    private boolean isEdit;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getDailyBrify() {
        return dailyBrify;
    }

    public void setDailyBrify(String dailyBrify) {
        this.dailyBrify = dailyBrify;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
