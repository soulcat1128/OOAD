package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class PaperBook extends Book {
    private Integer pageCount;

    public PaperBook(BookInfo bookInfo, Integer pageCount) {
        super(bookInfo);
        this.pageCount = pageCount;
    }

    @Override
    public String getMaterial() {
        return "paper";
    }

    public Integer getPageCount() {
        return pageCount;
    }
}
