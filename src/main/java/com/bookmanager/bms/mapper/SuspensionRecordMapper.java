package com.bookmanager.bms.mapper;

import com.bookmanager.bms.model.SuspensionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuspensionRecordMapper {
    int deleteByPrimaryKey(Integer suspensionId);

    int insert(SuspensionRecord record);

    int insertSelective(SuspensionRecord record);

    SuspensionRecord selectByPrimaryKey(Integer suspensionId);

    int updateByPrimaryKeySelective(SuspensionRecord record);

    int updateByPrimaryKey(SuspensionRecord record);

    SuspensionRecord checkUserBorrowingSuspension(Integer userId);
    
    // 查詢所有過期但未更新的停權記錄
    List<SuspensionRecord> findExpiredSuspensions();
    
    // 根據使用者ID和借閱ID查詢停權記錄
    SuspensionRecord findByUserIdAndBorrowId(@Param("userId") Integer userId, @Param("borrowId") Integer borrowId);

    void removeSuspensionByUserId(@Param("userId")Integer userId);

    SuspensionRecord findLastActiveSuspensionByUserId(@Param("userId")Integer userId);
}