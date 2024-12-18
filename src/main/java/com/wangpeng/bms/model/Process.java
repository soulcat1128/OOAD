package com.wangpeng.bms.model;

// BookTemplate.java
public abstract class Process {
    // Template Method: 定義固定的執行流程
    public final void process() {
        RecordUser();       // 紀錄使用者操作
        performOperation();   // 子類別實作的具體邏輯
        sendNotification();   // 發送通知
    }

    // 抽象方法: 子類別必須實作
    abstract void RecordUser();
    abstract void performOperation();
    abstract void sendNotification();
}