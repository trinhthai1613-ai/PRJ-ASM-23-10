package com.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userId;
    
    @Column(name = "EmployeeCode", unique = true, nullable = false, length = 20)
    private String employeeCode;
    
    @Column(name = "Username", unique = true, nullable = false, length = 100)
    private String username;
    
    @Column(name = "PasswordHash", nullable = false, length = 255)
    private String passwordHash;
    
    @Column(name = "Email", unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "FullName", nullable = false, length = 200)
    private String fullName;
    
    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DivisionID", nullable = false)
    private Division division;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagerID")
    private User manager;
    
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private Set<User> subordinates = new HashSet<>();
    
    @Column(name = "HireDate", nullable = false)
    private LocalDate hireDate;
    
    @Column(name = "AnnualLeaveDays")
    private Integer annualLeaveDays = 12;
    
    @Column(name = "RemainingLeaveDays", precision = 5, scale = 1)
    private BigDecimal remainingLeaveDays = new BigDecimal("12.0");
    
    @Column(name = "IsActive")
    private Boolean isActive = true;
    
    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<LeaveRequest> leaveRequests = new HashSet<>();
    
    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public User(String employeeCode, String username, String fullName, String email) {
        this();
        this.employeeCode = employeeCode;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }
    
    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getEmployeeCode() {
        return employeeCode;
    }
    
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Division getDivision() {
        return division;
    }
    
    public void setDivision(Division division) {
        this.division = division;
    }
    
    public User getManager() {
        return manager;
    }
    
    public void setManager(User manager) {
        this.manager = manager;
    }
    
    public Set<User> getSubordinates() {
        return subordinates;
    }
    
    public void setSubordinates(Set<User> subordinates) {
        this.subordinates = subordinates;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public Integer getAnnualLeaveDays() {
        return annualLeaveDays;
    }
    
    public void setAnnualLeaveDays(Integer annualLeaveDays) {
        this.annualLeaveDays = annualLeaveDays;
    }
    
    public BigDecimal getRemainingLeaveDays() {
        return remainingLeaveDays;
    }
    
    public void setRemainingLeaveDays(BigDecimal remainingLeaveDays) {
        this.remainingLeaveDays = remainingLeaveDays;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
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
    
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }
    
    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    
    public Set<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }
    
    public void setLeaveRequests(Set<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", employeeCode='" + employeeCode + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}