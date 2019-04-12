package com.claritynotes.model.event;

/**
 * Created by 简言 on 2019/4/8  20:28.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model.event
 * Description :
 */
public class ReadRecordTimeEvent {

    private String names;

    private String title;

    private String recordTime;

    private boolean isEdit;

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

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
