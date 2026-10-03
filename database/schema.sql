-- ==============================================================================
-- Vehicle Rental & Fleet Management System
-- Database Schema for MySQL 8.x / MariaDB
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS `vehiclerental_db` 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `vehiclerental_db`;

-- Drop tables in reverse order of foreign key dependencies
DROP TABLE IF EXISTS `maintenance`;
DROP TABLE IF EXISTS `payments`;
DROP TABLE IF EXISTS `rentals`;
DROP TABLE IF EXISTS `bookings`;
DROP TABLE IF EXISTS `vehicles`;
DROP TABLE IF EXISTS `users`;

-- 1. Users Table
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(120) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `address` VARCHAR(255),
    `driving_license_number` VARCHAR(50),
    `role` VARCHAR(20) NOT NULL DEFAULT 'ROLE_CUSTOMER',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Vehicles Table
CREATE TABLE `vehicles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `registration_number` VARCHAR(30) NOT NULL UNIQUE,
    `brand` VARCHAR(60) NOT NULL,
    `model` VARCHAR(60) NOT NULL,
    `vehicle_type` VARCHAR(30) NOT NULL, -- SEDAN, SUV, HATCHBACK, LUXURY, BIKE
    `fuel_type` VARCHAR(30) NOT NULL,    -- PETROL, DIESEL, ELECTRIC, CNG
    `transmission` VARCHAR(30) NOT NULL, -- MANUAL, AUTOMATIC
    `seating_capacity` INT NOT NULL,
    `price_per_day` DOUBLE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, BOOKED, RENTED, MAINTENANCE
    `image_url` VARCHAR(500),
    `description` VARCHAR(1000),
    `manufacture_year` INT,
    `mileage` VARCHAR(50),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Bookings Table
CREATE TABLE `bookings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `booking_number` VARCHAR(30) NOT NULL UNIQUE,
    `customer_id` BIGINT NOT NULL,
    `vehicle_id` BIGINT NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `total_days` INT NOT NULL,
    `total_amount` DOUBLE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, CONFIRMED, CANCELLED, COMPLETED
    `cancellation_reason` VARCHAR(500),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_booking_customer` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_booking_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 4. Rentals Table
CREATE TABLE `rentals` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `rental_number` VARCHAR(30) NOT NULL UNIQUE,
    `booking_id` BIGINT NOT NULL UNIQUE,
    `pickup_date` DATE NOT NULL,
    `expected_return_date` DATE NOT NULL,
    `actual_return_date` DATE,
    `rental_status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, RETURNED, OVERDUE
    `initial_odometer` DOUBLE,
    `return_odometer` DOUBLE,
    `fuel_level_return` VARCHAR(50),
    `condition_notes` VARCHAR(500),
    `additional_charge` DOUBLE DEFAULT 0.0,
    `additional_charge_reason` VARCHAR(255),
    `final_total_amount` DOUBLE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_rental_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Payments Table
CREATE TABLE `payments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `payment_number` VARCHAR(30) NOT NULL UNIQUE,
    `booking_id` BIGINT NOT NULL,
    `amount` DOUBLE NOT NULL,
    `payment_method` VARCHAR(20) NOT NULL, -- UPI, CARD, CASH
    `payment_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, SUCCESS, FAILED
    `transaction_id` VARCHAR(100),
    `payment_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `notes` VARCHAR(255),
    CONSTRAINT `fk_payment_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Maintenance Table
CREATE TABLE `maintenance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` BIGINT NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `maintenance_date` DATE NOT NULL,
    `completion_date` DATE,
    `cost` DOUBLE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, IN_PROGRESS, COMPLETED
    `service_provider` VARCHAR(100),
    `notes` VARCHAR(500),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_maintenance_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Indexes for performance
CREATE INDEX `idx_bookings_dates` ON `bookings` (`start_date`, `end_date`);
CREATE INDEX `idx_vehicles_status` ON `vehicles` (`status`);
CREATE INDEX `idx_vehicles_type` ON `vehicles` (`vehicle_type`);
