package com.claritynotes.model.event;

/**
 * Created by 简言 on 2019/4/4  10:03.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model.event
 * Description :
 */
public class PhraseRefreshEvent {

    private int postion;

    private boolean isDelete;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}
