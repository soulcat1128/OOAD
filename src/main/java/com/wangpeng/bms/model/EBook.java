package com.wangpeng.bms.model;

public class EBook extends Book{
    private Integer fileSize;

    public EBook(BookInfo bookInfo, Integer fileSize) {
        super(bookInfo);
        this.fileSize = fileSize;
    }

    @Override
    public String getMaterial() {
        return "digital";
    }

    public Integer getFileSize() {
        return fileSize;
    }
}
