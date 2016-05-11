package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread{
	
	private String username;
	protected Socket clientSocket;
	private ObjectInputStream in = null;
	public ObjectOutputStream out = null;
    //protected Thread clientThread;
    protected volatile boolean threadRunning;
    //protected AtomicInteger inactivityLevel;
    //protected static final int MAX_INACTIVITY_LEVEL = 2;
    protected boolean activeConnection;
    private boolean identityConfirmed;

	public Connection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.username = "Anonymous";
        this.identityConfirmed = false;
	}

	
	public void run() {
		threadRunning = true;
		String request = null;
		try{
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			while(!identityConfirmed){
				request = (String) in.readObject();
				int delimIndex = request.indexOf(";");
				String username;
				String password;
				
				if (request.startsWith("LOGIN")){
					
					username = request.substring(5,  delimIndex);
					password = request.substring(delimIndex + 1, request.length());
					String loginResult = Server.getInstance().logIn(username,password);
					if(loginResult == "OK"){
						identityConfirmed = true;
						this.username = username;
					}
					out.writeObject(loginResult);
					out.flush();
				}
				if(request.startsWith("SIGNUP")){
					
					username = request.substring(6,  delimIndex);
					password = request.substring(delimIndex + 1, request.length());
					String signupResult = Server.getInstance().signup(username,password);
					if(signupResult == "OK"){
						identityConfirmed = true;
						this.username = username;
					}
					out.writeObject(signupResult);
					out.flush();
				}
			}
			while(true){
				request = (String) in.readObject();
				if(request.startsWith("ONLINEUSERS")){
					String onlineUsers = "ONLINEUSERS";
					onlineUsers += Server.getInstance().getOnlineUsers(username);
					out.reset();
					out.writeObject(onlineUsers);
					out.flush();
				}
				
				if(request.startsWith("STARTGAME")){
					out.writeObject("GOGO");;
				}
				
				if (request.contains("SIGNOUT"))
					break;
				if (request.startsWith("SENDTO")){
					int delimIndex = request.indexOf("/");
					String opponent = request.substring(6,  delimIndex);
					String message = request.substring(delimIndex + 1, request.length());
					Server.getInstance().sendMessageToOpponent(opponent,message);
				}
			}
			Server.getInstance().removeConnection(username);
			signout();
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void signout()
	{
		try{
			in.close();
			out.close();
			clientSocket.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

}
