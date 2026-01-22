<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <title>Your Ride</title>
        <style>
            body {
                font-family: sans-serif;
                margin: 20px;
                color: #333;
            }

            h1 {
                font-size: 24px;
                margin-bottom: 20px;
            }

            h2 {
                font-size: 18px;
                margin-top: 20px;
                margin-bottom: 10px;
                border-bottom: 1px solid #ccc;
                padding-bottom: 5px;
            }

            .info-item {
                margin-bottom: 8px;
            }

            .label {
                font-weight: bold;
            }

            #status-display {
                font-size: 1.1em;
                font-weight: bold;
                margin: 15px 0;
                padding: 10px;
                background-color: #eee;
                border-left: 5px solid #333;
            }

            #error-message {
                color: red;
                display: none;
            }
        </style>
    </head>

    <body>

        <h1>Your Ride</h1>

        <div id="error-message"></div>

        <div id="ride-content" style="display: none;">

            <div id="status-display">Loading status...</div>

            <h2>Driver</h2>
            <div class="info-item">
                <span class="label">Name:</span> <span id="driver-name"></span>
            </div>
            <div class="info-item">
                <span class="label">Vehicle:</span> <span id="vehicle-type"></span> - <span id="vehicle-number"></span>
            </div>

            <h2>Details</h2>
            <div class="info-item">
                <span class="label">Pickup:</span> <span id="pickup-location"></span>
            </div>
            <div class="info-item">
                <span class="label">Drop:</span> <span id="drop-location"></span>
            </div>
            <div class="info-item">
                <span class="label">Fare:</span> <span id="estimated-fare"></span>
            </div>

            <div id="actions" style="display:none; margin-top: 20px;">
                <a href="dashboard.jsp">Back to Dashboard</a>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/auth.js"></script>
        <script>

            Auth.checkAuth();

            const CONTEXT_PATH = '<%= request.getContextPath() %>';
            const params = new URLSearchParams(window.location.search);
            const rideRequestId = params.get("id");

            const statusDisplay = document.getElementById("status-display");
            const driverNameEl = document.getElementById("driver-name");
            const vehicleTypeEl = document.getElementById("vehicle-type");
            const vehicleNumberEl = document.getElementById("vehicle-number");
            const pickupEl = document.getElementById("pickup-location");
            const dropEl = document.getElementById("drop-location");
            const fareEl = document.getElementById("estimated-fare");
            const rideContent = document.getElementById("ride-content");
            const errorMsg = document.getElementById("error-message");
            const actionsDiv = document.getElementById("actions");

            if (!rideRequestId) {
                showError("Invalid Ride ID");
            } else {
                fetchRideDetails();
                // Poll every 5 seconds for status updates
                setInterval(fetchRideDetails, 5000);
            }

            async function fetchRideDetails() {
                try {
                    const response = await fetch(CONTEXT_PATH + "/api/rides/active/" + rideRequestId, {
                        method: "GET",
                        credentials: "include"
                    });

                    const result = await response.json();

                    if (!response.ok || !result.success) {
                        showError(result.message || "Failed to load ride details");
                        return;
                    }

                    updateUI(result.data);

                } catch (error) {
                    console.error("Error fetching ride details:", error);
                }
            }

            function updateUI(data) {
                rideContent.style.display = "block";
                errorMsg.style.display = "none";

                driverNameEl.textContent = data.driverName;
                vehicleTypeEl.textContent = data.vehicleType;
                vehicleNumberEl.textContent = data.vehicleNumber;
                pickupEl.textContent = data.pickup;
                dropEl.textContent = data.drop;
                fareEl.textContent = "₹ " + data.estimatedFare.toFixed(2);

                let statusText = data.status;
                if (data.status === "ACCEPTED") {
                    statusText = "Driver is on the way";
                    statusDisplay.style.borderLeftColor = "orange";
                } else if (data.status === "STARTED") {
                    statusText = "Ride in Progress";
                    statusDisplay.style.borderLeftColor = "green";
                } else if (data.status === "ENDED") {
                    statusText = "Ride Completed";
                    statusDisplay.style.borderLeftColor = "gray";
                    actionsDiv.style.display = "block";
                }

                statusDisplay.textContent = statusText;
            }

            function showError(msg) {
                errorMsg.textContent = msg;
                errorMsg.style.display = "block";
                rideContent.style.display = "none";
            }
        </script>

    </body>

    </html>