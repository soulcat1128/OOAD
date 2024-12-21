import com.wangpeng.bms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

public class PublicAmountTest {
    
    private PublicAmount publicAmount;
    private Borrow borrow;
    
    @BeforeEach
    void setUp() {
        publicAmount = new PublicAmount();
        borrow = new Borrow();
    }
    
    @Test
    void testCalculateWithinGracePeriod() {
        // Test case: borrowed 15 days ago (within 30-day grace period)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -15);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = publicAmount.calculate(borrow);
        assertEquals(0.0, amount, 0.01);
    }
    
    @Test
    void testCalculateAfterGracePeriod() {
        // Test case: borrowed 40 days ago (10 days after grace period)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -40);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = publicAmount.calculate(borrow);
        assertEquals(70.0, amount, 0.01); // 10 days * $7 = $70
    }
    
    @Test
    void testCalculateExceedingMaximumFine() {
        // Test case: borrowed 102 days ago (72 days after grace period)
        // Would be $504 without cap, should be capped at $500
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -102);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = publicAmount.calculate(borrow);
        assertEquals(500.0, amount, 0.01); // Maximum fine is $500
    }
}
