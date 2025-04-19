package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.SuspensionRecordMapper;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.SuspensionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SuspensionServiceImpl implements SuspensionService {

    @Resource
    private SuspensionRecordMapper suspensionRecordMapper;

    @Override
    public boolean canUserBorrow(Integer userId) {
        SuspensionRecord record = suspensionRecordMapper.checkUserBorrowingSuspension(userId);
        return record == null; // 如果沒有找到限制記錄，表示使用者可以借閱
    }
}
