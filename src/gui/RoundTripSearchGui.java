package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;

import org.jdatepicker.impl.*;

import plans.SearchParams;
import user.UserInterface;

public class RoundTripSearchGui extends JFrame implements ActionListener, WindowListener{	
	private UtilDateModel modelDate;
	private ButtonGroup dOrAButtonGroup;
	private JSpinner timeSpinnerS, timeSpinnerE;
	private SearchParams knownParams;
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public RoundTripSearchGui (SearchParams knownParams) {
		this.knownParams = knownParams;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.ipady = 40; 
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("Date:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("Departure or Arrival Date Selection:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		add(new JLabel("Time Window Begin:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		add(new JLabel("Time Window End:"), gbc);
		
		// add radio button to choose departure or arrival date/time

		JRadioButton departureButton = new JRadioButton("Departure");
		departureButton.setMnemonic(0);
		departureButton.setSelected(true);
		
		JRadioButton arrivalButton = new JRadioButton("Arrival");
		arrivalButton.setMnemonic(1);

	    dOrAButtonGroup = new ButtonGroup();
	    dOrAButtonGroup.add(departureButton);
	    dOrAButtonGroup.add(arrivalButton);
	    
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(departureButton, gbc);
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(arrivalButton, gbc);

		// add the calendar selection for departure and arrival date
		modelDate = new UtilDateModel();

		modelDate.setDate(2017, 4, 5); // month is zero based, this is May 5

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JDatePanelImpl datePanel = new JDatePanelImpl(modelDate, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		// sets the default date as selected
		datePicker.getModel().setSelected(true);

		gbc.gridx = 1;
		gbc.gridy = 5;
		add(datePicker, gbc);
		
		timeSpinnerS = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditorS = new JSpinner.DateEditor(timeSpinnerS, "HH:mm");
		timeSpinnerS.setEditor(timeEditorS);
		timeSpinnerS.setValue(new Date());
		
		timeSpinnerE = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditorE = new JSpinner.DateEditor(timeSpinnerE, "HH:mm");
		timeSpinnerE.setEditor(timeEditorE);
		timeSpinnerE.setValue(new Date());

		gbc.gridx = 1;
		gbc.gridy = 6;
		add(timeSpinnerS, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		add(timeSpinnerE, gbc);
				
		// submit search button
		gbc.gridx = 1;
		gbc.gridy = 13;
		Button submitButton = new Button("Search");
		add(submitButton, gbc);  
		
		submitButton.addActionListener(this);
				
		setTitle("Search");  // "super" Frame sets its title
		setSize(1000, 400);        // "super" Frame sets its initial window size
	 
		addWindowListener(this);	 
		setVisible(true);         // "super" Frame shows
	}

	/**
	 * actionPerformed()
	 * Fills out the user params object when search is clicked.
	 * Calls handleSearch() with that object.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int departureOrArrival = dOrAButtonGroup.getSelection().getMnemonic();//.getActionCommand();
		utils.Time tripTime[] = new utils.Time[2];
		Calendar calendar = Calendar.getInstance();
		utils.Date tripDate = new utils.Date(modelDate.getDay(), modelDate.getMonth() + 1, modelDate.getYear());
		Date startTime = (Date) timeSpinnerS.getValue();
		Date endTime = (Date) timeSpinnerE.getValue();
		calendar.setTime(startTime);
		tripTime[0] = new utils.Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		calendar.setTime(endTime);
		tripTime[1] = new utils.Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		if(!utils.Time.validTimeWindow(tripTime)){
			dispose();
			new ErrorMessageGui("Second time must be after first time in window.", false);
			return;
		}

		if(departureOrArrival == 0){
			// departure date/time selected
			knownParams.setRDepartureDate(tripDate);
			knownParams.setRDepartureTime(tripTime);
		}
		else{
			// arrival date/time selected
			knownParams.setRArrivalDate(tripDate);
			knownParams.setRArrivalTime(tripTime);
		}
		dispose();
		
		// display a processing message
		LoadingGui loadingPage = new LoadingGui();
		
		Runnable handleSearch = new Runnable() {
		     public void run() {
		    	 UserInterface.instance.HandleSearch(knownParams, loadingPage);
		     }
		};
		
		SwingUtilities.invokeLater(handleSearch);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);  // Terminate the program
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
