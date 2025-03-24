import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.wangpeng.bms.model.AdminObserver;
import com.wangpeng.bms.model.BookFactory;
import com.wangpeng.bms.model.BookInfo;
import com.wangpeng.bms.model.BookSeries;
import com.wangpeng.bms.model.Borrow;
import com.wangpeng.bms.model.BorrowBook;
import com.wangpeng.bms.model.IBook;
import com.wangpeng.bms.model.NotificationManager;
import com.wangpeng.bms.model.Process;
import com.wangpeng.bms.model.ReturnBook;
import com.wangpeng.bms.model.User;


public class BorrowTest {

    // 書本集合
    private List<BookInfo> books;
    // 借還記錄集合
    private List<Borrow> borrows;
    private BookFactory bookFactory;
    private BookInfo book1;
    private BookInfo book2;
    private BookInfo book3;
    private BookInfo book4;
    private BookInfo book5;

    // 初始化測試資料
    @BeforeEach
    public void setUp() {
        // 測試資料
        bookFactory = new BookFactory();
        borrows = new ArrayList<>();
        books = new ArrayList<>();
        book1 = new BookInfo();
        book1.setBookid(0);
        book1.setBookname("Java程式設計");
        book1.setBookauthor("耿祥義");
        book1.setBookprice(new BigDecimal("55.50"));
        book1.setBooktypeid(1);
        book1.setBookdesc(
                "《Java2實用教程》不僅可以作為大專院校相關科系的教材，也適合自學者及軟體開發人員參考使用。Java是一種很優秀的程式語言，具有物件導向、跨平台、安全、穩定和多執行緒等特點，是目前軟體設計中極為健壯的程式語言。Java語言不僅可以用來開發大型的應用程式，而且特別適合於在網際網路上應用開發，Java已成為網路時代最重要的程式語言之一。");
        book1.setIsborrowed((byte) 0);
        book1.setMaterial("paper");

        book2 = new BookInfo();
        book2.setBookid(1);
        book2.setBookname("紅樓夢");
        book2.setBookauthor("曹雪芹");
        book2.setBookprice(new BigDecimal("36.00"));
        book2.setBooktypeid(3);
        book2.setBookdesc("《紅樓夢》是一部百科全書式的長篇小說。以寶黛愛情悲劇為主線，以四大家族的榮辱興衰為背景，描繪出18世紀中國封建社會的各個方面。");
        book2.setIsborrowed((byte) 0);
        book2.setMaterial("paper");

        book3 = new BookInfo();
        book3.setBookid(2);
        book3.setBookname("西遊記");
        book3.setBookauthor("吳承恩");
        book3.setBookprice(new BigDecimal("60.00"));
        book3.setBooktypeid(3);
        book3.setBookdesc(
                "《西遊記》主要描寫的是孫悟空保唐三藏西天取經，歷經九九八十一難的故事。唐三藏取經是歷史上一件真實的事。大約距今一千三百多年前，即唐太宗貞觀元年（627），年僅25歲的青年和尚玄奘離開京城長安，只身到天竺（印度）遊學。他從長安出發後，途經中亞、阿富汗、巴基斯坦，歷盡艱難險阻，最後到達了印度。他在那裡學習了兩年多，並在一次大型佛教經學辯論會任主講，受到了讚譽。");
        book3.setIsborrowed((byte) 0);
        book3.setMaterial("paper");

        book4 = new BookInfo();
        book4.setBookid(3);
        book4.setBookname("水滸傳");
        book4.setBookauthor("施耐庵");
        book4.setBookprice(new BigDecimal("50.60"));
        book4.setBooktypeid(3);
        book4.setBookdesc(
                "《水滸傳》是我國第一部以農民起義為題材的長篇章回小說，是我國文學史上一座巍然屹立的豐碑，也是世界文學寶庫中一顆光彩奪目的明珠。數百年來，它一直深受我國人民的喜愛，並被譯為多種文字，成為我國流傳最為廣泛的古代長篇小說之一。");
        book4.setIsborrowed((byte) 0);
        book4.setMaterial("paper");

        book5 = new BookInfo();
        book5.setBookid(4);
        book5.setBookname("三國演義");
        book5.setBookauthor("羅貫中");
        book5.setBookprice(new BigDecimal("42.00"));
        book5.setBooktypeid(3);
        book5.setBookdesc("《三國演義》又名《三國志演義》、《三國志通俗演義》，是我國小說史上最著名最傑出的長篇章回體歷史小說。");
        book5.setIsborrowed((byte) 0);
        book5.setMaterial("paper");

        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
    }

