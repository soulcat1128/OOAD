package com.wangpeng.bms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;

public class ReturnBook extends Process {

    private List<BookInfo> books; // 書籍列表
    private NotificationManager notificationManager;
    private HashMap<User, Borrow> User_Borrow;

    public ReturnBook(List<BookInfo> books, NotificationManager notificationManager, HashMap<User, Borrow> User_Borrow) {
        this.books = books;
        this.notificationManager = notificationManager;
        this.User_Borrow = User_Borrow;
    }

    public Boolean performOperation(IBook book) {
        if (book instanceof BookSeries) {
            List<IBook> bookList = ((BookSeries) book).books;
            List<Integer> successfullyReturnedBooks = new ArrayList<>(); // 記錄成功還書的書籍ID
            try {
                for (IBook b : bookList) {
                    int id = b.getId();
                    if (books.get(id).getIsborrowed() == 0) {
                        throw new IllegalStateException("書籍 '" + books.get(id).getBookname() + "' 未借出，無法歸還!!");
                    }
                    books.get(id).setIsborrowed((byte) 0);
                    successfullyReturnedBooks.add(id); // 記錄成功還書的書籍ID
                }
            } catch (Exception e) {
                // 回滾已成功還書的書籍
                for (int id : successfullyReturnedBooks) {
                    books.get(id).setIsborrowed((byte) 1);
                }
                throw e;
            }
        } else {
            int id = book.getId();
            if (books.get(id).getIsborrowed() == 0) {
                throw new IllegalStateException("書籍 '" + books.get(id).getBookname() + "' 未借出，無法歸還!!");
            }
            books.get(id).setIsborrowed((byte) 0); // 設定書籍為未借出
        }
        return true;
    }


    public void sendNotification(User user, IBook book) {
        String username = user.getUsername(), bookname;
        if (book instanceof BookSeries) {
            bookname = book.getName();
        } else{
            int id = book.getId();
            bookname = books.get(id).getBookname();
        }
        String message = username + "成功歸還 "+ bookname + "!!";
        notificationManager.notifyObservers(message);
    }

    public void record(User user, IBook book){
        Borrow borrow = User_Borrow.get(user);
        borrow.setReturntime(new Date());
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        borrow.setReturntimestr(sdFormat.format(borrow.getReturntime()));
        User_Borrow.put(user, borrow);
        System.out.println("--------------------");
    }
}
