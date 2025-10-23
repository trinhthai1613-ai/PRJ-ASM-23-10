package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RequestStatusHistory")
public class RequestStatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistoryID")
    private Integer historyId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RequestID", nullable = false)
    private LeaveRequest request;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FromStatusID")
    private RequestStatus fromStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToStatusID", nullable = false)
    private RequestStatus toStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChangedBy", nullable = false)
    private User changedBy;
    
    @Column(name = "ChangeNote", length = 1000)
    private String changeNote;
    
    @Column(name = "ChangedAt")
    private LocalDateTime changedAt;
    
    // Constructors
    public RequestStatusHistory() {
        this.changedAt = LocalDateTime.now();
    }
    
    public RequestStatusHistory(LeaveRequest request, RequestStatus fromStatus, RequestStatus toStatus, User changedBy) {
        this();
        this.request = request;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.changedBy = changedBy;
    }
    
    // Getters and Setters
    public Integer getHistoryId() {
        return historyId;
    }
    
    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }
    
    public LeaveRequest getRequest() {
        return request;
    }
    
    public void setRequest(LeaveRequest request) {
        this.request = request;
    }
    
    public RequestStatus getFromStatus() {
        return fromStatus;
    }
    
    public void setFromStatus(RequestStatus fromStatus) {
        this.fromStatus = fromStatus;
    }
    
    public RequestStatus getToStatus() {
        return toStatus;
    }
    
    public void setToStatus(RequestStatus toStatus) {
        this.toStatus = toStatus;
    }
    
    public User getChangedBy() {
        return changedBy;
    }
    
    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }
    
    public String getChangeNote() {
        return changeNote;
    }
    
    public void setChangeNote(String changeNote) {
        this.changeNote = changeNote;
    }
    
    public LocalDateTime getChangedAt() {
        return changedAt;
    }
    
    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}