import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;

public class ManageBookings extends JFrame {

    //private final int editingRow = -1; // -1 means no row editing active

    public ManageBookings(String username) {
        setTitle("Book a Vehicle");
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

            String role = DatabaseHelper.getUserRole(username);
            dispose(); // Close the current window

            if ("Admin".equalsIgnoreCase(role)) {
                new HomePageAdmin(username); // Navigate to admin home page
            } else if ("User".equalsIgnoreCase(role)) {
                new HomePageUser(username); // Navigate to user home page
            } else {
                CustomOptionPane.showMsgDialog(this, "Role not found! Please contact support.", "Error");
                new LoginPage(); // Fallback to login page
            }

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

        JLabel titleLabel = new JLabel("Bookings");
        titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255,211,105));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center panel for card layouts
        JPanel centerPanel = new JPanel(new CardLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(53, 300, 50, 300));


        // All Bookings Panel Setup (add inside wherever you set up your UI)
        JPanel allBookings = new JPanel(new BorderLayout());
        allBookings.setBackground(new Color(238,238,238));
        allBookings.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));

        // Create centered heading panel
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        headingPanel.setOpaque(false);

        JLabel headingLabel = new JLabel("All Bookings");
        headingLabel.setFont(new Font("Netflix Sans", Font.BOLD, 24));
        headingLabel.setForeground(new Color(57,62,70));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        headingPanel.add(headingLabel);



        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5,0,10,0));


        JTextField searchField = new JTextField("Search");
        searchField.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        searchField.setCaretColor(new Color(34,40,49));
        searchField.setBackground(new Color(245,245,245));
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Stretchable width
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Add Focus Listener
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setFocusable(true);

                    searchField.setText("");
                    searchField.setForeground(new Color(57,62,70));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.requestFocusInWindow();
                    searchField.setForeground(new Color(57,62,70));
                }
            }
        });

        searchPanel.add(searchField);

        // Stack heading and search panel vertically
        Box topBox = Box.createVerticalBox();
        topBox.add(headingPanel);
        topBox.add(searchPanel);

// Add combined box to the panel
        allBookings.add(topBox, BorderLayout.NORTH);
