package com.wangpeng.bms.model;

public class ReservationRecord {
    private Integer reservationid;

    private Integer userid;

    private Integer bookid;

    private Boolean status;

    public Integer getReservationid() {
        return reservationid;
    }

    public void setReservationid(Integer reservationid) {
        this.reservationid = reservationid;
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

    public Boolean getReservationStatus() {
        return status;
    }

    public void setReservationStatus(Boolean reservationStatus) {
        this.status = reservationStatus;
    }

}
