import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.wangpeng.bms.model.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.Arrays;
import java.math.BigDecimal;


class ProfessorAmountTest {
   private ProfessorAmount calculator;
   private Borrow borrow;
   private PaperBook book;
   private BookSeries series;
   
   @BeforeEach
   void setUp() {
       calculator = new ProfessorAmount();
       borrow = new Borrow();

       // Create BookInfo for single book
       BookInfo bookInfo = new BookInfo();
       bookInfo.setBookname("Test Book");
       bookInfo.setBookauthor("Test Author");
       bookInfo.setBookprice(new BigDecimal("29.99"));
       bookInfo.setBookdesc("Test Description");
       bookInfo.setPageCount(200);
       book = new PaperBook(bookInfo);
       
       // Create books for series
       BookInfo bookInfo1 = new BookInfo();
       bookInfo1.setBookname("Series Book 1");
       bookInfo1.setBookauthor("Author 1"); 
       AudioBook book1 = new AudioBook(bookInfo1);

       BookInfo bookInfo2 = new BookInfo();
       bookInfo2.setBookname("Series Book 2");  
       bookInfo2.setBookauthor("Author 2");
       EBook book2 = new EBook(bookInfo2);
       
       series = new BookSeries("Test Series");
       series.add(book1);
       series.add(book2);
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
       cal.add(Calendar.DATE, -35);
       borrow.setBorrowtime(cal.getTime());
       
       assertEquals(25.0, calculator.calculate(book, borrow), 0.01);
   }
   
   @Test
   void testBookSeriesWithinPeriod() {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -32);
       borrow.setBorrowtime(cal.getTime());
       
       assertEquals(0.0, calculator.calculate(series, borrow), 0.01);
   }
   
   @Test
   void testBookSeriesAfterPeriod() {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -50); 
       borrow.setBorrowtime(cal.getTime());
       
       double expectedFine = (50-35)*5.0 + (50-40)*5.0;
       assertEquals(expectedFine, calculator.calculate(series, borrow), 0.01);
   }

    @Test
    void testNullBook() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(null, borrow);
        });
        assertEquals("Book and borrow cannot be null.", exception.getMessage());
    }

    @Test
    void testEmptyBookSeries() {
        BookSeries emptySeries = new BookSeries("Empty Series");
        assertEquals(0.0, calculator.calculate(emptySeries, borrow), 0.01);
    }

}
