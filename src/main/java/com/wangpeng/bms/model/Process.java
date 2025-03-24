package com.wangpeng.bms.model;

import java.util.HashMap;

// BookTemplate.java
public abstract class Process {
    
    // Template Method: 定義固定的執行流程
    public final void process(IBook book, User user) {
        if(!performOperation(book)) // 檢查是否能執行操作
            return;     
        sendNotification(user,book);     // 發送通知
        record(user,book);    // 紀錄使用者操作
    }

    // 抽象方法: 子類別必須實作
    abstract Boolean performOperation(IBook book);
    abstract void sendNotification(User user,IBook book);
    abstract void record(User user, IBook book);
    
}
