import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Vector;

import static javax.swing.BorderFactory.createEmptyBorder;

public class ViewInventory extends JFrame {
    private DefaultTableModel tableModel = new DefaultTableModel();
        private DefaultTableModel customerTableModel; // Declare customerTableModel
        private JTable customerTable; // Declare customerTable

    public ViewInventory(String username) {
            setTitle("Book a Car");
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
            userIconLabel.setBorder(createEmptyBorder(10, 10, 10, 0));
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
            logoutButton.setSize(100, 80);
            logoutButton.setFont(new Font("Poppins", Font.BOLD, 14));
            logoutButton.setForeground(new Color(238, 238, 238));
            logoutButton.setBackground(new Color(34, 40, 49));
            logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            logoutButton.setBorder(createEmptyBorder(10, 10, 10, 20));
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

            JLabel titleLabel = new JLabel("Inventory");
            titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 36));
            titleLabel.setForeground(new Color(255, 211, 105));
            titlePanel.setBorder(createEmptyBorder(15, 0, 0, 0));
            titlePanel.add(titleLabel);

            mainPanel.add(titlePanel, BorderLayout.NORTH);

            // Center panel for card layouts
            JPanel centerPanel = new JPanel(new CardLayout());
            centerPanel.setBorder(createEmptyBorder(53, 300, 50, 300));

            // Garage Info Panel with JTable
            JPanel garageInfoPanel = new JPanel(new BorderLayout());
            garageInfoPanel.setBackground(new Color(57, 62, 70));
            garageInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                    createEmptyBorder(0, 0, 0, 0)));

            // Create a container panel for the title and table
            JPanel containerPanel = new JPanel();
            containerPanel.setBackground(new Color(57, 62, 70));
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
            containerPanel.setBackground(new Color(57, 62, 70));

            // Add the title to a separate wrapper panel
            JPanel titleWrapperPanel = new JPanel();
            titleWrapperPanel.setBackground(new Color(57, 62, 70)); // Set background for the title area
            titleWrapperPanel.setLayout(new BoxLayout(titleWrapperPanel, BoxLayout.Y_AXIS));
            titleWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleWrapperPanel.setBorder(createEmptyBorder(0, 0, 0, 0)); // Add some spacing

            JLabel vehicleTitle = new JLabel("Vehicle Data");
            vehicleTitle.setFont(new Font("Netflix Sans", Font.BOLD, 20));
            vehicleTitle.setForeground(new Color(255, 211, 105));
            vehicleTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            vehicleTitle.setBorder(createEmptyBorder(10, 10, 10, 10)); // Add some spacing

            titleWrapperPanel.add(vehicleTitle);
            containerPanel.add(titleWrapperPanel);



            // JTable to display car data
            String[] columnNames = {"License No.", "Brand", "Model", "Type", "Cost", "Status"};
            Object[][] data = {}; // Initially empty, data will be fetched dynamically.

            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Make all cells non-editable
                    return false;
                } // Initialize with column names
            };

            JTable carsTable = new JTable(tableModel);
            carsTable.setFont(new Font("Poppins", Font.BOLD, 14));
            carsTable.setRowHeight(30); // Increase vertical spacing
            carsTable.setBackground(new Color(245, 245, 245));
            carsTable.setShowGrid(true);
            carsTable.setGridColor(Color.LIGHT_GRAY);
            carsTable.setFillsViewportHeight(true); // Ensures table occupies the full viewport
            carsTable.setCellSelectionEnabled(true);
            carsTable.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                    createEmptyBorder(5, 50, 5, 50)));

            // Make the column headers bold
            carsTable.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
            carsTable.getTableHeader().setBackground(new Color(57, 62, 70));
            carsTable.getTableHeader().setForeground(new Color(255, 211, 105));
            carsTable.getTableHeader().setOpaque(true);

            // Center align the column data
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < carsTable.getColumnCount(); i++) {
                carsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Add buttons panel below the table
            JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 50, 50));
            buttonsPanel.setBackground(new Color(57, 62, 70));
            buttonsPanel.setBorder(createEmptyBorder(0, 30, 10, 30));

            JButton deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("Poppins", Font.BOLD, 14));
            deleteButton.setForeground(new Color(57, 62, 70));
            deleteButton.setBackground(new Color(255, 211, 105));
            deleteButton.setFocusPainted(false);
            deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                    createEmptyBorder(5, 5, 5, 5)));
            buttonsPanel.add(deleteButton);

            deleteButton.addActionListener(e -> {
                int selectedRow = carsTable.getSelectedRow();
                if (selectedRow != -1) { // Ensure a row is selected
                    String carId = carsTable.getValueAt(selectedRow, 0).toString(); // Assuming car_id is in the first column
                    DatabaseHelper dbHelper = null;
                    if (dbHelper.deleteCar(carId)) {
                        ((DefaultTableModel) carsTable.getModel()).removeRow(selectedRow);
                        CustomOptionPane.showDoneDialog(null, "Car deleted successfully!", "Done");
                    } else {
                        CustomOptionPane.showErrorDialog(null, "Failed to delete the car.");
                    }
                } else {
                    CustomOptionPane.showMsgDialog(null, "Please select a row to delete.", "Warning");
                }
            });

            JButton saveButton = new JButton("Save");
            saveButton.setFont(new Font("Poppins", Font.BOLD, 14));
            saveButton.setForeground(new Color(57, 62, 70));
            saveButton.setBackground(new Color(255, 211, 105));
            saveButton.setFocusPainted(false);
            saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            saveButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                    createEmptyBorder(0, 5, 0, 5)));
            buttonsPanel.add(saveButton);

            saveButton.addActionListener(e -> {
                int selectedRow = carsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Get the modified data from the table
                    String carId = carsTable.getValueAt(selectedRow, 0).toString();
                    String brand = carsTable.getValueAt(selectedRow, 1).toString();
                    String model = carsTable.getValueAt(selectedRow, 2).toString();
                    String type = carsTable.getValueAt(selectedRow, 3).toString();
                    Double cost = Double.parseDouble(carsTable.getValueAt(selectedRow, 4).toString());
                    String status = carsTable.getValueAt(selectedRow, 5).toString();

                    // Update the database
                    DatabaseHelper dbHelper = null;
                    try {
                        dbHelper = new DatabaseHelper();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (dbHelper.updateCar(carId, brand, model, type, cost, status)) {
                        CustomOptionPane.showDoneDialog(this, "Car information saved successfully!", "Done");
                    } else {
                        CustomOptionPane.showErrorDialog(this, "Failed to save car information.");
                    }
                } else {
                    CustomOptionPane.showMsgDialog(this, "No row selected to save.", "Warning");
                }
            });

        JButton editButton = new JButton("Edit");
        editButton.setFont(new Font("Poppins", Font.BOLD, 14));
        editButton.setForeground(new Color(57, 62, 70));
        editButton.setBackground(new Color(255, 211, 105));
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                createEmptyBorder(0, 5, 0, 5)));
        buttonsPanel.add(editButton);

        editButton.addActionListener(e -> {
            int selectedRow = carsTable.getSelectedRow();

            if (selectedRow >= 0) {
                // Get the current DefaultTableModel
                DefaultTableModel model = (DefaultTableModel) carsTable.getModel();

                // Override isCellEditable to enable editing for all cells in the selected row
                DefaultTableModel newModel = new DefaultTableModel(model.getDataVector(), getColumnIdentifiers(model)) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return row == selectedRow; // Enable editing only for the selected row
                    }
                };

                // Update the table's model
                carsTable.setModel(newModel);
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 0; i < carsTable.getColumnCount(); i++) {
                    carsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

                // Allow editing selected row
                CustomOptionPane.showDoneDialog(this, "You can now edit the selected row. Click Save to update.", "Edit Mode");
                carsTable.editCellAt(selectedRow, carsTable.getSelectedColumn());

            } else {
                CustomOptionPane.showErrorDialog(this, "No row selected!");
            }
        });


        // Add the buttons panel to the container
        containerPanel.add(buttonsPanel);

        // Vehicle Listings Search Bar
        JPanel vehicleSearchPanel = new JPanel();
        vehicleSearchPanel.setLayout(new BoxLayout(vehicleSearchPanel, BoxLayout.X_AXIS));
        vehicleSearchPanel.setBackground(new Color(57, 62, 70));
        vehicleSearchPanel.setBorder(BorderFactory.createEmptyBorder(0, 300, 10, 334)); // Padding for width adjustment

        JLabel vehicleSearchIcon = new JLabel(new ImageIcon("resources/yellow 50px/search.png")); // Replace with the actual icon path
        vehicleSearchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        vehicleSearchIcon.setAlignmentY(Component.CENTER_ALIGNMENT);

        JTextField vehicleSearchField = new JTextField("Search");
        vehicleSearchField.setFont(new Font("Poppins", Font.BOLD, 14));
        vehicleSearchField.setBackground(new Color(57,62,70));
        vehicleSearchField.setForeground(Color.GRAY);
        vehicleSearchField.setPreferredSize(new Dimension(400, 32));
        vehicleSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Stretchable width
        vehicleSearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,211,105), 1, true),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        vehicleSearchPanel.add(vehicleSearchIcon);
        vehicleSearchPanel.add(vehicleSearchField);

        // Set placeholder text and initial color
        vehicleSearchField.setText("Search");
        vehicleSearchField.setForeground(Color.GRAY);

        // Add Focus Listener
        vehicleSearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (vehicleSearchField.getText().equals("Search")) {
                    vehicleSearchField.setText("");
                    vehicleSearchField.setForeground(new Color(245, 245, 245));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (vehicleSearchField.getText().isEmpty()) {
                    vehicleSearchField.setText("Search");
                    vehicleSearchField.setForeground(Color.GRAY);
                } else {
                    vehicleSearchField.setForeground(new Color(245, 245, 245));
                }
            }
        });

        // Add row sorter for filtering
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        carsTable.setRowSorter(sorter);

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

        containerPanel.add(vehicleSearchPanel);


        JScrollPane scrollPane = new JScrollPane(carsTable);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide vertical scrollbar
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide horizontal scrollbar
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                createEmptyBorder(0, 0, 0, 0)));
        scrollPane.setBackground(new Color(238, 238, 238));

        // Add the scroll pane to the container panel
        containerPanel.add(scrollPane);

        // Add the container panel to the garageInfoPanel
        garageInfoPanel.setLayout(new BorderLayout());
        garageInfoPanel.add(containerPanel, BorderLayout.CENTER);

        // Load car data into the JTable
        loadCarData();

        // Customer Details Panel with JTable
        JPanel customerDetailsPanel = new JPanel(new BorderLayout());
        customerDetailsPanel.setBackground(new Color(57, 62, 70));
        customerDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                createEmptyBorder(0, 0, 0, 0)));

        // Create a container panel for the title and table
        JPanel customerContainerPanel = new JPanel();
        customerContainerPanel.setBackground(new Color(57, 62, 70));
        customerContainerPanel.setLayout(new BoxLayout(customerContainerPanel, BoxLayout.Y_AXIS));
        customerContainerPanel.setBackground(new Color(57, 62, 70));

        // Add the title to a separate wrapper panel
        JPanel customerTitleWrapperPanel = new JPanel();
        customerTitleWrapperPanel.setBackground(new Color(57, 62, 70)); // Set background for the title area
        customerTitleWrapperPanel.setLayout(new BoxLayout(customerTitleWrapperPanel, BoxLayout.Y_AXIS));
        customerTitleWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerTitleWrapperPanel.setBorder(createEmptyBorder(0, 0, 0, 0)); // Add some spacing

        JLabel customerTitle = new JLabel("Customer Data");
        customerTitle.setFont(new Font("Netflix Sans", Font.BOLD, 20));
        customerTitle.setForeground(new Color(255, 211, 105));
        customerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerTitle.setBorder(createEmptyBorder(10, 10, 10, 10)); // Add some spacing

        customerTitleWrapperPanel.add(customerTitle);
        customerContainerPanel.add(customerTitleWrapperPanel);

        // JTable to display customer data
        String[] customerColumnNames = {"Customer ID", "Name", "Password", "Phone", "Role"};
        customerTableModel = new DefaultTableModel(customerColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        customerTable = new JTable(customerTableModel);
        customerTable.setFont(new Font("Poppins", Font.BOLD, 14));
        customerTable.setRowHeight(30); // Increase vertical spacing
        customerTable.setBackground(new Color(245, 245, 245));
        customerTable.setShowGrid(true);
        customerTable.setGridColor(Color.LIGHT_GRAY);
        customerTable.setFillsViewportHeight(true); // Ensures table occupies the full viewport
        customerTable.setCellSelectionEnabled(true);
        customerTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                createEmptyBorder(5, 50, 5, 50)));

        // Make the column headers bold
        customerTable.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        customerTable.getTableHeader().setBackground(new Color(57, 62, 70));
        customerTable.getTableHeader().setForeground(new Color(255, 211, 105));
        customerTable.getTableHeader().setOpaque(true);

        // Center align the column data
        DefaultTableCellRenderer cusCenterRenderer = new DefaultTableCellRenderer();
        cusCenterRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < customerTable.getColumnCount(); i++) {
            customerTable.getColumnModel().getColumn(i).setCellRenderer(cusCenterRenderer);
        }

        //To Load the Customer Data in the table
        loadCustomerData();

        // Add buttons panel below the table
        JPanel customerButtonsPanel = new JPanel(new GridLayout(1, 3, 50, 50));
        customerButtonsPanel.setBackground(new Color(57, 62, 70));
        customerButtonsPanel.setBorder(createEmptyBorder(0, 30, 10, 30));

        JButton cusDeleteButton = new JButton("Delete");
        cusDeleteButton.setFont(new Font("Poppins", Font.BOLD, 14));
        cusDeleteButton.setForeground(new Color(57, 62, 70));
        cusDeleteButton.setBackground(new Color(255, 211, 105));
        cusDeleteButton.setFocusPainted(false);
        cusDeleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cusDeleteButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                createEmptyBorder(5, 5, 5, 5)));
        customerButtonsPanel.add(cusDeleteButton);

        cusDeleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) { // Ensure a row is selected
                String customerId = customerTable.getValueAt(selectedRow, 0).toString(); // Assuming customer_id is in the first column
                DatabaseHelper dbHelper = null;
                try {
                    dbHelper = new DatabaseHelper();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                if (dbHelper.deleteCustomer(customerId)) {
                    ((DefaultTableModel) customerTable.getModel()).removeRow(selectedRow);
                    CustomOptionPane.showDoneDialog(null, "Customer deleted successfully!","Done");
                } else {
                    CustomOptionPane.showErrorDialog(null, "Failed to delete the customer.");
                }
            } else {
                CustomOptionPane.showMsgDialog(null, "Please select a row to delete.","Warning");
            }
        });

        JButton cusSaveButton = new JButton("Save");
        cusSaveButton.setFont(new Font("Poppins", Font.BOLD, 14));
        cusSaveButton.setForeground(new Color(57, 62, 70));
        cusSaveButton.setBackground(new Color(255, 211, 105));
        cusSaveButton.setFocusPainted(false);
        cusSaveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cusSaveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                createEmptyBorder(0, 5, 0, 5)));
        customerButtonsPanel.add(cusSaveButton);

        cusSaveButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get the modified data from the table
                String customerId = customerTable.getValueAt(selectedRow, 0).toString();
                String name = customerTable.getValueAt(selectedRow, 1).toString();
                String password = customerTable.getValueAt(selectedRow, 2).toString();
                String phone = customerTable.getValueAt(selectedRow, 3).toString();
                String role = customerTable.getValueAt(selectedRow, 4).toString();

                // Update the database
                DatabaseHelper dbHelper = null;
                try {
                    dbHelper = new DatabaseHelper();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                if (dbHelper.updateCustomer(customerId, name, password, phone, role)) {
                    CustomOptionPane.showDoneDialog(this, "Customer information saved successfully!","Done");
                } else {
                    CustomOptionPane.showErrorDialog(this, "Failed to save customer information.");
                }
            } else {
                CustomOptionPane.showMsgDialog(this, "No row selected to save.","Warning");
            }
        });

        JButton cusEditButton = new JButton("Edit");
        cusEditButton.setFont(new Font("Poppins", Font.BOLD, 14));
        cusEditButton.setForeground(new Color(57, 62, 70));
        cusEditButton.setBackground(new Color(255, 211, 105));
        cusEditButton.setFocusPainted(false);
        cusEditButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cusEditButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                createEmptyBorder(0, 5, 0, 5)));
        customerButtonsPanel.add(cusEditButton);

        cusEditButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();

            if (selectedRow >= 0) {
                // Store the original data for comparison
                Object[] originalData = new Object[customerTable.getColumnCount()];
                for (int i = 0; i < customerTable.getColumnCount(); i++) {
                    originalData[i] = customerTable.getValueAt(selectedRow, i);
                }

                // Enable editing for the selected row
                DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
                model = new DefaultTableModel(model.getDataVector(), getColumnIdentifiers(model)) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return row == selectedRow; // Enable editing only for the selected row
                    }
                };
                customerTable.setModel(model);

                // Reapply cell renderer
                for (int i = 0; i < customerTable.getColumnCount(); i++) {
                    customerTable.getColumnModel().getColumn(i).setCellRenderer(cusCenterRenderer);
                }

                CustomOptionPane.showDoneDialog(this, "You can now edit the selected row. Click Save to update.", "Edit Mode");
                customerTable.editCellAt(selectedRow, customerTable.getSelectedColumn());
            } else {
                CustomOptionPane.showErrorDialog(this, "No row selected!");
            }
        });

        // Add the buttons panel to the container panel
        customerContainerPanel.add(customerButtonsPanel);



        // Vehicle Listings Search Bar
        JPanel cusSearchPanel = new JPanel();
        cusSearchPanel.setLayout(new BoxLayout(cusSearchPanel, BoxLayout.X_AXIS));
        cusSearchPanel.setBackground(new Color(57, 62, 70));
        cusSearchPanel.setBorder(BorderFactory.createEmptyBorder(0, 300, 10, 334)); // Padding for width adjustment

        JLabel cusSearchIcon = new JLabel(new ImageIcon("resources/yellow 50px/search.png")); // Replace with the actual icon path
        cusSearchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        cusSearchIcon.setAlignmentY(Component.CENTER_ALIGNMENT);

        JTextField cusSearchField = new JTextField("Search");
        cusSearchField.setFont(new Font("Poppins", Font.BOLD, 14));
        cusSearchField.setBackground(new Color(57,62,70));
        cusSearchField.setForeground(Color.GRAY);
        cusSearchField.setPreferredSize(new Dimension(400, 32));
        cusSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Stretchable width
        cusSearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,211,105), 1, true),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        cusSearchPanel.add(cusSearchIcon);
        cusSearchPanel.add(cusSearchField);

        // Set placeholder text and initial color
        cusSearchField.setText("Search");
        cusSearchField.setForeground(Color.GRAY);

        // Add row sorter for filtering
        TableRowSorter<DefaultTableModel> customerSorter = new TableRowSorter<>(customerTableModel);
        customerTable.setRowSorter(customerSorter);

        // Add Focus Listener
        cusSearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cusSearchField.getText().equals("Search")) {
                    cusSearchField.setText("");
                    cusSearchField.setForeground(new Color(245, 245, 245));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cusSearchField.getText().isEmpty()) {
                    cusSearchField.setText("Search");
                    cusSearchField.setForeground(Color.GRAY);
                } else {
                    cusSearchField.setForeground(new Color(245, 245, 245));
                }
            }
        });

        // Add the search filter functionality
        cusSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterCustomerTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterCustomerTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterCustomerTable();
            }

            private void filterCustomerTable() {
                String text = cusSearchField.getText();
                if (text.trim().isEmpty() || text.equals("Search")) {
                    customerSorter.setRowFilter(null);
                } else {
                    customerSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        //added in the Customer Details Container
        customerContainerPanel.add(cusSearchPanel);


        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide vertical scrollbar
        customerScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide horizontal scrollbar
        customerScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                createEmptyBorder(0, 0, 0, 0)));
        customerScrollPane.setBackground(new Color(238, 238, 238));

        // Add the scroll pane to the container panel
        customerContainerPanel.add(customerScrollPane);

        // Add the container panel to the customerDetailsPanel
        customerDetailsPanel.setLayout(new BorderLayout());
        customerDetailsPanel.add(customerContainerPanel, BorderLayout.CENTER);

        // Add cards to center panel
        centerPanel.add(garageInfoPanel, "GarageInfo");
        centerPanel.add(customerDetailsPanel, "CustomersDetails");

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for navigation buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 35, 20));
        bottomPanel.setMaximumSize(new Dimension(1400, 300)); // Adjust button section size
        bottomPanel.setBackground(new Color(238, 238, 238));
        bottomPanel.setBorder(createEmptyBorder(20, 120, 20, 120));

        JButton garageInfoButton = new JButton("Garage Info");
        garageInfoButton.setFont(new Font("Poppins", Font.BOLD, 18));
        garageInfoButton.setForeground(new Color(57, 62, 70));
        garageInfoButton.setBackground(new Color(255, 211, 105));
        garageInfoButton.setFocusPainted(false);
        garageInfoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        garageInfoButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                createEmptyBorder(5, 10, 5, 10)));

        JButton customerDetailsButton = new JButton("Customer Details");
        customerDetailsButton.setFont(new Font("Poppins", Font.BOLD, 18));
        customerDetailsButton.setForeground(new Color(57, 62, 70));
        customerDetailsButton.setBackground(new Color(255, 211, 105));
        customerDetailsButton.setFocusPainted(false);
        customerDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerDetailsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 2, true),
                createEmptyBorder(5, 10, 5, 10)));

        bottomPanel.add(garageInfoButton);
        bottomPanel.add(customerDetailsButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Action listeners for buttons to switch cards
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();

        garageInfoButton.addActionListener(e -> cardLayout.show(centerPanel, "GarageInfo"));

        customerDetailsButton.addActionListener(e -> cardLayout.show(centerPanel, "CustomersDetails"));

        // Should be at the Last
        setVisible(true);
    }

    private void loadCarData() {
        try {
            // Fetch car data from DatabaseHelper
            DatabaseHelper dbHelper = new DatabaseHelper();
            List<Object[]> carsData = dbHelper.fetchAllCars();

            // Clear existing rows
            tableModel.setRowCount(0);

            // Add rows to the table model
            for (Object[] rowData : carsData) {
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomOptionPane.showErrorDialog(this, "Failed to load car data.");
        }
    }

    private void loadCustomerData() {
        try {
            // Fetch customer data from DatabaseHelper
            DatabaseHelper dbHelper = new DatabaseHelper();
            List<Object[]> customersData = dbHelper.fetchAllCustomers();

            // Clear existing rows
            customerTableModel.setRowCount(0);

            // Add rows to the table model
            for (Object[] rowData : customersData) {
                customerTableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomOptionPane.showErrorDialog(this, "Failed to load customer data.");
        }
    }

    private Vector<String> getColumnIdentifiers(DefaultTableModel model) {
        Vector<String> columnNames = new Vector<>();
        for (int i = 0; i < model.getColumnCount(); i++) {
            columnNames.add(model.getColumnName(i));
        }
        return columnNames;
    }
}

