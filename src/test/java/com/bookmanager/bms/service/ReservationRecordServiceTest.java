package com.bookmanager.bms.service;

import com.bookmanager.bms.mapper.ReservationRecordMapper;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.model.ReservationRecord;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.impl.ReservationRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * 預約記錄服務的單元測試
 */
@ExtendWith(MockitoExtension.class)
public class ReservationRecordServiceTest {

    @Mock
    private ReservationRecordMapper reservationRecordMapper;

    @Mock
    private BookInfoService bookInfoService;
    
    @Mock
    private BorrowService borrowService;
    
    @Mock
    private SuspensionService suspensionService;

    @InjectMocks
    private ReservationRecordServiceImpl reservationRecordService;

    private BookInfo availableBook;
    private BookInfo borrowedBook;
    private ReservationRecord activeReservation;
    private ReservationRecord confirmedReservation;
    private List<ReservationRecord> reservationList;
    private List<ReservationRecord> userReservations;
    private ReservationRecord repeatReservation;
    private BookInfo repeatBook;
    private final Integer validUserId = 1;
    private final Integer suspendedUserId = 2;
    private final Integer availableBookId = 101;
    private final Integer borrowedBookId = 102;
    private final Integer validReservationId = 201;
    private final Integer repeatReservationId = 202;
    private final Integer repeatReservationBookId = 203;

    @BeforeEach
    void setUp() {
        // 設置可借閱的書籍
        availableBook = new BookInfo();
        availableBook.setBookid(availableBookId);
        availableBook.setBookname("可用的書籍");
        availableBook.setIsborrowed((byte) 0); // 未借出

        // 設置已借出的書籍
        borrowedBook = new BookInfo();
        borrowedBook.setBookid(borrowedBookId);
        borrowedBook.setBookname("已借出的書籍");
        borrowedBook.setIsborrowed((byte) 1); // 已借出

        // 設置重複預約的書籍
        repeatBook = new BookInfo();
        repeatBook.setBookid(repeatReservationBookId);
        repeatBook.setBookname("重複預約的書籍");
        repeatBook.setIsborrowed((byte) 1); // 已借出

        // 設置有效的預約記錄
        activeReservation = new ReservationRecord();
        activeReservation.setReservationid(validReservationId);
        activeReservation.setUserid(validUserId);
        activeReservation.setBookid(borrowedBookId);
        activeReservation.setStatus((byte) 0); // 預約中

        // 設置已確認的預約記錄
        confirmedReservation = new ReservationRecord();
        confirmedReservation.setReservationid(202);
        confirmedReservation.setUserid(validUserId);
        confirmedReservation.setBookid(borrowedBookId);
        confirmedReservation.setStatus((byte) 1); // 已確認

        // 設置預約列表
        reservationList = new ArrayList<>();
        reservationList.add(activeReservation);

        // 設置用戶的預約列表
        userReservations = new ArrayList<>();
        userReservations.add(activeReservation);
        userReservations.add(confirmedReservation);

        repeatReservation = new ReservationRecord();
        repeatReservation.setReservationid(repeatReservationId);
        repeatReservation.setUserid(validUserId);
        repeatReservation.setBookid(repeatReservationBookId);
        repeatReservation.setStatus((byte) 0); // 預約中

    }

    @Test
    @DisplayName("測試添加預約記錄成功")
    void testAddReservationRecordSuccess() {
        List<ReservationRecord> emptyList = new ArrayList<>();
        when(bookInfoService.queryBookInfoById(borrowedBookId)).thenReturn(borrowedBook);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);
        when(borrowService.getBorrowByUserIdAndBookId(validUserId, borrowedBookId)).thenReturn(0);
        when(reservationRecordMapper.selectByBookIdAndUserId(borrowedBookId,validUserId)).thenReturn(emptyList);
        when(reservationRecordMapper.insert(any(ReservationRecord.class))).thenReturn(1);

        
        Map<String, Object> result = reservationRecordService.addReservationRecord(validUserId, borrowedBookId);
        
