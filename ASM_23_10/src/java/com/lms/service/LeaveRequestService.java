package com.lms.service;

import com.lms.dao.*;
import com.lms.entity.*;
import com.lms.util.DateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service xử lý business logic cho Leave Request
 */
public class LeaveRequestService {
    
    private LeaveRequestDAO leaveRequestDAO;
    private UserDAO userDAO;
    private RequestStatusDAO statusDAO;
    private LeaveReasonDAO reasonDAO;
    
    public LeaveRequestService() {
        this.leaveRequestDAO = new LeaveRequestDAO();
        this.userDAO = new UserDAO();
        this.statusDAO = new RequestStatusDAO();
        this.reasonDAO = new LeaveReasonDAO();
    }
    
    /**
     * Tạo đơn xin nghỉ phép mới
     */
    public LeaveRequest createLeaveRequest(Integer userId, Integer reasonId, 
                                          LocalDate fromDate, LocalDate toDate, 
                                          String customReason) throws Exception {
        
        // Validate input
        if (userId == null || reasonId == null || fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Thiếu thông tin bắt buộc");
        }
        
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }
        
        // Lấy user
        User user = userDAO.findById(userId);
        if (user == null || !user.getIsActive()) {
            throw new IllegalArgumentException("User không tồn tại hoặc không active");
        }
        
        // Lấy reason
        LeaveReason reason = reasonDAO.findById(reasonId);
        if (reason == null || !reason.getIsActive()) {
            throw new IllegalArgumentException("Lý do nghỉ phép không hợp lệ");
        }
        
        // Tính số ngày nghỉ (sử dụng working days)
        BigDecimal totalDays = DateUtil.calculateWorkingDays(fromDate, toDate);
        
        // Kiểm tra số ngày phép còn lại (nếu reason yêu cầu trừ phép)
        if (reason.getDeductFromLeave()) {
            if (user.getRemainingLeaveDays().compareTo(totalDays) < 0) {
                throw new IllegalArgumentException(
                    "Số ngày phép không đủ. Còn lại: " + user.getRemainingLeaveDays() + 
                    " ngày, yêu cầu: " + totalDays + " ngày"
                );
            }
        }
        
        // Tạo đơn mới
        LeaveRequest request = new LeaveRequest();
        request.setRequestCode(leaveRequestDAO.generateRequestCode());
        request.setUser(user);
        request.setReason(reason);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        request.setTotalDays(totalDays);
        request.setCustomReason(customReason);
        
        // Set status mặc định là INPROGRESS
        RequestStatus inProgressStatus = statusDAO.findByCode("INPROGRESS");
        if (inProgressStatus == null) {
            throw new IllegalStateException("Status INPROGRESS không tồn tại trong database");
        }
        request.setStatus(inProgressStatus);
        
        // Lưu vào database
        leaveRequestDAO.create(request);
        
        return request;
    }
    
    /**
     * Approve đơn xin nghỉ phép
     */
    public LeaveRequest approveRequest(Integer requestId, Integer managerId, String note) throws Exception {
        
        LeaveRequest request = leaveRequestDAO.findById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Đơn không tồn tại");
        }
        
        // Kiểm tra status hiện tại
        if (!"INPROGRESS".equals(request.getStatus().getStatusCode())) {
            throw new IllegalStateException("Đơn đã được xử lý rồi");
        }
        
        // Kiểm tra manager có quyền approve không (phải là manager trực tiếp)
        User manager = userDAO.findById(managerId);
        if (manager == null) {
            throw new IllegalArgumentException("Manager không tồn tại");
        }
        
        if (request.getUser().getManager() == null || 
            !request.getUser().getManager().getUserId().equals(managerId)) {
            throw new IllegalArgumentException("Bạn không có quyền duyệt đơn này");
        }
        
        // Update status thành APPROVED
        RequestStatus approvedStatus = statusDAO.findByCode("APPROVED");
        if (approvedStatus == null) {
            throw new IllegalStateException("Status APPROVED không tồn tại trong database");
        }
        
        request.setStatus(approvedStatus);
        request.setProcessedBy(manager);
        request.setProcessedAt(LocalDateTime.now());
        request.setProcessNote(note);
        
        // Trừ số ngày phép của user (nếu reason yêu cầu)
        if (request.getReason().getDeductFromLeave()) {
            User user = request.getUser();
            BigDecimal newRemainingDays = user.getRemainingLeaveDays().subtract(request.getTotalDays());
            user.setRemainingLeaveDays(newRemainingDays);
            userDAO.update(user);
        }
        
        // Save request
        leaveRequestDAO.update(request);
        
        return request;
    }
    
    /**
     * Reject đơn xin nghỉ phép
     */
    public LeaveRequest rejectRequest(Integer requestId, Integer managerId, String note) throws Exception {
        
        LeaveRequest request = leaveRequestDAO.findById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Đơn không tồn tại");
        }
        
        // Kiểm tra status hiện tại
        if (!"INPROGRESS".equals(request.getStatus().getStatusCode())) {
            throw new IllegalStateException("Đơn đã được xử lý rồi");
        }
        
        // Kiểm tra manager có quyền reject không
        User manager = userDAO.findById(managerId);
        if (manager == null) {
            throw new IllegalArgumentException("Manager không tồn tại");
        }
        
        if (request.getUser().getManager() == null || 
            !request.getUser().getManager().getUserId().equals(managerId)) {
            throw new IllegalArgumentException("Bạn không có quyền từ chối đơn này");
        }
        
        // Update status thành REJECTED
        RequestStatus rejectedStatus = statusDAO.findByCode("REJECTED");
        if (rejectedStatus == null) {
            throw new IllegalStateException("Status REJECTED không tồn tại trong database");
        }
        
        request.setStatus(rejectedStatus);
        request.setProcessedBy(manager);
        request.setProcessedAt(LocalDateTime.now());
        request.setProcessNote(note);
        
        // Save request
        leaveRequestDAO.update(request);
        
        return request;
    }
    
    /**
     * Lấy các đơn của user
     */
    public List<LeaveRequest> getUserRequests(Integer userId) {
        return leaveRequestDAO.findByUser(userId);
    }
    
    /**
     * Lấy các đơn cần manager xét duyệt
     */
    public List<LeaveRequest> getPendingRequestsForManager(Integer managerId) {
        return leaveRequestDAO.findPendingRequestsForManager(managerId);
    }
    
    /**
     * Lấy các đơn của cấp dưới (bao gồm đã approve, reject)
     */
    public List<LeaveRequest> getSubordinatesRequests(Integer managerId) {
        // Lấy danh sách subordinates
        List<User> subordinates = userDAO.findSubordinates(managerId);
        List<Integer> subordinateIds = subordinates.stream()
            .map(User::getUserId)
            .toList();
        
        return leaveRequestDAO.findBySubordinates(subordinateIds);
    }
    
    /**
     * Lấy agenda của division (cho division leader)
     */
    public List<LeaveRequest> getDivisionAgenda(Integer divisionId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestDAO.findByDivisionAndDateRange(divisionId, startDate, endDate);
    }
}