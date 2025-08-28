import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class YourBookings extends JFrame {
    private JTable currentBookingsTable;
    private DefaultTableModel currentBookingsTableModel;

    private JTable prevBookingsTable;
    private DefaultTableModel prevBookingsTableModel;

    public YourBookings(String username) {
        setTitle("Your Bookings");
        setSize(1790, 930);
        setLocationRelativeTo(null);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        userInfoLabel.setForeground(Color.WHITE);
        leftPanel.add(userInfoLabel);

        // Right side of the nav bar (Logout button)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        JButton logoutButton = new JButton(" Home");
        logoutButton.setFocusPainted(false);
        logoutButton.setIcon(new ImageIcon("resources/yellow 50px/Home.png"));
        logoutButton.setSize(100,80);
        logoutButton.setFont(new Font("Poppins", Font.BOLD, 14));
        logoutButton.setForeground(new Color(238, 238, 238));
        logoutButton.setBackground(new Color(34, 40, 49));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        logoutButton.addActionListener(e -> {
            this.dispose(); // Close the current window
        });
        rightPanel.add(logoutButton);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        // Add navbar to the frame
        add(navBar, BorderLayout.NORTH);

        // Main content panel for the title and center functionality
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(238, 238, 238));

        // Title panel below navbar
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(34, 40, 49));
        titlePanel.setPreferredSize(new Dimension(getWidth(), 100));

        JLabel titleLabel = new JLabel("Rent a Car");
        titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255,211,105));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center panel for card layouts
        JPanel centerPanel = new JPanel(new CardLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(53, 300, 50, 300));

        // ----- Columns for both tables (without Actions) -----
        String[] columnNames = {
                "Bill_no", "Customer Name", "Phone No", "Booking Date",
                "No Plate", "Vehicle Details", "Days Booked", "Total Rent"
        };

        // Card 1: Current Bookings
        JPanel currentBookings = new JPanel(new BorderLayout());
        currentBookings.setBackground(new Color(238, 238, 238));
        currentBookings.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 0, 0, 0)));

        // Create heading label centered for current bookings
        JLabel currentHeadingLabel = new JLabel("Current Bookings", SwingConstants.CENTER);
        currentHeadingLabel.setFont(new Font("Netflix Sans", Font.BOLD, 22));
        currentHeadingLabel.setForeground(new Color(57, 62, 70));
        currentHeadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // ----- Current Bookings Table -----
        currentBookingsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable cells
            }
        };

        currentBookingsTable = new JTable(currentBookingsTableModel);
        styleTable(currentBookingsTable);

        JScrollPane currentScrollPane = new JScrollPane(currentBookingsTable);
        currentScrollPane.setBorder(BorderFactory.createEmptyBorder()); // no padding/border
        currentScrollPane.setViewportBorder(null);
        currentBookings.add(currentScrollPane, BorderLayout.CENTER);

        // Set layout for currentBookings panel
        currentBookings.add(currentHeadingLabel, BorderLayout.NORTH);



        // Card 2: Previous Bookings
        JPanel prevBookings = new JPanel(new BorderLayout());
        prevBookings.setBackground(new Color(238, 238, 238));
        prevBookings.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 0, 0, 0)));

        JLabel prevHeadingLabel = new JLabel("Previous Bookings", SwingConstants.CENTER);
        prevHeadingLabel.setFont(new Font("Netflix Sans", Font.BOLD, 22));
        prevHeadingLabel.setForeground(new Color(57, 62, 70));
        prevHeadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        prevBookings.setLayout(new BorderLayout());
        prevBookings.add(prevHeadingLabel, BorderLayout.NORTH);

        // ----- Previous Bookings Table -----
        prevBookingsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable cells
            }
        };

        prevBookingsTable = new JTable(prevBookingsTableModel);
        styleTable(prevBookingsTable);

        JScrollPane prevScrollPane = new JScrollPane(prevBookingsTable);
        prevScrollPane.setBorder(BorderFactory.createEmptyBorder()); // no padding/border
        prevScrollPane.setViewportBorder(null);
        prevBookings.add(prevScrollPane, BorderLayout.CENTER);


        // Add cards to center panel
        centerPanel.add(currentBookings, "CurrentBookings");
        centerPanel.add(prevBookings, "PreviousBookings");

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        DatabaseHelper dbHelper = null;
        try {
            dbHelper = new DatabaseHelper();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        DatabaseHelper dbHelper = new DatabaseHelper();
// Explicit import or full package name prevents ambiguity
        java.util.List<Object[]> currentData = dbHelper.fetchUserCurrentBookings(username);
        for (Object[] row : currentData) {
            currentBookingsTableModel.addRow(row);
        }

        java.util.List<Object[]> prevData = dbHelper.fetchUserPrevBookings(username);
        for (Object[] row : prevData) {
            prevBookingsTableModel.addRow(row);
        }



        // Bottom panel for navigation buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 35, 20));
        bottomPanel.setMaximumSize(new Dimension(1500, 300)); // Adjust button section size
        bottomPanel.setBackground(new Color(238, 238, 238));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 20, 150));

        JButton currentBookingsButton = new JButton("Current Bookings");
        currentBookingsButton.setFont(new Font("Poppins", Font.BOLD, 18));
        currentBookingsButton.setForeground(new Color(57,62,70));
        currentBookingsButton.setBackground(new Color(255,211,105));
        currentBookingsButton.setFocusPainted(false);
        currentBookingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        currentBookingsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JButton prevBookingsButton = new JButton("Previous Bookings");
        prevBookingsButton.setFont(new Font("Poppins", Font.BOLD, 18));
        prevBookingsButton.setForeground(new Color(57,62,70));
        prevBookingsButton.setBackground(new Color(255,211,105));
        prevBookingsButton.setFocusPainted(false);
        prevBookingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        prevBookingsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

