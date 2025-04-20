package com.bookmanager.bms.service;

import com.bookmanager.bms.model.Appeal;
import com.bookmanager.bms.model.SuspensionRecord;

import java.util.List;

public interface AppealService {
    /**
     * 創建申訴
     * @param appeal 申訴記錄
     * @return 成功返回申訴ID，失敗返回0
     */
    Integer createAppeal(Appeal appeal);
    
    /**
     * 獲取指定用戶的申訴記錄
     * @param userId 用戶ID
     * @return 申訴記錄列表
     */
    List<Appeal> getUserAppeals(Integer userId);
    
    /**
     * 獲取所有申訴記錄
     * @return 所有申訴記錄
     */
    List<Appeal> getAllAppeals();
    
    /**
     * 根據狀態獲取申訴記錄
     * @param status 申訴狀態 (0: 待處理, 1: 已批准, 2: 已拒絕)
     * @return 符合條件的申訴記錄
     */
    List<Appeal> getAppealsByStatus(Byte status);
    
    /**
     * 批准申訴
     * @param appealId 申訴ID
     * @param adminReply 管理員回覆
     * @return 成功返回1，失敗返回0
     */
    Integer approveAppeal(Integer appealId, String adminReply, SuspensionRecord suspensionRecord);
    
    /**
     * 拒絕申訴
     * @param appealId 申訴ID
     * @param adminReply 管理員回覆
     * @return 成功返回1，失敗返回0
     */
    Integer rejectAppeal(Integer appealId, String adminReply);
    
    /**
     * 獲取單個申訴記錄詳情
     * @param appealId 申訴ID
     * @return 申訴記錄
     */
    Appeal getAppealById(Integer appealId);
}
