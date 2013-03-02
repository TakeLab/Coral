package hr.fer.zemris.ktlab.sap.gui.test;

import hr.fer.zemris.ktlab.sap.gui.DemoBoard;
import hr.fer.zemris.ktlab.sap.gui.Icon;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TestDemoBoard extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public TestDemoBoard() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		//this.setSize(500, 500);
		this.setTitle("Test DemoBoard");
		
		this.getContentPane().setLayout(new BorderLayout());
		
		Icon.setImage(new ImageIcon("icons/icon.gif").getImage());
		final DemoBoard db = new DemoBoard((short)5); 
		this.add(db, BorderLayout.CENTER);
		
		JPanel panel = new JPanel(new GridBagLayout());
		final JTextField input = new JTextField(5);
		JButton apply = new JButton("Set Treshold");
		
		apply.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				short threshold = Short.parseShort(input.getText());
				db.setSelectionTreshold(threshold);
				db.repaint();
			}
		
		});
		
		panel.add(input);
		panel.add(apply);
		
		this.add(panel, BorderLayout.SOUTH);
		
		this.pack();
		this.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		
			public void run() {
				new TestDemoBoard(); 
			}
		});

	}

}
