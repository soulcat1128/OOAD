package com.bookmanager.bms.web;

import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.ReservationRecordService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
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
        try {
            return borrowService.returnBook(borrowid, bookid);
        }
        catch (Exception e) {
            System.out.println("發生異常，進行手動回滾");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return MyResult.getResultMap(1, e.getMessage());
        }
    }

    // 延長借閱
    @RequestMapping(value = {"/extendBorrow", "/reader/extendBorrow"})
    @Transactional
    public Map<String, Object> extendBorrow(Integer borrowid, Integer bookid) {
        try {
            return borrowService.extendBorrow(borrowid, bookid);
        } catch (Exception e) {
            System.out.println("發生異常，進行手動回滾");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return MyResult.getResultMap(1, e.getMessage());
        }
    }
}
