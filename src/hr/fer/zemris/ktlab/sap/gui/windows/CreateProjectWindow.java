package hr.fer.zemris.ktlab.sap.gui.windows;

import hr.fer.zemris.ktlab.sap.io.AdvancedFileLoader;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klasa CreateProjectWindow predstavlja prozor koji se prikazuje korisniku pri
 * kreiranju novog projekta. Pomoću ovog prozora potrebno je zadati ime projekta
 * te dvije ulazne datoteke (paralelni korpusi u XML ili TXT formatu). Također,
 * za svaku datoteku potrebno je odabrati (ili upisati) jezik i ispravan charset
 * encoding u kojemu je datoteka pohranjena. Podaci o jeziku koriste se pri
 * eksportiranju u TMX datoteku, a encoding je bitan kod čitanja datoteke. Nakon
 * pritiska na tipku "OK" provjerava se ispravnost podataka koje je korisnik
 * unio te se, ako su podaci ispravni, pokreće učitavanje datoteka u model
 * (pomoću klase FileLoader).
 * 
 * @author Igor Šoš
 * 
 */
public class CreateProjectWindow extends JDialog {

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
	private JButton jButton1;
	private JCheckBox jCheckBox2;
	private JButton jButton5;
	private JComboBox jComboBox4;
	private JComboBox jComboBox3;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel5;
	private JPanel jPanel2;
	private JTextField jTextField3;
	private JComboBox jComboBox2;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JComboBox jComboBox1;
	private JButton jButton4;
	private JButton jButton3;
	private JCheckBox jCheckBox1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JButton jButton2;
	private JTextField jTextField2;
	private JTextField jTextField1;
	private JFileChooser chooser;
	private Component parent = this;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/**
	 * Lista u koju se pohranjuju nazivi tagova koje treba ignorirati kod
	 * čitanja prve datoteke.
	 */
	public List<String> ignoreList = new ArrayList<String>();

	/**
	 * Lista u koju se pohranjuju nazivi tagova koji sadrže podatak o novom
	 * ulomku prve datoteke.
	 */
	public List<String> paragraphList = new ArrayList<String>();

	/**
	 * Lista u koju se pohranjuju nazivi tagova koje treba ignorirati kod
	 * čitanja druge datoteke.
	 */
	public List<String> ignoreList2 = new ArrayList<String>();

