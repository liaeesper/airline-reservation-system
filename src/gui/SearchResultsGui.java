package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import plans.FlightPlan;
import plans.FlightPlans;
import plans.SearchParams;

public class SearchResultsGui extends JFrame implements ActionListener, WindowListener{
	private JSpinner flightPlanSpinner;
	private FlightPlans fpList; 
	private ArrayList<FlightPlans> fpListArr; 
	private ArrayList<FlightPlan> userChoices;
	private SearchParams userParams;
	private boolean isReturn;
	private boolean hasReturn = false;
	private int modeLocal = 0;
	
	// constants for sort mode selection
	private static final int SORT_BY_PRICE_DESCENDING = 1;
	private static final int SORT_BY_PRICE_ASCENDING = 2;
	private static final int SORT_BY_TIME_DESCENDING = 3;
	private static final int SORT_BY_TIME_ASCENDING = 4;

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor to setup GUI components and event handlers
	public SearchResultsGui (ArrayList<FlightPlans> fpListArr, ArrayList<FlightPlan> userChoices, boolean isReturn, int mode, SearchParams userParams) {
		this.userChoices = userChoices;
		this.userParams = userParams;
		this.fpListArr = fpListArr;
		this.isReturn = isReturn;
		modeLocal = mode;
		if(fpListArr.size() > 1){
			hasReturn = true;
		}
		if(isReturn){
			fpList = fpListArr.get(1);
		}
		else{
			fpList = fpListArr.get(0);
		}
		if(fpList.getFlightPlansList(0).size() == 0){
			// TODO
			// failure message window
			dispose();
			new ErrorMessageGui("There are no flight plans that match these criteria.", true);
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
	    String displayText = "Search Criteria:\n" 
	    					+ userParams.toString()
	    					+ "Search Results:\n"
	    					+ fpList.toString(mode);
	    display.setText(displayText);
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
		
		flightPlanSpinner = new JSpinner( new SpinnerNumberModel(1, 1, fpList.getFlightPlansList(0).size(), 1) );
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
		if(arg0.getActionCommand() == "Submit selection"){
			userChoices.add(fpList.getFlightPlansList(modeLocal).get((int) flightPlanSpinner.getValue() - 1));
			// enter confirmation page if no return flight, otherwise call self?
			if(!isReturn && hasReturn){
				userParams.SetReturnParams(userParams); // convert to return params
				new SearchResultsGui(fpListArr, userChoices, true, 0, userParams);
			}
			else{
				new ConfirmationGui(userChoices);
			}
			dispose();
		}
		else if(arg0.getActionCommand() == "Sort by Price"){
			dispose();
			if(modeLocal == SORT_BY_PRICE_DESCENDING){
				modeLocal = SORT_BY_PRICE_ASCENDING;
			}
			else{
				modeLocal = SORT_BY_PRICE_DESCENDING;
			}
			new SearchResultsGui(fpListArr, userChoices, isReturn, modeLocal, userParams);
		}
		else if(arg0.getActionCommand() == "Sort by Time"){
			if(modeLocal == SORT_BY_TIME_DESCENDING){
				modeLocal = SORT_BY_TIME_ASCENDING;
			}
			else{
				modeLocal = SORT_BY_TIME_DESCENDING;
			}
			new SearchResultsGui(fpListArr, userChoices, isReturn, modeLocal, userParams);
			dispose();
		}
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
