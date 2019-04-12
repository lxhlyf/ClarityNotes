package com.claritynotes.database.helper;

import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.database.table.ReadBookItemTable;
import com.claritynotes.model.CharactorList;
import com.claritynotes.model.ReadBookItem;
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
public class ReadBookItemHelper extends BaseHelper {


    public static void save(String readBookItemKey, List<ReadBookItem> readBookItems) {

        String json = mGson.toJson(readBookItems);
        ReadBookItemTable table = new ReadBookItemTable(json, readBookItemKey);
        table.saveOrUpdate("readBookItemKey = ?",readBookItemKey);
    }

    /**
     *  返回一张 表
     * @param readBookItemKey
     * @return
     */
    public static List<ReadBookItemTable> getReadBookItems(String readBookItemKey){

        return DataSupport.where("readBookItemKey = ?", readBookItemKey).find(ReadBookItemTable.class);
    }

    /**
     *  删除 表中所有数据
     * @param readBookItemKey
     * @return
     */
    public static boolean deleteAll(String readBookItemKey){

        int i = DataSupport.deleteAll(ReadBookItemTable.class, "readBookItemKey = ?", readBookItemKey);
        return i >= 0;
    }


    public static List<ReadBookItem> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<ReadBookItem>>() {
        }.getType());
    }
}
