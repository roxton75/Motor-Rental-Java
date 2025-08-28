import javax.swing.*;
import java.awt.*;

public class CustomOptionPane {

    public static void showErrorDialog(JFrame parent, String message) {
        UIManager.put("OptionPane.background", new Color(238,238,238));
        UIManager.put("OptionPane.messageForeground", new Color(34,40,49));
        UIManager.put("Button.background", new Color(255,211,105));
        UIManager.put("Button.foreground", new Color(57,62,70));
        UIManager.put("Button.focusPainted", false);
        UIManager.put("Button.font", new Font("Netflix Sans", Font.PLAIN, 12));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 50, 5, 50)
        ));
        UIManager.put("OptionPane.messageFont", new Font("Netflix Sans", Font.PLAIN, 12));
        UIManager.put("OptionPane.buttonFont", new Font("Netflix Sans", Font.PLAIN, 12));

        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);

        resetUIManager();
    }

    public static void showMsgDialog(Component parent, String message, String title) {
        UIManager.put("OptionPane.messageFont", new Font("Netflix Sans", Font.PLAIN, 12));
        UIManager.put("OptionPane.background", new Color(238,238,238));
        UIManager.put("OptionPane.messageForeground", new Color(34,40,49));
        UIManager.put("Button.focusPainted", false);
        UIManager.put("Button.background", new Color(255,211,105));
        UIManager.put("Button.foreground", new Color(57,62,70));
        UIManager.put("Button.font", new Font("Netflix Sans", Font.BOLD, 12));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(5, 50, 5, 50)
        ));

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);

        resetUIManager();
    }

    // New method: Custom Yes/No confirm dialog with styling
    public static int chooseDialog(Component parent, String message, String title) {
        UIManager.put("OptionPane.messageFont", new Font("Netflix Sans", Font.PLAIN, 12));
        UIManager.put("OptionPane.background", new Color(238,238,238));
        UIManager.put("OptionPane.messageForeground", new Color(34,40,49));
        UIManager.put("Button.focusPainted", false);
        UIManager.put("Button.background", new Color(255,211,105));
        UIManager.put("Button.foreground", new Color(57,62,70));
        UIManager.put("Button.font", new Font("Netflix Sans", Font.BOLD, 12));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(5, 50, 5, 50)
        ));

        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        resetUIManager();

        return result;  // Returns JOptionPane.YES_OPTION or JOptionPane.NO_OPTION
    }

    public static void showDoneDialog(Component parent, String message, String title) {
        // Load tick icon from resources; adjust the path accordingly

        UIManager.put("OptionPane.messageFont", new Font("Netflix Sans", Font.PLAIN, 12));
        UIManager.put("OptionPane.background", new Color(238,238,238));
        UIManager.put("OptionPane.messageForeground", new Color(34,40,49));
        UIManager.put("Button.focusPainted", false);
        UIManager.put("Button.background", new Color(255,211,105));
        UIManager.put("Button.foreground", new Color(57,62,70));
        UIManager.put("Button.font", new Font("Netflix Sans", Font.BOLD, 12));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(5, 50, 5, 50)
        ));

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);

        resetUIManager();
    }


    // Utility method to reset UIManager keys to default
    private static void resetUIManager() {
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.focusPainted", null);
        UIManager.put("Button.font", null);
        UIManager.put("Button.border", null);
    }
}
