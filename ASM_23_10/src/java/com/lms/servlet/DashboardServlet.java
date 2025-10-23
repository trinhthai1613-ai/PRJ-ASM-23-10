package com.lms.servlet;

import com.lms.dao.UserDAO;
import com.lms.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAO();
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
        
        // Refresh user data từ database
        user = userDAO.findById(user.getUserId());
        if (user == null || !user.getIsActive()) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
            "<html><body>" +
            "<h1>Dashboard - Welcome " + user.getFullName() + "!</h1>" +
            "<p>Employee Code: " + user.getEmployeeCode() + "</p>" +
            "<p>Email: " + user.getEmail() + "</p>" +
            "<p>Division: " + user.getDivision().getDivisionName() + "</p>" +
            "<p>Remaining Leave Days: " + user.getRemainingLeaveDays() + " / " + user.getAnnualLeaveDays() + "</p>" +
            "<hr>" +
            "<h3>Menu:</h3>" +
            "<ul>" +
            "<li><a href='request/create'>Tạo đơn xin nghỉ phép</a></li>" +
            "<li><a href='request/list'>Xem các đơn đã tạo</a></li>" +
            "<li><a href='request/subordinates'>Xem đơn của cấp dưới (Manager)</a></li>" +
            "<li><a href='request/pending'>Đơn cần xét duyệt (Manager)</a></li>" +
            "<li><a href='division/agenda'>Xem agenda phòng ban (Division Leader)</a></li>" +
            "<li><a href='logout'>Logout</a></li>" +
            "</ul>" +
            "</body></html>"
        );
    }
}