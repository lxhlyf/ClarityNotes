package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/9  8:16.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class DailyItemTable extends DataSupport {

    private String json;

    private String dailyItemKey;

    public DailyItemTable(String json, String dailyItemKey) {
        this.json = json;
        this.dailyItemKey = dailyItemKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getDailyItemKey() {
        return dailyItemKey;
    }

    public void setDailyItemKey(String dailyItemKey) {
        this.dailyItemKey = dailyItemKey;
    }
}
