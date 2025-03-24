import com.wangpeng.bms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;

class PublicAmountTest {
   private PublicAmount calculator;
   private Borrow borrow;
   private PaperBook book;
   private BookSeries series;
   
   @BeforeEach
   void setUp() {
       calculator = new PublicAmount();
       borrow = new Borrow();

       BookInfo bookInfo = new BookInfo();
       bookInfo.setBookname("Test Book");
       bookInfo.setBookauthor("Test Author");
       bookInfo.setBookprice(new BigDecimal("29.99"));
       bookInfo.setBookdesc("Test Description");
       bookInfo.setPageCount(200);
       book = new PaperBook(bookInfo);
       
       BookInfo bookInfo1 = new BookInfo();
       bookInfo1.setBookname("Series Book 1");
       bookInfo1.setBookauthor("Author 1"); 
       AudioBook book1 = new AudioBook(bookInfo1);

       BookInfo bookInfo2 = new BookInfo();
       bookInfo2.setBookname("Series Book 2");  
       bookInfo2.setBookauthor("Author 2");
       EBook book2 = new EBook(bookInfo2);
       
       series = new BookSeries("Test Series", Arrays.asList(book1, book2));
   }
   
   @Test
   void testSingleBookWithinPeriod() {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -25);
       borrow.setBorrowtime(cal.getTime());
       assertEquals(0.0, calculator.calculate(book, borrow), 0.01);
   }
   
   @Test
   void testSingleBookAfterPeriod() {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -40);
       borrow.setBorrowtime(cal.getTime());
       assertEquals(70.0, calculator.calculate(book, borrow), 0.01);
   }
   
   @Test
   void testSingleBookExceedingMaxFine() {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -102);
       borrow.setBorrowtime(cal.getTime());
       assertEquals(500.0, calculator.calculate(book, borrow), 0.01);
   }
   
   @Test
   void testBookSeriesAfterPeriod() {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -102);
       borrow.setBorrowtime(cal.getTime());
       double expectedFine = Math.min((102-35)*7.0, 500.0) + Math.min((102-40)*7.0, 500.0);
       assertEquals(expectedFine, calculator.calculate(series, borrow), 0.01);
   }
}
