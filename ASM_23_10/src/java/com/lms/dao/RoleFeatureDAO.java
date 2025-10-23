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

public class RoleFeatureDAO extends BaseDAO<RoleFeature> {
    public RoleFeatureDAO() {
        super(RoleFeature.class);
    }
    
    public List<RoleFeature> findByRole(Integer roleId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RoleFeature> query = em.createQuery(
                "SELECT rf FROM RoleFeature rf WHERE rf.role.roleId = :roleId", 
                RoleFeature.class
            );
            query.setParameter("roleId", roleId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public RoleFeature findByRoleAndFeature(Integer roleId, Integer featureId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RoleFeature> query = em.createQuery(
                "SELECT rf FROM RoleFeature rf WHERE rf.role.roleId = :roleId AND rf.feature.featureId = :featureId", 
                RoleFeature.class
            );
            query.setParameter("roleId", roleId);
            query.setParameter("featureId", featureId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
