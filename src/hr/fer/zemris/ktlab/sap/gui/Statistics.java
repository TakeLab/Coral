package hr.fer.zemris.ktlab.sap.gui;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

/**
 * Klasa služi za praćenje statistike rada u programu i generiranje logova.
 * 
 * @author Igor Šoš
 * 
 */
public class Statistics implements Serializable {

	private static final long serialVersionUID = -6908206075332570482L;

	/** Broj kreiranih veza. */
	public int createdConnections = 0;

	/** Broj uklonjenih veza. */
	public int removedConnections = 0;

	/** Broj spojenih elemenata u lijevoj tablici. */
	public int mergedElements1 = 0;

	/** Broj spojenih elemenata u desnoj tablici. */
	public int mergedElements2 = 0;

	/** Broj rucno podijeljenih elemenata u lijevoj tablici. */
	public int splitedElements1 = 0;

	/** Broj rucno podijeljenih elemenata u desnoj tablici. */
	public int splitedElements2 = 0;

	/** Broj automatski podijeljenih elemenata u lijevoj tablici. */
	public int autoSplitedElements1 = 0;

	/** Broj automatski podijeljenih elemenata u lijevoj tablici. */
	public int autoSplitedElements2 = 0;

	/** Broj stvorenih knjižnih oznaka. */
	public int bookmarksAdded = 0;

	/** Broj uklonjenih knjižnih oznaka. */
	public int bookmarksRemoved = 0;

	/** Broj uklonjenih elemenata iz lijeve tablice. */
	public int elementsRemoved1 = 0;

	/** Broj uklonjenih elemenata iz desne tablice. */
	public int elementsRemoved2 = 0;

	/** Ukupni broj elemenata dodanih u lijevu tablicu. */
	public int elementsAdded1 = 0;

	/** Ukupni broj elemenata dodanih u desnu tablicu. */
	public int elementsAdded2 = 0;

	/** Log rada u programu. */
	public StringBuilder log = new StringBuilder();

	/** Log u koji se sprema ispis sa standardnog izlaza za greške */
	private StringBuilder errorLog = new StringBuilder();

	public SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yyyy. - H:m");

	/**
	 * Konstruktor razreda.
	 */
	public Statistics() {
//		System.setErr(new PrintStream(new FilteredStreamStdErr(
//				new ByteArrayOutputStream())));
	}

	/**
	 * Metoda koja vraća vrijednosti statistike pohranjene u ovoj klasi
	 * spremljene u jedan string u čitljivom formatu.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public String toString(MainWindow mainWindow) {
		StringBuilder sb = new StringBuilder();

		sb.append("Current number of left elements: ").append(
				mainWindow.jTable1.getRowCount()).append("\r\n");
		sb.append("Current number of right elements: ").append(
				mainWindow.jTable2.getRowCount()).append("\r\n");
		sb.append("Number of elements added to left list: ").append(
				elementsAdded1).append("\r\n");
		sb.append("Number of elements added to right list: ").append(
				elementsAdded2).append("\r\n");
		sb.append("Number of elements removed from left list: ").append(
				elementsRemoved1).append("\r\n");
		sb.append("Number of elements removed from right list: ").append(
				elementsRemoved2).append("\r\n");
		sb.append("Number of splited elements in left list: ").append(
				splitedElements1).append("\r\n");
		sb.append("Number of splited elements in right list: ").append(
				splitedElements2).append("\r\n");
		sb.append("Number of merged elements in left list: ").append(
				mergedElements1).append("\r\n");
		sb.append("Number of merged elements in right list: ").append(
				mergedElements2).append("\r\n");
		sb.append("Number of created connections: ").append(createdConnections)
				.append("\r\n");
		sb.append("Number of removed connections: ").append(removedConnections)
				.append("\r\n");

		return sb.toString();
	}

	/**
	 * Pomoću ove metode se dodaju novi zapisi u log programa.
	 * 
	 * @param event
	 *            Novi zapis koji se dodaje u log.
	 */
	public void addToLog(String event) {
		log.append(dateFormat.format(new Date()));
		log.append(": ");
		log.append(event);
		log.append("\r\n");
	}

	/**
	 * Metoda kojom se dohvaća cijeli log.
	 * 
	 * @return Log sa zapisom dosadašnjih aktivnosti.
	 */
	public String getLog() {
		return log.toString();
	}

	/**
	 * Metoda kojom se dohvaća log sa svim greškama.
	 * 
	 * @return Log sa zapisom svih grešaka koju su se dogodile od stvaranja ove
	 *         klase.
	 */
	public String getErrorLog() {
		return errorLog.toString();
	}

	private class FilteredStreamStdErr extends FilterOutputStream {
		public FilteredStreamStdErr(OutputStream out) {
			super(out);
		}

		@Override
		public void write(byte[] b) throws IOException {
			String text = new String(b);
			errorLog.append(text);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			String text = new String(b, off, len);
			errorLog.append(text);
		}
	}

}
