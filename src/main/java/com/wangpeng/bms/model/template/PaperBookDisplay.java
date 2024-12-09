package com.wangpeng.bms.model.template;

import com.wangpeng.bms.model.*;

public class PaperBookDisplay implements IBookDisplayTemplate {

    IBook book;

    public PaperBookDisplay(IBook book) {
        this.book = book;
    }

    @Override
    public void displaysicInfo() {
        System.out.println("Paper Book Display");
        System.out.println("Book Name: " + book.getName());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Price: " + book.getPrice());
        System.out.println("Description: " + book.getDesc());
    }

    @Override
    public void displayDetails() {
        System.out.println("Page Count: " + ((PaperBook) book).getPageCount());
        System.out.println("Paper Type: " + "normal");
        System.out.println("Dimensions: " + "A4");
    }
}
