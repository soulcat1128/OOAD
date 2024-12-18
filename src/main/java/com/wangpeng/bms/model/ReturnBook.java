package com.wangpeng.bms.model;

import java.util.List;

public class ReturnBook extends Process {

    private List<BookInfo> books;   // 書庫清單
    private int borrowBookId;   //  還書編號
    private String username; // 還書人
    private NotificationManager notificationManager;
    private UserObserver User;  
    public ReturnBook(List<BookInfo> books, int borrowBookId , String username , NotificationManager notificationManager) {
        this.books = books;        
        this.borrowBookId = borrowBookId;
        this.username = username;
        this.notificationManager = notificationManager;
        User = new UserObserver();
        notificationManager.subscribe(User);
    }

    public void RecordUser() {
        System.out.println("還書人:" + username);
        books.get(borrowBookId).setReturnHistory(username); // 設定還書紀錄加入使用者名稱
    }

    public void performOperation() {
        books.get(borrowBookId).setIsborrowed((byte) 0); // 設定書籍為未借出
    }

    public void sendNotification() {
        String message = "成功歸還 " + books.get(borrowBookId).getBookname();
        notificationManager.notifyObservers(message);
        System.out.println("--------------------");
        notificationManager.unsubscribe(User);
    }
}
