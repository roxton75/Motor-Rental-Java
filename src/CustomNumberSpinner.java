import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;

public class CustomNumberSpinner extends JPanel {
    private final JTextField textField;
    private final JButton upButton;
    private final JButton downButton;
    private int minValue = 1;
    private int maxValue = 99;

public CustomNumberSpinner(int initialValue) {
    setLayout(new BorderLayout());
    setBackground(new Color(245, 245, 245)); // Spinner background
    setBorder(BorderFactory.createLineBorder(new Color(34, 40, 49), 1)); // Spinner global border

    // Text field setup
    textField = new JTextField(String.valueOf(initialValue));
    textField.setFont(new Font("Poppins", Font.PLAIN, 15));
    textField.setHorizontalAlignment(JTextField.CENTER);
    textField.setBackground(new Color(245, 245, 245));
    textField.setForeground(new Color(34, 40, 49));
    // Flush left: no EmptyBorder
    textField.setBorder(BorderFactory.createEmptyBorder());

    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntegerFilter());

    // Buttons panel (up/down stacked vertically)
    JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 0));
    buttonsPanel.setPreferredSize(new Dimension(30, 32));

    upButton = new JButton("\u25B2"); // ▲
    styleButton(upButton);

    downButton = new JButton("\u25BC"); // ▼
    styleButton(downButton);

    buttonsPanel.add(upButton);
    buttonsPanel.add(downButton);

    add(textField, BorderLayout.CENTER);
    add(buttonsPanel, BorderLayout.EAST);

    upButton.addActionListener(e -> increment());
    downButton.addActionListener(e -> decrement());

    textField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            validateValue();
        }
    });
}


    public void setValueChangedListener(Runnable listener) {
        upButton.addActionListener(e -> listener.run());
        downButton.addActionListener(e -> listener.run());
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { listener.run(); }
            public void removeUpdate(DocumentEvent e) { listener.run(); }
            public void changedUpdate(DocumentEvent e) { listener.run(); }
        });
    }



    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(255, 211, 105));  // Your yellow palette
        button.setForeground(new Color(34, 40, 49));
//        button.setBorder(BorderFactory.createLineBorder(new Color(34, 40, 49), 0));
        button.setBorder(BorderFactory.createMatteBorder(0,1,0,0,Color.BLACK));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void increment() {
        int value = getValue();
        if (value < maxValue) {
            value++;
            textField.setText(String.valueOf(value));
        }
    }

    private void decrement() {
        int value = getValue();
        if (value > minValue) {
            value--;
            textField.setText(String.valueOf(value));
        }
    }

    private void validateValue() {
        int value = getValue();
        if (value < minValue) textField.setText(String.valueOf(minValue));
        else if (value > maxValue) textField.setText(String.valueOf(maxValue));
    }

    public int getValue() {
        try {
            int val = Integer.parseInt(textField.getText());
            return val;
        } catch (NumberFormatException e) {
            return minValue;  // default fallback
        }
    }

    public void setValue(int value) {
        if (value < minValue) value = minValue;
        else if (value > maxValue) value = maxValue;
        textField.setText(String.valueOf(value));
    }

    // DocumentFilter to allow only digits
    private static class IntegerFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
                throws BadLocationException {
            if (string.matches("\\d*")) {
                super.replace(fb, offset, length, string, attrs);
            }
        }
    }
}
