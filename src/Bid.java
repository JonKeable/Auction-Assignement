public class Bid {
	private int bidderID;
	private int price;
	
	public Bid(int id, int price){
		this.bidderID = id;
		this.price = price;
	}
	
	public int getValue() {
		return price;
	}
	
	public int getID() {
		return bidderID;
	}
}