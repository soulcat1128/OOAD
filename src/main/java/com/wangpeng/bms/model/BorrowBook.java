package com.wangpeng.bms.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;

public class BorrowBook extends Process {

    private List<BookInfo> books; // 書籍列表
    private NotificationManager notificationManager;
    private HashMap<User, Borrow> User_Borrow;

    public BorrowBook(List<BookInfo> books, NotificationManager notificationManager,
            HashMap<User, Borrow> User_Borrow) {
        this.books = books;
        this.notificationManager = notificationManager;
        this.User_Borrow = User_Borrow;
    }

    public Boolean performOperation(IBook book) {
        System.out.println("步驟一 : 執行借書操作");
        // 如果是書籍系列檢查是否都有庫存
        if (book instanceof BookSeries) {
            String title = book.getName();
            List<IBook> bookList = ((BookSeries) book).books;
            for (IBook b : bookList) {
                if (books.get(b.getId()).getIsborrowed() == 1) {
                    String message = "目前庫存不足無法借閱 " + title + "!!";
                    notificationManager.notifyObservers(message);
                    return false;
                }
            }
            for (IBook b : bookList)
                books.get(b.getId()).setIsborrowed((byte) 1); // 設定書籍為已借出
            System.out.println(title + " 已借出");
        } else {
            int id = book.getId();
            if (books.get(id).getIsborrowed() == 1) {
                String message = "目前庫存不足無法借閱 " + books.get(id).getBookname() + "!!";
                notificationManager.notifyObservers(message);
                return false;
            }
            books.get(id).setIsborrowed((byte) 1); // 設定書籍為已借出
            System.out.println(books.get(id).getBookname() + " 已借出");
        }
        return true;
    }

    public void sendNotification(User user, IBook book) {
        System.out.println("步驟二 : 發送借書通知");
        int id = book.getId();
        String username = user.getUsername(), bookname;
        if (book instanceof BookSeries) {
            bookname = book.getName();
        } else{
            bookname = books.get(id).getBookname();
        }
        String message = username + "成功借閱 " + bookname + "!!";
        notificationManager.notifyObservers(message);
    }

    public void record(User user, IBook book) {
        System.out.println("步驟三 : 紀錄借書操作");
        int id = book.getId();
        String bookname;
        if (book instanceof BookSeries) {
            bookname = book.getName();
        } else {
            bookname = books.get(id).getBookname();
        }
        Borrow newBorrowRecord = new Borrow();
        newBorrowRecord.setBookid(id);
        newBorrowRecord.setBookname(bookname);
        if(book instanceof BookSeries)
            System.out.println("系列名稱: " + bookname);
        else  
            System.out.println("書籍名稱: " + bookname + " bookid:" + id);
        newBorrowRecord.setUserid(user.getUserid());
        newBorrowRecord.setUsername(user.getUsername());
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        newBorrowRecord.setBorrowtime(date);
        newBorrowRecord.setBorrowtimestr(sdFormat.format(date));
        System.out.println("--------------------");
        User_Borrow.put(user, newBorrowRecord);
    }
}
