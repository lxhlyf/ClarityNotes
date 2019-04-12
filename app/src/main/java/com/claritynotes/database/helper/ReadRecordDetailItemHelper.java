package com.claritynotes.database.helper;

import com.claritynotes.database.table.ReadBookItemTable;
import com.claritynotes.database.table.ReadRecordDetailItemTable;
import com.claritynotes.model.ReadBookItem;
import com.claritynotes.model.ReadRecordDetailItem;
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
public class ReadRecordDetailItemHelper extends BaseHelper {


    public static void save(String readRecordDetailKey, List<ReadRecordDetailItem> readRecordDetailItems) {

        String json = mGson.toJson(readRecordDetailItems);
        ReadRecordDetailItemTable table = new ReadRecordDetailItemTable(json, readRecordDetailKey);
        table.saveOrUpdate("readRecordDetailKey = ?",readRecordDetailKey);
    }

    /**
     *  返回一张 表
     * @param readRecordDetailKey
     * @return
     */
    public static List<ReadRecordDetailItemTable> getReadRecordDetailItems(String readRecordDetailKey){

        return DataSupport.where("readRecordDetailKey = ?", readRecordDetailKey).find(ReadRecordDetailItemTable.class);
    }

    /**
     *  删除 表中所有数据
     * @param readRecordDetailKey
     * @return
     */
    public static boolean deleteAll(String readRecordDetailKey){

        int i = DataSupport.deleteAll(ReadRecordDetailItemTable.class, "readRecordDetailKey = ?", readRecordDetailKey);
        return i >= 0;
    }


    public static List<ReadRecordDetailItem> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<ReadRecordDetailItem>>() {
        }.getType());
    }
}
