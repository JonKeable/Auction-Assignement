import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.List;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ClientGui extends JFrame {

	private JPanel contentPane;
	private Comms comms;
	private int user;
	private JTextArea descArea;
	private JList<Item> itemList;
	private JTextField sellerField, priceField, catField;
	private JScrollPane scrollPane_1;

	/**
	 * Create the frame.
	 */
	public ClientGui(Comms c, int u) {
		comms = c;
		this.user = u;
		itemList = new JList<Item>();
		ArrayList<Item> egItems = new ArrayList<Item>();
		egItems.add(new Item("a","b","c",4,new Date(),new Date(),0, u));

		
		
		itemList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Item it = itemList.getSelectedValue();
				sellerField.setText(Integer.toString(it.getVendID()));
				priceField.setText(Integer.toString(it.getHBid()));
				catField.setText(it.getCatKey());
				descArea.setText(it.getDescr());
				
			}
		});
		
		this.setTitle("Auction Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		descArea = new JTextArea(" Some text. \n Some more text.");
		descArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel lPanel = new JPanel();
		lPanel.setLayout(new BoxLayout(lPanel, BoxLayout.Y_AXIS));
		
		JPanel filterPanel = new JPanel();
		lPanel.add(filterPanel);
		
		scrollPane_1 = new JScrollPane(itemList);
		lPanel.add(scrollPane_1);
		
		JPanel infoPanel = new JPanel(); 
		infoPanel.add(new JLabel("Seller ID"));
		infoPanel.add(sellerField = new JTextField(10));
		sellerField.setEditable(false);
		infoPanel.add(new JLabel("Price"));
		infoPanel.add(priceField = new JTextField(10));
		priceField.setEditable(false);
		infoPanel.add(new JLabel("Category"));
		infoPanel.add(catField = new JTextField(10));
		catField.setEditable(false);
		
		lPanel.add(infoPanel);
		
		
		splitPane.setLeftComponent(lPanel);

		
		JPanel rPanel = new JPanel();
		rPanel.setLayout(new BoxLayout(rPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(descArea);
		rPanel.add(scrollPane);
		
		
		JLabel lblItemDescription = new JLabel("Item Description");
		lblItemDescription.setFont(new Font("Tahoma", Font.BOLD, 14));
		scrollPane.setColumnHeaderView(lblItemDescription);
		
		JPanel rBotPanel = new JPanel();
		
		JButton bidButton = new JButton("BID");
		bidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item i = itemList.getSelectedValue();
				new BidGui(user, i.getId(), i.getHBid(), c);
			}
		});
		rBotPanel.add(bidButton);
		JButton auctionButton = new JButton("SELL");
		auctionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SellGui(c, u);
			}
		});
		rBotPanel.add(auctionButton);	
		rPanel.add(rBotPanel);
		splitPane.setRightComponent(rPanel);
		this.setVisible(true);
	}

	public void processMsg(Message msg) {
		if (msg instanceof AuctionsMessage) {
			List l = (List) ((AuctionsMessage) msg).getItemList();
			itemList.setModel((ListModel<Item>) l);
			JOptionPane.showMessageDialog(null, "items updated");
			return;
		}
	}

}
