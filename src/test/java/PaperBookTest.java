import com.wangpeng.bms.model.PaperBook;
import com.wangpeng.bms.model.BookInfo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PaperBookTest {

    @Test
    public void testPaperBookConstructorAndGetters() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("Test PaperBook");
        bookInfo.setBookauthor("Alice Author");
        bookInfo.setBookprice(BigDecimal.valueOf(15.99));
        bookInfo.setBookdesc("An interesting paper book");
        bookInfo.setPageCount(300);
        bookInfo.setBookid(200);
        bookInfo.setBookimg("paperbook-cover.jpg");
        bookInfo.setBooktypeid(3);
        bookInfo.setIsborrowed((byte) 0);

        PaperBook paperBook = new PaperBook(bookInfo);

        assertEquals("Test PaperBook", paperBook.getName());
        assertEquals("Alice Author", paperBook.getAuthor());
        assertEquals(BigDecimal.valueOf(15.99), paperBook.getPrice());
        assertEquals("An interesting paper book", paperBook.getDesc());
        assertEquals("paper", paperBook.getMaterial());
        assertEquals(300, paperBook.getPageCount());
        assertEquals(200, paperBook.getId());
        assertEquals("paperbook-cover.jpg", paperBook.getImg());
        assertEquals(3, paperBook.getTypeId());
        assertEquals((byte) 0, paperBook.getIsBorrowed());
        assertEquals(0, paperBook.getPrefix());
    }

    @Test
    public void testDisplayMethod() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("Test PaperBook");
        bookInfo.setBookauthor("Alice Author");
        bookInfo.setBookprice(BigDecimal.valueOf(15.99));
        bookInfo.setBookdesc("An interesting paper book");
        bookInfo.setPageCount(300);

        PaperBook paperBook = new PaperBook(bookInfo);
        String actualDisplay = paperBook.display();

        String expectedDisplay = "PaperBook: Test PaperBook by Alice Author, price: 15.99, desc: An interesting paper book, page count: 300";
        assertEquals(expectedDisplay, actualDisplay);
    }
}
