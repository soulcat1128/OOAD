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
import java.util.*;

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
    public Borrow getById(Integer borrowId) {
        return borrowMapper.selectByPrimaryKey(borrowId);
    }

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

        // 查詢該書的情況
        if(theBook == null) {  // 圖書不存在
            return MyResult.getResultMap(2, "圖書" + bookid + "不存在");
        }
        if(theBook.getIsborrowed() == 1) {  // 已經被借
            return MyResult.getResultMap(3, "圖書" + bookid + "庫存不足（已經被借走）");
        }

        // 檢查使用者借閱權限
        if(suspensionRecord != null)
        {
            return MyResult.getResultMap(1, "使用者已被停權，無法借閱圖書");
        }


        // 更新圖書表的isBorrowed
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookid(bookid);
        bookInfo.setIsborrowed((byte) 1);
        Integer res2 = bookInfoService.updateBookInfo(bookInfo);
        if(res2 == 0) return MyResult.getResultMap(4, "圖書" + bookid + "更新被借資訊失敗");

        // 添加一條紀錄到borrow表
        Date now = new Date(System.currentTimeMillis());
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 30);
        Date expected = calendar.getTime();
        Borrow borrow = Borrow.newBorrow(userid, bookid, null, null, now, expected);

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
        Borrow theBorrow = borrowMapper.selectByPrimaryKey(borrowid);

        if(theBook == null) {  // 圖書不存在
            throw new NullPointerException("圖書" + bookid + "不存在");
        } else if(theBorrow == null) {   //結束記錄不存在
            throw new NullPointerException("借書記錄" + borrowid + "不存在");
        }

        theBorrow.completeReturn(new Date(System.currentTimeMillis()));

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
    public List<Borrow> findOverdueBooks() { return borrowMapper.findOverdueBooks(); }

    @Override
    public Integer getBorrowByUserIdAndBookId(Integer userId, Integer bookId) {
        return borrowMapper.selectCountByUserIdAndBookId(userId, bookId);
    }

    @Override
    public Integer updateBorrow2(Borrow borrow) {
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }
    
    @Override
    public Map<String, Object> extendBorrow(Integer borrowid, Integer bookid) {
        try {
            // 查詢借書的情況
            Borrow theBorrow = borrowMapper.selectByPrimaryKey(borrowid);

            if (theBorrow == null) {
                return MyResult.getResultMap(2, "借閱記錄不存在");
            }

            // 檢查使用者借閱權限
            SuspensionRecord suspensionRecord = suspensionService.getUserActiveSuspension(theBorrow.getUserid());
            if(suspensionRecord != null) {
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

            Integer res = updateBorrow2(theBorrow);

            if (res == 0) {
                return MyResult.getResultMap(6, "延長借閱失敗，系統錯誤");
            }

            // 成功延長，返回新到期日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return MyResult.getResultMap(0, "延長成功，新的歸還日期為: " + sdf.format(newDueDate));

        } catch (Exception e) {
            e.printStackTrace();
            return MyResult.getResultMap(1, e.getMessage());
        }
    }
}
