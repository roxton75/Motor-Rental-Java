import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

public class HomePageAdmin extends JFrame {
    public HomePageAdmin(String username) {
        setTitle("Home Page: Admin");
        setSize(1790, 930);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background color for content pane
        getContentPane().setBackground(new Color(238,238,238));
        setLayout(new BorderLayout());

        // Create top navigation bar panel
        JPanel navBar = new JPanel();
        navBar.setLayout(new BorderLayout());
        navBar.setBackground(new Color(34,40,49));
        navBar.setPreferredSize(new Dimension(getWidth(), 120));
        navBar.setBorder(BorderFactory.createMatteBorder(2,0,2,0,Color.BLACK));

        // Left side of the nav bar (User icon, name)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        JLabel userIconLabel = new JLabel(new ImageIcon("resources/yellow 50px/User.png"));
        userIconLabel.setBorder(BorderFactory.createEmptyBorder(26, 10, 10, 0));
        leftPanel.add(userIconLabel);

        String formattedUsername = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();

        JLabel userInfoLabel = new JLabel("<html>Hello <b>" + formattedUsername + "</b></html>");
        userInfoLabel.setFont(new Font("San Serif", Font.PLAIN, 36));
        userInfoLabel.setForeground(new Color(238,238,238));
        userInfoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        leftPanel.add(userInfoLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setIcon(new ImageIcon("resources/yellow 50px/Logout.png"));
        logoutButton.setFont(new Font("Poppins", Font.BOLD, 14));
        logoutButton.setForeground(new Color(238,238,238));
        logoutButton.setBackground(new Color(34,40,49));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(22, 10, 10, 20));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        rightPanel.add(logoutButton);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        //OLDER CP
        // Center panel for car brands
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(238,238,238));
        centerPanel.setPreferredSize(new Dimension(1000, 400)); // Dynamic height adjustment

        // Title for car brands
        JLabel carBrandTitle = new JLabel("Brands we Provide");
        carBrandTitle.setFont(new Font("Netflix Sans", Font.BOLD, 28));
        carBrandTitle.setForeground(new Color(34,40,49));
        carBrandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        carBrandTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        centerPanel.add(carBrandTitle);

        // Panel for brand logos
        JPanel logoPanel = new JPanel(new GridLayout(2, 5, 20, 20)); // Adjust rows/columns for logos
        logoPanel.setBackground(new Color(238,238,238));
        logoPanel.setMaximumSize(new Dimension(1300, 200)); // Limit size for logos
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String[] carBrands = {"BMW", "Honda", "Citroen", "Jeep", "Mitsubishi", "Skoda", "Suzuki", "Toyota", "Volkswagen", "Volvo"};
        for (String brand : carBrands) {
            JPanel brandContainer = new JPanel(new BorderLayout());
            brandContainer.setBackground(new Color(238,238,238));

            JLabel logoLabel = new JLabel(new ImageIcon("resources/brand/" + brand + ".png"));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            brandContainer.add(logoLabel, BorderLayout.CENTER);

            JLabel nameLabel = new JLabel(brand);
            nameLabel.setFont(new Font("Netflix Sans", Font.BOLD, 16));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setForeground(new Color(57,62,70));
            brandContainer.add(nameLabel, BorderLayout.SOUTH);

            logoPanel.add(brandContainer);
        }

