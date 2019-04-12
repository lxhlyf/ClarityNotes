package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/11  10:00.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class ReadBookItemTable extends DataSupport {

    private String readBookItemKey;

    private String json;

    public ReadBookItemTable( String json, String readBookItemKey) {
        this.readBookItemKey = readBookItemKey;
        this.json = json;
    }

    public String getReadBookItemKey() {
        return readBookItemKey;
    }

    public void setReadBookItemKey(String readBookItemKey) {
        this.readBookItemKey = readBookItemKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
