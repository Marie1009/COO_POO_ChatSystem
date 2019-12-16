package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.BroadcastSender;
import model.BroadcastType;

public class ChatFrame implements ActionListener{
	
	JFrame chatFrame;
    JPanel listPanel;
    //JPanel convPanel;

    JTextField messageField;
    JLabel userlistLabel;
    JButton logoutButton;
	
    public ChatFrame(String pseudo) {
    	
    	BroadcastSender bs = new BroadcastSender(BroadcastType.CONNECTED_USERS) ; 
    	
        //Create and set up the window.
        chatFrame = new JFrame("Chat session opened for "+ pseudo);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(new Dimension(120, 40));

        //Create and set up the panel.
        listPanel = new JPanel(new GridLayout(2, 2));
        //convPanel = new JPanel(new GridLayout(3, 2));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        chatFrame.getRootPane().setDefaultButton(logoutButton);

        //Add the panel to the window.
        chatFrame.getContentPane().add(listPanel, BorderLayout.WEST);
        //chatFrame.getContentPane().add(convPanel, BorderLayout.EAST);

        //Display the window.
        chatFrame.pack();
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setVisible(true);
        
    }
	
    private void addWidgets() {
        messageField = new JTextField();
        userlistLabel = new JLabel("List of connected users :");
        logoutButton = new JButton("LOGOUT");
        listPanel.add(logoutButton);
        listPanel.add(messageField);
        listPanel.add(userlistLabel);

       
        
        //convPanel.add(messageField);
        
        
        
        userlistLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    	logoutButton.addActionListener(this);
    }
    

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		chatFrame.setVisible(false);
		WelcomeFrame wf = new WelcomeFrame(); 
		
		
	}
}
