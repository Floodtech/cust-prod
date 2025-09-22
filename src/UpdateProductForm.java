import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateProductForm extends JDialog {
    private JPanel rootPanel;
    private JButton cancelButton;
    private JButton updateButton;
    private JTextField prodCodeField;
    private JTextField quantityField;
    private JTextArea descArea;
    private JTextField prodStatusField;
    private JTextArea notesArea;
    private JLabel formName;


    public UpdateProductForm(JFrame parent){
        super(parent);
        setTitle("Edit Product Details");
        setContentPane(rootPanel);
        setMinimumSize(new Dimension(800, 450));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        setVisible(true);
    }

    public void createProduct() {

        String productCode = prodCodeField.getText();
        String qty = quantityField.getText();
        String prodNotes = notesArea.getText();
        String desc = descArea.getText();
        String prodStatus = prodStatusField.getText();
    }



    public static void main(String[] args) {
        // Set the Look and Feel (L&F) of UI
        String guiDesign = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        try {
            UIManager.setLookAndFeel(guiDesign);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        UpdateProductForm prodForm = new UpdateProductForm(null);

    }
}

/*
class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }
}
*/


