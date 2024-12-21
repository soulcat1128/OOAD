import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.wangpeng.bms.model.*;


import java.util.Calendar;
import java.util.Date;

public class StudentAmountTest {
    
    private StudentAmount studentAmount;
    private Borrow borrow;
    
    @BeforeEach
    public void setUp() {
        studentAmount = new StudentAmount();
        borrow = new Borrow();
    }
    
    @Test
    public void testCalculateWithinGracePeriod() {
        // Set borrow date 15 days ago (within 30 days grace period)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -15);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = studentAmount.calculate(borrow);
        assertEquals(0.0, amount, 0.01);
    }
    
    @Test
    public void testCalculateFirstTenDaysAfterGracePeriod() {
        // test after grace period 5 days
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -35);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = studentAmount.calculate(borrow);
        assertEquals(25.0, amount, 0.01); // 5 * $5 = $25
    }
    
    @Test
    public void testCalculateAfterFirstTenDays() {
        // test after grace period 15 days
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -45);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = studentAmount.calculate(borrow);
        assertEquals(100.0, amount, 0.01); // (10 * $5) + (5 * $10) = $50 + $50 = $100
    }
}