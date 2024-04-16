import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.regex.Pattern;

public class AddCustomerForm extends JDialog {
    private JTextField business;
    private JTextField contact;
    private JTextField email;
    private JTextField telephone;
    private JTextField cellular;
    private JButton cancelButton;
    private JButton submitButton;
    private JPanel customerPanel;

    public AddCustomerForm(JFrame parent) {
        super(parent);
        setTitle("Create a new customer");
        setContentPane(customerPanel);
        setMinimumSize(new Dimension(650, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCustomer();
            }
        });
        setVisible(true);
    }

    private void cancelActivity() {
    }

    private void createCustomer() {
        String businessName = business.getText();
        String contactName = contact.getText();
        String customerEmail = email.getText();
        String customerTelNum = telephone.getText();
        String customerCellNum = cellular.getText();

        if (businessName.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter Company/Business Name",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (contactName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Contact Person",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (!testUsingStrictRegex(customerEmail) && !customerEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid email address",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (customerTelNum.length() != 11 && !customerTelNum.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter telephone number, digits only \n" +
                    "Example: 12464265358",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (customerCellNum.length() != 11 && !customerCellNum.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter cellular phone number, digits only \n" +
                            "Example: 12462265358",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        customer = addCustomerToDatabase(businessName, contactName, customerEmail, customerTelNum, customerCellNum);
        if (customer != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create new customer",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Customer customer;
    private Customer addCustomerToDatabase(String businessName, String contactName, String customerEmail, String customerTelNum, String customerCellNum) {
    Customer customer = null;
    final String DB_URL ="jdbc:mysql://localhost/cps?serverTimezone=UTC-4";
    final String USERNAME ="root";
    final String PASSWORD ="";

    try {
        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        //Connected to database successfully

        //Input data entered as SQL Statement
        Statement stmt = conn.createStatement();
        String sql = "INSERT INTO customers (businessName, contactName, customerEmail, customerTelNum, customerCellNum) " +
                "VALUES (?,?,?,?,?) ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1,businessName);
        preparedStatement.setString(2,contactName);
        preparedStatement.setString(3,customerEmail);
        preparedStatement.setString(4,customerTelNum);
        preparedStatement.setString(5,customerCellNum);

        //Insert row into Table
        int addedRows = preparedStatement.executeUpdate();
        if (addedRows > 0){
            customer = new Customer();
            customer.businessName = businessName;
            customer.contactName = contactName;
            customer.customerEmail = customerEmail;
            customer.customerTelNum = customerTelNum;
            customer.customerCellNum = customerCellNum;
        }
        stmt.close();
        conn.close();
    } catch (Exception e){
        e.printStackTrace();
    }
    return customer;
    }

    public boolean testUsingStrictRegex(String emailAddress) {
        //emailAddress = "username@domain.com";
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static void main(String[] args) {
        AddCustomerForm custForm = new AddCustomerForm(null);
        Customer customer = custForm.customer;
        if (customer != null){
            System.out.println("Successful creation of customer: " + customer.businessName);
        } else {
            System.out.println("Customer Creation Canceled");
        }
        //CreateProductForm productForm = new CreateProductForm();
    }
}
