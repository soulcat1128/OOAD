package com.bookmanager.bms.model;

import java.util.Calendar;
import java.util.Date;

public class SuspensionRecord {
    // 常數定義
    public static final byte BORROWING_PROHIBITED = 0;  // 禁止借閱
    public static final byte BORROWING_ALLOWED = 1;     // 允許借閱
    public static final int DEFAULT_SUSPENSION_DAYS = 30; // 預設停權天數
    
    private Integer suspensionid;
    private Integer userid;
    private Integer borrowid;
    private Date startDate;
    private Date endDate;
    private String suspensionReason;
    private Byte borrowingPermission; // 0: 禁止借閱, 1: 允許借閱

    public Integer getSuspensionid() {
        return suspensionid;
    }

    public void setSuspensionid(Integer suspensionid) {
        this.suspensionid = suspensionid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getBorrowid() {
        return borrowid;
    }

    public void setBorrowid(Integer borrowid) {
        this.borrowid = borrowid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }

    public void setBorrowingPermission(Byte borrowingPermission) {
        this.borrowingPermission = borrowingPermission;
    }

    /**
     * 恢復使用者的借閱權限
     */
    public void restorePermission() {
        this.borrowingPermission = BORROWING_ALLOWED;
    }

    /**
     * 根據開始日期設置結束日期
     * @param days 停權天數
     */
    public void setEndDateFromStart(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.startDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        this.endDate = calendar.getTime();
    }
}
