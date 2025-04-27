package com.bookmanager.bms.model;

import java.util.Date;

public class Appeal {
    public static final byte STATUS_PENDING = 0;    // 待處理
    public static final byte STATUS_APPROVED = 1;   // 已批准
    public static final byte STATUS_REJECTED = 2;   // 已拒絕
    
    private Integer appealid;
    private Integer userid;
    private Integer suspensionid;  // 關聯的停權記錄ID
    private Date createTime;
    private Byte status;
    private String appealContent;
    private String adminReply;     // 管理員回覆
    private Date replyTime;        // 回覆時間
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

    /**
     * 判斷申訴是否可被處理
     * @return 如果申訴處於待處理狀態則返回true
     */
    public boolean canProcess() {
        return status == STATUS_PENDING;
    }

    /**
     * 處理申訴
     * @param status 處理後的狀態
     * @param adminReply 管理員回覆
     */
    private void processAppeal(byte status, String adminReply) {
        if (!canProcess()) {
            throw new IllegalStateException("這個申訴已經被處理過了");
        }
        this.status = status;
        this.adminReply = adminReply;
        this.replyTime = new Date();
    }

    /**
     * 批准此申訴
     * @param adminReply 管理員回覆
     */
    public void approve(String adminReply) {
        processAppeal(STATUS_APPROVED, adminReply);
    }

    /**
     * 拒絕此申訴
     * @param adminReply 管理員回覆
     */
    public void reject(String adminReply) {
        processAppeal(STATUS_REJECTED, adminReply);
    }
}
