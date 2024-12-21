import com.wangpeng.bms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

public class ProfessorAmountTest {
    
    private ProfessorAmount professorAmount;
    private Borrow borrow;
    
    @BeforeEach
    void setUp() {
        professorAmount = new ProfessorAmount();
        borrow = new Borrow();
    }
    
    @Test
    void testCalculateWithinGracePeriod() {
        // set borrow date 15 days ago (within 30 days grace period)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -15);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = professorAmount.calculate(borrow);
        assertEquals(0.0, amount, 0.01);
    }
    
    @Test
    void testCalculateAfterGracePeriod() {
        // set borrow date 35 days ago (after 30 days grace period)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -35);
        borrow.setBorrowtime(cal.getTime());
        
        double amount = professorAmount.calculate(borrow);
        assertEquals(25.0, amount, 0.01); // 5 * $5 = $25
    }
}