//        allBookings.add(searchPanel, BorderLayout.NORTH);

        // Table Model and JTable
        String[] columnNames = {
                "Bill_no", "Customer Name", "Phone No", "Booking Date",
                "Vehicle Details", "Days Booked", "Total Rent", "Actions"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make 'days_booked' AND 'Actions' column editable
                return column == 5 || column == 7;
            }
        };


        JTable table = new JTable(tableModel);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setRowHeight(40);
        table.setFont(new Font("Netflix Sans", Font.PLAIN, 12));
        table.setFocusable(false);
        table.setBackground(new Color(245,245,245));
        table.setGridColor(new Color(222, 222, 222));
        table.setSelectionBackground(new Color(222, 222, 222));
        table.setSelectionForeground(new Color(57,62,70));
        table.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        // Make the column headers bold
        table.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(57,62,70));
        table.getTableHeader().setFocusable(false);
        table.getTableHeader().setForeground(new Color(255,211,105));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(57,62,70)));
        table.getTableHeader().setOpaque(true);


        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value != null) {
                    setText(value.toString());
                } else {
                    setText("");
                }
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setVerticalAlignment(SwingConstants.TOP);
                return label;
            }
        });

        // After creating your JTable and DefaultTableModel
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            if (i == 4) {
                columnModel.getColumn(i).setCellRenderer(leftRenderer);
            } else {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Action buttons renderer/editor for last column
        columnModel.getColumn(7).setCellRenderer(new ActionsRenderer());
        columnModel.getColumn(7).setCellEditor(new ActionsEditor(table));

        columnModel.getColumn(0).setPreferredWidth(50);   // Bill_no
        columnModel.getColumn(1).setPreferredWidth(150);  // Customer Name
        columnModel.getColumn(2).setPreferredWidth(100);  // Phone No
        columnModel.getColumn(3).setPreferredWidth(100);  // Booking Date
        columnModel.getColumn(4).setPreferredWidth(200);  // Vehicle Details
        columnModel.getColumn(5).setPreferredWidth(80);   // Days Booked
        columnModel.getColumn(6).setPreferredWidth(100);  // Total Rent
        columnModel.getColumn(7).setPreferredWidth(150);  // Actions (buttons)

        // Populate bills data
        loadBillsData(tableModel);

        tableModel.addTableModelListener(e -> {
            // Only proceed if editing finished and the edited cell was in days_booked column (index 5)
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                int row = e.getFirstRow();
                try {
                    // Get updated days_booked
                    Object daysObj = tableModel.getValueAt(row, 5);
                    int daysBooked = Integer.parseInt(daysObj.toString());

                    // Get current total_rent (remove any formatting)
                    String rentStr = tableModel.getValueAt(row, 6).toString().replace("/-", "").trim();
                    int oldTotalRent = Integer.parseInt(rentStr);

                    // Calculate new total rent: add 500 to old total rent (or alternatively daysBooked * some rate)
                    int newTotalRent = oldTotalRent + (oldTotalRent/2+500);

                    // Update the table model with the new total rent
                    tableModel.setValueAt(newTotalRent + "/-", row, 6);

                    // Get bill_no to update the DB record
                    int billNo = (int) tableModel.getValueAt(row, 0);

                    // Update database record
                    try (Connection conn = DatabaseHelper.getConnection();
                         PreparedStatement ps = conn.prepareStatement("UPDATE bills SET days_booked = ?, total_rent = ? WHERE bill_no = ?")) {
                        ps.setInt(1, daysBooked);
                        ps.setInt(2, newTotalRent);
                        ps.setInt(3, billNo);
                        int updated = ps.executeUpdate();
                        if (updated != 1) {
                            CustomOptionPane.showErrorDialog(null, "Failed to update booking in database.");
                        } else {
                            CustomOptionPane.showDoneDialog(null, "Booking updated successfully.", "Success");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        CustomOptionPane.showErrorDialog(null, "Database error happened while updating.");
                    }

                } catch (NumberFormatException ex) {
                    CustomOptionPane.showErrorDialog(null, "Invalid number entered for days booked.");
                }
            }
        });


        // Table scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));


        // Enable sorting & filtering
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Real-time search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String text = searchField.getText().trim();
                if (text.equalsIgnoreCase("Search") || text.isEmpty()) {
                    sorter.setRowFilter(null); // Show all rows
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // (?i) makes it case-insensitive
                }
            }
        });

//        allBookings.add(scrollPane);
        allBookings.add(scrollPane, BorderLayout.CENTER);


        // Card 2: Booking History
        JPanel bookingHistory = new JPanel(new BorderLayout());
        bookingHistory.setBackground(new Color(238, 238, 238));
        bookingHistory.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));

// Centered Heading Panel for Booking History
        JPanel historyHeadingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        historyHeadingPanel.setOpaque(false);

        JLabel historyHeadingLabel = new JLabel("Booking History");
        historyHeadingLabel.setFont(new Font("Netflix Sans", Font.BOLD, 24));
        historyHeadingLabel.setForeground(new Color(57, 62, 70));
        historyHeadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        historyHeadingPanel.add(historyHeadingLabel);

// Search bar for Booking History
        JPanel historySearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        historySearchPanel.setOpaque(false);
        historySearchPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JTextField historySearchField = new JTextField("Search");
        historySearchField.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        historySearchField.setCaretColor(new Color(34, 40, 49));
        historySearchField.setBackground(new Color(245, 245, 245));
        historySearchField.setForeground(Color.GRAY);
        historySearchField.setPreferredSize(new Dimension(400, 35));
        historySearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        historySearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        historySearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (historySearchField.getText().equals("Search")) {
                    historySearchField.setFocusable(true);
                    historySearchField.setText("");
                    historySearchField.setForeground(new Color(57, 62, 70));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (historySearchField.getText().isEmpty()) {
                    historySearchField.setText("Search");
                    historySearchField.requestFocusInWindow();
                    historySearchField.setForeground(new Color(57, 62, 70));
                }
            }
        });

        historySearchPanel.add(historySearchField);

// Stack heading and search panel vertically
        Box historyTopBox = Box.createVerticalBox();
        historyTopBox.add(historyHeadingPanel);
        historyTopBox.add(historySearchPanel);

        bookingHistory.add(historyTopBox, BorderLayout.NORTH);

