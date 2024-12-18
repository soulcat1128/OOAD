package com.wangpeng.bms.model;

public class UserObserver implements Observer {

    @Override
    public void update(String message) {
        System.out.println(message);
    }
}
