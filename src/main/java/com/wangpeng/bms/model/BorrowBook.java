package com.wangpeng.bms.model;

import java.awt.print.Book;
import java.util.List;

import com.wangpeng.bms.exception.NotEnoughException;

public class BorrowBook extends Process{

    private List<BookInfo> books;   // 書庫清單
    private int borrowBookId;   // 借書編號
    private String username; // 借書人
    private NotificationManager notificationManager;

    public BorrowBook(List<BookInfo> books, NotificationManager notificationManager) {
        this.books = books;
        this.notificationManager = notificationManager;
    }

    public void RecordUser(User user) {
        System.out.println("借書人:" + user.getUsername());
        books.get(borrowBookId).setBorrowHistory(username); // 設定借書紀錄加入使用者名稱
    }

    public void performOperation(User user, IBook book) {
        if (books.get(borrowBookId).getIsborrowed() == 1) {
            String message = "目前無法借閱 " + books.get(borrowBookId).getBookname();
            notificationManager.notifyObservers(message);
            throw new NotEnoughException();
        }

//        寫一個查找 book id 並根據該id 刪除
        int id = book.getId();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
                books.get(borrowBookId).setIsborrowed((byte) 1); // 設定書籍為已借出
                break;
            }
        }
    }

    public void sendNotification(User user) {
        String message = "成功借閱 " + books.get(borrowBookId).getBookname();
        notificationManager.notifyObservers(message);
        System.out.println("--------------------");
    }
    
}
