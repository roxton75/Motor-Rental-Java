//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DatabaseHelper {
//    private static Connection connection;
//
//    // Constructor to initialize connection
//    public DatabaseHelper() throws ClassNotFoundException {
//        // Load the MySQL driver
//        Class.forName("com.mysql.cj.jdbc.Driver");
//    }
//
//    // Singleton method to get the database connection
//    public static synchronized Connection getConnection() {
//        if (connection == null || isConnectionClosed(connection)) {
//            try {
//                // Replace with your database URL, username, and password
//                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/motorrentals", "rudrx", "rudddyyy@123");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return connection;
//    }
//
//    // Check if a connection is closed
//    private static boolean isConnectionClosed(Connection conn) {
//        try {
//            return conn == null || conn.isClosed();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return true;
//        }
//    }
//
//    // Method to register a user
//    public boolean registerUser(String username, String password, String phoneNo, String role) {
//        String sql = "INSERT INTO User (username, password, phone_no, role) VALUES (?, ?, ?, ?)";
//
//        if (!role.equals("Admin")) {
//            role = "User";
//        }
//
//        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
//            pstmt.setString(1, username);
//            pstmt.setString(2, password);
//            pstmt.setString(3, phoneNo);
//            pstmt.setString(4, role);
//
//            int rowsAffected = pstmt.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLIntegrityConstraintViolationException e) {
//            CustomOptionPane.showMsgDialog(null,"Username already exists. Please choose another.","Registration Error");
//            return false;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // Method to validate login credentials and get the role
//    public static String validateLogin(String username, String password) {
//        String sql = "SELECT password, role FROM User WHERE username = ?";
//
//        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
//            pstmt.setString(1, username);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                String storedPassword = rs.getString("password");
//                if (storedPassword.equals(password)) {
//                    return rs.getString("role"); // Return the role if credentials match
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null; // Return null if login fails
//    }
//
//    public static String getUserRole(String username) {
//        String role = null;
//        String query = "SELECT role FROM user WHERE username = ?";
//
//        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
//            ps.setString(1, username);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    role = rs.getString("role");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return role;
//    }
//
//    // Method to fetch user details by username
//    public static User getUserDetails(String username) {
//        String sql = "SELECT role, phone_no, password FROM User WHERE username = ?";
//        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
//            pstmt.setString(1, username);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                return new User(
//                        username,
//                        rs.getString("role"),
//                        rs.getString("phone_no"),
//                        rs.getString("password")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // Method to update user details
//    public static boolean updateUserDetails(String username, String phoneNo, String password) {
//        String sql = "UPDATE User SET phone_no = ?, password = ? WHERE username = ?";
//
//        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
//            pstmt.setString(1, phoneNo);
//            pstmt.setString(2, password);
//            pstmt.setString(3, username);
//
//            int rowsAffected = pstmt.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    // Method to get total cars count
//    public int getTotalCarsCount() {
//        String query = "SELECT COUNT(*) AS total FROM cars";
//        try (Statement stmt = getConnection().createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            if (rs.next()) {
//                return rs.getInt("total");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    // Method to get available cars count
//    public int getAvailableCarsCount() {
//        String query = "SELECT COUNT(*) AS available FROM cars WHERE status = 'Available'";
//        try (Statement stmt = getConnection().createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            if (rs.next()) {
//                return rs.getInt("available");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    // Method to get booked cars count
//    public int getBookedCarsCount() {
//        String query = "SELECT COUNT(*) AS booked FROM cars WHERE status = 'Booked'";
//        try (Statement stmt = getConnection().createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            if (rs.next()) {
//                return rs.getInt("booked");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public List<String[]> fetchAvailableVehicles() {
//        List<String[]> vehicles = new ArrayList<>();
//
//        String query = "SELECT brand, model, category FROM cars WHERE status = 'Available'";
//        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                String brand = rs.getString("brand");
//                String model = rs.getString("model");
//                String category = rs.getString("category");
//                vehicles.add(new String[]{brand, model, category});
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return vehicles;
//    }
//
//    public List<String[]> fetchCarPricing() {
//        List<String[]> pricingData = new ArrayList<>();
//        String query = "SELECT brand, model, cost FROM cars";
//        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                String brand = rs.getString("brand");
//                String model = rs.getString("model");
//                String cost = rs.getString("cost");
//                pricingData.add(new String[]{brand, model, cost});
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return pricingData;
//    }
//
//    public List<Object[]> fetchAllCars() {
//        List<Object[]> carsData = new ArrayList<>();
//        String query = "SELECT * FROM cars";
//
//        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            int columnCount = rs.getMetaData().getColumnCount(); // Get the number of columns
//            while (rs.next()) {
//                Object[] row = new Object[columnCount];
//                for (int i = 1; i <= columnCount; i++) {
//                    row[i - 1] = rs.getObject(i); // Add each column's data to the row
//                }
//                carsData.add(row);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return carsData;
//    }
//
//    public static boolean updateCar(String carId, String brand, String model, String type, Double cost, String status) {
//        String query = "UPDATE cars SET brand = ?, model = ?, category = ?, cost = ?, status = ? WHERE no_plate = ?";
//        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
//            stmt.setString(1, brand);
//            stmt.setString(2, model);
//            stmt.setString(3, type);
//            stmt.setDouble(4, cost);
//            stmt.setString(5, status);
//            stmt.setString(6, carId);
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean deleteCar(String carId) {
//        String query = "DELETE FROM cars WHERE no_plate = ?";
//        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
//            stmt.setString(1, carId);
//            return stmt.executeUpdate() > 0; // Returns true if the row is deleted
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean updateCustomer(String customerId, String name, String email, String phone, String address) {
//        String query = "UPDATE user SET username = ?, password = ?, phone_no = ?, role = ? WHERE id = ?";
//        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, name);
//            stmt.setString(2, email);
//            stmt.setString(3, phone);
//            stmt.setString(4, address);
//            stmt.setString(5, customerId);
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean deleteCustomer(String customerId) {
//        String query = "DELETE FROM user WHERE id = ?";
//        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, customerId);
//            return stmt.executeUpdate() > 0; // Returns true if the row is deleted
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public List<Object[]> fetchAllCustomers() {
//        List<Object[]> customersData = new ArrayList<>();
//        String query = "SELECT * FROM user"; // Adjust the query based on your database schema
//
//        try (Connection conn = DatabaseHelper.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            int columnCount = rs.getMetaData().getColumnCount(); // Get the number of columns
//            while (rs.next()) {
//                Object[] row = new Object[columnCount];
//                for (int i = 1; i <= columnCount; i++) {
//                    row[i - 1] = rs.getObject(i); // Add each column's data to the row
//                }
//                customersData.add(row);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return customersData;
//    }
//
//    // Add this in DatabaseHelper.java
//    public static ResultSet getAvailableCars() {
//        ResultSet rs = null;
//        try {
//            String sql = "SELECT no_plate, brand, model, category, cost  FROM cars WHERE status = 'Available'";
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            rs = stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return rs;
//    }
//
//    public static boolean insertBill(
//            String cusName,
//            String phoneNo,
//            String bookingDate,
//            String noPlate,
//            String brandModel,
//            int daysBooked,
//            int totalRent
//    ) {
//        String sql = "INSERT INTO bills " +
//                "(cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, cusName);
//            ps.setString(2, phoneNo);
//            ps.setString(3, bookingDate);
//            ps.setString(4, noPlate);
//            ps.setString(5, brandModel);
//            ps.setInt(6, daysBooked);
//            ps.setInt(7, totalRent);
//            int rows = ps.executeUpdate();
//            return rows > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean updateCarStatus(String noPlate, String status) {
//        String sql = "UPDATE cars SET status=? WHERE no_plate=?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, status);
//            ps.setString(2, noPlate);
//            int rows = ps.executeUpdate();
//            return rows > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean archiveBill(int billNo, String cusName, String phoneNo,
//                                      String bookingDate, String noPlate, String brandModel,
//                                      int daysBooked, int totalRent) {
//        String sql = "INSERT INTO bookings (bill_no, cus_name, phone_no, booking_date, " +
//                "no_plate, brand_model, days_booked, total_rent, archived_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, billNo);
//            ps.setString(2, cusName);
//            ps.setString(3, phoneNo);
//            ps.setString(4, bookingDate);
//            ps.setString(5, noPlate);
//            ps.setString(6, brandModel);
//            ps.setInt(7, daysBooked);
//            ps.setInt(8, totalRent);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    // Close connection method
//    public static void closeConnection() {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
//// Utility class to store user details
//record User(String username, String role, String phoneNo, String password) {
//}
//
//class DatabaseHelperFactory {
//    public static DatabaseHelper createDatabaseHelper() throws ClassNotFoundException {
//        return new DatabaseHelper(); // Could be extended to return different types of helpers
//    }
//}
//
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // No shared static Connection field

    // Constructor loads driver once if needed
    public DatabaseHelper() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    // Always get a new connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/motorrentals",
                "rudrx",
                "rudddyyy@123"
        );
    }

    public boolean registerUser(String username, String password, String phoneNo, String role) {
        String sql = "INSERT INTO User (username, password, phone_no, role) VALUES (?, ?, ?, ?)";
        if (!role.equals("Admin")) {
            role = "User";
        }
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, phoneNo);
            pstmt.setString(4, role);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            CustomOptionPane.showMsgDialog(null,"Username already exists. Please choose another.","Registration Error");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String validateLogin(String username, String password) {
        String sql = "SELECT password, role FROM User WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserRole(String username) {
        String role = null;
        String query = "SELECT role FROM user WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    public static User getUserDetails(String username) {
        String sql = "SELECT role, phone_no, password FROM User WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        username,
                        rs.getString("role"),
                        rs.getString("phone_no"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateUserDetails(String username, String phoneNo, String password) {
        String sql = "UPDATE User SET phone_no = ?, password = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phoneNo);
            pstmt.setString(2, password);
            pstmt.setString(3, username);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalCarsCount() {
        String query = "SELECT COUNT(*) AS total FROM cars";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAvailableCarsCount() {
        String query = "SELECT COUNT(*) AS available FROM cars WHERE status = 'Available'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBookedCarsCount() {
        String query = "SELECT COUNT(*) AS booked FROM cars WHERE status = 'Booked'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("booked");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String[]> fetchAvailableVehicles() {
        List<String[]> vehicles = new ArrayList<>();
        String query = "SELECT brand, model, category FROM cars WHERE status = 'Available'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String category = rs.getString("category");
                vehicles.add(new String[]{brand, model, category});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<String[]> fetchCarPricing() {
        List<String[]> pricingData = new ArrayList<>();
        String query = "SELECT brand, model, cost FROM cars";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String cost = rs.getString("cost");
                pricingData.add(new String[]{brand, model, cost});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pricingData;
    }

    public List<Object[]> fetchAllCars() {
        List<Object[]> carsData = new ArrayList<>();
        String query = "SELECT * FROM cars";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                carsData.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carsData;
    }

    public static boolean updateCar(String carId, String brand, String model, String type, Double cost, String status) {
        String query = "UPDATE cars SET brand = ?, model = ?, category = ?, cost = ?, status = ? WHERE no_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, brand);
            stmt.setString(2, model);
            stmt.setString(3, type);
            stmt.setDouble(4, cost);
            stmt.setString(5, status);
            stmt.setString(6, carId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCar(String carId) {
        String query = "DELETE FROM cars WHERE no_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(String customerId, String name, String email, String phone, String address) {
        String query = "UPDATE user SET username = ?, password = ?, phone_no = ?, role = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setString(5, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String customerId) {
        String query = "DELETE FROM user WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> fetchAllCustomers() {
        List<Object[]> customersData = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                customersData.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersData;
    }

    public static boolean updateCarStatus(String noPlate, String status) {
        String sql = "UPDATE cars SET status=? WHERE no_plate=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, noPlate);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getAvailableCars() {
        try {
            Connection conn = getConnection();
            String sql = "SELECT no_plate, brand, model, category, cost FROM cars WHERE status = 'Available'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean insertBill(String cusName, String phoneNo, String bookingDate, String noPlate,
                                     String brandModel, int daysBooked, int totalRent) {
        String sql = "INSERT INTO bills (cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cusName);
            ps.setString(2, phoneNo);
            ps.setString(3, bookingDate);
            ps.setString(4, noPlate);
            ps.setString(5, brandModel);
            ps.setInt(6, daysBooked);
            ps.setInt(7, totalRent);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean archiveBill(int billNo, String cusName, String phoneNo,
                                      String bookingDate, String noPlate, String brandModel,
                                      int daysBooked, int totalRent) {
        String sql = "INSERT INTO bookings (bill_no, cus_name, phone_no, booking_date, " +
                "no_plate, brand_model, days_booked, total_rent, archived_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billNo);
            ps.setString(2, cusName);
            ps.setString(3, phoneNo);
            ps.setString(4, bookingDate);
            ps.setString(5, noPlate);
            ps.setString(6, brandModel);
            ps.setInt(7, daysBooked);
            ps.setInt(8, totalRent);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> fetchBookingHistory() {
        List<Object[]> historyList = new ArrayList<>();
        String query = "SELECT bill_no, cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent FROM bookings";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getObject("bill_no");
                row[1] = rs.getObject("cus_name");
                row[2] = rs.getObject("phone_no");
                row[3] = rs.getObject("booking_date");
                row[4] = rs.getObject("no_plate");
                row[5] = rs.getObject("brand_model");
                row[6] = rs.getObject("days_booked");
                row[7] = rs.getObject("total_rent");

                historyList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions suitably
        }
        return historyList;
    }

    public List<Object[]> fetchUserCurrentBookings(String username) {
        List<Object[]> records = new ArrayList<>();
        String query = "SELECT bill_no, cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent FROM bills WHERE cus_name = ?";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getObject("bill_no");
                row[1] = rs.getObject("cus_name");
                row[2] = rs.getObject("phone_no");
                row[3] = rs.getObject("booking_date");
                row[4] = rs.getObject("no_plate");
                row[5] = rs.getObject("brand_model");
                row[6] = rs.getObject("days_booked");
                row[7] = rs.getObject("total_rent");
                records.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<Object[]> fetchUserPrevBookings(String username) {
        List<Object[]> records = new ArrayList<>();
        String query = "SELECT bill_no, cus_name, phone_no, booking_date, no_plate, brand_model, days_booked, total_rent FROM bookings WHERE cus_name = ?";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getObject("bill_no");
                row[1] = rs.getObject("cus_name");
                row[2] = rs.getObject("phone_no");
                row[3] = rs.getObject("booking_date");
                row[4] = rs.getObject("no_plate");
                row[5] = rs.getObject("brand_model");
                row[6] = rs.getObject("days_booked");
                row[7] = rs.getObject("total_rent");
                records.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }


    //// Utility class to store user details
record User(String username, String role, String phoneNo, String password) {
}

class DatabaseHelperFactory {
    public static DatabaseHelper createDatabaseHelper() throws ClassNotFoundException {
        return new DatabaseHelper(); // Could be extended to return different types of helpers
    }
}

}
