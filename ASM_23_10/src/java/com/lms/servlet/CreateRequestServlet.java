package com.lms.servlet;

import com.lms.dao.LeaveReasonDAO;
import com.lms.entity.LeaveReason;
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
import java.time.LocalDate;
import java.util.List;

@WebServlet("/request/create")
public class CreateRequestServlet extends HttpServlet {
    
    private LeaveRequestService leaveRequestService;
    private LeaveReasonDAO reasonDAO;
    
    @Override
    public void init() throws ServletException {
        this.leaveRequestService = new LeaveRequestService();
        this.reasonDAO = new LeaveReasonDAO();
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
        
        // Lấy danh sách lý do nghỉ phép
        List<LeaveReason> reasons = reasonDAO.findAllActive();
        
        // Build form HTML
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Tạo Đơn Xin Nghỉ Phép</h2>");
        html.append("<p>User: ").append(user.getFullName()).append("</p>");
        html.append("<p>Số ngày phép còn lại: ").append(user.getRemainingLeaveDays()).append("</p>");
        html.append("<hr>");
        
        html.append("<form method='post' action='create'>");
        
        // Reason dropdown
        html.append("<label>Lý do nghỉ phép:</label><br>");
        html.append("<select name='reasonId' required>");
        for (LeaveReason reason : reasons) {
            html.append("<option value='").append(reason.getReasonId()).append("'>");
            html.append(reason.getReasonName()).append("</option>");
        }
        html.append("</select><br><br>");
        
        // From date
        html.append("<label>Từ ngày:</label><br>");
        html.append("<input type='date' name='fromDate' required><br><br>");
        
        // To date
        html.append("<label>Đến ngày:</label><br>");
        html.append("<input type='date' name='toDate' required><br><br>");
        
        // Custom reason
        html.append("<label>Lý do chi tiết (tùy chọn):</label><br>");
        html.append("<textarea name='customReason' rows='4' cols='50'></textarea><br><br>");
        
        html.append("<input type='submit' value='Gửi đơn'>");
        html.append("</form>");
        
        html.append("<br><a href='").append(request.getContextPath()).append("/dashboard'>Quay lại Dashboard</a>");
        html.append("</body></html>");
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(html.toString());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        try {
            // Parse parameters
            Integer reasonId = Integer.parseInt(request.getParameter("reasonId"));
            LocalDate fromDate = LocalDate.parse(request.getParameter("fromDate"));
            LocalDate toDate = LocalDate.parse(request.getParameter("toDate"));
            String customReason = request.getParameter("customReason");
            
            // Create request
            LeaveRequest leaveRequest = leaveRequestService.createLeaveRequest(
                user.getUserId(), 
                reasonId, 
                fromDate, 
                toDate, 
                customReason
            );
            
            // Success
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(
                "<html><body>" +
                "<h2>Tạo đơn thành công!</h2>" +
                "<p>Mã đơn: " + leaveRequest.getRequestCode() + "</p>" +
                "<p>Từ ngày: " + leaveRequest.getFromDate() + "</p>" +
                "<p>Đến ngày: " + leaveRequest.getToDate() + "</p>" +
                "<p>Số ngày: " + leaveRequest.getTotalDays() + "</p>" +
                "<p>Trạng thái: " + leaveRequest.getStatus().getStatusName() + "</p>" +
                "<br><a href='" + request.getContextPath() + "/request/list'>Xem danh sách đơn</a>" +
                "<br><a href='" + request.getContextPath() + "/dashboard'>Quay lại Dashboard</a>" +
                "</body></html>"
            );
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Invalid input format - " + e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}