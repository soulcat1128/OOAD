package com.bookmanager.bms.service;

import com.bookmanager.bms.model.ReservationRecord;

import java.util.List;
import java.util.Map;

public interface ReservationRecordService {
    Map<String, Object> addReservationRecord(Integer userid, Integer bookid);

    ReservationRecord getReservationRecord(Integer id);

    Integer updateReservationRecord(Integer bookid);

    Integer deleteReservationRecord(Integer reservationid);

    Integer searchStatusByUserIdAndBookId(Integer userId, Integer bookId);

    Integer setStatusById(Integer id, Integer status);

    Integer updateStatusByReservationList(Integer bookid);

    List<ReservationRecord> getReservationRecordsByUserId(Integer userId);

    Map<String, Object> searchReservationRecordsByPage(Map<String, Object> params);
}
