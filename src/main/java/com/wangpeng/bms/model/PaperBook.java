package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class PaperBook implements IBook {
    public BookInfo bookInfo;
    private Integer pageCount;

    public PaperBook(BookInfo bookInfo, Integer pageCount) {
        this.bookInfo = bookInfo;
        this.pageCount = pageCount;
    }

    @Override
    public String getName() {
        return bookInfo.getBookname();
    }

    @Override
    public String getAuthor() {
        return bookInfo.getBookauthor();
    }

    @Override
    public BigDecimal getPrice() {
        return bookInfo.getBookprice();
    }

    @Override
    public String getMaterial() {
        return "paper";
    }

    @Override
    public String getDesc() {
        return bookInfo.getBookdesc();
    }

    @Override
    public Integer getId() {
        return bookInfo.getBookid();
    }

    @Override
    public String getImg() {
        return bookInfo.getBookimg();
    }

    @Override
    public Integer getTypeId() {
        return bookInfo.getBooktypeid();
    }

    public Integer getPageCount() {
        return pageCount;
    }
}
