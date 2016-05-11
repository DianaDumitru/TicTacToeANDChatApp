package gui;

import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JScrollPane;

public class AirplanesPanel extends JPanel {

	private MainFrame rootFrame;
	
	/**
	 * Create the panel.
	 */
	public AirplanesPanel(MainFrame rootFrame) {
		this.rootFrame = rootFrame;
		
		initComponents();

	}
	private void initComponents(){
		this.rootFrame.setTitle("Airplanes Game");
		this.rootFrame.setSize(920,560);
		//this.rootFrame.setResizable(false);
		setBounds(100, 100, 920, 560);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setBackground(new Color(0, 0, 51));
		setLayout(null);
		
		JButton backButton = new JButton("<<BACK");
		backButton.setForeground(new Color(0, 0, 51));
		backButton.setBackground(new Color(100, 149, 237));
		backButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootFrame.changePanel(new MainPanel(rootFrame));
			}
		});
		backButton.setBounds(10, 11, 89, 23);
		add(backButton);
		
		JLabel vsLabel = new JLabel("VS.");
		vsLabel.setForeground(new Color(173, 216, 230));
		vsLabel.setFont(new Font("Bradley Hand ITC", Font.BOLD, 35));
		vsLabel.setBounds(401, 28, 64, 29);
		add(vsLabel);
		
		JLabel player1Label = new JLabel("");
		player1Label.setForeground(new Color(173, 216, 230));
		player1Label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		player1Label.setBounds(549, 28, 111, 29);
		add(player1Label);
		
		JLabel player2Label = new JLabel("");
		player2Label.setForeground(new Color(173, 216, 230));
		player2Label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		player2Label.setHorizontalAlignment(SwingConstants.RIGHT);
		player2Label.setBounds(206, 28, 111, 29);
		add(player2Label);
		
		JTextArea chatArea = new JTextArea();
		chatArea.setBounds(646, 75, 254, 378);
		chatArea.setBackground(new Color(240, 248, 255));
		add(chatArea);
		
		JTextField messageField = new JTextField();
		messageField.setBounds(646, 464, 171, 46);
		messageField.setBackground(new Color(240, 248, 255));
		add(messageField);
		messageField.setColumns(10);
		
		JButton sendButton = new JButton("SEND");
		sendButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		sendButton.setForeground(new Color(0, 0, 51));
		sendButton.setBounds(827, 464, 73, 46);
		add(sendButton);
		
		JPanel gamePanel = new JPanel();
		gamePanel.setBounds(10, 75, 616, 435);
		gamePanel.setBackground(new Color(100, 149, 237));
		add(gamePanel);
	}
}
