package com.wangpeng.bms.model;

// BookTemplate.java
public abstract class Process {
    
    // Template Method: 定義固定的執行流程
    public final Borrow borrowprocess(IBook book, User user) {
        if(!performOperation(book))// 執行操作
           return null;
        sendNotification(book);   // 發送通知
        return Borrowrecord(user,book);       // 紀錄使用者操作
    }

    public final Borrow returnprocess(IBook book, User user, Borrow borrow) {
        if(!performOperation(book))// 執行操作
           return null;
        sendNotification(book);   // 發送通知
        return returnecord(borrow);       // 紀錄使用者操作
    }

    // 抽象方法: 子類別必須實作
    abstract Boolean performOperation(IBook book);
    abstract void sendNotification(IBook book);
    abstract Borrow Borrowrecord(User user, IBook book);
    abstract Borrow returnecord(Borrow borrow);
    
}
