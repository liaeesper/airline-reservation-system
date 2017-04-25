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
import javax.swing.SwingUtilities;

import dao.ServerInterface;
import plans.FlightPlan;
import plans.Reservation;

public class ConfirmationGui extends JFrame implements ActionListener, WindowListener{
	
	private ArrayList<FlightPlan> user_choices_list;

	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public ConfirmationGui (ArrayList<FlightPlan> user_choices) {
		user_choices_list = user_choices;
		String flightPlansText = "";
		for(int i = 0; i < user_choices.size(); i++){
			flightPlansText += String.format("Flight plan %d:\n", i+1);
			flightPlansText += user_choices.get(i).toString();
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
	    display.setEditable ( false ); // set textArea non-editable
	    display.setText(flightPlansText);
	    JScrollPane scroll = new JScrollPane ( display );
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    display.setCaretPosition(0); // set scroll position to top
		
		add(scroll, gbc);
				
		// confirm selection button
		
		gbc.gridx = 0;
		gbc.gridy = 23;
		Button submitSelectionButton = new Button("Submit selection");
		add(submitSelectionButton, gbc);  
						
		submitSelectionButton.addActionListener(this);
				
		setTitle("Confirmation");  // "super" Frame sets its title
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
		
		Reservation user_plan;
		if(user_choices_list.size() == 1){
			user_plan = new Reservation(false, false, user_choices_list.get(0), null);
		}
		else{
			user_plan = new Reservation(true, false, user_choices_list.get(0), user_choices_list.get(1));
		}
		
		dispose();
		// display a processing message
		LoadingGui loadingPage = new LoadingGui();
		
		Runnable handleReserve = new Runnable() {
			public void run() {
				//TODO
				//// check flight seating here?
				ServerInterface.instance.lock();
				boolean reservationSuccessful = ServerInterface.instance.ReserveTicket(user_plan);
				ServerInterface.instance.unlock();
				if(reservationSuccessful){
					new ReservedGui(user_choices_list, loadingPage);
				}
				else{
					new ErrorMessageGui("Flight could not be reserved.",false);
				}
			}
		};
		
		SwingUtilities.invokeLater(handleReserve);
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