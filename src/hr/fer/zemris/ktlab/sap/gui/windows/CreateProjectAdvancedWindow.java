package hr.fer.zemris.ktlab.sap.gui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Klasa CreateProjectWindowAdvanced predstavlja prozor s naprednim opcijama kod
 * kreiranja novog projekta. Te napredne opcije se izravno odnose na učitavanje
 * ulaznih XML datoteka. Napredne opcije se podešavaju za svaku datoteku
 * zasebno, a moguće je podesiti: tagove koji se ignoriraju (XML tagovi koji će
 * biti ignorirani pri učitavanju datoteke) i tagove koji predstavljaju granicu
 * među ulomcima (ukoliko postoji podjela na ulomke u izvornoj datoteci).
 */
public class CreateProjectAdvancedWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JLabel jLabel2;
	private JTextField jTextField3;
	private JCheckBox jCheckBox3;
	private JPanel jPanel2;
	private JLabel jLabel4;
	private JButton jButton4;
	private JButton jButton3;
	private JLabel jLabel1;
	private JTextField jTextField2;
	private JTextField jTextField1;
	private CreateProjectWindow parentWindow;

	private JTextField jTextField4;
	private JLabel jLabel3;

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param parentWindow
	 *            Glavni prozor programa.
	 */
	public CreateProjectAdvancedWindow(CreateProjectWindow parentWindow) {
		super(parentWindow);
		this.parentWindow = parentWindow;
		initGUI();

	}

	/**
	 * Metoda koja osposobljava ili onesposobljava komponente ovog prozora.
	 * 
	 * @param state
	 *            Novo stanje komponenata.
	 */
	private void enableComponents(boolean state) {
		jTextField1.setEnabled(state);
		jTextField2.setEnabled(state);
		jTextField3.setEnabled(state);
		jTextField4.setEnabled(state);
	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		setTitle("Advanced settings");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(498, 405);
		setModal(true);
		setLocationRelativeTo(parentWindow);
		getContentPane().setLayout(null);
		this.setResizable(false);
		{
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1);
			jPanel1.setLayout(null);
			jPanel1.setBounds(7, 42, 476, 133);
			jPanel1.setBorder(BorderFactory.createTitledBorder("First file"));
			{
				jTextField1 = new JTextField();
				jPanel1.add(jTextField1);
				jTextField1.setBounds(14, 49, 448, 21);
			}
			{
				jLabel1 = new JLabel();
				jPanel1.add(jLabel1);
				jLabel1
						.setText("List of tags to ignore, seperated by space (e.g. \"s w seg\"):");
				jLabel1.setBounds(14, 21, 441, 28);
			}
			{
				jLabel4 = new JLabel();
				jPanel1.add(jLabel4);
				jLabel4
						.setText("List of tags that divide paragrafs (e.g. \"p\"):");
				jLabel4.setBounds(14, 70, 448, 28);
			}
			{
				jTextField3 = new JTextField();
				jPanel1.add(jTextField3);
				jTextField3.setBounds(14, 98, 448, 21);
			}
		}
		{
			jButton3 = new JButton();
			getContentPane().add(jButton3);
			jButton3.setText("OK");
			jButton3.setBounds(154, 336, 77, 28);
			jButton3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton3ActionPerformed(evt);
				}
			});
		}
		{
			jButton4 = new JButton();
			getContentPane().add(jButton4);
			jButton4.setText("Cancel");
			jButton4.setBounds(238, 336, 84, 28);
			jButton4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton4ActionPerformed(evt);
				}
			});
		}
		{
			jPanel2 = new JPanel();
			getContentPane().add(jPanel2);
			jPanel2.setLayout(null);
			jPanel2.setBounds(7, 189, 476, 133);
			jPanel2.setBorder(BorderFactory.createTitledBorder("Second file"));
			{
				jTextField2 = new JTextField();
				jPanel2.add(jTextField2);
				jTextField2.setBounds(14, 49, 448, 21);
			}
			{
				jLabel2 = new JLabel();
				jPanel2.add(jLabel2);
				jLabel2
						.setText("List of tags to ignore, seperated by space (e.g. \"s w seg\"):");
				jLabel2.setBounds(14, 21, 441, 28);
			}
			{
				jLabel3 = new JLabel();
				jPanel2.add(jLabel3);
				jLabel3
						.setText("List of tags that divide paragrafs (e.g. \"p\"):");
				jLabel3.setBounds(14, 70, 448, 28);
			}
			{
				jTextField4 = new JTextField();
				jPanel2.add(jTextField4);
				jTextField4.setBounds(14, 98, 448, 21);
			}
		}
		{
			jCheckBox3 = new JCheckBox();
			getContentPane().add(jCheckBox3);
			jCheckBox3
					.setText("Enable advanced options (only for XML source files)");
			jCheckBox3.setBounds(14, 7, 469, 28);
			jCheckBox3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jCheckBox3ActionPerformed(evt);
				}
			});

		}
		enableComponents(false);
		setVisible(true);
	}

	/**
	 * Metoda koja od niza tagova zapisanih u jednom stringu puni listu.
	 * 
	 * @param text
	 *            Tekst kojime se puni lista.
	 * @return
	 */
	private List<String> loadList(String text) {
		List<String> list = new ArrayList<String>();
		String[] seperated = text.split(" ");
		for (int i = 0; i < seperated.length; i++) {
			list.add(seperated[i]);
		}
		return list;
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na gumb Cancel.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton4ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na gumb OK.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton3ActionPerformed(ActionEvent evt) {
		parentWindow.ignoreList = loadList(jTextField1.getText());
		parentWindow.paragraphList = loadList(jTextField3.getText());
		parentWindow.ignoreList2 = loadList(jTextField2.getText());
		parentWindow.paragraphList2 = loadList(jTextField4.getText());

		this.dispose();
	}

	/**
	 * Metoda koja se izvršava nakon označavanja check boxa.
	 * 
	 * @param evt
	 *            Event označavanja.
	 */
	private void jCheckBox3ActionPerformed(ActionEvent evt) {
		if (jCheckBox3.isSelected()) {
			enableComponents(true);
		} else {
			enableComponents(false);
		}
	}
}
