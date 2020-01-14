package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.BroadcastListener;
import controller.BroadcastSender;
import controller.MessageWaiter;
import database.DatabaseConnection;
import model.BroadcastType;
import model.User;

public class ChatFrame implements ActionListener, WindowListener{

	private JFrame chatFrame;
	private JPanel listPanel;
	//JPanel convPanel;
	private JScrollPane conversationPane;
	private JTextArea convArea;
	private JButton logoutButton;
	private JMenuBar listOfUsers; 
	private JMenu usersMenu; 
	private JButton refreshButton;
	private MessageWaiter mw ; 
	private String pseudo ; 
	private BroadcastListener bl ; 

	public ChatFrame(String pseudo) {
		this.pseudo = pseudo ; 
		
		bl = new BroadcastListener(this.pseudo)	; 
		BroadcastSender bs = new BroadcastSender(this.pseudo, BroadcastType.NEW_CONNECTION) ; 
		

		//Create and set up the window.
		chatFrame = new JFrame("Chat session opened for "+ this.pseudo);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setSize(new Dimension(120, 40));

		//Create and set up the panel.
		listPanel = new JPanel(new BorderLayout(5,5) );
		//convPanel = new JPanel(new GridLayout(3, 2));

		//Add the widgets.
		addWidgets();

		this.mw = new MessageWaiter() ;
		//get connected users list and display it in the menu bar
		ArrayList<String> users = bl.getListOfConnected();
		//System.out.println(users.get(0));
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
		logoutButton = new JButton("LOGOUT");
		listOfUsers = new JMenuBar();
		usersMenu = new JMenu("Users") ; 
		conversationPane = new JScrollPane();
		convArea = new JTextArea(5,30);
		refreshButton = new JButton("REFRESH");

		listOfUsers.add(usersMenu); 
		listPanel.add(logoutButton);
		listPanel.add(refreshButton);

		//conversationPane.add(messageField);
		conversationPane.add(convArea);
		this.chatFrame.add(conversationPane, BorderLayout.CENTER);
		this.chatFrame.setJMenuBar(listOfUsers);

		//convPanel.add(messageField);
		chatFrame.addWindowListener(this);
		logoutButton.addActionListener(this);
		refreshButton.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);
		if(event.equals("LOGOUT")) {
			chatFrame.setVisible(false);
			WelcomeFrame wf = new WelcomeFrame(); 
		}else if (event.equals("REFRESH")) {

			ArrayList<String> users = bl.getListOfConnected();

			Iterator<String> it = users.iterator() ; 
			while (it.hasNext()) {
				JMenuItem mi = new JMenuItem(it.next()); 
				this.usersMenu.add(mi); 
				mi.addActionListener(this);
			}

			listOfUsers.add(usersMenu); 

			this.chatFrame.setJMenuBar(listOfUsers);


			chatFrame.validate();
			chatFrame.repaint();
		}
		//quand on clique sur un item du menu (un user)
		else {
			try {
				//on met une ip au pif pour commencer et on la set juste après
				User distant = new User(event,InetAddress.getByName("localhost"));
				distant.setIp(InetAddress.getByName(DatabaseConnection.selectIp(distant)));
				//DatabaseConnection.selectAllUsers();
				User self = new User(this.pseudo, InetAddress.getByName("localhost")) ; 
				ConversationFrame cf = new ConversationFrame(distant, self); 
			}catch(Exception ex) {ex.printStackTrace();}


		}


	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("fenêtre fermée chat frame");
		bl.stop() ; 
		mw.stop();
		BroadcastSender bs = new BroadcastSender(pseudo,BroadcastType.USER_LEAVING) ;

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
