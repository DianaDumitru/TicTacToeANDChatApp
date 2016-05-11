package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import client.ConnectionHandler;
import networking.Player;

public class LoginPanel extends JPanel {

	private final MainFrame rootFrame;
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel wrongLabel;
	private JLabel signUpLabel;
	private JLabel logoLabel;
	private JButton signupButton;
	
	/**
	 * Create the panel.
	 * @param rootFrame
	 */
	public LoginPanel(MainFrame rootFrame) {
		setBackground(Color.WHITE);
		this.rootFrame = rootFrame;
		
		initComponents();
		
	}
	
	private void initComponents(){
		rootFrame.setSize(560,430);
		setBounds(100, 100, 562, 426);
		this.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		//this.rootFrame.setResizable(false);
		setLayout(null);
		
		logoLabel = new JLabel("");
		logoLabel.setIcon(new ImageIcon(".\\res\\logo.png"));
		logoLabel.setBounds(62, 11, 212, 189);
		this.add(logoLabel);
		
		signUpLabel = new JLabel("You don't have an account? ");
		signUpLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		signUpLabel.setForeground(new Color(0, 0, 51));
		signUpLabel.setBounds(310, 75, 175, 20);
		this.add(signUpLabel);
		
		signupButton = new JButton("Sign Up");
		signupButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootFrame.changePanel(new SignupPanel(rootFrame));
			}
		});
		signupButton.setForeground(new Color(0, 0, 51));
		
		signupButton.setBounds(352, 105, 91, 23);
		this.add(signupButton);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setForeground(new Color(0, 0, 51));
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		usernameLabel.setBounds(82, 239, 83, 17);
		this.add(usernameLabel);
		
		usernameField = new JTextField();
		usernameField.setBounds(239, 239, 246, 20);
		this.add(usernameField);
		usernameField.setColumns(10);
		
		wrongLabel = new JLabel("");
		wrongLabel.setForeground(Color.RED);
		wrongLabel.setBounds(82, 214, 403, 14);
		this.add(wrongLabel);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(new Color(0, 0, 51));
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		passwordLabel.setBounds(82, 296, 83, 17);
		this.add(passwordLabel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(240, 296, 246, 20);
		this.add(passwordField);
		
		JButton loginButton = new JButton("Log in");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				wrongLabel.setText("");
				LoginWorker worker = new LoginWorker();
		        try {
		            worker.execute();
		            if(worker.get()){
		                //if (!connectToMasterServer())
		                   // return;
		                rootFrame.changePanel(new MainPanel(rootFrame));
		            }
		        } catch (InterruptedException | ExecutionException ex) {
		            //ConsoleFrame.showError(ex.getMessage());
		        }
			}
		});
		loginButton.setForeground(new Color(0, 0, 51));
		loginButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		this.add(wrongLabel);
		loginButton.setBounds(195, 352, 141, 23);
		this.add(loginButton);
		
	}
	
	
	
	private class LoginWorker extends SwingWorker<Boolean, String>{

        @Override
        protected Boolean doInBackground() throws Exception {
            //This should start false
            boolean success = false;
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if(checkUsername(username)){
            	passwordField.setText("");
                Player.getInstance().setUsername(username);
                Player.getInstance().setPassword(password);
                success = true;
                //String res = ConnectionHandler.getInstance().DatabaseOperations.searchPlayer(Player.getInstance());
                ConnectionHandler.getInstance().sendLoginRequest();
                boolean flag = true;
                String res = "flag";
                while(flag){
                	res = ConnectionHandler.getInstance().readFromServer();
                	if(!res.equals("flag"))
                		flag = false;
                }
                if (res.equals("WRONG PASSWORD") || res.equals("WRONG USERNAME")){
					wrongLabel.setText("WRONG USERNAME OR PASSWORD, TRY AGAIN!");
					usernameField.setText("");
					passwordField.setText("");
					success = false;
                }
            }
            
            return success;
        }
      
        private boolean checkUsername(String username){
            char[] unacceptebleChars = {'\'','=','+',';','\"'};
            for(char c:unacceptebleChars){
                if(username.indexOf(c) >= 0){
                 //ConsoleFrame.showError("Username can't contain these characters: ' = + ; \" ");
                	wrongLabel.setText("Username can't contain these characters: ' = + ; \"");
                	usernameField.setText("");
					passwordField.setText("");
                    return false;
                }
            }
            return true;
        }
        
    }
}
