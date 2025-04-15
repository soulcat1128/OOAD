package com.wangpeng.bms.model;


import java.util.Date;

public class Appeal {
    private Integer appealid;

    private Integer userid;
    
    private Date appealtime;

    private Boolean appealStatus;

    public Integer getAppealid() {
        return appealid;
    }

    public void setAppealid(Integer appealid) {
        this.appealid = appealid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getAppealtime() {
        return appealtime;
    }

    public void setAppealtime(Date appealtime) {
        this.appealtime = appealtime;
    }

    public Boolean getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(Boolean appealStatus) {
        this.appealStatus = appealStatus;
    }
}
