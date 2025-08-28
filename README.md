
# Motor Rentals Java Swing Application

## 📌 Overview

Motor Rentals is a desktop application built with **Java Swing** for managing vehicle rentals, bookings, inventory, and customer details. The app connects to a MySQL database to persist booking and inventory data. It features multiple user roles (Admin/User) with role-based navigation, intuitive UI panels, and real-time search and editing capabilities.

## ⚙️ Features

🔑 Login & Registration with role-based access (Admin / User)

🖥️ Modern UI with custom styled buttons, text fields, and dialogs

🚙 Car Management: Add, Update, Delete, Book Cars

📊 Database Integration with MySQL/SQLite

📂 Error Handling (e.g., duplicate usernames, missing data)

🔒 Logout Handling (closes related windows when logging out)


## 🛠️ Technologies Used
- Java 17+
- Java Swing for Desktop GUI
- MySQL Database
- JDBC for database connectivity

## 📑Prerequisites Tools

- Java Development Kit (JDK) 17 or above
- IntelliJ IDEA Community Edition
- MySQL Server installed and running
- MySQL Connector/J JDBC driver
- A MySQL database configured for Motor Rentals (schema and tables to be created)

## Screenshots

Here are the UI ScreenShots: [Preview](https://excalidraw.com/#json=akcQIkqto2wxRwOQtq4jn,LuT8eWs6k1P0gKs5ATIlLQ)

## 🚀Setup and Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/motor-rentals.git
   cd motor-rentals
   ```
2. **Import the project in your IDE (IntelliJ IDEA).**

3. **Add MySQL Connector/J library to the project dependencies.**

4. **Configure your database connection parameters in `DatabaseHelper` class.**

5. **Before running the application, Create the required database schema and tables:**
   
## 📂Prerequisite Database Setup

Create the following tables in your MySQL database named `motorrentals`:

### 1. cars (Vehicle Inventory)

```
CREATE TABLE cars (
    no_plate  VARCHAR(20) NOT NULL,
    brand     VARCHAR(50) NOT NULL,
    model     VARCHAR(50) NOT NULL,
    category  ENUM('Hatchback','SUV','Sedan','Minivan','Pickup','Motorcycle','Convertible','Sports') NOT NULL,
    cost      DECIMAL(10,2) NOT NULL,
    status    ENUM('Available','Booked') DEFAULT 'Available',
    PRIMARY KEY (no_plate)
);
```

### 2. user (User Account Data)

```
CREATE TABLE user (
    id        INT          NOT NULL AUTO_INCREMENT,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    phone_no  VARCHAR(15),
    role      ENUM('Admin','User') DEFAULT 'User',
    PRIMARY KEY (id)
);
```

### 3. bills (Current Active Bookings)

```
CREATE TABLE bills (
    bill_no      INT            NOT NULL AUTO_INCREMENT,
    cus_name     VARCHAR(100)   NOT NULL,
    phone_no     BIGINT         NOT NULL,
    booking_date VARCHAR(8)     NOT NULL,
    no_plate     VARCHAR(20),
    brand_model  VARCHAR(100),
    days_booked  INT            NOT NULL,
    total_rent   INT            NOT NULL,
    PRIMARY KEY (bill_no),
    KEY no_plate (no_plate)
);
```

### 4. bookings (Booking History / Archived Bills)

```
CREATE TABLE bookings (
    bill_no      INT            NOT NULL,
    cus_name     VARCHAR(100),
    phone_no     BIGINT,
    booking_date VARCHAR(8),
    no_plate     VARCHAR(10),
    brand_model  VARCHAR(100),
    days_booked  INT,
    total_rent   INT,
    archived_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bill_no)
);
```
### Sample Data Entries

#### Insert vehicle to inventory:

```
INSERT INTO cars (no_plate, brand, model, category, cost, status) VALUES
('MH12AB1234', 'Toyota', 'Camry', 'Sedan', 2500.00, 'Available');
```

#### Insert a user:

```
INSERT INTO user (username, password, phone_no, role) VALUES
('rudra', 'Pass123', '9876543210', 'User');
```

#### Insert current booking (bill):

```
INSERT INTO bills (cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent) VALUES
('rudra', 9876543210, '20250825', 'MH12AB1234', 'Toyota Camry', 5, 12500);
```

#### Insert booking history record:

```
INSERT INTO bookings (bill_no, cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent, archived_at) VALUES
(1, 'rudra', 9876543210, '20250810', 'MH12AB1234', 'Toyota Camry', 3, 7500, NOW());
```

### Database Event Jobs

You can automate cleanup and car status updates with database events.

#### Archive and delete expired bills every day

```
CREATE EVENT archive_and_delete_expired_bills
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    INSERT INTO bookings (bill_no, cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent, archived_at)
    SELECT bill_no, cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent, NOW()
    FROM bills
    WHERE booking_date < DATE_FORMAT(NOW(), '%Y%m%d');

    DELETE FROM bills WHERE booking_date < DATE_FORMAT(NOW(), '%Y%m%d');
END;
```

#### Update car statuses and clean expired bills every day

```
CREATE EVENT update_cars_and_delete_expired_bills
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    UPDATE cars
    SET status = 'Available'
    WHERE no_plate IN (
        SELECT no_plate FROM bills WHERE booking_date < DATE_FORMAT(NOW(), '%Y%m%d')
    );

    -- If needed, delete expired bills as well
    -- DELETE FROM bills WHERE booking_date < DATE_FORMAT(NOW(), '%Y%m%d');
END;
```

---

6. Build and run the application from your IDE or command line:
    ```bash
    java -jar MotorRentals.jar
    ```

***

## Usage

- Login with your credentials.
- Navigate between Inventory, Bookings, Customer Details panels using bottom navigation buttons.
- Use search bars to filter records in tables.
- Edit `days_booked` directly; changes auto-save and update total rent.
- Delete or edit bookings/customers using action buttons.
- Logs and confirmations help ensure safe data operations.

***

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your improvements or bug fixes.



## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

***

## Contact

Created by [roxton75](https://github.com/roxton75) - feel free to reach out for questions or collaboration.


## 📌Note:
- Make sure to user Intellije IDEA as an IDE to run this project.
- Before running complete the database Prerequisites.
- The Ui is custome made and also uses set bound in some modules please make sure if you are using in a small screen to lower the frame size according to your convinence.
