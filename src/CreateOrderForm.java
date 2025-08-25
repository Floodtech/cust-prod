import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.util.Objects;
import java.time.LocalDate;


public class CreateOrderForm extends JDialog{
    private JPanel rootPanel;
    private JButton cancelButton;
    private JButton addButton;
    private JButton saveButton;
    private JLabel formName;
    private JLabel referenceNumberLabel;
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
    private JComboBox<String> customerBox;
    private JComboBox<String> prodStatusBox;
    private JTable productListTable;
    private JButton calendarButton;
    //private DateChooser dateChooser = new DateChooser();
    private JFormattedTextField orderDateTextField;
    private JLabel employee1Label;
    private JTextField employee1Field;
    private JTextField prodStatusField;
    private JLabel employee2Label;
    private JTextField employee2Field;
    private JTextField orderCompletedDateField;


    public CreateOrderForm(JFrame parent) {
        super(parent);
        setTitle("Create a new Order");
        setContentPane(rootPanel);
        setMinimumSize(new Dimension(1100, 800));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createCustomerList();
        LocalDate currentDate = LocalDate.now();
        orderDateTextField.setText(currentDate.toString());
        cancelButton.addActionListener((event) -> dispose());
        saveButton.addActionListener((event) -> createOrder());
        addButton.addActionListener((event) -> createProduct());
        refNumField.setEditable(false);

        //showTable();
        setVisible(true);
    }

    public void createCustomerList() {
        customerBox.insertItemAt("---Select Customer---", 0); // Inserts at the beginning
        customerBox.setSelectedItem("---Select Customer---");

        final String DB_URL ="jdbc:mysql://localhost/cps?serverTimezone=UTC-4";
        final String USERNAME ="root";
        final String PASSWORD ="";
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //Connected to database successfully

            //Input data entered as SQL Statement
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT businessName FROM customers");
            int count = 1;
            while (rs.next()) { // Check if there's a row
                String value1 = rs.getString("businessName");

                // Populate your form fields (e.g., JTextField, JComboBox)
                customerBox.insertItemAt(value1, count); // Adds each item
                count++;
                //System.out.println(value1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* log error */ }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { /* log error */ }
            try { if (conn != null) conn.close(); } catch (SQLException e) { /* log error */ }
        }

    }
        public void createProduct() {
            String productCode = prodCodeField.getText();
            String productQuantity = quantityField.getText();
            String productDescription = descArea.getText();
            String productNotes = notesArea.getText();
            String productStatus = prodStatusField.getText();

            product = addProductToDatabase(productCode, productQuantity, productDescription, productNotes, productStatus);
            if (product != null) {
                System.out.println("Successfully created new product " + productCode);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to create new product",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
            }


        }
        public Product product;

        private Product addProductToDatabase(String productCode, String productQuantity, String productDescription,
                                             String productNotes, String productStatus){

            Product product = new Product();
            final String DB_URL ="jdbc:mysql://localhost/cps?serverTimezone=UTC-4";
            final String USERNAME ="root";
            final String PASSWORD ="";

            try {
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                //Connected to database successfully

                //Input data entered as SQL Statement
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO products (productCode, orderReferenceNumber, quantity, description, notes, status) " +
                        "VALUES (?,?,?,?,?,?) ";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,productCode);
                preparedStatement.setString(2,"202500001");
                preparedStatement.setInt(3,Integer.parseInt(productQuantity));
                preparedStatement.setString(4,productDescription);
                preparedStatement.setString(5,productNotes);
                preparedStatement.setString(6,productStatus);

                //Insert row into Table
                int addedRows = preparedStatement.executeUpdate();
                if (addedRows > 0){
                    product.productCode = productCode;
                    product.quantity = productQuantity;
                    product.description = productDescription;
                    product.productNotes = productNotes;
                    product.status = productStatus;
                }
                stmt.close();
                conn.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            showTable();
            return product;
        }

