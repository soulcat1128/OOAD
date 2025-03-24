package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class AudioBook implements IBook{
    public BookInfo bookInfo;
    private long prefix = 5;
    String narrator;
    Integer duration;
    String name;
    String author;
    BigDecimal price;
    String desc;
    Integer bookId;
    String bookImg;
    Integer bookTypeId;
    Byte BorrowedStatus;


    public AudioBook(BookInfo bookInfo) {
        this.narrator = bookInfo.getNarrator();
        this.duration = bookInfo.getDuration();
        this.name = bookInfo.getBookname();
        this.author = bookInfo.getBookauthor();
        this.price = bookInfo.getBookprice();
        this.desc = bookInfo.getBookdesc();
        this.bookId = bookInfo.getBookid();
        this.bookImg = bookInfo.getBookimg();
        this.bookTypeId = bookInfo.getBooktypeid();
        this.BorrowedStatus = bookInfo.getIsborrowed();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String getMaterial() {
        return "audio";
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getId() {
        return bookId;
    }

    @Override
    public String getImg() {
        return bookImg;
    }

    @Override
    public Integer getTypeId() {
        return bookTypeId;
    }

    @Override
    public Byte getIsBorrowed() {
        return BorrowedStatus;
    }

    @Override
    public void display() {
        System.out.println("AudioBook: " + name + " by " + author);
        System.out.println("Price: $" + price);
        System.out.println("Description: " + desc);
        System.out.println("Narrator: " + narrator);
        System.out.println("Duration: " + duration + " minutes");
    }

    @Override
    public long getPrefix() {
        return this.prefix;
    }

    public String getNarrator() { return narrator; }

    public Integer getDuration() { return duration; }


}
