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

public class WelcomeFrame implements ActionListener{
	JFrame firstpageFrame;
	JTextField pseudoField;
	JLabel welcomeLabel;
	JButton loginButton;

	public WelcomeFrame() {
		//Create and set up the window.
		firstpageFrame = new JFrame("ChAtSysTem");
		firstpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		firstpageFrame.setPreferredSize(new Dimension(300, 100));
		firstpageFrame.getContentPane().setBackground(new Color(255, 204, 229));
		//Add the widgets.
		addWidgets();

		//Add the panel to the window.
		firstpageFrame.getContentPane().setLayout(new BoxLayout(firstpageFrame.getContentPane(), BoxLayout.Y_AXIS));

		//Display the window.
		firstpageFrame.pack();
		firstpageFrame.setLocationRelativeTo(null);
		firstpageFrame.setVisible(true);
	}



	private void addWidgets() {
		welcomeLabel = new JLabel("Enter your pseudo");
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		welcomeLabel.setForeground(new Color(153,0,76));
		
		pseudoField = new JTextField();
		pseudoField.setBackground(new Color(252,239,245));
		pseudoField.setAlignmentX(Component.CENTER_ALIGNMENT);
		pseudoField.setPreferredSize(new Dimension(100, 30));
		pseudoField.setBorder(BorderFactory.createLineBorder(new Color(153,0,76)));

		loginButton = new JButton("LOGIN");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setForeground(new Color(153,0,76));
		
		firstpageFrame.getContentPane().add(welcomeLabel);
		firstpageFrame.getContentPane().add(pseudoField);
		firstpageFrame.getContentPane().add(loginButton);
		
		loginButton.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("click");
		String pseudo = pseudoField.getText();
		if (pseudo.length()<3) {
			JOptionPane.showMessageDialog(new JFrame(), "Pseudo must be at least 3 characters");
		} else {
			BroadcastSender bs = new BroadcastSender(pseudo, BroadcastType.PSEUDO_UNIQUE);
			if (bs.isUnique()) {
				MenuFrame cf = new MenuFrame(pseudo);
				firstpageFrame.setVisible(false);
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Pseudo Already Used");
			}
		}
	}
}
