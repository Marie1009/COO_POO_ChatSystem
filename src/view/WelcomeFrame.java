package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.BroadcastSender;
import model.BroadcastType;

/** First frame to be displayed when app is launched. Allows user to choose a unique pseudo and connect. 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class WelcomeFrame implements ActionListener{
	private JFrame firstpageFrame;
	private JTextField pseudoField;

	/** Constructor. Creates and sets up the windows, adds the widgets and displays the window. 
	 * 
	 */
	public WelcomeFrame() {
		//Create and set up the window.
		this.firstpageFrame = new JFrame("ChAtSysTem");
		this.firstpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.firstpageFrame.setPreferredSize(new Dimension(300, 100));
		this.firstpageFrame.getContentPane().setBackground(new Color(255, 204, 229));
		this.firstpageFrame.getContentPane().setLayout(new BoxLayout(this.firstpageFrame.getContentPane(), BoxLayout.Y_AXIS));

		//Add the widgets.
		addWidgets();

		//Display the window.
		this.firstpageFrame.pack();
		this.firstpageFrame.setLocationRelativeTo(null);
		this.firstpageFrame.setVisible(true);
	}

	/** Adds widgets (welcome label, pseudo field and log in button). 
	 * 
	 */
	private void addWidgets() {
		JLabel welcomeLabel = new JLabel("Enter your pseudo");
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		welcomeLabel.setForeground(new Color(153,0,76));
		
		this.pseudoField = new JTextField();
		this.pseudoField.setBackground(new Color(252,239,245));
		this.pseudoField.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.pseudoField.setPreferredSize(new Dimension(100, 30));
		this.pseudoField.setBorder(BorderFactory.createLineBorder(new Color(153,0,76)));

		JButton loginButton = new JButton("LOGIN");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setForeground(new Color(153,0,76));
		
		this.firstpageFrame.getContentPane().add(welcomeLabel);
		this.firstpageFrame.getContentPane().add(this.pseudoField);
		this.firstpageFrame.getContentPane().add(loginButton);
		
		loginButton.addActionListener(this);
	}

	/** When LOGIN button is clicked, checks if the pseudo is long enough and sends a PSEUDO_UNIQUE broadcast.
	 * If pseudo unique, launches MenuFrame.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String pseudo = this.pseudoField.getText();
		if (pseudo.length()<3) {
			JOptionPane.showMessageDialog(new JFrame(), "Pseudo must be at least 3 characters");
		} else {
			BroadcastSender bs = new BroadcastSender(pseudo, BroadcastType.PSEUDO_UNIQUE);
			if (bs.isUnique()) {
				
				@SuppressWarnings("unused")
				MenuFrame cf = new MenuFrame(pseudo);
				this.firstpageFrame.setVisible(false);
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Pseudo Already Used");
			}
		}
	}
}
