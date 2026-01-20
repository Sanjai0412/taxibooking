<html>

<head>
    <link rel="stylesheet" href="../css/locationSuggesion.css">
</head>

<body>

    <h2>Driver Dashboard</h2>
    <p>Welcome to the driver dashboard!</p>

    <h2>Set Your Current Location</h2>

    <form id="driver-location-form">
        <label>Current Location</label>
        <input type="text" id="driver-location" placeholder="Enter location name" required>
        <div id="driver-location-suggestions" class="suggestions"></div>

        <button type="submit">Go Online</button>
    </form>

    <div id="ride-requests"></div>

    <a href="javascript:void(0)">Logout</a>

    <script src="${pageContext.request.contextPath}/js/auth.js"></script>
    <script>
        const CONTEXT_PATH = '<%= request.getContextPath() %>';

        Auth.checkAuth('driver');

        const driverLocationInput = document.getElementById("driver-location");
        const driverLocationSuggestionsBox = document.getElementById("driver-location-suggestions");
        const driverLocationForm = document.getElementById("driver-location-form");
        const rideRequestsDiv = document.getElementById("ride-requests");

        let selectedDriverLocationId = null;

        // AUTOCOMPLETE
        driverLocationInput.addEventListener("input", getSuggestions);

        // SUBMIT FORM
        driverLocationForm.addEventListener("submit", handleGoOnline);

        async function handleGoOnline(e) {
            e.preventDefault();

            if (!selectedDriverLocationId) {
                alert("Please select a valid location");
                return;
            }

            try {
                // Save driver location
                const saveResponse = await fetch(
                    CONTEXT_PATH + "/api/drivers/location",
                    {
                        method: "POST",
                        credentials: "include",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            locationId: selectedDriverLocationId
                        })
                    }
                );

                if (!saveResponse.ok) {
                    alert("Failed to set driver location");
                    return;
                }

                // Fetch nearby ride requests
                await fetchRideRequests();

            } catch (err) {
                console.error(err);
            }
        }

        async function fetchRideRequests() {
            try {
                const response = await fetch(
                    CONTEXT_PATH + "/api/drivers/me/ride-requests",
                    {
                        method: "GET",
                        credentials: "include"
                    }
                );

                if (!response.ok) return;

                const result = await response.json();
                renderRideRequests(result.data);

            } catch (err) {
                console.error(err);
            }
        }

        // LOCATION SUGGESTIONS
        async function getSuggestions(e) {
            const query = e.target.value.trim();

            if (query.length < 2) {
                driverLocationSuggestionsBox.style.display = "none";
                return;
            }

            try {
                const url = CONTEXT_PATH +
                    "/api/locations/suggestions?locationName=" +
                    encodeURIComponent(query);

                const response = await fetch(url, {
                    method: "GET",
                    credentials: "include"
                });

                if (!response.ok) return;

                const result = await response.json();
                renderSuggestions(result.data);

            } catch (err) {
                console.error(err);
            }
        }

        function renderSuggestions(locations) {
            driverLocationSuggestionsBox.innerHTML = "";

            if (!locations || locations.length === 0) {
                driverLocationSuggestionsBox.style.display = "none";
                return;
            }

            locations.forEach(loc => {
                const div = document.createElement("div");
                div.className = "suggestion-item";
                div.textContent = loc.name;

                div.onclick = () => {
                    driverLocationInput.value = loc.name;
                    selectedDriverLocationId = loc.id;
                    driverLocationSuggestionsBox.style.display = "none";
                    driverLocationSuggestionsBox.style.position = "static";
                };

                driverLocationSuggestionsBox.appendChild(div);
            });

            driverLocationSuggestionsBox.style.display = "block";
        }

        // RENDER RIDES
        function renderRideRequests(rides) {
            rideRequestsDiv.innerHTML = "";

            if (!rides || rides.length === 0) {
                rideRequestsDiv.innerHTML = "<p>No nearby ride requests</p>";
                return;
            }

            rides.forEach(ride => {
                const div = document.createElement("div");
                div.innerHTML = `
                    <p><b>Pickup:</b> \${ride.pickup}</p>
                    <p><b>Drop:</b> \${ride.drop}</p>
                    <p><b>Distance:</b> \${(ride.estimatedDistance).toFixed(2)} km</p>
                    <p><b>Fare:</b> \${(ride.estimatedFare).toFixed(2)} Rupees</p>
                    <button type="button">Accept</button>
                    <hr>
                `;
                rideRequestsDiv.appendChild(div);
            });
        }
    </script>

</body>

</html>