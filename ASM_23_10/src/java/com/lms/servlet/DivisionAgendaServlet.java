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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebServlet("/division/agenda")
public class DivisionAgendaServlet extends HttpServlet {
    
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
        
        // Parse date range parameters (default: current month)
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        LocalDate startDate;
        LocalDate endDate;
        
        if (startDateStr != null && endDateStr != null) {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } else {
            // Default to current month
            LocalDate today = LocalDate.now();
            startDate = today.withDayOfMonth(1);
            endDate = today.withDayOfMonth(today.lengthOfMonth());
        }
        
        // Lấy agenda
        List<LeaveRequest> requests = leaveRequestService.getDivisionAgenda(
            user.getDivision().getDivisionId(), 
            startDate, 
            endDate
        );
        
        // Build HTML
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Agenda Phòng Ban - ").append(user.getDivision().getDivisionName()).append("</h2>");
        html.append("<p>Division Leader: ").append(user.getFullName()).append("</p>");
        html.append("<hr>");
        
        // Date range selector
        html.append("<form method='get' action='agenda'>");
        html.append("<label>Từ ngày:</label> ");
        html.append("<input type='date' name='startDate' value='").append(startDate).append("'> ");
        html.append("<label>Đến ngày:</label> ");
        html.append("<input type='date' name='endDate' value='").append(endDate).append("'> ");
        html.append("<input type='submit' value='Xem'>");
        html.append("</form>");
        html.append("<br>");
        
        if (requests.isEmpty()) {
            html.append("<p>Không có đơn nào trong khoảng thời gian này.</p>");
        } else {
            // Group requests by user
            Map<String, List<LeaveRequest>> requestsByUser = new HashMap<>();
            for (LeaveRequest req : requests) {
                String userName = req.getUser().getFullName();
                requestsByUser.computeIfAbsent(userName, k -> new ArrayList<>()).add(req);
            }
            
            // Generate calendar view
            html.append("<table border='1' cellpadding='5' cellspacing='0'>");
            html.append("<tr>");
            html.append("<th>Nhân viên</th>");
            
            // Header: all dates in range
            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                html.append("<th>").append(current.toString()).append("</th>");
                current = current.plusDays(1);
            }
            html.append("</tr>");
            
            // Row for each user
            for (Map.Entry<String, List<LeaveRequest>> entry : requestsByUser.entrySet()) {
                html.append("<tr>");
                html.append("<td>").append(entry.getKey()).append("</td>");
                
                // For each date, check if user has leave
                current = startDate;
                while (!current.isAfter(endDate)) {
                    boolean hasLeave = false;
                    
                    for (LeaveRequest req : entry.getValue()) {
                        if (!current.isBefore(req.getFromDate()) && !current.isAfter(req.getToDate())) {
                            hasLeave = true;
                            break;
                        }
                    }
                    
                    if (hasLeave) {
                        html.append("<td style='background-color: #ffcccc;'>Nghỉ</td>");
                    } else {
                        html.append("<td style='background-color: #ccffcc;'>Làm</td>");
                    }
                    
                    current = current.plusDays(1);
                }
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