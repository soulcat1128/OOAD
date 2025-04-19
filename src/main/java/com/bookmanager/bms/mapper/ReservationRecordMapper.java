package com.bookmanager.bms.mapper;

import com.bookmanager.bms.model.ReservationRecord;

import java.util.List;
import java.util.Map;

public interface ReservationRecordMapper {
    int deleteByPrimaryKey(Integer reservationId);

    int insert(ReservationRecord record);

    int insertSelective(ReservationRecord record);

    ReservationRecord selectByPrimaryKey(Integer reservationId);

    int updateByPrimaryKeySelective(ReservationRecord record);

    int updateByPrimaryKey(ReservationRecord record);

    List<ReservationRecord> selectByUserId(Integer reservationId);

    List<ReservationRecord> selectByBookId(Integer bookid);

    List<ReservationRecord> selectByBookIdAndUserId(Integer bookId, Integer userId);

    Integer selectCountBySearch(Map<String, Object> params);

    List<Map<String, Object>> selectBySearch(Map<String, Object> params);
}
