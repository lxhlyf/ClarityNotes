package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/9  13:55.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class DailyItemContentTable extends DataSupport {

    private String json;

    private String contentKay;

    public DailyItemContentTable(String json, String contentKay) {
        this.json = json;
        this.contentKay = contentKay;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getContentKay() {
        return contentKay;
    }

    public void setContentKay(String contentKay) {
        this.contentKay = contentKay;
    }
}
