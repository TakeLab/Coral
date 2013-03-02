package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Zadatak ove klase je da se omogući pomicanje (skrolanje) središnje komponente
 * te lijeve i desne tablice pomoću kotačića na mišu kada se pokazivač miša
 * nalazi iznad središnje komponente.
 * 
 * @author Igor
 * 
 */
public class SboardMouseWheelListener implements MouseWheelListener {

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public SboardMouseWheelListener(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 * Metoda koja se poziva kad se pokrene kotačić na mišu, a pokazivač se
	 * nalazi iznad središnje komponente. Radi tako da se scroll barovi lijeve i
	 * desne tablice uvećaju za isti iznos.
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		JScrollBar2 leftSBar = (JScrollBar2) mainWindow.jScrollPane1
				.getVerticalScrollBar();
		JScrollBar2 rightSBar = (JScrollBar2) mainWindow.jScrollPane2
				.getVerticalScrollBar();

		int scrollAmount = e.getUnitsToScroll()
				* Integer.parseInt(MainWindow.config
						.getProperty("sboardScrollAmount"));
		leftSBar.setValue(leftSBar.getValue() + scrollAmount);
		rightSBar.setValue(rightSBar.getValue() + scrollAmount);

	}

}
