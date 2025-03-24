
import com.wangpeng.bms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookFactoryTest {
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
        bookInfo.setNarrator("John");
        bookInfo.setDuration(100);
        audioBook = bookFactory.createBook(bookInfo);
    }

    @Test
    public void testPaperBook() {
        assertEquals("paper", paperBook.getMaterial());
        assertTrue(paperBook instanceof PaperBook);
        assertEquals(123, ((PaperBook)paperBook).getPageCount());
        assertEquals("paper", paperBook.getMaterial());
        assertEquals("PaperBook: book by author, price: 99, desc: desc, page count: 123", paperBook.display());
    }

    @Test
    public void testEBook() {
        assertEquals("digital", eBook.getMaterial());
        assertTrue(eBook instanceof EBook);
        assertEquals(100, ((EBook)eBook).getFileSize());
        assertEquals("digital", eBook.getMaterial());
        assertEquals("EBook: book by author, price: 99, desc: desc, file size: 100", eBook.display());
    }

    @Test
    public void testAudioBook() {
        assertEquals("audio", audioBook.getMaterial());
        assertTrue(audioBook instanceof AudioBook);
        assertEquals("John", ((AudioBook)audioBook).getNarrator());
        assertEquals(100, ((AudioBook)audioBook).getDuration());
        assertEquals("audio", audioBook.getMaterial());
        assertEquals("AudioBook: book by author, price: 99, desc: desc, narrator: John, duration: 100 min", audioBook.display());
    }

    @Test
    public void testSeriesBook() {
        List<IBook> books = new ArrayList<>();
        IBook bookSeries = bookFactory.createBookSeries("testSeries");
        bookSeries.add(paperBook);
        bookSeries.add(eBook);
        assertTrue(bookSeries instanceof BookSeries);
        BookSeries testSeries = (BookSeries)bookSeries;
        assertEquals("testSeries", bookSeries.getName());
        assertEquals(paperBook, testSeries.books.get(0));
        assertEquals(eBook, testSeries.books.get(1));

        // 測試新增書籍
        bookSeries.add(audioBook);
        assertEquals(audioBook, testSeries.books.get(2));

        // 測試移除書籍
        bookSeries.remove(paperBook);
        assertEquals(eBook, testSeries.books.get(0));
        assertEquals(audioBook, testSeries.books.get(1));

        String expectedOutput = "BookSeries: testSeries contains: \n" +
                "  EBook: book by author, price: 99, desc: desc, file size: 100\n" +
                "  AudioBook: book by author, price: 99, desc: desc, narrator: John, duration: 100 min";

        // 測試輸出
        assertEquals(expectedOutput, testSeries.display());
    }

    @Test
    public void testUnknownMaterial() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setMaterial("unknown");
        IllegalArgumentException exception =assertThrows(IllegalArgumentException.class, () -> bookFactory.createBook(bookInfo));        ;
        assertEquals("Can't create book, Unknown material: unknown", exception.getMessage());
    }

    @Test
    public void testEmptyBookSeries() {
        BookSeries bookSeries = new BookSeries("Empty Series");
        String expectedOutput = "BookSeries: Empty Series contains:";

        assertEquals(0, bookSeries.books.size());
        assertEquals(expectedOutput, bookSeries.display());
    }

    @Test
    public void testNestedBookSeriesDisplay() {
        BookSeries innerSeries = new BookSeries("Inner Series");
        innerSeries.add(paperBook);
        innerSeries.add(eBook);

        BookSeries outerSeries = new BookSeries("Outer Series");
        outerSeries.add(innerSeries);
        outerSeries.add(audioBook);

        String expectedOutput = "BookSeries: Outer Series contains: \n" +
                "  BookSeries: Inner Series contains: \n" +
                "    PaperBook: book by author, price: 99, desc: desc, page count: 123\n" +
                "    EBook: book by author, price: 99, desc: desc, file size: 100\n" +
                "  AudioBook: book by author, price: 99, desc: desc, narrator: John, duration: 100 min";

        assertEquals(expectedOutput, outerSeries.display());
    }



}
