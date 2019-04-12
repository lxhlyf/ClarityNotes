package com.claritynotes.database.helper;

import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.model.CharactorList;
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
public class CharaterListHelper {

    private static Gson mGson = new Gson();


    public static void save(String characterKey, List<CharactorList> characterLists) {

        String json = mGson.toJson(characterLists);
        CharacterListTable table = new CharacterListTable(json, characterKey);
        table.saveOrUpdate("characterKey = ?",characterKey);
    }

    /**
     *  返回一张 表
     * @param characterKey
     * @return
     */
    public static List<CharacterListTable> getCharacterList(String characterKey){

        return DataSupport.where("characterKey = ?", characterKey).find(CharacterListTable.class);
    }

    /**
     *  删除 表中所有数据
     * @param characterKey
     * @return
     */
    public static boolean deleteAll(String characterKey){

        int i = DataSupport.deleteAll(CharacterListTable.class, "characterKey = ?", characterKey);
        return i >= 0;
    }


    public static List<CharactorList> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<CharactorList>>() {
        }.getType());
    }
}
