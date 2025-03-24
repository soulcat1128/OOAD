import com.wangpeng.bms.model.EBook;
import com.wangpeng.bms.model.BookInfo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EBookTest {

    @Test
    public void testEBookConstructorAndGetters() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("Test EBook");
        bookInfo.setBookauthor("Jane Doe");
        bookInfo.setBookprice(BigDecimal.valueOf(9.99));
        bookInfo.setBookdesc("A fascinating digital book");
        bookInfo.setFileSize(500);
        bookInfo.setBookid(101);
        bookInfo.setBookimg("ebook-cover.jpg");
        bookInfo.setBooktypeid(2);
        bookInfo.setIsborrowed((byte) 1);

        EBook eBook = new EBook(bookInfo);

        assertEquals("Test EBook", eBook.getName());
        assertEquals("Jane Doe", eBook.getAuthor());
        assertEquals(BigDecimal.valueOf(9.99), eBook.getPrice());
        assertEquals("A fascinating digital book", eBook.getDesc());
        assertEquals("digital", eBook.getMaterial());
        assertEquals(500, eBook.getFileSize());
        assertEquals(101, eBook.getId());
        assertEquals("ebook-cover.jpg", eBook.getImg());
        assertEquals(2, eBook.getTypeId());
        assertEquals((byte) 1, eBook.getIsBorrowed());
        assertEquals(10, eBook.getPrefix());
    }

    @Test
    public void testDisplayMethod() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("Test EBook");
        bookInfo.setBookauthor("Jane Doe");
        bookInfo.setBookprice(BigDecimal.valueOf(9.99));
        bookInfo.setBookdesc("A fascinating digital book");
        bookInfo.setFileSize(500);

        EBook eBook = new EBook(bookInfo);
        String actualDisplay = eBook.display();

        String expectedDisplay = "EBook: Test EBook by Jane Doe, price: 9.99, desc: A fascinating digital book, file size: 500";
        assertEquals(expectedDisplay, actualDisplay);
    }
}
