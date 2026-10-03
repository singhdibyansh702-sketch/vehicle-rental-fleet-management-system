# ApexDrive — Vehicle Rental and Fleet Management System

A full-stack, enterprise-grade monolithic **Vehicle Rental & Fleet Management System** developed using **Spring Boot 3.5.16**, **Java 25 LTS**, **Spring Data JPA (Hibernate 6)**, **Spring Security 6**, **JJWT (JSON Web Tokens)**, and a **modern, responsive web frontend**.

---

## 🚀 Key Features

### 1. Public & Customer Experience
- **Interactive Fleet Catalog**: Filter vehicles by category (SUV, Sedan, Hatchback, Luxury, Motorcycle), fuel type (Petrol, Diesel, Electric, CNG), transmission (Automatic, Manual), price range, and real-time availability.
- **Conflict-Free Reservation System**: Live availability checker prevents double-booking across overlapping calendar dates.
- **Simulated Payment Gateway**: Supports simulated checkout via UPI (Google Pay / PhonePe), Credit/Debit Card, or Counter Cash.
- **Customer Self-Service Dashboard**:
  - View all past, confirmed, and active bookings.
  - Cancel pending/confirmed bookings with instant fleet release.
  - Printable official Booking Invoices & Rental Agreements.
  - Telematics overview for vehicles currently in customer possession.
  - Transaction history logs and driver profile management.

### 2. Fleet Manager & Admin Command Console
- **Executive KPI Dashboard**: Total fleet count, active rentals, vehicles under maintenance, available units, and total revenue tally.
- **Fleet Inventory CRUD**: Add, edit, remove vehicles, and toggle availability statuses (`AVAILABLE`, `BOOKED`, `RENTED`, `MAINTENANCE`).
- **Booking Management**: Approve pending reservations or cancel conflicting requests.
- **Vehicle Dispatch & Returns**:
  - Dispatch confirmed bookings into active rentals with initial odometer tracking.
  - Vehicle return inspection: log return odometer, return fuel level, condition notes, and calculate extra late/damage surcharges.
- **Fleet Maintenance Tracker**: Schedule service tasks (brake overhaul, engine oil service), log costs and service providers, and automatically restore vehicle status to `AVAILABLE` upon completion.
- **Customer Directory**: View registered customer profiles and driving license numbers.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Backend** | Spring Boot 3.5.16, Java 25 LTS, Spring MVC REST APIs |
| **Security** | Spring Security 6, JJWT (`io.jsonwebtoken 0.11.5`), BCrypt Password Encoding |
| **ORM & DB** | Spring Data JPA, Hibernate 6, MySQL 8.x / MariaDB, H2 In-Memory DB (Dev profile) |
| **Frontend** | Vanilla ES6+ JavaScript, Custom Modern CSS3 Design System, Bootstrap Icons |
| **Build & Test** | Maven 3.9+, JUnit 5, Spring Boot Test |

---

## 🔑 Demo User Accounts

The system automatically initializes realistic seed data upon startup:

| Role | Email Address | Password | Description |
| :--- | :--- | :--- | :--- |
| **Fleet Administrator** | `admin@vehiclerental.com` | `admin123` | Full access to Admin Console, Fleet CRUD, Dispatch, Returns & Maintenance |
| **Customer 1** | `rahul@gmail.com` | `customer123` | Active customer with an active rental (Toyota Fortuner) |
| **Customer 2** | `priya@gmail.com` | `customer123` | Customer with an upcoming booking (Hyundai Creta) |
| **Customer 3** | `amit@gmail.com` | `customer123` | Customer with completed trip history (Honda City) |

---

## ⚙️ Running the Project

### Prerequisites
- **Java 25 JDK** (`java -version`)
- **Maven 3.8+** or bundled Maven Wrapper (`./mvnw.cmd` on Windows)

### 1. Running with Default `dev` Profile (H2 In-Memory Database)
Zero setup required! Starts immediately with seed vehicles, bookings, and users:

```bash
mvn spring-boot:run
```
*Note: If your local port 8080 is in use by another application (e.g. MiniTool ShadowMaker), specify port 8081:*
```bash
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Access the application in your browser:
- **Web App**: [http://localhost:8080](http://localhost:8080) (or `http://localhost:8081`)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:vehiclerental_db`
  - Username: `sa`
  - Password: *(leave blank)*

### 2. Running with Local MySQL Database
1. Ensure MySQL server is running on `localhost:3306`.
2. Configure credentials in `src/main/resources/application-mysql.properties`.
3. Optionally run `database/schema.sql` and `database/data.sql`.
4. Run with the `mysql` profile:
```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

---

## 🧪 Running Automated Tests

Run the full integration test suite:
```bash
mvn test
```

---

## 📂 Project Directory Structure

```text
Vehicle Rental/
├── database/
│   ├── schema.sql             # MySQL DDL table schemas & foreign keys
│   └── data.sql               # Seed data for MySQL
├── src/
│   ├── main/
│   │   ├── java/com/vehiclerental/
│   │   │   ├── config/        # SecurityConfig, JwtAuthFilter, DataInitializer
│   │   │   ├── controller/    # Auth, Vehicle, Booking, Rental, Payment, Admin APIs
│   │   │   ├── dto/           # Request & Response Data Transfer Objects
│   │   │   ├── exception/     # GlobalExceptionHandler & custom exceptions
│   │   │   ├── model/         # User, Vehicle, Booking, Rental, Payment, Maintenance
│   │   │   ├── repository/    # JPA Repositories with custom overlap query methods
│   │   │   └── service/       # Business logic implementations
│   │   └── resources/
│   │       ├── application.properties        # Main config (port, JWT secret)
│   │       ├── application-dev.properties    # H2 Zero-setup demo profile
│   │       ├── application-mysql.properties  # MySQL database profile
│   │       └── static/        # Modern Frontend Web Application
│   │           ├── index.html                # Public Landing Page
│   │           ├── vehicles.html             # Fleet Catalog with Live Filters
│   │           ├── vehicle-details.html      # Vehicle Specs & Instant Booking
│   │           ├── login.html                # Split-screen Login with Demo Chips
│   │           ├── register.html             # Customer Registration Page
│   │           ├── about.html                # About Us & Tech Architecture
│   │           ├── contact.html              # 24/7 Roadside Assistance & FAQs
│   │           ├── customer/dashboard.html   # Customer Portal & My Bookings
│   │           ├── admin/dashboard.html      # Fleet Management Operations Console
│   │           ├── css/style.css             # Unified CSS Design System
│   │           └── js/api.js                 # JWT Auth State & Client API Service
│   └── test/java/com/vehiclerental/
│       └── VehicleRentalApplicationTests.java # Automated integration tests
└── pom.xml                    # Maven build dependencies
```
