import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class UppercaseTextField extends JTextField {
    public UppercaseTextField(int columns) {
        super(columns);
        setDocumentFilter();
    }

    private void setDocumentFilter() {
        AbstractDocument doc = (AbstractDocument) this.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text != null && (fb.getDocument().getLength() + text.length() <= 10)) {
                    super.insertString(fb, offset, text.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && (fb.getDocument().getLength() - length + text.length() <= 10)) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });
    }
}
