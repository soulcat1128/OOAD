package com.wangpeng.bms.model;

import java.util.ArrayList;
import java.util.List;

public class AdminObserver implements Observer {
    private List<String> receivedMessages = new ArrayList<>();

    @Override
    public void update(String message) {
        receivedMessages.add(message);
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}
