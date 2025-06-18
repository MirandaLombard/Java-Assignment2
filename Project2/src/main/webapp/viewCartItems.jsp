<%-- 
    Document   : viewCartItems
    Created on : Jun 13, 2025, 1:00:08 AM
    Author     : miran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%-- REMOVED: <%@taglib prefix="fn" uri="jakarta.tags.functions" %> --%>
<%-- REMOVED: <%@page import="java.text.NumberFormat" %> --%>
<%-- REMOVED: <%@page import="java.util.Locale" %> --%>
<%-- REMOVED: <%@page import="com.patelhardware.model.CartItem" %> --%>
<%-- REMOVED: <%@page import="java.util.List" %> --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patel Hardware - Your Cart</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f4f4f4; 
        }
        .header-bg {
            background-color: #2c3e50; 
        }
        .patel-hardware-title-small {
            color: #4CAF50; 
            font-weight: bold;
            font-size: 1.5rem;
        }
        .nav-link {
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }
        .nav-link:hover {
            background-color: #34495e; 
        }
        .btn-logout {
            background-color: #e74c3c; 
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }
        .btn-logout:hover {
            background-color: #c0392b;
        }
        .cart-card {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        .item-row {
            border-bottom: 1px solid #eee;
        }
        .item-row:last-child {
            border-bottom: none;
        }
        .total-price-display {
            color: #28a745; 
            font-weight: bold;
        }
        .btn-checkout {
            background-color: #007bff; 
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .btn-checkout:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body class="min-h-screen flex flex-col">
    <!-- Header/Navigation Bar (Consistent across pages) -->
    <header class="header-bg shadow-md py-4 px-6 flex flex-col md:flex-row justify-between items-center sticky top-0 z-10">
        <div class="mb-4 md:mb-0">
            <a href="main" class="patel-hardware-title-small no-underline">PATEL HARDWARE</a>
        </div>
        <nav>
            <ul class="flex flex-wrap justify-center md:justify-end space-x-4 md:space-x-6 text-lg">
                <li><a href="view-all-items" class="nav-link">View All Items</a></li>
                <li><a href="view-cart" class="nav-link">View Cart</a></li>
                <li><a href="search-items" class="nav-link">Search Items</a></li>
                <%-- Conditional link for admin functions --%>
                <c:if test="${sessionScope.currentUser.role eq 'admin'}">
                    <li><a href="#" class="nav-link">Admin Dashboard</a></li>
                </c:if>
                <li>
                    <form action="logout" method="POST" class="inline">
                        <button type="submit" class="btn-logout">Logout</button>
                    </form>
                </li>
            </ul>
        </nav>
    </header>

    <!-- Main Content Area -->
    <main class="flex-grow container mx-auto p-4 md:p-8">
        <h2 class="text-4xl font-extrabold text-gray-900 text-center mb-8">Your Shopping Cart</h2>

        <% 
            String errorMessage = (String) request.getAttribute("errorMessage");
            String successMessage = (String) request.getAttribute("successMessage"); 
            String itemAddedParam = request.getParameter("action"); 
            if ("itemAdded".equals(itemAddedParam)) {            
        %>
            <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-md mb-4 mx-auto max-w-xl" role="alert">
                Item successfully added to cart!
            </div>
        <%
            } else if (successMessage != null && !successMessage.isEmpty()) { 
        %>
            <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-md mb-4 mx-auto max-w-xl" role="alert">
                <%= successMessage %>
            </div>
        <% 
            } else if (errorMessage != null && !errorMessage.isEmpty()) { 
        %>
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-md mb-4 mx-auto max-w-xl" role="alert">
                <%= errorMessage %>
            </div>
        <% 
            } 
        %>

        <div class="cart-card p-6 md:p-8 max-w-3xl mx-auto">
            <c:choose>
                <c:when test="${not empty cartItems}">
                    <div class="space-y-4">
                        <c:forEach var="cartItem" items="${cartItems}">
                            <div class="item-row flex items-center justify-between py-3">
                                <div class="flex-1 mr-4">
                                    <p class="text-lg font-semibold text-gray-800">${cartItem.item.name}</p>
                                    <p class="text-sm text-gray-600">${cartItem.item.description} - Color: ${cartItem.item.color}</p>
                                </div>
                                <div class="w-24 text-right">
                                    <span class="text-gray-700">Qty: ${cartItem.quantity}</span>
                                </div>
                                <div class="w-32 text-right">
                                    <%-- Using the new formatted method from CartItem.java --%>
                                    <span class="text-xl font-bold text-gray-900">${cartItem.formattedTotalPrice}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="mt-8 pt-4 border-t-2 border-gray-200 flex justify-between items-center">
                        <span class="text-2xl font-bold text-gray-900">Total:</span>
                        <span class="text-3xl font-extrabold total-price-display">
                            <%-- totalCartPrice is now a formatted string from the servlet --%>
                            ${totalCartPrice}
                        </span>
                    </div>

                    <div class="mt-8 text-center">
                        <button class="btn-checkout">Proceed to Checkout</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center text-gray-600 text-xl py-10">
                        <p>Your shopping cart is currently empty.</p>
                        <p class="mt-2">Why not <a href="view-all-items" class="text-blue-600 hover:underline">start shopping</a>?</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <!-- Footer (Consistent) -->
    <footer class="bg-gray-200 text-gray-600 text-center py-4 mt-8">
        <p>&copy; 2025 Patel Hardware. All rights reserved.</p>
    </footer>
</body>
</html>