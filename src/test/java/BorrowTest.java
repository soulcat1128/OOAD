import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.wangpeng.bms.exception.NotEnoughException;
import com.wangpeng.bms.model.BookInfo;
import com.wangpeng.bms.model.BorrowBook;
import com.wangpeng.bms.model.NotificationManager;
import com.wangpeng.bms.model.Process;
import com.wangpeng.bms.model.ReturnBook;
import com.wangpeng.bms.model.TestObserver;
import com.wangpeng.bms.model.UserObserver;

public class BorrowTest {

    private List<BookInfo> books;
    private BookInfo book1;
    private BookInfo book2;
    private BookInfo book3;
    private BookInfo book4;
    private BookInfo book5;

    // 初始化測試資料
    @BeforeEach
    public void setUp() {
        // 測試資料
        books = new ArrayList<>();
        book1 = new BookInfo();
        book1.setBookid(1);
        book1.setBookname("Java程式設計");
        book1.setBookauthor("耿祥義");
        book1.setBookprice(new BigDecimal("55.50"));
        book1.setBooktypeid(1);
        book1.setBookdesc(
                "《Java2實用教程》不僅可以作為大專院校相關科系的教材，也適合自學者及軟體開發人員參考使用。Java是一種很優秀的程式語言，具有物件導向、跨平台、安全、穩定和多執行緒等特點，是目前軟體設計中極為健壯的程式語言。Java語言不僅可以用來開發大型的應用程式，而且特別適合於在網際網路上應用開發，Java已成為網路時代最重要的程式語言之一。");
        book1.setIsborrowed((byte) 0);

        book2 = new BookInfo();
        book2.setBookid(2);
        book2.setBookname("紅樓夢");
        book2.setBookauthor("曹雪芹");
        book2.setBookprice(new BigDecimal("36.00"));
        book2.setBooktypeid(3);
        book2.setBookdesc("《紅樓夢》是一部百科全書式的長篇小說。以寶黛愛情悲劇為主線，以四大家族的榮辱興衰為背景，描繪出18世紀中國封建社會的各個方面。");
        book2.setIsborrowed((byte) 1);

        book3 = new BookInfo();
        book3.setBookid(3);
        book3.setBookname("西遊記");
        book3.setBookauthor("吳承恩");
        book3.setBookprice(new BigDecimal("60.00"));
        book3.setBooktypeid(3);
        book3.setBookdesc(
                "《西遊記》主要描寫的是孫悟空保唐三藏西天取經，歷經九九八十一難的故事。唐三藏取經是歷史上一件真實的事。大約距今一千三百多年前，即唐太宗貞觀元年（627），年僅25歲的青年和尚玄奘離開京城長安，只身到天竺（印度）遊學。他從長安出發後，途經中亞、阿富汗、巴基斯坦，歷盡艱難險阻，最後到達了印度。他在那裡學習了兩年多，並在一次大型佛教經學辯論會任主講，受到了讚譽。");
        book3.setIsborrowed((byte) 0);

        book4 = new BookInfo();
        book4.setBookid(4);
        book4.setBookname("水滸傳");
        book4.setBookauthor("施耐庵");
        book4.setBookprice(new BigDecimal("50.60"));
        book4.setBooktypeid(3);
        book4.setBookdesc(
                "《水滸傳》是我國第一部以農民起義為題材的長篇章回小說，是我國文學史上一座巍然屹立的豐碑，也是世界文學寶庫中一顆光彩奪目的明珠。數百年來，它一直深受我國人民的喜愛，並被譯為多種文字，成為我國流傳最為廣泛的古代長篇小說之一。");
        book4.setIsborrowed((byte) 1);

        book5 = new BookInfo();
        book5.setBookid(5);
        book5.setBookname("三國演義");
        book5.setBookauthor("羅貫中");
        book5.setBookprice(new BigDecimal("42.00"));
        book5.setBooktypeid(3);
        book5.setBookdesc("《三國演義》又名《三國志演義》、《三國志通俗演義》，是我國小說史上最著名最傑出的長篇章回體歷史小說。");
        book5.setIsborrowed((byte) 0);

        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
    }

    // 測試借書流程
    @Test
    void testBorrow() {
        // 建立通知管理器群組
        NotificationManager notificationManager = new NotificationManager();
        TestObserver testObserver = new TestObserver();
        notificationManager.subscribe(testObserver);

        // 借書流程
        Process borrowBook = new BorrowBook(books, 0, "User1", notificationManager);
        borrowBook.process();
        borrowBook = new BorrowBook(books, 2, "User2", notificationManager);
        borrowBook.process();

        // 測試借書後的書籍狀態
        assertTrue(books.get(0).getIsborrowed() == 1);
        assertTrue(books.get(2).getIsborrowed() == 1);
        assertEquals("[User1]", books.get(0).getBorrowHistory().toString());
        assertEquals("[User2]", books.get(2).getBorrowHistory().toString());

        // 驗證無法借閱的書籍狀態
        Process failBorrowBook = new BorrowBook(books, 1, "User3", notificationManager);
        assertThrows(NotEnoughException.class, () -> {
            failBorrowBook.process();
        });

        // 驗證觀察者收到的通知
        List<String> messages = testObserver.getReceivedMessages();
        assertEquals(3, messages.size());
        assertEquals("成功借閱 Java程式設計", messages.get(0));
        assertEquals("成功借閱 西遊記", messages.get(1));
        assertEquals("目前無法借閱 紅樓夢", messages.get(2));
    }

