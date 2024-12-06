package com.wangpeng.bms.model;

public class AudioBook extends Book{
    private String narrator;
    private Integer duration;

    public AudioBook(BookInfo bookInfo, String narrator, Integer duration) {
        super(bookInfo);
        this.narrator = narrator;
        this.duration = duration;
    }

    @Override
    public String getMaterial() {
        return "audio";
    }

    public String getNarrator() {
        return narrator;
    }

    public Integer getDuration() {
        return duration;
    }
}
