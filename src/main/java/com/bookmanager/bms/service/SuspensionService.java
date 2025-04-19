package com.bookmanager.bms.service;

import com.bookmanager.bms.model.SuspensionRecord;

public interface SuspensionService {
    boolean canUserBorrow(Integer userId);
}
