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

public class SearchGui extends JFrame implements ActionListener, WindowListener{
	//private Label lblAir;    // Declare a Label component 
	//private TextField tfCount; // Declare a TextField component 
	//private Button btnCount;   // Declare a Button component
	//private int count = 0;     // Counter's value
	//JPanel panel = new JPanel();
	
	private Choice airportDepList, airportArrList;
	private UtilDateModel modelD, modelA;
	private ButtonGroup dOrAButtonGroup, seatButtonGroup, roundTripButtonGroup;
	private JSpinner timeSpinnerS, timeSpinnerE;
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public SearchGui () {
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


		add(new JLabel("Departure Airport:"), gbc);
		airportDepList = new Choice();
		airportArrList = new Choice();

		for(int i = 0; i< airport.Airports.instance.size(); i++){
			airportDepList.add(airport.Airports.instance.get(i).getCode());
			airportArrList.add(airport.Airports.instance.get(i).getCode());
		}
		gbc.gridx = 1;
		add(airportDepList, gbc);
		//gbc.insets = new Insets(30, 50, 50, 50);
		gbc.gridx = 0;
		gbc.gridy = 2;
		
        ///panel.add(new JLabel(" "),"span, grow");	
		
		add(new JLabel("Arrival Airport:"), gbc);
		gbc.gridx = 1;
		add(airportArrList, gbc);
		
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
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		add(new JLabel("Seating Type:"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 11;
		add(new JLabel("Round Trip:"), gbc);
		
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
		//departureButton.setActionCommand(departureButton.getText());
		departureButton.setSelected(true);
		
		JRadioButton arrivalButton = new JRadioButton("Arrival");
		arrivalButton.setMnemonic(1);
		//arrivalButton.setActionCommand(arrivalButton.getText());
		//arrivalButton.setSelected(true);

	    dOrAButtonGroup = new ButtonGroup();
	    dOrAButtonGroup.add(departureButton);
	    dOrAButtonGroup.add(arrivalButton);
	    
	    //departureButton.addActionListener(this);
	    //arrivalButton.addActionListener(this);


		gbc.gridx = 1;
		gbc.gridy = 5;
		add(departureButton, gbc);
		gbc.gridx = 1;
		gbc.gridy = 6;
		//gbc.insets = new Insets(10,0,0,0);
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
		
		// seating type selection
		JRadioButton economyButton = new JRadioButton("Economy");
		economyButton.setMnemonic(0);
		economyButton.setSelected(true);
		
		JRadioButton firstClassButton = new JRadioButton("First Class");
		firstClassButton.setMnemonic(1);
	    seatButtonGroup = new ButtonGroup();
	    seatButtonGroup.add(economyButton);
	    seatButtonGroup.add(firstClassButton);

		gbc.gridx = 1;
		gbc.gridy = 9;
		add(economyButton, gbc);
		gbc.gridx = 1;
		gbc.gridy = 10;
		add(firstClassButton, gbc);

		// round trip selection
		JRadioButton isNotRTButton = new JRadioButton("No");
		isNotRTButton.setMnemonic(0);
		isNotRTButton.setSelected(true);
		
		JRadioButton isRTButton = new JRadioButton("Yes");
		isRTButton.setMnemonic(1);

	    roundTripButtonGroup = new ButtonGroup();
	    roundTripButtonGroup.add(isNotRTButton);
	    roundTripButtonGroup.add(isRTButton);

		gbc.gridx = 1;
		gbc.gridy = 11;
		add(isNotRTButton, gbc);
		gbc.gridx = 1;
		gbc.gridy = 12;
		add(isRTButton, gbc);
		
		// submit search button
		gbc.gridx = 1;
		gbc.gridy = 14;
		Button submitButton = new Button("Search");
		add(submitButton, gbc);  
		
		submitButton.addActionListener(this);
		
		//add(panel, gbc);
		
		setTitle("Search");  // "super" Frame sets its title
		setSize(1000, 400);        // "super" Frame sets its initial window size
	 
		addWindowListener(this);	 
		setVisible(true);         // "super" Frame shows

		/*
		tfCount = new TextField("0", 10); // construct the TextField component
		tfCount.setEditable(false);       // set to read-only
		add(tfCount);                     // "super" Frame container adds TextField component
	 
		btnCount = new Button("Count");   // construct the Button component
		add(btnCount);                    // "super" Frame container adds Button component
	 
		btnCount.addActionListener(this);
	         // "btnCount" is the source object that fires an ActionEvent when clicked.
	         // The source add "this" instance as an ActionEvent listener, which provides
	         //   an ActionEvent handler called actionPerformed().
	         // Clicking "btnCount" invokes actionPerformed().
	 
		setTitle("Search");  // "super" Frame sets its title
		setSize(1000, 400);        // "super" Frame sets its initial window size
	 
		addWindowListener(this);	 
		setVisible(true);         // "super" Frame shows
		*/
	}

	/**
	 * actionPerformed()
	 * Fills out the user params object when search is clicked.
	 * Calls handleSearch() with that object.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SearchParams params = new SearchParams();
		String departureAirport = airportDepList.getSelectedItem();
		String arrivalAirport = airportArrList.getSelectedItem();
		params.setDepartureAirportCode(departureAirport.toCharArray());
		params.setArrivalAirportCode(arrivalAirport.toCharArray());
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
			params.setDepartureDate(tripDate);
			params.setDepartureTime(tripTime);
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
			params.setArrivalDate(tripDate);
			params.setArrivalTime(tripTime);
		}
		int isRoundTrip = roundTripButtonGroup.getSelection().getMnemonic();
		int seatingType = seatButtonGroup.getSelection().getMnemonic();
		if(isRoundTrip == 0){
			params.setIsRoundTrip(false);
		}
		else{
			params.setIsRoundTrip(true);
		}
		if(seatingType == 0){
			params.setSeatType('c');
		}
		else{
			params.setSeatType('f');
		}
		dispose();
		if(isRoundTrip == 1){
			RoundTripSearchGui round_trip_search = new RoundTripSearchGui(params);
			return;
		}
		UserInterface.instance.HandleSearch(params);
		
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
