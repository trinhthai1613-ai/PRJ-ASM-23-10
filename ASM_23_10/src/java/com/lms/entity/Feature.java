package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Features")
public class Feature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeatureID")
    private Integer featureId;
    
    @Column(name = "FeatureCode", unique = true, nullable = false, length = 50)
    private String featureCode;
    
    @Column(name = "FeatureName", nullable = false, length = 100)
    private String featureName;
    
    @Column(name = "FeaturePath", nullable = false, length = 200)
    private String featurePath;
    
    @Column(name = "Description", length = 500)
    private String description;
    
    @Column(name = "IsActive")
    private Boolean isActive = true;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY)
    private Set<RoleFeature> roleFeatures = new HashSet<>();
    
    // Constructors
    public Feature() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Feature(String featureCode, String featureName, String featurePath) {
        this();
        this.featureCode = featureCode;
        this.featureName = featureName;
        this.featurePath = featurePath;
    }
    
    // Getters and Setters
    public Integer getFeatureId() {
        return featureId;
    }
    
    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }
    
    public String getFeatureCode() {
        return featureCode;
    }
    
    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }
    
    public String getFeatureName() {
        return featureName;
    }
    
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
    
    public String getFeaturePath() {
        return featurePath;
    }
    
    public void setFeaturePath(String featurePath) {
        this.featurePath = featurePath;
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
    
    public Set<RoleFeature> getRoleFeatures() {
        return roleFeatures;
    }
    
    public void setRoleFeatures(Set<RoleFeature> roleFeatures) {
        this.roleFeatures = roleFeatures;
    }
    
    @Override
    public String toString() {
        return "Feature{" +
                "featureId=" + featureId +
                ", featureCode='" + featureCode + '\'' +
                ", featureName='" + featureName + '\'' +
                ", featurePath='" + featurePath + '\'' +
                '}';
    }
}