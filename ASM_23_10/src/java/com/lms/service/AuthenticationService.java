package com.lms.service;

import com.lms.dao.UserDAO;
import com.lms.entity.User;
import com.lms.util.PasswordUtil;
import java.time.LocalDateTime;

/**
 * Service xử lý authentication
 */
public class AuthenticationService {
    
    private UserDAO userDAO;
    
    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate user với username và password
     * @return User nếu thành công, null nếu thất bại
     */
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Tìm user theo username
            User user = userDAO.findByUsername(username.trim());
            
            if (user == null) {
                return null;
            }
            
            // Kiểm tra account có active không
            if (!user.getIsActive()) {
                return null;
            }
            
            // Verify password
            if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                return null;
            }
            
            // Update last login time
            user.setLastLogin(LocalDateTime.now());
            userDAO.update(user);
            
            return user;
            
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Change password
     */
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                return false;
            }
            
            // Verify old password
            if (!PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
                return false;
            }
            
            // Validate new password strength
            if (!PasswordUtil.isStrongPassword(newPassword)) {
                throw new IllegalArgumentException("Password không đủ mạnh. Cần ít nhất 8 ký tự, 1 chữ hoa, 1 chữ thường, 1 số.");
            }
            
            // Hash and save new password
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setPasswordHash(hashedPassword);
            userDAO.update(user);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reset password (admin function)
     */
    public boolean resetPassword(Integer userId, String newPassword) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                return false;
            }
            
            // Validate new password strength
            if (!PasswordUtil.isStrongPassword(newPassword)) {
                throw new IllegalArgumentException("Password không đủ mạnh");
            }
            
            // Hash and save new password
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setPasswordHash(hashedPassword);
            userDAO.update(user);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }
}