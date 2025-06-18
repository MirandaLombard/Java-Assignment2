<%-- 
    Document   : main
    Created on : Jun 13, 2025, 12:37:43 AM
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
    <title>Patel Hardware - Home</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f4f4f4; /* Light gray background */
        }
        .header-bg {
            background-color: #2c3e50; /* Dark blue-gray for header */
        }
        .patel-hardware-title-small {
            color: #4CAF50; /* Green for title */
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
            background-color: #34495e; /* Slightly darker on hover */
        }
        .btn-logout {
            background-color: #e74c3c; /* Red for logout */
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }
        .btn-logout:hover {
            background-color: #c0392b;
        }
        .card {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        .highlight-text {
            color: #4CAF50; /* Green highlight */
            font-weight: 600;
        }
    </style>
</head>
<body class="min-h-screen flex flex-col">
    <!-- Header/Navigation Bar -->
    <header class="header-bg shadow-md py-4 px-6 flex flex-col md:flex-row justify-between items-center sticky top-0 z-10">
        <div class="mb-4 md:mb-0">
            <a href="main" class="patel-hardware-title-small no-underline">PATEL HARDWARE</a>
        </div>
        <nav>
            <ul class="flex flex-wrap justify-center md:justify-end space-x-4 md:space-x-6 text-lg">
                <li><a href="view-all-items" class="nav-link">View All Items</a></li>
                <li><a href="view-cart" class="nav-link">View Cart</a></li>
                <li><a href="search-items" class="nav-link">Search Items</a></li>
                <%-- Conditional link for admin functions, if applicable --%>
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
        <div class="card p-6 md:p-8 text-center">
            <h2 class="text-3xl font-semibold mb-4 text-gray-800">Welcome to Patel Hardware!</h2>
            
            <%-- Display welcome message for logged-in user --%>
            <c:set var="user" value="${sessionScope.currentUser}" />
            <c:if test="${user != null}">
                <p class="text-xl text-gray-700 mb-6">Hello, <span class="highlight-text">${user.username}</span>!</p>
                <p class="text-md text-gray-600 mb-8">Your role: <span class="font-medium">${user.role}</span></p>
                
                <p class="text-gray-700 text-lg">
                    Explore our wide range of products or manage your shopping cart.
                </p>
            </c:if>
            <c:if test="${user == null}">
                 <p class="text-xl text-red-500">User information not found in session. Please log in again.</p>
                 <a href="login.jsp" class="text-blue-600 hover:underline">Go to Login</a>
            </c:if>

            <div class="mt-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div class="bg-gray-100 p-4 rounded-lg shadow-sm">
                    <h3 class="text-xl font-semibold mb-2">Browse Items</h3>
                    <p class="text-gray-600">See all products available in our store.</p>
                    <a href="view-all-items" class="mt-4 inline-block bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300">Browse Now</a>
                </div>
                <div class="bg-gray-100 p-4 rounded-lg shadow-sm">
                    <h3 class="text-xl font-semibold mb-2">Your Cart</h3>
                    <p class="text-gray-600">Review items in your shopping cart.</p>
                    <a href="view-cart" class="mt-4 inline-block bg-green-500 text-white py-2 px-4 rounded-lg hover:bg-green-600 transition duration-300">View Cart</a>
                </div>
                <div class="bg-gray-100 p-4 rounded-lg shadow-sm">
                    <h3 class="text-xl font-semibold mb-2">Search Products</h3>
                    <p class="text-gray-600">Find specific items quickly.</p>
                    <a href="search-items" class="mt-4 inline-block bg-purple-500 text-white py-2 px-4 rounded-lg hover:bg-purple-600 transition duration-300">Search Now</a>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer (Optional) -->
    <footer class="bg-gray-200 text-gray-600 text-center py-4 mt-8">
        <p>&copy; 2025 Patel Hardware. All rights reserved.</p>
    </footer>
</body>
</html>
