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

// ===== DivisionDAO =====
class DivisionDAO extends BaseDAO<Division> {
    public DivisionDAO() {
        super(Division.class);
    }
    
    public Division findByCode(String divisionCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Division> query = em.createQuery(
                "SELECT d FROM Division d WHERE d.divisionCode = :divisionCode AND d.isActive = true", 
                Division.class
            );
            query.setParameter("divisionCode", divisionCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}

