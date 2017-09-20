import java.io.Serializable;

public class BidMessage extends Message implements Serializable{

	int u, i, bid;
	
	public int getU() {
		return u;
	}

	public int getI() {
		return i;
	}

	public int getBid() {
		return bid;
	}

	public BidMessage(int u, int i, int bid) {
		super("BID");
		this.u = u;
		this.i = i;
		this.bid =  bid;
	}
	
}