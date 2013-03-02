package hr.fer.zemris.ktlab.sap.gui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klasa StatisticsWindow predstavlja prozor pomoću kojega korisnik može dobiti
 * uvid u statistiku rada na projektu (brojčano izražene sve akcije koje je
 * korisnik napravio) te zapise (eng. log) rada programa. Za pohranjivanje i
 * obradu statistike i zapisa koristi se klasa Statistics.
 * 
 * @author Igor Šoš
 * 
 */
public class StatisticsWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Prvi panel s kliznim trakama. */
	private JScrollPane jScrollPane1;

	/** Tekstualno područje za prikazivanje statistike. */
	private JTextArea jTextArea1;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Drugi panel s kliznim trakama. */
	private JScrollPane jScrollPane2;

	/** Gumb za spremanje statistike. */
	private JButton jButton1;

	/** Gumb za spremanje zapisa. */
	private JButton jButton2;

	/** Tekstualno područje za prikazivanje zapisa (log). */
	private JTextArea jTextArea2;

	/** Panel s karticama (tabovima). */
	private JTabbedPane jTabbedPane1;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public StatisticsWindow(MainWindow mainWindow) {
		super(mainWindow, true);
		this.mainWindow = mainWindow;
		initGUI();

	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		this.setSize(400, 440);
		this.setTitle("Statistics & Log");
		setLocationRelativeTo(mainWindow);

		this.setResizable(false);
		getContentPane().setLayout(null);
		{
			jTabbedPane1 = new JTabbedPane();
			getContentPane().add(jTabbedPane1);
			jTabbedPane1.setBounds(7, 7, 378, 357);
			{
				jScrollPane1 = new JScrollPane();
				jTabbedPane1.addTab("Statistics", null, jScrollPane1, null);
				jScrollPane1.setBounds(14, 105, 371, 259);
				{
					jTextArea1 = new JTextArea();
					jScrollPane1.setViewportView(jTextArea1);
					jTextArea1.setText(mainWindow.stats.toString(mainWindow));
					jTextArea1.setEditable(false);
					jTextArea1
							.setPreferredSize(new java.awt.Dimension(367, 279));
					jTextArea1.setFont(new java.awt.Font("Tahoma", 0, Integer
							.parseInt((String) MainWindow.config
									.get("fontSize"))));
				}
			}
			{
				jScrollPane2 = new JScrollPane();
				jTabbedPane1.addTab("Log", null, jScrollPane2, null);
				{
					jTextArea2 = new JTextArea();
					jScrollPane2.setViewportView(jTextArea2);
					jTextArea2.setText(mainWindow.stats.getLog());
					jTextArea2.setEditable(false);
					jTextArea2.setFont(new java.awt.Font("Tahoma", 0, Integer
							.parseInt((String) MainWindow.config
									.get("fontSize"))));
				}
			}
		}
		{
			jButton1 = new JButton();
			getContentPane().add(jButton1);
			jButton1.setText("Save stats...");
			jButton1.setBounds(77, 378, 98, 28);
			jButton1.setSize(100, 28);
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});
		}
		{
			jButton2 = new JButton();
			getContentPane().add(jButton2);
			jButton2.setText("Save log...");
			jButton2.setBounds(182, 378, 98, 28);
			jButton2.setSize(100, 28);
			jButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton2ActionPerformed(evt);
				}
			});
		}
		setVisible(true);
	}

	/**
	 * Metoda pomoću koje se sprema sadržaj tekstualnog područja u datoteku.
	 * 
	 * @param textArea
	 *            Tekstualno područje čiji sadržaj se sprema.
	 */
	private void save(JTextArea textArea) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"TXT files (.txt)", "txt");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File(MainWindow.config
				.getProperty("lastOpenDir")));
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			OutputStream fout = null;
			OutputStream bout = null;
			OutputStreamWriter output = null;
			try {
				fout = new FileOutputStream(chooser.getSelectedFile()
						.getAbsolutePath()
						+ ".txt");
				bout = new BufferedOutputStream(fout);
				output = new OutputStreamWriter(bout, "UTF-8");

				output.write(textArea.getText());
				output.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

		}
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na prvi gumb.
	 * @param evt Event pritiska na gumb.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {
		save(jTextArea1);
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na drugi gumb.
	 * @param evt Event pritiska na gumb.
	 */
	private void jButton2ActionPerformed(ActionEvent evt) {
		save(jTextArea2);
	}

}
