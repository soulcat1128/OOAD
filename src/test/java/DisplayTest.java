import com.wangpeng.bms.model.*;
import com.wangpeng.bms.model.template.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DisplayTest {

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
        bookInfo.setBookname("bookname");
        bookInfo.setBookauthor("bookauthor");
        bookInfo.setBookprice(BigDecimal.valueOf(100));
        bookInfo.setBookdesc("bookdesc");
        bookInfo.setBookid(1);
        bookInfo.setBookimg("bookimg");
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
    public void testPaperBookDisplay() {
        IBookDisplayTemplate bookDisplay = bookFactory.createBookDisplay(paperBook);
        assertTrue(bookDisplay instanceof PaperBookDisplay);
        bookDisplay.display();
    }

    @Test
    public void testEBookDisplay() {
        IBookDisplayTemplate bookDisplay = bookFactory.createBookDisplay(eBook);
        assertTrue(bookDisplay instanceof EBookDisplay);
        bookDisplay.display();
    }

    @Test
    public void testAudioBookDisplay() {
        IBookDisplayTemplate bookDisplay = bookFactory.createBookDisplay(audioBook);
        assertTrue(bookDisplay instanceof AudioBookDisplay);
        bookDisplay.display();
    }
}
