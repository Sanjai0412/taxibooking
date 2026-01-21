// CONTEXT_PATH should be defined in the JSP before loading this file

const Auth = {

    checkSession: async () => {
        try {
            const res = await fetch(`${CONTEXT_PATH}/api/auth/me`, {
                method: "GET",
                credentials: "include"
            });

            if (!res.ok) {
                throw new Error("Not Authenticated");
            }
            const data = await res.json();
            sessionStorage.setItem('user', JSON.stringify(data.data));
            return data.data;
        } catch (e) {
            sessionStorage.removeItem('user');
            return null;
        }
    },

    checkAuth: async function (requiredRole) {
        const user = await this.checkSession();
        console.log(user);
        if (!user) {
            window.location.href = `${CONTEXT_PATH}/login.jsp`;
            return;
        }

        if (requiredRole && user.role !== requiredRole) {
            this.redirectToDashboard(user);
        }
    },

    redirectIfLoggedIn: async function () {
        const user = await this.checkSession();
        if (user) {
            this.redirectToDashboard(user);
        }
    },

    redirectToDashboard: function (user) {
        if (user.role === 'customer') {
            window.location.href = `${CONTEXT_PATH}/customer/dashboard.jsp`;
        } else if (user.role === 'driver') {
            window.location.href = `${CONTEXT_PATH}/driver/dashboard.jsp`;
        } else {
            window.location.href = `${CONTEXT_PATH}/login.jsp`;
        }
    },

    redirectToDriverInfo: function () {
        window.location.href = `${CONTEXT_PATH}/driver/driverInfo.jsp`;
    },

    logout: async function () {
        try {
            await fetch(`${CONTEXT_PATH}/api/auth/logout`, {
                method: "POST",
                credentials: "include"
            });
        } catch (e) {
            console.error("Logout error:", e);
        } finally {
            sessionStorage.removeItem('user');
            window.location.href = `${CONTEXT_PATH}/login.jsp`;
        }
    },

    redirectToRidePage: function () {
        window.location.href = `${CONTEXT_PATH}/driver/ride.jsp`;
    },
};
