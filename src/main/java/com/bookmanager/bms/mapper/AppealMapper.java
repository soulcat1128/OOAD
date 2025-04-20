package com.bookmanager.bms.mapper;

import com.bookmanager.bms.model.Appeal;

import java.util.List;

public interface AppealMapper {
    int deleteByPrimaryKey(Integer appealId);

    int insert(Appeal record);

    int insertSelective(Appeal record);

    Appeal selectByPrimaryKey(Integer appealId);

    int updateByPrimaryKeySelective(Appeal record);

    int updateByPrimaryKey(Appeal record);
    
    // 根據用戶ID查詢申訴記錄
    List<Appeal> selectByUserId(Integer userId);
    
    // 查詢所有申訴記錄（帶用戶信息）
    List<Appeal> selectAllWithUserInfo();
    
    // 根據狀態查詢申訴記錄（帶用戶信息）
    List<Appeal> selectByStatusWithUserInfo(Byte status);
}
