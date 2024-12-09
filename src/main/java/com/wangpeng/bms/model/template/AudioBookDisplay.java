package com.wangpeng.bms.model.template;

import com.wangpeng.bms.model.AudioBook;
import com.wangpeng.bms.model.IBook;

public class AudioBookDisplay implements IBookDisplayTemplate{
    IBook book;

    public AudioBookDisplay(IBook book) {
        this.book = book;
    }

    @Override
    public void displaysicInfo() {
        System.out.println("Audio Book Display");
        System.out.println("Book Name: " + book.getName());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Price: " + book.getPrice());
        System.out.println("Description: " + book.getDesc());
    }

    @Override
    public void displayDetails() {
        System.out.println("Duration: " + ((AudioBook) book).getDuration() + " hours");
        System.out.println("Narrator: " + ((AudioBook) book).getNarrator());
        System.out.println("Supported Formats: " + "MP3, WAV");
    }
}
