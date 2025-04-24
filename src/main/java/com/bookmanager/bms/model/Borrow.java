package com.bookmanager.bms.model;

import com.bookmanager.bms.exception.NotEnoughException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Borrow {
    private Integer borrowid;

    private Integer userid;

    private String username;

    private Integer bookid;

    private String bookname;

    private Date borrowtime;

    private String borrowtimestr;

    private Date returntime;

    private String returntimestr;

    private Date expectedReturnTime;

    private String expectedReturnTimeStr;

    private Integer isExtended;


    public Integer getBorrowid() {
        return borrowid;
    }

    public void setBorrowid(Integer borrowid) {
        this.borrowid = borrowid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public Date getBorrowtime() {
        return borrowtime;
    }

    public void setBorrowtime(Date borrowtime) {
        this.borrowtime = borrowtime;
    }

    public String getBorrowtimestr() {
        return borrowtimestr;
    }

    public void setBorrowtimestr(String borrowtimestr) {
        this.borrowtimestr = borrowtimestr;
    }

    public Date getReturntime() {
        return returntime;
    }

    public void setReturntime(Date returntime) {
        this.returntime = returntime;
    }

    public String getReturntimestr() {
        return returntimestr;
    }

    public void setReturntimestr(String returntimestr) {
        this.returntimestr = returntimestr;
    }

    public Date getExpectedReturnTime() { return expectedReturnTime; }

    public void setExpectedReturnTime(Date expectedReturnTime) { this.expectedReturnTime = expectedReturnTime; }

    public String getExpectedReturnTimeStr() { return expectedReturnTimeStr; }

    public void setExpectedReturnTimeStr(String expectedReturnTimeStr) { this.expectedReturnTimeStr = expectedReturnTimeStr; }

    public Integer getIsExtended() { return isExtended;}

    public void setIsExtended(Integer isExtended) { this.isExtended = isExtended; }

    public static Borrow newBorrow(Integer userid, Integer bookid, String username, String bookname, Date borrowtime, Date expectedReturnTime) {
        Borrow b = new Borrow();
        b.userid = userid;
        b.bookid = bookid;
        b.username = username;
        b.bookname = bookname;
        b.borrowtime = borrowtime;
        b.expectedReturnTime = expectedReturnTime;
        b.isExtended = 0;
        return b;
    }

    public boolean isReturned() {
        return returntime != null;
    }

    public boolean isOverdue(Date borrowtime) {
        return !isReturned() && borrowtime.after(expectedReturnTime);
    }

    public boolean isExtended() {
        return isExtended == 1;
    }

    public void extend() {
        if (isExtended()) {
            throw new IllegalStateException("已經延長過，無法再次延長");
        }
        if (isReturned()) {
            throw new IllegalStateException("已歸還，無法延長");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.expectedReturnTime);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        this.expectedReturnTime = cal.getTime();
        this.isExtended = 1;
    }

    public void completeReturn(Date returnTime) {
        if (isReturned()) {
            throw new NotEnoughException("已經還過了");
        }
        returntime = returnTime;
    }

    public long daysUntilDue(Date now) {
        Instant exp = expectedReturnTime.toInstant();
        Instant cur = now          .toInstant();
        if (isReturned()) {
            // 已歸還：算實際歸還時間（actualReturnTime）到到期日的差距
            // 假設你重構後把 actualReturnTime 記錄在 model，就改成 actualReturnTime.toInstant()
            return ChronoUnit.DAYS.between(exp, cur);
        }
        // 尚未歸還：算現在到到期日的差距
        return ChronoUnit.DAYS.between(cur, exp);
    }



}
