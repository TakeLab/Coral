package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;
import hr.fer.zemris.ktlab.sap.util.Sentence;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * Ova klasa je implementacija MouseListener-a. Koristi se za praćenje rada
 * mišom nad lijevom i desnom tablicom. Kod desnog klika na neki element,
 * aktivira se odgovarajući padajući izbornik. Kod otpuštanja lijeve tipka miša,
 * označavaju se ikone koje odogovaraju odnačenim elementima te se označavaju
 * elementi suprotne tablice koji su povezani s označenim elementima tablice nad
 * kojom je kliknuto.
 * 
 * @author Igor Šoš
 * 
 */
public class TableMouseListener implements MouseListener {

	/**
	 * Vrijednost koja označava lijevu ili desnu tablicu (1 == lijevo, 2 ==
	 * desno).
	 */
	private int table;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Konstruktor klase. */
	public TableMouseListener(MainWindow theWindow, int table) {
		this.mainWindow = theWindow;
		this.table = table;
	}

	/**
	 * Metoda koja se poziva pri kliku miša nad tablicom. Ne radi ništa.
	 */
	@Override
	public void mouseClicked(MouseEvent evt) {
	}

	/**
	 * Metoda koja se poziva pri ulasku miša nad tablicu. Ne radi ništa.
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja se poziva kada miš napusti tablicu. Ne radi ništa.
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja se poziva kada se pritisne tipka na mišu. Ne radi ništa.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja se poziva kada se pusti tipka na mišu. Kod desnog klika na
	 * neki element, aktivira se odgovarajući padajući izbornik. Kod otpuštanja
	 * lijeve tipka miša, označavaju se ikone koje odogovaraju odnačenim
	 * elementima te se označavaju elementi suprotne tablice koji su povezani s
	 * označenim elementima tablice nad kojom je kliknuto.
	 */
	@Override
	public void mouseReleased(MouseEvent evt) {

		if (table == 1) {
			if (SwingUtilities.isLeftMouseButton(evt)) {
				int[] selectedRows = mainWindow.jTable1.getSelectedRows();
				Sentence tmpSentence = null;

				mainWindow.sboard.clearSelection();

				for (int i = 0; i < selectedRows.length; i++) {
					tmpSentence = (Sentence) mainWindow.jTable1Model.getValueAt(selectedRows[i], 0);

					if (tmpSentence.getIcon().isSelected()) {
						tmpSentence.getIcon().deselect();
					} else {
						tmpSentence.getIcon().select();
					}
				}

				mainWindow.jTable2.clearSelection();
				Set<Integer> keys = mainWindow.dataModel.getConnections(tmpSentence.getId());
				for (Integer key : keys) {
					mainWindow.jTable2.changeSelection(mainWindow.rightRowWithId.get(key), 0, true, false);
				}
				
				//if (mainWindow.mar)
			}

			if (SwingUtilities.isRightMouseButton(evt)) {
				int row = mainWindow.jTable1.rowAtPoint(evt.getPoint());
				mainWindow.jTable1.changeSelection(row, 0, false, false);

				if (!mainWindow.rowsWithBookmarksLeft.contains(row)) {
					if (mainWindow.leftNotSure.contains(row)) {
						mainWindow.popupMenuLeft1.remove(mainWindow.markAsOk1);
						mainWindow.popupMenuLeft1.add(mainWindow.markAsOk1);
					} else {
						mainWindow.popupMenuLeft1.remove(mainWindow.markAsOk1);
					}

					mainWindow.popupMenuLeft1.show(mainWindow.jTable1, evt.getX(), evt.getY());
				} else {
					if (mainWindow.leftNotSure.contains(row)) {
						mainWindow.popupMenuLeft2.remove(mainWindow.markAsOk1);
						mainWindow.popupMenuLeft2.add(mainWindow.markAsOk1);
					} else {
						mainWindow.popupMenuLeft2.remove(mainWindow.markAsOk1);
					}

					mainWindow.popupMenuLeft2.show(mainWindow.jTable1, evt.getX(), evt.getY());
				}

			}

		} else if (table == 2) {
			if (SwingUtilities.isLeftMouseButton(evt)) {
				int[] selectedRows = mainWindow.jTable2.getSelectedRows();
				Sentence tmpSentence = null;

				mainWindow.sboard.clearSelection();


				for (int i = 0; i < selectedRows.length; i++) {
					tmpSentence = (Sentence) mainWindow.jTable2Model.getValueAt(selectedRows[i], 0);

					if (tmpSentence.getIcon().isSelected()) {
						tmpSentence.getIcon().deselect();
					} else {
						tmpSentence.getIcon().select();
					}
				}

				mainWindow.jTable1.clearSelection();
				Set<Integer> keys = mainWindow.dataModel.getConnections(tmpSentence.getId());
				for (Integer key : keys) {
					mainWindow.jTable1.changeSelection(mainWindow.leftRowWithId.get(key), 0, true, false);
				}
			}

			if (SwingUtilities.isRightMouseButton(evt)) {
				int row = mainWindow.jTable2.rowAtPoint(evt.getPoint());
				mainWindow.jTable2.changeSelection(row, 0, false, false);

				if (!mainWindow.rowsWithBookmarksRight.contains(row)) {
					if (mainWindow.rightNotSure.contains(row)) {
						mainWindow.popupMenuRight1.remove(mainWindow.markAsOk2);
						mainWindow.popupMenuRight1.add(mainWindow.markAsOk2);
					} else {
						mainWindow.popupMenuRight1.remove(mainWindow.markAsOk2);
					}

					mainWindow.popupMenuRight1.show(mainWindow.jTable2, evt.getX(), evt.getY());

				} else {
					if (mainWindow.rightNotSure.contains(row)) {
						mainWindow.popupMenuRight2.remove(mainWindow.markAsOk2);
						mainWindow.popupMenuRight2.add(mainWindow.markAsOk2);
					} else {
						mainWindow.popupMenuRight2.remove(mainWindow.markAsOk2);
					}

					mainWindow.popupMenuRight2.show(mainWindow.jTable2, evt.getX(), evt.getY());
				}

			}
		}
		mainWindow.reDrawVisibleIcons();
		mainWindow.sboard.repaint();
	}
}
