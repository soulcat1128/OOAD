package com.wangpeng.bms.model;

import java.util.List;

public class BookSeries extends Book {
    private String title;
    public List<Book> books;

    public BookSeries(String title, List<Book> books) {
        this.title = title;
        this.books = books;
    }

    @Override
    public  String getMaterial() {
        throw new UnsupportedOperationException("does not support this operation.");
    }

    @Override
    public void add(Book book) {
        books.add(book);
    }

    public String getTitle() {
        return title;
    }
}
