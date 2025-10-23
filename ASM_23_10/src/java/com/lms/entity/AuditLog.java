package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLogs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Integer logId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User user;
    
    @Column(name = "TableName", nullable = false, length = 100)
    private String tableName;
    
    @Column(name = "RecordID")
    private Integer recordId;
    
    @Column(name = "Action", nullable = false, length = 50)
    private String action;
    
    @Column(name = "OldValue", columnDefinition = "NVARCHAR(MAX)")
    private String oldValue;
    
    @Column(name = "NewValue", columnDefinition = "NVARCHAR(MAX)")
    private String newValue;
    
    @Column(name = "IPAddress", length = 50)
    private String ipAddress;
    
    @Column(name = "UserAgent", length = 500)
    private String userAgent;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    // Constructors
    public AuditLog() {
        this.createdAt = LocalDateTime.now();
    }
    
    public AuditLog(User user, String tableName, String action) {
        this();
        this.user = user;
        this.tableName = tableName;
        this.action = action;
    }
    
    // Getters and Setters
    public Integer getLogId() {
        return logId;
    }
    
    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public Integer getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}