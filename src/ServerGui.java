import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerGui extends JFrame {

	private JPanel contentPane;
	private JTextArea console;
	private Server myServ;

	/**
	 * Create the frame.
	 */
	public ServerGui(Server s) {
		
		myServ = s;
		
		console = new JTextArea();
		PrintStream consoleStream = new PrintStream(new TextAreaOutputStream(console));
		System.setOut(consoleStream);
		System.setErr(consoleStream);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		JScrollPane scrollPane = new JScrollPane(console);
		System.out.println("SERVER STARTED @ " + new java.util.Date());
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel botPanel = new JPanel();
		
		JButton btnGenerateUserlist = new JButton("Generate Userlist");
		btnGenerateUserlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myServ.genUList();
			}
		});
		botPanel.add(btnGenerateUserlist);
		
		JButton saveButton = new JButton("save server");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myServ.saveData();
			}
		});
		botPanel.add(saveButton);
		
		contentPane.add(botPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public void write(String s) {
		System.out.println(s);
	}
	
	//used to redirect sysout to a text area
	public class TextAreaOutputStream extends OutputStream {
	    private JTextArea textArea;
	     
	    public TextAreaOutputStream(JTextArea jta) {
	        textArea = jta;
	    }
	     
	    @Override
	    public void write(int b) throws IOException {
	        // redirects the data to the text area, where b is the byte to be written
	        textArea.append(String.valueOf((char)b));
	        // Moves the writer caret to the end of the text area's data, so that following bytes will be written to the correct place
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
	}

}
