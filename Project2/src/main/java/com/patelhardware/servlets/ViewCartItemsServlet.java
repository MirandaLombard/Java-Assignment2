/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.servlets;

import com.patelhardware.model.CartItem; 
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
import java.text.NumberFormat; 
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; 
import java.util.logging.Level;
import java.util.logging.Logger;

/* ViewCartItemsServlet retrieves and displays the items currently in the user's shopping cart*/
public class ViewCartItemsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ViewCartItemsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Check if user is logged in
        if (session == null || session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            LOGGER.info("Attempted to access View Cart Items without valid session. Redirecting to login.");
            response.sendRedirect("login.jsp?sessionExpired=true");
            return;
        }

        User currentUser = (User) session.getAttribute("currentUser");
        int userId = currentUser.getUserId();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CartItem> cartItems = new ArrayList<>();
        double totalCartPrice = 0.0; 

        try {
            conn = DBConnection.getConnection();
            // SQL query to join cart and items tables to get all necessary details
            String sql = "SELECT c.quantity, i.item_id, i.name, i.color, i.description, i.price, i.available " +
                         "FROM cart c JOIN items i ON c.item_id = i.item_id WHERE c.user_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Create an Items object for the product details
                Items item = new Items(
                    rs.getInt("item_id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("available")
                );
                // Create a CartItem object, combining Item details and quantity
                int quantity = rs.getInt("quantity");
                CartItem cartItem = new CartItem(item, quantity);
                cartItems.add(cartItem);
                
                totalCartPrice += cartItem.getTotalPrice(); 
            }
            
            // Format the totalCartPrice here in the servlet before passing to JSP
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "ZA")); // For South African Rand (R)
            currencyFormat.setMinimumFractionDigits(2);
            currencyFormat.setMaximumFractionDigits(2);
            String formattedTotalCartPrice = currencyFormat.format(totalCartPrice);

            request.setAttribute("cartItems", cartItems); 
            request.setAttribute("totalCartPrice", formattedTotalCartPrice); 
            LOGGER.log(Level.INFO, "Fetched {0} items from cart for user {1}. Total price: {2}", 
                    new Object[]{cartItems.size(), currentUser.getUsername(), formattedTotalCartPrice});
            
            request.getRequestDispatcher("viewCartItems.jsp").forward(request, response);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error while fetching cart items for user: " + userId, ex);
            request.setAttribute("errorMessage", "Error retrieving cart items. Please try again later.");
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