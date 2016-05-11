package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import client.ConnectionHandler;
import networking.Opponent;
import networking.Player;

public class TicTacToePanel extends JPanel {

	private final MainFrame rootFrame;
	private JTextField messageField;
	private JButton sendButton;
	private JPanel gamePanel;
	private JTextArea chatArea;
	private JLabel player2Label;
	private JLabel player1Label;
	private JLabel vsLabel;
	private JButton backButton;
	
	/**
	 * Create the panel.
	 */
	public TicTacToePanel(MainFrame rootFrame) {
		this.rootFrame = rootFrame;
		this.rootFrame.setTitle("Tic Tac Toe Game");
		this.rootFrame.setSize(790,560);
		//this.rootFrame.setResizable(false);
		setBounds(100, 100, 790, 560);
		this.setBackground(new Color(0, 0, 51));
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		initComponents();
		chatArea.setEditable(false);
		player1Label.setText(Player.getInstance().getUsername());
		player2Label.setText(Opponent.getInstance().getUsername());
		ListenerWorker worker = new ListenerWorker();
        try {
            worker.execute();
        } catch (Exception e) {
        	Logger.getLogger(ListenerWorker.class.getName() + " Failed to read from server "+ e);
        	System.out.println(" Failed to read from server ");
        }
	}
	
	private void initComponents(){
		backButton = new JButton("<<BACK");
		backButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		backButton.setForeground(new Color(0, 0, 51));
		backButton.setBackground(new Color(100, 149, 237));
		backButton.setBounds(10, 11, 89, 23);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootFrame.changePanel(new MainPanel(rootFrame));
			}
		});
		setLayout(null);
		add(backButton);
		
		vsLabel = new JLabel("VS.");
		vsLabel.setForeground(new Color(173, 216, 230));
		vsLabel.setFont(new Font("Bradley Hand ITC", Font.BOLD | Font.ITALIC, 35));
		vsLabel.setBounds(357, 45, 64, 29);
		add(vsLabel);
		
		player1Label = new JLabel("");
		player1Label.setForeground(new Color(173, 216, 230));
		player1Label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		player1Label.setBounds(471, 45, 111, 29);
		add(player1Label);
		
		player2Label = new JLabel("");
		player2Label.setForeground(new Color(173, 216, 230));
		player2Label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		player2Label.setHorizontalAlignment(SwingConstants.RIGHT);
		player2Label.setBounds(196, 45, 111, 29);
		add(player2Label);
		
		chatArea = new JTextArea();
		chatArea.setBounds(470, 103, 284, 343);
		chatArea.setBackground(new Color(240, 248, 255));
		add(chatArea);
		
		messageField = new JTextField();
		messageField.setBounds(470, 457, 202, 46);
		messageField.setBackground(new Color(240, 248, 255));
		add(messageField);
		messageField.setColumns(10);
		
		sendButton = new JButton("SEND");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SendMessageWorker worker = new SendMessageWorker();
				worker.execute();
			}
		});
		sendButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		sendButton.setForeground(new Color(0, 0, 51));
		sendButton.setBounds(682, 456, 72, 46);
		add(sendButton);
		
		gamePanel = new JPanel();
		gamePanel.setBounds(30, 103, 391, 399);
		gamePanel.setBackground(new Color(100, 149, 237));
		add(gamePanel);
	}
	private class SendMessageWorker extends SwingWorker<Void, String>{
		String message = null;
		@Override
		protected Void doInBackground() throws Exception 
		{
			message = "SENDTO" + Opponent.getInstance().getUsername() + "/";
			message += messageField.getText();
			messageField.setText("");
			ConnectionHandler.getInstance().sendMessage(message);
			return null;
		}
		
		protected void done()
		{
			chatArea.append(Player.getInstance().getUsername() +": "+ message + "\n");
			
		}
	}
	private class ListenerWorker extends SwingWorker<Void,String>{
		String messageRecieved = null;

		@Override
		protected Void doInBackground() throws Exception {
			boolean listen = true;
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
			chatArea.append(Opponent.getInstance().getUsername() + ":" + messageRecieved + "\n");
		}
		
	}
}
