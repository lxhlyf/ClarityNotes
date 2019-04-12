package com.claritynotes.daily.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 简言 on 2019/4/8  9:18.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.daily.data
 * Description :
 */
public class DailyItem {

    private String recordMonthDay;

    private String recordHourMounth;

    private String contentBrefiy;

    private List<DailyItemDetail> dailyItemDetails = new ArrayList<>();

    public DailyItem(String recordMonthDay, String recordHourMounth, String contentBrefiy) {
        this.recordMonthDay = recordMonthDay;
        this.recordHourMounth = recordHourMounth;
        this.contentBrefiy = contentBrefiy;
    }

    public List<DailyItemDetail> getDailyItemDetails() {
        return dailyItemDetails;
    }

    public void setDailyItemDetails(DailyItemDetail dailyItemDetail) {
        this.dailyItemDetails.add(0, dailyItemDetail);
    }

    public String getRecordMonthDay() {
        return recordMonthDay;
    }

    public void setRecordMonthDay(String recordMonthDay) {
        this.recordMonthDay = recordMonthDay;
    }

    public String getRecordHourMounth() {
        return recordHourMounth;
    }

    public void setRecordHourMounth(String recordHourMounth) {
        this.recordHourMounth = recordHourMounth;
    }

    public String getContentBrefiy() {
        return contentBrefiy;
    }

    public void setContentBrefiy(String contentBrefiy) {
        this.contentBrefiy = contentBrefiy;
    }
}
