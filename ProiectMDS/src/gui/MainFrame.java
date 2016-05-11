package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.ConnectionHandler;

public class MainFrame extends JFrame{
	private static MainFrame instance;
	
	private boolean loggedIn = false;
	
	private MainFrame(){
		super("ADD Games");
	}
	
	private void setup(){
		this.setSize(560,430);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new LoginPanel(this));
        
        this.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            closingActionPerformed(e);
	        }

	    });
	}
	
	protected void closingActionPerformed(WindowEvent e) {
		ConnectionHandler.getInstance().signout();
		
	}

	public static MainFrame getInstance(){
        if(instance == null){
            instance = new MainFrame();
            instance.setup();
            //need to tell the engine the window is closing);
            instance.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    /*if(VisualEngine.initialized()){
                        //telling the engine to close with the window
                        VisualEngine.getInstance().dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));    //firing a closing event
                    }*/
                    super.windowClosing(e);
                }

            });
        }
        return instance;
    }
	public void changePanel(JPanel panel){
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        //this.pack();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }
}
