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

CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE driver_locations (
    driver_id INT PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_driver_location
        FOREIGN KEY (driver_id)
        REFERENCES drivers(driver_id)
        ON DELETE CASCADE
);

CREATE TABLE ride_requests (
    id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    pickup_location_id INT NOT NULL,
    pickup_label TEXT NOT NULL,
    pickup_lat DOUBLE PRECISION NOT NULL,
    pickup_lng DOUBLE PRECISION NOT NULL,

    drop_location_id INT NOT NULL,
    drop_label TEXT NOT NULL,
    drop_lat DOUBLE PRECISION NOT NULL,
    drop_lng DOUBLE PRECISION NOT NULL,

    estimated_distance DOUBLE PRECISION NOT NULL,
    estimated_fare NUMERIC(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ride_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_pickup_location
        FOREIGN KEY (pickup_location_id)
        REFERENCES locations(id),
        ON DELETE CASCADE,

    CONSTRAINT fk_drop_location
        FOREIGN KEY (drop_location_id)
        REFERENCES locations(id)
        ON DELETE CASCADE
);


CREATE TABLE rides (
    id SERIAL PRIMARY KEY,

    ride_request_id INT UNIQUE NOT NULL,
    driver_id INT NOT NULL,

    start_time TIMESTAMP,
    end_time TIMESTAMP,

    final_distance DOUBLE PRECISION,
    final_fare NUMERIC(10,2),

    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_ride_request
        FOREIGN KEY (ride_request_id)
        REFERENCES ride_requests(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ride_driver
        FOREIGN KEY (driver_id)
        REFERENCES drivers(driver_id)
        ON DELETE CASCADE
);

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    ride_id INT NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    paid_at TIMESTAMP,

    CONSTRAINT fk_payment_ride
        FOREIGN KEY (ride_id)
        REFERENCES rides(id)
        ON DELETE CASCADE
);
