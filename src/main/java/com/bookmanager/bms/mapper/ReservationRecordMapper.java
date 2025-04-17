package com.bookmanager.bms.mapper;

import com.bookmanager.bms.model.ReservationRecord;

public interface ReservationRecordMapper {
    int deleteByPrimaryKey(Integer reservationId);

    int insert(ReservationRecord record);

    int insertSelective(ReservationRecord record);

    ReservationRecord selectByPrimaryKey(Integer reservationId);

    int updateByPrimaryKeySelective(ReservationRecord record);

    int updateByPrimaryKey(ReservationRecord record);
}
