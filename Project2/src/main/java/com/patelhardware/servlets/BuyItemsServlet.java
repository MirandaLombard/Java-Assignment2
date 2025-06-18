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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author miran
 */
public class BuyItemsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(BuyItemsServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Check if user is logged in
        if (session == null || session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            LOGGER.info("Attempted to buy item without valid session. Redirecting to login.");
            response.sendRedirect("login.jsp?sessionExpired=true");
            return;
        }

        User currentUser = (User) session.getAttribute("currentUser");
        int userId = currentUser.getUserId();

        // Get item ID and quantity from the request
        String itemIdStr = request.getParameter("itemId");
        String quantityStr = request.getParameter("quantity"); 

        int itemId;
        int quantity = 1; 

        try {
            itemId = Integer.parseInt(itemIdStr);
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    quantity = 1; 
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid item ID or quantity format: " + itemIdStr + ", " + quantityStr, e);
            request.setAttribute("errorMessage", "Invalid item ID or quantity provided.");
            request.getRequestDispatcher("viewAllItems.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            // 2. Validate if the item exists and is available
            String checkItemSql = "SELECT name, available FROM items WHERE item_id = ?";
            pstmt = conn.prepareStatement(checkItemSql);
            pstmt.setInt(1, itemId);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                request.setAttribute("errorMessage", "Item not found.");
                LOGGER.log(Level.WARNING, "Attempted to buy non-existent item ID: {0}", itemId);
                conn.rollback(); // Rollback transaction
                request.getRequestDispatcher("viewAllItems.jsp").forward(request, response);
                return;
            }

            boolean itemAvailable = rs.getBoolean("available");
            String itemName = rs.getString("name");
            if (!itemAvailable) {
                request.setAttribute("errorMessage", itemName + " is currently out of stock.");
                LOGGER.log(Level.INFO, "Attempted to buy out-of-stock item: {0}", itemName);
                conn.rollback(); 
                request.getRequestDispatcher("viewAllItems.jsp").forward(request, response);
                return;
            }

            // Close current statement and result set
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();

            // 3. Check if the item is already in the user's cart
            String checkCartSql = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND item_id = ?";
            pstmt = conn.prepareStatement(checkCartSql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, itemId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Item already in cart, update quantity
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + quantity;
                int cartId = rs.getInt("cart_id");

                String updateCartSql = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
                pstmt = conn.prepareStatement(updateCartSql);
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, cartId);
                pstmt.executeUpdate();
                
                request.setAttribute("successMessage", "Added " + quantity + " more of " + itemName + " to your cart. Total: " + newQuantity);
                LOGGER.log(Level.INFO, "User {0} updated cart for item {1}. New quantity: {2}", 
                        new Object[]{userId, itemId, newQuantity});

            } else {
                // Item not in cart, insert new entry
                String insertCartSql = "INSERT INTO cart (user_id, item_id, quantity) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(insertCartSql);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, itemId);
                pstmt.setInt(3, quantity);
                pstmt.executeUpdate();

                request.setAttribute("successMessage", "Added " + quantity + " of " + itemName + " to your cart.");
                LOGGER.log(Level.INFO, "User {0} added item {1} to cart with quantity: {2}", 
                        new Object[]{userId, itemId, quantity});
            }

            conn.commit(); 

            // Redirect to the view cart page or back to view all items
            response.sendRedirect("view-cart?action=itemAdded"); 

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during adding item to cart for user: " + userId + " and item: " + itemId, ex);
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction.", rollbackEx);
            }
            request.setAttribute("errorMessage", "A database error occurred while adding item to cart. Please try again later.");
            request.getRequestDispatcher("viewAllItems.jsp").forward(request, response); 
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing ResultSet", e); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e); }
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Error setting auto commit back to true.", e); }
            DBConnection.closeConnection(conn);
        }
    }

    /* Handles the HTTP GET method.*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If someone directly accesses /buy-item via GET, redirect them to view all items
        response.sendRedirect("view-all-items?error=directAccess");
    }
}
