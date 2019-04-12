package com.claritynotes.database.helper;

import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.database.table.SentenceItemTable;
import com.claritynotes.model.CharactorList;
import com.claritynotes.model.SentenceItem;
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
public class SourceItemHelper {

    private static Gson mGson = new Gson();


    public static void save(String sourceItemKey, List<SentenceItem> sentenceItems) {

        String json = mGson.toJson(sentenceItems);
        SentenceItemTable table = new SentenceItemTable(json, sourceItemKey);
        table.saveOrUpdate("sourceItemKey = ?",sourceItemKey);
    }

    /**
     *  返回一张 表
     * @param sourceItemKey
     * @return
     */
    public static List<SentenceItemTable> getSentenceTableList(String sourceItemKey){

        return DataSupport.where("sourceItemKey = ?", sourceItemKey).find(SentenceItemTable.class);
    }

    /**
     *  删除 表中所有数据
     * @param sourceItemKey
     * @return
     */
    public static boolean deleteAll(String sourceItemKey){

        int i = DataSupport.deleteAll(SentenceItemTable.class, "sourceItemKey = ?", sourceItemKey);
        return i >= 0;
    }


    public static List<SentenceItem> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<SentenceItem>>() {
        }.getType());
    }
}
