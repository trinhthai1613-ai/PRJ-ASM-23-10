package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RequestStatuses")
public class RequestStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusID")
    private Integer statusId;
    
    @Column(name = "StatusCode", unique = true, nullable = false, length = 50)
    private String statusCode;
    
    @Column(name = "StatusName", nullable = false, length = 100)
    private String statusName;
    
    @Column(name = "Description", length = 500)
    private String description;
    
    @Column(name = "DisplayOrder")
    private Integer displayOrder = 0;
    
    @Column(name = "ColorCode", length = 20)
    private String colorCode;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    // Constructors
    public RequestStatus() {
        this.createdAt = LocalDateTime.now();
    }
    
    public RequestStatus(String statusCode, String statusName, String colorCode) {
        this();
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.colorCode = colorCode;
    }
    
    // Getters and Setters
    public Integer getStatusId() {
        return statusId;
    }
    
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    
    public String getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "RequestStatus{" +
                "statusId=" + statusId +
                ", statusCode='" + statusCode + '\'' +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}