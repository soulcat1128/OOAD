package com.bookmanager.bms.service;

import com.bookmanager.bms.mapper.BorrowMapper;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.impl.BorrowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 借閱延長服務的單元測試
 */
@ExtendWith(MockitoExtension.class)
public class BorrowServiceExtendTest {

    @Mock
    private BorrowMapper borrowMapper;

    @Mock
    private SuspensionService suspensionService;

    @Mock
    private BookInfoService bookInfoService;
    
    @Mock
    private ReservationRecordService reservationRecordService;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    private Borrow validBorrow;
    private Borrow returnedBorrow;
    private Borrow extendedBorrow;
    private Borrow overdueBorrow;
    private Integer validBorrowId = 1;
    private Integer validBookId = 101;
    private Integer validUserId = 201;

    @BeforeEach
    void setUp() {
        // 設置有效的借閱記錄
        validBorrow = new Borrow();
        validBorrow.setBorrowid(validBorrowId);
        validBorrow.setBookid(validBookId);
        validBorrow.setUserid(validUserId);
        validBorrow.setIsExtended(0);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        validBorrow.setExpectedReturnTime(cal.getTime());
        validBorrow.setBorrowtime(new Date());
        
        // 設置已歸還的借閱記錄
        returnedBorrow = new Borrow();
        returnedBorrow.setBorrowid(2);
        returnedBorrow.setBookid(102);
        returnedBorrow.setUserid(validUserId);
        returnedBorrow.setIsExtended(0);
        returnedBorrow.setBorrowtime(new Date());
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 10);
        returnedBorrow.setExpectedReturnTime(cal.getTime());
        returnedBorrow.setReturntime(new Date());
        
        // 設置已延長的借閱記錄
        extendedBorrow = new Borrow();
        extendedBorrow.setBorrowid(3);
        extendedBorrow.setBookid(103);
        extendedBorrow.setUserid(validUserId);
        extendedBorrow.setIsExtended(1); // 已延長
        extendedBorrow.setBorrowtime(new Date());
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 20);
        extendedBorrow.setExpectedReturnTime(cal.getTime());
        
        // 設置已逾期的借閱記錄
        overdueBorrow = new Borrow();
        overdueBorrow.setBorrowid(4);
        overdueBorrow.setBookid(104);
        overdueBorrow.setUserid(validUserId);
        overdueBorrow.setIsExtended(0);
        overdueBorrow.setBorrowtime(new Date());
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -5); // 已經逾期5天
        overdueBorrow.setExpectedReturnTime(cal.getTime());
    }

    @Test
    @DisplayName("測試成功延長借閱")
    void testExtendBorrowSuccess() {
        when(borrowMapper.selectByPrimaryKey(validBorrowId)).thenReturn(validBorrow);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);
        when(borrowMapper.updateByPrimaryKeySelective(any(Borrow.class))).thenReturn(1);

        Map<String, Object> result = borrowService.extendBorrow(validBorrowId, validBookId);

        assertEquals(0, result.get("status"));
        assertTrue(((String) result.get("message")).contains("延長成功"));
        verify(borrowMapper, times(1)).updateByPrimaryKeySelective(any(Borrow.class));
    }

    @Test
    @DisplayName("測試借閱記錄不存在")
    void testExtendBorrowNotExist() {
        when(borrowMapper.selectByPrimaryKey(99)).thenReturn(null);

        Map<String, Object> result = borrowService.extendBorrow(99, validBookId);

        assertEquals(2, result.get("status"));
        assertEquals("借閱記錄不存在", result.get("message"));
        verify(borrowMapper, never()).updateByPrimaryKeySelective(any(Borrow.class));
    }

    @Test
    @DisplayName("測試使用者已被停權")
    void testExtendBorrowUserSuspended() {
        SuspensionRecord suspension = new SuspensionRecord();
        suspension.setSuspensionid(1);
        suspension.setUserid(validUserId);

        when(borrowMapper.selectByPrimaryKey(validBorrowId)).thenReturn(validBorrow);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(suspension);

        Map<String, Object> result = borrowService.extendBorrow(validBorrowId, validBookId);

        assertEquals(1, result.get("status"));
        assertEquals("使用者已被停權，無法延長借閱", result.get("message"));
        verify(borrowMapper, never()).updateByPrimaryKeySelective(any(Borrow.class));
    }

    @Test
    @DisplayName("測試書籍已歸還")
    void testExtendBorrowAlreadyReturned() {
        when(borrowMapper.selectByPrimaryKey(returnedBorrow.getBorrowid())).thenReturn(returnedBorrow);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);

        Map<String, Object> result = borrowService.extendBorrow(returnedBorrow.getBorrowid(), returnedBorrow.getBookid());

        assertEquals(3, result.get("status"));
        assertEquals("該書籍已歸還，無法延長借閱", result.get("message"));
        verify(borrowMapper, never()).updateByPrimaryKeySelective(any(Borrow.class));
    }

    @Test
    @DisplayName("測試借閱已延長過")
    void testExtendBorrowAlreadyExtended() {
        when(borrowMapper.selectByPrimaryKey(extendedBorrow.getBorrowid())).thenReturn(extendedBorrow);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);

        Map<String, Object> result = borrowService.extendBorrow(extendedBorrow.getBorrowid(), extendedBorrow.getBookid());

        assertEquals(4, result.get("status"));
        assertEquals("該借閱已經延長過，每次借閱只能延長一次", result.get("message"));
        verify(borrowMapper, never()).updateByPrimaryKeySelective(any(Borrow.class));
    }

    @Test
    @DisplayName("測試借閱已逾期")
    void testExtendBorrowOverdue() {
        when(borrowMapper.selectByPrimaryKey(overdueBorrow.getBorrowid())).thenReturn(overdueBorrow);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);

        Map<String, Object> result = borrowService.extendBorrow(overdueBorrow.getBorrowid(), overdueBorrow.getBookid());

        assertEquals(5, result.get("status"));
        assertEquals("該借閱已超過原借閱期限，故無法延長", result.get("message"));
        verify(borrowMapper, never()).updateByPrimaryKeySelective(any(Borrow.class));
    }

    @Test
    @DisplayName("測試系統錯誤")
    void testExtendBorrowSystemError() {
        when(borrowMapper.selectByPrimaryKey(validBorrowId)).thenReturn(validBorrow);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);
        when(borrowMapper.updateByPrimaryKeySelective(any(Borrow.class))).thenReturn(0); // 更新失敗

        Map<String, Object> result = borrowService.extendBorrow(validBorrowId, validBookId);

        assertEquals(6, result.get("status"));
        assertEquals("延長借閱失敗，系統錯誤", result.get("message"));
        verify(borrowMapper, times(1)).updateByPrimaryKeySelective(any(Borrow.class));
    }
}
