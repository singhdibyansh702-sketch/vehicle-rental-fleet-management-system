package com.vehiclerental.config;

import com.vehiclerental.model.*;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Seed data already initialized
        }

        System.out.println("Initializing seed data for Vehicle Rental System...");

        // 1. Create Users
        User admin = new User(
                "Fleet Admin",
                "admin@vehiclerental.com",
                passwordEncoder.encode("admin123"),
                "9876543210",
                "Fleet Operations HQ, MG Road, Bengaluru",
                "DL-01ADMIN2020",
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        User rahul = new User(
                "Rahul Sharma",
                "rahul@gmail.com",
                passwordEncoder.encode("customer123"),
                "9812345678",
                "Flat 402, Green Glen Layout, Bellandur, Bengaluru",
                "KA-0420190012345",
                Role.ROLE_CUSTOMER
        );
        userRepository.save(rahul);

        User priya = new User(
                "Priya Patel",
                "priya@gmail.com",
                passwordEncoder.encode("customer123"),
                "9823456789",
                "12/A, Koramangala 4th Block, Bengaluru",
                "KA-0720210054321",
                Role.ROLE_CUSTOMER
        );
        userRepository.save(priya);

        User amit = new User(
                "Amit Verma",
                "amit@gmail.com",
                passwordEncoder.encode("customer123"),
                "9834567890",
                "45, Indiranagar 100ft Road, Bengaluru",
                "KA-1420200098765",
                Role.ROLE_CUSTOMER
        );
        userRepository.save(amit);

        // 2. Create 10 Realistic Vehicles
        Vehicle fortuner = new Vehicle(
                "KA-01-MJ-2024", "Toyota", "Fortuner 4x4", VehicleType.SUV,
                FuelType.DIESEL, Transmission.AUTOMATIC, 7, 3800.0,
                VehicleStatus.RENTED,
                "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
                "Premium 7-seater rugged SUV with 4x4 capability, plush leather seats, and high highway stability.",
                2023, "14 km/l"
        );
        vehicleRepository.save(fortuner);

        Vehicle creta = new Vehicle(
                "KA-03-AB-4501", "Hyundai", "Creta SX(O)", VehicleType.SUV,
                FuelType.PETROL, Transmission.AUTOMATIC, 5, 2400.0,
                VehicleStatus.BOOKED,
                "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80",
                "Modern compact SUV featuring panoramic sunroof, ventilated seats, and smooth CVT transmission.",
                2023, "16 km/l"
        );
        vehicleRepository.save(creta);

        Vehicle city = new Vehicle(
                "KA-05-CD-8822", "Honda", "City ZX", VehicleType.SEDAN,
                FuelType.PETROL, Transmission.MANUAL, 5, 2000.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1542282088-72c9c27ed0cd?auto=format&fit=crop&w=800&q=80",
                "Iconic executive sedan offering legendary i-VTEC performance, spacious rear legroom, and refined comfort.",
                2022, "17 km/l"
        );
        vehicleRepository.save(city);

        Vehicle nexonEv = new Vehicle(
                "KA-01-EE-3344", "Tata", "Nexon.ev Max", VehicleType.SUV,
                FuelType.ELECTRIC, Transmission.AUTOMATIC, 5, 2600.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=800&q=80",
                "Eco-friendly electric SUV with 400+ km real-world range, instant torque, and 5-star safety rating.",
                2024, "453 km/charge"
        );
        vehicleRepository.save(nexonEv);

        Vehicle swift = new Vehicle(
                "KA-04-GH-9911", "Maruti Suzuki", "Swift ZXi", VehicleType.HATCHBACK,
                FuelType.PETROL, Transmission.MANUAL, 5, 1400.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80",
                "Agile city hatchback, highly fuel-efficient, easy to park, and perfect for daily urban commuting.",
                2023, "22 km/l"
        );
        vehicleRepository.save(swift);

        Vehicle i20 = new Vehicle(
                "KA-02-KL-7755", "Hyundai", "i20 Asta", VehicleType.HATCHBACK,
                FuelType.PETROL, Transmission.AUTOMATIC, 5, 1600.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80",
                "Premium hatchback equipped with Bose audio, digital cockpit, and smooth driving dynamics.",
                2023, "19 km/l"
        );
        vehicleRepository.save(i20);

        Vehicle bmw3 = new Vehicle(
                "KA-01-ZZ-0007", "BMW", "3 Series Gran Limousine", VehicleType.LUXURY,
                FuelType.PETROL, Transmission.AUTOMATIC, 5, 5500.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80",
                "Ultimate luxury sedan combining sheer driving pleasure, extended wheelbase luxury, and Harman Kardon audio.",
                2023, "13 km/l"
        );
        vehicleRepository.save(bmw3);

        Vehicle merc = new Vehicle(
                "KA-05-MM-1234", "Mercedes-Benz", "C-Class C220d", VehicleType.LUXURY,
                FuelType.DIESEL, Transmission.AUTOMATIC, 5, 6000.0,
                VehicleStatus.MAINTENANCE,
                "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=800&q=80",
                "Flagship executive luxury sedan with modern ambient lighting, advanced driving assist, and unmatched ride quality.",
                2022, "15 km/l"
        );
        vehicleRepository.save(merc);

        Vehicle re350 = new Vehicle(
                "KA-04-RE-3500", "Royal Enfield", "Classic 350", VehicleType.BIKE,
                FuelType.PETROL, Transmission.MANUAL, 2, 900.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=800&q=80",
                "Timeless retro cruiser motorcycle featuring the signature thumping J-series engine, perfect for leisure road trips.",
                2023, "36 km/l"
        );
        vehicleRepository.save(re350);

        Vehicle duke390 = new Vehicle(
                "KA-01-KT-3900", "KTM", "Duke 390", VehicleType.BIKE,
                FuelType.PETROL, Transmission.MANUAL, 2, 1200.0,
                VehicleStatus.AVAILABLE,
                "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=800&q=80",
                "High-performance streetfighter motorcycle with cornering ABS, quickshifter, and aggressive power delivery.",
                2024, "28 km/l"
        );
        vehicleRepository.save(duke390);

        // 3. Sample Bookings
        LocalDate today = LocalDate.now();

        // Booking 1: Rahul rented the Fortuner (Active)
        Booking booking1 = new Booking(
                rahul, fortuner,
                today.minusDays(1), today.plusDays(2),
                3, 3 * 3800.0, BookingStatus.CONFIRMED
        );
        booking1.setBookingNumber("BK-1001");
        bookingRepository.save(booking1);

        // Rental 1 for Booking 1 (ACTIVE)
        Rental rental1 = new Rental(
                booking1, today.minusDays(1), today.plusDays(2),
                RentalStatus.ACTIVE, 14250.0
        );
        rental1.setRentalNumber("RNT-2001");
        rentalRepository.save(rental1);

        // Payment 1 for Booking 1
        Payment payment1 = new Payment(
                booking1, 11400.0, PaymentMethod.UPI,
                PaymentStatus.SUCCESS, "TXN-UPI-98432170",
                "UPI payment received via Google Pay"
        );
        paymentRepository.save(payment1);

        // Booking 2: Priya booked Creta (Upcoming)
        Booking booking2 = new Booking(
                priya, creta,
                today.plusDays(2), today.plusDays(4),
                2, 2 * 2400.0, BookingStatus.CONFIRMED
        );
        booking2.setBookingNumber("BK-1002");
        bookingRepository.save(booking2);

        Payment payment2 = new Payment(
                booking2, 4800.0, PaymentMethod.CARD,
                PaymentStatus.SUCCESS, "TXN-CARD-44219800",
                "Credit Card payment via Visa ending in 4022"
        );
        paymentRepository.save(payment2);

        // Booking 3: Amit booked Honda City in the past (Completed)
        Booking booking3 = new Booking(
                amit, city,
                today.minusDays(6), today.minusDays(3),
                3, 3 * 2000.0, BookingStatus.COMPLETED
        );
        booking3.setBookingNumber("BK-1003");
        bookingRepository.save(booking3);

        Rental rental3 = new Rental(
                booking3, today.minusDays(6), today.minusDays(3),
                RentalStatus.RETURNED, 25100.0
        );
        rental3.setRentalNumber("RNT-2003");
        rental3.setActualReturnDate(today.minusDays(3));
        rental3.setReturnOdometer(25650.0);
        rental3.setFuelLevelReturn("Full (100%)");
        rental3.setConditionNotes("Vehicle returned in pristine clean condition. No scratches.");
        rental3.setAdditionalCharge(0.0);
        rental3.setFinalTotalAmount(6000.0);
        rentalRepository.save(rental3);

        Payment payment3 = new Payment(
                booking3, 6000.0, PaymentMethod.CASH,
                PaymentStatus.SUCCESS, "TXN-CASH-100344",
                "Cash settled at return counter"
        );
        paymentRepository.save(payment3);

        // 4. Sample Maintenance
        Maintenance maint1 = new Maintenance(
                merc,
                "Scheduled Brake Pad & Transmission Fluid Inspection",
                today.minusDays(2),
                8500.0,
                MaintenanceStatus.IN_PROGRESS,
                "Mercedes-Benz Authorized Service Center",
                "Undergoing scheduled replacement of front ceramic brake pads and software diagnosis."
        );
        maintenanceRepository.save(maint1);

        Maintenance maint2 = new Maintenance(
                fortuner,
                "20,000 km Periodic Engine Oil & Filter Service",
                today.minusDays(20),
                4500.0,
                MaintenanceStatus.COMPLETED,
                "Toyota Lanson Motors",
                "Engine oil replaced, wheel alignment balanced, AC air filter cleaned."
        );
        maint2.setCompletionDate(today.minusDays(19));
        maintenanceRepository.save(maint2);

        System.out.println("Seed data successfully loaded with 1 Admin, 3 Customers, 10 Vehicles, and active Bookings/Rentals!");
    }
}
