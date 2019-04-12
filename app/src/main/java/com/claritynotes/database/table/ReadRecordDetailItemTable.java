package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/11  10:33.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class ReadRecordDetailItemTable extends DataSupport {

    private String json;

    private String readRecordDetailKey;

    public ReadRecordDetailItemTable(String json, String readRecordDetailKey) {
        this.json = json;
        this.readRecordDetailKey = readRecordDetailKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getReadRecordDetailKey() {
        return readRecordDetailKey;
    }

    public void setReadRecordDetailKey(String readRecordDetailKey) {
        this.readRecordDetailKey = readRecordDetailKey;
    }
}
