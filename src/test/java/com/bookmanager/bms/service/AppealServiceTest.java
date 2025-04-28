package com.bookmanager.bms.service;

import com.bookmanager.bms.mapper.AppealMapper;
import com.bookmanager.bms.mapper.SuspensionRecordMapper;
import com.bookmanager.bms.model.Appeal;
import com.bookmanager.bms.model.SuspensionRecord;
import com.bookmanager.bms.service.impl.AppealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 申訴服務的單元測試
 */
@ExtendWith(MockitoExtension.class)
public class AppealServiceTest {

    @Mock
    private AppealMapper appealMapper;
    
    @Mock
    private SuspensionRecordMapper suspensionRecordMapper;

    @InjectMocks
    private AppealServiceImpl appealService;

    private Appeal pendingAppeal;
    private Appeal approvedAppeal;
    private Appeal rejectedAppeal;
    private SuspensionRecord suspension;
    private final Integer validAppealId = 1;
    private final Integer validSuspensionId = 101;
    private final Integer validUserId = 201;
    private String adminReply = "這是管理員的回覆";

    @BeforeEach
    void setUp() {
        // 設置待處理的申訴
        pendingAppeal = new Appeal();
        pendingAppeal.setAppealid(validAppealId);
        pendingAppeal.setSuspensionid(validSuspensionId);
        pendingAppeal.setUserid(validUserId);
        pendingAppeal.setStatus(Appeal.STATUS_PENDING);
        pendingAppeal.setCreateTime(new Date());
        pendingAppeal.setAppealContent("我要申訴停權處分");
        
        // 設置已批准的申訴
        approvedAppeal = new Appeal();
        approvedAppeal.setAppealid(2);
        approvedAppeal.setSuspensionid(validSuspensionId);
        approvedAppeal.setUserid(validUserId);
        approvedAppeal.setStatus(Appeal.STATUS_APPROVED);
        approvedAppeal.setCreateTime(new Date());
        approvedAppeal.setAppealContent("已批准的申訴");
        approvedAppeal.setAdminReply("已批准的回覆");
        approvedAppeal.setReplyTime(new Date());
        
        // 設置已拒絕的申訴
        rejectedAppeal = new Appeal();
        rejectedAppeal.setAppealid(3);
        rejectedAppeal.setSuspensionid(validSuspensionId);
        rejectedAppeal.setUserid(validUserId);
        rejectedAppeal.setStatus(Appeal.STATUS_REJECTED);
        rejectedAppeal.setCreateTime(new Date());
        rejectedAppeal.setAppealContent("已拒絕的申訴");
        rejectedAppeal.setAdminReply("已拒絕的回覆");
        rejectedAppeal.setReplyTime(new Date());
        
        // 設置停權記錄
        suspension = new SuspensionRecord();
        suspension.setSuspensionid(validSuspensionId);
        suspension.setUserid(validUserId);
        suspension.setBorrowid(1);
        suspension.setStartDate(new Date());
        suspension.setSuspensionReason("逾期未還書");
        suspension.setBorrowingPermission(SuspensionRecord.BORROWING_PROHIBITED);
    }

    @Test
    @DisplayName("測試創建申訴成功")
    void testCreateAppealSuccess() {
        Appeal newAppeal = new Appeal();
        newAppeal.setUserid(validUserId);
        newAppeal.setSuspensionid(validSuspensionId);
        newAppeal.setAppealContent("冤枉阿~~~~ 我要申訴!!!");
        
        when(appealMapper.insertSelective(any(Appeal.class))).thenAnswer(invocation -> {
            Appeal appeal = invocation.getArgument(0);
            appeal.setAppealid(10);
            return 1;
        });
        
        Integer appealId = appealService.createAppeal(newAppeal);
        
        assertEquals(10, appealId);
        assertEquals(Appeal.STATUS_PENDING, newAppeal.getStatus());
        assertNotNull(newAppeal.getCreateTime());
        verify(appealMapper, times(1)).insertSelective(newAppeal);
    }
    
    @Test
    @DisplayName("測試創建申訴失敗")
    void testCreateAppealFailure() {
        Appeal newAppeal = new Appeal();
        newAppeal.setUserid(validUserId);
        newAppeal.setSuspensionid(validSuspensionId);
        newAppeal.setAppealContent("我要申訴");
        
        when(appealMapper.insertSelective(any(Appeal.class))).thenReturn(0); // 插入失敗
        
        Integer appealId = appealService.createAppeal(newAppeal);
        
        assertEquals(0, appealId);
        verify(appealMapper, times(1)).insertSelective(newAppeal);
    }
    
