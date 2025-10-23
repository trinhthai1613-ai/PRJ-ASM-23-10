package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Divisions")
public class Division {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DivisionID")
    private Integer divisionId;
    
    @Column(name = "DivisionCode", unique = true, nullable = false, length = 20)
    private String divisionCode;
    
    @Column(name = "DivisionName", nullable = false, length = 100)
    private String divisionName;
    
    @Column(name = "Description", length = 500)
    private String description;
    
    @Column(name = "IsActive")
    private Boolean isActive = true;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "division", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
    
    // Constructors
    public Division() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Division(String divisionCode, String divisionName) {
        this();
        this.divisionCode = divisionCode;
        this.divisionName = divisionName;
    }
    
    // Getters and Setters
    public Integer getDivisionId() {
        return divisionId;
    }
    
    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }
    
    public String getDivisionCode() {
        return divisionCode;
    }
    
    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }
    
    public String getDivisionName() {
        return divisionName;
    }
    
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Division{" +
                "divisionId=" + divisionId +
                ", divisionCode='" + divisionCode + '\'' +
                ", divisionName='" + divisionName + '\'' +
                '}';
    }
}