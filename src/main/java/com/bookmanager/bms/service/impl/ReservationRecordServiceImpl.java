package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.ReservationRecordMapper;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.ReservationRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.ReservationRecordService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ReservationRecordServiceImpl implements ReservationRecordService {

    @Resource
    private ReservationRecordMapper reservationRecordMapper;

    @Resource
    private BookInfoService bookInfoService;
    
    @Resource
    private BorrowService borrowService;
    
    @Resource
    private SuspensionService suspensionService;

    @Override
    public Map<String, Object> addReservationRecord(Integer userid, Integer bookid) {
        BookInfo theBook = bookInfoService.queryBookInfoById(bookid);
        if (theBook == null) {
            return MyResult.getResultMap(1, "書籍不存在");
        } else if (theBook.getIsborrowed() == 0) {
            return MyResult.getResultMap(1, theBook.getBookname() + " 未借出，無法預約");
        }
        if (suspensionService.getUserActiveSuspension(userid) != null) {
            return MyResult.getResultMap(1, "使用者已被停權，無法借閱圖書");
        }
        if (borrowService.getBorrowByUserIdAndBookId(userid, bookid) > 0) {
            return MyResult.getResultMap(1, theBook.getBookname() + " 已經借閱過了，無法再次預約");
        }
        if (!reservationRecordMapper.selectByBookIdAndUserId(bookid, userid).isEmpty()) {
            return MyResult.getResultMap(1, theBook.getBookname() + " 已經預約過了，無法再次預約");
        }

        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setUserid(userid);
        reservationRecord.setBookid(bookid);
        reservationRecord.setStatus((byte) 0); // 預約狀態設置為 0
        int result = reservationRecordMapper.insert(reservationRecord);
        if (result == 0) {
            return MyResult.getResultMap(1, "預約失敗");
        }
        return MyResult.getResultMap(0, "預約書籍成功");
    }

    @Override
    public ReservationRecord getReservationRecord(Integer id) {
        return reservationRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateReservationRecord(Integer bookid) {
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setBookid(bookid);
        return reservationRecordMapper.updateByPrimaryKey(reservationRecord);
    }

    @Override
    public Integer deleteReservationRecord(Integer reservationid) {
        return reservationRecordMapper.deleteByPrimaryKey(reservationid);
    }

    @Override
    public Integer searchStatusByUserIdAndBookId(Integer userId, Integer bookId) {
        List<ReservationRecord> list = reservationRecordMapper.selectByBookIdAndUserId(bookId, userId);
        for (ReservationRecord rec : list) {
            if (rec.isActive()) {
                return 1;
            }
        }
        return 0;
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
    public Integer updateStatusByReservationList(Integer bookId) {
        List<ReservationRecord> list = reservationRecordMapper.selectByBookId(bookId);
        for (ReservationRecord rec : list) {
            if (rec.isActive()) {
                rec.confirm();
                reservationRecordMapper.updateByPrimaryKeySelective(rec);
                return rec.getUserid();
            }
        }
        return 0;
    }

    @Override
    public List<ReservationRecord> getReservationRecordsByUserId(Integer userId) {
        return reservationRecordMapper.selectByUserId(userId);
    }

    @Override
    public Map<String, Object> searchReservationRecordsByPage(Map<String, Object> params) {
        MyUtils.parsePageParams(params);
        int count = reservationRecordMapper.selectCountBySearch(params);
        List<Map<String, Object>> reservationRecords = reservationRecordMapper.selectBySearch(params);
        return MyResult.getListResultMap(0, "success", count, reservationRecords);
    }
}
