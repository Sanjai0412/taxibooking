CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE drivers (
    driver_id INT PRIMARY KEY,   
    vehicle_type VARCHAR(50) NOT NULL,    
    vehicle_number VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_driver_user
        FOREIGN KEY (driver_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE TABLE ride_requests (
    id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    pickup_location_id INT NOT NULL,
    pickup_label TEXT NOT NULL,

    drop_location_id INT NOT NULL,
    drop_label TEXT NOT NULL,

    estimated_distance DOUBLE PRECISION NOT NULL,
    estimated_fare NUMERIC(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT fk_ride_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pickup_location
        FOREIGN KEY (pickup_location_id)
        REFERENCES locations(id),

    CONSTRAINT fk_drop_location
        FOREIGN KEY (drop_location_id)
        REFERENCES locations(id)
);


CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO locations (name, latitude, longitude) VALUES
('Chennai Central', 13.0851617, 80.275155),
('Egmore', 13.0732259, 80.2609209),
('T Nagar', 13.0417591, 80.2340761),
('Guindy', 13.0066625, 80.2206369),
('Velachery', 12.9754605, 80.2207047),
('Gandhipuram', 11.0175845, 76.9674075),
('RS Puram', 11.0101698, 76.9504373),
('Peelamedu', 11.034576, 77.0155603),
('Singanallur', 11.000658, 77.0295709),
('Saravanampatti', 11.0796991, 76.9997393),
('Pollachi', 10.6608925, 77.004759),
('Trichy Junction', 10.7947068, 78.6848465),
('Theppakulam', 9.9114761, 78.1498516),
('Srirangam', 10.8502661, 78.6997699),
('Thillai Nagar', 10.833811, 78.6568149),
('Woraiyur', 10.8307782, 78.6799037);



CREATE TABLE driver_locations (
    driver_id INT PRIMARY KEY,
    location_id INT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_driver_location
        FOREIGN KEY (driver_id)
        REFERENCES drivers(driver_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_location
        FOREIGN KEY (location_id)
        REFERENCES locations(id)
        ON DELETE CASCADE
);

