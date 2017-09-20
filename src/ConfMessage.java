import java.io.Serializable;

public class ConfMessage extends Message implements Serializable{
	
	private boolean isConf;

	public ConfMessage(String msg, boolean isConf) {
		super(msg);
		this.isConf = isConf;
	}
	
	public boolean isConf() {
		return isConf;
	}
	
}