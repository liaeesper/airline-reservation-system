package gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * Date formatting class from the following tutorial:
 * @see <a href="http://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component">http://www.codejava.net/java-se/swing/how-to-use-jdatepicker-to-display-calendar-component</a>
 */
public class DateLabelFormatter extends AbstractFormatter {
	private static final long serialVersionUID = 1L;
	
	private String datePattern = "MM-dd-yyyy";
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