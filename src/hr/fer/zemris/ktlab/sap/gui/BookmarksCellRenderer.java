package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.util.Bookmark;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * Jednostavna implementacija TableCellRenderer-a napravljena za prikazivanje
 * čelija tablice s knjižnim oznakama. Složeno je da se kao tooltip čelije
 * prikazuje opis knjižne oznake.
 * 
 * @author Igor Šoš
 * 
 */
public class BookmarksCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * Metoda koju tablica automatski poziva dok se označi neka čelija.
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Bookmark bookmark = (Bookmark) value;
		setToolTipText(bookmark.getDescription());

		return this;
	}

}
