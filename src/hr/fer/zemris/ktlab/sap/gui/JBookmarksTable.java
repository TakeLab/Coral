package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;
import hr.fer.zemris.ktlab.sap.util.Bookmark;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;

/**
 * Ova klasa nasljeđuje klasu <code>JTable</code> te predstavlja malo
 * modificiranu verziju tablice. Ova tablica je prilagođena prikazivanju
 * knjižnih oznaka. Pri označavanju čelije ove tablice, automatski se označava i
 * element kojemu ta knjižna oznaka odgovara.
 * 
 * @author Igor Šoš
 * 
 */
public class JBookmarksTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/**
	 * Konstruktor klase.
	 * @param mainWindow Glavni prozor programa.
	 * @param model Podatkovni model tablice.
	 */
	public JBookmarksTable(MainWindow mainWindow, TableModel model) {
		super(model);
		this.mainWindow = mainWindow;
	}

	/**
	 * Metoda koja se automatski poziva kada se promijeni neka vrijednost u tablici.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int row = getSelectedRow();

		if (row >= 0) {
			Bookmark bookmark = (Bookmark) getModel().getValueAt(row, 0);
			if (bookmark.getSetNumber() == Bookmark.SET1) {
				int rowAtTable = mainWindow.leftRowWithId.get(bookmark.getKey());
				mainWindow.jTable1.changeSelection(rowAtTable, 0, false, false);
			}
			if (bookmark.getSetNumber() == Bookmark.SET2) {
				int rowAtTable = mainWindow.rightRowWithId.get(bookmark.getKey());
				mainWindow.jTable2.changeSelection(rowAtTable, 0, false, false);
			}
		}
		super.valueChanged(e);
	}
}
