package com.claritynotes.ui.utils;

import android.util.Log;

import com.claritynotes.data.constant.Constant;
import com.claritynotes.database.helper.ReadBookItemHelper;
import com.claritynotes.database.helper.ReadRecordDetailItemHelper;
import com.claritynotes.database.helper.ReadRecordTimeItemHelper;
import com.claritynotes.database.table.ReadBookItemTable;
import com.claritynotes.database.table.ReadRecordDetailItemTable;
import com.claritynotes.database.table.ReadRecordTimeItemTable;
import com.claritynotes.model.ReadBookItem;
import com.claritynotes.model.ReadRecordDetailItem;
import com.claritynotes.model.ReadRecordTimeItem;
import com.claritynotes.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by 简言 on 2019/4/8  17:09.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.utils
 * Description :
 */
public class BookRecordUtil {

    private static String TAG = "BookRecordUtil";

    public static List<ReadBookItem> getReadBookItem() {

        List<ReadBookItem> readBookItems = new ArrayList<>();

        //1.从数据库中获取数据
        List<ReadBookItemTable> tables = ReadBookItemHelper.getReadBookItems(Constant.READBOOKITEM_KEY);
        if (tables != null && tables.size() > 0) {

            Log.e(TAG, "getReadBookItem_从数据库中获取");
            readBookItems.addAll(ReadBookItemHelper.convertToNewsList(tables.get(0).getJson()));
            return readBookItems;
        }

        //2.如果数据库中没有数据，就示例化数据
        Log.e(TAG, "getReadBookItem_实例化数据");

        ReadBookItem readBookItem = new ReadBookItem("《My Book》", "刘一峰", DateUtils.toDateStr(new Date()));
        readBookItems.add(readBookItem);

        return readBookItems;
    }

    public static List<ReadBookItem> updataReadBookItem(String bookName, String bookAuthor, List<ReadBookItem> readBookItems, boolean isEdit, int itemPosition) {

        if (!isEdit) { //添加状态

            if (readBookItems.get(0).getAuthor().equals("刘一峰")) {

                readBookItems.clear();
            }

            ReadBookItem item = new ReadBookItem(bookName, bookAuthor, DateUtils.toDateStr(new Date()));
            readBookItems.add(0, item);
        } else {

            readBookItems.get(itemPosition).setBookName(bookName);
            readBookItems.get(itemPosition).setAuthor(bookAuthor);
            readBookItems.get(itemPosition).setRecordTime(DateUtils.toDateStr(new Date()));
        }

        //无论是添加新的数据，还是修改数据，都需要进行重新的保存数据
        if (ReadBookItemHelper.deleteAll(Constant.READBOOKITEM_KEY)) {

            ReadBookItemHelper.save(Constant.READBOOKITEM_KEY, readBookItems);
        }
        return readBookItems;
    }

