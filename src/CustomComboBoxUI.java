import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

class CustomComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        JButton arrowButton = new JButton("≡");
        arrowButton.setFont(new Font("Netflix Sans", Font.BOLD, 18));
        arrowButton.setForeground(Color.BLACK);
        arrowButton.setBackground(new Color(255, 211, 105)); // Yellow arrow button
        arrowButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,1,0,0,Color.black),
                BorderFactory.createEmptyBorder(0,0,3,0)
        ));
        return arrowButton;
    }

    @Override
    protected ComboPopup createPopup() {
        BasicComboPopup popup = new BasicComboPopup(comboBox) {
            @Override
            protected JScrollPane createScroller() {
                JScrollPane scrollPane = super.createScroller();

                // Hide vertical scrollbar if it exists
                if (scrollPane.getVerticalScrollBar() != null) {
                    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
                }

                // Hide horizontal scrollbar if it exists
                if (scrollPane.getHorizontalScrollBar() != null) {
                    scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
                }

                return scrollPane;
            }
        };
        return popup;
    }
}
