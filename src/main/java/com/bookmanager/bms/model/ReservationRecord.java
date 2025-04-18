package com.bookmanager.bms.model;

public class ReservationRecord {
    private Integer reservationid;

    private Integer userid;

    private Integer bookid;

    private Byte status;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte reservationStatus) {
        this.status = reservationStatus;
    }

}
