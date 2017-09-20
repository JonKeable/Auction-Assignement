import java.io.Serializable;

public class SellItemMessage extends Message implements Serializable{

	private Item i;
	
	public SellItemMessage(Item i) {
		super("SELL");
		this.i = i;
	}

	public Item getItem() {
		return i;
	}
}
