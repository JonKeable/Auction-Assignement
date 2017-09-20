import java.io.Serializable;

public class LoginMessage extends Message implements Serializable{

	private int  user;
	
	public LoginMessage(int user, String pass) {
		super(pass);
		this.user = user;
	}
	
	public int getUser() {
		return user;
	}
	
}