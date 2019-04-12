package com.claritynotes.database.helper;

import com.claritynotes.daily.data.DailyItem;
import com.claritynotes.database.table.DailyItemTable;
import com.claritynotes.database.table.SentenceAuthorSourceTable;
import com.claritynotes.model.SentenceAuthorSource;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 简言 on 2019/4/9  8:17.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.helper
 * Description :
 */
public class DailyItemHelper extends BaseHelper{

    public static void save(String dailyItemKey, List<DailyItem> dailyItems) {

        String json = mGson.toJson(dailyItems);
        DailyItemTable table = new DailyItemTable(json, dailyItemKey);
        table.saveOrUpdate("dailyItemKey = ?",dailyItemKey);
    }

    /**
     *  返回一张 表
     * @param dailyItemKey
     * @return
     */
    public static List<DailyItemTable> getDailyItemsTableList(String dailyItemKey){

        return DataSupport.where("dailyItemKey = ?", dailyItemKey).find(DailyItemTable.class);
    }

    public static boolean deleteAll(String dailyItemKey){

        int i = DataSupport.deleteAll(DailyItemTable.class, "dailyItemKey = ?", dailyItemKey);
        return i > 0;
    }


    public static List<DailyItem> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<DailyItem>>() {
        }.getType());
    }
}
