package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
//import javax.swing.SwingConstants;

import org.jdatepicker.impl.*;

import airport.Airports;
import plans.SearchParams;
import user.UserInterface;

public class RoundTripSearchGui extends JFrame implements ActionListener, WindowListener{	
	private UtilDateModel modelD, modelA;
	private ButtonGroup dOrAButtonGroup;
	private JSpinner timeSpinnerS, timeSpinnerE;
	private SearchParams params;
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public RoundTripSearchGui (SearchParams known_params) {
		params = known_params;
		setLayout(new GridBagLayout());
	         // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
	         // the components from left-to-right, and flow to next row from top-to-bottom.
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
		gbc.gridy = 3;
		add(new JLabel("Departure Date:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new JLabel("Arrival Date:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("Departure or Arrival Date Selection:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		add(new JLabel("Time Window Begin:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		add(new JLabel("Time Window End:"), gbc);
		
		// add the calendar selection for departure and arrival date
		modelD = new UtilDateModel();
		modelA = new UtilDateModel();

		modelD.setDate(2017, 4, 5); // month is zero based, this is May 5
		modelA.setDate(2017, 4, 5);

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JDatePanelImpl datePanelD = new JDatePanelImpl(modelD, p);
		JDatePanelImpl datePanelA = new JDatePanelImpl(modelA, p);
		JDatePickerImpl datePickerD = new JDatePickerImpl(datePanelD, new DateLabelFormatter());
		JDatePickerImpl datePickerA = new JDatePickerImpl(datePanelA, new DateLabelFormatter());

		// sets the default date as selected
		datePickerD.getModel().setSelected(true);
		datePickerA.getModel().setSelected(true);

		gbc.gridx = 1;
		gbc.gridy = 3;
		add(datePickerD, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(datePickerA, gbc);
		
		JRadioButton departureButton = new JRadioButton("Departure");
		departureButton.setMnemonic(0);
		departureButton.setSelected(true);
		
		JRadioButton arrivalButton = new JRadioButton("Arrival");
		arrivalButton.setMnemonic(1);

	    dOrAButtonGroup = new ButtonGroup();
	    dOrAButtonGroup.add(departureButton);
	    dOrAButtonGroup.add(arrivalButton);
	    
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(departureButton, gbc);
		gbc.gridx = 1;
		gbc.gridy = 6;
		add(arrivalButton, gbc);

		
		timeSpinnerS = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditorS = new JSpinner.DateEditor(timeSpinnerS, "HH:mm:ss");
		timeSpinnerS.setEditor(timeEditorS);
		timeSpinnerS.setValue(new Date());
		
		timeSpinnerE = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditorE = new JSpinner.DateEditor(timeSpinnerE, "HH:mm:ss");
		timeSpinnerE.setEditor(timeEditorE);
		timeSpinnerE.setValue(new Date());

		gbc.gridx = 1;
		gbc.gridy = 7;
		add(timeSpinnerS, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 8;
		add(timeSpinnerE, gbc);
				
		// submit search button
		gbc.gridx = 1;
		gbc.gridy = 14;
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
		utils.Date tripDate;
		utils.Time tripTime[] = new utils.Time[2];
		Calendar calendar = Calendar.getInstance();
		Date startTime;
		Date endTime;
		if(departureOrArrival == 0){
			// departure date/time selected
			tripDate = new utils.Date(modelD.getDay(), modelD.getMonth() + 1, modelD.getYear());
			startTime = (Date) timeSpinnerS.getValue();
			endTime = (Date) timeSpinnerE.getValue();
			calendar.setTime(startTime);
			tripTime[0] = new utils.Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
			calendar.setTime(endTime);
			tripTime[1] = new utils.Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
			params.setRDepartureDate(tripDate);
			params.setRDepartureTime(tripTime);
		}
		else{
			// arrival date/time selected
			tripDate = new utils.Date(modelD.getDay(), modelD.getMonth() + 1, modelD.getYear());
			startTime = (Date) timeSpinnerS.getValue();
			endTime = (Date) timeSpinnerE.getValue();
			calendar.setTime(startTime);
			tripTime[0] = new utils.Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
			calendar.setTime(endTime);
			tripTime[1] = new utils.Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
			params.setRArrivalDate(tripDate);
			params.setRArrivalTime(tripTime);
		}
		dispose();
		LoadingGui loadingPage = new LoadingGui();
		UserInterface.instance.HandleSearch(params, loadingPage);
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
