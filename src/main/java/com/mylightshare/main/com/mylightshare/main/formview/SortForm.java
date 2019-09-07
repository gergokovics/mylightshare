package com.mylightshare.main.com.mylightshare.main.formview;

public class SortForm {

    private String sort;
    private String order;

    public SortForm(String sort, String order) {
        this.sort = sort;
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
