package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class EBook implements IBook{
    private Integer fileSize;
    public BookInfo bookInfo;

    public EBook(BookInfo bookInfo, Integer fileSize) {
        this.bookInfo = bookInfo;
        this.fileSize = fileSize;
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
        return "digital";
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

    public Integer getFileSize() {
        return fileSize;
    }
}
