import com.wangpeng.bms.model.AudioBook;
import com.wangpeng.bms.model.BookInfo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AudioBookTest {

    @Test
    public void testAudioBookConstructorAndGetters() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("Test AudioBook");
        bookInfo.setBookauthor("John Doe");
        bookInfo.setBookprice(BigDecimal.valueOf(19.99));
        bookInfo.setBookdesc("An engaging audiobook");
        bookInfo.setNarrator("Jane Smith");
        bookInfo.setDuration(120);
        bookInfo.setBookid(123);
        bookInfo.setBookimg("test-image.jpg");
        bookInfo.setBooktypeid(1);
        bookInfo.setIsborrowed((byte) 0);

        AudioBook audioBook = new AudioBook(bookInfo);

        assertEquals("Test AudioBook", audioBook.getName());
        assertEquals("John Doe", audioBook.getAuthor());
        assertEquals(BigDecimal.valueOf(19.99), audioBook.getPrice());
        assertEquals("An engaging audiobook", audioBook.getDesc());
        assertEquals("audio", audioBook.getMaterial());
        assertEquals("Jane Smith", audioBook.getNarrator());
        assertEquals(120, audioBook.getDuration());
        assertEquals(123, audioBook.getId());
        assertEquals("test-image.jpg", audioBook.getImg());
        assertEquals(1, audioBook.getTypeId());
        assertEquals((byte) 0, audioBook.getIsBorrowed());
        assertEquals(5, audioBook.getPrefix());
    }

    @Test
    public void testDisplayMethod() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("Test AudioBook");
        bookInfo.setBookauthor("John Doe");
        bookInfo.setBookprice(BigDecimal.valueOf(19.99));
        bookInfo.setBookdesc("An engaging audiobook");
        bookInfo.setNarrator("Jane Smith");
        bookInfo.setDuration(120);

        AudioBook audioBook = new AudioBook(bookInfo);
        String actualDisplay = audioBook.display();

        String expectedDisplay = "AudioBook: Test AudioBook by John Doe, price: 19.99, desc: An engaging audiobook, narrator: Jane Smith, duration: 120 min";
        assertEquals(expectedDisplay, actualDisplay);
    }
}
