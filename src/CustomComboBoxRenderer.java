import javax.swing.*;
import java.awt.*;

class CustomComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setBackground(Color.WHITE);
        label.setFont(new Font("Netflix Sans", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        if (isSelected) {
            label.setBackground(new Color(255, 211, 105)); // Yellow background for selected item
            label.setForeground(new Color(34,40,42));
        } else {
            label.setBackground(Color.WHITE);
            label.setForeground(new Color(34,40,42));
        }

        return label;
    }
}
