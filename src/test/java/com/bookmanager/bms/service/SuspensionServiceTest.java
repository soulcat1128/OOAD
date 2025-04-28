package com.bookmanager.bms.service;

import com.bookmanager.bms.mapper.SuspensionRecordMapper;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.impl.SuspensionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 停權服務的單元測試
 */
@ExtendWith(MockitoExtension.class)
public class SuspensionServiceTest {

    @Mock
    private SuspensionRecordMapper suspensionRecordMapper;
    
    @Mock
    private BorrowService borrowService;
    
    @Mock
    private BookInfoService bookInfoService;
    
    @Mock
    private TransactionStatus transactionStatus;

    @InjectMocks
    private SuspensionServiceImpl suspensionService;

    private Borrow overdueBorrow;
    private SuspensionRecord activeSuspension;
    private SuspensionRecord expiredSuspension;
    private List<SuspensionRecord> expiredSuspensions;
    private List<Borrow> overdueBooks;
    private final Integer validBorrowId = 1;
    private final Integer validBookId = 101;
    private final Integer validUserId = 201;

    @BeforeEach
    void setUp() {
        // 設置逾期的借閱記錄
        overdueBorrow = new Borrow();
        overdueBorrow.setBorrowid(validBorrowId);
        overdueBorrow.setBookid(validBookId);
        overdueBorrow.setUserid(validUserId);
        overdueBorrow.setIsExtended(0);
        overdueBorrow.setBorrowtime(new Date());
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10); // 已逾期10天
        overdueBorrow.setExpectedReturnTime(cal.getTime());
        
        // 設置有效的停權記錄
        activeSuspension = new SuspensionRecord();
        activeSuspension.setSuspensionid(1);
        activeSuspension.setUserid(validUserId);
        activeSuspension.setBorrowid(validBorrowId);
        activeSuspension.setBorrowingPermission(SuspensionRecord.BORROWING_PROHIBITED);
        activeSuspension.setSuspensionReason("逾期未還書");
        activeSuspension.setStartDate(new Date());
        
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        activeSuspension.setEndDate(cal.getTime());
        
