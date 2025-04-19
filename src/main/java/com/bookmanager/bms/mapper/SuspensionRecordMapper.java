package com.bookmanager.bms.mapper;

import com.bookmanager.bms.model.SuspensionRecord;

public interface SuspensionRecordMapper {
    int deleteByPrimaryKey(Integer suspensionId);

    int insert(SuspensionRecord record);

    int insertSelective(SuspensionRecord record);

    SuspensionRecord selectByPrimaryKey(Integer suspensionId);

    int updateByPrimaryKeySelective(SuspensionRecord record);

    int updateByPrimaryKey(SuspensionRecord record);

    SuspensionRecord checkUserBorrowingSuspension(Integer userId);
}
