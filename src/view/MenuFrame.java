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

public class MenuFrame extends TimerTask implements ActionListener, WindowListener{

	private JFrame menuFrame;
	private JButton logoutButton;
	private int numUsers ; 
	private JList<String> usersList; 
	private MessageWaiter mw ; 
	private String pseudo ; 
	private BroadcastListener bl ;
	private JScrollPane listScroller;
	private JButton startButton; 
	private Timer timer ; 
	private ArrayList<ChatFrame> activeConv = new ArrayList<ChatFrame>();

	public MenuFrame(String pseudo) {
		this.pseudo = pseudo ; 

		bl = new BroadcastListener(this.pseudo)	; 
		BroadcastSender bs = new BroadcastSender(this.pseudo, BroadcastType.NEW_CONNECTION) ; 
		this.numUsers = 0; 
		//Create and set up the window.
		menuFrame = new JFrame("Hello "+ this.pseudo);
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWidget();
		this.mw = new MessageWaiter() ;

		timer = new Timer(true);
		timer.scheduleAtFixedRate(this, 0, 100);

		
		//Display the window.
		menuFrame.addWindowListener(this);
		menuFrame.pack();
		menuFrame.setLocationRelativeTo(null);
		menuFrame.setVisible(true);


	}


	@Override
	public void run() {
		// TODO Auto-generated method stub

		//System.out.println("pudate users");
		ArrayList<String> users = bl.getListOfConnected();
		if (users.size() != this.numUsers) {
			this.numUsers = users.size(); 
			DefaultListModel<String> l1 = (DefaultListModel<String>) usersList.getModel();  
			l1.removeAllElements();
			Iterator<String> it = users.iterator() ; 
			while (it.hasNext()) {
				String l=it.next(); 
				l1.addElement(l); 
				System.out.println(l);
			} 
			menuFrame.validate();
			menuFrame.repaint();
		}

	}

	private void addWidget() {
		usersList = new JList<String>(new DefaultListModel<String>()) ; 
		usersList.setBackground(new Color(208,247,228));
		
		logoutButton = new JButton("LOGOUT");
		logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		logoutButton.addActionListener(this);
		logoutButton.setForeground(new Color(0,102,51));

		startButton = new JButton("Start chat with") ; 
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.addActionListener(this);
		startButton.setForeground(new Color(0,102,51));

		listScroller = new JScrollPane(usersList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Lists of connected users");
		label.setLabelFor(usersList);
		label.setForeground(new Color(0,102,51));
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		listPane.setBackground(new Color(117,218,167));

		//Lay out the buttons from left to right.
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);
		if(event.equals("LOGOUT")) {
			menuFrame.dispose(); 
			this.windowClosing(null);
			WelcomeFrame wf = new WelcomeFrame(); 
			}
		//quand on clique sur un item du menu (un user)
		else if (event.equals("Start chat with")){
			if (this.usersList.getSelectedValue()==null) {
				JOptionPane.showMessageDialog(new JFrame(), "You must select a user !");	
			} else {
				try {
					String dest = (String)(this.usersList.getSelectedValue());
					//on met une ip au pif pour commencer et on la set juste après
					User distant = new User(dest,InetAddress.getByName("localhost"));
					distant.setIp(InetAddress.getByName(DatabaseConnection.selectIp(distant.getPseudo())));
					//DatabaseConnection.selectAllUsers();
					User self = new User(this.pseudo, MessageSender.getLocalIp()) ; 
					ChatFrame cf = new ChatFrame(distant, self); 
					activeConv.add(cf);
				}catch(Exception ex) {ex.printStackTrace();}
			}

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
		timer.cancel();
		timer.purge();
		for(ChatFrame cf : activeConv) {
			cf.getConversationFrame().dispose();
			cf.getTimer().cancel();
			cf.getTimer().purge() ;
		}
		
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
