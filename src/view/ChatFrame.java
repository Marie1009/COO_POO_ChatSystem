package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

public class ChatFrame extends TimerTask implements ActionListener {
	private JFrame chatFrame;
	private JTextPane chatDisplay;
	private JTextArea msgArea ;
	
	private User dest;
	private User self; 

	private int numMsg ;
	private Timer timer; 
 

	public ChatFrame(User dest, User self) {
		this.dest = dest;
		this.self = self ; 
		this.numMsg = 0 ;
		//Create and set up the window.
		chatFrame = new JFrame("Chating with "+this.dest.getPseudo());
		chatFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		chatFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	chatFrame.dispose();
		    	System.out.println("fenÃªtre fermÃ©e conversation frame");
				timer.cancel();
				timer.purge(); 

		    }
		});
		
		//Add the widgets.
		addWidgets();
		updateDisplay();

		timer = new Timer(true);
		timer.scheduleAtFixedRate(this, 0, 500);
		//Display the window.
		chatFrame.pack();
		chatFrame.setLocationRelativeTo(null);
		chatFrame.setVisible(true);

	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		updateDisplay();
	}

	private void updateDisplay() {
		
		ArrayList<String> history = DatabaseConnection.selectHistory(self, dest) ; 
		StyleContext sc = StyleContext.getDefaultStyleContext();

		if (history.size() > this.numMsg) {
			
			List<String> subHistory = history.subList(this.numMsg, history.size()) ;
			this.numMsg = history.size() ; 
			
			for (String a : subHistory) {
				String[] n = a.split("\t") ;
				String date = n[3].substring(5, n[3].length()-3) ; 
				if (!n[0].equals(dest.getIp().getHostAddress())) {
					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0,0,204));
					chatDisplay.setCharacterAttributes(aset, false);
					Document doc = chatDisplay.getDocument();
				    try {
						doc.insertString(doc.getLength(), n[2]+"\n   sent at : "+date+"\n", aset);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (n[0].equals(dest.getIp().getHostAddress())) {					
					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(204,0,0));
					chatDisplay.setCharacterAttributes(aset, false);
					Document doc = chatDisplay.getDocument();
					
					try {
						doc.insertString(doc.getLength(),  n[2]+"\n   received at : "+date+"\n", aset);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	}
	private void addWidgets() {
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));
		chatPanel.setBackground(new Color(153,203,255));
		chatFrame.add(chatPanel, BorderLayout.CENTER) ; 


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.setBackground(new Color(153,203,255));
		chatFrame.add(buttonPanel, BorderLayout.PAGE_END) ; 
		JLabel enterLabel = new JLabel("Enter your message : ") ;
		enterLabel.setForeground(new Color(0,76,153));

		chatDisplay = new JTextPane();
		chatDisplay.setEditable(false);
		chatDisplay.setBackground(new Color(236,245,255));
		JScrollPane scrollChatDisplay = new JScrollPane(chatDisplay);
		scrollChatDisplay.setPreferredSize(new Dimension(300,300));

		msgArea = new JTextArea();
		JScrollPane scrollMsgArea = new JScrollPane(msgArea);
		msgArea.setBackground(new Color(204,229,255));
		
		scrollMsgArea.setPreferredSize(new Dimension(300,50));
		JButton sendButton = new JButton("SEND") ; 
		sendButton.addActionListener(this);
		sendButton.setForeground(new Color(0,76,153));
		
		chatPanel.add(scrollChatDisplay);
		chatPanel.add(enterLabel); 
		chatPanel.add(scrollMsgArea);

		buttonPanel.add(sendButton); 
	


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);

		if (event.equals("SEND")) {
			Message message = new Message(msgArea.getText(), dest, self);
			msgArea.setText("");
			MessageSender ms = new MessageSender(message);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateDisplay();
		} 
	}


	public Timer getTimer() {
		return timer;
	}
	
	public JFrame getConversationFrame() {
		return chatFrame;
	}


}
