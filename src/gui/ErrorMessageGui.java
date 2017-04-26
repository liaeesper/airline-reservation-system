package gui;

import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author Team G
 * Displays an error message of choice, including a special page for returning the user 
 * to the search page.
 */
public class ErrorMessageGui extends JFrame implements ActionListener, WindowListener{
	private static final long serialVersionUID = 1L;

	/** Constructor to setup GUI components and event handlers
	 * 
	 * @param errorMessage			A string to display as an error message.
	 * @param suggestAlternative    Set to true for suggesting a search for alternative flights 
	 * 								if none are found. If set to false, shows errorMessage.
	 */
	public ErrorMessageGui (String errorMessage, boolean suggestAlternative) {		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.5;
		
		// error message
		
		if(suggestAlternative){
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(new JLabel("Try searching for flights with a different seating type:"), gbc); 
			
			gbc.gridx = 1;
			Button backButton = new Button("Back to Search");
			add(backButton, gbc);  
			
			backButton.addActionListener(this);
		}
		else{
			gbc.gridx = 1;
			gbc.gridy = 1;
			add(new JLabel(errorMessage, SwingConstants.CENTER), gbc); 
		}
										
		setTitle("Error");  // "super" Frame sets its title
		setSize(500, 300);        // "super" Frame sets its initial window size
	 
		addWindowListener(this);	 
		setVisible(true);         // "super" Frame shows
	}

	/**
	 * actionPerformed()
	 * Sends the user back to the search page.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		dispose();
		new SearchGui();
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