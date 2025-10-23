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

@WebServlet("/request/pending")
public class PendingRequestServlet extends HttpServlet {
    
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
        
        // Lấy danh sách đơn cần xét duyệt
        List<LeaveRequest> pendingRequests = leaveRequestService.getPendingRequestsForManager(user.getUserId());
        
        // Build HTML
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Đơn Cần Xét Duyệt</h2>");
        html.append("<p>Manager: ").append(user.getFullName()).append("</p>");
        html.append("<hr>");
        
        if (pendingRequests.isEmpty()) {
            html.append("<p>Không có đơn nào cần xét duyệt.</p>");
        } else {
            html.append("<table border='1' cellpadding='5' cellspacing='0'>");
            html.append("<tr>");
            html.append("<th>Mã đơn</th>");
            html.append("<th>Nhân viên</th>");
            html.append("<th>Từ ngày</th>");
            html.append("<th>Đến ngày</th>");
            html.append("<th>Số ngày</th>");
            html.append("<th>Lý do</th>");
            html.append("<th>Lý do chi tiết</th>");
            html.append("<th>Ngày tạo</th>");
            html.append("<th>Hành động</th>");
            html.append("</tr>");
            
            for (LeaveRequest req : pendingRequests) {
                html.append("<tr>");
                html.append("<td>").append(req.getRequestCode()).append("</td>");
                html.append("<td>").append(req.getUser().getFullName()).append("</td>");
                html.append("<td>").append(req.getFromDate()).append("</td>");
                html.append("<td>").append(req.getToDate()).append("</td>");
                html.append("<td>").append(req.getTotalDays()).append("</td>");
                html.append("<td>").append(req.getReason().getReasonName()).append("</td>");
                html.append("<td>").append(req.getCustomReason() != null ? req.getCustomReason() : "-").append("</td>");
                html.append("<td>").append(req.getCreatedAt()).append("</td>");
                html.append("<td>");
                html.append("<a href='process?id=").append(req.getRequestId()).append("'>Xét duyệt</a>");
                html.append("</td>");
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