package com.wangpeng.bms.model;

import java.util.List;

import com.wangpeng.bms.exception.NotEnoughException;

public class BorrowBook extends Process{

    private List<BookInfo> books;   // 書庫清單
    private int borrowBookId;   // 借書編號
    private String username; // 借書人
    private NotificationManager notificationManager;
    private UserObserver User;

    public BorrowBook(List<BookInfo> books, int borrowBookId , String username , NotificationManager notificationManager) {
        this.books = books;        
        this.borrowBookId = borrowBookId;
        this.username = username;
        this.notificationManager = notificationManager;
        User = new UserObserver();
        notificationManager.subscribe(User);
    }

    public void RecordUser() { 
        System.out.println("借書人:" + username);
        books.get(borrowBookId).setBorrowHistory(username); // 設定借書紀錄加入使用者名稱
    }

    public void performOperation() {
        if (books.get(borrowBookId).getIsborrowed() == 1) {
            String message = "目前無法借閱 " + books.get(borrowBookId).getBookname();
            notificationManager.notifyObservers(message);
            throw new NotEnoughException();
        }
        books.get(borrowBookId).setIsborrowed((byte) 1); // 設定書籍為已借出
    }

    public void sendNotification() {
        String message = "成功借閱 " + books.get(borrowBookId).getBookname();
        notificationManager.notifyObservers(message);
        System.out.println("--------------------");
        notificationManager.unsubscribe(User);
    }
    
}
