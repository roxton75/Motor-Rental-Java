import javax.swing.*;
import java.awt.*;

public class ProfileView extends JPanel {

    private boolean editClicked = false; // Flag to track if any edit button was clicked

    public ProfileView(String username) {
        // Set layout and background
        setLayout(new GridBagLayout()); // Centered layout with control
        setBackground(new Color(238,238,238));
        setPreferredSize(new Dimension(900, 700));
        setBorder(null);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Title Label
        JLabel profileTitle = new JLabel("Profile Info");
        profileTitle.setFont(new Font("Netflix Sans", Font.BOLD, 32));
        profileTitle.setForeground(new Color(34,40,49));
        gbc.gridx = 0; // Center horizontally
        gbc.gridy = 0; // Row 0
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        add(profileTitle, gbc);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null); // Absolute layout for fixed positions
        infoPanel.setPreferredSize(new Dimension(500, 180));
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(34,40,49), 2));
        infoPanel.setBackground((new Color(238,238,238)));

        // User info
        String[] labels = {"Name:", "Role:", "Phone No:", "Password:"};
        String[] values = {username, "User", "123-456-7890", "********"}; // Placeholder values

        DatabaseHelper.User user = DatabaseHelper.getUserDetails(username);
        if (user != null) {
            values[1] = user.role();       // Role
            values[2] = user.phoneNo();   // Phone
            values[3] = user.password();  // Password
        } else {
            CustomOptionPane.showMsgDialog(this, "Error fetching user details!","Error");
        }

        int yPosition = 10; // Starting Y position for fields
        JTextField phoneTextField = null;
        JTextField passwordTextField = null;

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Poppins", Font.BOLD, 18));
            label.setForeground((new Color(34,40,49)));
            label.setBounds(20, yPosition, 150, 30);
            infoPanel.add(label);

            if (i == 2 || i == 3) { // Phone No and Password (Editable)
                JTextField textField = new JTextField(values[i]);
                textField.setFont(new Font("Poppins", Font.PLAIN, 18));
                textField.setForeground(Color.BLACK);
                textField.setBackground(new Color(245,245,245));
                textField.setBounds(170, yPosition, 200, 30);
                textField.setEditable(false); // Initially not editable
                textField.setFocusable(false);
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                infoPanel.add(textField);

                // Add Edit Button
                JButton editButton = new JButton(new ImageIcon("resources/Icons 50px/edit.png")); // Replace with actual icon path
                editButton.setBounds(380, yPosition, 20, 20); // Position for edit icon
                editButton.setContentAreaFilled(false);
                editButton.setBorderPainted(false);
                editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                infoPanel.add(editButton);

                // Toggle edit mode
                editButton.addActionListener(e -> {
                    textField.setEditable(true);
                    textField.requestFocus();
                    textField.setFocusable(true);
                    editClicked = true; // Set the flag that edit was clicked
                });

                if (i == 2) phoneTextField = textField;
                if (i == 3) passwordTextField = textField;

            } else { // Name and Role (Non-editable)
                JLabel valueLabel = new JLabel(values[i]);
                valueLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
                valueLabel.setForeground(new Color(57,62,70));
                valueLabel.setBounds(170, yPosition, 300, 30);
                infoPanel.add(valueLabel);
            }

            yPosition += 40; // Increment Y position for the next row
        }

        // Add Info Panel to Layout
        gbc.gridx = 0;
        gbc.gridy = 1; // Row 1
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        gbc.anchor = GridBagConstraints.CENTER;
        add(infoPanel, gbc);

        // Button Container
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonContainer.setBounds(200, 280, 500, 60); // Increase height to fit larger buttons
        buttonContainer.setBackground((new Color(238,238,238)));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Poppins", Font.BOLD, 18));
        backButton.setForeground(new Color(57,62,70));
        backButton.setBackground(new Color(255,211,105));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(200, 40)); // Increased width and height
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 25, 5, 25)));
        buttonContainer.add(backButton);

        backButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(ProfileView.this);
            parentFrame.dispose(); // Close the current window
        });

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Poppins", Font.BOLD, 18));
        saveButton.setForeground(new Color(57,62,70));
        saveButton.setBackground(new Color(255,211,105));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(200, 40)); // Increased width and height
        saveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34,40,49), 2, true),
                BorderFactory.createEmptyBorder(5, 25, 5, 25)));
        buttonContainer.add(saveButton);

        JTextField finalPhoneTextField = phoneTextField;
        JTextField finalPasswordTextField = passwordTextField;

        saveButton.addActionListener(e -> {
            if (editClicked) {
                // Get updated values from the text fields
                String updatedPhoneNo = finalPhoneTextField.getText().trim();
                String updatedPassword = finalPasswordTextField.getText().trim();

                // Call the updateUserDetails method from DatabaseHelper
                DatabaseHelper dbHelper = null;
                try {
                    dbHelper = new DatabaseHelper();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                boolean isUpdated = dbHelper.updateUserDetails(username, updatedPhoneNo, updatedPassword);

                if (isUpdated) {
                    CustomOptionPane.showDoneDialog(ProfileView.this, "Details updated successfully!", "Success");
                } else {
                    CustomOptionPane.showMsgDialog(ProfileView.this, "Failed to update details. Please try again.", "Error");
                }

                // Reset the editable fields and edit flag
                editClicked = false;
                if (finalPhoneTextField != null) finalPhoneTextField.setEditable(false);
                if (finalPasswordTextField != null) finalPasswordTextField.setEditable(false);

            } else {
                CustomOptionPane.showMsgDialog(ProfileView.this, "No changes made.", "Save");
            }
        });

        // Add Button Container to Layout
        gbc.gridx = 0;
        gbc.gridy = 2; // Row 2
        gbc.anchor = GridBagConstraints.SOUTH;
        add(buttonContainer, gbc);

    }

}

