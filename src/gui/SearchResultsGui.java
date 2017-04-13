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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
//import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;

import org.jdatepicker.impl.*;

import airport.Airports;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.SearchParams;
import user.UserInterface;

public class SearchResultsGui extends JFrame implements ActionListener, WindowListener{
	//private Label lblAir;    // Declare a Label component 
	//private TextField tfCount; // Declare a TextField component 
	//private Button btnCount;   // Declare a Button component
	//private int count = 0;     // Counter's value
	//JPanel panel = new JPanel();
	
	private JSpinner flightPlanSpinner;
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public SearchResultsGui (FlightPlans fpList) {
		if(fpList.getFlightPlansList().size() == 0){
			// TODO
			// failure message window
			return;
		}
		
		setLayout(new GridBagLayout());
	         // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
	         // the components from left-to-right, and flow to next row from top-to-bottom.
		GridBagConstraints gbc = new GridBagConstraints();
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		
		// sort by price button
		gbc.gridx = 0;
		gbc.gridy = 0;
		Button sortPriceButton = new Button("Sort by Price");
		add(sortPriceButton, gbc);  
				
		sortPriceButton.addActionListener(this);
		
		// sort by flight time button
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		Button sortTimeButton = new Button("Sort by Time");
		add(sortTimeButton, gbc);  
				
		sortTimeButton.addActionListener(this);

		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 20;
		
		// display flight plan list string		
		
		JTextArea display = new JTextArea ( 20, 58 );
	    display.setEditable ( false ); // set textArea non-editable
	    display.setText(fpList.toString());
	    JScrollPane scroll = new JScrollPane ( display );
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    display.setCaretPosition(0); // set scroll position to top
		
		add(scroll, gbc);
		
		// flight plan selection spinner 
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 22;
		add(new JLabel("Select flight plan number:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 22;
		
		flightPlanSpinner = new JSpinner( new SpinnerNumberModel(1, 1, fpList.getFlightPlansList().size(), 1) );
		JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(flightPlanSpinner, "");
		flightPlanSpinner.setEditor(numberEditor);
		//flightPlanSpinner.setValue(1);
		add(flightPlanSpinner, gbc);
		
		// submit selection button
		
		gbc.gridx = 0;
		gbc.gridy = 23;
		Button submitSelectionButton = new Button("Submit selection");
		add(submitSelectionButton, gbc);  
						
		submitSelectionButton.addActionListener(this);
				
		setTitle("Search Results");  // "super" Frame sets its title
		setSize(1000, 600);        // "super" Frame sets its initial window size
	 
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
