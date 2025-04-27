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
        appeal.setCreateTime(new Date());
        appeal.setStatus(Appeal.STATUS_PENDING);

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

        // 檢查申訴是否可以處理
        if (!appeal.canProcess()) {
            return 0;
        }

        // 將停權記錄移除（允許借閱）
        Integer userid = appeal.getUserid();
        suspensionRecordMapper.removeSuspensionByUserId(userid);
        
        appeal.approve(adminReply);
        
        return appealMapper.updateByPrimaryKeySelective(appeal);
    }

    @Override
    public Integer rejectAppeal(Integer appealId, String adminReply) {
        // 獲取申訴記錄
        Appeal appeal = appealMapper.selectByPrimaryKey(appealId);
        if (appeal == null) {
            return 0;
        }
        
        // 檢查申訴是否可以處理
        if (!appeal.canProcess()) {
            return 0; // 已處理的申訴不能再次處理
        }
        
        appeal.reject(adminReply);
        
        return appealMapper.updateByPrimaryKeySelective(appeal);
    }

    @Override
    public Appeal getAppealById(Integer appealId) {
        return appealMapper.selectByPrimaryKey(appealId);
    }
}
