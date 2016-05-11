package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import networking.Server;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerFrame extends JFrame {

	private JPanel contentPane;
	public JButton stopServerButton;
	public JTextArea serverLoggerField;

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setTitle("Server ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		stopServerButton = new JButton("Stop server");
		stopServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				stopServerButtonActionPerformed(evt);
			}
		});
		stopServerButton.setBounds(345, 0, 89, 23);
		contentPane.add(stopServerButton);
		
		serverLoggerField = new JTextArea();
		serverLoggerField.setBounds(0, 27, 424, 234);
		contentPane.add(serverLoggerField);
		
		this.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            closingActionPerformed(e);
	        }

	    });
		
	}

	protected void stopServerButtonActionPerformed(ActionEvent evt) {
		Server.getInstance().stop();
		
	}

	protected void closingActionPerformed(WindowEvent e) {
		Server.getInstance().stop();
	}
	
}
