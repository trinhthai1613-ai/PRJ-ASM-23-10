package com.lms.dao;

import com.lms.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UserDAO extends BaseDAO<User> {
    
    public UserDAO() {
        super(User.class);
    }
    
    /**
     * Tìm user theo username
     */
    public User findByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.isActive = true", 
                User.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Tìm user theo email
     */
    public User findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email AND u.isActive = true", 
                User.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Tìm user theo employee code
     */
    public User findByEmployeeCode(String employeeCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.employeeCode = :employeeCode AND u.isActive = true", 
                User.class
            );
            query.setParameter("employeeCode", employeeCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy tất cả users theo division
     */
    public List<User> findByDivision(Integer divisionId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.division.divisionId = :divisionId AND u.isActive = true ORDER BY u.fullName", 
                User.class
            );
            query.setParameter("divisionId", divisionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy tất cả subordinates (cấp dưới) của một manager
     */
    public List<User> findSubordinates(Integer managerId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.manager.userId = :managerId AND u.isActive = true ORDER BY u.fullName", 
                User.class
            );
            query.setParameter("managerId", managerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy tất cả subordinates đệ quy (bao gồm cả cấp dưới của cấp dưới)
     */
    public List<User> findAllSubordinatesRecursive(Integer managerId) {
        EntityManager em = getEntityManager();
        try {
            // Sử dụng native query với CTE để lấy tất cả subordinates
            String sql = "WITH SubordinatesCTE AS (" +
                        "    SELECT UserID, ManagerID, FullName, EmployeeCode, Email " +
                        "    FROM Users " +
                        "    WHERE ManagerID = :managerId AND IsActive = 1 " +
                        "    UNION ALL " +
                        "    SELECT u.UserID, u.ManagerID, u.FullName, u.EmployeeCode, u.Email " +
                        "    FROM Users u " +
                        "    INNER JOIN SubordinatesCTE s ON u.ManagerID = s.UserID " +
                        "    WHERE u.IsActive = 1" +
                        ") " +
                        "SELECT UserID FROM SubordinatesCTE";
            
            @SuppressWarnings("unchecked")
            List<Integer> userIds = em.createNativeQuery(sql)
                .setParameter("managerId", managerId)
                .getResultList();
            
            if (userIds.isEmpty()) {
                return List.of();
            }
            
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.userId IN :userIds ORDER BY u.fullName", 
                User.class
            );
            query.setParameter("userIds", userIds);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean existsByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", 
                Long.class
            );
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean existsByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", 
                Long.class
            );
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}