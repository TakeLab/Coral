package hr.fer.zemris.ktlab.sap.gui.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

/**
 * Klasa predstavlja prozor koji se pojavljuje nakon što se u programu desi
 * greška. Sadrži kratke informacije o uzorku greške.
 * 
 * @author Igor Šoš
 * 
 */
public class ErrorWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	
	/** Glavni prozor programa. */
	MainWindow mainWindow;
	
	/** Gumb OK */
	private JButton jButton1;
	
	/** Tekstalno područje s detaljima o grešci. */
	private JTextPane jTextPane2;
	
	/** Drugi panel. */
	private JPanel jPanel2;
	
	/** Tekstualni panel s nazivom greške. */
	private JTextPane jTextPane1;
	private JScrollPane jScrollPane1;
	private JPanel jPanel1;
	
	/** Podaci od grešci. */
	private Exception exception;

	/** Javni konstruktor klase.
	 * 
	 * @param mainWindow Glavni prozor programa.
	 * @param exception Podaci o grešci.
	 */
	public ErrorWindow(MainWindow mainWindow, Exception exception) {
		super(mainWindow, true);
		this.mainWindow = mainWindow;
		this.exception = exception;
		initGUI();
	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		setTitle("Error!");
		this.setSize(378, 251);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(mainWindow);

		{
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			jPanel1.setLayout(null);
			{
				jScrollPane1 = new JScrollPane();
				jPanel1.add(jScrollPane1);
				jScrollPane1.setBounds(7, 7, 357, 140);
				{
					jTextPane1 = new JTextPane();
					jScrollPane1.setViewportView(jTextPane1);
					jTextPane1.setText(exception.getMessage());
					jTextPane1
							.setPreferredSize(new java.awt.Dimension(342, 113));
				}
			}
			{
				jButton1 = new JButton();
				jPanel1.add(jButton1);
				jButton1.setText("Close");
				jButton1.setBounds(149, 155, 70, 21);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
		}
		{
			jPanel2 = new JPanel();
			getContentPane().add(jPanel2, BorderLayout.NORTH);
			jPanel2.setPreferredSize(new java.awt.Dimension(370, 38));
			jPanel2.setLayout(null);
			{
				jTextPane2 = new JTextPane();
				jPanel2.add(jTextPane2);
				jTextPane2.setText("Exception occured:");
				jTextPane2.setBounds(7, 5, 356, 31);
				jTextPane2.setEditable(false);
				jTextPane2.setOpaque(false);
			}
		}
		pack();
		setVisible(true);
		this.setResizable(false);
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na tipku OK.
	 * @param evt Event pritiska na tipku.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

}
