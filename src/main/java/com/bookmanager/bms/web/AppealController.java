package com.bookmanager.bms.web;

import com.bookmanager.bms.model.Appeal;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.AppealService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;
    
    @Autowired
    private SuspensionService suspensionService;

    /**
     * 創建申訴
     */
    @RequestMapping(value = {"/createAppeal", "/reader/createAppeal"})
    public Map<String, Object> createAppeal(@RequestBody Appeal appeal) {
        try {
            Integer appealId = appealService.createAppeal(appeal);
            if (appealId > 0) {
                return MyResult.getResultMap(0, "申訴提交成功，請等待管理員審核", appealId);
            } else {
                return MyResult.getResultMap(1, "申訴提交失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(2, "系統錯誤: " + e.getMessage());
        }
    }

    /**
     * 獲取用戶的申訴記錄
     */
    @RequestMapping(value = {"/getUserAppeals", "/reader/getUserAppeals"})
    public Map<String, Object> getUserAppeals(@RequestParam Integer userId) {
        try {
            List<Appeal> appeals = appealService.getUserAppeals(userId);
            return MyResult.getListResultMap(0, "success", appeals.size(), appeals);
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(1, "獲取申訴記錄失敗: " + e.getMessage());
        }
    }

    /**
     * 獲取所有申訴（管理員使用）
     */
    @RequestMapping(value = "/getAllAppeals")
    public Map<String, Object> getAllAppeals() {
        try {
            List<Appeal> appeals = appealService.getAllAppeals();
            return MyResult.getListResultMap(0, "success", appeals.size(), appeals);
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(1, "獲取申訴記錄失敗: " + e.getMessage());
        }
    }

    /**
     * 根據狀態獲取申訴（管理員使用）
     */
    @RequestMapping(value = "/getAppealsByStatus")
    public Map<String, Object> getAppealsByStatus(@RequestParam Byte status) {
        try {
            List<Appeal> appeals = appealService.getAppealsByStatus(status);
            return MyResult.getListResultMap(0, "success", appeals.size(), appeals);
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(1, "獲取申訴記錄失敗: " + e.getMessage());
        }
    }

    /**
     * 批准申訴（管理員使用）
     */
    @RequestMapping(value = "/approveAppeal")
    public Map<String, Object> approveAppeal(@RequestParam Integer appealId, @RequestParam String adminReply, @RequestParam Integer userId) {
        try {
            // 獲取停權相關資訊
            SuspensionRecord SuspensionRecord = suspensionService.getUserActiveSuspension(userId);
            if (SuspensionRecord == null) {
                return MyResult.getResultMap(3, "使用者當前不為封鎖狀態，無需處理");
            }


            Integer result = appealService.approveAppeal(appealId, adminReply, SuspensionRecord);
            if (result > 0) {
                return MyResult.getResultMap(0, "申訴已批准，用戶借閱權限已恢復");
            } else {
                return MyResult.getResultMap(1, "申訴處理失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(2, "系統錯誤: " + e.getMessage());
        }
    }

    /**
     * 拒絕申訴（管理員使用）
     */
    @RequestMapping(value = "/rejectAppeal")
    public Map<String, Object> rejectAppeal(@RequestParam Integer appealId, @RequestParam String adminReply) {
        try {
            Integer result = appealService.rejectAppeal(appealId, adminReply);
            if (result > 0) {
                return MyResult.getResultMap(0, "申訴已拒絕");
            } else {
                return MyResult.getResultMap(1, "申訴處理失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(2, "系統錯誤: " + e.getMessage());
        }
    }
    
    /**
     * 獲取用戶當前的停權記錄（用於提交申訴時選擇）
     */
    @RequestMapping(value = {"/getUserActiveSuspensions", "/reader/getUserActiveSuspensions"})
    public Map<String, Object> getUserActiveSuspensions(@RequestParam Integer userId) {
        try {
            SuspensionRecord record = suspensionService.getUserActiveSuspension(userId);
            if (record != null) {
                return MyResult.getResultMap(0, "success", record);
            } else {
                return MyResult.getResultMap(1, "未找到當前有效的停權記錄");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(2, "系統錯誤: " + e.getMessage());
        }
    }
}
