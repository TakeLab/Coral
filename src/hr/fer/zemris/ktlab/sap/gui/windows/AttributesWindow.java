package hr.fer.zemris.ktlab.sap.gui.windows;

import hr.fer.zemris.ktlab.sap.io.FileExporter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Klasa AttributesWindow predstavlja prozor koji se prikazuje nakon odabira
 * opcije za eksportiranje lijevog ili desnog jezika u XML datoteku te kod
 * eksportiranja translation unita. Na ovome prozoru se pomoću više instanci
 * komponente JCheckBox prikazuju svi atributi pročitani iz tagova izvornih XML
 * datoteka. Oni atributi koje korisnik označi biti će dodani i u eksportiranu
 * datoteku, uz odgovarajući jezični element (XML tag).
 */
public class AttributesWindow extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	MainWindow mainWindow;

	/** Glavna klizna traka prozora. */
	private JScrollPane jScrollPane1;

	/** Gumb "OK". */
	private JButton jButton1;

	/** Gumb "Cancel". */
	private JButton jButton2;

	/** Tekstualni panel s kratkim opisom ponuđenih opcija. */
	private JTextPane jTextPane1;

	/**
	 * Panel unutar kojega se generiraju <code>JCheckBox</code>-ovi za odabir
	 * atributa.
	 */
	private JPanel jPanel1;

	/** Strana koja se eksportira (lijeva ili desna). */
	private short side;

	/**
	 * Lista u koju se pohranjuju svi <code>JCheckBox</code>-ovi za odabir
	 * atributa.
	 */
	private List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

	/** Varijabla koja označava što se eksportira (XML ili TEI). */
	private short what;

	/** Konstanta koja označava da će se eksportirati XML datoteka. */
	public static final short XML = 0;

	/** Konstanta koja označava da će se eksportirati translation unit datoteka. */
	public static final short TEI = 1;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 * @param side
	 *            Strana koja se eksportira (lijeva ili desna).
	 * @param what
	 *            Označava što se eksportira (XML ili TEI).
	 */
	public AttributesWindow(MainWindow mainWindow, short side, short what) {
		super(mainWindow, true);
		this.mainWindow = mainWindow;
		this.side = side;
		this.what = what;
		initGUI();
		listArguments();
		this.setVisible(true);
	}

	/**
	 * Metoda koja dohvaća skup svih atributa iz podatkovnog modela i prema tome
	 * generira odgovarajući broj <code>JCheckBox</code>-ova te ih dodaje u
	 * panel za prikaz i sprema ih u listu.
	 */
	private void listArguments() {
		Set<String> atributes = mainWindow.dataModel.getXmlAttributeNames();
		for (String string : atributes) {
			JCheckBox checkBox = new JCheckBox();
			checkBox.setText(string);
			checkBoxes.add(checkBox);
			jPanel1.add(checkBox);
		}
	}

	/**
	 * Metoda pomoću koje se inicijalizira grafičko sučelje. 
	 */
	private void initGUI() {
		this.setTitle("Choose Attributes");
		this.setSize(330, 356);
		setLocationRelativeTo(mainWindow);
		getContentPane().setLayout(null);
		this.setResizable(false);
		{
			jScrollPane1 = new JScrollPane();
			getContentPane().add(jScrollPane1);
			jScrollPane1.setBounds(7, 49, 301, 238);
			{
				jPanel1 = new JPanel();
				BoxLayout jPanel1Layout = new BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS);
				jPanel1.setLayout(jPanel1Layout);
				jScrollPane1.setViewportView(jPanel1);
			}
		}
		{
			jTextPane1 = new JTextPane();
			getContentPane().add(jTextPane1);
			jTextPane1.setText("Select XML attributes from source files that you wish to include in exported file:");
			jTextPane1.setBounds(7, 7, 301, 35);
			jTextPane1.setOpaque(false);
			jTextPane1.setEditable(false);
		}
		{
			jButton1 = new JButton();
			getContentPane().add(jButton1);
			jButton1.setText("OK");
			jButton1.setBounds(77, 294, 77, 28);
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});
		}
		{
			jButton2 = new JButton();
			getContentPane().add(jButton2);
			jButton2.setText("Cancel");
			jButton2.setBounds(161, 294, 77, 28);
			jButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton2ActionPerformed(evt);
				}
			});
		}
		// setVisible(true);
	}

	/** 
	 * Metoda koja se izvršava nakon pritiska na gumb "Cancel".
	 * @param evt Event koji se stvara pri pritisku na gumb.
	 */
	private void jButton2ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	/** 
	 * Metoda koja se izvršava nakon pritiska na gumb "OK".
	 * @param evt Event koji se stvara pri pritisku na gumb.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {
		Set<String> attributes = new HashSet<String>();
		for (int i = 0; i < checkBoxes.size(); i++) {
			if (checkBoxes.get(i).isSelected()) {
				attributes.add(checkBoxes.get(i).getText());
			}
		}

		if (what == XML) {
			mainWindow.fileExporter.setOutAtts(attributes);
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files (.xml)", "xml");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File(MainWindow.config.getProperty("lastOpenDir")));
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (chooser.getFileFilter().getDescription().equals("XML files (.xml)"))
					try {
						mainWindow.fileExporter.xmlExporter(chooser.getSelectedFile().getAbsolutePath(), side,
								FileExporter.XML);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						mainWindow.fileExporter.xmlExporter(chooser.getSelectedFile().getAbsolutePath(), side,
								FileExporter.ALL);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				MainWindow.config.setProperty("lastOpenDir", chooser.getSelectedFile().getAbsolutePath());
			}
		} else if (what == TEI) {
			mainWindow.fileExporter.setOutAtts(attributes);
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files (.xml)", "xml");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File(MainWindow.config.getProperty("lastOpenDir")));
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (chooser.getFileFilter().getDescription().equals("XML files (.XML)"))
					try {
						mainWindow.fileExporter
								.exportTEI(chooser.getSelectedFile().getAbsolutePath(), FileExporter.XML);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						mainWindow.fileExporter
								.exportTEI(chooser.getSelectedFile().getAbsolutePath(), FileExporter.ALL);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				MainWindow.config.setProperty("lastOpenDir", chooser.getSelectedFile().getAbsolutePath());
			}
		}

		this.dispose();
	}

}
