package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import networking.Player;

public class ConnectionHandler {

	private final static ConnectionHandler instance = new ConnectionHandler();
    
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String IP = "localhost";
	private int port = 60110;
	private ConnectionHandler() {
        clientSocket = null;
        out = null;
        in = null;
    }
	
	public static ConnectionHandler getInstance() {
        return instance;
    }
	
	private void connectToServer(){
		try
		{
			clientSocket = new Socket(IP, port);
			
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try
		{
			out.close();
			in.close();
			clientSocket.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		if (clientSocket == null){
			connectToServer();
		}
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String readFromServer() throws ClassNotFoundException, IOException{
		if (clientSocket == null)
			connectToServer();
		String result = null;
		synchronized (in) {
			result = (String) in.readObject();
		}
		
		
		return result;
	}
	
	public void sendLoginRequest() throws IOException {
		if(clientSocket == null){
			connectToServer();
		}
		out.writeObject("LOGIN"+Player.getInstance().getUsername() + ";" + Player.getInstance().getPassword());
		out.flush();
	}
	
	public void sendSignupRequest() throws IOException {
		if(clientSocket == null){
			connectToServer();
		}
		out.writeObject("SIGNUP"+Player.getInstance().getUsername() + ";" + Player.getInstance().getPassword());
		out.flush();		
	}
	
	public void signout() {
		if(clientSocket != null)
			sendMessage("SIGNOUT");
		
		close();
	}
	
}
