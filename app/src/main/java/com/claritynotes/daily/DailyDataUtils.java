package com.claritynotes.daily;

import android.util.Log;

import com.claritynotes.daily.data.DailyContent;
import com.claritynotes.daily.data.DailyItem;
import com.claritynotes.daily.data.DailyItemDetail;
import com.claritynotes.data.constant.Constant;
import com.claritynotes.database.helper.DailyItemContentHelper;
import com.claritynotes.database.helper.DailyItemHelper;
import com.claritynotes.database.table.DailyItemContentTable;
import com.claritynotes.database.table.DailyItemTable;
import com.claritynotes.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by 简言 on 2019/4/8  9:16.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.daily
 * Description :
 */
public class DailyDataUtils {

    private static String TAG = "DailyDataUtils";

    public static List<DailyItem> getDailyData() {

        List<DailyItem> items = new ArrayList<>();

        String nowDay = Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(0, 10);

        //从数据库中获取 存储的最近一天
        List<DailyItemTable> tables = DailyItemHelper.getDailyItemsTableList(Constant.DAILY_TIME_KEY);
        if (tables != null && tables.size() > 0) {
            Log.e(TAG, "getDailyData--从数据库获取");
            if (!nowDay.equals(DailyItemHelper.convertToNewsList(tables.get(0).getJson()).get(0).getRecordMonthDay())) {
                DailyItem item = new DailyItem(nowDay, DateUtils.toDateStr(new Date()).substring(11), "今日还没有书写!!");
                item.setDailyItemDetails(new DailyItemDetail("今日还没有书写!!", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11), null));
                items.addAll(DailyItemHelper.convertToNewsList(tables.get(0).getJson()));
                if (items.get(0).getDailyItemDetails().get(0).getContent().equals("今日还没有书写!!")){

                    items.remove(0);
                }
                items.add(0, item);

                if (DailyItemHelper.deleteAll(Constant.DAILY_TIME_KEY)) {

                    DailyItemHelper.save(Constant.DAILY_TIME_KEY, items);
                }
                return items;
            }
            //当前后俩次的时间项相等的时候调用
            items.addAll(DailyItemHelper.convertToNewsList(tables.get(0).getJson()));
            return items;
        } else {


            Log.e(TAG, "getDailyData--实例化数据");

            //数据库中没有数据时， 执行这里,  用户在哪天第一次使用， 就显示那一天的时间
            DailyItem item = new DailyItem(nowDay, Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11), "今日还没有书写!!");
            item.setDailyItemDetails(new DailyItemDetail("今日还没有书写!!", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11), null));
            items.add(0, item);

            //在这里进行保存
            DailyItemHelper.save(Constant.DAILY_TIME_KEY, items);
        }
        return items;
    }

    public static List<DailyItemDetail> getDailyItemsDetail() {

        List<DailyItemDetail> dailyItemDetails = new ArrayList<>();

        DailyItemDetail dailyItemDetail = new DailyItemDetail("今日还没有书写!!", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11), null);
        dailyItemDetails.add(0, dailyItemDetail);

        return dailyItemDetails;
    }

    public static List<DailyItem> updateDailyItems(String dailyBrify, List<DailyItem> dailyItems, boolean isEdit, int mDailyPosition, int mDetailPosition) {

        if (!isEdit){ //添加状态

            //在更改数据之后，就需要将虚拟的数据，删除掉
            if (dailyItems.get(0).getDailyItemDetails().get(0).getContent().equals("今日还没有书写!!")) {

                dailyItems.get(0).getDailyItemDetails().remove(0);
            }

            DailyItemDetail dailyItemDetail = new DailyItemDetail(dailyBrify, Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11), null);
            dailyItems.get(mDailyPosition).setDailyItemDetails(dailyItemDetail);
        }else {
            //编辑状态
            dailyItems.get(mDailyPosition).getDailyItemDetails().get(mDetailPosition).setContent(dailyBrify);
        }

        //重新向数据库中 保存数据
        if (DailyItemHelper.deleteAll(Constant.DAILY_TIME_KEY)) {

            DailyItemHelper.save(Constant.DAILY_TIME_KEY, dailyItems);
        }
        return dailyItems;
    }

    public static DailyContent getDailyEditData(String contentKay) {

        //从数据库中 查找数据
        List<DailyItemContentTable> tables = DailyItemContentHelper.getDailyItemsDetailTableList(contentKay);
        if (tables != null && tables.size() > 0) {

            return DailyItemContentHelper.convertToDailyItemDetail(tables.get(0).getJson());
        }
        return null;
    }

    /**
     * @param contentKey
     * @param dailyItems
     * @param dailyItemPosition 对应概略页 的页码
     * @param position          对应详情页的 页码
     * @return
     */
    public static List<DailyItem> deleteDailyItem(String contentKey, List<DailyItem> dailyItems, int position, int dailyItemPosition) {

        //删掉 对应的日记详情
        DailyItemContentHelper.deleteAll(contentKey);


        dailyItems.get(dailyItemPosition).getDailyItemDetails().remove(position);

        if (dailyItems.get(dailyItemPosition).getDailyItemDetails().size() == 0) {

            DailyItemDetail dailyItemDetail = new DailyItemDetail("今日还没有书写!!", Objects.requireNonNull(DateUtils.toDateStr(new Date())).substring(11), null);
            dailyItems.get(dailyItemPosition).setDailyItemDetails(dailyItemDetail);
          }

        //删掉对应的 日记概略
        if (DailyItemHelper.deleteAll(Constant.DAILY_TIME_KEY)) {

            DailyItemHelper.save(Constant.DAILY_TIME_KEY, dailyItems);
        }

        return dailyItems;
    }
}
