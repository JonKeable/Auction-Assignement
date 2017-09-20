import java.awt.List;
import java.io.Serializable;
import java.util.Set;

import javax.swing.JList;

public class AuctionsMessage extends Message implements Serializable{

	private Set<Item> items;
	
	public AuctionsMessage(Set<Item> items) {
		super("ITEMS");
		this.items = items;
	}
	
	public List getItemList() {
		return (List) items;
	}

}
