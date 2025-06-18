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
public class ViewAllServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ViewAllServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            LOGGER.info("Attempted to access View All Items without valid session. Redirecting to login.");
            response.sendRedirect("login.jsp?sessionExpired=true");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Items> itemList = new ArrayList<>(); 

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT item_id, name, color, description, price, available FROM items WHERE available = TRUE"; // Only show available items
            pstmt = conn.prepareStatement(sql);
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
                itemList.add(item);
            }
            
            request.setAttribute("itemList", itemList); 
            LOGGER.log(Level.INFO, "Fetched {0} items for display.", itemList.size());
            request.getRequestDispatcher("viewAllItems.jsp").forward(request, response);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error while fetching all items.", ex);
            request.setAttribute("errorMessage", "Error retrieving items. Please try again later.");
            request.getRequestDispatcher("main.jsp").forward(request, response); 
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing ResultSet", e); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e); }
            DBConnection.closeConnection(conn); 
        }
    }

    /* Handles the HTTP POST method.*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); 
    }
}
