import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTextField extends JTextField {

    public DateTextField() {
        super();
        setColumns(8);
//        setText("Enter Date of Booking");
//        setFont(new Font("Netflix Sans", Font.BOLD, 16));
//        setBackground(new Color(245,245,245));
//        setForeground(new Color(34,40,49));
//        setBorder(BorderFactory.createLineBorder(new Color(34,40,49), 2, true));

        ((AbstractDocument) this.getDocument()).setDocumentFilter(new DateFilter());
    }

    private static class DateFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null) {
                replace(fb, offset, 0, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            StringBuilder current = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            current.replace(offset, offset + length, text);

            String newText = formatDate(current.toString());
            if (isValidInput(newText)) {
                fb.replace(0, fb.getDocument().getLength(), newText, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length)
                throws BadLocationException {
            StringBuilder current = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            current.delete(offset, offset + length);
            String newText = formatDate(current.toString());
            fb.replace(0, fb.getDocument().getLength(), newText, null);
        }

        private boolean isValidInput(String input) {
            return input.matches("^\\d{0,2}(/\\d{0,2}(/\\d{0,2})?)?$");
        }

        private String formatDate(String raw) {
            raw = raw.replaceAll("[^\\d]", ""); // Remove all non-digit chars
            StringBuilder out = new StringBuilder();

            int len = raw.length();
            for (int i = 0; i < len && i < 6; i++) {
                out.append(raw.charAt(i));
                if ((i == 1 || i == 3) && i < len - 1) out.append('/');
            }
            return out.toString();
        }


    }
    public void setToToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");  // two-digit year as you want
        String todayStr = sdf.format(new Date());
        setText(todayStr);
    }
}
