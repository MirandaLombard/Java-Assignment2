/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.servlets;
import com.patelhardware.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
public class MainServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MainServlet.class.getName());

    /* Handles both GET and POST requests for the main/home page.*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /* Common method to process both GET and POST requests.*/
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); 

        // Check if user is logged in
        if (session != null && session.getAttribute("loggedIn") != null && (Boolean) session.getAttribute("loggedIn")) {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser != null) {
                LOGGER.log(Level.INFO, "Accessing main page for user: {0} (Role: {1})", 
                        new Object[]{currentUser.getUsername(), currentUser.getRole()});
                request.setAttribute("user", currentUser);
                request.getRequestDispatcher("main.jsp").forward(request, response);
            } else {
                LOGGER.log(Level.WARNING, "Logged-in flag set but currentUser object is null. Redirecting to login.");
                session.invalidate(); 
                response.sendRedirect("login.jsp?sessionExpired=true");
            }
        } else {
            // User is not logged in or session expired
            LOGGER.info("Attempted to access main page without valid session. Redirecting to login.");
            response.sendRedirect("login.jsp?sessionExpired=true"); // Redirect to login page
        }
    }
}
