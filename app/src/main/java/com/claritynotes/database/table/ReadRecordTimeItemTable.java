package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/11  10:59.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class ReadRecordTimeItemTable extends DataSupport {

    private String json;

    private String readRecordTimeKey;

    public ReadRecordTimeItemTable(String json, String readRecordTimeKey) {
        this.json = json;
        this.readRecordTimeKey = readRecordTimeKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getReadRecordTimeKey() {
        return readRecordTimeKey;
    }

    public void setReadRecordTimeKey(String readRecordTimeKey) {
        this.readRecordTimeKey = readRecordTimeKey;
    }
}
