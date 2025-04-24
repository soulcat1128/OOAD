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

    /** 是否仍處於「預約中」狀態 */
    public boolean isActive() {
        return status != null && status == 0;
    }

    /** 確認借閱—將狀態由 0 -> 1 */
    public void confirm() {
        if (!isActive()) {
            throw new IllegalStateException("此預約紀錄不可確認借閱（status=" + status + "）");
        }
        this.status = 1;
    }
}
