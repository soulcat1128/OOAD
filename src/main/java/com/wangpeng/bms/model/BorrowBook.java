package com.wangpeng.bms.model;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class BorrowBook extends Process {

    private List<BookInfo> books; // 書籍列表
    private NotificationManager notificationManager;

    public BorrowBook(List<BookInfo> books, NotificationManager notificationManager) {
        this.books = books;
        this.notificationManager = notificationManager;
    }

    public Boolean performOperation(IBook book) {

        System.out.println("執行借書操作");
        int id = book.getId();
        if (books.get(id).getIsborrowed() == 1) {
            String message = books.get(id).getBookname() + " 目前無法借閱!!";
            notificationManager.notifyObservers(message);
            return false;
        }
        books.get(id).setIsborrowed((byte) 1); // 設定書籍為已借出
        System.out.println(books.get(id).getBookname() + " 已借出");
        return true;
    }

    public void sendNotification(IBook book) {
        System.out.println("發送借書通知");
        int id = book.getId();
        String message = books.get(id).getBookname() + " 成功借閱!!";
        notificationManager.notifyObservers(message);
    }

    public Borrow Borrowrecord(User user, IBook book) {
        System.out.println("紀錄借書操作");
        int id = book.getId();
        Borrow newBorrowRecord = new Borrow();
        newBorrowRecord.setBookid(id);
        newBorrowRecord.setBookname(books.get(id).getBookname());
        System.out.println("書籍名稱: " + books.get(id).getBookname()+" bookid:"+ id );
        newBorrowRecord.setUserid(user.getUserid());
        newBorrowRecord.setUsername(user.getUsername());
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        newBorrowRecord.setBorrowtime(date);
        newBorrowRecord.setBorrowtimestr(sdFormat.format(date));
        System.out.println("--------------------");
        return newBorrowRecord;
    }

	@Override
    Borrow returnecord(Borrow borrow){ 
        return null;
	}
}
