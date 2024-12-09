package com.wangpeng.bms.model;

import com.wangpeng.bms.model.template.*;

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
                throw new IllegalArgumentException("Can't create book, Unknown material: " + material);
        }
    }

    public IBook createBookSeries(String title, List<IBook> books) {
        return new BookSeries(title, books);
    }

    public IBookDisplayTemplate createBookDisplay(IBook book) {
        String material = book.getMaterial();

        switch (material.toLowerCase()) {
            case "paper":
                return new PaperBookDisplay(book);
            case "digital":
                return new EBookDisplay(book);
            case "audio":
                return new AudioBookDisplay(book);
            default:
                throw new IllegalArgumentException("Can't display, Unknown material: " + material);
        }
    }
}
