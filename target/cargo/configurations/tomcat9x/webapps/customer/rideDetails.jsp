<html>

<head>
    <title>Ride Details</title>
</head>

<body>
    <h1>Ride Details</h1>

    <div>
        <p>Pickup Location: <span id="pickup-location"></span></p>
        <p>Drop Location: <span id="drop-location"></span></p>
        <p>Estimated Distance: <span id="estimated-distance"></span></p>
        <p>Estimated Fare: <span id="estimated-fare"></span></p>
        <p>Status: <span id="status"></span></p>
    </div>

    <script>
        const CONTEXT_PATH = '<%= request.getContextPath() %>';

        const params = new URLSearchParams(window.location.search);
        const rideId = params.get("id");

        if (!rideId) {
            alert("Invalid ride");
        } else {
            fetch(CONTEXT_PATH + "/api/rides/" + rideId, {
                method: "GET",
                credentials: "include"
            })
                .then(res => res.json())
                .then(result => {
                    console.log(result);

                    if (!result.success || !result.data) {
                        alert("Ride not found");
                        return;
                    }

                    const rideRequest = result.data;

                    document.getElementById("pickup-location").textContent =
                        rideRequest.pickup;

                    document.getElementById("drop-location").textContent =
                        rideRequest.drop;

                    document.getElementById("estimated-distance").textContent =
                        rideRequest.estimatedDistance.toFixed(2) + " km";

                    document.getElementById("estimated-fare").textContent =
                        "₹ " + rideRequest.estimatedFare.toFixed(2);

                    document.getElementById("status").textContent =
                        rideRequest.status;
                })
                .catch(err => {
                    console.error(err);
                    alert("Failed to load ride details");
                });
        }
    </script>
</body>

</html>