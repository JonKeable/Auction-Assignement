import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BidGui extends JFrame {

	private JPanel contentPane;
	private JTextField lastBidField, bidField;
	private int u, i, lb;
	private Comms c;
	
	/**
	 * Create the frame.
	 */
	public BidGui(int u, int i, int lb, Comms c) {
		
		this.u = u;
		this.i = i;
		this.lb = lb;
		this.c = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Last Bid"));
		topPanel.add(lastBidField = new JTextField(10));
		lastBidField.setHorizontalAlignment(SwingConstants.CENTER);
		lastBidField.setText(Integer.toString(lb));
		lastBidField.setEditable(false);
		topPanel.add(new JLabel("Your Bid"));
		topPanel.add(bidField = new JTextField(10));
		bidField.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		getContentPane().add(topPanel);
		
		JButton bidButton = new JButton("BID");
		bidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newBid = 0;
				try {
					newBid = Integer.parseInt(bidField.getText());
				}
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Bid not a valid int");
				}
				
				if (newBid > lb) {
					c.sendMessage(newBid, u, i);
					closeFrame();
				}
				else {
					JOptionPane.showMessageDialog(null, "Bid must be greater than last bid");
				}

			}
		});
		getContentPane().add(bidButton);
		
		this.setVisible(true);
	}
	
	public void closeFrame() {
		this.setVisible(false);
		this.dispose();
	}

}
