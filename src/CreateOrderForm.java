import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

public class CreateOrderForm extends JDialog{
    private JPanel orderPanel;
    private JTextField orderReferenceNumberText;
    private JTextField orderDateText;
    private JTextField scheduledDeliveryDateText;
    private JComboBox<String> orderReceivedBySelection;
    private JComboBox<String> customerNameSelection;
    private JTextField customerReferenceNumberText;
    private JTextField productReferenceNumberText;
    private JTextField orderStatusText;
    private JTextArea orderInstructionsText;
    private JComboBox<String> orderCompletedBySignatureSelection;
    private JTextField orderCompletedDateText;
    private JTextField productCodeText;
    private JTextField productQuantityText;
    private JTextField productDescriptionText;
    private JTextField productNotesText;
    private JButton addProductButton;
    private JButton cancelButton;
    private JButton submitButton;

    public CreateOrderForm(JFrame parent) {
        super(parent);
        setTitle("Create a new Order");
        setContentPane(orderPanel);
        setMinimumSize(new Dimension(500, 650));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cancelButton.addActionListener((event) -> dispose());
        submitButton.addActionListener((event) -> createOrder());
        setVisible(true);
    }

    public void createOrder() {
        String orderReferenceNumber = orderReferenceNumberText.getText();
        String orderDate = orderDateText.getText();
        String scheduledDeliveryDate = scheduledDeliveryDateText.getText();
        String orderReceivedBySignature = (String) orderReceivedBySelection.getSelectedItem();
        String customerName = (String) customerNameSelection.getSelectedItem();
        String customerReferenceNumber = customerReferenceNumberText.getText();
        String productReferenceNumber = productReferenceNumberText.getText();
        String orderStatus = orderStatusText.getText();
        String orderInstructions = orderInstructionsText.getText();
        String orderCompletedBySignature = (String) orderCompletedBySignatureSelection.getSelectedItem();
        String orderCompletedDate = orderCompletedDateText.getText();
        String productCode = productCodeText.getText();
        String productQuantity = productQuantityText.getText();
        String productDescription = productDescriptionText.getText();
        String productNotes = productNotesText.getText();

        //Input validations / regulatory expressions
        if (orderReferenceNumber.isEmpty()){
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
        } else if (scheduledDeliveryDate.isEmpty()) { //Expand to check whether date is valid
            JOptionPane.showMessageDialog(this,
                    "Please enter the scheduled delivery date.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderReceivedBySignature!=null || Objects.equals(orderReceivedBySignature, "---Select Employee---")) {
            JOptionPane.showMessageDialog(this,
                    "Please confirm who received this order.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (customerName!=null || customerName=="---Select Customer---") {
            JOptionPane.showMessageDialog(this,
                    "Please select the customer.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderInstructions.isEmpty()) {
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
        } else if (productQuantity.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the quantity of product required.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (productNotes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input product notes or N/A.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (productDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input product description or N/A.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderStatus.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input order status.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderCompletedBySignature!=null || Objects.equals(orderCompletedBySignature, "---Select Employee---")) {
            JOptionPane.showMessageDialog(this,
                    "Please confirm who completed this order.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderCompletedDate.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the order completed date.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        order = addOrderToDatabase(orderReferenceNumber, customerReferenceNumber, productReferenceNumber, orderDate, scheduledDeliveryDate, orderReceivedBySignature,
                customerName, orderStatus, orderInstructions, orderCompletedBySignature, orderCompletedDate,
                productCode, productQuantity, productDescription, productNotes);
        if (order != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create new order",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Order order;

    private Order addOrderToDatabase(String orderReferenceNumber, String customerReferenceNumber, String productReferenceNumber,
                                     String orderDate, String scheduledDeliveryDate, String  orderReceivedBySignature,
                                     String customerName, String orderStatus, String orderInstructions, String
            orderCompletedBySignature, String orderCompletedDate, String productCode, String productQuantity, String
            productDescription, String productNotes){

        Customer customer = null;
        Order order = null;
        final String DB_URL ="jdbc:mysql://localhost/cps?serverTimezone=UTC-4";
        final String USERNAME ="root";
        final String PASSWORD ="";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //Connected to database successfully

            //Input data entered as SQL Statement
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO orders (orderReferenceNumber, orderDate, scheduledDeliveryDate, orderReceivedBySignature,\n" +
                    "                customerName, orderStatus, orderInstructions, orderCompletedBySignature, orderCompletedDate,\n" +
                    "                productCode, productQuantity, productDescription, productNotes, customerReferenceNumber, " +
                    "productReferenceNumber) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,orderReferenceNumber);
            preparedStatement.setString(2,orderDate);
            preparedStatement.setString(3,scheduledDeliveryDate);
            preparedStatement.setString(4,orderReceivedBySignature);
            preparedStatement.setString(5,customerName);
            preparedStatement.setString(6,orderStatus);
            preparedStatement.setString(7,orderInstructions);
            preparedStatement.setString(8,orderCompletedBySignature);
            preparedStatement.setString(9,orderCompletedDate);
            preparedStatement.setString(10,productCode);
            preparedStatement.setString(11,productQuantity);
            preparedStatement.setString(12,productDescription);
            preparedStatement.setString(13,productNotes);
            preparedStatement.setString(14,customerReferenceNumber);
            preparedStatement.setString(15,productReferenceNumber);

            //Insert row into Table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                order = new Order();
                order.orderReferenceNumber = orderReferenceNumber;
                order.customerReferenceNumber = customerReferenceNumber;
                order.productReferenceNumber = productReferenceNumber;
                order.orderDate = orderDate;
                order.scheduledDeliveryDate = scheduledDeliveryDate;
                order.orderReceivedBySignature = orderReceivedBySignature;
                order.customerName = customerName;
                order.orderInstructions = orderInstructions;
                order.orderStatus = orderStatus;
                order.productCode = productCode;
                order.productQuantity = productQuantity;
                order.productDescription = productDescription;
                order.productNotes = productNotes;
                order.orderCompletedBySignature = orderCompletedBySignature;
                order.orderCompletedDate = orderCompletedDate;
            }
            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return order;
    }

    public static void main(String[] args) {
        CreateOrderForm orderForm = new CreateOrderForm(null);
        Order order = orderForm.order;
        if (order != null){
            System.out.println("Successful creation of order: " + order.orderReferenceNumber);
        } else {
            System.out.println("Order Creation Canceled");
        }
    }



}
