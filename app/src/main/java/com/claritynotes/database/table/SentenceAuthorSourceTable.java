package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/6  18:21.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class SentenceAuthorSourceTable extends DataSupport {

    private String sentenceASTKey;
    private String json;

    public SentenceAuthorSourceTable(String json, String sentenceASTKey) {
        this.sentenceASTKey = sentenceASTKey;
        this.json = json;
    }

    public String getSentenceASTKey() {
        return sentenceASTKey;
    }

    public void setSentenceASTKey(String sentenceASTKey) {
        this.sentenceASTKey = sentenceASTKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
