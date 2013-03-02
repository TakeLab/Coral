package hr.fer.zemris.ktlab.sap.gui.windows;

import hr.fer.zemris.ktlab.sap.gui.Worker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Klasa <code>GaleChurchWindow</code> predstavlja prozor koji se pojavljuje
 * prije pokretanja Gale-Church algoritma za povezivanje rečenica. U ovome
 * prozoru korisnik mora odabrati širinu djelovanja Gale-Church algoritma te
 * unijeti podatak da li je u izvornim datotekama postojala podjela na ulomke.
 * Nakon pritiska na tipku "OK", uneseni podaci se prosljeđuju klasi Gale-Church
 * algoritma i pokreće se spajanje rečenica.
 * 
 * @author Igor Šoš
 * 
 */
public class GaleChurchWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	MainWindow mainWindow;

	/** Gumb za recenicno sravnjivanje */
	private JButton jButton1;
	private JLabel jLabel1;

	/** Gumb za odlomacko sravnjivanje  */
	private JButton jButton2;
	private JLabel jLabel2;
	
	/** Gumb Cancel */
	private JButton jButton3;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public GaleChurchWindow(MainWindow mainWindow) {
		super(mainWindow, true);
		this.mainWindow = mainWindow;
		initGUI();
	}

	/**
	 * Metoda sluzi za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		setTitle("Gale-Church");
		this.setSize(300, 210);
		setLocationRelativeTo(mainWindow);
		this.setResizable(false);
		getContentPane().setLayout(null);
		
		{
			jButton1 = new JButton();
			getContentPane().add(jButton1);
			jButton1.setText("Sentence alignment");
			jButton1.setBounds(21, 20, 100, 25);
			jButton1.setSize(100, 25);
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});
		}
		{
			jButton2 = new JButton();
			getContentPane().add(jButton2);
			jButton2.setText("Paragraph alignement");
			jButton2.setBounds(21, 80, 100, 25);
			jButton2.setSize(100, 25);
			jButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton2ActionPerformed(evt);
				}
			});
		}
		{
			jButton3 = new JButton();
			getContentPane().add(jButton3);
			jButton3.setText("Cancel");
			jButton3.setBounds(50, 140, 50, 25);
			jButton3.setSize(50, 25);
			jButton3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton3ActionPerformed(evt);
				}
			});
		}
		{
			jLabel1 = new JLabel();
			getContentPane().add(jLabel1);
			jLabel1.setText("Aligns sentences between paragraph boundaries.");
			jLabel1.setBounds(31, 50, 164, 25);
		}
		{
			jLabel2 = new JLabel();
			getContentPane().add(jLabel2);
			jLabel2.setText("Aligns paragraphs. Produces no result if sentence alignment has already been performed.");
			jLabel2.setBounds(31, 110, 164, 25);
		}

		setVisible(true);

	}

	/**
	 * Metoda koja se izvršava nakon pritiska na Sentence alignment gumb.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {
		Worker worker = new Worker(mainWindow);
		this.dispose();
		worker.alignSentencesGaleChurch();
	}
	
	/**
	 * Metoda koja se izvršava nakon pritiska na Paragraph alignment gumb.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton2ActionPerformed(ActionEvent evt) {
		Worker worker = new Worker(mainWindow);
		this.dispose();
		worker.alignParagraphsGaleChurch();
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na Cancel gumb.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton3ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

}
