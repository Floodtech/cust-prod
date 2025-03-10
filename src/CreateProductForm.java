import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

public class CreateProductForm extends JDialog {
    private JPanel productPanel;
    private JTextField expectedDeliveryDate;
    private JTextField orderDateText;
    private JTextField receivedBy;
    private JTextField orderCodeText;
    private JTextField productCodeText;
    private JTextField quantity;
    private JTextField notes;
    private JTextArea description;
    private JTextArea instructions;
    private JComboBox<String> productionSignature;
    private JComboBox<String> designBySignature;
    private JTextField designByDate;
    private JComboBox<String> spellCheckedSignature;
    private JTextField spellCheckedDate;
    private JTextField productionDate;
    private JButton cancelButton;
    private JButton submitButton;
    private JButton addNewFieldsButton;
    private JPanel panel1;
    private JTextArea instructionsTextArea;
    private JTextArea notesTextArea;
    private JTextArea descriptionTextArea;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField designByDateTextField;
    private JComboBox comboBox3;
    private JTextField spellCheckedDateTextField;
    private JTextField productionDateTextField;
    private JTextField expectedDeliveryDateTextField;
    private JTextField orderDateTextField;
    private JTextField codeTextField;
    private JTextField quantityTextField;
    private JTextField receivedByTextField;
    private JTextField notesTextField;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        UtilDateModel model = new UtilDateModel();
//model.setDate(20,04,2014);
// Need this...
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
    // Don't know about the formatter, but there it is...
        //JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }
    public CreateProductForm(JFrame parent){
        super(parent);
        setTitle("Create a new Product");
        setContentPane(productPanel);
        setMinimumSize(new Dimension(1100, 800));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cancelButton.addActionListener((event) -> dispose());
        submitButton.addActionListener((event) -> createProduct());
        setVisible(true);

        productionSignature.setModel(new DefaultComboBoxModel<>(new String[] {
                "---- Select Employees ----", "User 1", "User 2", "User 3"
        }));
        designBySignature.setModel(new DefaultComboBoxModel<>(new String[] {
                "---- Select Employees ----", "User 1", "User 2", "User 3"
        }));
        spellCheckedSignature.setModel(new DefaultComboBoxModel<>(new String[] {
                "---- Select Employees ----", "User 1", "User 2", "User 3"
        }));
    }

    public void createProduct() {

        String expDelDate = expectedDeliveryDate.getText();
        String orderDate = orderDateText.getText();
        String recBy = receivedBy.getText();
        String productCode = productCodeText.getText();
        String orderCode = orderCodeText.getText();
        String qnty = quantity.getText();
        String prodNotes = notes.getText();
        String desc = description.getText();
        String instr = instructions.getText();
        String spellChkSig = (String) spellCheckedSignature.getSelectedItem();
        String desBySig = (String) designBySignature.getSelectedItem();
        String prodSig = (String) productionSignature.getSelectedItem();
        String spellChkDate = spellCheckedDate.getText();
        String desByDate = designByDate.getText();
        String prodDate = productionDate.getText();

        //Input validations / regulatory expressions
        if (expDelDate.isEmpty()){ //Expand to check whether date is valid
            JOptionPane.showMessageDialog(this,
                    "Please enter expected delivery date",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (orderDate.isEmpty()) { //Expand to check whether date is valid
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid order date.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (recBy.isEmpty()) { //Expand to check whether date is valid
            JOptionPane.showMessageDialog(this,
                    "Please confirm who received this order.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (productCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid product code.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid order code.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (qnty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the quantity of product required.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (prodNotes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input product notes or N/A.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (desc.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input product description or N/A.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (instr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input product instructions or N/A.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            assert prodSig != null;
            if (prodSig.equals("---- Select Employees ----")) {
                JOptionPane.showMessageDialog(this,
                        "Please confirm who completed production.",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                assert desBySig != null;
                if (desBySig.equals("---- Select Employees ----")) {
                    JOptionPane.showMessageDialog(this,
                            "Please confirm who completed design.",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    assert spellChkSig != null;
                    if (spellChkSig.equals("---- Select Employees ----")) {
                        JOptionPane.showMessageDialog(this,
                                "Please confirm who completed spell check.",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (spellChkDate.isEmpty()) { //Expand to check whether date is valid
                        JOptionPane.showMessageDialog(this,
                                "Please input spell checked date.",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (desByDate.isEmpty()) { //Expand to check whether date is valid
                        JOptionPane.showMessageDialog(this,
                                "Please input designed by date.",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (prodDate.isEmpty()) { //Expand to check whether date is valid
                        JOptionPane.showMessageDialog(this,
                                "Please input production completion date.",
                                "Try again",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        }

        product = addProductToDatabase(expDelDate, orderDate, recBy, productCode, qnty, prodNotes, desc, instr, prodSig,
                desBySig, spellChkSig, spellChkDate, desByDate, prodDate, orderCode);
        if (product != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create new product",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Product product;
    private Product addProductToDatabase(String expDelDate, String orderDate, String recBy, String productCode,
                                         String qnty, String prodNotes, String desc, String instr,
                                         String prodSig, String desBySig, String spellChkSig, String spellChkDate,
                                         String desByDate, String prodDate, String orderCode) {
        Product product = null;
        final String DB_URL ="jdbc:mysql://localhost/cps?serverTimezone=UTC-4";
        final String USERNAME ="root";
        final String PASSWORD ="";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //Connected to database successfully

            //Input data entered as SQL Statement
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO products (expDelDate, orderDate, receivedBy, productCode, quantity, notes, desc, " +
                    "instructions, prodSig, designBySig, spellCheckSig, spellCheckDate, designByDate, productionDate, orderCode) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,expDelDate);
            preparedStatement.setString(2,orderDate);
            preparedStatement.setString(3,recBy);
            preparedStatement.setString(4,productCode);
            preparedStatement.setString(5,qnty);
            preparedStatement.setString(6,prodNotes);
            preparedStatement.setString(7,desc);
            preparedStatement.setString(8,instr);
            preparedStatement.setString(9,prodSig);
            preparedStatement.setString(10,desBySig);
            preparedStatement.setString(11,spellChkSig);
            preparedStatement.setString(12,spellChkDate);
            preparedStatement.setString(13,desByDate);
            preparedStatement.setString(14,prodDate);
            preparedStatement.setString(15,orderCode);

            //Insert row into Table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                product = new Product();
                product.expectedDeliveryDate = expDelDate;
                product.requiredByDate = orderDate;
                product.receivedByDate = recBy;
                product.productCode = productCode;
                product.quantity = qnty;
                product.productNotes = prodNotes;
                product.description = desc;
                product.instructions = instr;
                product.production = prodSig;
                product.designedBy = desBySig;
                product.spellCheckedBy = spellChkSig;
                product.spellCheckedByDate = spellChkDate;
                product.designedByDate = desByDate;
                product.productionDate = prodDate;
                product.orderCode = orderCode;
            }
            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return product;
    }

    public static void main(String[] args) {
        CreateProductForm prodForm = new CreateProductForm(null);
        Product product = prodForm.product;
        if (product != null){
            System.out.println("Successful creation of product: " + product.productCode);
        } else {
            System.out.println("Product Creation Canceled");
        }
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


