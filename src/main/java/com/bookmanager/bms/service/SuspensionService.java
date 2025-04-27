package com.bookmanager.bms.service;

import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;

import java.util.List;

public interface SuspensionService {
    /**
     * 創建停權記錄
     * @param borrow 借閱記錄
     * @return 創建的停權記錄ID
     */
    boolean createSuspensionRecord(Borrow borrow);
    
    /**
     * 檢查並更新過期的停權記錄
     * @return 更新的記錄數量
     */
    Integer checkAndUpdateExpiredSuspensions();
    
    /**
     * 獲取用戶當前有效的停權記錄
     * @param userId 用戶ID
     * @return 停權記錄，如果沒有則返回null
     */
    SuspensionRecord getUserActiveSuspension(Integer userId);

    /**
     * 檢查並處理超時未歸還的書籍及相關停權
     * 包含更新已過期的停權記錄和處理過期未還的書籍
     */
    void checkAndProcessOverdueBooks();
    
    /**
     * 處理過期書籍的歸還
     * @param borrowid 借閱ID
     * @param bookid 書籍ID
     */
    void processReturnOverdueBook(Integer borrowid, Integer bookid);

    /**
     * 獲取最新被停權狀態
     * @param userId
     * @return
     */
    SuspensionRecord getLastActiveSuspensionByUserId(Integer userId);
}
