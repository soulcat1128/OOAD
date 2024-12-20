package com.wangpeng.bms.model;

// BookTemplate.java
public abstract class Process {
    // Template Method: 定義固定的執行流程
    public final Borrow process(IBook book, User user) {
        RecordUser(user);       // 紀錄使用者操作
        performOperation(user, book);   // 子類別實作的具體邏輯
        return sendNotification(user);   // 發送通知
    }

    // 抽象方法: 子類別必須實作
    abstract void RecordUser(User user);
    abstract void performOperation(User user, IBook book);
    abstract Borrow sendNotification(User user);
}