package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RoleFeatures", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"RoleID", "FeatureID"})
})
public class RoleFeature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleFeatureID")
    private Integer roleFeatureId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FeatureID", nullable = false)
    private Feature feature;
    
    @Column(name = "CanCreate")
    private Boolean canCreate = false;
    
    @Column(name = "CanRead")
    private Boolean canRead = false;
    
    @Column(name = "CanUpdate")
    private Boolean canUpdate = false;
    
    @Column(name = "CanDelete")
    private Boolean canDelete = false;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    // Constructors
    public RoleFeature() {
        this.createdAt = LocalDateTime.now();
    }
    
    public RoleFeature(Role role, Feature feature) {
        this();
        this.role = role;
        this.feature = feature;
    }
    
    // Getters and Setters
    public Integer getRoleFeatureId() {
        return roleFeatureId;
    }
    
    public void setRoleFeatureId(Integer roleFeatureId) {
        this.roleFeatureId = roleFeatureId;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Feature getFeature() {
        return feature;
    }
    
    public void setFeature(Feature feature) {
        this.feature = feature;
    }
    
    public Boolean getCanCreate() {
        return canCreate;
    }
    
    public void setCanCreate(Boolean canCreate) {
        this.canCreate = canCreate;
    }
    
    public Boolean getCanRead() {
        return canRead;
    }
    
    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }
    
    public Boolean getCanUpdate() {
        return canUpdate;
    }
    
    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }
    
    public Boolean getCanDelete() {
        return canDelete;
    }
    
    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}