        assertEquals(0, result.get("status"));
        assertEquals("預約書籍成功", result.get("message"));
        verify(reservationRecordMapper, times(1)).insert(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試添加預約記錄-書籍不存在")
    void testAddReservationRecordBookNotExists() {
        when(bookInfoService.queryBookInfoById(999)).thenReturn(null);
        
        Map<String, Object> result = reservationRecordService.addReservationRecord(validUserId, 999);
        
        assertEquals(1, result.get("status"));
        assertEquals("書籍不存在", result.get("message"));
        verify(reservationRecordMapper, never()).insert(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試添加預約記錄-書籍未借出")
    void testAddReservationRecordBookAvailable() {
        when(bookInfoService.queryBookInfoById(availableBookId)).thenReturn(availableBook);
        
        Map<String, Object> result = reservationRecordService.addReservationRecord(validUserId, availableBookId);
        
        assertEquals(1, result.get("status"));
        assertTrue(result.get("message").toString().contains("未借出，無法預約"));
        verify(reservationRecordMapper, never()).insert(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試添加預約記錄-用戶已被停權")
    void testAddReservationRecordUserSuspended() {
        SuspensionRecord suspension = new SuspensionRecord();
        when(bookInfoService.queryBookInfoById(borrowedBookId)).thenReturn(borrowedBook);
        when(suspensionService.getUserActiveSuspension(suspendedUserId)).thenReturn(suspension);
        
        Map<String, Object> result = reservationRecordService.addReservationRecord(suspendedUserId, borrowedBookId);
        
        assertEquals(1, result.get("status"));
        assertEquals("使用者已被停權，無法借閱圖書", result.get("message"));
        verify(reservationRecordMapper, never()).insert(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試添加預約記錄-用戶已借閱過該書")
    void testAddReservationRecordAlreadyBorrowed() {
        when(bookInfoService.queryBookInfoById(borrowedBookId)).thenReturn(borrowedBook);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);
        when(borrowService.getBorrowByUserIdAndBookId(validUserId, borrowedBookId)).thenReturn(1);
        
        Map<String, Object> result = reservationRecordService.addReservationRecord(validUserId, borrowedBookId);
        
        assertEquals(1, result.get("status"));
        assertTrue(result.get("message").toString().contains("已經借閱過了，無法再次預約"));
        verify(reservationRecordMapper, never()).insert(any(ReservationRecord.class));
    }

    @Test
    @DisplayName("測試添加預約記錄-用戶已預約過該書")
    void testAddReservationRecordAlreadyReserve() {
        when(bookInfoService.queryBookInfoById(repeatReservationBookId)).thenReturn(repeatBook);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);

        List<ReservationRecord> alreadyReservedList = new ArrayList<>();
        alreadyReservedList.add(repeatReservation);
        when(reservationRecordMapper.selectByBookIdAndUserId(repeatReservationBookId, validUserId)).thenReturn(alreadyReservedList);

        Map<String, Object> result = reservationRecordService.addReservationRecord(validUserId, repeatReservationBookId);

        assertEquals(1, result.get("status"));
        assertTrue(result.get("message").toString().contains("已經預約過了，無法再次預約"));
        verify(reservationRecordMapper, never()).insert(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試添加預約記錄-插入失敗")
    void testAddReservationRecordInsertFailed() {
        List<ReservationRecord> emptyList = new ArrayList<>();
        when(bookInfoService.queryBookInfoById(borrowedBookId)).thenReturn(borrowedBook);
        when(suspensionService.getUserActiveSuspension(validUserId)).thenReturn(null);
        when(borrowService.getBorrowByUserIdAndBookId(validUserId, borrowedBookId)).thenReturn(0);
        when(reservationRecordMapper.selectByBookIdAndUserId(borrowedBookId, validUserId)).thenReturn(emptyList);
        when(reservationRecordMapper.insert(any(ReservationRecord.class))).thenReturn(0);
        
        Map<String, Object> result = reservationRecordService.addReservationRecord(validUserId, borrowedBookId);
        
        assertEquals(1, result.get("status"));
        assertEquals("預約失敗", result.get("message"));
        verify(reservationRecordMapper, times(1)).insert(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試根據ID獲取預約記錄")
    void testGetReservationRecord() {
        when(reservationRecordMapper.selectByPrimaryKey(validReservationId)).thenReturn(activeReservation);
        
        ReservationRecord result = reservationRecordService.getReservationRecord(validReservationId);
        
        assertEquals(activeReservation, result);
        verify(reservationRecordMapper, times(1)).selectByPrimaryKey(validReservationId);
    }
    
    @Test
    @DisplayName("測試刪除預約記錄")
    void testDeleteReservationRecord() {
        when(reservationRecordMapper.deleteByPrimaryKey(validReservationId)).thenReturn(1);
        
        Integer result = reservationRecordService.deleteReservationRecord(validReservationId);
        
        assertEquals(1, result);
        verify(reservationRecordMapper, times(1)).deleteByPrimaryKey(validReservationId);
    }
    
    @Test
    @DisplayName("測試根據用戶ID和書籍ID查詢預約狀態")
    void testSearchStatusByUserIdAndBookId() {
        when(reservationRecordMapper.selectByBookIdAndUserId(borrowedBookId, validUserId)).thenReturn(reservationList);
        
        Integer result = reservationRecordService.searchStatusByUserIdAndBookId(validUserId, borrowedBookId);
        
        assertEquals(1, result);
        verify(reservationRecordMapper, times(1)).selectByBookIdAndUserId(borrowedBookId, validUserId);
    }
    
    @Test
    @DisplayName("測試根據ID設置預約狀態")
    void testSetStatusById() {
        when(reservationRecordMapper.selectByPrimaryKey(validReservationId)).thenReturn(activeReservation);
        when(reservationRecordMapper.updateByPrimaryKey(activeReservation)).thenReturn(1);
        
        Integer result = reservationRecordService.setStatusById(validReservationId, 1);
        
        assertEquals(1, result);
        assertEquals((byte) 1, activeReservation.getStatus());
        verify(reservationRecordMapper, times(1)).updateByPrimaryKey(activeReservation);
    }
    
    @Test
    @DisplayName("測試根據ID設置預約狀態-記錄不存在")
    void testSetStatusByIdNotFound() {
        when(reservationRecordMapper.selectByPrimaryKey(999)).thenReturn(null);
        
        Integer result = reservationRecordService.setStatusById(999, 1);
        
        assertEquals(0, result);
        verify(reservationRecordMapper, never()).updateByPrimaryKey(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試根據預約列表更新狀態")
    void testUpdateStatusByReservationList() {
        when(reservationRecordMapper.selectByBookId(borrowedBookId)).thenReturn(reservationList);
        when(reservationRecordMapper.updateByPrimaryKeySelective(activeReservation)).thenReturn(1);
        
        Integer result = reservationRecordService.updateStatusByReservationList(borrowedBookId);
        
        assertEquals(validUserId, result);
        assertEquals((byte) 1, activeReservation.getStatus());
        verify(reservationRecordMapper, times(1)).updateByPrimaryKeySelective(activeReservation);
    }
    
    @Test
    @DisplayName("測試根據預約列表更新狀態-無有效預約")
    void testUpdateStatusByReservationListNoActive() {
        List<ReservationRecord> inactiveList = new ArrayList<>();
        inactiveList.add(confirmedReservation);
        
        when(reservationRecordMapper.selectByBookId(borrowedBookId)).thenReturn(inactiveList);
        
        Integer result = reservationRecordService.updateStatusByReservationList(borrowedBookId);
        
        assertEquals(0, result);
        verify(reservationRecordMapper, never()).updateByPrimaryKeySelective(any(ReservationRecord.class));
    }
    
    @Test
    @DisplayName("測試獲取用戶的預約記錄")
    void testGetReservationRecordsByUserId() {
        when(reservationRecordMapper.selectByUserId(validUserId)).thenReturn(userReservations);
        
        List<ReservationRecord> result = reservationRecordService.getReservationRecordsByUserId(validUserId);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(activeReservation));
        assertTrue(result.contains(confirmedReservation));
        verify(reservationRecordMapper, times(1)).selectByUserId(validUserId);
    }
    
    @Test
    @DisplayName("測試分頁查詢預約記錄")
    void testSearchReservationRecordsByPage() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
        
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> record1 = new HashMap<>();
        record1.put("reservationid", validReservationId);
        records.add(record1);
        
        when(reservationRecordMapper.selectCountBySearch(params)).thenReturn(1);
        when(reservationRecordMapper.selectBySearch(params)).thenReturn(records);
        
        Map<String, Object> result = reservationRecordService.searchReservationRecordsByPage(params);
        
        assertEquals(0, result.get("code"));
        assertEquals("success", result.get("message"));
        assertEquals(1, result.get("count"));
        assertEquals(records, result.get("data"));
        
        verify(reservationRecordMapper, times(1)).selectCountBySearch(params);
        verify(reservationRecordMapper, times(1)).selectBySearch(params);
    }
}
