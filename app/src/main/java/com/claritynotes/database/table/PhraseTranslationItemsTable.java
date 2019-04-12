package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/6  18:02.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.table
 * Description :
 */
public class PhraseTranslationItemsTable extends DataSupport {

    private String phraseKey;
    private String json;

    public PhraseTranslationItemsTable(String json, String phraseKey) {
        this.phraseKey = phraseKey;
        this.json = json;
    }

    public String getPhraseKey() {
        return phraseKey;
    }

    public void setPhraseKey(String phraseKey) {
        this.phraseKey = phraseKey;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
