<html>

</body>
<h1>Sign Up</h1>
<form id="signup-form">

    <div id="errorMessage" class="error-message"></div>
    <div id="successMessage" class="success-message"></div>

    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>

    <label for="role">Role:</label>
    <select id="role" name="role">
        <option value="customer">Customer</option>
        <option value="driver">Driver</option>
    </select><br><br>

    <input type="submit" value="Sign Up"> <br><br>

    <a href="login.jsp">Already have an account? Login</a>
</form>

<script>
    const CONTEXT_PATH = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
<script>
    // Redirect if already logged in
    Auth.redirectIfLoggedIn();

    document.getElementById('signup-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;

        const errorDiv = document.getElementById('errorMessage');
        const successDiv = document.getElementById('successMessage');

        if (!username.trim() || !email.trim() || !password.trim()) {
            errorDiv.textContent = "All fields are required!";
            return;
        }

        try {
            const response = await fetch('${pageContext.request.contextPath}/api/auth/signup', {
                method: 'POST',
                headers: { 'Content-type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ username, email, password, role })
            })

            const result = await response.json();

            if (result.success && result.data) {
                sessionStorage.setItem('user', JSON.stringify(result.data));
                successDiv.textContent = "Signup successful! Redirecting ...";

                // Use Auth utility helpers
                setTimeout(() => {
                    if (result.data.role === 'driver') {
                        Auth.redirectToDriverInfo();
                    } else {
                        Auth.redirectToDashboard(result.data);
                    }
                }, 1000)
            }
        } catch (err) {
            console.log(err);
            errorDiv.textContent = "An error occurred. Please try again.";
        }
    });

</script>
</body>

</html>