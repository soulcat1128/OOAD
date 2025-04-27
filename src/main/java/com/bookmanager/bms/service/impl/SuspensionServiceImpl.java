package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.SuspensionRecordMapper;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.service.BorrowService;
import com.bookmanager.bms.service.SuspensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SuspensionServiceImpl implements SuspensionService {

    @Resource
    private SuspensionRecordMapper suspensionRecordMapper;
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private BookInfoService bookInfoService;
    
    @Override
    public boolean createSuspensionRecord(Borrow borrow) {
        // 檢查是否已經存在相同的停權記錄
        SuspensionRecord existingRecord = suspensionRecordMapper.findByUserIdAndBorrowId(borrow.getUserid(), borrow.getBorrowid());
        if (existingRecord != null) {
            throw new RuntimeException("已存在相同停權紀錄 suspensionId : " + existingRecord.getSuspensionid());
        }

        // 找處於停權狀態最後一筆停權紀錄
        SuspensionRecord latestSuspensionRecord = suspensionRecordMapper.findLastActiveSuspensionByUserId(borrow.getUserid());

        SuspensionRecord record = new SuspensionRecord();
        record.setUserid(borrow.getUserid());
        record.setBorrowid(borrow.getBorrowid());
        record.setBorrowingPermission(SuspensionRecord.BORROWING_PROHIBITED);
        record.setSuspensionReason("逾期歸還書籍");
        Date startDate;

        if (latestSuspensionRecord == null) {
            startDate = new Date();
        } else {
            // 存在先前的停權紀錄，使用先前紀錄的結束日期作為新記錄的開始日期
            startDate = latestSuspensionRecord.getEndDate();
        }

        record.setStartDate(startDate);
        record.setEndDateFromStart(SuspensionRecord.DEFAULT_SUSPENSION_DAYS);

        // 儲存停權記錄
        if (suspensionRecordMapper.insertSelective(record) == 0) {
            throw new RuntimeException("停權記錄創建失敗");
        }
        
        return true;
    }
    
    @Override
    public Integer checkAndUpdateExpiredSuspensions() {
        // 獲取所有已過期但仍在停權狀態的記錄
        List<SuspensionRecord> expiredSuspensions = suspensionRecordMapper.findExpiredSuspensions();
        int updatedCount = 0;
        
        // 更新這些記錄的借閱權限
        for (SuspensionRecord record : expiredSuspensions) {
            record.restorePermission();
            suspensionRecordMapper.updateByPrimaryKeySelective(record);
            updatedCount++;
        }
        
        return updatedCount;
    }
    
    @Override
    public SuspensionRecord getUserActiveSuspension(Integer userId) {
        return suspensionRecordMapper.checkUserBorrowingSuspension(userId);
    }
    
    @Override
    //@Scheduled(cron = "0 0 0 * * ?") // 每天12:00執行
    @Scheduled(cron = "*/10 * * * * ?") // 測試 10秒/次
    @Transactional
    public void checkAndProcessOverdueBooks() {
        try {
            // 更新已過期的停權記錄
            Integer updatedCount = checkAndUpdateExpiredSuspensions();
            System.out.println("已更新 " + updatedCount + " 條到期停權記錄");

            // 找到過期的書 執行停權動作
            List<Borrow> overdueBooks = borrowService.findOverdueBooks();
            int processedCount = 0;

            for(Borrow book: overdueBooks) {
                try {
                    // 執行停權並歸還書籍
                    if(createSuspensionRecord(book)) {
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
    @Override
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

    @Override
    public SuspensionRecord getLastActiveSuspensionByUserId(Integer userId)
    {
        return suspensionRecordMapper.findLastActiveSuspensionByUserId(userId);
    }
}
