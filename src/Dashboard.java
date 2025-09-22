import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JDialog {
    private JPanel rootPanel;
    private JButton newCustomerButton;
    private JButton newOrderButton;
    private JButton customerDatabaseButton;
    private JButton orderDatabaseButton;
    private JButton quitButton;

    public Dashboard(JFrame parent) {
        super(parent);
        setTitle("Management Board");
        setContentPane(rootPanel);
        setMinimumSize(new Dimension(800, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        newCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCustomerForm customerForm = new AddCustomerForm(null);
                customerForm.setVisible(true);
            }
        });

        newOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateOrderForm orderForm = new CreateOrderForm(null);
                orderForm.setVisible(true);
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }



    public static void main(String[] args) {
        // Set the Look and Feel (L&F) of UI
        String guiDesign = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

        try {
            UIManager.setLookAndFeel(guiDesign);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Dashboard dashboardForm = new Dashboard(null);
    }
}
