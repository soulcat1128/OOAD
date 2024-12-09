
import com.wangpeng.bms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class BookTest {
    private BookFactory bookFactory;
    private BookInfo bookInfo;
    private IBook paperBook;
    private IBook eBook;
    private IBook audioBook;

    @BeforeEach
    public void setUp() {
        bookFactory = new BookFactory();
        bookInfo = new BookInfo();
        bookInfo.setMaterial("paper");
        bookInfo.setPageCount(100);
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
        assertEquals(100, ((PaperBook)paperBook).getPageCount());
        assertEquals("paper", paperBook.getMaterial());
    }

    @Test
    public void testEBook() {
        IBook book = bookFactory.createBook(bookInfo);
        assertEquals("digital", book.getMaterial());
        assertTrue(book instanceof EBook);
        EBook eBook = (EBook) book;
        assertEquals(100, eBook.getFileSize());
        assertEquals("digital", eBook.getMaterial());
    }

    @Test
    public void testAudioBook() {
        BookFactory bookFactory = new BookFactory();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setMaterial("audio");
        bookInfo.setNarrator("narrator");
        bookInfo.setDuration(100);
        IBook book = bookFactory.createBook(bookInfo);
        assertEquals("audio", book.getMaterial());
        assertTrue(book instanceof AudioBook);
        AudioBook audioBook = (AudioBook) book;
        assertEquals("narrator", audioBook.getNarrator());
        assertEquals(100, audioBook.getDuration());
        assertEquals("audio", audioBook.getMaterial());
    }

    @Test
    public void testSeriesBook() {
        BookFactory bookFactory = new BookFactory();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setMaterial("paper");
        bookInfo.setPageCount(100);
        IBook book1 = bookFactory.createBook(bookInfo);
        bookInfo.setMaterial("digital");
        bookInfo.setFileSize(100);
        IBook book2 = bookFactory.createBook(bookInfo);
        bookInfo.setMaterial("audio");
        bookInfo.setNarrator("narrator");
        bookInfo.setDuration(100);
        IBook book3 = bookFactory.createBook(bookInfo);
        List<IBook> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        IBook book = bookFactory.createBookSeries("series", books);
        assertTrue(book instanceof BookSeries);
        BookSeries bookSeries = (BookSeries) book;
        assertEquals("series", bookSeries.getTitle());
        assertEquals(book1, bookSeries.books.get(0));
        assertEquals(book2, bookSeries.books.get(1));
        assertEquals(book3, bookSeries.books.get(2));
    }
    
}
