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

public class RequestStatusDAO extends BaseDAO<RequestStatus> {
    public RequestStatusDAO() {
        super(RequestStatus.class);
    }
    
    public RequestStatus findByCode(String statusCode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RequestStatus> query = em.createQuery(
                "SELECT rs FROM RequestStatus rs WHERE rs.statusCode = :statusCode", 
                RequestStatus.class
            );
            query.setParameter("statusCode", statusCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
