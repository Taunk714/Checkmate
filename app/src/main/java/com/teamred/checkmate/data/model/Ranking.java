package com.teamred.checkmate.data.model;

public enum Ranking {

    Newest("desc", "createDate", "Newest"),
    Oldest("asc", "createDate", "Oldest"),
    Default("", "", "Default"),
    Popular("desc", "memberNum", "Most Popular");

    private String order;
    private String attr;
    private String display;

    Ranking(String order, String attr, String display) {
        this.order = order;
        this.attr = attr;
        this.display = display;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
