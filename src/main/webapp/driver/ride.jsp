<html>

<head>
    <title>Ride</title>
</head>

<body>
    <h1>Ride</h1>
    <p>Welcome to the ride page!</p>
    <button id="startRideBtn" onclick="startRide()">Start Ride</button> <br> <br>
    <button style="visibility: hidden;" id="endRideBtn" onclick="endRide()">End Ride</button>

    <script src="${pageContext.request.contextPath}/js/auth.js"></script>
    <script>
        const CONTEXT_PATH = '<%= request.getContextPath() %>';

        Auth.checkAuth('driver');

        async function startRide() {
            const rideRequestId = sessionStorage.getItem('rideRequestId');
            try {
                const response = await fetch(
                    CONTEXT_PATH + "/api/rides/start/" + rideRequestId,
                    {
                        method: "POST",
                        credentials: "include",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            rideRequestId: rideRequestId
                        })
                    }
                );

                if (!response.ok) {
                    throw new Error("Failed to start ride");
                }

                const result = await response.json();
                alert(result.message);
                document.getElementById('startRideBtn').style.visibility = 'hidden';
                document.getElementById('endRideBtn').style.visibility = 'visible';
            } catch (err) {
                console.error(err);
            }
        }

        async function endRide() {
            const rideRequestId = sessionStorage.getItem('rideRequestId');
            try {
                const response = await fetch(
                    CONTEXT_PATH + "/api/rides/end/" + rideRequestId,
                    {
                        method: "POST",
                        credentials: "include",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            rideRequestId: rideRequestId
                        })
                    }
                );

                if (!response.ok) {
                    throw new Error("Failed to end ride");
                }

                const result = await response.json();
                alert(result.message);

                document.getElementById('endRideBtn').style.visibility = 'hidden';
                sessionStorage.removeItem('rideRequestId');
                sessionStorage.removeItem('rideId');
                const user = sessionStorage.getItem('user');
                Auth.redirectToDashboard(JSON.parse(user));

            } catch (err) {
                console.error(err);
            }
        }
    </script>
</body>

</html>