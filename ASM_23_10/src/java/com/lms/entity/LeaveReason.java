package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LeaveReasons")
public class LeaveReason {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReasonID")
    private Integer reasonId;
    
    @Column(name = "ReasonCode", unique = true, nullable = false, length = 50)
    private String reasonCode;
    
    @Column(name = "ReasonName", nullable = false, length = 200)
    private String reasonName;
    
    @Column(name = "Description", length = 500)
    private String description;
    
    @Column(name = "DeductFromLeave")
    private Boolean deductFromLeave = true;
    
    @Column(name = "RequiresApproval")
    private Boolean requiresApproval = true;
    
    @Column(name = "DisplayOrder")
    private Integer displayOrder = 0;
    
    @Column(name = "IsActive")
    private Boolean isActive = true;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    // Constructors
    public LeaveReason() {
        this.createdAt = LocalDateTime.now();
    }
    
    public LeaveReason(String reasonCode, String reasonName) {
        this();
        this.reasonCode = reasonCode;
        this.reasonName = reasonName;
    }
    
    // Getters and Setters
    public Integer getReasonId() {
        return reasonId;
    }
    
    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }
    
    public String getReasonCode() {
        return reasonCode;
    }
    
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    
    public String getReasonName() {
        return reasonName;
    }
    
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getDeductFromLeave() {
        return deductFromLeave;
    }
    
    public void setDeductFromLeave(Boolean deductFromLeave) {
        this.deductFromLeave = deductFromLeave;
    }
    
    public Boolean getRequiresApproval() {
        return requiresApproval;
    }
    
    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "LeaveReason{" +
                "reasonId=" + reasonId +
                ", reasonCode='" + reasonCode + '\'' +
                ", reasonName='" + reasonName + '\'' +
                '}';
    }
}