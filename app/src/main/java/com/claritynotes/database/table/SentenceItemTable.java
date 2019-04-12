package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/7  10:43.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class SentenceItemTable extends DataSupport {

    private String json;
    private String sourceItemKey;

    public SentenceItemTable(String json, String sourceItemKey) {
        this.json = json;
        this.sourceItemKey = sourceItemKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getSourceItemKey() {
        return sourceItemKey;
    }

    public void setSourceItemKey(String sourceItemKey) {
        this.sourceItemKey = sourceItemKey;
    }
}
