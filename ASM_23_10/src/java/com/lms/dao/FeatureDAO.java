/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lms.dao;

import com.lms.entity.Division;
import com.lms.entity.Feature;
import com.lms.entity.LeaveReason;
import com.lms.entity.RequestStatus;
import com.lms.entity.UserRole;
import com.lms.entity.RoleFeature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;


class FeatureDAO extends BaseDAO<Feature> {
    public FeatureDAO() {
        super(Feature.class);
    }
    
    public Feature findByCode(String featureCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Feature> query = em.createQuery(
                "SELECT f FROM Feature f WHERE f.featureCode = :featureCode AND f.isActive = true", 
                Feature.class
            );
            query.setParameter("featureCode", featureCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public Feature findByPath(String featurePath) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Feature> query = em.createQuery(
                "SELECT f FROM Feature f WHERE f.featurePath = :featurePath AND f.isActive = true", 
                Feature.class
            );
            query.setParameter("featurePath", featurePath);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}