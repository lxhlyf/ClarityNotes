package com.claritynotes.database.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 简言 on 2019/4/6  21:13.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.tabl
 * Description :
 */
public class PhraseWordsListByCharacterTable extends DataSupport {

    private String json;
    private String ciGen;
    private String phraseKey;

    public PhraseWordsListByCharacterTable(String json, String ciGen, String phraseKey) {
        this.json = json;
        this.ciGen = ciGen;
        this.phraseKey = phraseKey;
    }

    public String getCiGen() {
        return ciGen;
    }

    public void setCiGen(String ciGen) {
        this.ciGen = ciGen;
    }



    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getPhraseKey() {
        return phraseKey;
    }

    public void setPhraseKey(String phraseKey) {
        this.phraseKey = phraseKey;
    }
}