    // 測試還書流程
    @Test
    void testReturn() {

        // 建立通知管理器群組
        NotificationManager notificationManager = new NotificationManager();
        TestObserver testObserver = new TestObserver();
        notificationManager.subscribe(testObserver);

        Process returnBook = new ReturnBook(books, 0, "User1", notificationManager);
        returnBook.process();
        returnBook = new ReturnBook(books, 2, "User2", notificationManager);
        returnBook.process();

        // 測試還書後的書籍狀態
        assertTrue(books.get(0).getIsborrowed() == 0);
        assertTrue(books.get(2).getIsborrowed() == 0);

        // 測試還書後的書籍借閱紀錄
        assertEquals("[User1]", books.get(0).getReturnHistory().toString());
        assertEquals("[User2]", books.get(2).getReturnHistory().toString());

        // 驗證觀察者收到的通知
        List<String> messages = testObserver.getReceivedMessages();
        assertEquals(2, messages.size());
        assertEquals("成功歸還 Java程式設計", messages.get(0));
        assertEquals("成功歸還 西遊記", messages.get(1));
    }

    // 測試許多人借還書流程
    @Test
    void testBorrowAndReturn() {
        // 建立通知管理器群組
        NotificationManager notificationManager = new NotificationManager();
        TestObserver testObserver = new TestObserver();
        notificationManager.subscribe(testObserver);

        Process borrowBook, returnBook;
        String[] userNames = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Hank", "Ivy", "Jack" };

        for (BookInfo book : books) {
            book.setIsborrowed((byte) 0);
        }

        // 借還書交錯流程
        borrowBook = new BorrowBook(books, 0, userNames[0], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 0, userNames[0], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 1, userNames[1], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 1, userNames[1], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 2, userNames[2], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 2, userNames[2], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 3, userNames[3], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 3, userNames[3], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 4, userNames[4], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 4, userNames[4], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 0, userNames[5], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 0, userNames[5], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 2, userNames[7], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 2, userNames[7], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 2, userNames[6], notificationManager);
        borrowBook.process();

        borrowBook = new BorrowBook(books, 0, userNames[6], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 0, userNames[6], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 0, userNames[7], notificationManager);
        borrowBook.process();

        borrowBook = new BorrowBook(books, 3, userNames[8], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 3, userNames[8], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 4, userNames[9], notificationManager);
        borrowBook.process();
        returnBook = new ReturnBook(books, 4, userNames[9], notificationManager);
        returnBook.process();

        borrowBook = new BorrowBook(books, 3, userNames[4], notificationManager);
        borrowBook.process();

        // 測試借書後的書籍借閱紀錄
        assertEquals("[Alice, Frank, Grace, Hank]", books.get(0).getBorrowHistory().toString());
        assertEquals("[Bob]", books.get(1).getBorrowHistory().toString());
        assertEquals("[Charlie, Hank, Grace]", books.get(2).getBorrowHistory().toString());
        assertEquals("[David, Ivy, Eve]", books.get(3).getBorrowHistory().toString());
        assertEquals("[Eve, Jack]", books.get(4).getBorrowHistory().toString());

        // 測試還書後的書籍借閱紀錄
        assertEquals("[Alice, Frank, Grace]", books.get(0).getReturnHistory().toString());
        assertEquals("[Bob]", books.get(1).getReturnHistory().toString());
        assertEquals("[Charlie, Hank]", books.get(2).getReturnHistory().toString());
        assertEquals("[David, Ivy]", books.get(3).getReturnHistory().toString());
        assertEquals("[Eve, Jack]", books.get(4).getReturnHistory().toString());


        // 驗證觀察者收到的通知


        List<String> messages = testObserver.getReceivedMessages(); 
        assertEquals(23, messages.size());
        assertEquals("成功借閱 Java程式設計", messages.get(0));
        assertEquals("成功歸還 Java程式設計", messages.get(1));
        assertEquals("成功借閱 紅樓夢", messages.get(2));
        assertEquals("成功歸還 紅樓夢", messages.get(3));
        assertEquals("成功借閱 西遊記", messages.get(4));
        assertEquals("成功歸還 西遊記", messages.get(5));
        assertEquals("成功借閱 水滸傳", messages.get(6));
        assertEquals("成功歸還 水滸傳", messages.get(7));
        assertEquals("成功借閱 三國演義", messages.get(8));
        assertEquals("成功歸還 三國演義", messages.get(9));
        assertEquals("成功借閱 Java程式設計", messages.get(10));
        assertEquals("成功歸還 Java程式設計", messages.get(11));
        assertEquals("成功借閱 西遊記", messages.get(12));
        assertEquals("成功歸還 西遊記", messages.get(13));
        assertEquals("成功借閱 西遊記", messages.get(14));
        assertEquals("成功借閱 Java程式設計", messages.get(15));
        assertEquals("成功歸還 Java程式設計", messages.get(16));
        assertEquals("成功借閱 Java程式設計", messages.get(17));
        assertEquals("成功借閱 水滸傳", messages.get(18));
        assertEquals("成功歸還 水滸傳", messages.get(19));
        assertEquals("成功借閱 三國演義", messages.get(20));
        assertEquals("成功歸還 三國演義", messages.get(21));
        assertEquals("成功借閱 水滸傳", messages.get(22));
    }
}
