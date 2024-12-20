package com.wangpeng.bms.model;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class ReturnBook extends Process {

    private List<BookInfo> books; // 書籍列表
    private NotificationManager notificationManager;

    public ReturnBook(List<BookInfo> books, NotificationManager notificationManager) {
        this.books = books;
        this.notificationManager = notificationManager;
    }

    public Boolean performOperation(IBook book) {
        int id = book.getId();
        books.get(id).setIsborrowed((byte) 0); // 設定書籍為未借出
        return true;
    }

    public void sendNotification(IBook book) {
        int id = book.getId();
        String message = books.get(id).getBookname() + " 成功歸還!!";
        notificationManager.notifyObservers(message);
    }

    public Borrow returnecord(Borrow borrow) {
        borrow.setReturntime(new Date());
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        borrow.setReturntimestr(sdFormat.format(borrow.getReturntime()));
        System.out.println("--------------------");
        return borrow;
    }

    @Override
    Borrow Borrowrecord(User user, IBook book) {
        return null;
    }
}
