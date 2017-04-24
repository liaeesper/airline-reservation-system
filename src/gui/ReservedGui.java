package gui;

import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import dao.ServerInterface;
import plans.FlightPlan;
import plans.FlightPlans;
import plans.Reservation;

public class ReservedGui extends JFrame implements ActionListener, WindowListener{
	//private Label lblAir;    // Declare a Label component 
	//private TextField tfCount; // Declare a TextField component 
	//private Button btnCount;   // Declare a Button component
	//private int count = 0;     // Counter's value
	//JPanel panel = new JPanel();
	
	private JSpinner flightPlanSpinner;
	private FlightPlans fpList; 
	private ArrayList<FlightPlans> fpListArray; 
	private ArrayList<FlightPlan> user_choices_list;
	private boolean isReturnBool;

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public ReservedGui (ArrayList<FlightPlan> user_choices, LoadingGui loadingPage) {
		loadingPage.dispose();
		user_choices_list = user_choices;
		String flightPlansText = "";
		for(int i = 0; i < user_choices.size(); i++){
			flightPlansText += String.format("Flight plan %d:\n", i+1);
			flightPlansText += user_choices.get(i).toString();
		}
		// TODO
		// error message if either choice is no longer free, also lock
		
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
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 20;
		
		// display flight plan list string		
		
		JTextArea display = new JTextArea ( 20, 58 );
	    display.setEditable ( false ); // set textArea non-editable
	    display.setText(flightPlansText);
	    JScrollPane scroll = new JScrollPane ( display );
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    display.setCaretPosition(0); // set scroll position to top
		
		add(scroll, gbc);
				
		// confirm selection button
		
		gbc.gridx = 0;
		gbc.gridy = 23;
		Button submitSelectionButton = new Button("Okay");
		add(submitSelectionButton, gbc);  
						
		submitSelectionButton.addActionListener(this);
				
		setTitle("Reserved");  // "super" Frame sets its title
		setSize(1000, 600);        // "super" Frame sets its initial window size
	 
		addWindowListener(this);	 
		setVisible(true);         // "super" Frame shows
	}

	/**
	 * actionPerformed()
	 * Reserves user flight plans. Opens reserved page.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.exit(0);
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