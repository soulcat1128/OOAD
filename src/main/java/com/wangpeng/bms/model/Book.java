package com.wangpeng.bms.model;

import java.math.BigDecimal;

public abstract class Book {
    public BookInfo bookInfo;

    public Book() {}

    public Book(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public String getAuthor() {
        return bookInfo.getBookauthor();
    }

    public BigDecimal getPrice() {
        return bookInfo.getBookprice();
    }

    public String getDesc() {
        return bookInfo.getBookdesc();
    }

    public Integer getId() {
        return bookInfo.getBookid();
    }

    public String getImg() {
        return bookInfo.getBookimg();
    }

    public String getName() {
        return bookInfo.getBookname();
    }

    public Integer getTypeId() {
        return bookInfo.getBooktypeid();
    }

    abstract public String getMaterial();

    public void add(Book book) {
        throw new UnsupportedOperationException("does not support this operation.");
    }
}