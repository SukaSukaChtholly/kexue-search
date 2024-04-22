package com.kexue.common.util;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kexue.common.domain.PageParam;

public class PageUtils {

    private static final Long DEFAULT_PAGE_NUM = 1L;
    private static final Long DEFAULT_PAGE_SIZE = 10L;

    public static <T extends PageParam> void initPage(T t) {
        Long pageNum = t.getPageNum();
        Long pageSize = t.getPageSize();
        t.setPageNum((pageNum == null || pageNum <= 0) ? DEFAULT_PAGE_NUM : pageNum);
        t.setPageSize((pageSize == null || pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize);
    }

    public static <T> IPage<T> startPage() {
        return startPage(null, null);
    }

    public static <T> IPage<T> startPage(Long pageNum, Long pageSize) {
        pageNum = (pageNum == null || pageNum <= 0) ? DEFAULT_PAGE_NUM : pageNum;
        pageSize = (pageSize == null || pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        return Page.of(pageNum, pageSize);
    }
}
