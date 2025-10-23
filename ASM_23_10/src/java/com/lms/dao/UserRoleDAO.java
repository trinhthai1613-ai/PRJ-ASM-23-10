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

public class UserRoleDAO extends BaseDAO<UserRole> {
    public UserRoleDAO() {
        super(UserRole.class);
    }
    
    public List<UserRole> findByUser(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<UserRole> query = em.createQuery(
                "SELECT ur FROM UserRole ur WHERE ur.user.userId = :userId", 
                UserRole.class
            );
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
