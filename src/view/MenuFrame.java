package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.BroadcastListener;
import controller.BroadcastSender;
import controller.MessageSender;
import controller.MessageWaiter;
import database.DatabaseConnection;
import model.BroadcastType;
import model.User;

/** Opens when the user is connected. 
 * Shows the connected users and allows to choose one of them to open a new ChatFrame
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class MenuFrame extends TimerTask implements ActionListener, WindowListener{

	private JFrame menuFrame;
	private JList<String> usersList; 

	private MessageWaiter mw ; 
	private int numUsers ; 
	private String pseudo ; 
	private BroadcastListener bl ;
	private Timer timer ; 
	private ArrayList<ChatFrame> activeConv = new ArrayList<ChatFrame>();

	/** Constructor. Sets pseudo of the current user. 
	 * Sets number of displayed users to 0.
	 * 
	 * @param pseudo
	 */
	public MenuFrame(String pseudo) {
		this.pseudo = pseudo ; 
		this.numUsers = 0; 

		this.mw = new MessageWaiter() ;		
		this.bl = new BroadcastListener(this.pseudo)	; 	
		@SuppressWarnings("unused")
		BroadcastSender bs = new BroadcastSender(this.pseudo, BroadcastType.NEW_CONNECTION) ; 


		//Create and set up the window.
		this.menuFrame = new JFrame("Hello "+ this.pseudo);
		this.menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWidget();

		this.timer = new Timer(true);
		this.timer.scheduleAtFixedRate(this, 0, 100);


		//Display the window.
		this.menuFrame.addWindowListener(this);
		this.menuFrame.pack();
		this.menuFrame.setLocationRelativeTo(null);
		this.menuFrame.setVisible(true);


	}

	/** Launches updateUsers()
	 * 
	 */
	@Override
	public void run() {
		updateUsers();
	}

	/** Gets the list of currently connected through the BroadcastListener and prints them all. 
	 * 
	 */
	private void updateUsers() {
		ArrayList<String> users = bl.getListOfConnected();
		if (users.size() != this.numUsers) {
			this.numUsers = users.size(); 
			DefaultListModel<String> l1 = (DefaultListModel<String>) this.usersList.getModel();  
			l1.removeAllElements();
			Iterator<String> it = users.iterator() ; 
			while (it.hasNext()) {
				String l=it.next(); 
				l1.addElement(l); 
				System.out.println(l);
			} 
			this.menuFrame.validate();
			this.menuFrame.repaint();
		}
	}

	/** Adds widgets to the menu frame. 
	 * 
	 */
	private void addWidget() {
		this.usersList = new JList<String>(new DefaultListModel<String>()) ; 
		this.usersList.setBackground(new Color(208,247,228));

		JButton logoutButton = new JButton("LOGOUT");
		logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		logoutButton.addActionListener(this);
		logoutButton.setForeground(new Color(0,102,51));

		JButton startButton = new JButton("Start chat with") ; 
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.addActionListener(this);
		startButton.setForeground(new Color(0,102,51));

		JScrollPane listScroller = new JScrollPane(this.usersList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Lists of connected users");
		label.setLabelFor(this.usersList);
		label.setForeground(new Color(0,102,51));
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		listPane.setBackground(new Color(117,218,167));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(logoutButton);
		buttonPane.add(startButton); 
		buttonPane.setBackground(new Color(117,218,167));

		Container contentPane = menuFrame.getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

	}

	/** Handles the different actions performed. 
	 * LOGOUT or Start chat with (a user need to be selected in the users list)
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand() ; 
		System.out.println(event);
		if(event.equals("LOGOUT")) {
			menuFrame.dispose(); 
			this.windowClosing(null);
			
			@SuppressWarnings("unused")
			WelcomeFrame wf = new WelcomeFrame(); 
		}
		else if (event.equals("Start chat with")){
			if (this.usersList.getSelectedValue()==null) {
				JOptionPane.showMessageDialog(new JFrame(), "You must select a user !");	
			} else {
				try {
					String dest = (String)(this.usersList.getSelectedValue());
					//On met une IP localhost pour commencer et on la set avec la vraie IP juste après
					User distant = new User(dest,InetAddress.getByName("localhost"));
					distant.setIp(InetAddress.getByName(DatabaseConnection.selectIp(distant.getPseudo())));
					User self = new User(this.pseudo, MessageSender.getLocalIp()) ; 
					ChatFrame cf = new ChatFrame(distant, self); 
					activeConv.add(cf);
				}catch(Exception ex) {ex.printStackTrace();}
			}
		}
	}

	/** Stops the BroadcastListener and the MessageWaiter and cancels the timed thread (for updating display)
	 * Closes all of the open ChatFrame and finally sends a USER_LEAVING broadcast. 
	 *  
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		bl.stop() ; 
		mw.stop();
		timer.cancel();
		timer.purge();
		for(ChatFrame cf : activeConv) {
			cf.getChatFrame().dispose();
			cf.getTimer().cancel();
			cf.getTimer().purge() ;
		}

		@SuppressWarnings("unused")
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
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}


}
