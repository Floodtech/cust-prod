import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class CreateProductForm {
    private JPanel panel1;
    private JTextField expectedDeliveryDateTextField;
    private JTextField orderDateTextField;
    private JTextField receivedByTextField;
    private JTextField codeTextField;
    private JTextField quantityTextField;
    private JTextField notesTextField;
    private JTextArea descriptionTextArea;
    private JTextArea instructionsTextArea;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField designByDateTextField;
    private JComboBox comboBox3;
    private JTextField spellCheckedDateTextField;
    private JTextField productionDateTextField;
    private JButton cancelButton;
    private JButton submitButton;
    private JButton addNewFieldsButton;

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
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }
    }


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

