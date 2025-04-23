package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.exception.NotEnoughException;
import com.bookmanager.bms.exception.OperationFailureException;
import com.bookmanager.bms.mapper.BorrowMapper;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.ReservationRecordService;
import com.bookmanager.bms.service.SuspensionService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private SuspensionService suspensionService;
    @Resource
    private BookInfoService bookInfoService;
    @Resource
    private ReservationRecordService reservationRecordService;


    @Override
    public Integer getCount() {
        return borrowMapper.selectCount();
    }

    @Override
    public Integer getSearchCount(Map<String, Object> params) {
        return borrowMapper.selectCountBySearch(params);
    }

    @Override
    public Map<String, Object> searchBorrowsByPage(Map<String, Object> params) {
        MyUtils.parsePageParams(params);
        int count = getSearchCount(params);
        List<Borrow> borrows = borrowMapper.selectBySearch(params);
        for(Borrow borrow : borrows) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(borrow.getBorrowtime() != null) borrow.setBorrowtimestr(simpleDateFormat.format(borrow.getBorrowtime()));
            if(borrow.getReturntime() != null) borrow.setReturntimestr(simpleDateFormat.format(borrow.getReturntime()));
            if(borrow.getExpectedReturnTime() != null) borrow.setExpectedReturnTimeStr(simpleDateFormat.format(borrow.getExpectedReturnTime()));
        }
        return MyResult.getListResultMap(0, "success", count, borrows);
    }

    @Override
    public Integer addBorrow(Borrow borrow) {
        // 將string類型的時間重新調整
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            borrow.setBorrowtime(simpleDateFormat.parse(borrow.getBorrowtimestr()));
            borrow.setReturntime(simpleDateFormat.parse(borrow.getReturntimestr()));

            // 新增：設定預期歸還時間（如果為null）
            if (borrow.getExpectedReturnTime() == null) {
                // 預設為借閱時間 + 30天
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(borrow.getBorrowtime());
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 30);
                borrow.setExpectedReturnTime(calendar.getTime());
            }

            // 新增：設定是否延長借閱（如果為null）
            if (borrow.getIsExtended() == null) {
                borrow.setIsExtended(0); // 0表示未延長
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowMapper.insertSelective(borrow);
    }

    // 不會調整時間格式的add
    @Override
    public Map<String, Object> addBorrow2(Integer userid, Integer bookid) {
        SuspensionRecord suspensionRecord = suspensionService.getUserActiveSuspension(userid);
        BookInfo theBook = bookInfoService.queryBookInfoById(bookid);

        // 檢查使用者借閱權限
        if(suspensionRecord != null)
        {
            return MyResult.getResultMap(1, "使用者已被停權，無法借閱圖書");
        }

        // 查詢該書的情況
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

        // 新增：設定預期歸還時間（如果為null）
        if (borrow.getExpectedReturnTime() == null) {
            // 預設為借閱時間 + 30天
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(borrow.getBorrowtime());
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 30);
            borrow.setExpectedReturnTime(calendar.getTime());
        }

        // 新增：設定是否延長借閱（如果為null）
        if (borrow.getIsExtended() == null) {
            borrow.setIsExtended(0); // 0表示未延長
        }

        int res1 = borrowMapper.insertSelective(borrow);
        if(res1 == 0) return MyResult.getResultMap(5, "圖書" + bookid + "添加借閱記錄失敗");

        return MyResult.getResultMap(0, "借閱成功");
    }

    @Override
    public Integer deleteBorrow(Borrow borrow) {
        // 先查詢有沒有還書
        Borrow borrow1 = borrowMapper.selectByPrimaryKey(borrow.getBorrowid());
        if(borrow1.getReturntime() == null) return 0;
        return borrowMapper.deleteByPrimaryKey(borrow.getBorrowid());
    }

    @Override
    public Integer deleteBorrows(List<Borrow> borrows) {
        int count = 0;
        for(Borrow borrow : borrows) {
            count += deleteBorrow(borrow);
        }
        return count;
    }

    @Override
    public Integer updateBorrow(Borrow borrow) {
        // 將string類型的時間重新調整
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            borrow.setBorrowtime(simpleDateFormat.parse(borrow.getBorrowtimestr()));
            borrow.setReturntime(simpleDateFormat.parse(borrow.getReturntimestr()));
            borrow.setExpectedReturnTimeStr(simpleDateFormat.format(borrow.getExpectedReturnTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }

    // 不調整時間格式的更新
    @Override
    public Map<String, Object> returnBook(Integer borrowid, Integer bookid) {
        // 查詢該書的情況
        BookInfo theBook = bookInfoService.queryBookInfoById(bookid);
        // 查詢借書的情況
        Borrow theBorrow = queryBorrowsById(borrowid);

        if(theBook == null) {  // 圖書不存在
            throw new NullPointerException("圖書" + bookid + "不存在");
        } else if(theBorrow == null) {   //結束記錄不存在
            throw new NullPointerException("借書記錄" + borrowid + "不存在");
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
        Date now = new Date();
        Borrow borrow = new Borrow();
        borrow.setBorrowid(borrowid);
        borrow.setReturntime(now);
        int res1 = borrowMapper.updateByPrimaryKeySelective(borrow);
        if(res1 == 0) throw new OperationFailureException("圖書" + bookid + "更新借閱記錄失敗");

        int resp = reservationRecordService.updateStatusByReservationList(bookid);
        if (resp != 0) {
            Map<String, Object> res3 = addBorrow2(resp, bookid);
            Integer status = (Integer) res3.get("status");
            if (status != null && status > 0) {
                throw new OperationFailureException("圖書" + bookid + "借閱失敗");
            }
        }
        return MyResult.getResultMap(0, "圖書歸還成功");
    }

    @Override
    public Borrow queryBorrowsById(Integer borrowid) {
        return borrowMapper.selectByPrimaryKey(borrowid);
    }

    @Override
    public List<Borrow> findOverdueBooks() { return borrowMapper.findOverdueBooks(); }

    @Override
    public Integer getBorrowByUserIdAndBookId(Integer userId, Integer bookId) {
        return borrowMapper.selectCountByUserIdAndBookId(userId, bookId);
    }

    @Override
    public Integer updateBorrow2(Borrow borrow) {
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }
}