    @Test // 單本書籍借還測試、觀察者模式測試
    void test_SingleBook_Borrow_And_Return_And_Observer() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        // 建立通知管理器群組
        NotificationManager notificationManager = new NotificationManager();
        AdminObserver AdminObserver = new AdminObserver();// 用來紀錄測試的通知訊息
        notificationManager.subscribe(AdminObserver);

        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process borrowBook = new BorrowBook(books, notificationManager, User_Borrow);
        Process returnBook = new ReturnBook(books, notificationManager, User_Borrow);
        IBook book_1 = bookFactory.createBook(book1),
                book_2 = bookFactory.createBook(book2),
                book_3 = bookFactory.createBook(book3);

        User user_1 = new User(1, "User1"),
                user_2 = new User(2, "User2"),
                user_3 = new User(3, "User3");

        // 新版測試
        // 測試庫存足夠情況
        borrowBook.process(book_1, user_1);// user1 借book1
        borrowBook.process(book_2, user_2);// user2 借book2
        returnBook.process(book_1, user_1);// user1 還book1
        borrowBook.process(book_3, user_3);// user3 借book3

        // 測試庫存不足情況
        borrowBook.process(book_2, user_1);
        borrowBook.process(book_3, user_2);
        assertEquals((byte) 1, book2.getIsborrowed());
        assertEquals((byte) 1, book3.getIsborrowed());


        User_Borrow.forEach((k, v) -> {
            System.out.println("用戶: " + k.getUsername() + ", 書籍: " + v.getBookname() + ", 借書時間: " + v.getBorrowtimestr()
                    + ", 還書時間: " + v.getReturntimestr());
        });
        assertEquals("用戶: User1, 書籍: Java程式設計, 借書時間: " + sdFormat.format(new Date()) + ", 還書時間: "
                + sdFormat.format(new Date()), User_Borrow.get(user_1).toString());
        assertEquals("用戶: User2, 尚未歸還此書籍: 紅樓夢", User_Borrow.get(user_2).toString());
        assertEquals("用戶: User3, 尚未歸還此書籍: 西遊記", User_Borrow.get(user_3).toString());