    public static List<ReadRecordTimeItem> getReadRecordTimeItems(String bookKey) {

        List<ReadRecordTimeItem> readRecordTimeItems = new ArrayList<>();

        //从数据库中获取
        List<ReadRecordTimeItemTable> tables = ReadRecordTimeItemHelper.getReadRecordTimeItems(bookKey);
        if (tables != null && tables.size() > 0) {

            Log.e(TAG, "getReadRecordTimeItems_from database");
            readRecordTimeItems.addAll(ReadRecordTimeItemHelper.convertToNewsList(tables.get(0).getJson()));
            return readRecordTimeItems;
        }

        Log.e(TAG, "getReadRecordTimeItems_实例化数据");

        ReadRecordTimeItem item = new ReadRecordTimeItem("请点击上面添加数据", "title", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(0, 10), Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11));
        readRecordTimeItems.add(item);
        return readRecordTimeItems;
    }

    public static List<ReadRecordDetailItem> getReadRecordDetailItems(String contentKey) {

        List<ReadRecordDetailItem> readRecordDetailItems = new ArrayList<>();

        if (contentKey != null) {

            List<ReadRecordDetailItemTable> tables = ReadRecordDetailItemHelper.getReadRecordDetailItems(contentKey);
            if (tables != null && tables.size() > 0) {

                Log.e(TAG, "getReadRecordDetailItems_from database");
                readRecordDetailItems.addAll(ReadRecordDetailItemHelper.convertToNewsList(tables.get(0).getJson()));
                return readRecordDetailItems;
            }
        } else {

            Log.e(TAG, "getReadRecordDetailItems_实例化数据");
            /*ReadRecordDetailItem item = new ReadRecordDetailItem("刘一峰", "男", "正在人生的路上挣扎前行", "敦敏慎行", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11));
            readRecordDetailItems.add(0, item);*/
        }


        return readRecordDetailItems;
    }


    public static List<ReadRecordDetailItem> updataRecordDetailItems(String newTitle, String oldTitle, String name, String gander, String character, String things, List<ReadRecordDetailItem> readRecordDetailItems, boolean isEdit, int itemPosition) {


        if (!isEdit) {
//            if (readRecordDetailItems.get(0).getName().equals("刘一峰")) {
//                readRecordDetailItems.remove(0);
//            }

            ReadRecordDetailItem item = new ReadRecordDetailItem(name, gander, things, character, Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11));
            readRecordDetailItems.add(0, item);
        } else {

            readRecordDetailItems.get(itemPosition).setName(name);
            readRecordDetailItems.get(itemPosition).setGender(gander);
            readRecordDetailItems.get(itemPosition).setCharacter(character);
            readRecordDetailItems.get(itemPosition).setThings(things);
        }

        //如果 title 进行了修改的化， 需要用新的 替换 旧的
        if (ReadRecordDetailItemHelper.deleteAll(oldTitle)) {

            ReadRecordDetailItemHelper.save(newTitle, readRecordDetailItems);
        }
        return readRecordDetailItems;
    }

    public static List<ReadRecordTimeItem> deleteReadRecordTimeItems(int i, String bookKey, String contentKey, List<ReadRecordTimeItem> readRecordTimeItems) {

        readRecordTimeItems.remove(i);

        if (readRecordTimeItems.size() <= 0) {

            ReadRecordTimeItem item = new ReadRecordTimeItem("请点击上面添加数据", "title", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(0, 10), Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11));
            readRecordTimeItems.add(item);

        } else {

            if (ReadRecordTimeItemHelper.deleteAll(bookKey)) {

                ReadRecordTimeItemHelper.save(bookKey, readRecordTimeItems);
            }
        }

        ReadRecordDetailItemHelper.deleteAll(contentKey);
        return readRecordTimeItems;
    }

    public static List<ReadRecordDetailItem> deleteReadRecordDetailItems(String contentKey, int i, List<ReadRecordDetailItem> readRecordDetailItems) {

        readRecordDetailItems.remove(i);

        if (ReadRecordDetailItemHelper.deleteAll(contentKey)) {

            ReadRecordDetailItemHelper.save(contentKey, readRecordDetailItems);
        }
        return readRecordDetailItems;
    }

    public static List<ReadBookItem> deleteReadBookItems(int positin, List<ReadBookItem> readBookItems) {

        String bookKey = readBookItems.get(positin).getBookName() + readBookItems.get(positin).getAuthor();

        List<ReadRecordTimeItem> readRecordTimeItems = getReadRecordTimeItems(bookKey);

        //用一个循环去删除
        for (ReadRecordTimeItem readRecordTimeItem : readRecordTimeItems) {

            ReadRecordDetailItemHelper.deleteAll(readRecordTimeItem.getTitle());
        }

        ReadRecordTimeItemHelper.deleteAll(bookKey);

        readBookItems.remove(positin);

        if (readBookItems.size() <= 0){

            ReadBookItem readBookItem = new ReadBookItem("《My Book》", "刘一峰", DateUtils.toDateStr(new Date()));
            readBookItems.add(readBookItem);
        }else {

            if (ReadBookItemHelper.deleteAll(Constant.READBOOKITEM_KEY)) {

                ReadBookItemHelper.save(Constant.READBOOKITEM_KEY, readBookItems);
            }
        }

        return readBookItems;
    }
}
