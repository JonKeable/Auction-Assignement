import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Item implements Serializable{

	private String title, descr, catKey;
	private int vendID;
	private Date startTime, endTime;
	private int reservePrice;
	private int hBid;
	private ArrayList<Bid> bidsList;
	private int id;
	private boolean sold;
	
	public Item(String title, String descr, String cat, int u, 
			Date startTime, Date endTime, int reservePrice, int id) {
		this.title = title;
		this.descr = descr;
		this.catKey = cat;
		this.vendID = u;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reservePrice = reservePrice;
		this.id = id;
		sold = false;
		
		hBid = reservePrice;
	}
	
	public boolean bid(Bid b) {
		if (b.getValue() > hBid) {
			bidsList.add(b);
			hBid = b.getValue();
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getHBid() {
		return hBid;
	}
	
	public String getTitle() {
		return title;
	}
	public String getDescr() {
		return descr;
	}
	public String getCatKey() {
		return catKey;
	}
	public int getVendID() {
		return vendID;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public long getReservePrice() {
		return reservePrice;
	}
	public int getId() {
		return id;
	}
	
	
}

