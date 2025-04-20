package com.bookmanager.bms.model;

import java.util.Date;

public class Appeal {
    private Integer appealid;

    private Integer userid;
    
    private Integer suspensionid;  // 關聯的停權記錄ID
    
    private Date createTime;

    private Byte status;  // 0: 待處理, 1: 已批准, 2: 已拒絕

    private String appealContent;
    
    private String adminReply;  // 管理員回覆
    
    private Date replyTime;  // 回覆時間
    
    // 以下是非數據庫欄位，用於顯示
    private String username;
    private String suspensionReason;

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
    
    public Integer getSuspensionid() {
        return suspensionid;
    }

    public void setSuspensionid(Integer suspensionid) {
        this.suspensionid = suspensionid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getAppealContent() {
        return appealContent;
    }

    public void setAppealContent(String appealContent) {
        this.appealContent = appealContent;
    }
    
    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }
    
    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getSuspensionReason() {
        return suspensionReason;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }
}
