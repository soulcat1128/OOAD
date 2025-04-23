package com.bookmanager.bms.web;

import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.ReservationRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.ReservationRecordService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/reservation")
public class ReservationRecordController {
    @Autowired
    ReservationRecordService reservationRecordService;
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    BorrowService borrowService;
    @Autowired
    SuspensionService suspensionService;

    // 預約書籍
    @PostMapping(value = "/addReservationRecord")
    @Transactional
    public Map<String, Object> addReservationRecord(Integer userid, Integer bookid) {
        return reservationRecordService.addReservationRecord(userid, bookid);
    }

    @GetMapping(value = "/getReservationRecord")
    public ReservationRecord getReservationRecord(Integer id) {
        return reservationRecordService.getReservationRecord(id);
    }

    @PutMapping(value = "/updateStatusByReservationList")
    public Integer updateReservationRecord(Integer bookid) {
        return reservationRecordService.updateStatusByReservationList(bookid);
    }

    @DeleteMapping(value = "/cancelReservationRecord")
    public Integer deleteReservationRecord(Integer reservationId) {
        return reservationRecordService.deleteReservationRecord(reservationId);
    }

    @GetMapping(value = "/getReservationRecordsByUserId")
    public List<ReservationRecord> getReservationRecordsByUserId(Integer userid) {
        return reservationRecordService.getReservationRecordsByUserId(userid);
    }

    @GetMapping(value = "/queryReservationRecordByPage")
    public Map<String, Object> queryReservationRecordByPage(@RequestParam Map<String, Object> params) {
        return reservationRecordService.searchReservationRecordsByPage(params);
    }
}
