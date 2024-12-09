
import com.wangpeng.bms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookTest {
    private BookFactory bookFactory;
    private IBook paperBook;
    private IBook eBook;
    private IBook audioBook;

    @BeforeEach
    public void setUp() {
        bookFactory = new BookFactory();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookname("book");
        bookInfo.setBookauthor("author");
        bookInfo.setBookprice(BigDecimal.valueOf(99));
        bookInfo.setBookdesc("desc");
        bookInfo.setMaterial("paper");
        bookInfo.setPageCount(123);
        paperBook = bookFactory.createBook(bookInfo);
        bookInfo.setMaterial("digital");
        bookInfo.setFileSize(100);
        eBook = bookFactory.createBook(bookInfo);
        bookInfo.setMaterial("audio");
        bookInfo.setNarrator("narrator");
        bookInfo.setDuration(100);
        audioBook = bookFactory.createBook(bookInfo);
    }

    @Test
    public void testPaperBook() {
        assertEquals("paper", paperBook.getMaterial());
        assertTrue(paperBook instanceof PaperBook);
        assertEquals(123, ((PaperBook)paperBook).getPageCount());
        assertEquals("paper", paperBook.getMaterial());
        paperBook.display();
    }

    @Test
    public void testEBook() {
        assertEquals("digital", eBook.getMaterial());
        assertTrue(eBook instanceof EBook);
        assertEquals(100, ((EBook)eBook).getFileSize());
        assertEquals("digital", eBook.getMaterial());
        eBook.display();
    }

    @Test
    public void testAudioBook() {
        assertEquals("audio", audioBook.getMaterial());
        assertTrue(audioBook instanceof AudioBook);
        assertEquals("narrator", ((AudioBook)audioBook).getNarrator());
        assertEquals(100, ((AudioBook)audioBook).getDuration());
        assertEquals("audio", audioBook.getMaterial());
        audioBook.display();
    }

    @Test
    public void testSeriesBook() {
        List<IBook> books = new ArrayList<>();
        books.add(paperBook);
        books.add(eBook);
        books.add(audioBook);
        IBook book = bookFactory.createBookSeries("series", books);
        assertTrue(book instanceof BookSeries);
        BookSeries bookSeries = (BookSeries) book;
        assertEquals("series", bookSeries.getName());
        assertEquals(paperBook, bookSeries.books.get(0));
        assertEquals(eBook, bookSeries.books.get(1));
        assertEquals(audioBook, bookSeries.books.get(2));
        bookSeries.display();
    }
    
}
