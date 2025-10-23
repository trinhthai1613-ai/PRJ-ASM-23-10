package com.lms.servlet;

import com.lms.dao.LeaveRequestDAO;
import com.lms.entity.LeaveRequest;
import com.lms.entity.User;
import com.lms.service.LeaveRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/request/process")
public class ProcessRequestServlet extends HttpServlet {
    
    private LeaveRequestService leaveRequestService;
    private LeaveRequestDAO leaveRequestDAO;
    
    @Override
    public void init() throws ServletException {
        this.leaveRequestService = new LeaveRequestService();
        this.leaveRequestDAO = new LeaveRequestDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String requestIdStr = request.getParameter("id");
        if (requestIdStr == null) {
            response.getWriter().write("Error: Missing request ID");
            return;
        }
        
        try {
            Integer requestId = Integer.parseInt(requestIdStr);
            LeaveRequest leaveRequest = leaveRequestDAO.findById(requestId);
            
            if (leaveRequest == null) {
                response.getWriter().write("Error: Request not found");
                return;
            }
            
            // Build form
            StringBuilder html = new StringBuilder();
            html.append("<html><body>");
            html.append("<h2>Xét Duyệt Đơn Nghỉ Phép</h2>");
            html.append("<hr>");
            
            // Display request details
            html.append("<p><strong>Mã đơn:</strong> ").append(leaveRequest.getRequestCode()).append("</p>");
            html.append("<p><strong>Nhân viên:</strong> ").append(leaveRequest.getUser().getFullName()).append("</p>");
            html.append("<p><strong>Từ ngày:</strong> ").append(leaveRequest.getFromDate()).append("</p>");
            html.append("<p><strong>Đến ngày:</strong> ").append(leaveRequest.getToDate()).append("</p>");
            html.append("<p><strong>Số ngày:</strong> ").append(leaveRequest.getTotalDays()).append("</p>");
            html.append("<p><strong>Lý do:</strong> ").append(leaveRequest.getReason().getReasonName()).append("</p>");
            html.append("<p><strong>Lý do chi tiết:</strong> ").append(
                leaveRequest.getCustomReason() != null ? leaveRequest.getCustomReason() : "-"
            ).append("</p>");
            html.append("<p><strong>Trạng thái:</strong> ").append(leaveRequest.getStatus().getStatusName()).append("</p>");
            html.append("<hr>");
            
            // Process form
            html.append("<form method='post' action='process'>");
            html.append("<input type='hidden' name='requestId' value='").append(requestId).append("'>");
            
            html.append("<label>Ghi chú:</label><br>");
            html.append("<textarea name='note' rows='4' cols='50'></textarea><br><br>");
            
            html.append("<button type='submit' name='action' value='approve'>Duyệt (Approve)</button>");
            html.append(" ");
            html.append("<button type='submit' name='action' value='reject'>Từ chối (Reject)</button>");
            html.append("</form>");
            
            html.append("<br><a href='pending'>Quay lại danh sách</a>");
            html.append("</body></html>");
            
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(html.toString());
            
        } catch (NumberFormatException e) {
            response.getWriter().write("Error: Invalid request ID format");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User manager = (User) session.getAttribute("user");
        
        try {
            Integer requestId = Integer.parseInt(request.getParameter("requestId"));
            String action = request.getParameter("action");
            String note = request.getParameter("note");
            
            LeaveRequest result;
            
            if ("approve".equals(action)) {
                result = leaveRequestService.approveRequest(requestId, manager.getUserId(), note);
                
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(
                    "<html><body>" +
                    "<h2>Duyệt đơn thành công!</h2>" +
                    "<p>Mã đơn: " + result.getRequestCode() + "</p>" +
                    "<p>Trạng thái: " + result.getStatus().getStatusName() + "</p>" +
                    "<br><a href='pending'>Quay lại danh sách</a>" +
                    "<br><a href='" + request.getContextPath() + "/dashboard'>Dashboard</a>" +
                    "</body></html>"
                );
                
            } else if ("reject".equals(action)) {
                result = leaveRequestService.rejectRequest(requestId, manager.getUserId(), note);
                
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(
                    "<html><body>" +
                    "<h2>Từ chối đơn thành công!</h2>" +
                    "<p>Mã đơn: " + result.getRequestCode() + "</p>" +
                    "<p>Trạng thái: " + result.getStatus().getStatusName() + "</p>" +
                    "<br><a href='pending'>Quay lại danh sách</a>" +
                    "<br><a href='" + request.getContextPath() + "/dashboard'>Dashboard</a>" +
                    "</body></html>"
                );
            } else {
                response.getWriter().write("Error: Invalid action");
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}