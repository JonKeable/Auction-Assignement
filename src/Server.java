import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Server {
	
	private Set<User> regUsers;
	private Set<User> conUsers;
	private Set<User> dcUsers;
	private Set<Item> auctions;
	public ServerGui gui;
	private int lastUid, lastIid;
	public Comms c;
	
	public Server() {
		SavedData sd = DataPersistance.loadFile();
		if (sd == null) {
			regUsers = new HashSet<User>();
			conUsers = new HashSet<User>();
			dcUsers = new HashSet<User>();
			auctions = new HashSet<Item>();
			lastUid = 0;
		}
		else{
			regUsers = sd.regUsers;
			conUsers = new HashSet<User>();
			dcUsers = new HashSet<User>();
			for(User u : regUsers) {
				dcUsers.add(u);
			}
			auctions = sd.auctions;
			lastUid = sd.lastUID;
			lastIid = sd.lastIID;
		}
		gui = new ServerGui(this);
		c = new Comms();
		c.startServer(this);
	}
	
	public void saveData() {
		SavedData sd = new SavedData(regUsers, auctions, lastUid, lastIid);
		DataPersistance.saveToFile(sd);
	}
	
	
	//Acts on type and content of message
	public void processMsg(Message msg) {
		gui.write("Message Recieved");
		if (msg instanceof LoginMessage) {
			for(User u : regUsers) {
				if (u.getID() == ((LoginMessage) msg).getUser()) {
					if(u.getPassword().equals(msg.getMsg())) {
						gui.write("User " + u.getGivenName() + " " + u.getFamName() +" logged in");
						conUsers.add(u);
						dcUsers.remove(u);
						c.sendMessage(true, u.getID());
						c.sendMessage(auctions, u.getID());
						return;
					}
					else {
						c.sendMessage(false, u.getID());
						c.removeUser(u.getID());
						return;
					}
				}
			}
			c.sendMessage(false, ((LoginMessage) msg).getUser());
			c.removeUser(((LoginMessage) msg).getUser());
			return;
		}
		else if(msg instanceof RegMessage) {
			String n = ((RegMessage) msg).getName();
			String fn = null, ln = null;
			String[] starr = n.split(":");
			fn = starr[0];
			ln = starr[1];
			int id = addUser(fn,ln,msg.getMsg());
			c.sendMessage(true, n ,id);
		}
		else if(msg instanceof BidMessage) {
			int item = ((BidMessage) msg).getI();
			for (Item i : auctions) {
				if(i.getId() == item) {
					i.bid(new Bid(((BidMessage) msg).getU(), ((BidMessage) msg).getBid()));
					c.sendMessage(auctions);
				}
			}
		}
		else if (msg instanceof SellItemMessage) {
			Item it = ((SellItemMessage) msg).getItem();
			lastIid++;
			it.setId(lastIid);
			auctions.add(it);
			c.sendMessage(auctions);
			gui.write("posted " + it.getTitle());
		}
		
		else {
			gui.write("invalid message recieved");
		}
	}

	private int addUser(String gn, String fn, String msg) {
		lastUid++;
		User u = new User(gn, fn, msg, lastUid);
		regUsers.add(u);
		conUsers.add(u);
		String out = "User " + gn + " " +  fn + " registered";
		gui.write(out);
		return lastUid;
	}

	public void dcUser(User u) {
		conUsers.remove(u);
		dcUsers.add(u);
		c.removeUser(u.getID());
		
	}

	public void genUList() {
		DataPersistance.genUserList(regUsers);
	}
}
