package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import controller.BroadcastSender;
import model.BroadcastType;

public class ChatFrame implements ActionListener{

	JFrame chatFrame;
	JPanel listPanel;
	//JPanel convPanel;
	JScrollPane conversationPane;
	JTextField messageField;
	JTextArea convArea;
	JButton logoutButton;
	JMenuBar listOfUsers; 
	JMenu usersMenu; 
	public ChatFrame(String pseudo) {


		//Create and set up the window.
		chatFrame = new JFrame("Chat session opened for "+ pseudo);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setSize(new Dimension(120, 40));

		//Create and set up the panel.
		listPanel = new JPanel(new BorderLayout(5,5) );
		//convPanel = new JPanel(new GridLayout(3, 2));

		//Add the widgets.
		addWidgets();

		//get connected users list and display it in the menu bar
		BroadcastSender bs = new BroadcastSender(BroadcastType.CONNECTED_USERS) ; 
		ArrayList<String> users = bs.getConnectedUsersAnswer();
		System.out.println(users.get(0));
		Iterator<String> it = users.iterator() ; 
		while (it.hasNext()) {
			JMenuItem mi = new JMenuItem(it.next()); 
			this.usersMenu.add(mi); 
			mi.addActionListener(this);
		}

		//Set the default button.
		chatFrame.getRootPane().setDefaultButton(logoutButton);

		//Add the panel to the window.
		chatFrame.getContentPane().add(listPanel, BorderLayout.WEST);

		//Display the window.
		chatFrame.pack();
		chatFrame.setLocationRelativeTo(null);
		chatFrame.setVisible(true);

	}

	private void addWidgets() {
		/*BroadcastSender bs = new BroadcastSender(BroadcastType.CONNECTED_USERS) ; 
    	ArrayList<String> users = bs.getConnectedUsersAnswer();

    	DefaultListModel<String> l1 = new DefaultListModel<>();  
    	Iterator<String> it = users.iterator() ; 
    	while (it.hasNext()) {
    		l1.addElement(it.next());
    	} 
       	this.listOfUsers = new JList<>(l1);  
        listOfUsers.setBounds(100,100, 75,75); */

		messageField = new JTextField();
		logoutButton = new JButton("LOGOUT");
		listOfUsers = new JMenuBar();
		usersMenu = new JMenu("Users") ; 
		conversationPane = new JScrollPane();
		convArea = new JTextArea(5,30);

		listOfUsers.add(usersMenu); 
		listPanel.add(logoutButton);

		//conversationPane.add(messageField);
		conversationPane.add(convArea);
		this.chatFrame.add(conversationPane, BorderLayout.CENTER);
		this.chatFrame.setJMenuBar(listOfUsers);

		//convPanel.add(messageField);

		logoutButton.addActionListener(this);

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);
		if(event.equals("LOGOUT")) {
			chatFrame.setVisible(false);
			WelcomeFrame wf = new WelcomeFrame(); 
		} else {
			ConversationFrame cf = new ConversationFrame(event); 
		}


	}
}
