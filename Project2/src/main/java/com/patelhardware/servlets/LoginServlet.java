/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.servlets;

import com.patelhardware.model.User;
import com.patelhardware.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miran
 */
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Basic validation for empty fields
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please fill out all fields.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT user_id, username, password, role FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // User found, login successful
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );

                // Create a session for the logged-in user
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user); 
                session.setAttribute("loggedIn", true); 

                LOGGER.log(Level.INFO, "User {0} logged in successfully with role: {1}", 
                        new Object[]{username, user.getRole()});

                // Redirect based on role or to a main page
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect("main.jsp?role=admin"); 
                } else {
                    response.sendRedirect("main.jsp?role=user"); 
                }
            } else {
                // Invalid credentials
                request.setAttribute("errorMessage", "Invalid username or password.");
                LOGGER.log(Level.WARNING, "Failed login attempt for username: {0}", username);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during login for user: " + username, ex);
            request.setAttribute("errorMessage", "A database error occurred. Please try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing ResultSet", e); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e); }
            DBConnection.closeConnection(conn); 
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just forward to login.jsp if someone tries to GET /login
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
