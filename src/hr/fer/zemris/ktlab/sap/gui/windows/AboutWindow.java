package hr.fer.zemris.ktlab.sap.gui.windows;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 * Klasa <code>AboutWindow</code> predstavlja prozor u kojemu se nalaze
 * osnovne informacije o programu i autorima. Tekst koji se ispisuje u ovom
 * prozoru nalazi se u .html datotekama u help direktoriju, a prikazuje se
 * pomoću <code>JEditorPane</code>-a.
 */
public class AboutWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Scroll pane Authors taba. */
	private JScrollPane jScrollPane1;

	/** Scroll pane Coral taba. */
	private JScrollPane jScrollPane2;

	/** Naslovni Editor pane. */
	private JEditorPane jEditorPane3;

	/** Editor pane Coral taba. */
	private JEditorPane jEditorPane2;

	/** Editor pane Authors taba. */
	private JEditorPane jEditorPane1;

	/** Panel s tabovima u kojemu se nalaze Editor paneli za prikaz sadržaja. */
	private JTabbedPane jTabbedPane1;

	/** Glavni prozor programa. */
	private MainWindow parentWindow;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param parent
	 *            Glavni prozor programa.
	 * @param modal
	 *            <code>true</code> ako se nakon otvaranja ovog prozora zeli
	 *            onesposobiti glavni prozor, inace <code>false</code>.
	 */
	public AboutWindow(MainWindow parent, boolean modal) {
		super(parent);
		this.parentWindow = parent;
		initGUI();
		setAuthors2();
		setCoral();
		setTitle();
		setVisible(true);
	}

	/**
	 * Metoda u kojoj se inicijalizira grafičko sučelje. Sučelje se sastoji od
	 * <code>JTabbedPane</code>-a unutar kojega se nalazi
	 * <code>JEditorPane</code> za prikaz .html datoteka.
	 */
	private void initGUI() {
		this.setSize(449, 474);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parentWindow);
		setModal(true);
		AnchorLayout thisLayout = new AnchorLayout();
		getContentPane().setLayout(thisLayout);
		this.setTitle("About");
		{
			jEditorPane3 = new JEditorPane();
			getContentPane().add(
					jEditorPane3,
					new AnchorConstraint(12, 14, 253, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS,
							AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
			jEditorPane3.setPreferredSize(new java.awt.Dimension(415, 101));
			jEditorPane3.setEditable(false);
			jEditorPane3.setOpaque(false);
		}

		{
			jTabbedPane1 = new JTabbedPane();
			getContentPane().add(
					jTabbedPane1,
					new AnchorConstraint(112, 12, 14, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS,
							AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
			jTabbedPane1.setPreferredSize(new java.awt.Dimension(364, 270));
			{
				jScrollPane2 = new JScrollPane();
				jTabbedPane1.addTab("Coral", null, jScrollPane2, null);
				jScrollPane2.setBounds(14, 78, 359, 188);
				jScrollPane2.setPreferredSize(new java.awt.Dimension(359, 237));
				{
					jEditorPane2 = new JEditorPane();
					jScrollPane2.setViewportView(jEditorPane2);
					// jEditorPane2.setText("O programu...");
					jEditorPane2.setEditable(false);
				}
			}
			{
				jScrollPane1 = new JScrollPane();
				jTabbedPane1.addTab("Authors", null, jScrollPane1, null);
				{
					jEditorPane1 = new JEditorPane();
					jScrollPane1.setViewportView(jEditorPane1);
					jEditorPane1.setEditable(false);
				}
			}
		}

	}

	/**
	 * Metoda pomoću koje se u prvi <code>JEditorPane</code> učitava sadržaj
	 * authors.html datoteke iz help direktorija.
	 */
	private void setAuthors2() {
		try {
			String sPath = System.getProperty("user.dir") + "/";
			URL url;
			url = new URL("file:///" + sPath + "help/authors.html");

			jEditorPane1.setPage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Metoda pomoću koje se u drugi <code>JEditorPane</code> učitava sadržaj
	 * coral.html datoteke iz help direktorija.
	 */
	private void setCoral() {
		try {
			String sPath = System.getProperty("user.dir") + "/";
			URL url;
			url = new URL("file:///" + sPath + "help/coral.html");

			jEditorPane2.setPage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Metoda pomoću koje se u naslovni <code>JEditorPane</code> učitava
	 * sadržaj title.html datoteke iz help direktorija.
	 */
	private void setTitle() {
		try {
			String sPath = System.getProperty("user.dir") + "/";
			URL url;
			url = new URL("file:///" + sPath + "help/title.html");

			jEditorPane3.setPage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
