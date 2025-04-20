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
        try{
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);
            if (theBook == null) {
                throw new NullPointerException("書籍不存在");
            } else if (theBook.getIsborrowed() == 0) {
                throw new Exception(theBook.getBookname() + " 未借出，無法預約");
            }

            if (reservationRecordService.searchStatusByUserIdAndBookId(userid, bookid) == 1) {
                throw new Exception(theBook.getBookname() + " 已經預約過了，無法再次預約");
            }

            if (suspensionService.getUserActiveSuspension(userid) != null) {
                throw new Exception(theBook.getBookname() + " 使用者已被停權，無法借閱圖書");
            }

            // 檢查用戶書籍是否已借閱 !!
            if (borrowService.getBorrowByUserIdAndBookId(userid, bookid) != null) {
                throw new Exception(theBook.getBookname() + " 已經借閱過了，無法再次預約");
            }

            ReservationRecord reservationRecord = new ReservationRecord();
            reservationRecord.setUserid(userid);
            reservationRecord.setBookid(bookid);
            reservationRecord.setStatus((byte) 0); // 預約狀態設置為 0
            Integer result = reservationRecordService.addReservationRecord(reservationRecord);
            if (result == 0) throw new Exception("預約書籍失敗");

        } catch (Exception e) {
            System.out.println("發生異常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            if (e.getMessage().contains("不存在")) {
                return MyResult.getResultMap(1, e.getMessage());
            } else if (e.getMessage().contains("未借出")) {
                return MyResult.getResultMap(1, e.getMessage());
            } else if (e.getMessage().contains("已經預約過了，無法再次預約")) {
                return MyResult.getResultMap(1, e.getMessage());
            } else if (e.getMessage().contains("使用者已被停權")) {
                return MyResult.getResultMap(1, e.getMessage());
            } else if (e.getMessage().contains("已經借閱過了")) {
                return MyResult.getResultMap(1, e.getMessage());
            } else if (e.getMessage().contains("預約書籍失敗")) {
                return MyResult.getResultMap(2, "請聯絡管理員");
            }
        }
        return MyResult.getResultMap(0, "預約書籍成功" );
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
        MyUtils.parsePageParams(params);
        int count = reservationRecordService.getSearchCount(params);
        List<Map<String, Object>> reservationRecords = reservationRecordService.searchReservationRecordsByPage(params);
        return MyResult.getListResultMap(0, "success", count, reservationRecords);
    }
}