        centerPanel.add(logoPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Add three new panels
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 20, 20)); // Layout for new panels
        infoPanel.setBackground(new Color(238,238,238));
        infoPanel.setMaximumSize(new Dimension(1350, 500)); // Adjust size
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        FocusListener textFieldFocusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextComponent component = (JTextComponent) e.getComponent();
                component.setBorder(BorderFactory.createLineBorder(new Color(34,40,49), 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextComponent component = (JTextComponent) e.getComponent();
                component.setBorder(BorderFactory.createLineBorder(new Color(57,62,70), 2));
            }
        };

        // Panel1: View Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(new Color(245, 245, 245));
        profilePanel.setBorder(BorderFactory.createLineBorder(new Color(34,40,49), 2));

        // Panel to hold the profile title with extended background
        JPanel profileTitlePanel = new JPanel();
        profileTitlePanel.setLayout(new BoxLayout(profileTitlePanel, BoxLayout.X_AXIS)); // Horizontal alignment
        profileTitlePanel.setBackground(new Color(57, 62, 70)); // Background for the extended title
        profileTitlePanel.setBorder(BorderFactory.createEmptyBorder(1, 75, 1, 75)); // Padding (top, left, bottom, right)
        // Add the title panel to the profilePanel
        profilePanel.add(profileTitlePanel); // Add title panel as the first component

        JLabel profileTitle = new JLabel("Profile Section");
        profileTitle.setFont(new Font("Netflix Sans", Font.BOLD, 20));
        profileTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileTitle.setForeground(new Color(255,211,105));
        profileTitle.setBackground(new Color(57,62,70));
        profileTitle.setBorder(BorderFactory.createEmptyBorder(0,70,0,80));
        profilePanel.add(profileTitle);

        // Add Profile Icon
        JLabel profileIconLabel = new JLabel(new ImageIcon("resources/Icons 50px/Contact100.png")); // Use a 100px icon for better visibility
        profileIconLabel.setSize(100, 80);
        profileIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        profilePanel.add(Box.createRigidArea(new Dimension(0, 45))); // Add some spacing above
        profilePanel.add(profileIconLabel);

        // Add "View Profile" Button
        JButton viewProfileButton = new JButton("View Profile");
        viewProfileButton.setFont(new Font("Poppins", Font.BOLD, 14));
        viewProfileButton.setForeground(new Color(57,62,70));
        viewProfileButton.setBackground(new Color(255,211,105));
        viewProfileButton.setFocusPainted(false);
        viewProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        viewProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewProfileButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 50, 5, 50)));

        // Action for View Profile Button (can be updated later)
        viewProfileButton.addActionListener(e -> {
            if (!viewProfileButton.isEnabled()) return; // Prevent double actions
            // Disable the button
            viewProfileButton.setEnabled(false);

            // Open UserProfilePanel in a new window
            JFrame profileFrame = new JFrame("User Profile");
            profileFrame.setSize(700, 400);
            profileFrame.setLocationRelativeTo(null);
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            profileFrame.add(new ProfileView(formattedUsername));
            profileFrame.setResizable(false);
            profileFrame.setVisible(true);

            profileFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    viewProfileButton.setEnabled(true); // Re-enable the button
                }
            });

            if (!this.isActive() && profileFrame.isActive()){
                profileFrame.dispose();
            }

            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (profileFrame.isEnabled()){
                    profileFrame.dispose();
                    }
                }
            });

        });

        profilePanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add some spacing between the icon and button
        profilePanel.add(viewProfileButton);

        // Add the title to this panel
        profileTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Keep the title centered
        profileTitlePanel.add(profileTitle); // Add the title to the extended background panel

        // Add Panel1 to the center or appropriate section of your layout
        centerPanel.add(profilePanel); //Add to centerPanel or modify according to your layout

        infoPanel.add(profilePanel);

        // Panel 2: Vehicle Listings
        JPanel vehiclePanel = new JPanel();
        vehiclePanel.setLayout(new BoxLayout(vehiclePanel, BoxLayout.Y_AXIS));
        vehiclePanel.setBackground(new Color(57,62,70));
        vehiclePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        JLabel vehicleTitle = new JLabel("Vehicle Listings");
        vehicleTitle.setFont(new Font("Netflix Sans", Font.BOLD, 20));
        vehicleTitle.setForeground(new Color(255,211,105));
        vehicleTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehiclePanel.add(vehicleTitle);

        // Vehicle Listings Search Bar
        JPanel vehicleSearchPanel = new JPanel();
        vehicleSearchPanel.setLayout(new BoxLayout(vehicleSearchPanel, BoxLayout.X_AXIS));
        vehicleSearchPanel.setBackground(new Color(57, 62, 70));
        vehicleSearchPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 55)); // Padding for width adjustment

        JLabel vehicleSearchIcon = new JLabel(new ImageIcon("resources/yellow 50px/search.png")); // Replace with the actual icon path
        vehicleSearchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        vehicleSearchIcon.setAlignmentY(Component.CENTER_ALIGNMENT);

        JTextField vehicleSearchField = new JTextField("Search");
        vehicleSearchField.setFont(new Font("Poppins", Font.BOLD, 14));
        vehicleSearchField.setBackground(new Color(57,62,70));
        vehicleSearchField.setForeground(Color.GRAY);
        vehicleSearchField.setPreferredSize(new Dimension(400, 30));
        vehicleSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Stretchable width
        vehicleSearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,211,105), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        vehicleSearchPanel.add(vehicleSearchIcon);
        vehicleSearchPanel.add(vehicleSearchField);


        // Add Focus Listener
        vehicleSearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (vehicleSearchField.getText().equals("Search")) {
                    vehicleSearchField.setText("");
                    vehicleSearchField.setForeground(new Color(245,245,245));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (vehicleSearchField.getText().isEmpty()) {
                    vehicleSearchField.setText("Search");
                    vehicleSearchField.setForeground(Color.GRAY);
                }
            }
        });

        // Add Search Bar to Vehicle Panel
        vehiclePanel.add(Box.createRigidArea(new Dimension(0, 0))); // Spacing below title
        vehiclePanel.add(vehicleSearchPanel);

        // 2. **JTable Setup**:
        String[] columnNames = {"Brand", "Model","Type"};  // Table headers

        DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable editing for all cells
            }
        };
        JTable vehicleTable = new JTable(tableModel);
        vehicleTable.setFont(new Font("Poppins", Font.BOLD, 14));
        vehicleTable.setForeground(new Color(57,62,70));
        vehicleTable.setBackground(new Color(245, 245, 245));
        vehicleTable.setRowHeight(30);
        vehicleTable.setFocusable(false);
        vehicleTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(5, 50, 5, 50)));

        // Make the column headers bold
        vehicleTable.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        vehicleTable.getTableHeader().setBackground(new Color(57,62,70));
        vehicleTable.getTableHeader().setForeground(new Color(255,211,105));
        vehicleTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(57,62,70)));
        vehicleTable.getTableHeader().setOpaque(true);

        // Center align the column data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vehicleTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // For "Brand" column
        vehicleTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // For "Model" column
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // For "Category" column

        // Add row sorter for filtering
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        vehicleTable.setRowSorter(sorter);

        loadAvailableVehicles(vehicleTable); // Called the Vehicle Listing Method

        // Add the search filter functionality
        vehicleSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String text = vehicleSearchField.getText();
                if (text.trim().isEmpty() || text.equals("Search")) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        vehiclePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Refresh the vehicle table data
                loadAvailableVehicles(vehicleTable); // Reload data (implement this based on your data source)

                // Reapply the filter if a search term exists
                String searchText = vehicleSearchField.getText();
                if (!searchText.equals("Search") && !searchText.trim().isEmpty()) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                } else {
                    sorter.setRowFilter(null); // Clear filter if search is empty
                }
            }
        });

        // JScrollPane for JTable
        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTable);

        // Hide both vertical and horizontal scrollbars but still allow scrolling functionality
        vehicleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Only show vertical scrollbar if needed
        vehicleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  // Only show horizontal scrollbar if needed

        // Set scrollbars to be hidden explicitly
        vehicleScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        vehicleScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        vehiclePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadAvailableVehicles(vehicleTable);  // Reload vehicle data
                sorter.setRowFilter(null);  // Clear the filter for a fresh view
            }
        });

        // Add the scroll pane with hidden scrollbars to the panel
        vehiclePanel.add(vehicleScrollPane);

        // Add the pricing panel to the main panel
        infoPanel.add(vehiclePanel);

        // Panel 3: Car Pricing
        JPanel pricingPanel = new JPanel();
        pricingPanel.setLayout(new BoxLayout(pricingPanel, BoxLayout.Y_AXIS));
        pricingPanel.setBackground(new Color(57, 62, 70));
        pricingPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        JLabel pricingTitle = new JLabel("Vehicle Rent");
        pricingTitle.setFont(new Font("Netflix Sans", Font.BOLD, 20));
        pricingTitle.setForeground(new Color(255, 211, 105));
        pricingTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pricingTitle.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        pricingPanel.add(pricingTitle);

        // Car Pricing JTable Setup
        String[] columnNamesCarP = {"Brand","Model", "Rent Cost"};  // Table headers

        DefaultTableModel tableModelCarP = new DefaultTableModel(null, columnNamesCarP){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable editing for all cells
            }
        };

        JTable pricingTable = new JTable(tableModelCarP);
        pricingTable.setFont(new Font("Poppins", Font.BOLD, 14));
        pricingTable.setForeground(new Color(57,62,70));
        pricingTable.setBackground(new Color(245, 245, 245));
        pricingTable.setRowHeight(30);
        pricingTable.setFocusable(false);
        pricingTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(20, 50, 5, 50)));

        // Make the column headers bold
        pricingTable.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        pricingTable.getTableHeader().setBackground(new Color(57,62,70));
        pricingTable.getTableHeader().setForeground(new Color(255,211,105));
        pricingTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(57,62,70)));
        pricingTable.getTableHeader().setOpaque(true);

        // Center align the column data and apply styles to table cells
        DefaultTableCellRenderer centerRendererCarP = new DefaultTableCellRenderer();
        centerRendererCarP.setHorizontalAlignment(SwingConstants.CENTER);
        centerRendererCarP.setBackground(new Color(57, 62, 70));  // Dark background for cells
        centerRendererCarP.setForeground(Color.WHITE);  // White text for cells

        // Set the custom renderer for both columns
        pricingTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // "Brand" column
        pricingTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // "Model" column
        pricingTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // "Cost" column

        // Create the TableRowSorter
        TableRowSorter<DefaultTableModel> sorterCarP = new TableRowSorter<>(tableModelCarP);
        pricingTable.setRowSorter(sorterCarP);

        // Load data into the table from the database
        loadCarPricingData(pricingTable);

        // JScrollPane for JTable
        JScrollPane pricingScrollPane = new JScrollPane(pricingTable);

        // Hide both vertical and horizontal scrollbars but still allow scrolling functionality
        pricingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Only show vertical scrollbar if needed
        pricingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  // Only show horizontal scrollbar if needed

        // Set scrollbars to be hidden explicitly
        pricingScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        pricingScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        // Add the scroll pane with hidden scrollbars to the panel
        pricingPanel.add(pricingScrollPane);

        // Add the pricing panel to the main panel
        infoPanel.add(pricingPanel);

        // Add new panels to center panel
        centerPanel.add(infoPanel);

        // Combined bottom section (panelSection + buttonPanel)
        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setBackground(new Color(238,238,238));

        // Panel section for data (above buttons)
        JPanel panelSection = new JPanel(new GridLayout(1, 3, 20, 20));
        panelSection.setBackground(new Color(238,238,238));
        panelSection.setMaximumSize(new Dimension(1200, 150)); // Adjust height
        panelSection.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20)); // Add spacing around the panels

        // Initialize titles for panel sections
        String[] titles = {"Total Vehicles", "Available Rides", "Vehicles Booked"};
        JLabel[] valueLabels = new JLabel[3]; // Store value labels for updates

        DatabaseHelper dbHelper = null;
        try {
            dbHelper = DatabaseHelper.DatabaseHelperFactory.createDatabaseHelper();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < titles.length; i++) { // Use titles.length instead of panelData
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(245, 245, 245));
            panel.setBorder(BorderFactory.createLineBorder(new Color(34, 40, 49), 2)); // Add dark gray border

            JLabel titleLabel = new JLabel(titles[i]); // Use titles[i] here
            titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 18));
            titleLabel.setForeground(new Color(34, 40, 49));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0)); // Add spacing around the title

            JLabel valueLabel = new JLabel("..."); // Initial placeholder
            valueLabel.setFont(new Font("Poppins", Font.BOLD, 28));
            valueLabel.setForeground(new Color(57, 62, 70));
            valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(titleLabel);
            panel.add(valueLabel);

            panelSection.add(panel); // Ensure panelSection is correctly initialized
            valueLabels[i] = valueLabel; // Store label for future updates
        }

        // Add Timer for periodic updates
        DatabaseHelper finalDbHelper = dbHelper;
        Timer updateTimer = new Timer(2000, e -> {
            try {
                // Fetch values from the database
                int totalCars =  finalDbHelper.getTotalCarsCount();
                int availableCars = finalDbHelper.getAvailableCarsCount();
                int bookedCars = finalDbHelper.getBookedCarsCount();

                // Update labels dynamically
                valueLabels[0].setText(String.valueOf(totalCars));
                valueLabels[1].setText(String.valueOf(availableCars));
                valueLabels[2].setText(String.valueOf(bookedCars));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        updateTimer.start();

        bottomSection.add(panelSection);

        // Button section
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 20));
        buttonPanel.setBackground(new Color(238,238,238));
        buttonPanel.setMaximumSize(new Dimension(1500, 300)); // Adjust button section size
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addCarButton = new JButton("Add a Vehicle");
        addCarButton.setFont(new Font("Poppins", Font.BOLD, 18));
        addCarButton.setForeground(new Color(57,62,70));
        addCarButton.setBackground(new Color(255,211,105));
        addCarButton.setFocusPainted(false);
        addCarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCarButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        addCarButton.addActionListener(e -> {
            dispose();
            new AddCar(username);

        });

        JButton viewInventoryButton = new JButton("View Inventory");
        viewInventoryButton.setFont(new Font("Poppins", Font.BOLD, 18));
        viewInventoryButton.setForeground(new Color(57,62,70));
        viewInventoryButton.setBackground(new Color(255,211,105));
        viewInventoryButton.setFocusPainted(false);
        viewInventoryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewInventoryButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        viewInventoryButton.addActionListener(e -> {
            dispose();
            new ViewInventory(username);
        });

        JButton manageBookingsButton = new JButton("Manage Bookings");
        manageBookingsButton.setFont(new Font("Poppins", Font.BOLD, 18));
        manageBookingsButton.setForeground(new Color(57,62,70));
        manageBookingsButton.setBackground(new Color(255,211,105));
        manageBookingsButton.setFocusPainted(false);
        manageBookingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        manageBookingsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        manageBookingsButton.addActionListener(e -> {
            dispose();
            new ManageBookings(username);

        });

        JButton rentCarButton = new JButton("Rent a Ride");
        rentCarButton.setFont(new Font("Poppins", Font.BOLD, 18));
        rentCarButton.setForeground(new Color(57,62,70));
        rentCarButton.setBackground(new Color(255,211,105));
        rentCarButton.setFocusPainted(false);
        rentCarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rentCarButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        rentCarButton.addActionListener(e -> {
            dispose();
            new BookCar(username);

        });

        // Add buttons to the panel
        buttonPanel.add(addCarButton);
        buttonPanel.add(viewInventoryButton);
        buttonPanel.add(manageBookingsButton);
        buttonPanel.add(rentCarButton);

        bottomSection.add(buttonPanel);

        // Add bottom section to frame
        add(bottomSection, BorderLayout.SOUTH);

        setVisible(true);
    }


    private void loadAvailableVehicles(JTable vehicleTable) {
        DatabaseHelper dbHelper = null; // Instantiate your helper class
        try {
            dbHelper = new DatabaseHelper();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        java.util.List<String[]> vehicles = dbHelper.fetchAvailableVehicles();

        DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();

        // Clear existing rows
        model.setRowCount(0);

        // Add new rows for available vehicles
        for (String[] vehicle : vehicles) {
            model.addRow(new Object[]{vehicle[0], vehicle[1], vehicle[2]} );
        }
    }

    // Method to fetch and load car pricing data from the database
    private void loadCarPricingData(JTable pricingTable) {
        DatabaseHelper dbHelper = null; // Instantiate your helper class
        try {
            dbHelper = new DatabaseHelper();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        java.util.List<String[]> pricingData = dbHelper.fetchCarPricing();  // Assuming fetchCarPricing returns brand, model and cost

        // Add rows to the table
        DefaultTableModel model = (DefaultTableModel) pricingTable.getModel();
        for (String[] pricing : pricingData) {
            model.addRow(new Object[]{pricing[0],pricing[1],pricing[2]});  // Adding each row with brand, model and cost data
        }
    }

}
