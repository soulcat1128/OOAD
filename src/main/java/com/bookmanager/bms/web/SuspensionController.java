package com.bookmanager.bms.web;

import com.bookmanager.bms.mapper.SuspensionRecordMapper;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/suspension")
public class SuspensionController {

    @Autowired
    private SuspensionService suspensionService;
    
    @Autowired
    private SuspensionRecordMapper suspensionRecordMapper;

    /**
     * 檢查使用者是否被停權
     * @param userId 使用者ID
     * @return 停權狀態資訊
     */
    @RequestMapping(value = {"/checkUserSuspension", "/reader/checkUserSuspension"})
    public Map<String, Object> checkUserSuspension(@RequestParam Integer userId) {
        if (suspensionService.getUserActiveSuspension(userId) == null) {
            return MyResult.getResultMap(0, "使用者未被停權，可以借閱圖書");
        }
        else
        {
            SuspensionRecord record = suspensionRecordMapper.findLastActiveSuspensionByUserId(userId);
            if (record != null) {
                return MyResult.getResultMap(1, "使用者已被停權，停權原因: " + record.getSuspensionReason() +
                                              "，停權開始時間: " + record.getStartDate() +
                                              "，停權結束時間: " + record.getEndDate());
            } else {
                return MyResult.getResultMap(2, "無法獲取使用者停權資訊");
            }
        }
    }
    
    /**
     * 手動執行停權記錄檢查和更新
     * @return 更新結果
     */
    @RequestMapping(value = "/checkAndUpdateSuspensions")
    public Map<String, Object> checkAndUpdateSuspensions() {
        try {
            Integer count = suspensionService.checkAndUpdateExpiredSuspensions();
            return MyResult.getResultMap(0, "成功更新 " + count + " 條過期停權記錄");
        } catch (Exception e) {
            return MyResult.getResultMap(1, "檢查停權記錄失敗: " + e.getMessage());
        }
    }
}
