-- ==============================================================================
-- Vehicle Rental & Fleet Management System
-- Seed Data SQL Script
-- Passwords:
-- admin@vehiclerental.com -> admin123 ($2a$10$w3U6Y0/uT4WvR8/2/qYtYeL7wT34N28U6gUa.hT1s7sYk6eQ4Z81u or BCrypt hash)
-- customer accounts       -> customer123
-- ==============================================================================

USE `vehiclerental_db`;

-- 1. Insert Users (BCrypt hashes for admin123 and customer123)
-- Hash for admin123: $2a$10$7qB5Vj3kE8X1sQyZ9uN2O.5FqK2W1b3G5m7p9r1t3v5x7z9A1b3C5
-- Note: Spring DataInitializer automatically hashes and loads credentials if run via app.
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `address`, `driving_license_number`, `role`, `created_at`) VALUES
(1, 'Fleet Admin', 'admin@vehiclerental.com', '$2a$10$4y9pA7mOq9w7y7O.2a9uTeT13Q6b5OqYVp8WkL5rU4i8c7q6o.qGe', '9876543210', 'Fleet Operations HQ, MG Road, Bengaluru', 'DL-01ADMIN2020', 'ROLE_ADMIN', NOW()),
(2, 'Rahul Sharma', 'rahul@gmail.com', '$2a$10$9y1wB5nOp8v6x6N.1z8tSdR02P5a4NpXUo7VjK4qT3h7b6p5n.pFd', '9812345678', 'Flat 402, Green Glen Layout, Bellandur, Bengaluru', 'KA-0420190012345', 'ROLE_CUSTOMER', NOW()),
(3, 'Priya Patel', 'priya@gmail.com', '$2a$10$9y1wB5nOp8v6x6N.1z8tSdR02P5a4NpXUo7VjK4qT3h7b6p5n.pFd', '9823456789', '12/A, Koramangala 4th Block, Bengaluru', 'KA-0720210054321', 'ROLE_CUSTOMER', NOW()),
(4, 'Amit Verma', 'amit@gmail.com', '$2a$10$9y1wB5nOp8v6x6N.1z8tSdR02P5a4NpXUo7VjK4qT3h7b6p5n.pFd', '9834567890', '45, Indiranagar 100ft Road, Bengaluru', 'KA-1420200098765', 'ROLE_CUSTOMER', NOW());