        public void createOrder() {
        String orderReferenceNumber = refNumField.getText();
        String orderReceivedBySignature = employee1Field.getText();
        String orderDate = orderDateTextField.getText();
        String scheduledDeliveryDate = plannedDateField.getText();
        String customerName = (String) customerBox.getSelectedItem();
        String orderStatus = statusField.getText();
        String orderInstructions = instructionArea.getText();
        String orderCompletedBySignature = employee2Field.getText();
        String orderCompletedDate = orderDateTextField.getText();

        //Input validations / regulatory expressions
        if (orderReceivedBySignature.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter employee name or department.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (scheduledDeliveryDate.isEmpty()) { //Expand to check whether date is valid
            JOptionPane.showMessageDialog(this,
                    "Please enter the scheduled delivery date.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (customerName==null || customerName.equals("---Select Customer---")) {
            JOptionPane.showMessageDialog(this,
                    "Please select the customer.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (orderInstructions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please confirm who received this order.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (orderStatus.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please input order status.",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }else if (orderCompletedBySignature.isEmpty()) {
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

        order = addOrderToDatabase(orderReferenceNumber, orderDate, scheduledDeliveryDate, orderReceivedBySignature,
                customerName, orderStatus, orderInstructions, orderCompletedBySignature, orderCompletedDate);
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

    private Order addOrderToDatabase(String orderReferenceNumber, String orderDate, String scheduledDeliveryDate, String  orderReceivedBySignature,
                                     String customerName, String orderStatus, String orderInstructions, String
            orderCompletedBySignature, String orderCompletedDate){

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
            String sql = "INSERT INTO orders (orderDate, scheduledDeliveryDate, employeeName, orderStatus, orderInstructions, businessName) " +
                    "VALUES (?,?,?,?,?,?) ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,orderDate);
            preparedStatement.setString(2,scheduledDeliveryDate);
            preparedStatement.setString(3,orderReceivedBySignature);
            preparedStatement.setString(4,orderStatus);
            preparedStatement.setString(5,orderInstructions);
            preparedStatement.setString(6,customerName);

            //Insert row into Table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                order = new Order();
                order.orderReferenceNumber = orderReferenceNumber;
                order.orderDate = orderDate;
                order.scheduledDeliveryDate = scheduledDeliveryDate;
                order.orderReceivedBySignature = orderReceivedBySignature;
                order.customerName = customerName;
                order.orderInstructions = orderInstructions;
                order.orderStatus = orderStatus;
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

        String productCode = "", productQuantity = "", productDescription = "", productNotes = "", productStatus = "";

        final String DB_URL ="jdbc:mysql://localhost/cps?serverTimezone=UTC-4";
        final String USERNAME ="root";
        final String PASSWORD ="";
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //Connected to database successfully

            //Input data entered as SQL Statement
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT productCode, quantity, description, notes, status FROM products");
            int count = 1;
            // Column names
            String[] columnNames = {"Product Code", "Quantity", "Description", "Notes", "Status"};

            // Set the model with dummy data
            DefaultTableModel model = new DefaultTableModel(columnNames,0);
            while (rs.next()) { // Check if there's a row
                productCode = rs.getString("productCode");
                productQuantity = rs.getString("quantity");
                productDescription = rs.getString("description");
                productNotes = rs.getString("notes");
                productStatus = rs.getString("status");

                Object[] data = {productCode, productQuantity, productDescription, productNotes, productStatus};
                model.addRow(data);
                count++;
                //aSWSCSystem.out.println(productCode + productQuantity + productDescription + productNotes + productStatus);
            }

            productListTable.setModel(model);
            TableColumnModel columns = productListTable.getColumnModel();
            columns.getColumn(2).setMinWidth(250);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            columns.getColumn(0).setCellRenderer(centerRenderer);
            columns.getColumn(1).setCellRenderer(centerRenderer);
            columns.getColumn(4).setCellRenderer(centerRenderer);
            // Set table header alignment to center
            JTableHeader tableHeader = productListTable.getTableHeader();
            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            headerRenderer.setFont(new Font("Arial", Font.BOLD, 23));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* log error */ }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { /* log error */ }
            try { if (conn != null) conn.close(); } catch (SQLException e) { /* log error */ }
        }

        /*Object[][] data = {
                {"P002", 5, "Product 2 Description", "Notes 2", "Production"},
                {"P003", 8, "Product 3 Description", "Notes 3", "With Spell Checker"},
                {"P004", 12, "Product 4 Description", "Notes 4", "Completed"}
        };*/

    }



    public static void main(String[] args) {
        // Set the Look and Feel (L&F) of UI
        String guiDesign = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        //Nimbus Look and Feel: "javax.swing.plaf.nimbus.NimbusLookAndFeel"
        //Cross-platform (Metal): "javax.swing.plaf.metal.MetalLookAndFeel"
        //Windows (Windows look): "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
        //Mac OS X Aqua (for Mac users): "com.apple.laf.AquaLookAndFeel"
        //Motif: "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
        try {
            UIManager.setLookAndFeel(guiDesign);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

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
