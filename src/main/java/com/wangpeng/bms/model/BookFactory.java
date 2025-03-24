package com.wangpeng.bms.model;


import java.util.List;

public class BookFactory {
    public IBook createBook(BookInfo bookInfo) {
        String material = bookInfo.getMaterial();

        switch (material.toLowerCase()) {
            case "paper":
                return new PaperBook(bookInfo);
            case "digital":
                return new EBook(bookInfo);
            case "audio":
                return new AudioBook(bookInfo);
            default:
                throw new IllegalArgumentException("Can't create book, Unknown material: " + material);
        }
    }

    public IBook createBookSeries(String title) {
        return new BookSeries(title);
    }

}
