package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class AudioBook implements IBook{
    private String narrator;
    private Integer duration;
    public BookInfo bookInfo;

    public AudioBook(BookInfo bookInfo, String narrator, Integer duration) {
        this.bookInfo = bookInfo;
        this.narrator = narrator;
        this.duration = duration;
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
        return null;
    }

    @Override
    public String getMaterial() {
        return "audio";
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

    public String getNarrator() {
        return narrator;
    }

    public Integer getDuration() {
        return duration;
    }
}
