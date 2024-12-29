package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class EBook implements IBook{
    public BookInfo bookInfo;
    private long prefix = 10;
    Integer fileSize;
    String name;
    String author;
    BigDecimal price;
    String desc;
    Integer bookId;
    String bookImg;
    Integer bookTypeId;
    Byte BorrowedStatus;

    public EBook(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        this.name = bookInfo.getBookname();
        this.author = bookInfo.getBookauthor();
        this.price = bookInfo.getBookprice();
        this.desc = bookInfo.getBookdesc();
        this.bookId = bookInfo.getBookid();
        this.bookImg = bookInfo.getBookimg();
        this.bookTypeId = bookInfo.getBooktypeid();
        this.fileSize = bookInfo.getFileSize();
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
        return "digital";
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
    public String display() {
        return "EBook: " + name + " by " + author + ", price: " + price + ", desc: " + desc + ", file size: " + fileSize;
    }

    @Override
    public long getPrefix() {
        return this.prefix;
    }

    public Integer getFileSize() { return fileSize; }


}
