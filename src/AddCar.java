import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCar extends JFrame {
    public AddCar(String username) {
        setTitle("Add Vehicle");
        setSize(1790, 930);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Set background color for content pane
        getContentPane().setBackground(new Color(238, 238, 238));
        setLayout(new BorderLayout());

        // Top navigation bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(34, 40, 49));
        navBar.setPreferredSize(new Dimension(getWidth(), 80));

        // Left side of the nav bar (User icon, name)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        JLabel userIconLabel = new JLabel(new ImageIcon("resources/yellow 50px/User.png"));
        userIconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        leftPanel.add(userIconLabel);

        String formattedUsername = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        JLabel userInfoLabel = new JLabel("<html>Hello, <b>" + formattedUsername + "</b></html>");
        userInfoLabel.setFont(new Font("San Serif", Font.PLAIN, 24));
        userInfoLabel.setForeground(Color.white);
        leftPanel.add(userInfoLabel);

        // Right side of the nav bar (Logout button)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        JButton logoutButton = new JButton(" Home");
        logoutButton.setFocusPainted(false);
        logoutButton.setIcon(new ImageIcon("resources/yellow 50px/Home.png"));
        logoutButton.setSize(100, 80);
        logoutButton.setFont(new Font("Poppins", Font.BOLD, 14));
        logoutButton.setForeground(new Color(238, 238, 238));
        logoutButton.setBackground(new Color(34, 40, 49));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        logoutButton.addActionListener(e -> {
            String role = DatabaseHelper.getUserRole(username);
            dispose();
            if ("Admin".equalsIgnoreCase(role)) {
                new HomePageAdmin(username);
            } else if ("User".equalsIgnoreCase(role)) {
                new HomePageUser(username);
            } else {
                CustomOptionPane.showErrorDialog(this, "Role not found! Please contact support.");
                new LoginPage();
            }
        });
        rightPanel.add(logoutButton);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(238, 238, 238));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(34, 40, 49));
        titlePanel.setPreferredSize(new Dimension(getWidth(), 100));

        JLabel titleLabel = new JLabel("Add a Vehicle");
        titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 211, 105));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Mid Panel (Divided into two panels)
        JPanel midPanel = new JPanel(null);
        midPanel.setBackground(new Color(245, 245, 245));
        midPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        midPanel.setBounds(300, 170, 1200, 550);

        // Left Section (for labels & text fields)
        JPanel leftSection = new JPanel(null);
        leftSection.setBackground(new Color(238, 238, 238));
        leftSection.setBounds(20, 20, 570, 510);
        leftSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Adding Heading "Car Details"
        JLabel heading = new JLabel("Vehicle Details");
        heading.setFont(new Font("Netflix Sans", Font.BOLD, 28));
        heading.setForeground(new Color(57, 62, 70));
        heading.setBounds(200, 35, 200, 40);
        leftSection.add(heading);

        // Adding labels & text fields inside leftSection
        int labelX = 60, labelY = 120, labelWidth = 120, labelHeight = 27;
        int fieldX = 180, fieldWidth = 300, fieldHeight = 30;
        int gap = 40;

        JLabel l1 = new JLabel("License No.");
        l1.setBounds(labelX, labelY, labelWidth, labelHeight);
        l1.setFont(new Font("Poppins", Font.BOLD, 17));
        l1.setForeground(new Color(57, 62, 70));

        leftSection.add(l1);

        JTextField t1 = new UppercaseTextField(10);
        t1.setBounds(fieldX, labelY, fieldWidth, fieldHeight);
        t1.setFont(new Font("Poppins", Font.BOLD, 15));
        t1.setBackground(Color.WHITE);
        t1.setForeground(new Color(84, 86, 95));
        t1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection.add(t1);

        JLabel l2 = new JLabel("Brand");
        l2.setBounds(labelX, labelY + (labelHeight + gap), labelWidth, labelHeight);
        l2.setFont(new Font("Poppins", Font.BOLD, 17));
        l2.setForeground(new Color(57, 62, 70));

        leftSection.add(l2);

        JTextField t2 = new JTextField();
        t2.setBounds(fieldX, labelY + (labelHeight + gap), fieldWidth, fieldHeight);
        t2.setFont(new Font("Poppins", Font.BOLD, 15));
        t2.setBackground(Color.WHITE);
        t2.setForeground(new Color(84, 86, 95));
        t2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection.add(t2);

        JLabel l3 = new JLabel("Model");
        l3.setBounds(labelX, labelY + 2 * (labelHeight + gap), labelWidth, labelHeight);
        l3.setFont(new Font("Poppins", Font.BOLD, 17));
        l3.setForeground(new Color(57, 62, 70));

        leftSection.add(l3);

        JTextField t3 = new JTextField();
        t3.setBounds(fieldX, labelY + 2 * (labelHeight + gap), fieldWidth, fieldHeight);
        t3.setFont(new Font("Poppins", Font.BOLD, 15));
        t3.setBackground(Color.WHITE);
        t3.setForeground(new Color(84, 86, 95));
        t3.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection.add(t3);

        JLabel l4 = new JLabel("Type");
        l4.setBounds(labelX, labelY + 3 * (labelHeight + gap), labelWidth, labelHeight);
        l4.setFont(new Font("Poppins", Font.BOLD, 17));
        l4.setForeground(new Color(57, 62, 70));

        leftSection.add(l4);

        String[] carTypes = {"Sedan", "SUV", "Hatchback", "Convertible","Pickup","Minivan","Sports","Motorcycle"};
        JComboBox<String> cb1 = new JComboBox<>(carTypes);
        cb1.setBounds(fieldX, labelY + 3 * (labelHeight + gap), fieldWidth, fieldHeight);
        cb1.setFont(new Font("Poppins", Font.BOLD, 15));
        cb1.setBackground(new Color(245,245,245));
        cb1.setForeground(new Color(84, 86, 95));
        cb1.setFocusable(false);
        cb1.setUI(new CustomComboBoxUI()); // Apply custom UI
        cb1.setRenderer(new CustomComboBoxRenderer());
        cb1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        leftSection.add(cb1);

        JLabel l5 = new JLabel("Cost");
        l5.setBounds(labelX, labelY + 4 * (labelHeight + gap), labelWidth, labelHeight);
        l5.setFont(new Font("Poppins", Font.BOLD, 17));
        l5.setForeground(new Color(57, 62, 70));

        leftSection.add(l5);

        JTextField t5 = new IntegerTextField(5);
        t5.setBounds(fieldX, labelY + 4 * (labelHeight + gap), fieldWidth, fieldHeight);
        t5.setFont(new Font("Poppins", Font.BOLD, 15));
        t5.setBackground(Color.WHITE);
        t5.setForeground(new Color(84, 86, 95));
        t5.setText("0"); // Set default value when the page loads
        t5.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection.add(t5);

        midPanel.add(leftSection);

        // Right Section (for rectangles - to be modified later)
        JPanel rightSection = new JPanel(null);
        rightSection.setBackground(new Color(238, 238, 238));
        rightSection.setBounds(600, 20, 580, 510);
        rightSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Adding Heading "Vehicle Type"
        JLabel rightHeading = new JLabel("Vehicle Types");
        rightHeading.setFont(new Font("Netflix Sans", Font.BOLD, 28));
        rightHeading.setForeground(new Color(57, 62, 70));
        rightHeading.setBounds(190, 35, 300, 40);
        rightSection.add(rightHeading);

        // Vehicle types and their icons
        String[] vehicleTypes = {"Sedan", "SUV", "Hatchback", "Convertible", "Pickup", "Minivan", "Sports", "Motorcycle"};
        String[] iconPaths = {
                "resources/Car Class/Sedanblk.png", "resources/Car Class/SUVblk.png", "resources/Car Class/Hatchbackblk.png",
                "resources/Car Class/Convertible.png",
                "resources/Car Class/Pickupblk.png",
                "resources/Car Class/Station Wagonblk.png",
                "resources/Car Class/sports.png",
                "resources/Car Class/motorcycle.png"
        };

        // Grid properties
        int startX = 20, startY = 135, boxWidth = 120, boxHeight = 120;
        int colGap = 20, rowGap = 20;
        int cols = 4;

        // Creating 2x5 grid manually using setBounds
        for (int i = 0; i < vehicleTypes.length; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + col * (boxWidth + colGap);
            int y = startY + row * (boxHeight + rowGap);

            // Box panel
            JPanel box = new JPanel(null);
            box.setBounds(x, y, boxWidth, boxHeight);
            box.setBackground(Color.WHITE);
            box.setBorder(BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true));

            // Icon
            JLabel iconLabel = new JLabel(new ImageIcon(iconPaths[i]));
            iconLabel.setBounds(30, 10, 60, 80); // Centering icon
            box.add(iconLabel);

            // Label
            JLabel textLabel = new JLabel(vehicleTypes[i], SwingConstants.CENTER);
            textLabel.setFont(new Font("Poppins", Font.BOLD, 14));
            textLabel.setForeground(new Color(34,40,42));
            textLabel.setBounds(10, 80, 100, 20); // Centering label
            textLabel.setHorizontalAlignment(SwingConstants.CENTER);
            box.add(textLabel);

            // Add to right section
            rightSection.add(box);
        }


        midPanel.add(rightSection);


        // Center panel
        JPanel centerPanel = new JPanel(new CardLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(53, 300, 50, 300));

        centerPanel.add(midPanel);

        mainPanel.add(midPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        bottomPanel.setMaximumSize(new Dimension(1000, 300));
        bottomPanel.setBackground(new Color(238, 238, 238));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 400, 20, 400));

        JButton addCar = new JButton("Add the Vehicle");
        addCar.setFont(new Font("Poppins", Font.BOLD, 18));
        addCar.setForeground(new Color(57, 62, 70));
        addCar.setBackground(new Color(255, 211, 105));
        addCar.setFocusPainted(false);
        addCar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        addCar.addActionListener(e -> addVehicleToDatabase(t1, t2, t3, cb1, t5));

        bottomPanel.add(addCar);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addVehicleToDatabase(JTextField t1, JTextField t2, JTextField t3, JComboBox<String> cb1, JTextField t5) {
        String licenseNo = t1.getText().trim();
        String brand = t2.getText().trim();
        String model = t3.getText().trim();
        String category = (String) cb1.getSelectedItem();
        String cost = t5.getText().trim() + ".00";  // Append .00
        String status = "Available";

        // Validation - Check if all fields are filled
        if (licenseNo.isEmpty() || brand.isEmpty() || model.isEmpty() || cost.isEmpty()) {
            CustomOptionPane.showErrorDialog(this, "All fields are required!");
            return;
        }

        // Database Connection
        try (Connection conn = DatabaseHelper.getConnection()) {
            // Check if the license number already exists
            String checkQuery = "SELECT COUNT(*) FROM cars WHERE no_plate = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, licenseNo);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    CustomOptionPane.showErrorDialog(this, "License number already exists!");
                    return;
                }
            }

            // Insert into the database
            String insertQuery = "INSERT INTO cars (no_plate, brand, model, category, cost, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, licenseNo);
                stmt.setString(2, brand);
                stmt.setString(3, model);
                stmt.setString(4, category);
                stmt.setString(5, cost);
                stmt.setString(6, status);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    CustomOptionPane.showDoneDialog(this, "Vehicle added successfully!", "Success");
                    clearFields(t1, t2, t3, cb1, t5);  // Clear fields after successful insertion
                } else {
                    CustomOptionPane.showErrorDialog(this, "Failed to add vehicle!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomOptionPane.showErrorDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields(JTextField t1, JTextField t2, JTextField t3, JComboBox<String> cb1, JTextField t5) {
        t1.setText("");
        t2.setText("");
        t3.setText("");
        cb1.setSelectedIndex(0);
        t5.setText("0");

    }

}
