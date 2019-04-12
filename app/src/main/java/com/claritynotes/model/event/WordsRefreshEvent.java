package com.claritynotes.model.event;

/**
 * Created by 简言 on 2019/4/2  18:30.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.model.event
 * Description :
 */
public class WordsRefreshEvent {

    private int ciGenPostion;

    private int characterPosition;

    private boolean isDelete;

    private boolean isNotify;

    public int getCiGenPostion() {
        return ciGenPostion;
    }

    public void setCiGenPostion(int ciGenPostion) {
        this.ciGenPostion = ciGenPostion;
    }

    public int getCharacterPosition() {
        return characterPosition;
    }

    public void setCharacterPosition(int characterPosition) {
        this.characterPosition = characterPosition;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }
}
