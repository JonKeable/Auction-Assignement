import java.io.Serializable;

public class User implements Serializable{
	
	private String givenName, familyName;
	private String password;
	private int id;
	
	public User(String gName, String fName, String pWord, int id) {
		this.givenName = gName;
		this.familyName = fName;
		this.password = pWord;
		this.id = id;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public String getFamName() {
		return familyName;
	}
	
	public int getID() {
		return id;
	}
	
	public boolean setPassword(String oldPword, String newPword) {
		if (password.equals(oldPword)) {
			password = newPword;
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getPassword() {
		return password;
	}
}
