package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.ErrorWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Klasa koja sadrži samo <code>main</code> metodu. Koristi se za pokretanje
 * programa.
 * 
 * @author Igor Šoš
 * 
 */
public class Main {

	/**
	 * Metoda koja se poziva nakon pokretanja programa.
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				public void run() {
					MainWindow theWindow = null;
					try {
						theWindow = new MainWindow();
					} catch (Exception ex) {
						//PROVJERITI JEL TU TREBA NESTO NAPRAVITI
					}

				}
			});
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		}

	}

}
