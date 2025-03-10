import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

public class CreateOrderForm extends JDialog{
    private JPanel rootPanel;
    private JButton cancelButton;
    private JButton addButton;
    private JButton saveButton;
    private JLabel formName;
    private JLabel referenceNumberLabel;
    private JLabel datePlacedLabel;
    private JLabel deliveryDateLabel;
    private JLabel statusLabel;
    private JLabel instructionLabel;
    private JLabel productCodeLabel;
    private JLabel quantityLabel;
    private JLabel descLabel;
    private JLabel notesLabel;
    private JLabel prodStatusLabel;
    private JTextField refNumField;
    private JTextField plannedDateField;
    private JTextField statusField;
    private JTextField prodCodeField;
    private JTextField quantityField;
    private JTextArea descArea;
    private JTextArea notesArea;
    private JTextArea instructionArea;
    private JComboBox employee1Box;
    private JComboBox customerBox;
    private JComboBox employee2Box;
    private JComboBox prodStatusBox;
    private JTable productListTable;


    public CreateOrderForm(JFrame parent) {
        super(parent);
        setTitle("Create a new Order");
        setContentPane(rootPanel);
        setMinimumSize(new Dimension(1100, 800));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cancelButton.addActionListener((event) -> dispose());
        saveButton.addActionListener((event) -> createOrder());
        showTable();
        setVisible(true);


    }

    public void createOrder() {
        String orderReferenceNumber = refNumField.getText();
        String orderDate = null;
        String scheduledDeliveryDate = plannedDateField.getText();
        String orderReceivedBySignature = (String) null;
        String customerName = (String) customerBox.getSelectedItem();
        String customerReferenceNumber = null;
        String productReferenceNumber = null;
        String orderStatus = statusField.getText();
        String orderInstructions = instructionArea.getText();
        String orderCompletedBySignature = (String) null;
        String orderCompletedDate = null;
        String productCode = prodCodeField.getText();
        String productQuantity = quantityField.getText();
        String productDescription = descArea.getText();
        String productNotes = notesArea.getText();

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

    private void showTable(){
        Object[][] data = {
                {"P001", 10, "Product 1 Description", "Notes 1", "With Designer"},
                {"P002", 5, "Product 2 Description", "Notes 2", "Production"},
                {"P003", 8, "Product 3 Description", "Notes 3", "With Spell Checker"},
                {"P004", 12, "Product 4 Description", "Notes 4", "Completed"}
        };

        // Column names
        String[] columnNames = {"Product Code", "Quantity", "Description", "Notes", "Status"};

        // Set the model with dummy data
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        productListTable.setModel(model);
        TableColumnModel columns = productListTable.getColumnModel () ;
        columns.getColumn (2).setMinWidth (250) ;
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer ();
        centerRenderer.setHorizontalAlignment (JLabel. CENTER) ;
        columns.getColumn (0).setCellRenderer (centerRenderer);
        columns.getColumn (1).setCellRenderer (centerRenderer);
        columns.getColumn (4).setCellRenderer (centerRenderer);
        // Set table header alignment to center
        JTableHeader tableHeader = productListTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(new Font("Arial", Font.BOLD, 23));

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


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
