package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

/**
 * Klasa je implementacija MouseListener-a. Koristi se za efekte nad gumbima
 * grafičkog sučelja pri prelasku miša preko njih.
 * 
 * @author Igor Šoš
 * 
 */
public class MouseButtonListener implements MouseListener {

	/** Glavni prozor programa. */
	MainWindow mainWindow;

	/** Border koji se postavlja na gumb kada je pokazivač miša iznad njega. */
	Border mOverBorder = BorderFactory.createBevelBorder(0);

	/**
	 * Konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public MouseButtonListener(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 * Metoda koja se poziva pri kliku miša. Ne radi ništa.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja se poziva kada se pokazivač miša pozicionira iznad gumba.
	 * Mijenja se izgled bordera.
	 */
	@Override
	public void mouseEntered(MouseEvent evt) {
		if (evt.getSource() instanceof JButton) {
			JButton button = (JButton) evt.getSource();
			button.setBorder(mOverBorder);
		} else {
			JToggleButton button = (JToggleButton) evt.getSource();
			button.setBorder(mOverBorder);
		}

	}

	/**
	 * Metoda koja se poziva kada pokazivač miša napusti poziciju iznad gumba.
	 * Izgled bordera se vraća na originalnu vrijednost.
	 */
	@Override
	public void mouseExited(MouseEvent evt) {
		if (evt.getSource() instanceof JButton) {
			JButton button = (JButton) evt.getSource();
			button.setBorder(mainWindow.buttonBorder);
		} else {
			JToggleButton button = (JToggleButton) evt.getSource();
			button.setBorder(mainWindow.buttonBorder);
		}

	}

	/**
	 * Metoda koja se poziva kada se pritisne tipka na mišu. Ne radi ništa.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja se poziva kada se pusti pritistnuta tipka na mišu. Ne radi
	 * ništa.
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
