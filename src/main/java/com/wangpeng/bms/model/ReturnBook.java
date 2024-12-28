package com.wangpeng.bms.model;

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
        if(book instanceof BookSeries){
            List<IBook> bookList = ((BookSeries) book).books;
            for (IBook b : bookList) {
                books.get(b.getId()).setIsborrowed((byte) 0); // 設定書籍為未借出
            }
        }else{
            int id = book.getId();
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
