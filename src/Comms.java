import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

public class Comms implements Serializable{

	private HashMap<Integer, Socket> userSocs;
	private HashMap<String,Socket> nameSocs;
	private HashMap<Socket, ObjectOutputStream> socOuts;
	private Socket serverSoc;
	private static final int serverPort = 5077;
	//used by clients to listen to server
	private ObjectInputStream oisClient;
	private ObjectOutputStream oos;
	
	public Comms() {
		userSocs = new HashMap<Integer, Socket>();
		nameSocs = new HashMap<String, Socket>();
		socOuts = new HashMap<Socket, ObjectOutputStream>();
	}
	
	public void sendMessage(int Price, int u, int i) {
		try {
			oos.reset();
			oos.writeObject(new BidMessage(u, i, Price));
			oos.flush();
			//oos.close();
		} catch (IOException e) {
		
		}
	}
	
	public void sendMessage(Item item) {
		try {
			oos.reset();
			oos.writeObject(new SellItemMessage(item));
			oos.flush();
			//oos.close();
		} catch (IOException e) {
		
		}
	}
	
	public void sendMessage(Set<Item> items) {
		for(Socket s : userSocs.values()) {
			/*
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
			} 
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Could not establish connection to specified socket");
				return;
			}
			*/
			try {
				ObjectOutputStream oos = socOuts.get(s);
				oos.reset();
				oos.writeObject(new AuctionsMessage(items));
				oos.flush();
			} catch (IOException e) {
			
			}
		}

	}
	
	public void sendMessage(Set<Item> items, int u) {
			Socket s = userSocs.get(u);
			
			OutputStream fos;
			ObjectOutputStream oos;
			/*
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
			} 
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Could not establish connection to specified socket");
				return;
			}
			*/
			try {
				oos = socOuts.get(s);
				oos.reset();
				oos.writeObject(new AuctionsMessage(items));
				oos.flush();
			} catch (IOException e) {
			
			}
	}
	
	//for sending confirmation/rejection messages on login
	public void sendMessage(boolean bool, int u) {
		Socket soc = userSocs.get(u);
		OutputStream fos;
		ObjectOutputStream oos;
		try {
			//These operations and similar open an object stream, which will be used hereafter to send message objects through sockets
			fos = new BufferedOutputStream(soc.getOutputStream());
			oos = new ObjectOutputStream(fos);
			socOuts.put(soc, oos);
		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not establish connection to specified server");
			return;
		}
		try {
			oos.writeObject(new ConfMessage(Integer.toString(u),bool));
			oos.flush();
			//oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error trying to output message");
		}
	}
	//for sending confirmation/rejection messages on registration
	public void sendMessage(boolean bool, String name, int id) {
		Socket soc = nameSocs.get(name);
		OutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new BufferedOutputStream(soc.getOutputStream());
			oos = new ObjectOutputStream(fos);
			socOuts.put(soc, oos);
		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not establish connection");
			return;
		}
		try {
			oos.writeObject(new ConfMessage(Integer.toString(id),bool));
			oos.flush();
			//oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error trying to output message");
		}
	}
	
	
	//For logging in
	public boolean sendMessage(String host, int port, int user, String pass) {
		
		OutputStream fos;
		try {
			serverSoc = new Socket(host, port);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not establish connection to specified server");
			return false;
		}
		try {
			fos = new BufferedOutputStream(serverSoc.getOutputStream());
			oos = new ObjectOutputStream(fos);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not open output stream");
			return false;
		}
		
		if(userSocs.keySet().contains(user)) {
			JOptionPane.showMessageDialog(null, "User already connected");
			return false;
		}
		
		Message lm = new LoginMessage(user, pass);
		try {
			oos.writeObject(lm);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error trying to output message to server");
			return false;
		}
		
		return loginRecieve();

	}

	//For registration
	public boolean sendMessage(String host, int port, String firstname, String lastname, String pass) {
		
		OutputStream fos;
		try {
			serverSoc = new Socket(host, port);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not establish connection to specified server");
			return false;
		}
		try {
			fos = new BufferedOutputStream(serverSoc.getOutputStream());
			oos = new ObjectOutputStream(fos);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not open output stream");
			return false;
		}


		RegMessage rm = new RegMessage(firstname, lastname, pass);
		try {
			oos.writeObject(rm);
			oos.flush();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error trying to output message to server");
			e.printStackTrace();
			return false;
		}

		
		return regRecieve();
	}
	
	//Unused
	private void receive() {
		
		try {
			Message msg = (Message) oisClient.readObject();
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error listening to server");
		}
		
	}
	
	//checks to see of login is valid
	public boolean loginRecieve() {
		Message msg = null;
		try {
			oisClient = new ObjectInputStream(serverSoc.getInputStream());
		} 
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error trying to set up input stream");
			ioe.printStackTrace();
			return false;
		}
		while(true) {
			try {
				msg = (Message) oisClient.readObject();
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, "Error listening to server");
			}
			
			if (msg instanceof ConfMessage) {
				if (((ConfMessage) msg).isConf()) {
					ClientGui gui = new ClientGui(this, Integer.parseInt(msg.getMsg()));
					CommsCListen cl = new CommsCListen(serverSoc, gui);
					return true;
				}
				else return false;
			}
		}

	}
	
	//Used by clients to check msg sent back by server confirms or denes registration
	public boolean regRecieve() {
		Message msg = null;
		try {
			oisClient = new ObjectInputStream(serverSoc.getInputStream());
		} 
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error trying to set up input stream");
			ioe.printStackTrace();
			return false;
		}
		while(true) {
			try {
				msg = (Message) oisClient.readObject();
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, "Error listening to server");
			}
			
			if (msg instanceof ConfMessage) {
				if (((ConfMessage) msg).isConf()) {
					ClientGui gui = new ClientGui(this, Integer.parseInt(msg.getMsg()));
					JOptionPane.showMessageDialog(null, "Your UID for logging in is: " + msg.getMsg());
					CommsCListen cl = new CommsCListen(serverSoc, gui);
					return true;
				}
				else return false;
			}
		}

	}

	
	public Message serverRecieve() {
		Message msg = null;
			try {
				msg = (Message) oisClient.readObject();
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, "Error listening to server");
			}
		return msg;
	}
	
	//Opens up a server socket, and accepts connections to it, starting threads to then listen to these connections
	public void startServer(Server s) {
		
		ServerSocket serverSoc = null;
		try {
			serverSoc = new ServerSocket(serverPort);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not start server");
		}
		
		while(true) {
            Socket soc;
			try {
				soc = serverSoc.accept();
	            CommsSListen cl = new CommsSListen(soc, s, this);
	            Thread t = new Thread(cl);
	            t.start();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error accepting connetion");
			}

		}
	}


	public void removeUser(int u) {
		userSocs.remove(u);
	}

	public void addSoc(String name, Socket soc) {
		nameSocs.put(name, soc);
	}
	
	public void addSoc(int user, Socket soc) {
		userSocs.put(user, soc);
	}




}
