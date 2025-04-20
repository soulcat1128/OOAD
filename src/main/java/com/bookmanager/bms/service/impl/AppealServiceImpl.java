package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.AppealMapper;
import com.bookmanager.bms.mapper.SuspensionRecordMapper;
import com.bookmanager.bms.model.Appeal;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.AppealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AppealServiceImpl implements AppealService {

    @Resource
    private AppealMapper appealMapper;
    
    @Resource
    private SuspensionRecordMapper suspensionRecordMapper;

    @Override
    public Integer createAppeal(Appeal appeal) {
        // 設置默認值
        appeal.setCreateTime(new Date());
        appeal.setStatus((byte) 0); // 0: 待處理
        
        return appealMapper.insertSelective(appeal) > 0 ? appeal.getAppealid() : 0;
    }

    @Override
    public List<Appeal> getUserAppeals(Integer userId) {
        return appealMapper.selectByUserId(userId);
    }

    @Override
    public List<Appeal> getAllAppeals() {
        return appealMapper.selectAllWithUserInfo();
    }

    @Override
    public List<Appeal> getAppealsByStatus(Byte status) {
        return appealMapper.selectByStatusWithUserInfo(status);
    }

    @Override
    @Transactional
    public Integer approveAppeal(Integer appealId, String adminReply, SuspensionRecord suspensionRecord) {
        // 首先獲取申訴記錄
        Appeal appeal = appealMapper.selectByPrimaryKey(appealId);
        if (appeal == null) {
            return 0;
        }

        // 檢查申訴是否已處理
        if (appeal.getStatus() != 0) {
            return 0; // 已處理的申訴不能再次處理
        }

        // 將停權記錄的borrowingPermission設置為1（允許借閱）
        Integer userid = appeal.getUserid();
        suspensionRecordMapper.removeSuspensionByUserId(userid);
//        suspensionRecord.setBorrowingPermission((byte) 1);
//        suspensionRecordMapper.updateByPrimaryKeySelective(suspensionRecord);
        
        // 更新申訴記錄
        appeal.setStatus((byte) 1); // 1: 已批准
        appeal.setAdminReply(adminReply);
        appeal.setReplyTime(new Date());
        
        return appealMapper.updateByPrimaryKeySelective(appeal);
    }

    @Override
    public Integer rejectAppeal(Integer appealId, String adminReply) {
        // 獲取申訴記錄
        Appeal appeal = appealMapper.selectByPrimaryKey(appealId);
        if (appeal == null) {
            return 0;
        }
        
        // 檢查申訴是否已處理
        if (appeal.getStatus() != 0) {
            return 0; // 已處理的申訴不能再次處理
        }
        
        // 更新申訴記錄
        appeal.setStatus((byte) 2); // 2: 已拒絕
        appeal.setAdminReply(adminReply);
        appeal.setReplyTime(new Date());
        
        return appealMapper.updateByPrimaryKeySelective(appeal);
    }

    @Override
    public Appeal getAppealById(Integer appealId) {
        return appealMapper.selectByPrimaryKey(appealId);
    }
}
