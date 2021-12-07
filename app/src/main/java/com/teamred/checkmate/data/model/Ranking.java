package com.teamred.checkmate.data.model;

public enum Ranking {

    Newest("Newest", "desc", "createDate" ),
    Oldest("Oldest","asc", "createDate"),
    Default("Default"),
    Popular("Most Popular", "desc", "numMember", "desc", "numView");

    private String[] order;
//    private String attr;
    private String display;
    private int numPair;

    Ranking(String display, String... order) {
        this.order = order;
        assert order.length % 2 == 0;
//        this.attr = attr;
        this.display = display;
    }

    public String[] getOrder() {
        return order;
    }

    public void setOrder(String[] order) {
        this.order = order;
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