//        bottomPanel.add(bookByDistanceButton);
        bottomPanel.add(currentBookingsButton);
        bottomPanel.add(prevBookingsButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Action listeners for buttons to switch cards
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();

//        bookByDistanceButton.addActionListener(e -> cardLayout.show(centerPanel, "BookByDistance"));

        currentBookingsButton.addActionListener(e -> cardLayout.show(centerPanel, "CurrentBookings"));

        prevBookingsButton.addActionListener(e -> cardLayout.show(centerPanel, "PreviousBookings"));

        // Should be at the Last
        setVisible(true);
    }

    private void styleTable(JTable table) {
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setRowHeight(40);
        table.setFont(new Font("Netflix Sans", Font.PLAIN, 12));
        table.setFocusable(false);
        table.setBackground(new Color(245, 245, 245));
        table.setGridColor(new Color(222, 222, 222));
        table.setSelectionBackground(new Color(222, 222, 222));
        table.setSelectionForeground(new Color(57, 62, 70));
        table.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        header.setBackground(new Color(57, 62, 70));
        header.setFocusable(false);
        header.setForeground(new Color(255, 211, 105));
        header.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(57, 62, 70)));
        header.setOpaque(true);

        // Adjust cell renderer for new column indexes (No Plate is column 4, Vehicle Details now column 5)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setText(value != null ? value.toString() : "");
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setVerticalAlignment(SwingConstants.TOP);
                return label;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            if (i == 5) {
                columnModel.getColumn(i).setCellRenderer(leftRenderer);
            } else {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        columnModel.getColumn(0).setPreferredWidth(50);  // Bill_no
        columnModel.getColumn(1).setPreferredWidth(150); // Customer Name
        columnModel.getColumn(2).setPreferredWidth(100); // Phone No
        columnModel.getColumn(3).setPreferredWidth(100); // Booking Date
        columnModel.getColumn(4).setPreferredWidth(100); // No Plate
        columnModel.getColumn(5).setPreferredWidth(200); // Vehicle Details
        columnModel.getColumn(6).setPreferredWidth(80);  // Days Booked
        columnModel.getColumn(7).setPreferredWidth(100); // Total Rent
    }




}

