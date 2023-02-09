package com.zd.flowable.common;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


public class PageResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6186338203754406219L;

    private long total;

    private long totalPages;

    private Collection<T> data = new ArrayList<>(0);

    public PageResult() {

    }

    public PageResult(long total, long totalPages, Collection<T> data) {
        this.total = total;
        this.totalPages = totalPages;
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }
}
