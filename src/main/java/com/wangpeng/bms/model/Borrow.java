package com.wangpeng.bms.model;

import java.util.Date;

public class Borrow {
    // 借書記錄的唯一標識符
    private Integer borrowid;

    // 借書用戶的唯一標識符
    private Integer userid;

    // 借書用戶的名稱
    private String username;

    // 借閱的書籍的唯一標識符
    private Integer bookid;

    // 借閱的書籍名稱
    private String bookname;

    // 借書時間
    private Date borrowtime;

    // 借書時間的字串表示
    private String borrowtimestr;

    // 還書時間
    private Date returntime;

    // 還書時間的字串表示
    private String returntimestr;

    // 獲取借書記錄的唯一標識符
    public Integer getBorrowid() {
        return borrowid;
    }

    // 設定借書記錄的唯一標識符
    public void setBorrowid(Integer borrowid) {
        this.borrowid = borrowid;
    }

    // 獲取借書用戶的唯一標識符
    public Integer getUserid() {
        return userid;
    }

    // 設定借書用戶的唯一標識符
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    // 獲取借書用戶的名稱
    public String getUsername() {
        return username;
    }

    // 設定借書用戶的名稱
    public void setUsername(String username) {
        this.username = username;
    }

    // 獲取借閱的書籍的唯一標識符
    public Integer getBookid() {
        return bookid;
    }

    // 設定借閱的書籍的唯一標識符
    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    // 獲取借閱的書籍名稱
    public String getBookname() {
        return bookname;
    }

    // 設定借閱的書籍名稱
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    // 獲取借書時間
    public Date getBorrowtime() {
        return borrowtime;
    }

    // 設定借書時間
    public void setBorrowtime(Date borrowtime) {
        this.borrowtime = borrowtime;
    }

    // 獲取借書時間的字串表示
    public String getBorrowtimestr() {
        return borrowtimestr;
    }

    // 設定借書時間的字串表示
    public void setBorrowtimestr(String borrowtimestr) {
        this.borrowtimestr = borrowtimestr;
    }

    // 獲取還書時間
    public Date getReturntime() {
        return returntime;
    }

    // 設定還書時間
    public void setReturntime(Date returntime) {
        this.returntime = returntime;
    }

    // 獲取還書時間的字串表示
    public String getReturntimestr() {
        return returntimestr;
    }

    // 設定還書時間的字串表示
    public void setReturntimestr(String returntimestr) {
        this.returntimestr = returntimestr;
    }

    @Override
    public String toString() {
        String str ;
        if(returntimestr == null){
            str = "用戶: " + username + ", 尚未歸還此書籍: "+ bookname; 
            return str;
        }
        str = "用戶: " + username + ", 書籍: " + bookname + ", 借書時間: "
                + borrowtimestr+ ", 還書時間: " + returntimestr;
        return str;
    }
}