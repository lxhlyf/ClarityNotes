package com.claritynotes.database.helper;

import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.database.table.SentenceAuthorSourceTable;
import com.claritynotes.model.CharactorList;
import com.claritynotes.model.SentenceAuthorSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 简言 on 2019/4/6  15:17.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.helper
 * Description :
 */
public class SentenceAuthoeSourceHelper extends BaseHelper{


    public static void save(String sentenceASTKey, List<SentenceAuthorSource> sentenceAuthorSources) {

        String json = mGson.toJson(sentenceAuthorSources);
        SentenceAuthorSourceTable table = new SentenceAuthorSourceTable(json, sentenceASTKey);
        table.saveOrUpdate("sentenceASTKey = ?",sentenceASTKey);
    }

    /**
     *  返回一张 表
     * @param sentenceASTKey
     * @return
     */
    public static List<SentenceAuthorSourceTable> getsentenceASTList(String sentenceASTKey){

        return DataSupport.where("sentenceASTKey = ?", sentenceASTKey).find(SentenceAuthorSourceTable.class);
    }

    public static boolean deleteAll(String sentenceASTKey){

        int i = DataSupport.deleteAll(SentenceAuthorSourceTable.class, "sentenceASTKey = ?", sentenceASTKey);
        return i > 0;
    }


    public static List<SentenceAuthorSource> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<SentenceAuthorSource>>() {
        }.getType());
    }
}
