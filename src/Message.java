import java.io.Serializable;

//Used to send messages across sockets, extended by instantiatable children
public abstract class Message implements Serializable{
	
	private String msg;
	
	public Message(String msg){
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	
}