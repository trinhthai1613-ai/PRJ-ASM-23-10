package com.lms.dao;

import com.lms.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class RoleDAO extends BaseDAO<Role> {
    
    public RoleDAO() {
        super(Role.class);
    }
    
    /**
     * Tìm role theo code
     */
    public Role findByCode(String roleCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.roleCode = :roleCode AND r.isActive = true", 
                Role.class
            );
            query.setParameter("roleCode", roleCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Kiểm tra role code đã tồn tại chưa
     */
    public boolean existsByCode(String roleCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Role r WHERE r.roleCode = :roleCode", 
                Long.class
            );
            query.setParameter("roleCode", roleCode);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}