package com.lms.servlet;

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
import java.util.List;

@WebServlet("/request/list")
public class ListRequestServlet extends HttpServlet {
    
    private LeaveRequestService leaveRequestService;
    
    @Override
    public void init() throws ServletException {
        this.leaveRequestService = new LeaveRequestService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Lấy danh sách đơn của user
        List<LeaveRequest> requests = leaveRequestService.getUserRequests(user.getUserId());
        
        // Build HTML table
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Danh Sách Đơn Nghỉ Phép Của Tôi</h2>");
        html.append("<p>User: ").append(user.getFullName()).append("</p>");
        html.append("<hr>");
        
        if (requests.isEmpty()) {
            html.append("<p>Chưa có đơn nào.</p>");
        } else {
            html.append("<table border='1' cellpadding='5' cellspacing='0'>");
            html.append("<tr>");
            html.append("<th>Mã đơn</th>");
            html.append("<th>Từ ngày</th>");
            html.append("<th>Đến ngày</th>");
            html.append("<th>Số ngày</th>");
            html.append("<th>Lý do</th>");
            html.append("<th>Trạng thái</th>");
            html.append("<th>Người duyệt</th>");
            html.append("<th>Ngày duyệt</th>");
            html.append("<th>Ghi chú</th>");
            html.append("</tr>");
            
            for (LeaveRequest req : requests) {
                html.append("<tr>");
                html.append("<td>").append(req.getRequestCode()).append("</td>");
                html.append("<td>").append(req.getFromDate()).append("</td>");
                html.append("<td>").append(req.getToDate()).append("</td>");
                html.append("<td>").append(req.getTotalDays()).append("</td>");
                html.append("<td>").append(req.getReason().getReasonName()).append("</td>");
                html.append("<td>").append(req.getStatus().getStatusName()).append("</td>");
                html.append("<td>").append(req.getProcessedBy() != null ? req.getProcessedBy().getFullName() : "-").append("</td>");
                html.append("<td>").append(req.getProcessedAt() != null ? req.getProcessedAt().toString() : "-").append("</td>");
                html.append("<td>").append(req.getProcessNote() != null ? req.getProcessNote() : "-").append("</td>");
                html.append("</tr>");
            }
            
            html.append("</table>");
        }
        
        html.append("<br><a href='").append(request.getContextPath()).append("/dashboard'>Quay lại Dashboard</a>");
        html.append("</body></html>");
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(html.toString());
    }
}