// Column names without 'Actions' column
        String[] historyColumnNames = {
                "Bill_no", "Customer Name", "Phone No", "Booking Date",
                "Vehicle Details","Brand/Model", "Days Booked", "Total Rent"
        };

        DefaultTableModel historyTableModel = new DefaultTableModel(historyColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable columns for booking history
            }
        };

        JTable historyTable = new JTable(historyTableModel);
        historyTable.setShowVerticalLines(false);
        historyTable.setShowHorizontalLines(true);
        historyTable.setRowHeight(40);
        historyTable.setFont(new Font("Netflix Sans", Font.PLAIN, 12));
        historyTable.setFocusable(false);
        historyTable.setBackground(new Color(245, 245, 245));
        historyTable.setGridColor(new Color(222, 222, 222));
        historyTable.setSelectionBackground(new Color(222, 222, 222));
        historyTable.setSelectionForeground(new Color(57, 62, 70));
        historyTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

// Style table header same as allBookings panel
        historyTable.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        historyTable.getTableHeader().setBackground(new Color(57, 62, 70));
        historyTable.getTableHeader().setFocusable(false);
        historyTable.getTableHeader().setForeground(new Color(255, 211, 105));
        historyTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(57, 62, 70)));
        historyTable.getTableHeader().setOpaque(true);

        historyTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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

// Cell Renderers for centering or left-aligning columns
        DefaultTableCellRenderer historyCenterRenderer = new DefaultTableCellRenderer();
        historyCenterRenderer.setHorizontalAlignment(JLabel.CENTER);

//        DefaultTableCellRenderer historyLeftRenderer = new DefaultTableCellRenderer();
//        historyLeftRenderer.setHorizontalAlignment(JLabel.LEFT);

        TableColumnModel historyColumnModel = historyTable.getColumnModel();
        historyColumnModel.getColumn(0).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(1).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(2).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(3).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(4).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(5).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(6).setCellRenderer(historyCenterRenderer);
        historyColumnModel.getColumn(7).setCellRenderer(historyCenterRenderer);

//        for (int i = 0; i < historyColumnModel.getColumnCount(); i++) {
//            if (i == 4) { // Vehicle Details left aligned
//                historyColumnModel.getColumn(i).setCellRenderer(historyLeftRenderer);
//            } else {
//                historyColumnModel.getColumn(i).setCellRenderer(historyCenterRenderer);
//            }
//        }

        historyColumnModel.getColumn(0).setPreferredWidth(40);   // Bill_no
        historyColumnModel.getColumn(1).setPreferredWidth(150);  // Customer Name
        historyColumnModel.getColumn(2).setPreferredWidth(80);  // Phone No
        historyColumnModel.getColumn(3).setPreferredWidth(80);  // Booking Date
        historyColumnModel.getColumn(4).setPreferredWidth(100);  // Vehicle Details
        historyColumnModel.getColumn(5).setPreferredWidth(200);  // Brand & Model
        historyColumnModel.getColumn(6).setPreferredWidth(80);   // Days Booked
        historyColumnModel.getColumn(7).setPreferredWidth(50);  // Total Rent

// load bookingHistory data
        loadBookingHistoryData(historyTableModel);

// Table scroll pane
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        bookingHistory.add(historyScrollPane, BorderLayout.CENTER);

// Enable sorting & filtering
        TableRowSorter<DefaultTableModel> historySorter = new TableRowSorter<>(historyTableModel);
        historyTable.setRowSorter(historySorter);

