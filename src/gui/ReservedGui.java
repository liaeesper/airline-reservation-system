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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import plans.FlightPlan;

public class ReservedGui extends JFrame implements ActionListener, WindowListener{
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public ReservedGui (ArrayList<FlightPlan> userChoices, LoadingGui loadingPage) {
		loadingPage.dispose();
		String flightPlansText = "";
		for(int i = 0; i < userChoices.size(); i++){
			flightPlansText += String.format("Flight plan %d:\n", i+1);
			flightPlansText += userChoices.get(i).toString();
		}
		// TODO
		// error message if either choice is no longer free, also lock
		
		setLayout(new GridBagLayout());
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
	    display.setEditable ( false ); 
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
				
		setTitle("Reserved");  
		setSize(1000, 600);        // initial window size
	 
		addWindowListener(this);	 
		setVisible(true);         // Frame shows
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