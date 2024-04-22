package com.kexue.common.domain;

import java.util.List;

public class PageInfo<T> {

    private Long pageNum = 1L;

    private Long pageSize = 10L;

    private Long total;

    private List<T> records;

    public PageInfo() {
    }

    public PageInfo(List<T> records, Long total) {
        this.records = records;
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
