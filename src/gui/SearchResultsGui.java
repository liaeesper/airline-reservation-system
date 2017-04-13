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

public class SearchResultsGui extends JFrame implements ActionListener, WindowListener{
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
	public SearchResultsGui () {
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
				
		setTitle("Search Results");  // "super" Frame sets its title
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
