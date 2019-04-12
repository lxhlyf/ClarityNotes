package com.claritynotes.database.helper;

import com.claritynotes.database.table.ReadBookItemTable;
import com.claritynotes.database.table.ReadRecordTimeItemTable;
import com.claritynotes.model.ReadBookItem;
import com.claritynotes.model.ReadRecordTimeItem;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 简言 on 2019/4/11  11:00.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.helper
 * Description :
 */
public class ReadRecordTimeItemHelper  extends BaseHelper{

    public static void save(String readRecordTimeKey, List<ReadRecordTimeItem> readRecordTimeItems) {

        String json = mGson.toJson(readRecordTimeItems);
        ReadRecordTimeItemTable table = new ReadRecordTimeItemTable(json, readRecordTimeKey);
        table.saveOrUpdate("readRecordTimeKey = ?",readRecordTimeKey);
    }

    /**
     *  返回一张 表
     * @param readRecordTimeKey
     * @return
     */
    public static List<ReadRecordTimeItemTable> getReadRecordTimeItems(String readRecordTimeKey){

        return DataSupport.where("readRecordTimeKey = ?", readRecordTimeKey).find(ReadRecordTimeItemTable.class);
    }

    /**
     *  删除 表中所有数据
     * @param readRecordTimeKey
     * @return
     */
    public static boolean deleteAll(String readRecordTimeKey){

        int i = DataSupport.deleteAll(ReadRecordTimeItemTable.class, "readRecordTimeKey = ?", readRecordTimeKey);
        return i >= 0;
    }


    public static List<ReadRecordTimeItem> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<ReadRecordTimeItem>>() {
        }.getType());
    }
}
