package hr.fer.zemris.ktlab.sap.gui.windows;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 * Klasa <code>WorkingWindow</code> predstavlja prozor koji se pojavljuje pri
 * izvršavanju neke dugotrajne akcije (npr. izvršavanje Gale-Church algoritma
 * nad dva velika paralelna korpusa, segmentacija na rečenice, …) te onemogućuje
 * korisnikovu interakciju s programom sve dok se ne dovrši rad algoritma.
 * 
 * @author Igor Šoš
 * 
 */
public class WorkingWindow extends javax.swing.JDialog implements
		PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Traka za prikazivanje napretka. */
	private JProgressBar jProgressBar1;

	/** Tekstualni panel za prikazivanje poruke. */
	private JTextPane jTextPane1;

	/** Panel s kliznim trakama. */
	private JScrollPane jScrollPane1;

	/**
	 * Varijabla koja se koristi za pokretanje nove dretve unutar koje se odvija
	 * neki dugotrajan posao.
	 */
	private SwingWorker<Void, Void> worker;

	/**
	 * Vrijeme koliko neka akcija treba trajati prije nego se ovaj prozor
	 * pojavi.
	 */
	private int timeToWait = 2000;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 * @param worker
	 *            Radnik.
	 */
	public WorkingWindow(MainWindow mainWindow, SwingWorker<Void, Void> worker) {
		super(mainWindow, true);
		this.worker = worker;
		this.worker.addPropertyChangeListener(this);
		this.worker.execute();

		this.mainWindow = mainWindow;
		initGUI();
	}

	/**
	 * Metoda služi za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		this.setTitle("Working...");
		this.setSize(285, 140);
		this.setLocationRelativeTo(mainWindow);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);

		getContentPane().setLayout(null);
		{
			jScrollPane1 = new JScrollPane();
			getContentPane().add(jScrollPane1);
			jScrollPane1.setBounds(12, 12, 253, 57);
			{
				jTextPane1 = new JTextPane();
				jScrollPane1.setViewportView(jTextPane1);
				jTextPane1.setText("Working...\nPlease wait...");
				jTextPane1.setEditable(false);
			}
		}
		{
			jProgressBar1 = new JProgressBar();
			getContentPane().add(jProgressBar1);
			jProgressBar1.setBounds(12, 87, 253, 14);
			jProgressBar1.setIndeterminate(true);
		}
		startWaitingThread();
	}

	/**
	 * Metoda podiže novu dretvu koja čeka <code>timeToWait</code>
	 * milisekundi. Ako se u tom vremenu nije izvršila određena akcija, onda se
	 * pokreće <code>WorkingWindow</code>.
	 */
	private void startWaitingThread() {
		Thread waitingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(timeToWait);
				} catch (InterruptedException ignorable) {
				}

				if (!worker.isDone()) {
					WorkingWindow.this.setVisible(true);
				}
			}

		});

		waitingThread.setDaemon(true);
		waitingThread.start();
	}

	/**
	 * Metoda služi za postavljanje teksta u prozor.
	 * 
	 * @param text
	 *            Tekst koji se postavlja.
	 */
	public void setText(String text) {
		jTextPane1.setText(text);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("DONE")) {
			this.dispose();
		}
		// } else if (evt.getPropertyName().equals("SHOW")) {
		// this.setVisible(true);
		// }

	}

}
