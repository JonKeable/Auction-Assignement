import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SellGui extends JFrame {

	private JPanel contentPane;
	private JTextField titleField, catField, desField, resField;
	private JSpinner stSpinner, etSpinner;
	private Comms c;
	private int u;

	/**
	 * Create the frame.
	 */
	public SellGui(Comms c, int u) {
		
		this.c = c;
		this.u = u;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 182, 317);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(new JLabel("Start Time"));
		stSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor stEditor = new JSpinner.DateEditor(stSpinner, "HH:mm:ss");
		stSpinner.setEditor(stEditor);
		stSpinner.setValue(new Date());
		contentPane.add(stSpinner);
		
		contentPane.add(new JLabel("End Time"));
		etSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor etEditor = new JSpinner.DateEditor(stSpinner, "HH:mm:ss");
		etSpinner.setEditor(etEditor);
		etSpinner.setValue(new Date());
		contentPane.add(etSpinner);
		
		contentPane.add(new JLabel("Item Title"));
		contentPane.add(titleField = new JTextField(10));
		
		contentPane.add(new JLabel("Item Category"));
		contentPane.add(catField = new JTextField(10));
		
		contentPane.add(new JLabel("Item Description"));
		contentPane.add(desField = new JTextField(10));
		
		contentPane.add(new JLabel("Reserve Price"));
		contentPane.add(resField = new JTextField(10));
		
		JButton postButton = new JButton("Post Item");
		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean validInputs = true;
				int reserve = 0;
				try {
					reserve = Integer.parseInt(resField.getText());
				}
				catch (NumberFormatException nfe) {
					validInputs = false;
					JOptionPane.showMessageDialog(null, "reserve price should be an int");
				}
				
				if(validInputs){
					c.sendMessage(new Item(titleField.getText(), desField.getText(), catField.getText(),
					u, (Date) stSpinner.getValue(), (Date) etSpinner.getValue(), reserve, 0));
					closeFrame();
				}
				
			}
		});
		contentPane.add(postButton);
		
		this.setVisible(true);
	}
	
	private void closeFrame() {
		this.setVisible(false);
		this.dispose();
	}

}
