<html>

<head>
    <title>Driver Info</title>
</head>

<body>
    <h1>Driver Info</h1>
    <form id="driverForm">
        <label for="vehicleType">Vehicle Type:</label>
        <select id="vehicleType" name="vehicleType">
            <option value="car">Car</option>
            <option value="bike">Bike</option>
        </select><br><br>
        <label for="vehicleNumber">Vehicle Number:</label>
        <input type="text" id="vehicleNumber" name="vehicleNumber"><br><br>
        <input type="submit" value="Submit">
    </form>

    <script>
        const CONTEXT_PATH = '${pageContext.request.contextPath}';
        const form = document.getElementById('driverForm');
        form.addEventListener('submit', (event) => {
            event.preventDefault();
            const vehicleType = document.getElementById('vehicleType').value;
            const vehicleNumber = document.getElementById('vehicleNumber').value;
            const driver = {
                vehicleType,
                vehicleNumber
            };
            fetch(`${CONTEXT_PATH}/api/drivers/create`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(driver)
            })
                .then(response => response.json())
                .then(data => {
                    Auth.redirectToDriverDashboard();
                })
                .catch(error => {
                    console.error(error);
                });
        });
    </script>
</body>

</html>