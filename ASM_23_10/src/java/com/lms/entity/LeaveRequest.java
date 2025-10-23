package com.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "LeaveRequests")
public class LeaveRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID")
    private Integer requestId;
    
    @Column(name = "RequestCode", unique = true, nullable = false, length = 50)
    private String requestCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReasonID", nullable = false)
    private LeaveReason reason;
    
    @Column(name = "FromDate", nullable = false)
    private LocalDate fromDate;
    
    @Column(name = "ToDate", nullable = false)
    private LocalDate toDate;
    
    @Column(name = "TotalDays", nullable = false, precision = 5, scale = 1)
    private BigDecimal totalDays;
    
    @Column(name = "CustomReason", length = 1000)
    private String customReason;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StatusID", nullable = false)
    private RequestStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProcessedBy")
    private User processedBy;
    
    @Column(name = "ProcessedAt")
    private LocalDateTime processedAt;
    
    @Column(name = "ProcessNote", length = 1000)
    private String processNote;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
    
    // Constructors
    public LeaveRequest() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public LeaveRequest(User user, LeaveReason reason, LocalDate fromDate, LocalDate toDate, BigDecimal totalDays) {
        this();
        this.user = user;
        this.reason = reason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.totalDays = totalDays;
    }
    
    // Getters and Setters
    public Integer getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
    
    public String getRequestCode() {
        return requestCode;
    }
    
    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LeaveReason getReason() {
        return reason;
    }
    
    public void setReason(LeaveReason reason) {
        this.reason = reason;
    }
    
    public LocalDate getFromDate() {
        return fromDate;
    }
    
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    
    public LocalDate getToDate() {
        return toDate;
    }
    
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
    
    public BigDecimal getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(BigDecimal totalDays) {
        this.totalDays = totalDays;
    }
    
    public String getCustomReason() {
        return customReason;
    }
    
    public void setCustomReason(String customReason) {
        this.customReason = customReason;
    }
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    public User getProcessedBy() {
        return processedBy;
    }
    
    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public String getProcessNote() {
        return processNote;
    }
    
    public void setProcessNote(String processNote) {
        this.processNote = processNote;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "requestId=" + requestId +
                ", requestCode='" + requestCode + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", totalDays=" + totalDays +
                '}';
    }
}