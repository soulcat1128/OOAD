package com.wangpeng.bms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookSeries implements IBook {
    private String title;
    public List<IBook> books = new ArrayList<IBook>();

    public BookSeries(String title) {
        this.title = title;
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

    @Override
    public Byte getIsBorrowed() {
        throw new UnsupportedOperationException("does not support this operation.");
    }

    @Override
    public String display() {
        StringBuilder builder = new StringBuilder();
        builder.append("BookSeries: ").append(title).append(" contains: \n");
        for (IBook book : books) {
            if (book instanceof BookSeries) {
                String nestedDisplay = ((BookSeries) book).display();
                for (String line : nestedDisplay.split("\n")) {
                    builder.append("  ").append(line).append("\n");
                }
            } else {
                builder.append("  ").append(book.display()).append("\n");
            }
        }
        return builder.toString().replace("\r\n", "\n").trim(); // 強制標準化為 Unix 格式
    }





}
