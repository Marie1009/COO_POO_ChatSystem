package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConversationFrame {
	JFrame conversationFrame;
	JScrollPane convPane;
	JTextArea convArea;
	//JTextField messageToSendField ; 
	JButton sendButton;
	JPanel convPanel ;
	public ConversationFrame(String pseudo) {
		//Create and set up the window.
				conversationFrame = new JFrame("Conversation opened with "+pseudo);
				conversationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//	conversationFrame.setPreferredSize(new Dimension(450, 110));
				convPanel = new JPanel(); 
				conversationFrame.add(convPanel) ; 
				
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
	}
}
