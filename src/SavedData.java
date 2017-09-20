import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SavedData implements Serializable{
	public HashSet<User> regUsers;
	public HashSet<Item> auctions;
	public int lastUID;
	public int lastIID;
	
	public SavedData(Set<User> users, Set<Item> auctions, int uid, int iid) {
		this.regUsers = (HashSet<User>) users;
		this.auctions = (HashSet<Item>) auctions;
		this.lastUID =  uid;
		this.lastIID = iid;
	}
}