    @Test
    @DisplayName("測試獲取用戶的申訴記錄")
    void testGetUserAppeals() {
        List<Appeal> appeals = new ArrayList<>();
        appeals.add(pendingAppeal);
        appeals.add(approvedAppeal);
        
        when(appealMapper.selectByUserId(validUserId)).thenReturn(appeals);
        
        List<Appeal> result = appealService.getUserAppeals(validUserId);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(pendingAppeal));
        assertTrue(result.contains(approvedAppeal));
        verify(appealMapper, times(1)).selectByUserId(validUserId);
    }
    
    @Test
    @DisplayName("測試獲取所有申訴記錄")
    void testGetAllAppeals() {
        List<Appeal> appeals = new ArrayList<>();
        appeals.add(pendingAppeal);
        appeals.add(approvedAppeal);
        appeals.add(rejectedAppeal);
        
        when(appealMapper.selectAllWithUserInfo()).thenReturn(appeals);
        
        List<Appeal> result = appealService.getAllAppeals();
        
        assertEquals(3, result.size());
        verify(appealMapper, times(1)).selectAllWithUserInfo();
    }
    
    @Test
    @DisplayName("測試按狀態獲取申訴記錄")
    void testGetAppealsByStatus() {
        List<Appeal> pendingAppeals = new ArrayList<>();
        pendingAppeals.add(pendingAppeal);
        
        when(appealMapper.selectByStatusWithUserInfo(Appeal.STATUS_PENDING)).thenReturn(pendingAppeals);
        
        List<Appeal> result = appealService.getAppealsByStatus(Appeal.STATUS_PENDING);
        
        assertEquals(1, result.size());
        assertEquals(pendingAppeal, result.get(0));
        verify(appealMapper, times(1)).selectByStatusWithUserInfo(Appeal.STATUS_PENDING);
    }
    
    @Test
    @DisplayName("測試批准申訴成功")
    void testApproveAppealSuccess() {
        when(appealMapper.selectByPrimaryKey(validAppealId)).thenReturn(pendingAppeal);
        when(appealMapper.updateByPrimaryKeySelective(any(Appeal.class))).thenReturn(1);
        
        Integer result = appealService.approveAppeal(validAppealId, adminReply, suspension);
        
        assertEquals(1, result);
        assertEquals(Appeal.STATUS_APPROVED, pendingAppeal.getStatus());
        assertEquals(adminReply, pendingAppeal.getAdminReply());
        assertNotNull(pendingAppeal.getReplyTime());
        verify(suspensionRecordMapper, times(1)).removeSuspensionByUserId(validUserId);
        verify(appealMapper, times(1)).updateByPrimaryKeySelective(pendingAppeal);
    }
    
    @Test
    @DisplayName("測試批准不存在的申訴")
    void testApproveNonExistentAppeal() {
        when(appealMapper.selectByPrimaryKey(99)).thenReturn(null);
        
        Integer result = appealService.approveAppeal(99, adminReply, suspension);
        
        assertEquals(0, result);
        verify(suspensionRecordMapper, never()).removeSuspensionByUserId(anyInt());
        verify(appealMapper, never()).updateByPrimaryKeySelective(any(Appeal.class));
    }
    
    @Test
    @DisplayName("測試批准已處理的申訴")
    void testApproveAlreadyProcessedAppeal() {
        when(appealMapper.selectByPrimaryKey(approvedAppeal.getAppealid())).thenReturn(approvedAppeal);
        
        Integer result = appealService.approveAppeal(approvedAppeal.getAppealid(), adminReply, suspension);
        
        assertEquals(0, result);
        verify(suspensionRecordMapper, never()).removeSuspensionByUserId(anyInt());
        verify(appealMapper, never()).updateByPrimaryKeySelective(any(Appeal.class));
    }
    
    @Test
    @DisplayName("測試拒絕申訴成功")
    void testRejectAppealSuccess() {
        when(appealMapper.selectByPrimaryKey(validAppealId)).thenReturn(pendingAppeal);
        when(appealMapper.updateByPrimaryKeySelective(any(Appeal.class))).thenReturn(1);
        
        Integer result = appealService.rejectAppeal(validAppealId, adminReply);
        
        assertEquals(1, result);
        assertEquals(Appeal.STATUS_REJECTED, pendingAppeal.getStatus());
        assertEquals(adminReply, pendingAppeal.getAdminReply());
        assertNotNull(pendingAppeal.getReplyTime());
        verify(appealMapper, times(1)).updateByPrimaryKeySelective(pendingAppeal);
    }
    
    @Test
    @DisplayName("測試拒絕不存在的申訴")
    void testRejectNonExistentAppeal() {
        when(appealMapper.selectByPrimaryKey(99)).thenReturn(null);
        
        Integer result = appealService.rejectAppeal(99, adminReply);
        
        assertEquals(0, result);
        verify(appealMapper, never()).updateByPrimaryKeySelective(any(Appeal.class));
    }
    
    @Test
    @DisplayName("測試拒絕已處理的申訴")
    void testRejectAlreadyProcessedAppeal() {
        when(appealMapper.selectByPrimaryKey(rejectedAppeal.getAppealid())).thenReturn(rejectedAppeal);
        
        Integer result = appealService.rejectAppeal(rejectedAppeal.getAppealid(), adminReply);
        
        assertEquals(0, result);
        verify(appealMapper, never()).updateByPrimaryKeySelective(any(Appeal.class));
    }
    
    @Test
    @DisplayName("測試通過ID獲取申訴")
    void testGetAppealById() {
        when(appealMapper.selectByPrimaryKey(validAppealId)).thenReturn(pendingAppeal);
        
        Appeal result = appealService.getAppealById(validAppealId);
        
        assertEquals(pendingAppeal, result);
        verify(appealMapper, times(1)).selectByPrimaryKey(validAppealId);
    }
}
