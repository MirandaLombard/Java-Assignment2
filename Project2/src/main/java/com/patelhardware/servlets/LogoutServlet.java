/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author miran
 */
public class LogoutServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); 

        if (session != null) {
            String username = null;
            // Attempt to get username before invalidating session for logging purposes
            Object currentUserObj = session.getAttribute("currentUser");
            if (currentUserObj instanceof com.patelhardware.model.User) {
                username = ((com.patelhardware.model.User) currentUserObj).getUsername();
            }
            session.invalidate(); 
            LOGGER.log(Level.INFO, "User {0} logged out successfully.", username != null ? username : "unknown");
        } else {
            LOGGER.info("Attempted to logout, but no active session found.");
        }
        
        // Redirect to the login page
        response.sendRedirect("login.jsp?logout=true");
    }

    /*Handles the HTTP GET method.*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response); 
    }
}
