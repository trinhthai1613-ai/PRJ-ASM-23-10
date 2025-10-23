package com.lms.dao;

import com.lms.entity.LeaveRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class LeaveRequestDAO extends BaseDAO<LeaveRequest> {
    
    public LeaveRequestDAO() {
        super(LeaveRequest.class);
    }
    
    /**
     * Tìm đơn theo request code
     */
    public LeaveRequest findByRequestCode(String requestCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr WHERE lr.requestCode = :requestCode", 
                LeaveRequest.class
            );
            query.setParameter("requestCode", requestCode);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy tất cả đơn của một user
     */
    public List<LeaveRequest> findByUser(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.userId = :userId " +
                "ORDER BY lr.createdAt DESC", 
                LeaveRequest.class
            );
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn của user theo status
     */
    public List<LeaveRequest> findByUserAndStatus(Integer userId, String statusCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.userId = :userId AND lr.status.statusCode = :statusCode " +
                "ORDER BY lr.createdAt DESC", 
                LeaveRequest.class
            );
            query.setParameter("userId", userId);
            query.setParameter("statusCode", statusCode);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn của subordinates (cấp dưới)
     */
    public List<LeaveRequest> findBySubordinates(List<Integer> subordinateIds) {
        EntityManager em = getEntityManager();
        try {
            if (subordinateIds == null || subordinateIds.isEmpty()) {
                return List.of();
            }
            
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.userId IN :subordinateIds " +
                "ORDER BY lr.createdAt DESC", 
                LeaveRequest.class
            );
            query.setParameter("subordinateIds", subordinateIds);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn cần xét duyệt của manager
     */
    public List<LeaveRequest> findPendingRequestsForManager(Integer managerId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.manager.userId = :managerId " +
                "AND lr.status.statusCode = 'INPROGRESS' " +
                "ORDER BY lr.createdAt ASC", 
                LeaveRequest.class
            );
            query.setParameter("managerId", managerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn theo division (phòng ban)
     */
    public List<LeaveRequest> findByDivision(Integer divisionId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.division.divisionId = :divisionId " +
                "ORDER BY lr.createdAt DESC", 
                LeaveRequest.class
            );
            query.setParameter("divisionId", divisionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn theo division và status
     */
    public List<LeaveRequest> findByDivisionAndStatus(Integer divisionId, String statusCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.division.divisionId = :divisionId " +
                "AND lr.status.statusCode = :statusCode " +
                "ORDER BY lr.createdAt DESC", 
                LeaveRequest.class
            );
            query.setParameter("divisionId", divisionId);
            query.setParameter("statusCode", statusCode);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn trong khoảng thời gian
     */
    public List<LeaveRequest> findByDateRange(LocalDate startDate, LocalDate endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.fromDate <= :endDate AND lr.toDate >= :startDate " +
                "AND lr.status.statusCode = 'APPROVED' " +
                "ORDER BY lr.fromDate", 
                LeaveRequest.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy đơn của division trong khoảng thời gian (cho agenda)
     */
    public List<LeaveRequest> findByDivisionAndDateRange(Integer divisionId, LocalDate startDate, LocalDate endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LeaveRequest> query = em.createQuery(
                "SELECT lr FROM LeaveRequest lr " +
                "WHERE lr.user.division.divisionId = :divisionId " +
                "AND lr.fromDate <= :endDate AND lr.toDate >= :startDate " +
                "AND lr.status.statusCode IN ('APPROVED', 'INPROGRESS') " +
                "ORDER BY lr.user.fullName, lr.fromDate", 
                LeaveRequest.class
            );
            query.setParameter("divisionId", divisionId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Sinh request code tự động
     */
    public String generateRequestCode() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(lr) FROM LeaveRequest lr", 
                Long.class
            );
            long count = query.getSingleResult();
            String year = String.valueOf(LocalDate.now().getYear());
            return "LR" + year + String.format("%06d", count + 1);
        } finally {
            em.close();
        }
    }
}