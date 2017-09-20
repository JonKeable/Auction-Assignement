import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;

public class LoginGui extends JFrame {

	private JPanel contentPane;
	private JTextField hostField, portField, userField, passField, regPassField, firstNameField, lastNameField;
	private Comms comms;

	/**
	 * Create the frame.
	 */
	public LoginGui(Comms c) {

		comms = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 390, 300);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		
		JPanel loginPane = new JPanel();
		BoxLayout bl = new BoxLayout(loginPane, BoxLayout.Y_AXIS);
		loginPane.setLayout(bl);
		
		JPanel topPanel = new JPanel(new FlowLayout());
		
		JPanel hostnamePanel = new JPanel();
		hostnamePanel.add(new JLabel("hostname"));
		hostnamePanel.add(hostField = new JTextField(10));
		hostField.setText("localhost");
		topPanel.add(hostnamePanel);
		
		JPanel portPanel = new JPanel();
		portPanel.add(new JLabel("port"));
		portPanel.add(portField = new JTextField(10));
		portField.setText("5077");
		topPanel.add(portPanel);
		
		JPanel useridPanel = new JPanel();
		useridPanel.add(new JLabel("user ID"));
		useridPanel.add(userField = new JTextField(10));
		loginPane.add(useridPanel);
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.add(new JLabel("password"));
		passwordPanel.add(passField = new JTextField(10));
		loginPane.add(passwordPanel);

		
		JButton loginButton = new JButton("LOGIN");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean validInputs = true;
				
				int port = 0;
				int user = 0;
				
				try {
					port = Integer.parseInt(portField.getText());
					user = Integer.parseInt(userField.getText());
				}
				catch (NumberFormatException nfe) {
					validInputs = false;
					JOptionPane.showMessageDialog(null, "Port and userID must be integers");
				}
				
				if(validInputs) {
					if (comms.sendMessage(hostField.getText(), port, user, passField.getText())) {
						JOptionPane.showMessageDialog(null, "Login Successful");
						close();
					}
					else {
						JOptionPane.showMessageDialog(null, "Login Unsuccessful");
					}
				}

				
			}
			
		});
		JPanel logButtonPanel = new JPanel();
		logButtonPanel.add(loginButton);
		loginPane.add(logButtonPanel);
		
		JPanel regPane = new JPanel();
		regPane.setLayout(new BoxLayout(regPane, BoxLayout.Y_AXIS));
		
		JPanel firstNamePanel = new JPanel();
		firstNamePanel.add(new JLabel("first name"));
		firstNamePanel.add(firstNameField = new JTextField(10));
		regPane.add(firstNamePanel);
		
		JPanel lastNamePanel = new JPanel();
		lastNamePanel.add(new JLabel("last name"));
		lastNamePanel.add(lastNameField = new JTextField(10));
		regPane.add(lastNamePanel);
		
		JPanel regPasswordPanel = new JPanel();
		regPasswordPanel.add(new JLabel("password"));
		regPasswordPanel.add(regPassField = new JTextField(10));
		regPane.add(regPasswordPanel);
		
		JButton regButton = new JButton("REGISTER");
		regButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean validInputs = true;
				
				int port = 0;
				
				try {
					port = Integer.parseInt(portField.getText());
				}
				catch (NumberFormatException nfe) {
					validInputs = false;
					JOptionPane.showMessageDialog(null, "Port must be an integer");
				}
				
				String fn = firstNameField.getText();
				String ln = lastNameField.getText();
				if (fn.contains(":") || ln.contains(":")) {
					validInputs = false;
					JOptionPane.showMessageDialog(null, "Names cannot contain colons (':')");
				}
				if (fn.length() == 0 || ln.length() == 0) {
					validInputs = false;
					JOptionPane.showMessageDialog(null, "Names must not be blank");
				}
	
				String pass =regPassField.getText();
				if (pass.length() < 4) {
					validInputs = false;
					JOptionPane.showMessageDialog(null, "password must be at least 4 chars");
				}
				
				if(validInputs) {
					if (comms.sendMessage(hostField.getText(), port, fn, ln, pass)) {
						JOptionPane.showMessageDialog(null, "Registration Successful");
						close();
					}
					else {
						JOptionPane.showMessageDialog(null, "Registration Unsuccessful");
					}
				}
			}
			
		});
		JPanel regButtonPanel = new JPanel();
		regButtonPanel.add(regButton);
		regPane.add(regButtonPanel);
		
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("login", loginPane);
		tabbedPane.addTab("register", regPane);
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		this.setContentPane(contentPane);
		this.setVisible(true);
	}
	
	private void close() {
		this.setVisible(false);
		this.dispose();
	}

}