-- 2. Insert Vehicles
INSERT INTO `vehicles` (`id`, `registration_number`, `brand`, `model`, `vehicle_type`, `fuel_type`, `transmission`, `seating_capacity`, `price_per_day`, `status`, `image_url`, `description`, `manufacture_year`, `mileage`, `created_at`) VALUES
(1, 'KA-01-MJ-2024', 'Toyota', 'Fortuner 4x4', 'SUV', 'DIESEL', 'AUTOMATIC', 7, 3800.0, 'RENTED', 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80', 'Premium 7-seater rugged SUV with 4x4 capability, plush leather seats, and high highway stability.', 2023, '14 km/l', NOW()),
(2, 'KA-03-AB-4501', 'Hyundai', 'Creta SX(O)', 'SUV', 'PETROL', 'AUTOMATIC', 5, 2400.0, 'BOOKED', 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80', 'Modern compact SUV featuring panoramic sunroof, ventilated seats, and smooth CVT transmission.', 2023, '16 km/l', NOW()),
(3, 'KA-05-CD-8822', 'Honda', 'City ZX', 'SEDAN', 'PETROL', 'MANUAL', 5, 2000.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1542282088-72c9c27ed0cd?auto=format&fit=crop&w=800&q=80', 'Iconic executive sedan offering legendary i-VTEC performance, spacious rear legroom, and refined comfort.', 2022, '17 km/l', NOW()),
(4, 'KA-01-EE-3344', 'Tata', 'Nexon.ev Max', 'SUV', 'ELECTRIC', 'AUTOMATIC', 5, 2600.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=800&q=80', 'Eco-friendly electric SUV with 400+ km real-world range, instant torque, and 5-star safety rating.', 2024, '453 km/charge', NOW()),
(5, 'KA-04-GH-9911', 'Maruti Suzuki', 'Swift ZXi', 'HATCHBACK', 'PETROL', 'MANUAL', 5, 1400.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80', 'Agile city hatchback, highly fuel-efficient, easy to park, and perfect for daily urban commuting.', 2023, '22 km/l', NOW()),
(6, 'KA-02-KL-7755', 'Hyundai', 'i20 Asta', 'HATCHBACK', 'PETROL', 'AUTOMATIC', 5, 1600.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80', 'Premium hatchback equipped with Bose audio, digital cockpit, and smooth driving dynamics.', 2023, '19 km/l', NOW()),
(7, 'KA-01-ZZ-0007', 'BMW', '3 Series Gran Limousine', 'LUXURY', 'PETROL', 'AUTOMATIC', 5, 5500.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80', 'Ultimate luxury sedan combining sheer driving pleasure, extended wheelbase luxury, and Harman Kardon audio.', 2023, '13 km/l', NOW()),
(8, 'KA-05-MM-1234', 'Mercedes-Benz', 'C-Class C220d', 'LUXURY', 'DIESEL', 'AUTOMATIC', 5, 6000.0, 'MAINTENANCE', 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=800&q=80', 'Flagship executive luxury sedan with modern ambient lighting, advanced driving assist, and unmatched ride quality.', 2022, '15 km/l', NOW()),
(9, 'KA-04-RE-3500', 'Royal Enfield', 'Classic 350', 'BIKE', 'PETROL', 'MANUAL', 2, 900.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=800&q=80', 'Timeless retro cruiser motorcycle featuring the signature thumping J-series engine, perfect for leisure road trips.', 2023, '36 km/l', NOW()),
(10, 'KA-01-KT-3900', 'KTM', 'Duke 390', 'BIKE', 'PETROL', 'MANUAL', 2, 1200.0, 'AVAILABLE', 'https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=800&q=80', 'High-performance streetfighter motorcycle with cornering ABS, quickshifter, and aggressive power delivery.', 2024, '28 km/l', NOW());

-- 3. Insert Bookings
INSERT INTO `bookings` (`id`, `booking_number`, `customer_id`, `vehicle_id`, `start_date`, `end_date`, `total_days`, `total_amount`, `status`, `cancellation_reason`, `created_at`) VALUES
(1, 'BK-1001', 2, 1, CURDATE() - INTERVAL 1 DAY, CURDATE() + INTERVAL 2 DAY, 3, 11400.0, 'CONFIRMED', NULL, NOW() - INTERVAL 2 DAY),
(2, 'BK-1002', 3, 2, CURDATE() + INTERVAL 2 DAY, CURDATE() + INTERVAL 4 DAY, 2, 4800.0, 'CONFIRMED', NULL, NOW() - INTERVAL 1 DAY),
(3, 'BK-1003', 4, 3, CURDATE() - INTERVAL 6 DAY, CURDATE() - INTERVAL 3 DAY, 3, 6000.0, 'COMPLETED', NULL, NOW() - INTERVAL 7 DAY);

-- 4. Insert Rentals
INSERT INTO `rentals` (`id`, `rental_number`, `booking_id`, `pickup_date`, `expected_return_date`, `actual_return_date`, `rental_status`, `initial_odometer`, `return_odometer`, `fuel_level_return`, `condition_notes`, `additional_charge`, `additional_charge_reason`, `final_total_amount`, `created_at`) VALUES
(1, 'RNT-2001', 1, CURDATE() - INTERVAL 1 DAY, CURDATE() + INTERVAL 2 DAY, NULL, 'ACTIVE', 14250.0, NULL, NULL, NULL, 0.0, NULL, 11400.0, NOW() - INTERVAL 1 DAY),
(2, 'RNT-2003', 3, CURDATE() - INTERVAL 6 DAY, CURDATE() - INTERVAL 3 DAY, CURDATE() - INTERVAL 3 DAY, 'RETURNED', 25100.0, 25650.0, 'Full (100%)', 'Vehicle returned in pristine clean condition. No scratches.', 0.0, NULL, 6000.0, NOW() - INTERVAL 6 DAY);

-- 5. Insert Payments
INSERT INTO `payments` (`id`, `payment_number`, `booking_id`, `amount`, `payment_method`, `payment_status`, `transaction_id`, `payment_date`, `notes`) VALUES
(1, 'PAY-1001', 1, 11400.0, 'UPI', 'SUCCESS', 'TXN-UPI-98432170', NOW() - INTERVAL 2 DAY, 'UPI payment received via Google Pay'),
(2, 'PAY-1002', 2, 4800.0, 'CARD', 'SUCCESS', 'TXN-CARD-44219800', NOW() - INTERVAL 1 DAY, 'Credit Card payment via Visa ending in 4022'),
(3, 'PAY-1003', 3, 6000.0, 'CASH', 'SUCCESS', 'TXN-CASH-100344', NOW() - INTERVAL 6 DAY, 'Cash settled at return counter');

-- 6. Insert Maintenance
INSERT INTO `maintenance` (`id`, `vehicle_id`, `description`, `maintenance_date`, `completion_date`, `cost`, `status`, `service_provider`, `notes`, `created_at`) VALUES
(1, 8, 'Scheduled Brake Pad & Transmission Fluid Inspection', CURDATE() - INTERVAL 2 DAY, NULL, 8500.0, 'IN_PROGRESS', 'Mercedes-Benz Authorized Service Center', 'Undergoing scheduled replacement of front ceramic brake pads and software diagnosis.', NOW() - INTERVAL 2 DAY),
(2, 1, '20,000 km Periodic Engine Oil & Filter Service', CURDATE() - INTERVAL 20 DAY, CURDATE() - INTERVAL 19 DAY, 4500.0, 'COMPLETED', 'Toyota Lanson Motors', 'Engine oil replaced, wheel alignment balanced, AC air filter cleaned.', NOW() - INTERVAL 20 DAY);
