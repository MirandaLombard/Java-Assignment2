/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.servlets;

import com.patelhardware.model.User;
import com.patelhardware.response.RegistrationResponse; // Import RegistrationResponse
import com.patelhardware.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper; // For JSON serialization

/**
 *
 * @author miran
 */
public class RegistrationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set content type for JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper(); 

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // Default role for new registrations is 'user'
        String role = "user"; 

        RegistrationResponse regResponse = null;

        // 1. Basic validation for empty fields
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            regResponse = new RegistrationResponse(false, "Please fill out all fields.");
            out.print(objectMapper.writeValueAsString(regResponse));
            out.flush();
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            
            // 2. Check if username already exists
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                regResponse = new RegistrationResponse(false, "Username already exists. Please choose a different one.");
                out.print(objectMapper.writeValueAsString(regResponse));
                out.flush();
                return;
            }
            // Close ResultSet and PreparedStatement for checkSql
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();

            // 3. Register the new user
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                regResponse = new RegistrationResponse(true, "Registration successful! You can now log in.");
                LOGGER.log(Level.INFO, "New user registered: {0}", username);
            } else {
                regResponse = new RegistrationResponse(false, "Registration failed. Please try again.");
                LOGGER.log(Level.WARNING, "Registration failed for username: {0}", username);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during registration for user: " + username, ex);
            regResponse = new RegistrationResponse(false, "A database error occurred during registration. Please try again later.");
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing ResultSet", e); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e); }
            DBConnection.closeConnection(conn); 
        }
        
        // Send the JSON response back to the client
        out.print(objectMapper.writeValueAsString(regResponse));
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just forward to registration.jsp if someone tries to GET /register
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
