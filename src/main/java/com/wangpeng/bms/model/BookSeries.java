package com.wangpeng.bms.model;

import java.math.BigDecimal;
import java.util.List;

public class BookSeries implements IBook {
    private String title;
    public List<IBook> books;

    public BookSeries(String title, List<IBook> books) {
        this.title = title;
        this.books = books;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public BigDecimal getPrice() {
        return null;
    }

    @Override
    public  String getMaterial() {
        throw new UnsupportedOperationException("does not support this operation.");
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Integer getId() {
        return 0;
    }

    @Override
    public String getImg() {
        return null;
    }

    @Override
    public Integer getTypeId() {
        return 0;
    }

    @Override
    public void add(IBook book) {
        books.add(book);
    }

    @Override
    public void remove(IBook book) {
        books.remove(book);
    }

    public String getTitle() {
        return title;
    }

    public void printSeriesInfo() {
        System.out.println("Book Series: " + title);
        for (IBook book : books) {
            System.out.println("Book: " + book.getName());
        }
    }
}