	/**
	 * Lista u koju se pohranjuju nazivi tagova koji sadrže podatak o novom
	 * ulomku druge datoteke.
	 */
	public List<String> paragraphList2 = new ArrayList<String>();

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param parentWindow
	 *            Glavni prozor programa.
	 */
	public CreateProjectWindow(MainWindow parentWindow) {
		super(parentWindow);
		this.mainWindow = parentWindow;
		initGUI();
	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		setTitle("Create New Project");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(498, 496);
		setModal(true);
		setLocationRelativeTo(mainWindow);
		addPropertyChangeListener(mainWindow);
		getContentPane().setLayout(null);
		this.setResizable(false);
		{
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1);
			jPanel1.setLayout(null);
			jPanel1.setBounds(14, 60, 462, 155);
			jPanel1.setBorder(BorderFactory
					.createTitledBorder("First language"));
			{
				jTextField1 = new JTextField();
				jPanel1.add(jTextField1);
				jTextField1.setBounds(16, 48, 336, 20);
				jTextField1.setSize(336, 22);
			}
			{
				jButton1 = new JButton();
				jPanel1.add(jButton1);
				jButton1.setText("Browse");
				jButton1.setBounds(362, 49, 84, 20);
				jButton1.setSize(84, 22);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jLabel1 = new JLabel();
				jPanel1.add(jLabel1);
				jLabel1.setText(MainWindow.language.getProperty("SelFirstXML"));
				jLabel1.setBounds(16, 20, 336, 28);
			}
			{
				jCheckBox1 = new JCheckBox();
				jPanel1.add(jCheckBox1);
				jCheckBox1.setText(MainWindow.language
						.getProperty("isSentenceSplitted"));
				jCheckBox1.setBounds(15, 70, 336, 28);
				jCheckBox1.setSize(336, 22);
			}
			{
				jLabel4 = new JLabel();
				jPanel1.add(jLabel4);
				jLabel4.setText("Language:");
				jLabel4.setBounds(16, 94, 100, 28);
			}
			{
				if (mainWindow.languages == null) {
					mainWindow.languages = new String[] { "en-US", "hr" };
				}
				ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(
						mainWindow.languages);
				jComboBox2 = new JComboBox();
				jPanel1.add(jComboBox2);
				jComboBox2.setModel(jComboBox2Model);
				jComboBox2.setBounds(16, 117, 150, 20);
				jComboBox2.setSize(150, 22);
				jComboBox2.setEditable(true);
			}
			{
				jLabel6 = new JLabel();
				jPanel1.add(jLabel6);
				jLabel6.setText("Encoding:");
				jLabel6.setBounds(211, 101, 108, 14);
			}
			{
				if (mainWindow.encodings == null) {
					mainWindow.encodings = new String[] { "UTF-8", "Cp1250",
							"ISO8859_2" };
				}
				ComboBoxModel jComboBox3Model = new DefaultComboBoxModel(
						mainWindow.encodings);
				jComboBox3 = new JComboBox();
				jPanel1.add(jComboBox3);
				jComboBox3.setModel(jComboBox3Model);
				jComboBox3.setBounds(211, 116, 126, 22);
				jComboBox3.setEditable(true);
			}
		}
		{
			jButton3 = new JButton();
			getContentPane().add(jButton3);
			jButton3.setText("OK");
			jButton3.setBounds(153, 427, 77, 28);
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
			jButton4.setBounds(240, 427, 84, 28);
			jButton4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton4ActionPerformed(evt);
				}
			});
		}
		{
			jLabel5 = new JLabel();
			getContentPane().add(jLabel5);
			jLabel5.setText("Project name:");
			jLabel5.setBounds(18, 16, 99, 14);
		}
		{
			jTextField3 = new JTextField();
			getContentPane().add(jTextField3);
			jTextField3.setBounds(18, 32, 458, 20);
			jTextField3.setSize(458, 22);
		}
		{
			jPanel2 = new JPanel();
			getContentPane().add(jPanel2);
			jPanel2.setLayout(null);
			jPanel2.setBounds(14, 226, 462, 190);
			jPanel2.setBorder(BorderFactory
					.createTitledBorder("Second language"));
			{
				jTextField2 = new JTextField();
				jPanel2.add(jTextField2);
				jTextField2.setBounds(16, 48, 336, 20);
				jTextField2.setSize(336, 22);
			}
			{
				jCheckBox2 = new JCheckBox();
				jPanel2.add(jCheckBox2);
				jCheckBox2.setText(MainWindow.language
						.getProperty("isSentenceSplitted"));
				jCheckBox2.setBounds(16, 67, 336, 28);
			}
			{
				jLabel2 = new JLabel();
				jPanel2.add(jLabel2);
				jLabel2.setText(MainWindow.language.getProperty("SelSecXML"));
				jLabel2.setBounds(16, 19, 336, 28);
			}
			{
				jLabel3 = new JLabel();
				jPanel2.add(jLabel3);
				jLabel3.setText("Language:");
				jLabel3.setBounds(16, 95, 98, 28);
			}
			{
				ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(
						mainWindow.languages);
				jComboBox1 = new JComboBox();
				jPanel2.add(jComboBox1);
				jComboBox1.setModel(jComboBox1Model);
				jComboBox1.setBounds(16, 119, 147, 20);
				jComboBox1.setSize(147, 22);
				jComboBox1.setEditable(true);
			}
			{
				jButton2 = new JButton();
				jPanel2.add(jButton2);
				jButton2.setText("Browse");
				jButton2.setBounds(362, 49, 84, 20);
				jButton2.setSize(84, 22);
				jButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2ActionPerformed(evt);
					}
				});
			}
			{
				jLabel7 = new JLabel();
				jPanel2.add(jLabel7);
				jLabel7.setText("Encoding:");
				jLabel7.setBounds(204, 102, 129, 14);
			}
			{
				ComboBoxModel jComboBox4Model = new DefaultComboBoxModel(
						mainWindow.encodings);
				jComboBox4 = new JComboBox();
				jPanel2.add(jComboBox4);
				jComboBox4.setModel(jComboBox4Model);
				jComboBox4.setBounds(204, 119, 129, 22);
				jComboBox4.setEditable(true);
			}
		}
		{
			jButton5 = new JButton();
			getContentPane().add(jButton5);
			jButton5.setText("Advanced");
			jButton5.setBounds(371, 427, 105, 28);
			jButton5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton5ActionPerformed(evt);
				}
			});
		}
		setVisible(true);
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na tipku Browse.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"TXT files (.txt)", "txt");
		chooser.setFileFilter(filter);
		filter = new FileNameExtensionFilter("XML files (.xml)", "xml");
		chooser.setFileFilter(filter);

		chooser.setCurrentDirectory(new File(MainWindow.config
				.getProperty("lastOpenDir")));
		int returnVal = chooser.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			jTextField1.setText(chooser.getSelectedFile().getAbsolutePath());
			MainWindow.config.setProperty("lastOpenDir", chooser
					.getSelectedFile().getAbsolutePath());
		}
	}

	private void jButton2ActionPerformed(ActionEvent evt) {
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"TXT files (.txt)", "txt");
		chooser.setFileFilter(filter);
		filter = new FileNameExtensionFilter("XML files (.xml)", "xml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File(MainWindow.config
				.getProperty("lastOpenDir")));
		int returnVal = chooser.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			jTextField2.setText(chooser.getSelectedFile().getAbsolutePath());
			MainWindow.config.setProperty("lastOpenDir", chooser
					.getSelectedFile().getAbsolutePath());
		}
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na tipku Cancel.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton4ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na tipku OK.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton3ActionPerformed(ActionEvent evt) {
		boolean ok = true;
		jTextField1.setBackground(Color.WHITE);
		jTextField2.setBackground(Color.WHITE);
		jTextField3.setBackground(Color.WHITE);

		if ((jTextField1.getText()).trim().equals("")) {
			jTextField1.setBackground(Color.RED);
			ok = false;
		}

		if ((jTextField2.getText()).trim().equals("")) {
			jTextField2.setBackground(Color.RED);
			ok = false;
		}

		if ((jTextField3.getText()).trim().equals("")) {
			jTextField3.setBackground(Color.RED);
			ok = false;
		}

		if (ok) {
			// mainWindow.clear();
			// mainWindow.sboard.clear();

			AdvancedFileLoader handler = new AdvancedFileLoader(
					mainWindow.dataModel, AdvancedFileLoader.LEFT, ignoreList,
					paragraphList);

			String path = jTextField1.getText();
			// String[] splited = path.split("\\.");
			// String extension = "";
			// if (splited.length > 1) {
			// extension = splited[splited.length - 1];
			// }
			//
			// XMLReader xr = null;
			// FileInputStream fis = null;
			// InputStreamReader isr = null;
			// if (extension.equals("xml")) {
			//
			// try {
			// xr = XMLReaderFactory.createXMLReader();
			// xr.setContentHandler(handler);
			// // xr.setErrorHandler(handler);
			// fis = new FileInputStream(jTextField1.getText());
			// isr = new InputStreamReader(fis, (String)
			// jComboBox3.getSelectedItem());
			// xr.parse(new InputSource(isr));
			//
			// } catch (SAXException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// } else {
			// handler.loadTXT(path, (String) jComboBox3.getSelectedItem());
			// }

			AdvancedFileLoader handler2 = new AdvancedFileLoader(
					mainWindow.dataModel, AdvancedFileLoader.RIGHT,
					ignoreList2, paragraphList2);

			String path2 = jTextField2.getText();
			// splited = path2.split("\\.");
			// extension = "";
			// if (splited.length > 1) {
			// extension = splited[splited.length - 1];
			// }
			//
			// if (extension.equals("xml")) {
			// try {
			// xr = XMLReaderFactory.createXMLReader();
			// xr.setContentHandler(handler2);
			// // xr.setErrorHandler(handler2);
			// fis = new FileInputStream(jTextField2.getText());
			// isr = new InputStreamReader(fis, (String)
			// jComboBox4.getSelectedItem());
			// xr.parse(new InputSource(isr));
			// } catch (SAXException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// } else {
			// handler2.loadTXT(path2, (String) jComboBox4.getSelectedItem());
			// }

			// TableColumnModel cmodel = mainWindow.jTable1.getColumnModel();
			// TextAreaRenderer textAreaRenderer = new
			// TextAreaRenderer(mainWindow, TextAreaRenderer.LEFT);
			// cmodel.getColumn(0).setCellRenderer(textAreaRenderer);
			// TextAreaEditor textEditor = new
			// TextAreaEditor(mainWindow.dataModel);
			// cmodel.getColumn(0).setCellEditor(textEditor);
			//
			// TableColumnModel cmodel2 = mainWindow.jTable2.getColumnModel();
			// TextAreaRenderer textAreaRenderer2 = new
			// TextAreaRenderer(mainWindow, TextAreaRenderer.RIGHT);
			// cmodel2.getColumn(0).setCellRenderer(textAreaRenderer2);
			// TextAreaEditor textEditor2 = new
			// TextAreaEditor(mainWindow.dataModel);
			// cmodel2.getColumn(0).setCellEditor(textEditor2);
			//			
			// mainWindow.reloadKeyListener();

			// mainWindow.sboard.repaint();
			this.dispose();
			mainWindow.worker.startCreatingNewProject(handler, handler2, path,
					path2, (String) jComboBox3.getSelectedItem(),
					(String) jComboBox4.getSelectedItem(), jTextField3
							.getText().trim(), (String) jComboBox2
							.getSelectedItem(), (String) jComboBox1
							.getSelectedItem());

//			mainWindow.setProjectProperties(jTextField3.getText().trim(),
//					(String) jComboBox2.getSelectedItem(), (String) jComboBox1
//							.getSelectedItem(), jTextField1.getText(),
//					jTextField2.getText());

			// mainWindow.loadProjectProperties();

			mainWindow.languages = mainWindow
					.addToStringArray(mainWindow.languages, (String) jComboBox1
							.getSelectedItem());
			mainWindow.languages = mainWindow
					.addToStringArray(mainWindow.languages, (String) jComboBox2
							.getSelectedItem());
			mainWindow.encodings = mainWindow
					.addToStringArray(mainWindow.encodings, (String) jComboBox3
							.getSelectedItem());
			mainWindow.encodings = mainWindow
					.addToStringArray(mainWindow.encodings, (String) jComboBox4
							.getSelectedItem());

			firePropertyChange("ClosedCreateProjectWindow", false, true);
		}
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na tipku Advanced.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton5ActionPerformed(ActionEvent evt) {
		new CreateProjectAdvancedWindow(this);
	}
}
