package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class PaperBook implements IBook {
    public BookInfo bookInfo;
    private Integer pageCount;
    private long prefix = 0;
    String name;
    String author;
    BigDecimal price;
    String desc;
    Integer bookId;
    String bookImg;
    Integer bookTypeId;
    Byte BorrowedStatus;

    public PaperBook(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        this.pageCount = bookInfo.getPageCount();
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
        return "paper";
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
    public Byte getIsBorrowed() { return BorrowedStatus; }

    @Override
    public void display() {
        System.out.println("PaperBook: " + name + " by " + author);
        System.out.println("price: $" + price);
        System.out.println("Description: " + desc);
        System.out.println("Page Count: " + pageCount + " pages");
    }

    @Override
    public long getPrefix() {
        return this.prefix;
    }

    public Integer getPageCount() {
        return pageCount;
    }


}
