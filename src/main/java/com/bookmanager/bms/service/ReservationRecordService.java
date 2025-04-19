package com.bookmanager.bms.service;

import com.bookmanager.bms.model.ReservationRecord;

import java.util.List;
import java.util.Map;

public interface ReservationRecordService {
    Integer addReservationRecord(ReservationRecord reservationRecord);

    ReservationRecord getReservationRecord(Integer id);

    Integer updateReservationRecord(ReservationRecord reservationRecord);

    Integer deleteReservationRecord(Integer reservationid);

    Integer searchStatusByUserIdAndBookId(Integer userId, Integer bookId);

    Integer setStatusById(Integer id, Integer status);

    Integer updateStatusByReservationList(List<ReservationRecord> reservationRecords);

    List<ReservationRecord> getReservationRecordsByUserId(Integer userId);

    int getSearchCount(Map<String, Object> map);

    List<Map<String, Object>> searchReservationRecordsByPage(Map<String, Object> params);
}
