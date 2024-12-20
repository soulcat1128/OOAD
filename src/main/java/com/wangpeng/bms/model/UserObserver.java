package com.wangpeng.bms.model;

public class UserObserver implements Observer {
    String username;
    NotificationManager notificationManager;

    public UserObserver(String username, NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        notificationManager.subscribe(this);
        this.username = username;
    }

    @Override
    public void update(String message) {
        System.out.println(username + ": 收到借閱通知 "+ message);
    }
}