        // 設置已到期的停權記錄
        expiredSuspension = new SuspensionRecord();
        expiredSuspension.setSuspensionid(2);
        expiredSuspension.setUserid(202);
        expiredSuspension.setBorrowid(2);
        expiredSuspension.setBorrowingPermission(SuspensionRecord.BORROWING_PROHIBITED);
        expiredSuspension.setSuspensionReason("逾期未還書");
        
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -40); // 40天前開始
        expiredSuspension.setStartDate(cal.getTime());
        
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10); // 10天前結束
        expiredSuspension.setEndDate(cal.getTime());
        
        // 設置已過期的停權記錄列表
        expiredSuspensions = new ArrayList<>();
        expiredSuspensions.add(expiredSuspension);
        
        // 設置逾期書籍列表
        overdueBooks = new ArrayList<>();
        overdueBooks.add(overdueBorrow);
    }

    @Test
    @DisplayName("測試創建停權記錄成功")
    void testCreateSuspensionRecordSuccess() {
        when(suspensionRecordMapper.findByUserIdAndBorrowId(validUserId, validBorrowId)).thenReturn(null);
        when(suspensionRecordMapper.findLastActiveSuspensionByUserId(validUserId)).thenReturn(null);
        when(suspensionRecordMapper.insertSelective(any(SuspensionRecord.class))).thenReturn(1);
        
        boolean result = suspensionService.createSuspensionRecord(overdueBorrow);
        
        assertTrue(result);
        verify(suspensionRecordMapper, times(1)).insertSelective(any(SuspensionRecord.class));
    }
    
    @Test
    @DisplayName("測試創建停權記錄 - 已存在有效停權記錄")
    void testCreateSuspensionRecordAlreadyExists() {
        when(suspensionRecordMapper.findByUserIdAndBorrowId(validUserId, validBorrowId)).thenReturn(activeSuspension);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            suspensionService.createSuspensionRecord(overdueBorrow);
        });
        
        assertTrue(exception.getMessage().contains("已存在相同停權紀錄"));
        verify(suspensionRecordMapper, never()).insertSelective(any(SuspensionRecord.class));
    }
    
    @Test
    @DisplayName("測試創建停權記錄 - 連續停權")
    void testCreateSuspensionRecordConsecutive() {
        when(suspensionRecordMapper.findByUserIdAndBorrowId(validUserId, validBorrowId)).thenReturn(null);
        when(suspensionRecordMapper.findLastActiveSuspensionByUserId(validUserId)).thenReturn(activeSuspension);
        when(suspensionRecordMapper.insertSelective(any(SuspensionRecord.class))).thenReturn(1);
        
        boolean result = suspensionService.createSuspensionRecord(overdueBorrow);
        
        assertTrue(result);
        verify(suspensionRecordMapper, times(1)).insertSelective(any(SuspensionRecord.class));
    }
    
    @Test
    @DisplayName("測試創建停權記錄 - 插入失敗")
    void testCreateSuspensionRecordInsertFailed() {
        when(suspensionRecordMapper.findByUserIdAndBorrowId(validUserId, validBorrowId)).thenReturn(null);
        when(suspensionRecordMapper.findLastActiveSuspensionByUserId(validUserId)).thenReturn(null);
        when(suspensionRecordMapper.insertSelective(any(SuspensionRecord.class))).thenReturn(0);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            suspensionService.createSuspensionRecord(overdueBorrow);
        });
        
        assertTrue(exception.getMessage().contains("停權記錄創建失敗"));
    }
    
    @Test
    @DisplayName("測試檢查並更新過期的停權記錄")
    void testCheckAndUpdateExpiredSuspensions() {
        when(suspensionRecordMapper.findExpiredSuspensions()).thenReturn(expiredSuspensions);
        when(suspensionRecordMapper.updateByPrimaryKeySelective(any(SuspensionRecord.class))).thenReturn(1);
        
        Integer updatedCount = suspensionService.checkAndUpdateExpiredSuspensions();
        
        assertEquals(1, updatedCount);
        assertEquals(SuspensionRecord.BORROWING_ALLOWED, expiredSuspension.getBorrowingPermission());
        verify(suspensionRecordMapper, times(1)).updateByPrimaryKeySelective(expiredSuspension);
    }
    
    @Test
    @DisplayName("測試獲取用戶當前有效的停權記錄")
    void testGetUserActiveSuspension() {
        when(suspensionRecordMapper.checkUserBorrowingSuspension(validUserId)).thenReturn(activeSuspension);
        
        SuspensionRecord result = suspensionService.getUserActiveSuspension(validUserId);
        
        assertEquals(activeSuspension, result);
        verify(suspensionRecordMapper, times(1)).checkUserBorrowingSuspension(validUserId);
    }
    
    @Test
    @DisplayName("測試檢查並處理超時未歸還的書籍")
    void testCheckAndProcessOverdueBooks() {
        when(suspensionRecordMapper.findExpiredSuspensions()).thenReturn(expiredSuspensions);
        when(suspensionRecordMapper.updateByPrimaryKeySelective(any(SuspensionRecord.class))).thenReturn(1);
        when(borrowService.findOverdueBooks()).thenReturn(overdueBooks);
        
        // createSuspensionRecord()
        when(suspensionRecordMapper.findByUserIdAndBorrowId(validUserId, validBorrowId)).thenReturn(null);
        when(suspensionRecordMapper.findLastActiveSuspensionByUserId(validUserId)).thenReturn(null);
        when(suspensionRecordMapper.insertSelective(any(SuspensionRecord.class))).thenReturn(1);
        
        // processReturnOverdueBook()
        when(bookInfoService.updateBookInfo(any(BookInfo.class))).thenReturn(1);
        when(borrowService.updateBorrow2(any(Borrow.class))).thenReturn(1);
        
        try (MockedStatic<TransactionAspectSupport> mockedStatic = Mockito.mockStatic(TransactionAspectSupport.class)) {
            mockedStatic.when(TransactionAspectSupport::currentTransactionStatus).thenReturn(transactionStatus);
            
            suspensionService.checkAndProcessOverdueBooks();
            
            verify(suspensionRecordMapper, times(1)).findExpiredSuspensions();
            verify(suspensionRecordMapper, times(1)).updateByPrimaryKeySelective(expiredSuspension);
            verify(borrowService, times(1)).findOverdueBooks();
            verify(suspensionRecordMapper, times(1)).findByUserIdAndBorrowId(validUserId, validBorrowId);
            verify(suspensionRecordMapper, times(1)).insertSelective(any(SuspensionRecord.class));
            verify(bookInfoService, times(1)).updateBookInfo(any(BookInfo.class));
            verify(borrowService, times(1)).updateBorrow2(any(Borrow.class));
        }
    }
    
    @Test
    @DisplayName("測試處理逾期書籍的歸還")
    void testProcessReturnOverdueBook() {
        when(bookInfoService.updateBookInfo(any(BookInfo.class))).thenReturn(1);
        when(borrowService.updateBorrow2(any(Borrow.class))).thenReturn(1);
        
        try (MockedStatic<TransactionAspectSupport> mockedStatic = Mockito.mockStatic(TransactionAspectSupport.class)) {
            mockedStatic.when(TransactionAspectSupport::currentTransactionStatus).thenReturn(transactionStatus);
            
            suspensionService.processReturnOverdueBook(validBorrowId, validBookId);
            
            verify(bookInfoService, times(1)).updateBookInfo(any(BookInfo.class));
            verify(borrowService, times(1)).updateBorrow2(any(Borrow.class));
        }
    }
    
    @Test
    @DisplayName("測試處理逾期書籍的歸還 - 發生錯誤")
    void testProcessReturnOverdueBookError() {
        when(bookInfoService.updateBookInfo(any(BookInfo.class))).thenThrow(new RuntimeException("更新錯誤"));
        
        try (MockedStatic<TransactionAspectSupport> mockedStatic = Mockito.mockStatic(TransactionAspectSupport.class)) {
            mockedStatic.when(TransactionAspectSupport::currentTransactionStatus).thenReturn(transactionStatus);
            
            Exception exception = assertThrows(RuntimeException.class, () -> {
                suspensionService.processReturnOverdueBook(validBorrowId, validBookId);
            });
            
            assertEquals("自動歸還過期書籍失敗: 更新錯誤", exception.getMessage());
            
            verify(bookInfoService, times(1)).updateBookInfo(any(BookInfo.class));
            verify(borrowService, never()).updateBorrow2(any(Borrow.class));
            
            verify(transactionStatus, times(1)).setRollbackOnly();
        }
    }
    
    @Test
    @DisplayName("測試獲取用戶最後一次有效的停權記錄")
    void testGetLastActiveSuspensionByUserId() {
        when(suspensionRecordMapper.findLastActiveSuspensionByUserId(validUserId)).thenReturn(activeSuspension);
        
        SuspensionRecord result = suspensionService.getLastActiveSuspensionByUserId(validUserId);
        
        assertEquals(activeSuspension, result);
        verify(suspensionRecordMapper, times(1)).findLastActiveSuspensionByUserId(validUserId);
    }
}
