<%-- 
    Document   : searchItems
    Created on : Jun 13, 2025, 2:51:05 AM
    Author     : miran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %> 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patel Hardware - Search Items</title>
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
        .search-card {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        .item-card {
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: transform 0.2s ease-in-out;
        }
        .item-card:hover {
            transform: translateY(-3px);
        }
        .btn-search {
            background-color: #28a745; 
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .btn-search:hover {
            background-color: #218838;
        }
        .btn-buy-search {
            background-color: #4CAF50;
            color: white;
            padding: 8px 16px;
            border-radius: 8px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .btn-buy-search:hover {
            background-color: #45a049;
        }
        .price-text {
            color: #28a745; 
            font-weight: bold;
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
        <h2 class="text-4xl font-extrabold text-gray-900 text-center mb-8">Search for Items</h2>

        <div class="search-card p-6 md:p-8 max-w-2xl mx-auto mb-8">
            <form action="search-items" method="GET" class="flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-4">
                <input type="text" name="searchTerm" placeholder="Enter item name or description..." 
                       value="${param.searchTerm != null ? param.searchTerm : ''}"
                       class="flex-1 shadow appearance-none border rounded-md w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       required>
                <button type="submit" class="btn-search">Search</button>
            </form>
            <%-- Display error message if present (from SearchItemsServlet) --%>
            <% 
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null && !errorMessage.isEmpty()) { 
            %>
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-md mt-4 text-left" role="alert">
                    <%= errorMessage %>
                </div>
            <% 
                } 
                String message = (String) request.getAttribute("message"); 
                if (message != null && !message.isEmpty()) {
            %>
                 <div class="bg-blue-100 border border-blue-400 text-blue-700 px-4 py-3 rounded-md mt-4 text-left" role="alert">
                    <%= message %>
                </div>
            <%
                }
            %>
        </div>

        <c:choose>
            <c:when test="${not empty searchResults}">
                <h3 class="text-2xl font-semibold text-gray-800 mb-6 text-center">Search Results for "${param.searchTerm}"</h3>
                <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                    <c:forEach var="item" items="${searchResults}">
                        <div class="item-card p-6 flex flex-col justify-between">
                            <div>
                                <h4 class="text-xl font-semibold text-gray-800 mb-2">${item.name}</h4>
                                <p class="text-sm text-gray-600 mb-1">Color: ${item.color}</p>
                                <p class="text-gray-700 text-sm mb-3">${item.description}</p>
                            </div>
                            <div>
                                <p class="text-2xl price-text mb-2">R${item.price}</p>
                                <p class="text-sm ${item.available ? 'text-green-600' : 'text-red-600'} mb-4">
                                    Status: ${item.available ? 'Available' : 'Out of Stock'}
                                </p>
                                <c:if test="${item.available}">
                                    <form action="buy-item" method="POST">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <div class="flex items-center justify-between mb-4">
                                            <label for="quantity-search-${item.itemId}" class="text-gray-700 text-sm mr-2">Qty:</label>
                                            <input type="number" id="quantity-search-${item.itemId}" name="quantity" value="1" min="1" class="w-20 px-2 py-1 border rounded-md text-center text-sm">
                                        </div>
                                        <button type="submit" class="btn-buy-search w-full">Add to Cart</button>
                                    </form>
                                </c:if>
                                <c:if test="${!item.available}">
                                    <button class="btn-buy-search w-full opacity-50 cursor-not-allowed" disabled>Out of Stock</button>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center text-gray-600 text-xl py-10">
                    <p>No items found matching your search term "${param.searchTerm != null ? param.searchTerm : ''}".</p>
                    <p>Please try a different search or <a href="view-all-items" class="text-blue-600 hover:underline">view all items</a>.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <!-- Footer (Consistent) -->
    <footer class="bg-gray-200 text-gray-600 text-center py-4 mt-8">
        <p>&copy; 2025 Patel Hardware. All rights reserved.</p>
    </footer>
</body>
</html>