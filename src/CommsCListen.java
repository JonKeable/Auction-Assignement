import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class CommsCListen {

	private Socket soc;
	private User u;
	private ClientGui gui;
	
	public CommsCListen(Socket sc, ClientGui gui) {
		soc = sc;
		this.gui = gui;
	}
	
	//Waits for input, then passes the message to the client gui.
	public void run() {
		Message msg;
		BufferedInputStream bis;
		ObjectInputStream ois;
		msg = null;
		try {
			ois = new ObjectInputStream(soc.getInputStream());
			while(true) {
				msg = (Message) ois.readObject();
				JOptionPane.showMessageDialog(null, "msg recieved");
				gui.processMsg(msg);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "error occured listening for messages (CLIENT)");
		}
		finally {
				try {
					soc.close();
				} catch (IOException e) {
				}		
		}
	}
}
