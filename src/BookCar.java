import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookCar extends JFrame {
    private JTable table;
    private JTextField t1, t2, t3, t4, t5;
    private JTextField cusTxtFeild, phnoFeild;
    private DateTextField bookingDateFeild;
    private CustomNumberSpinner noOfDaysFeild;
    private JLabel reslSr1, reslSr2, reslSr3, reslSr4, reslSr5;
    private JLabel rideNoPlate, rideBM, rentalCostValue, bookedForDaysValue, totalRentCost;
    private boolean rideConfirmed = false;


    public BookCar(String username) {
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
                CustomOptionPane.showErrorDialog(this, "Role not found! Please contact support.");
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

        JLabel titleLabel = new JLabel("Rent a Ride");
        titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255,211,105));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center panel for card layouts
        JPanel centerPanel = new JPanel(new CardLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(53, 300, 50, 300));

        // Card 1: Choose Ride
        JPanel chooseRide = new JPanel(new GridLayout(1,2,10,0));
        chooseRide.setBackground(new Color(238,238,238));
        chooseRide.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Left Section (for labels & text fields)
        JPanel leftSection1 = new JPanel(null);
        leftSection1.setBackground(new Color(238, 238, 238));
        leftSection1.setBounds(20, 20, 570, 545);
        leftSection1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Adding Heading "Car Details"
        JLabel heading = new JLabel("Choose Your Ride");
        heading.setFont(new Font("Netflix Sans", Font.BOLD, 28));
        heading.setForeground(new Color(57, 62, 70));
        heading.setBounds(155, 35, 250, 40);
        leftSection1.add(heading);

        // Adding labels & text fields inside leftSection
        int labelX = 60, labelY = 120, labelWidth = 120, labelHeight = 27;
        int fieldX = 180, fieldWidth = 300, fieldHeight = 30;
        int gap = 40;

        JLabel l1 = new JLabel("License No.");
        l1.setBounds(labelX, labelY, labelWidth, labelHeight);
        l1.setFont(new Font("Poppins", Font.BOLD, 17));
        l1.setForeground(new Color(57, 62, 70));

        leftSection1.add(l1);

        t1 = new UppercaseTextField(10);
        t1.setBounds(fieldX, labelY, fieldWidth, fieldHeight);
        t1.setFont(new Font("Poppins", Font.BOLD, 15));
        t1.setBackground(new Color(245,245,245));
        t1.setFocusable(false);
        t1.setForeground(new Color(84, 86, 95));
        t1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection1.add(t1);

        JLabel l2 = new JLabel("Brand");
        l2.setBounds(labelX, labelY + (labelHeight + gap), labelWidth, labelHeight);
        l2.setFont(new Font("Poppins", Font.BOLD, 17));
        l2.setForeground(new Color(57, 62, 70));

        leftSection1.add(l2);

        t2 = new JTextField();
        t2.setBounds(fieldX, labelY + (labelHeight + gap), fieldWidth, fieldHeight);
        t2.setFont(new Font("Poppins", Font.BOLD, 15));
        t2.setBackground(new Color(245,245,245));
        t2.setFocusable(false);
        t2.setForeground(new Color(84, 86, 95));
        t2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection1.add(t2);

        JLabel l3 = new JLabel("Model");
        l3.setBounds(labelX, labelY + 2 * (labelHeight + gap), labelWidth, labelHeight);
        l3.setFont(new Font("Poppins", Font.BOLD, 17));
        l3.setForeground(new Color(57, 62, 70));

        leftSection1.add(l3);

        t3 = new JTextField();
        t3.setBounds(fieldX, labelY + 2 * (labelHeight + gap), fieldWidth, fieldHeight);
        t3.setFont(new Font("Poppins", Font.BOLD, 15));
        t3.setBackground(new Color(245,245,245));
        t3.setFocusable(false);
        t3.setForeground(new Color(84, 86, 95));
        t3.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection1.add(t3);

        JLabel l4 = new JLabel("Type");
        l4.setBounds(labelX, labelY + 3 * (labelHeight + gap), labelWidth, labelHeight);
        l4.setFont(new Font("Poppins", Font.BOLD, 17));
        l4.setForeground(new Color(57, 62, 70));

        leftSection1.add(l4);

        t4 = new JTextField();
        t4.setBounds(fieldX, labelY + 3 * (labelHeight + gap), fieldWidth, fieldHeight);
        t4.setFont(new Font("Poppins", Font.BOLD, 15));
        t4.setBackground(new Color(245,245,245));
        t4.setFocusable(false);
        t4.setForeground(new Color(84, 86, 95));
        t4.setFocusable(false);

        t4.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection1.add(t4);

        JLabel l5 = new JLabel("Cost");
        l5.setBounds(labelX, labelY + 4 * (labelHeight + gap), labelWidth, labelHeight);
        l5.setFont(new Font("Poppins", Font.BOLD, 17));
        l5.setForeground(new Color(57, 62, 70));

        leftSection1.add(l5);

        t5 = new IntegerTextField(10);
        t5.setBounds(fieldX, labelY + 4 * (labelHeight + gap), fieldWidth, fieldHeight);
        t5.setFont(new Font("Poppins", Font.BOLD, 15));
        t5.setBackground(new Color(245,245,245));
        t5.setFocusable(false);
        t5.setForeground(new Color(84, 86, 95));
        t5.setText("0"); // Set default value when the page loads
        t5.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        leftSection1.add(t5);

        JButton clrBtn = new JButton("Clear Data");
        clrBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        clrBtn.setBounds(80,448,183,40);
        clrBtn.setForeground(new Color(57,62,70));
        clrBtn.setBackground(new Color(255,211,105));
        clrBtn.setFocusPainted(false);
        clrBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clrBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        leftSection1.add(clrBtn);

        clrBtn.addActionListener(e -> {
            t1.setText("");
            t1.setBackground(new Color(245,245,245));
            t2.setText("");
            t2.setBackground(new Color(245,245,245));
            t3.setText("");
            t3.setBackground(new Color(245,245,245));
            t4.setText("");
            t4.setBackground(new Color(245,245,245));
            t5.setText("0");
            t5.setBackground(new Color(245,245,245));

            rideConfirmed = false;
        });



        JButton selectBtn = new JButton("Confirm Ride");
        selectBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        selectBtn.setBounds(300,448,183,40);
        selectBtn.setForeground(new Color(57,62,70));
        selectBtn.setBackground(new Color(255,211,105));
        selectBtn.setFocusPainted(false);
        selectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        leftSection1.add(selectBtn);

        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rideConfirmed = true; // locks the text fields
                if
                (
                        t1.getText().trim().isEmpty() ||
                        t2.getText().trim().isEmpty() ||
                        t3.getText().trim().isEmpty() ||
                        t4.getText().trim().isEmpty() ||
                        t5.getText().trim().isEmpty() ||
                        t5.getText().trim().equals("0")
                ) {
                    CustomOptionPane.showMsgDialog(BookCar.this,"Please Select the Ride from the table", "Ride Not Selected");
                    rideConfirmed = false; /* locks the text fields */
                    t1.setBackground(new Color(245,245,245));
                    t2.setBackground(new Color(245,245,245));
                    t3.setBackground(new Color(245,245,245));
                    t4.setBackground(new Color(245,245,245));
                    t5.setBackground(new Color(245,245,245));
                }
                else {
                    t1.setBackground(new Color(230, 230, 230));
                    t2.setBackground(new Color(230, 230, 230));
                    t3.setBackground(new Color(230, 230, 230));
                    t4.setBackground(new Color(230, 230, 230));
                    t5.setBackground(new Color(230, 230, 230));

                    reslSr1.setText(t1.getText());
                    reslSr2.setText(t2.getText());
                    reslSr3.setText(t3.getText());
                    reslSr4.setText(t4.getText());
                    reslSr5.setText(t5.getText()+"/-");

                    rideNoPlate.setText(t1.getText());
                    rideBM.setText(t2.getText()+"("+t3.getText()+")");

                    noOfDaysFeild.setValueChangedListener(() -> {
                        bookedForDaysValue.setText(noOfDaysFeild.getValue() + " Days");
                        updateTotalRent();
                    });


                    int noOfDays = noOfDaysFeild.getValue();

                    rentalCostValue.setText(t5.getText()+"/-");
                    bookedForDaysValue.setText(String.valueOf(noOfDays)+" Days");


                    // t5 might be a text field, so parse safely:
                    double rentalCostPerDay;
                    try {
                        rentalCostPerDay = Double.parseDouble(t5.getText());
                    } catch(NumberFormatException ex) {
                        rentalCostPerDay = 0;
                    }
                    double totalCost = noOfDays * rentalCostPerDay;

                    // Update label
                    totalRentCost.setText(String.format("%.2f/-", totalCost));

                    t5.getDocument().addDocumentListener(new DocumentListener() {
                        public void insertUpdate(DocumentEvent e) { updateTotalRent(); }
                        public void removeUpdate(DocumentEvent e) { updateTotalRent(); }
                        public void changedUpdate(DocumentEvent e) { updateTotalRent(); }
                    });



                }


            }
        });


        chooseRide.add(leftSection1);

        // Right Section (for rectangles - to be modified later)
        JPanel rightSection = new JPanel();
        rightSection.setLayout(null);
        rightSection.setBackground(new Color(238,238,238));
        rightSection.setBounds(600, 20, 570, 545);
        rightSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Title Label
        JLabel rightSecHeading = new JLabel("Available Rides", SwingConstants.CENTER);
        rightSecHeading.setForeground(new Color(57,62,70));
        rightSecHeading.setFont(new Font("Netflix Sans", Font.BOLD, 24));
        rightSecHeading.setBounds(0, 10, rightSection.getWidth(), 30);
        rightSection.add(rightSecHeading);

        // Search Bar Panel
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(new Color(238,238,238));
        searchPanel.setBounds((rightSection.getWidth() - 325) / 2, 50, 330, 35);

        JLabel searchIcon = new JLabel(new ImageIcon("resources/Icons 50px/search.png")); // <-- replace with your icon path
        searchIcon.setBounds(5, 4, 25, 25);
        searchPanel.add(searchIcon);

        JTextField searchField = new JTextField("Search");
        searchField.setFont(new Font("Poppins", Font.BOLD, 14));
                searchField.setBounds(35, 2, 260, 30);
        searchField.setCaretColor(new Color(34,40,49));
        searchField.setBackground(new Color(245,245,245));

        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Stretchable width
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
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

        rightSection.add(searchPanel);

        String[] columnNames = {"No Plate", "Brand", "Model", "Type", "Cost"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        // Table Styling (like earlier)
        table.setRowHeight(30);
        table.setFont(new Font("Netflix Sans", Font.PLAIN, 12));
        table.setFocusable(false);
        table.setBackground(new Color(245,245,245));
        table.setGridColor(new Color(222, 222, 222));
        table.setSelectionBackground(new Color(255,211,105));
        table.setSelectionForeground(new Color(57,62,70));

        // Make the column headers bold
        table.getTableHeader().setFont(new Font("Netflix Sans Black", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(57,62,70));
        table.getTableHeader().setForeground(new Color(255,211,105));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(57,62,70)));
        table.getTableHeader().setOpaque(true);

        // Center align the column data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // For PK "License_No" column
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // For "Brand" column
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // For "Model" column
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // For "Type" column
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // For "Cost" column
        //Loads the Table Data
        loadAvailableCars();

        // Add MouseListener for double-click
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!rideConfirmed && e.getClickCount() == 2) { // only if not confirmed
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);

                        String noPlate = table.getModel().getValueAt(modelRow, 0).toString();
                        String brand = table.getModel().getValueAt(modelRow, 1).toString();
                        String carModel = table.getModel().getValueAt(modelRow, 2).toString();
                        String carType = table.getModel().getValueAt(modelRow, 3).toString();
                        Object costObj = table.getModel().getValueAt(modelRow, 4);
                        double costValue = (costObj instanceof Number)
                                ? ((Number) costObj).doubleValue()
                                : Double.parseDouble(costObj.toString());
                        String costStr = (costValue == Math.floor(costValue))
                                ? String.format("%.0f", costValue)
                                : String.format("%.2f", costValue);

                        t1.setText(noPlate);
                        t2.setText(brand);
                        t3.setText(carModel);
                        t4.setText(carType);
                        t5.setText(costStr);
                    }
                }
            }
        });


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


        // Scroll Pane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 100, rightSection.getWidth() - 5, 412);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black,1,false)); // top padding
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide vertical scrollbar
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide horizontal scrollbarw        w

        rightSection.add(scrollPane);

        // Add to main panel
        chooseRide.add(rightSection);


        // Card 2: Book Ride Panel
        JPanel bookRidePanel = new JPanel(new GridLayout(1,2,10,0));
        bookRidePanel.setBackground(new Color(238,238,238));
        bookRidePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JPanel leftSection2 = new JPanel(null);
        leftSection2.setBackground(new Color(238, 238, 238));
        leftSection2.setBounds(20, 20, 570, 545);
        leftSection2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Adding Heading "Selected Ride Details"
        JLabel selectedRideHeading = new JLabel("Selected Ride Details");
        selectedRideHeading.setFont(new Font("Netflix Sans", Font.BOLD, 32));
        selectedRideHeading.setForeground(new Color(57, 62, 70));
        selectedRideHeading.setBounds(115, 35, 350, 40);
        leftSection2.add(selectedRideHeading);

        // Adding labels & Display labels inside leftSection of Book Ride Car
        int labelSrX = 70, labelSrY = 130, labelSrWidth = 120, labelSrHeight = 27;
        int resLableSrX = 200, resLableSrWidth = 290, resLableSrHeight = 30;
        int gapSr = 50;

        JLabel lSr1 = new JLabel("License No.");
        lSr1.setBounds(labelSrX, labelSrY, labelSrWidth, labelSrHeight);
        lSr1.setFont(new Font("Poppins", Font.BOLD, 17));
        lSr1.setForeground(new Color(57, 62, 70));

        leftSection2.add(lSr1);

        reslSr1 = new JLabel("No Data");
        reslSr1.setBounds(resLableSrX, labelSrY, resLableSrWidth, resLableSrHeight);
        reslSr1.setFont(new Font("Poppins", Font.BOLD, 24));
        reslSr1.setBackground(new Color(245,245,245));
        reslSr1.setFocusable(false);
        reslSr1.setForeground(new Color(84, 86, 95));
        reslSr1.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        leftSection2.add(reslSr1);

        JLabel lSr2 = new JLabel("Brand");
        lSr2.setBounds(labelSrX, labelSrY + (labelSrHeight + gapSr), labelSrWidth, labelSrHeight);
        lSr2.setFont(new Font("Poppins", Font.BOLD, 17));
        lSr2.setForeground(new Color(57, 62, 70));

        leftSection2.add(lSr2);

        reslSr2 = new JLabel("No Data");
        reslSr2.setBounds(resLableSrX, labelSrY + (labelSrHeight + gapSr), resLableSrWidth, resLableSrHeight);
        reslSr2.setFont(new Font("Poppins", Font.BOLD, 24));
        reslSr2.setBackground(new Color(245,245,245));
        reslSr2.setFocusable(false);
        reslSr2.setForeground(new Color(84, 86, 95));
        reslSr2.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        leftSection2.add(reslSr2);

        JLabel lSr3 = new JLabel("Model");
        lSr3.setBounds(labelSrX, labelSrY + 2 * (labelSrHeight + gapSr), labelSrWidth, labelSrHeight);
        lSr3.setFont(new Font("Poppins", Font.BOLD, 17));
        lSr3.setForeground(new Color(57, 62, 70));

        leftSection2.add(lSr3);

        reslSr3 = new JLabel("No Data");
        reslSr3.setBounds(resLableSrX, labelSrY + 2 * (labelSrHeight + gapSr), resLableSrWidth, resLableSrHeight);
        reslSr3.setFont(new Font("Poppins", Font.BOLD, 24));
        reslSr3.setBackground(new Color(245,245,245));
        reslSr3.setFocusable(false);
        reslSr3.setForeground(new Color(84, 86, 95));
        reslSr3.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        leftSection2.add(reslSr3);

        JLabel lSr4 = new JLabel("Type");
        lSr4.setBounds(labelSrX, labelSrY + 3 * (labelSrHeight + gapSr), labelSrWidth, labelSrHeight);
        lSr4.setFont(new Font("Poppins", Font.BOLD, 17));
        lSr4.setForeground(new Color(57, 62, 70));

        leftSection2.add(lSr4);

        reslSr4 = new JLabel("No Data");
        reslSr4.setBounds(resLableSrX, labelSrY + 3 * (labelSrHeight + gapSr), resLableSrWidth, resLableSrHeight);
        reslSr4.setFont(new Font("Poppins", Font.BOLD, 24));
        reslSr4.setBackground(new Color(245,245,245));
        reslSr4.setFocusable(false);
        reslSr4.setForeground(new Color(84, 86, 95));
        reslSr4.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        leftSection2.add(reslSr4);

        JLabel lSr5 = new JLabel("Cost");
        lSr5.setBounds(labelSrX, labelSrY + 4 * (labelSrHeight + gapSr), labelSrWidth, labelSrHeight);
        lSr5.setFont(new Font("Poppins", Font.BOLD, 17));
        lSr5.setForeground(new Color(57, 62, 70));

        leftSection2.add(lSr5);

        reslSr5 = new JLabel("No Data");
        reslSr5.setBounds(resLableSrX, labelSrY + 4 * (labelSrHeight + gapSr), resLableSrWidth, resLableSrHeight);
        reslSr5.setFont(new Font("Poppins", Font.BOLD, 24));
        reslSr5.setBackground(new Color(245,245,245));
        reslSr5.setFocusable(false);
        reslSr5.setForeground(new Color(84, 86, 95));
        reslSr5.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        leftSection2.add(reslSr5);


        bookRidePanel.add(leftSection2);

        JPanel rightSection2 = new JPanel(null);
        rightSection2.setLayout(null);
        rightSection2.setBackground(new Color(238,238,238));
        rightSection2.setBounds(600, 20, 570, 545);
        rightSection2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 40, 49), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Title Label
        JLabel rightSec2Heading = new JLabel("Booking By Days");
        rightSec2Heading.setForeground(new Color(57,62,70));
        rightSec2Heading.setFont(new Font("Netflix Sans", Font.BOLD, 32));
        rightSec2Heading.setBounds(155, 35, rightSection.getWidth(), 40);

        rightSection2.add(rightSec2Heading);

        // Adding labels & Display labels inside leftSection of Book Ride Car
        int labelBrX = 60, labelBrY = 100, labelBrWidth = 150, labelBrHeight = 27;
        int resLableBrX = 200, resLableBrWidth = 290, resLableBrHeight = 32;
        int gapBr = 25;

        JLabel lBr1 = new JLabel("Customer Name");
        lBr1.setBounds(labelBrX, labelBrY, labelBrWidth, labelBrHeight);
        lBr1.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        lBr1.setForeground(new Color(57, 62, 70));
        lBr1.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        rightSection2.add(lBr1);

        String role = DatabaseHelper.getUserRole(username);

        if (!"Admin".equalsIgnoreCase(role)) {
            // Regular user: show their own username as label (not editable)
            JLabel userCusLabel = new JLabel(username);
            userCusLabel.setFont(new Font("Poppins", Font.BOLD, 15));
            userCusLabel.setBounds(resLableBrX, labelBrY, resLableBrWidth, resLableBrHeight);
            userCusLabel.setForeground(new Color(34, 40, 49));
            rightSection2.add(userCusLabel);
            cusTxtFeild = null;  // Prevent accidental use
        } else {
            // Admin: show editable text field for customer name
            cusTxtFeild = new JTextField("");
            cusTxtFeild.setBounds(resLableBrX, labelBrY, resLableBrWidth, resLableBrHeight);
            cusTxtFeild.setEditable(true);
            cusTxtFeild.setCaretColor(Color.BLACK);
            cusTxtFeild.setFont(new Font("Poppins", Font.PLAIN, 15));
            cusTxtFeild.setBackground(new Color(245,245,245));
            cusTxtFeild.setForeground(new Color(34, 40, 49));
            cusTxtFeild.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                    BorderFactory.createEmptyBorder(0, 10, 0, 0)
            ));
            rightSection2.add(cusTxtFeild);
        }

        JLabel lBr2 = new JLabel("Phone No");
        lBr2.setBounds(labelBrX, labelBrY + (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        lBr2.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        lBr2.setForeground(new Color(57, 62, 70));
        lBr2.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));

        rightSection2.add(lBr2);

        phnoFeild = new IntegerTextField(10);
        phnoFeild.setBounds(resLableBrX, labelBrY + (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        phnoFeild.setFont(new Font("Poppins", Font.PLAIN, 15));
        phnoFeild.setBackground(new Color(245,245,245));
        phnoFeild.setCaretColor(Color.BLACK);
        phnoFeild.setForeground(new Color(34, 40, 49));
        phnoFeild.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));

        rightSection2.add(phnoFeild);

        JLabel lBr3 = new JLabel("Date Of Booking");
        lBr3.setBounds(labelBrX, labelBrY + 2 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        lBr3.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        lBr3.setForeground(new Color(57, 62, 70));
        lBr3.setBorder(BorderFactory.createEmptyBorder(0,0,2,0));

        rightSection2.add(lBr3);

        bookingDateFeild = new DateTextField();
        bookingDateFeild.setToToday();
        bookingDateFeild.setBounds(resLableBrX, labelBrY + 2 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        bookingDateFeild.setFont(new Font("Poppins", Font.BOLD, 15));
        bookingDateFeild.setBackground(new Color(245,245,245));
        bookingDateFeild.setFocusable(false);
        bookingDateFeild.setCaretColor(new Color(245,245,245));
        bookingDateFeild.setEditable(false);
        bookingDateFeild.setForeground(new Color(34, 40, 49));
        bookingDateFeild.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));

        rightSection2.add(bookingDateFeild);

        JLabel lBr4 = new JLabel("Ride Details");
        lBr4.setBounds(labelBrX, labelBrY + 3 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        lBr4.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        lBr4.setForeground(new Color(57, 62, 70));
        lBr4.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(lBr4);

        rideNoPlate = new JLabel();
        rideNoPlate.setBounds(200, labelBrY + 3 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        rideNoPlate.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        rideNoPlate.setForeground(new Color(57, 62, 70));
        rideNoPlate.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));

        rightSection2.add(rideNoPlate);

        rideBM = new JLabel();
        rideBM.setBounds(200, 115 + 3 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        rideBM.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        rideBM.setForeground(new Color(57, 62, 70));
        rideBM.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));

        rightSection2.add(rideBM);


        JLabel lBr5 = new JLabel("No of Days");
        lBr5.setBounds(labelBrX, labelBrY + 4 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        lBr5.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        lBr5.setForeground(new Color(57, 62, 70));
        lBr5.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(lBr5);

        noOfDaysFeild = new CustomNumberSpinner(1);
        noOfDaysFeild.setBounds(resLableBrX, labelBrY + 4 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        noOfDaysFeild.setFont(new Font("Poppins", Font.PLAIN, 15));
        noOfDaysFeild.setBackground(new Color(245,245,245));
        noOfDaysFeild.setForeground(new Color(34, 40, 49));
        noOfDaysFeild.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)));

        rightSection2.add(noOfDaysFeild);

        JLabel lBr6 = new JLabel("Estimate Costing");
        lBr6.setBounds(labelBrX, labelBrY + 5 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        lBr6.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        lBr6.setForeground(new Color(57, 62, 70));
        lBr6.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(lBr6);

        JLabel rentalCost = new JLabel("Rental Cost    =");
        rentalCost.setBounds(resLableBrX, labelBrY + 5 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        rentalCost.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        rentalCost.setForeground(new Color(57, 62, 70));
        rentalCost.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(rentalCost);

        rentalCostValue = new JLabel("XXXX"+"/-");
        rentalCostValue.setBounds(350, labelBrY + 5 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        rentalCostValue.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        rentalCostValue.setForeground(new Color(57, 62, 70));
        rentalCostValue.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(rentalCostValue);

        JLabel bookedForXdays = new JLabel("Booked for     =");
        bookedForXdays.setBounds(resLableBrX, 125 + 5 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        bookedForXdays.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        bookedForXdays.setForeground(new Color(57, 62, 70));
        bookedForXdays.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));

        rightSection2.add(bookedForXdays);

        bookedForDaysValue = new JLabel("XX"+" Days");
        bookedForDaysValue.setBounds(350, 125 + 5 * (labelBrHeight + gapBr), 100, resLableBrHeight);
        bookedForDaysValue.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        bookedForDaysValue.setForeground(new Color(57, 62, 70));
        bookedForDaysValue.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));


        rightSection2.add(bookedForDaysValue);

        JLabel totalRent = new JLabel("Total Rent       =");
        totalRent.setBounds(resLableBrX, 155 + 5 * (labelBrHeight + gapBr), resLableBrWidth, resLableBrHeight);
        totalRent.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        totalRent.setForeground(new Color(57, 62, 70));
        totalRent.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(totalRent);

        totalRentCost = new JLabel("XXXXX"+" /-");
        totalRentCost.setBounds(350, 155 + 5 * (labelBrHeight + gapBr), 100, resLableBrHeight);
        totalRentCost.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        totalRentCost.setForeground(new Color(57, 62, 70));
        totalRentCost.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        rightSection2.add(totalRentCost);

        JButton confirmBookingBtn = new JButton("Confirm Booking");
        confirmBookingBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        confirmBookingBtn.setBounds(190, labelBrY + 7 * (labelBrHeight + gapBr), 200, 35);
        confirmBookingBtn.setForeground(new Color(57,62,70));
        confirmBookingBtn.setBackground(new Color(255,211,105));
        confirmBookingBtn.setFocusPainted(false);
        confirmBookingBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmBookingBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        rightSection2.add(confirmBookingBtn);

        confirmBookingBtn.addActionListener(e -> {
            if (
                    ("Admin".equalsIgnoreCase(role) && cusTxtFeild.getText().trim().isEmpty()) ||
                    phnoFeild.getText().trim().isEmpty()
            ){
                CustomOptionPane.showMsgDialog(BookCar.this,"Please fill the required fields", "Information Required");
            }
            else {
                String cusName;
                if (!"Admin".equalsIgnoreCase(role)) {
                    cusName = username;  // Admin, name is username
                } else {
                    cusName = cusTxtFeild.getText().trim();
                }

                String phoneNo      = phnoFeild.getText().trim();
                String bookingDate  = bookingDateFeild.getText().trim();    // Should be in DD/MM/YY
                String noPlate      = rideNoPlate.getText().trim();
                String brandModel   = rideBM.getText().trim();
                int daysBooked      = noOfDaysFeild.getValue();

                String totalRentStr = totalRentCost.getText().replaceAll("[^\\d.]", "");
                int totalRentInt    = totalRentStr.isEmpty() ? 0 : (int) Double.parseDouble(totalRentStr);

                boolean inserted = DatabaseHelper.insertBill(
                        cusName,
                        phoneNo,
                        bookingDate,
                        noPlate,
                        brandModel,
                        daysBooked,
                        totalRentInt
                );

                if (inserted) {
                    boolean carUpdated = DatabaseHelper.updateCarStatus(noPlate, "Booked");
                    if (carUpdated) {
                        CustomOptionPane.showDoneDialog(BookCar.this, "Booking saved successfully!\nVehicle status set to Booked.", "Success");
                    } else {
                        CustomOptionPane.showMsgDialog(BookCar.this, "Booking saved, but Car status not updated.", "Something went wrong!!");
                    }
                } else {
                    CustomOptionPane.showErrorDialog(this,"Failed to save Booking.");
                }


                if ("Admin".equalsIgnoreCase(role)) {
                    cusTxtFeild.setText("");
                }
                phnoFeild.setText("");
                noOfDaysFeild.setValue(1);
            }

        });

        // Add rightSection2 to Book Ride Card
        bookRidePanel.add(rightSection2);

        // Add cards to center panel
        centerPanel.add(chooseRide, "ChooseRide");
        centerPanel.add(bookRidePanel, "BookYourRide");

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for navigation buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 35, 20));
        bottomPanel.setMaximumSize(new Dimension(1000, 300)); // Adjust button section size
        bottomPanel.setBackground(new Color(238, 238, 238));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 20, 200));

        JButton choseRideBtn = new JButton("Choose Ride");
        choseRideBtn.setFont(new Font("Poppins", Font.BOLD, 18));
        choseRideBtn.setForeground(new Color(57,62,70));
        choseRideBtn.setBackground(new Color(255,211,105));
        choseRideBtn.setFocusPainted(false);
        choseRideBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        choseRideBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        JButton bookRideButton = new JButton("Book Your Ride");
        bookRideButton.setFont(new Font("Poppins", Font.BOLD, 18));
        bookRideButton.setForeground(new Color(57,62,70));
        bookRideButton.setBackground(new Color(255,211,105));
        bookRideButton.setFocusPainted(false);
        bookRideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookRideButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        bottomPanel.add(choseRideBtn);
        bottomPanel.add(bookRideButton);


        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Action listeners for buttons to switch cards
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();

        choseRideBtn.addActionListener(e ->{
            if(rideConfirmed){
                loadAvailableCars();
            }
            cardLayout.show(centerPanel, "ChooseRide");

        });

        bookRideButton.addActionListener(e -> {
            if (!rideConfirmed) {
                // show a popup if ride not confirmed yet
                CustomOptionPane.showMsgDialog(
                        this,
                        "Please confirm your ride before booking.",
                        "Ride Not Selected"
                );
            } else {
                // proceed if ride is confirmed
                cardLayout.show(centerPanel, "BookYourRide");
            }

        });
        
        // Should be at the Last
        setVisible(true);
    }

    private void loadAvailableCars() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear old data

        ResultSet rs = DatabaseHelper.getAvailableCars();
        try {
            while (rs != null && rs.next()) {
                String noPlate = rs.getString("no_plate");
                String brand = rs.getString("brand");
                String carModel = rs.getString("model");
                String carType = rs.getString("category");
                double cost = rs.getDouble("cost");
                model.addRow(new Object[]{noPlate, brand, carModel, carType, cost});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalRent() {
        int noOfDays = noOfDaysFeild.getValue();
        double rentalCostPerDay;
        try {
            rentalCostPerDay = Double.parseDouble(t5.getText());
        } catch(NumberFormatException ex) {
            rentalCostPerDay = 0;
        }
        double totalCost = noOfDays * rentalCostPerDay;
        totalRentCost.setText(String.format("%.2f/-", totalCost));
    }





}