package com.wangpeng.bms.model;

import java.util.List;

public class BookFactory {
    public IBook createBook(BookInfo bookInfo) {
        String material = bookInfo.getMaterial();

        switch (material.toLowerCase()) {
            case "paper":
                return new PaperBook(bookInfo, bookInfo.getPageCount());
            case "digital":
                return new EBook(bookInfo, bookInfo.getFileSize());
            case "audio":
                return new AudioBook(bookInfo, bookInfo.getNarrator(), bookInfo.getDuration());
            default:
                throw new IllegalArgumentException("Unknown material: " + material);
        }
    }

    public IBook createBookSeries(String title, List<IBook> books) {
        return new BookSeries(title, books);
    }
}
