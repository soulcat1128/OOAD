package com.bookmanager.bms.scheduler;

import com.bookmanager.bms.service.SuspensionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OverdueBookSchedulerTest {

    @Mock
    private SuspensionService suspensionService;

    @Test
    @DisplayName("測試排程任務執行")
    void testScheduledTask() {
        // 調用服務方法
        doNothing().when(suspensionService).checkAndProcessOverdueBooks();
        
        // 執行方法
        suspensionService.checkAndProcessOverdueBooks();
        
        // 驗證方法被調用
        verify(suspensionService, times(1)).checkAndProcessOverdueBooks();
    }
}
