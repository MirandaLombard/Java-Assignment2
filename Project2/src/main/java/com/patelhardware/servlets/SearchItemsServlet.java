/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.servlets;
import com.patelhardware.model.Items;
import com.patelhardware.model.User;  
import com.patelhardware.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author miran
 */
public class SearchItemsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchItemsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        // 1. Check if user is logged in
        if (session == null || session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            LOGGER.info("Attempted to access Search Items without valid session. Redirecting to login.");
            response.sendRedirect("login.jsp?sessionExpired=true");
            return;
        }

        String searchTerm = request.getParameter("searchTerm"); 

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Items> searchResults = new ArrayList<>();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            try {
                conn = DBConnection.getConnection();
                // SQL query to search for items by name or description (case-insensitive)
                String sql = "SELECT item_id, name, color, description, price, available " +
                             "FROM items WHERE (name LIKE ? OR description LIKE ?) AND available = TRUE";
                
                pstmt = conn.prepareStatement(sql);
                // Use '%' for wildcard matching in LIKE queries
                pstmt.setString(1, "%" + searchTerm.trim() + "%"); 
                pstmt.setString(2, "%" + searchTerm.trim() + "%"); 
                
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Items item = new Items(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getBoolean("available")
                    );
                    searchResults.add(item);
                }
                
                LOGGER.log(Level.INFO, "Search for '{0}' returned {1} results.", 
                        new Object[]{searchTerm, searchResults.size()});

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Database error while searching for items with term: " + searchTerm, ex);
                request.setAttribute("errorMessage", "Error during search. Please try again later.");
            } finally {
                // Close resources
                try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing ResultSet", e); }
                try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e); }
                DBConnection.closeConnection(conn);
            }
        } else {
            // If no search term is provided, display all items or redirect to viewAllItems
            LOGGER.info("No search term provided for searchItems.jsp.");
            request.setAttribute("message", "Enter a search term to find items.");
        }

        request.setAttribute("searchResults", searchResults); 
        request.getRequestDispatcher("searchItems.jsp").forward(request, response);
    }

    /*Handles the HTTP POST method.*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}