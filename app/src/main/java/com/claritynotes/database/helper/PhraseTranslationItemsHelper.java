package com.claritynotes.database.helper;

import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.database.table.PhraseTranslationItemsTable;
import com.claritynotes.database.table.PhraseWordsListByCharacterTable;
import com.claritynotes.model.CharactorList;
import com.claritynotes.model.PhraseTranslationItem;
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
public class PhraseTranslationItemsHelper extends BaseHelper{


    public PhraseTranslationItemsHelper() {
        super();
    }

    public static void save(String phraseKey, List<PhraseTranslationItem> phraseTranslationItems) {

        String json = mGson.toJson(phraseTranslationItems);
        PhraseTranslationItemsTable table = new PhraseTranslationItemsTable(json, phraseKey);
        table.saveOrUpdate("phraseKey = ?",phraseKey);
    }

    /**
     *  返回一张 表
     * @param phraseKey
     * @return
     */
    public static List<PhraseTranslationItemsTable> getPhraseTranslationItemList(String phraseKey){

        return DataSupport.where("phraseKey = ?", phraseKey).find(PhraseTranslationItemsTable.class);
    }


    /**
     *  删除 单词 表中所有数据
     * @param phraseKey
     * @return
     */
    public static boolean deleteAll(String phraseKey){

        int i = DataSupport.deleteAll(PhraseTranslationItemsTable.class, "phraseKey = ?", phraseKey);
        return i >= 0;
    }

    public static List<PhraseTranslationItem> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<PhraseTranslationItem>>() {
        }.getType());
    }
}
