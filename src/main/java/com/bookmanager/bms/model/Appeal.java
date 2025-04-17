package com.bookmanager.bms.model;

import java.util.Date;

public class Appeal {
    private Integer appealid;

    private Integer userid;
    
    private Date createTime;

    private Byte status;

    private String appealContent;

    public Integer getAppealId() {
        return appealid;
    }

    public void setAppealId(Integer appealid) {
        this.appealid = appealid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getAppealStatus() {
        return status;
    }

    public void setAppealStatus(Byte status) {
        this.status = status;
    }

    public String getAppealContent() {
        return appealContent;
    }

    public void setAppealContent(String appealContent) {
        this.appealContent = appealContent;
    }
}
