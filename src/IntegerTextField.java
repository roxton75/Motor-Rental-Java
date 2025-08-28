import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class IntegerTextField extends JTextField {
    private final int maxDigits;

    public IntegerTextField(int columns) {
        super(columns);
        this.maxDigits = columns;
        setDocumentFilter();
    }

    private void setDocumentFilter() {
        AbstractDocument doc = (AbstractDocument) this.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                String newValue = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()))
                        .insert(offset, text).toString();
                if (isValidInput(newValue)) {
                    super.insertString(fb, offset, text, attr);
                } // else ignore input
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String docText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(docText).replace(offset, offset + length, text);
                String newValue = sb.toString();
                if (isValidInput(newValue)) {
                    super.replace(fb, offset, length, text, attrs);
                } // else ignore input
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length); // always allow remove
            }

            private boolean isValidInput(String value) {
                // Allow empty for user to erase
                return value.isEmpty() || (value.matches("\\d{0," + maxDigits + "}"));
            }
        });
    }
}
