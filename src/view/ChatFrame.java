package view;

import java.awt.BorderLayout;
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
import controller.MessageWaiter;
import database.DatabaseConnection;
import model.BroadcastType;
import model.User;

public class ChatFrame extends TimerTask implements ActionListener, WindowListener{

	private JFrame chatFrame;
	private JButton logoutButton;
	private int numUsers ; 
	private JList<String> usersList; 
	private MessageWaiter mw ; 
	private String pseudo ; 
	private BroadcastListener bl ;
	private JScrollPane listScroller;
	private JButton startButton; 
	private Timer timer ; 

	public ChatFrame(String pseudo) {
		this.pseudo = pseudo ; 

		bl = new BroadcastListener(this.pseudo)	; 
		BroadcastSender bs = new BroadcastSender(this.pseudo, BroadcastType.NEW_CONNECTION) ; 

		this.numUsers = 0; 
		//Create and set up the window.
		chatFrame = new JFrame("Chat session opened for "+ this.pseudo);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//chatFrame.setPreferredSize(new Dimension(250, 100));
		usersList = new JList<String>(new DefaultListModel<String>()) ; 

		logoutButton = new JButton("LOGOUT");
		logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		logoutButton.addActionListener(this);

		startButton = new JButton("Start chat with") ; 
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.addActionListener(this);

		listScroller = new JScrollPane(usersList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);

		//Create a container so that we can add a title around
		//the scroll pane.  Can't add a title directly to the
		//scroll pane because its background would be white.
		//Lay out the label and scroll pane from top to bottom.
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Lists of connected users");
		label.setLabelFor(usersList);
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		//Add the widgets.
		//addWidgets();

		this.mw = new MessageWaiter() ;

		timer = new Timer(true);
		timer.scheduleAtFixedRate(this, 0, 100);

		//Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(logoutButton);
		buttonPane.add(startButton); 

		Container contentPane = chatFrame.getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

		//Display the window.
		chatFrame.addWindowListener(this);
		chatFrame.pack();
		chatFrame.setLocationRelativeTo(null);
		chatFrame.setVisible(true);


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
			chatFrame.validate();
			chatFrame.repaint();
		}

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String event = e.getActionCommand() ; 
		System.out.println(event);
		if(event.equals("LOGOUT")) {
			chatFrame.dispose(); 
			WelcomeFrame wf = new WelcomeFrame(); }
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
					User self = new User(this.pseudo, InetAddress.getByName("localhost")) ; 
					ConversationFrame cf = new ConversationFrame(distant, self); 
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
		BroadcastSender bs = new BroadcastSender(pseudo,BroadcastType.USER_LEAVING) ;

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("fenêtre fermée chat frame");
		bl.stop() ; 
		mw.stop();
		timer.cancel();
		timer.purge();
		BroadcastSender bs = new BroadcastSender(pseudo,BroadcastType.USER_LEAVING) ;
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
