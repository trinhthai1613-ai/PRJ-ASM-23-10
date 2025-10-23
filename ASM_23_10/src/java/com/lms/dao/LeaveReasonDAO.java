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

public class LeaveReasonDAO extends BaseDAO<LeaveReason> {
    public LeaveReasonDAO() {
        super(LeaveReason.class);
    }
    
    public LeaveReason findByCode(String reasonCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveReason> query = em.createQuery(
                "SELECT lr FROM LeaveReason lr WHERE lr.reasonCode = :reasonCode AND lr.isActive = true", 
                LeaveReason.class
            );
            query.setParameter("reasonCode", reasonCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<LeaveReason> findAllActive() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveReason> query = em.createQuery(
                "SELECT lr FROM LeaveReason lr WHERE lr.isActive = true ORDER BY lr.displayOrder", 
                LeaveReason.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