// Real-time search for booking history
        historySearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String text = historySearchField.getText().trim();
                if (text.equalsIgnoreCase("Search") || text.isEmpty()) {
                    historySorter.setRowFilter(null);
                } else {
                    historySorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });


        // Add cards to center panel
        centerPanel.add(allBookings, "AllBookings");
        centerPanel.add(bookingHistory, "BookingHistory");

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for navigation buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 35, 20));
        bottomPanel.setMaximumSize(new Dimension(1500, 300)); // Adjust button section size
        bottomPanel.setBackground(new Color(238, 238, 238));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 20, 150));

        JButton allBookingsButton = new JButton("All Bookings");
        allBookingsButton.setFont(new Font("Poppins", Font.BOLD, 18));
        allBookingsButton.setForeground(new Color(57,62,70));
        allBookingsButton.setBackground(new Color(255,211,105));
        allBookingsButton.setFocusPainted(false);
        allBookingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        allBookingsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JButton bookingHistoryButton = new JButton("Booking History");
        bookingHistoryButton.setFont(new Font("Poppins", Font.BOLD, 18));
        bookingHistoryButton.setForeground(new Color(57,62,70));
        bookingHistoryButton.setBackground(new Color(255,211,105));
        bookingHistoryButton.setFocusPainted(false);
        bookingHistoryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookingHistoryButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        bottomPanel.add(allBookingsButton);
        bottomPanel.add(bookingHistoryButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Action listeners for buttons to switch cards
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();

//        allBookingsButton.addActionListener(e -> cardLayout.show(centerPanel, "AllBookings"));

        allBookingsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, "AllBookings");
                loadBillsData(tableModel);
            }
        });

        bookingHistoryButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, "BookingHistory");
                loadBookingHistoryData(historyTableModel);
            }
        });


        // Should be at the Last
        setVisible(true);
    }

    // --- Helper classes ---
    static class ActionsRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
            panel.setBackground(new Color(245,245,245));
            panel.setOpaque(true);

            // Set size to 30x30 pixels (adjust width and height as you want)
            Dimension buttonSize = new Dimension(60, 28);

            JButton deleteBtn = new JButton("Delete");
            // Styling Delete button
            deleteBtn.setPreferredSize(buttonSize);
            deleteBtn.setBackground(new Color(255, 211, 105));
            deleteBtn.setForeground(new Color(57, 62, 70));
            deleteBtn.setFont(new Font("Poppins", Font.BOLD, 14));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorder(BorderFactory.createLineBorder(new Color(57, 62, 70), 2, true));
            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            deleteBtn.setMargin(new Insets(3, 10, 3, 10));
            deleteBtn.setOpaque(true);
            deleteBtn.setFocusable(false);

            panel.add(deleteBtn);

            JButton editBtn = new JButton("Edit"); // pencil icon
            // Styling Edit button
            editBtn.setPreferredSize(buttonSize);
            editBtn.setBackground(new Color(255, 211, 105));
            editBtn.setForeground(new Color(57, 62, 70));
            editBtn.setFont(new Font("Poppins", Font.BOLD, 14));
            editBtn.setFocusPainted(false);
            editBtn.setBorder(BorderFactory.createLineBorder(new Color(57, 62, 70), 2, true));
            editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            editBtn.setMargin(new Insets(3, 7, 3, 7));
            editBtn.setOpaque(true);
            editBtn.setFocusable(false);

            panel.add(editBtn);


            return panel;
        }
    }

    class ActionsEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel;
        JButton deleteBtn, editBtn;
        JTable table;

        public ActionsEditor(JTable table) {
            this.table = table;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
            panel.setBackground(new Color(245,245,245));
            panel.setOpaque(true);

            // Set size to 30x30 pixels (adjust width and height as you want)
            Dimension buttonSize = new Dimension(60, 28);

            deleteBtn = new JButton("Delete");
            deleteBtn.setPreferredSize(buttonSize);
            deleteBtn.setBackground(new Color(255, 211, 105));
            deleteBtn.setForeground(new Color(57, 62, 70));
            deleteBtn.setFont(new Font("Poppins", Font.BOLD, 14));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorder(BorderFactory.createLineBorder(new Color(57, 62, 70), 2, true));
            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            deleteBtn.setMargin(new Insets(3, 10, 3, 10));
            deleteBtn.setOpaque(true);
            deleteBtn.setFocusable(false);


            editBtn = new JButton("Edit");
            // Styling Edit button
            editBtn.setPreferredSize(buttonSize);
            editBtn.setBackground(new Color(255, 211, 105));
            editBtn.setForeground(new Color(57, 62, 70));
            editBtn.setFont(new Font("Poppins", Font.BOLD, 14));
            editBtn.setFocusPainted(false);
            editBtn.setBorder(BorderFactory.createLineBorder(new Color(57, 62, 70), 2, true));
            editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            editBtn.setMargin(new Insets(3, 7, 3, 7));
            editBtn.setOpaque(true);
            editBtn.setFocusable(false);

            // Delete logic
            deleteBtn.addActionListener(e -> {
                int row = table.getEditingRow();

                int billNo = (int) table.getValueAt(row, 0);
                String cusName = (String) table.getValueAt(row, 1);
                String phoneNo = (String) table.getValueAt(row, 2);
                String bookingDate = (String) table.getValueAt(row, 3);
                String vehicleDetails = (String) table.getValueAt(row, 4);
                vehicleDetails = vehicleDetails.replaceAll("<html>|</html>", "").trim();

                String noPlate = vehicleDetails.split(" ")[0];
                if (noPlate.length() > 10) noPlate = noPlate.substring(0, 10);

                int daysBooked = Integer.parseInt(table.getValueAt(row, 5).toString());
                String totalRentStr = table.getValueAt(row, 6).toString().replace("/-", "").trim();
                int totalRent = Integer.parseInt(totalRentStr);

                // Fetch brand_model from bills table
                String brandModel = "";
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement psBrand = conn.prepareStatement("SELECT brand_model FROM bills WHERE no_plate = ?")) {
                    psBrand.setString(1, noPlate);
                    try (ResultSet rs = psBrand.executeQuery()) {
                        if (rs.next()) {
                            brandModel = rs.getString("brand_model");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    CustomOptionPane.showMsgDialog(table, "Failed to retrieve vehicle info.", "Error");
                    fireEditingStopped();
                    return;
                }

                int result = CustomOptionPane.chooseDialog(ManageBookings.this, "Are you sure you want to delete?", "Confirm Deletion");

                if (result == JOptionPane.YES_OPTION) {
                    Connection conn = null;
                    try {
                        conn = DatabaseHelper.getConnection();
                        conn.setAutoCommit(false);

                        // Archive the bill
                        boolean archived = DatabaseHelper.archiveBill(billNo, cusName, phoneNo,
                                bookingDate, noPlate, brandModel, daysBooked, totalRent);

                        if (!archived) {
                            throw new SQLException("Failed to archive booking.");
                        }

                        // Update car status to Available
                        try (PreparedStatement updateStatus = conn.prepareStatement(
                                "UPDATE cars SET status = 'Available' WHERE no_plate = ?")) {
                            updateStatus.setString(1, noPlate);
                            updateStatus.executeUpdate();
                        }

                        // Delete the bill record
                        try (PreparedStatement deleteBill = conn.prepareStatement("DELETE FROM bills WHERE bill_no = ?")) {
                            deleteBill.setInt(1, billNo);
                            deleteBill.executeUpdate();
                        }

                        conn.commit();

                        // Update UI table model
                        ((DefaultTableModel) table.getModel()).removeRow(row);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        if (conn != null) {
                            try {
                                conn.rollback();
                            } catch (SQLException rollbackEx) {
                                rollbackEx.printStackTrace();
                            }
                        }
                        CustomOptionPane.showMsgDialog(table, "Error occurred while deleting booking.", "Error");
                    } finally {
                        if (conn != null) {
                            try {
                                conn.setAutoCommit(true);
                                conn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                fireEditingStopped();
            });



            // Edit logic
            editBtn.addActionListener(e -> {
                int row = table.getEditingRow();
                int billNo = (int) table.getValueAt(row, 0);
                CustomOptionPane.showDoneDialog(table, "Edit dialog for Bill_no " + billNo,"Done");
                fireEditingStopped();
            });

            panel.add(deleteBtn);
            panel.add(editBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                panel.setBackground(new Color(222,222,222));
            }


            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // --- Helper functions to load data ---
    private void loadBillsData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM bills")) {
            while (rs.next()) {
                String vehicleDetails = "<html>" + rs.getString("no_plate") + "<br/>" + rs.getString("brand_model") + "</html>";
                Object[] row = {
                        rs.getInt("bill_no"),
                        rs.getString("cus_name"),
                        rs.getString("phone_no"),
                        rs.getString("booking_date"),
//                        rs.getString("no_plate") + "<br>" + rs.getString("brand_model"),
                        vehicleDetails,
                        rs.getInt("days_booked"),
                        rs.getInt("total_rent") + "/-",
                        ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadBookingHistoryData(DefaultTableModel tableModel) {
        DatabaseHelper dbHelper = null;
        try {
            dbHelper = new DatabaseHelper();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Object[]> historyData = dbHelper.fetchBookingHistory();

        tableModel.setRowCount(0); // clear table
        for (Object[] rowData : historyData) {
            tableModel.addRow(rowData); // rowData is Object[], as required by addRow
        }
    }


}