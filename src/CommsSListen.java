import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class CommsSListen implements Runnable {
	
	private Socket soc;
	private User u;
	private Server s;
	private Comms c;
	
	public CommsSListen(Socket sc, Server sv, Comms c) {
		soc = sc;
		this.s = sv;
		this.c = c;
	}
	
	public CommsSListen(Socket sc, Server sv, Comms c, User u) {
		soc = sc;
		this.s = sv;
		this.c = c;
		this.u = u;
	}
	
	private boolean isSocketConnected() throws IOException{
		try {
			if(!soc.isConnected()) {
				
				s.dcUser(u);
				return false;
			}
			else return true;
		}	
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An error occured whilst trying to remove a socket");
			return false;
		}
	}
	
	//Waits for input, then passes message to server
	public void run() {
		s.gui.write("Connection");
		Message msg;
		ObjectInputStream ois;
		msg = null;
		try {
			ois = new ObjectInputStream(soc.getInputStream());
			while(true) {
				if(isSocketConnected()) {
					msg = (Message) ois.readObject();
					if (msg instanceof RegMessage) {
						c.addSoc(((RegMessage) msg).getName(), soc);
					}
					if (msg instanceof LoginMessage) {
						c.addSoc(((LoginMessage) msg).getUser(), soc);
					}
					s.processMsg(msg);
				}
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "error occured listening for messages (SERVER)");
			e.printStackTrace();
		}
		finally {
				try {
					soc.close();
				} catch (IOException e) {
				}		
		}
	}
	

}