        // 驗證觀察者收到的通知
        List<String> messages = AdminObserver.getReceivedMessages();
        for (String message : messages) {
            System.out.println(message);
        }
        assertEquals(6, messages.size());
        assertEquals("User1成功借閱 Java程式設計!!", messages.get(0));
        assertEquals("User2成功借閱 紅樓夢!!", messages.get(1));
        assertEquals("User1成功歸還 Java程式設計!!", messages.get(2));
        assertEquals("User3成功借閱 西遊記!!", messages.get(3));
        assertEquals("目前庫存不足無法借閱 紅樓夢 請管理員補貨!!", messages.get(4));
        assertEquals("目前庫存不足無法借閱 西遊記 請管理員補貨!!", messages.get(5));

    }

    @Test // 系列書籍借還測試、觀察者模式測試
    void test_MultiBooks_Borrow_And_Return_And_Observer() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        // 建立通知管理器群組
        NotificationManager notificationManager = new NotificationManager();
        AdminObserver AdminObserver = new AdminObserver();// 用來紀錄測試的通知訊息
        notificationManager.subscribe(AdminObserver);

        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process borrowBook = new BorrowBook(books, notificationManager, User_Borrow);
        Process returnBook = new ReturnBook(books, notificationManager, User_Borrow);
        IBook book_1 = bookFactory.createBook(book1),
                book_2 = bookFactory.createBook(book2),
                book_3 = bookFactory.createBook(book3),
                book_4 = bookFactory.createBook(book4),
                book_5 = bookFactory.createBook(book5);

        BookSeries bookSeries = (BookSeries) bookFactory.createBookSeries("四大名著");
        bookSeries.add(book_2);
        bookSeries.add(book_3);
        bookSeries.add(book_4);
        bookSeries.add(book_5);



        User user_1 = new User(1, "User1"),
                user_2 = new User(2, "User2"),
                user_3 = new User(3, "User3");

        borrowBook.process(bookSeries, user_1);
        borrowBook.process(bookSeries, user_2);
        returnBook.process(bookSeries, user_1);
        borrowBook.process(bookSeries, user_3);

        User_Borrow.forEach((k, v) -> {
            System.out.println("用戶: " + k.getUsername() + ", 書籍: " + v.getBookname() + ", 借書時間: " + v.getBorrowtimestr()
                    + ", 還書時間: " + v.getReturntimestr());
        });

        assertEquals("用戶: User1, 書籍: 四大名著, 借書時間: " + sdFormat.format(new Date()) + ", 還書時間: "
                + sdFormat.format(new Date()), User_Borrow.get(user_1).toString());
        assertEquals("用戶: User3, 尚未歸還此書籍: 四大名著", User_Borrow.get(user_3).toString());

        // 驗證觀察者收到的通知
        List<String> messages = AdminObserver.getReceivedMessages();
        for (String message : messages) {
            System.out.println(message);
        }

        assertEquals(4, messages.size());
        assertEquals("User1成功借閱 四大名著!!", messages.get(0));
        assertEquals("目前庫存不足無法借閱 四大名著 請管理員補貨!!", messages.get(1));
        assertEquals("User1成功歸還 四大名著!!", messages.get(2));
        assertEquals("User3成功借閱 四大名著!!", messages.get(3));

    }

    @Test // 混和單本書籍和系列書籍借還測試、觀察者模式測試
    void test_SingleBook_MultiBooks_Borrow_And_Return_And_Observer() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        // 建立通知管理器群組
        NotificationManager notificationManager = new NotificationManager();
        AdminObserver AdminObserver = new AdminObserver();// 用來紀錄測試的通知訊息
        notificationManager.subscribe(AdminObserver);

        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process borrowBook = new BorrowBook(books, notificationManager, User_Borrow);
        Process returnBook = new ReturnBook(books, notificationManager, User_Borrow);
        IBook book_1 = bookFactory.createBook(book1),
                book_2 = bookFactory.createBook(book2),
                book_3 = bookFactory.createBook(book3),
                book_4 = bookFactory.createBook(book4),
                book_5 = bookFactory.createBook(book5);

        BookSeries bookSeries = (BookSeries) bookFactory.createBookSeries("四大名著");
        bookSeries.add(book_2);
        bookSeries.add(book_3);
        bookSeries.add(book_4);
        bookSeries.add(book_5);

        User user_1 = new User(1, "User1"),
                user_2 = new User(2, "User2"),
                user_3 = new User(3, "User3");

        User_Borrow.forEach((k, v) -> {
            System.out.println("用戶: " + k.getUsername() + ", 書籍: " + v.getBookname() + ", 借書時間: " + v.getBorrowtimestr()
                    + ", 還書時間: " + v.getReturntimestr());
        });


        //模擬測試系列書籍中某一本借出時，系列書籍會無法借出
        borrowBook.process(book_2, user_1);
        borrowBook.process(bookSeries, user_2);
        returnBook.process(book_2, user_1);
        borrowBook.process(bookSeries, user_2);

        //模擬測試系列書籍借出時，該系列書籍中的每一本都會無法借出
        borrowBook.process(book_4, user_3);
        borrowBook.process(book_5, user_1);
        returnBook.process(bookSeries, user_2);

        //模擬測試系列書籍歸還時，即可借用系列書籍中的任何一本
        borrowBook.process(book_5, user_1);
        borrowBook.process(book_4, user_3);


        User_Borrow.forEach((k, v) -> {
            System.out.println("用戶: " + k.getUsername() + ", 書籍: " + v.getBookname() + ", 借書時間: " + v.getBorrowtimestr()
                    + ", 還書時間: " + v.getReturntimestr());
        });

        assertEquals("用戶: User1, 尚未歸還此書籍: 三國演義", User_Borrow.get(user_1).toString());
        assertEquals("用戶: User2, 書籍: 四大名著, 借書時間: " + sdFormat.format(new Date()) + ", 還書時間: "+ sdFormat.format(new Date()), User_Borrow.get(user_2).toString());
        assertEquals("用戶: User3, 尚未歸還此書籍: 水滸傳", User_Borrow.get(user_3).toString());
        



        // 驗證觀察者收到的通知
        List<String> messages = AdminObserver.getReceivedMessages();
        for (String message : messages) {
            System.out.println(message);
        }

        assertEquals(9, messages.size());
        assertEquals("User1成功借閱 紅樓夢!!", messages.get(0));
        assertEquals("目前庫存不足無法借閱 四大名著 請管理員補貨!!", messages.get(1));
        assertEquals("User1成功歸還 紅樓夢!!", messages.get(2));
        assertEquals("User2成功借閱 四大名著!!", messages.get(3));
        assertEquals("目前庫存不足無法借閱 水滸傳 請管理員補貨!!", messages.get(4));
        assertEquals("目前庫存不足無法借閱 三國演義 請管理員補貨!!", messages.get(5));
        assertEquals("User2成功歸還 四大名著!!", messages.get(6));
        assertEquals("User1成功借閱 三國演義!!", messages.get(7));
        assertEquals("User3成功借閱 水滸傳!!", messages.get(8));
    }

    @Test
    public void testObserverBehavior() {
        NotificationManager notificationManager = new NotificationManager();
        AdminObserver adminObserver1 = new AdminObserver();
        AdminObserver adminObserver2 = new AdminObserver();

        // 測試無觀察者的情況
        notificationManager.notifyObservers("No observers should receive this message.");
        assertEquals(0, adminObserver1.getReceivedMessages().size());

        // 測試添加觀察者
        notificationManager.subscribe(adminObserver1);
        notificationManager.notifyObservers("Message to Observer1");
        assertEquals(1, adminObserver1.getReceivedMessages().size());
        assertEquals("Message to Observer1", adminObserver1.getReceivedMessages().get(0));

        // 測試添加多個觀察者
        notificationManager.subscribe(adminObserver2);
        notificationManager.notifyObservers("Message to both Observers");
        assertEquals(2, adminObserver1.getReceivedMessages().size());
        assertEquals(1, adminObserver2.getReceivedMessages().size());
        assertEquals("Message to both Observers", adminObserver1.getReceivedMessages().get(1));
        assertEquals("Message to both Observers", adminObserver2.getReceivedMessages().get(0));

        // 測試移除觀察者
        notificationManager.unsubscribe(adminObserver1);
        notificationManager.notifyObservers("Only Observer2 should receive this");
        assertEquals(2, adminObserver1.getReceivedMessages().size()); // Observer1不再接收新消息
        assertEquals(2, adminObserver2.getReceivedMessages().size());
        assertEquals("Only Observer2 should receive this", adminObserver2.getReceivedMessages().get(1));
    }

    @Test
    public void testBorrowWithNullBook() {
        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process borrowBook = new BorrowBook(books, new NotificationManager(), User_Borrow);
        Process returnBook = new ReturnBook(books, new NotificationManager(), User_Borrow);
        User user_1 = new User(1, "User1");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowBook.process(null, user_1);
        });

        assertEquals("Book cannot be null.", exception.getMessage());
    }

    @Test
    public void testReturnWithNullBook() {
        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process returnBook = new ReturnBook(books, new NotificationManager(), User_Borrow);
        User user_1 = new User(1, "User1");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            returnBook.process(null, user_1);
        });

        assertEquals("Book cannot be null.", exception.getMessage());
    }

    @Test
    public void testReturnNotBorrowedBook() {
        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process returnBook = new ReturnBook(books, new NotificationManager(), User_Borrow);
        book1.setIsborrowed((byte) 0); // 模擬書籍未被借出
        IBook book_1 = bookFactory.createBook(book1);
        User user_1 = new User(1, "User1");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            returnBook.process(book_1, user_1);
        });

        assertEquals("書籍 'Java程式設計' 未借出，無法歸還!!", exception.getMessage());
    }

    @Test
    public void testReturnNotBorrowedBookInSeries() {
        // 借書流程
        HashMap<User, Borrow> User_Borrow = new HashMap<>();
        Process returnBook = new ReturnBook(books, new NotificationManager(), User_Borrow);
        book1.setIsborrowed((byte) 1); // 模擬書籍已被借出
        book2.setIsborrowed((byte) 0); // 模擬書籍未被借出
        IBook book_1 = bookFactory.createBook(book1);
        IBook book_2 = bookFactory.createBook(book2);
        BookSeries bookSeries = (BookSeries) bookFactory.createBookSeries("testSeries");
        bookSeries.add(book_1);
        bookSeries.add(book_2);
        User user_1 = new User(1, "User1");
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            returnBook.process(bookSeries, user_1);
        });

        assertEquals("書籍 '紅樓夢' 未借出，無法歸還!!", exception.getMessage());
        assertEquals((byte) 1, book1.getIsborrowed());
        assertEquals((byte) 0, book2.getIsborrowed());
    }
}
