<html>

<head>
    <link rel="stylesheet" href="../css/locationSuggesion.css">
</head>

<body>
    <h1>Customer Dashboard</h1>
    <p>Welcome to the customer dashboard!</p>

    <form id="ride-request-form">

        <label for="pickup">Pickup Location:</label>
        <input type="text" id="pickup-location" name="pickup-location" required><br><br>
        <div id="pickup-suggestions" class="suggestions"></div>

        <label for="dropoff">Drop Location:</label>
        <input type="text" id="drop-location" name="drop-location" required><br><br>
        <div id="drop-suggestions" class="suggestions"></div>

        <input type="submit" value="Book Ride">

    </form>
    <a href="javascript:void(0)">Logout</a>

    <script src="${pageContext.request.contextPath}/js/auth.js"></script>
    <script>
        const CONTEXT_PATH = '${pageContext.request.contextPath}';

        // Protect route
        Auth.checkAuth('customer');

        const pickupInput = document.getElementById("pickup-location");
        const dropInput = document.getElementById("drop-location");
        const pickupSuggestionsBox = document.getElementById("pickup-suggestions");
        const dropSuggestionsBox = document.getElementById("drop-suggestions");

        let selectedPickupId = null;
        let selectedDropId = null;

        // Event listeners for input fields
        pickupInput.addEventListener("input", (e) => getSuggestions(e, "pickup"));
        dropInput.addEventListener("input", (e) => getSuggestions(e, "drop"));


        // Event listener for the form submission
        const form = document.getElementById("ride-request-form").addEventListener("submit", handleRideRequest);

        // Function to handle form submission
        async function handleRideRequest(e) {

            e.preventDefault();

            if (selectedPickupId == null || selectedDropId == null
                || selectedPickupId == selectedDropId) {
                alert("Please select valid pickup and drop locations");
                return;
            }

            const response = await fetch(CONTEXT_PATH + "/api/rides/request", {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    pickupLocationId: selectedPickupId,
                    dropLocationId: selectedDropId
                })
            });

            if (!response.ok) return;

            const result = await response.json();
            console.log(result);
            const rideRequest = result.data;

            window.location.href = "../customer/rideDetails.jsp?id=" + rideRequest.id;
        }

        // Function to get location suggestions
        async function getSuggestions(e, type) {
            const query = e.target.value.trim();
            const suggestionsBox = type === "pickup"
                ? pickupSuggestionsBox
                : dropSuggestionsBox;

            if (query.length < 2) {
                suggestionsBox.style.display = "none";
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
                const locations = result.data;

                renderSuggestions(locations, suggestionsBox, type);

            } catch (err) {
                console.error(err);
            }
        }

        // Function to render location suggestions
        function renderSuggestions(locations, suggestionsBox, type) {
            suggestionsBox.innerHTML = "";

            if (!locations || locations.length === 0) {
                suggestionsBox.style.display = "none";
                return;
            }

            locations.forEach(loc => {
                const div = document.createElement("div");
                div.className = "suggestion-item";
                div.textContent = loc.name;

                div.onclick = () => {
                    if (type === "pickup") {
                        pickupInput.value = loc.name;
                        selectedPickupId = loc.id;
                    } else {
                        dropInput.value = loc.name;
                        selectedDropId = loc.id;
                    }

                    suggestionsBox.style.display = "none";
                };

                suggestionsBox.appendChild(div);
            });

            suggestionsBox.style.display = "block";
        }
    </script>
</body>

</html>