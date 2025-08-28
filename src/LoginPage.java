import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LoginPage extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public LoginPage() {
        setTitle("MotoRentals | Login & Registration");
        setSize(650, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // CardLayout to switch between Login and Registration panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add the panels to CardLayout
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegistrationPanel(), "Register");

        add(mainPanel);
        setVisible(true);
    }

    // Login Panel
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(238,238,238));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setForeground(new Color(34,40,49));
        titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userField = createTextField("Enter Username");
        userField.setFont(new Font("Poppins", Font.BOLD, 15));
        JPasswordField passwordField = createPasswordField("Enter Password");
        JCheckBox showPasswordCheckbox = createShowPasswordCheckbox(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(300, 45));
        loginButton.setFont(new Font("Poppins", Font.BOLD, 18));
        loginButton.setForeground(new Color(57,62,70));
        loginButton.setBackground(new Color(255,211,105));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // In LoginPage.java
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validation check for empty fields
            if (username.equals("Enter Username") || password.equals("Enter Password")) {
                CustomOptionPane.showErrorDialog(this, "All fields are required!");
            } else {
                // Get the role of the user
                String role = DatabaseHelper.validateLogin(username, password);

                if (role == null) {
                    CustomOptionPane.showErrorDialog(this, "Invalid Username or Password");
                } else {
                    CustomOptionPane.showDoneDialog(this,"Login successful!", "Login Status");
                    dispose(); // Close the login frame

                    if (role.equalsIgnoreCase("Admin")) {
                        HomePageAdmin homePageAdmin = new HomePageAdmin(username);
                    } else if (role.equalsIgnoreCase("User")) {
                        HomePageUser homePageUser = new HomePageUser(username);
                    }
                }
            }
        });


        // Register Link
        JLabel registerLink = new JLabel("<html><u>Don't have an account? Register here</u></html>");
        registerLink.setFont(new Font("Netflix Sans", Font.PLAIN, 12));
        registerLink.setForeground(new Color(34,40,49));
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Switch to Register panel when link is clicked
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Register");
            }
        });

        // Adding components with spacing
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        loginPanel.add(userField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(passwordField);
        loginPanel.add(showPasswordCheckbox);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(registerLink);

        return loginPanel;
    }



    // Registration Panel
    private JPanel createRegistrationPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBackground(new Color(238,238,238));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        // Title Label
        JLabel titleLabel = new JLabel("Create an Account as User");
        titleLabel.setForeground(new Color(34,40,49));
        titleLabel.setFont(new Font("Netflix Sans", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerPanel.add(titleLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Form Fields
        JTextField usernameField = createTextField("Enter Username");
        usernameField.setFont(new Font("Poppins", Font.BOLD, 15));
        JTextField phoneField = createTextField("Enter Phno.");
        phoneField.setFont(new Font("Poppins", Font.BOLD, 15));
        JPasswordField passwordField = createPasswordField("Enter Password");
        passwordField.setFont(new Font("Poppins", Font.BOLD, 15));
        JCheckBox showPasswordCheckbox = createShowPasswordCheckbox(passwordField);

        // Button Panel to hold "Sign Up as Admin/User" and "Register" buttons side by side
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(238,238,238));

        // User/Admin Toggle Button
        JButton toggleRoleButton = new JButton("Admin");
        setupButton(toggleRoleButton);
        toggleRoleButton.setFont(new Font("Poppins", Font.BOLD, 18));
        toggleRoleButton.setBackground(new Color(255,211,105));
        toggleRoleButton.setForeground(new Color(57,62,70));
        toggleRoleButton.setPreferredSize(new Dimension(200, 45));
        toggleRoleButton.setMaximumSize(new Dimension(200, 45));
        toggleRoleButton.setFocusPainted(false);
        toggleRoleButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Toggle between User and Admin signup modes
        toggleRoleButton.addActionListener(new ActionListener() {
            private boolean isAdmin = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                isAdmin = !isAdmin;
                toggleRoleButton.setText(isAdmin ? "User" : "Admin");
                titleLabel.setText(isAdmin ? "Create an Account as Admin" : "Create an Account as User");
            }
        });

        // Register Button
        JButton registerButton = new JButton("Register");
        setupButton(registerButton);
        registerButton.setPreferredSize(new Dimension(200, 45));
        registerButton.setMaximumSize(new Dimension(200, 45));
        registerButton.setFont(new Font("Poppins", Font.BOLD, 18));
        registerButton.setBackground(new Color(255,211,105));
        registerButton.setForeground(new Color(57,62,70));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String phoneNo = phoneField.getText().toString();
                String role = toggleRoleButton.getText().toString() == "Admin" ? "User" : "Admin";

                if (username.equals("Enter Username") || phoneNo.equals("Enter Phno.") || password.equals("Enter Password")){
                    CustomOptionPane.showErrorDialog(LoginPage.this,"All fields are required!");
                } else {
                    DatabaseHelper databaseHelper = null;
                    try {
                        databaseHelper = new DatabaseHelper();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (databaseHelper.registerUser(username, password, phoneNo, role)){
                        CustomOptionPane.showDoneDialog(LoginPage.this, "Account Created!\nPlease Login", "Done");
                        cardLayout.show(mainPanel, "Login");  // Redirect to Login panel
                    }
                    else {
                        cardLayout.show(mainPanel, "Login");  // Redirect to Login panel
                    }

                }
            }
        });


        // Adding the buttons to the button panel
        buttonPanel.add(toggleRoleButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Space between buttons
        buttonPanel.add(registerButton);

        // Adding components to the register panel
        registerPanel.add(usernameField);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(phoneField);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(passwordField);
        registerPanel.add(showPasswordCheckbox);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        registerPanel.add(buttonPanel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Back to "Login" Link
        JLabel loginLink = new JLabel("<html><u>Already have an account? Login here</u></html>");
        loginLink.setForeground(new Color(34,40,49));
        loginLink.setFont(new Font("Netflix Sans", Font.BOLD, 12));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Switch back to "Login" panel when link is clicked
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Login");
            }
        });

        // Center align the login link
        registerPanel.add(loginLink);

        return registerPanel;
    }

    // Utility methods to create reusable UI components
    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(15);
        textField.setMaximumSize(new Dimension(300, 40));
        textField.setFont(new Font("Poppins", Font.BOLD, 15));
        textField.setForeground(new Color(84, 86, 95));
        textField.setText(placeholder);
        textField.setBackground(new Color(245, 245, 245));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Focus listener for placeholder behavior
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(new Color(34,40,49));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(84, 86, 95));
                }
            }
        });
        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Poppins", Font.BOLD, 15));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setForeground(new Color(84, 86, 95));
        passwordField.setText(placeholder);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Focus listener for placeholder behavior
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(new Color(34,40,49));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(new Color(84, 86, 95));
                }
            }
        });
        return passwordField;
    }

    private JCheckBox createShowPasswordCheckbox(JPasswordField passwordField) {
        JCheckBox checkBox = new JCheckBox("Show Password");
        checkBox.setBackground(new Color(238,238,238));
        checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBox.setFont(new Font("Netflix Sans", Font.BOLD, 10));
        checkBox.setForeground(new Color(57,62,70));
        checkBox.setFocusPainted(false);
        checkBox.setBorder(BorderFactory.createEmptyBorder(8, 200, 0, 0));

        // Toggle password visibility
        checkBox.addActionListener(e -> passwordField.setEchoChar(checkBox.isSelected() ? '\0' : '•'));
        return checkBox;
    }

    private void setupButton(JButton button) {
        button.setFont(new Font("Poppins", Font.BOLD, 14));
        button.setBackground(new Color(255,211,105));
        button.setForeground(new Color(84, 86, 95));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(300, 40));
    }

//    public static void showDialog(Component parent, String message, String title) {
//        // Custom JOptionPane settings
//        UIManager.put("OptionPane.messageFont", new Font("Netflix Sans", Font.BOLD, 14));
//        UIManager.put("OptionPane.messageForeground", new Color(34,40,49));
//        UIManager.put("Button.background", new Color(255,211,105));
//        UIManager.put("Button.foreground", new Color(84, 86, 95));
//        UIManager.put("Button.focusPainted",false);
//        UIManager.put("Button.font", new Font("Netflix Sans", Font.BOLD, 12));
//        UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 70, 5, 70));;
//
//        // Use PLAIN_MESSAGE to avoid any default icons
//        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
//
//        // Reset UIManager settings after showing the dialog
//        UIManager.put("OptionPane.messageFont", null);
//        UIManager.put("OptionPane.messageForeground", null);
//        UIManager.put("Button.background", null);
//        UIManager.put("Button.foreground", null);
//        UIManager.put("Button.focusPainted", null);
//        UIManager.put("Button.font", null);
//        UIManager.put("Button.focusPainted", null);
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}
