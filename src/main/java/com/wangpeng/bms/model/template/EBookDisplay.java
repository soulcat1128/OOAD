package com.wangpeng.bms.model.template;

import com.wangpeng.bms.model.EBook;
import com.wangpeng.bms.model.IBook;

import java.io.EOFException;

public class EBookDisplay implements IBookDisplayTemplate{
    IBook book;

    public EBookDisplay(IBook book) {
        this.book = book;
    }

    @Override
    public void displaysicInfo() {
        System.out.println("EBook Display");
        System.out.println("Book Name: " + book.getName());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Price: " + book.getPrice());
        System.out.println("Description: " + book.getDesc());
    }

    @Override
    public void displayDetails() {
        System.out.println("File Size: " + ((EBook)book).getFileSize() + " MB");
        System.out.println("Supported Formats: " + "PDF, EPUB, MOBI");
        System.out.println("Recommended Device: " + "Kindle, iPad");
    }
}
