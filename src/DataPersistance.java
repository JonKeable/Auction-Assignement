import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class DataPersistance {

	private static final String filename = "save.txt";
	private static final String ulFilename = "uList.txt";
	
	//loads data from default file
	public static SavedData loadFile(){
		File f = new File(filename);
		FileInputStream fis;
		ObjectInputStream oos;
		Object o = null;
		//tries to read the savedData object
		//returns the object id it exists and is the correct type
		try {
			fis = new FileInputStream(f);
			oos = new ObjectInputStream(fis);
			o = oos.readObject();
		} catch (IOException | ClassNotFoundException e) {
		}

		if (o instanceof SavedData) {
			return (SavedData) o;
		}
		else return null;

	}
	
	//saves data to defualt file
	public static void saveToFile(SavedData sd){
		File f = new File(filename);
		//tries to write a serialized saveData object to the file
		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sd);
			oos.flush();
			oos.close();
		} 
		catch (IOException e) {
		}
	}
	
	//Generates a list of users registered
	public static void genUserList(Set<User> users) {
		File f = new File(ulFilename);
		PrintWriter pr = null;
		HashSet<User> uStore = (HashSet<User>) users;
		try {
			f.createNewFile();
			pr = new PrintWriter(f);
			for(User u : uStore) {
				pr.println(u.getGivenName() + " " + u.getFamName() + "  ID: " + u.getID() );
				pr.flush();
			}
		} 
		catch (IOException e) {
		}
		finally {
			pr.close();
		}
	}
}
