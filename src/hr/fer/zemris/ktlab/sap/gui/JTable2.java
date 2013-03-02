package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

/**
 * Klasa nasljeđuje Javinu <code>JTable</code> klasu. Modificirana je metoda
 * <code>public void valueChanged(ListSelectionEvent e)</code> kako bi se
 * ponašanje tablice bolje prilagodilo programu.
 * 
 * @author Igor Šoš
 * 
 */
public class JTable2 extends JTable {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Strana na kojoj se tablica nalazi. */
	private short side;

	/** Konstanta koja reprezentira lijevu stranu prozora. */
	public static final short LEFT = 0;

	/** Konstanta koja reprezentira desnu stranu prozora. */
	public static final short RIGHT = 1;

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param mainWindow
	 *            Glavni prozor.
	 * @param side
	 *            Strana prozora na kojoj se tablica nalazi.
	 */
	public JTable2(MainWindow mainWindow, short side) {
		super();
		this.mainWindow = mainWindow;
		this.side = side;
	}

	/**
	 * Metoda je modificirana tako da se pri svakoj promijeni vrijednosti u
	 * tablici poziva metoda za osvježavanje tablice s detaljima elementa.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Sentence tmpSentence = null;
		//
		// for (int i = 0; i < getRowCount(); i++) {
		// tmpSentence = (Sentence) getModel().getValueAt(i, 0);
		// tmpSentence.getIcon().deselect();
		// }
		//
		// int selectedRows[] = getSelectedRows();
		// for (int i = 0; i < selectedRows.length; i++) {
		// tmpSentence = (Sentence) getModel().getValueAt(selectedRows[i], 0);
		// //tmpSentence.getIcon().select();
		//			
		// Set<Integer> keys = dataModel.getConnections(tmpSentence.getId());
		// for (Integer key : keys) {
		// int row = rightRows.get(key);
		// jTable2.changeSelection(row, 0, true, false);
		// }
		// }
		if (side == LEFT) {
			mainWindow.leftDetailsTableValueChanged(e);
		} else if (side == RIGHT) {
			mainWindow.rightDetailsTableValueChanged(e);
		}

		super.valueChanged(e);
	}

}
