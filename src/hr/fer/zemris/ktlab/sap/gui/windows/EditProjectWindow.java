package hr.fer.zemris.ktlab.sap.gui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;



/**
 * Klasa EditProjectWindow predstavlja jedan vrlo jednostavan prozor koji
 * omogućuje promjenu naziva projekta te promjenu oba jezika. Pri otvaranju
 * prozora, u njega se učitavaju trenutni naziv projekta i odabrani jezici. Ti
 * podaci pohranjeni su u podatkovnom modelu.
 * 
 * @author Igor Šoš
 * 
 */
public class EditProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Gumb OK. */
	private JButton jButton1;

	/** Gumb Cancel. */
	private JButton jButton2;

	/** Izbor drugog jezika. */
	private JComboBox jComboBox2;
	private JLabel jLabel3;

	/** Izbor prvog jezika. */
	private JComboBox jComboBox1;
	private JLabel jLabel2;

	/** Polje za promjenu naziva projekta. */
	private JTextField jTextField1;
	private JLabel jLabel1;

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public EditProjectWindow(MainWindow mainWindow) {
		super(mainWindow, true);
		this.mainWindow = mainWindow;
		initGUI();
	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		{
			this.setTitle(MainWindow.language.getProperty("editProjectWindow"));
			this.setResizable(false);
			getContentPane().setLayout(null);
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText(MainWindow.language.getProperty("chProjName"));
				jLabel1.setBounds(12, 12, 262, 14);
			}
			{
				jTextField1 = new JTextField();
				getContentPane().add(jTextField1);
				jTextField1.setText(mainWindow.dataModel
						.getProperty("projectName"));
				jTextField1.setBounds(12, 32, 262, 21);
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2);
				jLabel2.setText(MainWindow.language
						.getProperty("chLeftLanguage"));
				jLabel2.setBounds(12, 65, 262, 14);
			}
			{
				ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(
						mainWindow.languages);
				jComboBox1 = new JComboBox();
				getContentPane().add(jComboBox1);
				jComboBox1.setSelectedItem(mainWindow.dataModel
						.getProperty("language1"));
				jComboBox1.setModel(jComboBox1Model);
				jComboBox1.setBounds(12, 85, 142, 21);
				jComboBox1.setSize(150, 21);
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3);
				jLabel3.setText(MainWindow.language
						.getProperty("chRightLanguage"));
				jLabel3.setBounds(12, 118, 262, 14);
			}
			{
				ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(
						mainWindow.languages);
				jComboBox2 = new JComboBox();
				getContentPane().add(jComboBox2);
				jComboBox2.setSelectedItem(mainWindow.dataModel
						.getProperty("language1"));
				jComboBox2.setModel(jComboBox2Model);
				jComboBox2.setBounds(12, 138, 150, 21);
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1);
				jButton1.setText(MainWindow.language.getProperty("buttonOK"));
				jButton1.setBounds(56, 188, 80, 21);
				jButton1.setSize(80, 23);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2);
				jButton2.setText(MainWindow.language
						.getProperty("buttonCancel"));
				jButton2.setBounds(147, 188, 80, 21);
				jButton2.setSize(80, 23);
				jButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2ActionPerformed(evt);
					}
				});
			}
			this.setSize(300, 250);
		}
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(mainWindow);
		this.setVisible(true);

	}

	/**
	 * Metoda koja se izvršava nakon pritiska na gumb OK.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {
		mainWindow.setProjectProperties(jTextField1.getText().trim(),
				(String) jComboBox1.getSelectedItem(), (String) jComboBox2
						.getSelectedItem(), mainWindow.dataModel
						.getProperty("file1"), mainWindow.dataModel
						.getProperty("file2"));
		mainWindow.loadProjectProperties();
		this.dispose();
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na gumb Cancel.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton2ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

}
