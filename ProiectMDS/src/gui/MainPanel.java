package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import client.ConnectionHandler;
import networking.Opponent;
import networking.Player;

public class MainPanel extends JPanel {

	private final MainFrame rootFrame;
	private JPanel profilePanel;
	private JLabel profileLabel;
	private JLabel usernameLabel;
	private JLabel rankLabel;
	private JLabel usernameContentLabel;
	private JLabel rankContentLabel;
	private JList listPlayers;
	private JLabel gamePickLabel;
	private JLabel oponentPickLabel;
	private JButton tictactoeButton;
	private JButton airplanesButton;
	private JLabel tictactoeLabel;
	private JLabel airplanesLabel;
	private JButton signoutButton;
	private JScrollPane scrollPlayers;
	private JButton selectButton;
	
	private List<String> nameList;
	private String selectedName;
	private DefaultListModel listModel = new DefaultListModel();
	private boolean listen;
	private ListenerWorker worker;
	
	
	/**
	 * Create the panel.
	 */
	public MainPanel(MainFrame rootFrame) {
		this.rootFrame = rootFrame;
		
		nameList = new ArrayList<String>();
		nameList.add("mai");
		nameList.add("ddd");
		
		
		initComponents();
		listen = true;
		worker = new ListenerWorker();
        try {
            worker.execute();
        } catch (Exception e) {
        	Logger.getLogger(ListenerWorker.class.getName() + " Failed to read from server in main panel "+ e);
        	System.out.println(" Failed to read from server in main panel");
        }
		
	}
	private void initComponents(){
		
		this.rootFrame.setSize(770,590);
		this.rootFrame.setTitle("ADD Games");
		this.rootFrame.setResizable(false);
		setBounds(100, 100, 770, 590);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		this.setBackground(new Color(0, 0, 51));
		
		profilePanel = new JPanel();
		profilePanel.setBorder(new LineBorder(new Color(100, 149, 237)));
		profilePanel.setBackground(new Color(25, 25, 112));
		profilePanel.setBounds(22, 22, 392, 97);
		this.add(profilePanel);
		profilePanel.setLayout(null);
		
		profileLabel = new JLabel("PROFILE");
		profileLabel.setForeground(new Color(173, 216, 230));
		profileLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		profileLabel.setBounds(10, 11, 102, 23);
		profilePanel.add(profileLabel);
		
		usernameLabel = new JLabel("Username:");
		usernameLabel.setForeground(new Color(173, 216, 230));
		usernameLabel.setBounds(30, 40, 102, 14);
		profilePanel.add(usernameLabel);
		
		rankLabel = new JLabel("Rank:");
		rankLabel.setForeground(new Color(173, 216, 230));
		rankLabel.setBounds(30, 61, 102, 14);
		profilePanel.add(rankLabel);
		
		usernameContentLabel = new JLabel("");
		usernameContentLabel.setForeground(Color.WHITE);
		usernameContentLabel.setText(Player.getInstance().getUsername());
		usernameContentLabel.setBounds(127, 40, 183, 14);
		profilePanel.add(usernameContentLabel);
		
		rankContentLabel = new JLabel("");
		rankContentLabel.setForeground(Color.WHITE);
		rankContentLabel.setText(Integer.toString(Player.getInstance().getRank()));
		rankContentLabel.setBounds(127, 61, 183, 14);
		profilePanel.add(rankContentLabel);
		
		signoutButton = new JButton("Sign out");
		signoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				signoutButtonActionPerformed(evt);
			}
		});
		signoutButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		signoutButton.setForeground(new Color(0, 0, 51));
		signoutButton.setBackground(new Color(100, 149, 237));
		signoutButton.setBounds(293, 11, 89, 23);
		profilePanel.add(signoutButton);
		
		scrollPlayers = new JScrollPane();
		scrollPlayers.setBounds(22, 207, 279, 331);
		this.add(scrollPlayers);
		
		listPlayers = new JList();
		listPlayers.setBorder(new EmptyBorder(5, 8, 5, 5));
		listPlayers.setForeground(Color.PINK);
		listPlayers.setFont(new Font("Tahoma", Font.BOLD, 18));
		listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPlayers.setBackground(new Color(25, 25, 112));
		listPlayers.setBounds(22, 179, 279, 359);
	
		
		listPlayers.setModel(listModel);
		
		
		gamePickLabel = new JLabel("Pick your oponent then....");
		gamePickLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		gamePickLabel.setForeground(new Color(173, 216, 230));
		gamePickLabel.setBounds(22, 140, 250, 28);
		this.add(gamePickLabel);
		
		oponentPickLabel = new JLabel("...pick your favourite game");
		oponentPickLabel.setForeground(new Color(173, 216, 230));
		oponentPickLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		oponentPickLabel.setBounds(355, 140, 228, 29);
		this.add(oponentPickLabel);
		
		tictactoeButton = new JButton("Play>>");
		tictactoeButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		tictactoeButton.setForeground(new Color(0, 0, 51));
		tictactoeButton.setBackground(new Color(100, 149, 237));
		tictactoeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tictactoeButtonActionPerformed(evt);
			}
		});
		tictactoeButton.setBounds(648, 320, 89, 23);
		this.add(tictactoeButton);
		
		airplanesButton = new JButton("Play>>");
		airplanesButton.setForeground(new Color(0, 0, 51));
		airplanesButton.setBackground(new Color(100, 149, 237));
		airplanesButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		airplanesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				airplanesButtonActionPerformed(evt);
			}
		});
		airplanesButton.setBounds(648, 515, 89, 23);
		this.add(airplanesButton);
		
		tictactoeLabel = new JLabel("");
		tictactoeLabel.setBounds(355, 179, 382, 130);
		tictactoeLabel.setIcon(new ImageIcon(".\\res\\tictactoe.png"));
		this.add(tictactoeLabel);
		
		airplanesLabel = new JLabel("");
		airplanesLabel.setBounds(355, 374, 382, 130);
		airplanesLabel.setIcon(new ImageIcon(".\\res\\airplanes.png"));
		this.add(airplanesLabel);
		
		
		scrollPlayers.setViewportView(listPlayers);
		
		selectButton = new JButton("Select");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				selectButtonActionPerformed(evt);
			}
		});
		selectButton.setBounds(256, 145, 89, 23);
		add(selectButton);
		
		JButton refreshUsersButton = new JButton("Rerfresh list");
		refreshUsersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				refreshUsersButtonACtionPerformed(evt);
			}
		});
		refreshUsersButton.setBounds(22, 173, 133, 23);
		add(refreshUsersButton);
		
		
		
	}
	
	protected void refreshUsersButtonACtionPerformed(ActionEvent evt) {
		ConnectionHandler.getInstance().sendMessage("ONLINEUSERS");
	}
	
	protected void selectButtonActionPerformed(ActionEvent evt) {
		int index = listPlayers.getSelectedIndex();
		selectedName = (String)listModel.getElementAt(index);
		Opponent.getInstance().setUsername(selectedName);
	}
	
	protected void airplanesButtonActionPerformed(ActionEvent evt) {
		rootFrame.changePanel(new AirplanesPanel(rootFrame));
	}
	
	protected void tictactoeButtonActionPerformed(ActionEvent evt) {
		listen = false;
		ConnectionHandler.getInstance().sendMessage("STARTGAME");
		//if(!worker.isDone()){
		
			while(!worker.cancel(true));
		//}
			
		rootFrame.changePanel(new TicTacToePanel(rootFrame));
	}
	
	protected void signoutButtonActionPerformed(ActionEvent evt) {
		ConnectionHandler.getInstance().signout();
		
		rootFrame.changePanel(new LoginPanel(rootFrame));
		
	}
	
	private class ListenerWorker extends SwingWorker<Void,String>{
		String messageRecieved = null;
		@Override
		protected Void doInBackground() throws Exception {
			
			while(listen){
				try{
					messageRecieved = ConnectionHandler.getInstance().readFromServer();
					publish(messageRecieved);
				}catch (IOException | ClassNotFoundException e) {
					ConnectionHandler.getInstance().close();
					Logger.getLogger(ListenerWorker.class.getName() + " Failed to read from server "+ e);
                    System.out.println(" Failed to read from server");
					listen = false;
                }
				
			}
			return null;
		}
		
		protected void process(List<String> list)
		{
			if(messageRecieved.startsWith("ONLINEUSERS")){
				String[] users = messageRecieved.split(":");
				listModel.clear();
				int i = 0;
				for(String username:users){
					if(i != 0)
						listModel.addElement(username);
					i++;
				}
			}else{
				System.out.println(messageRecieved);
			}
		}
		
	}
}
