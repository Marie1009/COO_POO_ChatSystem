package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.MessageSender;
import controller.MessageWaiter;
import database.DatabaseConnection;
import model.Message;
import model.User;

public class ConversationFrame implements ActionListener, WindowListener{
	private JFrame conversationFrame;
	private JScrollPane convPane;
	private JTextArea convArea;
	//JTextField messageToSendField ; 
	private JButton sendButton;
	private JPanel convPanel ;
	private MessageWaiter mw ;
	private User dest;
	
	public ConversationFrame(User dest) {
		this.dest = dest;
		//Create and set up the window.
		conversationFrame = new JFrame("Conversation opened with "+dest.getPseudo());
		conversationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	conversationFrame.setPreferredSize(new Dimension(450, 110));
		convPanel = new JPanel(); 
		conversationFrame.add(convPanel) ; 

		mw = new MessageWaiter(); 
		//Add the widgets.
		addWidgets();

		//Display the window.
		conversationFrame.pack();
		conversationFrame.setLocationRelativeTo(null);
		conversationFrame.setVisible(true);

	}

	private void addWidgets() {

		convArea = new JTextArea();
		convPane = new JScrollPane(convArea);
		convPane.setPreferredSize(new Dimension(300,50));

		//messageToSendField = new JTextField("default"); 
		sendButton = new JButton("SEND") ; 
		

		JLabel truc = new JLabel("coucou"); 
		convPanel.add(truc); 
		convPanel.add(convPane);
		//convPane.add(messageToSendField); 
		convPanel.add(sendButton); 
		sendButton.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);
		
		if (event.equals("SEND")) {
			
			Message message = new Message(convArea.getText());
			MessageSender ms = new MessageSender(dest,message)  ;
			DatabaseConnection.insertMessage(dest, message);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("fenêtre fermée conversation frame");
		this.mw.stop();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
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
