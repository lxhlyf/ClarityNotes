package com.claritynotes.database.helper;

import com.claritynotes.daily.data.DailyContent;
import com.claritynotes.daily.data.DailyItemDetail;
import com.claritynotes.database.table.DailyItemContentTable;
import com.claritynotes.database.table.DailyItemTable;

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
public class DailyItemContentHelper extends BaseHelper{

    public static void save(String contentKay, DailyContent dailyContent) {

        String json = mGson.toJson(dailyContent);
        DailyItemContentTable table = new DailyItemContentTable(json, contentKay);
        table.saveOrUpdate("contentKay = ?",contentKay);
    }

    /**
     *  返回一张 表
     * @param contentKay
     * @return
     */
    public static List<DailyItemContentTable> getDailyItemsDetailTableList(String contentKay){

        return DataSupport.where("contentKay = ?", contentKay).find(DailyItemContentTable.class);
    }

    public static boolean deleteAll(String contentKay){

        int i = DataSupport.deleteAll(DailyItemContentTable.class, "contentKay = ?", contentKay);
        return i > 0;
    }


    public static DailyContent convertToDailyItemDetail(String json) {
        return mGson.fromJson(json, DailyContent.class);
    }
}
