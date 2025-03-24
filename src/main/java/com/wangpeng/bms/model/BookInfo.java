package com.wangpeng.bms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * BookInfo 類別代表書籍的詳細資訊。
 */
public class BookInfo {

    public BookInfo() {
        borrowHistory = new ArrayList<>();
        returnHistory = new ArrayList<>();
    }

    /**
     * 書籍ID
     */
    private Integer bookid;

    /**
     * 書名
     */
    private String bookname;

    /**
     * 作者
     */
    private String bookauthor;

    /**
     * 價格
     */
    private BigDecimal bookprice;

    /**
     * 書籍類型ID
     */
    private Integer booktypeid;

    /**
     * 書籍類型名稱
     */
    private String booktypename;

    /**
     * 書籍描述
     */
    private String bookdesc;

    /**
     * 是否已借出
     */
    private Byte isborrowed;

    /**
     * 書籍圖片URL
     */
    private String bookimg;

    /**
     * 敘述者
     */
    private String narrator;

    /**
     * 書籍時長（適用於有聲書）
     */
    private Integer duration;

    /**
     * 頁數（適用於實體書）
     */
    private Integer pageCount;

    /**
     * 材質（適用於實體書）
     */
    private String material;

    /**
     * 檔案大小（適用於電子書）
     */
    private Integer fileSize;


    /**
     * 借書歷史紀錄
     */
    private List<String> borrowHistory;
    /**
     * 還書歷史紀錄
     */
    private List<String> returnHistory;

    /**
     * 獲取借閱歷史紀錄
     * @return 借閱歷史紀錄
     */
    public List<String> getBorrowHistory() {
        return borrowHistory;
    }

    /**
     * 設定借閱歷史紀錄
     * @param borrowHistory 借閱歷史紀錄
     */
    public void setBorrowHistory(String History) {
        this.borrowHistory.add(History);
    }

    /**
     * 獲取還書歷史紀錄
     * @return 還書歷史紀錄
     */
    public List<String> getReturnHistory() {
        return returnHistory;
    }

    /**
     * 設定還書歷史紀錄
     * @param returnHistory 還書歷史紀錄
     */
    public void setReturnHistory(String History) {
        this.returnHistory.add(History);
    }

    /**
     * 獲取頁數
     * @return 頁數
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * 設定頁數
     * @param pageCount 頁數
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 獲取材質
     * @return 材質
     */
    public String getMaterial() {
        return material;
    }

    /**
     * 設定材質
     * @param material 材質
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * 獲取敘述者
     * @return 敘述者
     */
    public String getNarrator() {
        return narrator;
    }

    /**
     * 設定敘述者
     * @param narrator 敘述者
     */
    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    /**
     * 獲取時長
     * @return 時長
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * 設定時長
     * @param duration 時長
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * 獲取檔案大小
     * @return 檔案大小
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * 設定檔案大小
     * @param fileSize 檔案大小
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 獲取書籍ID
     * @return 書籍ID
     */
    public Integer getBookid() {
        return bookid;
    }

    /**
     * 設定書籍ID
     * @param bookid 書籍ID
     */
    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    /**
     * 獲取書名
     * @return 書名
     */
    public String getBookname() {
        return bookname;
    }

    /**
     * 設定書名
     * @param bookname 書名
     */
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    /**
     * 獲取作者
     * @return 作者
     */
    public String getBookauthor() {
        return bookauthor;
    }

    /**
     * 設定作者
     * @param bookauthor 作者
     */
    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    /**
     * 獲取價格
     * @return 價格
     */
    public BigDecimal getBookprice() {
        return bookprice;
    }

    /**
     * 設定價格
     * @param bookprice 價格
     */
    public void setBookprice(BigDecimal bookprice) {
        this.bookprice = bookprice;
    }

    /**
     * 獲取書籍類型ID
     * @return 書籍類型ID
     */
    public Integer getBooktypeid() {
        return booktypeid;
    }

    /**
     * 設定書籍類型ID
     * @param booktypeid 書籍類型ID
     */
    public void setBooktypeid(Integer booktypeid) {
        this.booktypeid = booktypeid;
    }

    /**
     * 獲取書籍類型名稱
     * @return 書籍類型名稱
     */
    public String getBooktypename() {
        return booktypename;
    }

    /**
     * 設定書籍類型名稱
     * @param booktypename 書籍類型名稱
     */
    public void setBooktypename(String booktypename) {
        this.booktypename = booktypename;
    }

    /**
     * 獲取書籍描述
     * @return 書籍描述
     */
    public String getBookdesc() {
        return bookdesc;
    }

    /**
     * 設定書籍描述
     * @param bookdesc 書籍描述
     */
    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    /**
     * 獲取是否已借出
     * @return 是否已借出
     */
    public Byte getIsborrowed() {
        return isborrowed;
    }

    /**
     * 設定是否已借出
     * @param isborrowed 是否已借出
     */
    public void setIsborrowed(Byte isborrowed) {
        this.isborrowed = isborrowed;
    }

    /**
     * 獲取書籍圖片URL
     * @return 書籍圖片URL
     */
    public String getBookimg() {
        return bookimg;
    }

    /**
     * 設定書籍圖片URL
     * @param bookimg 書籍圖片URL
     */
    public void setBookimg(String bookimg) {
        this.bookimg = bookimg;
    }
}