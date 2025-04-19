package com.bookmanager.bms.web;

import com.bookmanager.bms.exception.NotEnoughException;
import com.bookmanager.bms.exception.OperationFailureException;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    SuspensionService suspensionService;

    // 分頁查詢借閱 params: {page, limit, userid, bookid}
    @RequestMapping(value = "/queryBorrowsByPage")
    public Map<String, Object> queryBorrowsByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = borrowService.getSearchCount(params);
        List<Borrow> borrows = borrowService.searchBorrowsByPage(params);
        return MyResult.getListResultMap(0, "success", count, borrows);
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
        try{
            // 檢查使用者借閱權限
            if(suspensionService.canUserBorrow(userid) == false)
            {
                return MyResult.getResultMap(1, "使用者已被停權，無法借閱圖書");
            }
            // 查詢該書的情況
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);

            if(theBook == null) {  // 圖書不存在
                return MyResult.getResultMap(2, "圖書" + bookid + "不存在");
            } else if(theBook.getIsborrowed() == 1) {  // 已經被借
                return MyResult.getResultMap(3, "圖書" + bookid + "庫存不足（已經被借走）");  // TODO : 之後改預約
            }

            // 更新圖書表的isBorrowed
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            bookInfo.setIsborrowed((byte) 1);
            Integer res2 = bookInfoService.updateBookInfo(bookInfo);
            if(res2 == 0) return MyResult.getResultMap(4, "圖書" + bookid + "更新被借資訊失敗");

            // 添加一條紀錄到borrow表
            Borrow borrow = new Borrow();
            borrow.setUserid(userid);
            borrow.setBookid(bookid);
            borrow.setBorrowtime(new Date(System.currentTimeMillis()));
            Integer res1 = borrowService.addBorrow2(borrow);
            if(res1 == 0) return MyResult.getResultMap(5, "圖書" + bookid + "添加借閱記錄失敗");

            return MyResult.getResultMap(0, "借閱成功");
        } catch (Exception e) {
            System.out.println("發生異常，進行手動回滾");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return MyResult.getResultMap(6, e.getMessage());
        }
    }

    // 還書
    @RequestMapping(value = {"/returnBook", "/reader/returnBook"})
    @Transactional
    public Integer returnBook(Integer borrowid, Integer bookid){
        try {
            // 查詢該書的情況
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);
            // 查詢借書的情況
            Borrow theBorrow = borrowService.queryBorrowsById(borrowid);

            if(theBook == null) {  // 圖書不存在
                throw new NullPointerException("圖書" + bookid + "不存在");
            } else if(theBorrow == null) {   //結束記錄不存在
                throw new NullPointerException("借書記錄" + bookid + "不存在");
            } else if(theBorrow.getReturntime() != null) {  // 已經還過書
                throw new NotEnoughException("圖書" + bookid + "已經還過了");
            }

            // 更新圖書表的isBorrowed
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            bookInfo.setIsborrowed((byte) 0);
            Integer res2 = bookInfoService.updateBookInfo(bookInfo);
            if(res2 == 0) throw new OperationFailureException("圖書" + bookid + "更新被借資訊失敗");

            // 更新Borrow表，更新結束時間
            Borrow borrow = new Borrow();
            borrow.setBorrowid(borrowid);
            borrow.setReturntime(new Date(System.currentTimeMillis()));
            Integer res1 = borrowService.updateBorrow2(borrow);
            if(res1 == 0) throw new OperationFailureException("圖書" + bookid + "更新借閱記錄失敗");

        } catch (Exception e) {
            System.out.println("發生異常，進行手動回滾");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    // 延長借閱
    @RequestMapping(value = {"/extendBorrow", "/reader/extendBorrow"})
    @Transactional
    public Map<String, Object> extendBorrow(Integer borrowid, Integer bookid) {
        try {
            // 查詢借書的情況
            Borrow theBorrow = borrowService.queryBorrowsById(borrowid);
            Integer theBorrowUserId = theBorrow.getUserid();
            if(suspensionService.canUserBorrow(theBorrowUserId) == false)
            {
                return MyResult.getResultMap(1, "使用者已被停權，無法延長借閱時間");
            }
            if (theBorrow == null) {
                return MyResult.getResultMap(2, "借閱記錄不存在");
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

}
