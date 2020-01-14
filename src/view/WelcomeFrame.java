package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;

import javax.swing.BorderFactory;
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
		firstpageFrame.setLocationRelativeTo(null);
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
		loginButton.addActionListener(this);
		firstpageFrame.addWindowListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("click");
		String pseudo = pseudoField.getText();
		if (pseudo.length()<5) {
			JOptionPane.showMessageDialog(new JFrame(), "Pseudo must be at least 5 characters");
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
