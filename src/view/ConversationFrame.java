package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import controller.MessageSender;
import database.DatabaseConnection;
import model.Message;
import model.User;

public class ConversationFrame extends TimerTask implements ActionListener, WindowListener {
	private JFrame conversationFrame;
	private JScrollPane convPane;
	private JTextArea convArea;
	//JTextField messageToSendField ; 
	private JButton sendButton;
	private JPanel convPanel ;
	private User dest;
	private User self; 
	private JLabel enterMsg ; 
	//private JTextArea convAreaDisplay;
	private JTextPane convDisplay;
	private JScrollPane convPaneDisplay;
	private int numMsg ;
	private Timer timer; 
	private JButton quitButton ;
	private JPanel buttonPanel;

	public ConversationFrame(User dest, User self) {
		this.dest = dest;
		this.self = self ; 
		this.numMsg = 0 ;
		//Create and set up the window.
		conversationFrame = new JFrame("Conversation opened with "+this.dest.getPseudo());
		conversationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	conversationFrame.setPreferredSize(new Dimension(450, 110));
		convPanel = new JPanel();
		convPanel.setLayout(new BoxLayout(convPanel, BoxLayout.PAGE_AXIS));
		conversationFrame.add(convPanel, BorderLayout.CENTER) ; 


		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalGlue());
		conversationFrame.add(buttonPanel, BorderLayout.PAGE_END) ; 
		//Add the widgets.
		addWidgets();
		updateDisplay();

		timer = new Timer(true);
		timer.scheduleAtFixedRate(this, 0, 500);
		//Display the window.
		//conversationFrame.setUndecorated(true);
		conversationFrame.pack();
		conversationFrame.setLocationRelativeTo(null);
		conversationFrame.setVisible(true);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub*
		//System.out.println("jupdate la converaea");
		updateDisplay();
	}

	private void updateDisplay() {
		
		ArrayList<String> history = DatabaseConnection.selectHistory(self, dest) ; 
		StyleContext sc = StyleContext.getDefaultStyleContext();


		if (history.size() > this.numMsg) {
			this.numMsg = history.size() ; 
			convDisplay.setText("");
			
			for (String a : history) {
				String[] n = a.split("\t") ; 
				if (n[0].equals(self.getPseudo())) {
					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
					convDisplay.setCharacterAttributes(aset, false);
					Document doc = convDisplay.getDocument();
				    try {
						doc.insertString(doc.getLength(), n[2]+" sent at : "+n[3]+"\n", aset);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (n[0].equals(dest.getPseudo())) {					
					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.GRAY);
					convDisplay.setCharacterAttributes(aset, false);
					Document doc = convDisplay.getDocument();
					try {
						doc.insertString(doc.getLength(),  n[2]+" received at : "+n[3]+"\n", aset);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				//convAreaDisplay.append(a+"\n");
			}
		}
	}
	private void addWidgets() {
		enterMsg = new JLabel("Enter your message : ") ;
		enterMsg.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, Color.ORANGE, Color.orange));
		quitButton = new JButton("Quit chat") ; 
		quitButton.addActionListener(this);

		convDisplay = new JTextPane();
		convDisplay.setEditable(false);
		convPaneDisplay = new JScrollPane(convDisplay);
		//convAreaDisplay = new JTextArea();
		//convAreaDisplay.setEditable(false);
		//convPaneDisplay = new JScrollPane(convAreaDisplay);

		convPaneDisplay.setPreferredSize(new Dimension(400,300));

		convArea = new JTextArea();
		convArea.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, Color.CYAN, Color.CYAN));
		convPane = new JScrollPane(convArea);
		convPane.setPreferredSize(new Dimension(150,50));
		//messageToSendField = new JTextField("default"); 
		sendButton = new JButton("SEND") ; 
		sendButton.addActionListener(this);

		convPanel.add(convPaneDisplay);
		convPanel.add(enterMsg); 
		convPanel.add(convPane);

		//convPane.add(messageToSendField); 
		buttonPanel.add(sendButton); 
		buttonPanel.add(quitButton); 


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);

		if (event.equals("SEND")) {
			Message message = new Message(convArea.getText(), dest, self);
			convArea.setText("");
			MessageSender ms = new MessageSender(message);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateDisplay();
		} else if(event.equals("Quit chat")) {
			conversationFrame.dispose();
			this.windowClosing(null);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("fenÃªtre fermÃ©e conversation frame");
		timer.cancel();
		timer.purge(); 
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
