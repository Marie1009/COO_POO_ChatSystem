package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.BroadcastSender;
import controller.MessageWaiter;
import model.BroadcastType;
import model.User;

public class WelcomeFrame implements ActionListener, WindowListener {
	JFrame firstpageFrame;
	JTextField pseudoField;
	JLabel welcomeLabel;
	JButton loginButton;

	public WelcomeFrame() {
		//Create and set up the window.
		firstpageFrame = new JFrame("ChAtSysTem");
		firstpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		firstpageFrame.setPreferredSize(new Dimension(300, 100));

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
		
		pseudoField = new JTextField();
		pseudoField.setAlignmentX(Component.CENTER_ALIGNMENT);
		pseudoField.setPreferredSize(new Dimension(100, 30));
		pseudoField.setBorder(BorderFactory.createLineBorder(Color.RED));

		loginButton = new JButton("LOGIN");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setForeground(Color.MAGENTA);
		loginButton.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		firstpageFrame.getContentPane().add(welcomeLabel);
		firstpageFrame.getContentPane().add(pseudoField);
		firstpageFrame.getContentPane().add(loginButton);
		
		loginButton.addActionListener(this);
		firstpageFrame.addWindowListener(this);
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
			if (bs.getBroadcastAnswer()) {
				ChatFrame cf = new ChatFrame(pseudo);
				firstpageFrame.setVisible(false);
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Pseudo Already Used");
			}
		}
	}
	
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
    }



	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	
	}



	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("fenêtre fermée welcom frame");
	}



	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



}
