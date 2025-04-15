package com.wangpeng.bms.model;

import java.util.Date;

public class SuspensionRecord {
    private Integer suspensionid;

    private Integer userid;

    private Integer bookid;

    private Date startDate;

    private Date endDate;

    private String suspensionReason;

    private Boolean borrowingPermission;

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

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
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

    public Boolean getBorrowingPermission() {
        return borrowingPermission;
    }

    public void setBorrowingPermission(Boolean borrowingPermission) {
        this.borrowingPermission = borrowingPermission;
    }
}
