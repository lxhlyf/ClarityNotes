package com.claritynotes.model;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class CharactorList{
    /**
     * 对应字母
     */
    private String charactor;

    /**
     * 对应字母的 icon

     */
    private int icon;

    private boolean expanded = false;

    /**
     * 对应字母的 词根列表
     */
    private List<CiGenList> ciGenLists = new ArrayList<>();

    public CharactorList() {

    }

    public CharactorList(String charactor, int icon, CiGenList CiGenList) {
        this.charactor = charactor;
        this.icon = icon;
        this.ciGenLists.add(CiGenList);
    }


    public List<CiGenList> getCiGenLists() {
        return ciGenLists;
    }

    public void setCiGenLists(CiGenList ciGenList) {
        this.ciGenLists.add(ciGenList);
    }

    public String getCharactor() {
        return charactor;
    }

    public void setCharactor(String charactor) {
        this.charactor = charactor;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isExpanded() { //是否是可展开的
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "CharactorList{" +
                "charactor='" + charactor + '\'' +
                ", wordsListByCharactors=" + ciGenLists.toString() +
                ", icon=" + icon +
                ", expanded=" + expanded +
                '}';
    }
}