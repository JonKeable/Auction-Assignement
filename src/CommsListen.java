import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class CommsListen implements Runnable {
	
	private Socket soc;
	private User u;
	private Server s;
	
	public CommsListen(Socket sc, User u, Server sv) {
		soc = sc;
		this.s = sv;
		this.u =u;
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
	
	public void run() {
		Comms.Message msg;
		BufferedInputStream bis;
		ObjectInputStream ois;
		try {
			msg = null;
			bis = new BufferedInputStream(soc.getInputStream());
			ois = new ObjectInputStream(bis);
			while(true) {
				if(isSocketConnected()) {
					msg = (Comms.Message) ois.readObject();
					s.processMsg(msg);
				}
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "error occured listening for messages");
		}
		finally {
				try {
					soc.close();
				} catch (IOException e) {
				}		
		}
	}
	

}
