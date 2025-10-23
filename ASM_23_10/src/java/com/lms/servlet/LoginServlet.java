package com.lms.servlet;

import com.lms.entity.User;
import com.lms.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private AuthenticationService authService;
    
    @Override
    public void init() throws ServletException {
        this.authService = new AuthenticationService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Nếu đã login rồi thì redirect về home
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Hiển thị trang login (chưa có JSP nên return JSON message)
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
            "<html><body>" +
            "<h2>Login Form</h2>" +
            "<form method='post' action='login'>" +
            "Username: <input type='text' name='username' required><br><br>" +
            "Password: <input type='password' name='password' required><br><br>" +
            "<input type='submit' value='Login'>" +
            "</form>" +
            "</body></html>"
        );
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            User user = authService.authenticate(username, password);
            
            if (user != null) {
                // Tạo session và lưu user
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Redirect về dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // Login failed
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(
                    "<html><body>" +
                    "<h2>Login Failed!</h2>" +
                    "<p>Username hoặc password không đúng.</p>" +
                    "<a href='login'>Thử lại</a>" +
                    "</body></html>"
                );
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}