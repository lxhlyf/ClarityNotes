package com.claritynotes.model;

/**
 * Created by 简言 on 2019/4/8  19:58.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model
 * Description :
 */
public class ReadRecordDetailItem {

    private String name;
    private String gender;
    private String things;
    private String character;
    private String recordTime;

    public ReadRecordDetailItem(String name, String gender, String things, String character, String recordTime) {
        this.name = name;
        this.gender = gender;
        this.things = things;
        this.character = character;
        this.recordTime = recordTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
