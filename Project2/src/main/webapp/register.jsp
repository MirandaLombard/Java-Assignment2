<%-- 
    Document   : register
    Created on : Jun 12, 2025, 11:53:03 PM
    Author     : miran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patel Hardware - Register</title>
    <!-- Tailwind CSS CDN for basic styling -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #333; 
            background-image: url('https://placehold.co/1920x1080/333/333?text='); 
            background-size: cover;
            background-position: center;
        }
        .register-card {
            background-color: #f0f0f0; 
            border-radius: 15px; 
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }
        .patel-hardware-title {
            color: #4CAF50; 
            text-shadow: 2px 2px 4px rgba(0,0,0,0.5); 
            font-size: 2.5rem;
            font-weight: bold;
        }
        input[type="text"], input[type="password"] {
            border-radius: 8px; 
            border: 1px solid #ccc;
            padding: 10px 15px;
            width: 100%;
            box-sizing: border-box; 
        }
        input[type="text"]:focus, input[type="password"]:focus {
            outline: none;
            border-color: #4CAF50; 
            box-shadow: 0 0 5px rgba(76, 175, 80, 0.5);
        }
        .btn-register {
            background-color: #FF0000;
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .btn-register:hover {
            background-color: #CC0000; 
        }
    </style>
</head>
<body class="flex items-center justify-center min-h-screen">
    <div class="register-card p-8 md:p-10 w-full max-w-md text-center">
        <h1 class="patel-hardware-title mb-8">PATEL HARDWARE</h1>

        <%-- Display messages (success/error) from RegistrationResponse via JSON --%>
        <div id="responseMessage" class="mb-4"></div>

        <form id="registrationForm" class="space-y-4">
            <div class="text-left">
                <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username</label>
                <input type="text" id="username" name="username" placeholder="Enter your username" 
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       required>
            </div>
            <div class="text-left">
                <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" 
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                       required>
            </div>
            <div>
                <button type="submit" class="btn-register w-full">Register</button>
            </div>
        </form>

        <p class="mt-6 text-gray-700">Already registered? <a href="login.jsp" class="text-blue-600 hover:underline">Login here</a></p>
    </div>

   <script>
        document.getElementById('registrationForm').addEventListener('submit', async function(event) {
            event.preventDefault(); 

            const form = event.target;
            const formData = new FormData(form);
            const responseMessageDiv = document.getElementById('responseMessage');

            responseMessageDiv.innerHTML = ''; 

            try {
                const response = await fetch('register', { 
                    method: 'POST',
                    body: new URLSearchParams(formData) 
                });

                const data = await response.json(); 

                if (data.success) {
                    // Create a success message div dynamically
                    const successMessageElement = document.createElement('div');
                    successMessageElement.className = "bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-md text-left";
                    successMessageElement.textContent = data.message; 
                    responseMessageDiv.appendChild(successMessageElement); 

                    form.reset(); 
                    
                    // Redirect to login page after a short delay
                    setTimeout(() => {
                        window.location.href = 'login.jsp?registered=true';
                    }, 2000); 
                } else {
                    console.log("Response data from server:", data); 
                    
                    // Create an error message div dynamically
                    const errorMessageElement = document.createElement('div');
                    errorMessageElement.className = "bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-md text-left";
                    errorMessageElement.textContent = data.message; 
                    responseMessageDiv.appendChild(errorMessageElement); 
                }
            } catch (error) {
                console.error('Error during registration:', error);
                responseMessageDiv.innerHTML = `<div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-md text-left">An unexpected error occurred. Please try again.</div>`;
            }
        });
    </script>
</body>
</html>