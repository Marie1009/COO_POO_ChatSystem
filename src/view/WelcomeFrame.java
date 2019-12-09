package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WelcomeFrame {
	JFrame firstpageFrame;
    JPanel pagePanel;
    JTextField pseudoField;
    JLabel welcomeLabel;
    JButton loginButton;
	
	
    public WelcomeFrame() {
        //Create and set up the window.
        firstpageFrame = new JFrame("WelcomeFrame");
        firstpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        firstpageFrame.setSize(new Dimension(120, 40));

        //Create and set up the panel.
        pagePanel = new JPanel(new GridLayout(3, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        firstpageFrame.getRootPane().setDefaultButton(loginButton);

        //Add the panel to the window.
        firstpageFrame.getContentPane().add(pagePanel, BorderLayout.CENTER);

        //Display the window.
        firstpageFrame.pack();
        firstpageFrame.setVisible(true);
    }
    
    
    
    private void addWidgets() {
        pseudoField = new JTextField();
        welcomeLabel = new JLabel("Enter your pseudo");
        loginButton = new JButton("LOGIN");
        pagePanel.add(welcomeLabel);
        pagePanel.add(pseudoField);
        
        pagePanel.add(loginButton);
        
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    	
    }
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
    }
    
   
  
}
