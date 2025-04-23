package com.bookmanager.bms.web;

import com.bookmanager.bms.exception.NotEnoughException;
import com.bookmanager.bms.exception.OperationFailureException;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.ReservationRecordService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    SuspensionService suspensionService;
    @Autowired
    ReservationRecordService reservationRecordService;

    // 分頁查詢借閱 params: {page, limit, userid, bookid}
    @RequestMapping(value = "/queryBorrowsByPage")
    public Map<String, Object> queryBorrowsByPage(@RequestParam Map<String, Object> params){
        return borrowService.searchBorrowsByPage(params);
    }

    // 添加借閱
    @RequestMapping(value = "/addBorrow")
    public Integer addBorrow(@RequestBody Borrow borrow){
        return borrowService.addBorrow(borrow);
    }

    // 獲得數量
    @RequestMapping(value = "/getCount")
    public Integer getCount(){
        return borrowService.getCount();
    }

    // 刪除借閱
    @RequestMapping(value = "/deleteBorrow")
    public Integer deleteBorrow(@RequestBody Borrow borrow){
        return borrowService.deleteBorrow(borrow);
    }

    // 刪除一些借閱
    @RequestMapping(value = "/deleteBorrows")
    public Integer deleteBorrows(@RequestBody List<Borrow> borrows){
        return borrowService.deleteBorrows(borrows);
    }

    // 更新借閱
    @RequestMapping(value = "/updateBorrow")
    public Integer updateBorrow(@RequestBody Borrow borrow){
        return borrowService.updateBorrow(borrow);
    }

    // 借書
    @RequestMapping(value = {"/borrowBook", "/reader/borrowBook"})
    @Transactional
    public Map<String, Object> borrowBook(Integer userid, Integer bookid){
        return borrowService.addBorrow2(userid, bookid);
    }

    // 還書
    @RequestMapping(value = {"/returnBook", "/reader/returnBook"})
    @Transactional
    public Map<String, Object> returnBook(Integer borrowid, Integer bookid){
        return borrowService.returnBook(borrowid, bookid);
    }

    // 延長借閱
    @RequestMapping(value = {"/extendBorrow", "/reader/extendBorrow"})
    @Transactional
    public Map<String, Object> extendBorrow(Integer borrowid, Integer bookid) {
        try {
            // 查詢借書的情況
            Borrow theBorrow = borrowService.queryBorrowsById(borrowid);
            if (theBorrow == null) {
                return MyResult.getResultMap(2, "借閱記錄不存在");
            }

            // 檢查使用者借閱權限
            SuspensionRecord suspensionRecord =  suspensionService.getUserActiveSuspension(theBorrow.getUserid());
            if(suspensionRecord != null)
            {
                return MyResult.getResultMap(1, "使用者已被停權，無法延長借閱");
            }

            if (theBorrow.getReturntime() != null) {
                return MyResult.getResultMap(3, "該書籍已歸還，無法延長借閱");
            }

            if (theBorrow.getIsExtended() != null && theBorrow.getIsExtended() == 1) {
                return MyResult.getResultMap(4, "該借閱已經延長過，每次借閱只能延長一次");
            }

            // 檢查是否已逾期
            Date now = new Date();

            if (now.after(theBorrow.getExpectedReturnTime())) {
                return MyResult.getResultMap(5, "該借閱已超過原借閱期限，故無法延長");
            }

            // 設置isExtended為1，並延長借閱期限 14 天
            theBorrow.setIsExtended(1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(theBorrow.getExpectedReturnTime()); // 原始到期日
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            Date newDueDate = calendar.getTime();
            theBorrow.setExpectedReturnTime(newDueDate);

            Integer res = borrowService.updateBorrow2(theBorrow);

            if (res == 0) {
                return MyResult.getResultMap(6, "延長借閱失敗，系統錯誤");
            }

            // 成功延長，返回新的到期日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return MyResult.getResultMap(0, "延長成功，新的歸還日期為: " + sdf.format(newDueDate));

        } catch (Exception e) {
            System.out.println("發生異常，進行手動回滾");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return MyResult.getResultMap(1, e.getMessage());
        }
    }

    //@Scheduled(cron = "0 0 0 * * ?") // 每天12:00執行
    @Scheduled(cron = "*/10 * * * * ?") // 測試 10秒/次
    @Transactional
    public void checkAndUpdateExpiredSuspensions() {
        try {
            // 更新已過期的停權記錄
            Integer updatedCount = suspensionService.checkAndUpdateExpiredSuspensions();
            System.out.println("已更新 " + updatedCount + " 條到期停權記錄");

            // 找到過期的書 執行停權動作
            List<Borrow> overdueBooks = borrowService.findOverdueBooks();
            int processedCount = 0;

            for(Borrow book: overdueBooks) {
                try {
                    // 執行停權並歸還書籍
                    if(suspensionService.createSuspensionRecord(book)) {
                        processReturnOverdueBook(book.getBorrowid(), book.getBookid());
                        processedCount++;
                    }
                } catch (Exception e) {
                    System.out.println("處理過期書籍失敗 (borrowId=" + book.getBorrowid() + "): " + e.getMessage());
                }
            }

            System.out.println("已處理共 " + processedCount + " 條過期書籍並建立停權紀錄");
        } catch (Exception e) {
            System.out.println("檢查停權記錄時發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 批次處理歸還
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processReturnOverdueBook(Integer borrowid, Integer bookid) {
        try {
            // 更新圖書狀態
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            bookInfo.setIsborrowed((byte) 0);
            bookInfoService.updateBookInfo(bookInfo);

            // 更新借閱記錄
            Borrow borrow = new Borrow();
            borrow.setBorrowid(borrowid);
            borrow.setReturntime(new Date());
            borrowService.updateBorrow2(borrow);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("自動歸還過期書籍失敗: " + e.getMessage(), e);
        }
    }
}
