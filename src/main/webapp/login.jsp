<html>

<body>
    <h2>Login Page</h2>
    <form id="login-form">

        <div id="errorMessage" class="error-message"></div>
        <div id="successMessage" class="success-message"></div>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Login"> <br><br>

        <a href="signup.jsp">Don't have an account? Sign Up</a>
    </form>

    <script>
        const CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/js/auth.js"></script>
    <script>
        // Redirect if already logged in
        Auth.redirectIfLoggedIn();

        document.getElementById('login-form').addEventListener('submit', async (e) => {
            e.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            const errorDiv = document.getElementById('errorMessage');
            const successDiv = document.getElementById('successMessage');

            if (!email.trim() || !password.trim()) {
                errorDiv.textContent = "Username and password are required!";
                return;
            }

            try {
                const response = await fetch('${pageContext.request.contextPath}/api/auth/login', {
                    method: 'POST',
                    headers: { 'Content-type': 'application/json' },
                    credentials: 'include',
                    body: JSON.stringify({ email, password })
                })

                const result = await response.json();

                if (result.success && result.data) {
                    sessionStorage.setItem('user', JSON.stringify(result.data));
                    successDiv.textContent = "Login successful! Redirecting ...";

                    // Use Auth utility helpers
                    setTimeout(() => {
                        Auth.redirectToDashboard(result.data);
                    }, 1000)
                }
            } catch (err) {
                console.log(err);
                errorDiv.textContent = "An error occurred. Please try again.";
            }
        })
    </script>
</body>

</html>