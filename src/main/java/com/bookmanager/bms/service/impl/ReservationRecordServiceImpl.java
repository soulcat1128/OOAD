package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.ReservationRecordMapper;
import com.bookmanager.bms.model.ReservationRecord;
import com.bookmanager.bms.service.ReservationRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ReservationRecordServiceImpl implements ReservationRecordService {

    @Resource
    private ReservationRecordMapper reservationRecordMapper;

    @Override
    public Integer addReservationRecord(ReservationRecord reservationRecord) {
        return reservationRecordMapper.insert(reservationRecord);
    }

    @Override
    public ReservationRecord getReservationRecord(Integer id) {
        return reservationRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateReservationRecord(ReservationRecord reservationRecord) {
        return reservationRecordMapper.updateByPrimaryKey(reservationRecord);
    }

    @Override
    public Integer deleteReservationRecord(Integer reservationid) {
        return reservationRecordMapper.deleteByPrimaryKey(reservationid);
    }

    @Override
    public Integer searchStatusByUserIdAndBookId(Integer userId, Integer bookId) {
        List<ReservationRecord> RList = reservationRecordMapper.selectByBookIdAndUserId(bookId, userId);
        for (ReservationRecord record : RList) {
            if (record.getStatus() != null && record.getStatus() == 0) {
                return 1; // 找到 status == 0 的預約紀錄，並還沒變成借閱
            }
        }

        return 0; // 沒有任何 status == 0 的紀錄，代表沒有預約
                  // 或者是 status == 1 的紀錄，代表已經借閱了
    }

    @Override
    public Integer setStatusById(Integer id, Integer status) {
        ReservationRecord reservationRecord = reservationRecordMapper.selectByPrimaryKey(id);
        if (reservationRecord != null) {
            reservationRecord.setStatus(status.byteValue());
            return reservationRecordMapper.updateByPrimaryKey(reservationRecord);
        }
        return 0; // 如果找不到該預約紀錄，則返回 0
    }

    @Override
    public Integer updateStatusByReservationList(List<ReservationRecord> reservationRecords) {
        if (reservationRecords == null || reservationRecords.isEmpty()) {
            return 0;
        }
        for (ReservationRecord record : reservationRecords) {
            if (record.getStatus() != null && record.getStatus() == 0) {
                record.setStatus((byte) 1); // 將 status 改為 1
                return reservationRecordMapper.updateByPrimaryKeySelective(record);
            }
        }
        return 0;
    }

    @Override
    public List<ReservationRecord> getReservationRecordsByUserId(Integer userId) {
        return reservationRecordMapper.selectByUserId(userId);
    }

    @Override
    public int getSearchCount(Map<String, Object> params) {
        return reservationRecordMapper.selectCountBySearch(params);
    }

    @Override
    public List<Map<String, Object>> searchReservationRecordsByPage(Map<String, Object> params) {
        List<Map<String, Object>> reservationRecords = reservationRecordMapper.selectBySearch(params);
        return reservationRecordMapper.selectBySearch(params);
    }
}
