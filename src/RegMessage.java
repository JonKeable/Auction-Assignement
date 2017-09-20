import java.io.Serializable;

public class RegMessage extends Message implements Serializable{

	public String name;
	
	public RegMessage(String fn, String ln, String pass) {
		super(pass);
		this.name = fn + ":" + ln;
	}
	
	public String getName() {
		return name;
	}
	
}