package com.zd.flowable.common;

public class PageParam {
    /**
     * 分页参数 起始页
     */
    private int pageIndex = 1;

    /**
     * 分页参数 显示数
     */
    private int pageSize = 50;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {

        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {

        this.pageSize = pageSize;
    }

}
