package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

/**
 * Klasa predstavlja renderer za čeliju tablice. Koristi se u lijevoj i desnoj
 * tablici. Glavna funkcionalnost ovog renderera je automatsko prikazivanje
 * teksta čelije u više redaka (tako da se vidi cijeli tekst).
 */
public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	/** Varijabla predstavlja defaultni renderer za čeliju. */
	private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();

	/** map from table to map of rows to map of column heights */
	private final Map cellSizes = new HashMap();

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Konstanta koja reprezentira lijevu stranu prozora. */
	public static final short LEFT = 1;

	/** Konstanta koja reprezentira desnu stranu prozora. */
	public static final short RIGHT = 2;

	/** Varijabla koja označava stranu prozora. */
	private short side;

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 * @param side
	 *            Strana prozora (lijeva ili desna).
	 */
	public TextAreaRenderer(MainWindow mainWindow, short side) {
		setLineWrap(true);
		setWrapStyleWord(true);
		this.mainWindow = mainWindow;
		this.side = side;
	}

	public Component getTableCellRendererComponent(
			//
			JTable table, Object obj, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// set the colours, etc. using the standard for that platform
		Color notSureColor = Color.decode(MainWindow.config
				.getProperty("unsafeSplitColor"));
		Color listColor1 = Color.decode(MainWindow.config
				.getProperty("listColor1"));
		Color listColor2 = Color.decode(MainWindow.config
				.getProperty("listColor2"));
		Color fontColor1 = Color.decode(MainWindow.config
				.getProperty("fontColor1"));
		Color fontColor2 = Color.decode(MainWindow.config
				.getProperty("fontColor2"));
		Color selectedFontColor = Color.decode(MainWindow.config
				.getProperty("selectedFontColor"));
		Color listSelectedColor = Color.decode(MainWindow.config
				.getProperty("listSelectedColor"));

		Font font = new java.awt.Font("Tahoma", 0, Integer
				.parseInt(MainWindow.config.getProperty("fontSize")));

		adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus,
				row, column);

		if (!isSelected) {
			if (side == LEFT) {
				if (mainWindow.leftNotSure.contains(row)) {
					adaptee.setBackground(notSureColor);
				} else {
					if (row % 2 == 0) {
						adaptee.setBackground(listColor1);
						adaptee.setForeground(fontColor1);
					} else {
						adaptee.setBackground(listColor2);
						adaptee.setForeground(fontColor2);
					}

				}
			} else {
				if (mainWindow.rightNotSure.contains(row)) {
					adaptee.setBackground(notSureColor);
				} else {
					if (row % 2 == 0) {
						adaptee.setBackground(listColor1);
						adaptee.setForeground(fontColor1);
					}

					else {
						adaptee.setBackground(listColor2);
						adaptee.setForeground(fontColor2);
					}
				}
			}
		} else {
			adaptee.setBackground(listSelectedColor);
			adaptee.setForeground(selectedFontColor);

			if (Boolean.parseBoolean(MainWindow.config
					.getProperty("markOKOnSelect"))) {
				if (side == LEFT) {
					if (mainWindow.leftNotSure.contains(row)) {
						mainWindow.leftNotSure.remove(row);
					}
				} else {
					if (mainWindow.rightNotSure.contains(row)) {
						mainWindow.rightNotSure.remove(row);
					}
				}
			}

		}

		setForeground(adaptee.getForeground());
		setBackground(adaptee.getBackground());

		adaptee.setFont(font);

		if (adaptee.getText().length() <= 2000) {
			setText(adaptee.getText());
		} else {
			setText(adaptee.getText().substring(0, 2000)
					+ "...\n\n <This element is too big. You can see only first 2000 characters. Use Auto split to get smaller elements.>");
		}
		setFont(adaptee.getFont());
		// setRows(getLineCount() + 5);
		setBorder(adaptee.getBorder());

		// if (MainWindow.comments.containsKey(row) == false) {
		// setToolTipText("");
		// } else {
		// setToolTipText(MainWindow.comments.get(row));
		// }

		// This line was very important to get it working with JDK1.4
		TableColumnModel columnModel = table.getColumnModel();
		setSize(columnModel.getColumn(column).getWidth(), 100000);

		int height_wanted;
		if ((int) getPreferredSize().getHeight() > 40) {
			height_wanted = (int) getPreferredSize().getHeight();
		} else {
			height_wanted = 40;
		}

		// MainWindow.heights.put(new Integer(row), new Integer(height_wanted));

		addSize(table, row, column, height_wanted);
		height_wanted = findTotalMaximumRowSize(table, row);
		if (height_wanted != table.getRowHeight(row)) {
			table.setRowHeight(row, height_wanted);
		}

		return this;
	}

	private void addSize(JTable table, int row, int column, int height) {
		Map rows = (Map) cellSizes.get(table);
		if (rows == null) {
			cellSizes.put(table, rows = new HashMap());
		}
		Map rowheights = (Map) rows.get(new Integer(row));
		if (rowheights == null) {
			rows.put(new Integer(row), rowheights = new HashMap());
		}
		rowheights.put(new Integer(column), new Integer(height));
		// MainWindow.heights.put(new Integer(row), new Integer(height));
	}

	/**
	 * Traži kroz sve stupce i dohvaća renderer. Ako je taj renderer
	 * <code>TextAreaRenderer</code>, onda dohvaća njegovu maksimalnu visinu.
	 * 
	 * @param table
	 *            Tablica u kojoj se redak nalazi.
	 * @param row
	 *            Redak kojemu se traži visina.
	 * @return Visina retka.
	 */
	private int findTotalMaximumRowSize(JTable table, int row) {
		int maximum_height = 0;
		Enumeration columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn tc = (TableColumn) columns.nextElement();
			TableCellRenderer cellRenderer = tc.getCellRenderer();
			if (cellRenderer instanceof TextAreaRenderer) {
				TextAreaRenderer tar = (TextAreaRenderer) cellRenderer;
				maximum_height = Math.max(maximum_height, tar
						.findMaximumRowSize(table, row));
			}
		}
		return maximum_height;
	}

	/**
	 * Metoda se koristi za pronalazak maksimalne veličine retka.
	 * 
	 * @param table
	 *            Tablica u kojoj se redak nalazi.
	 * @param row
	 *            Indeks retka.
	 * @return Izračunata visina retka.
	 */
	private int findMaximumRowSize(JTable table, int row) {
		Map rows = (Map) cellSizes.get(table);
		if (rows == null)
			return 0;
		Map rowheights = (Map) rows.get(new Integer(row));
		if (rowheights == null)
			return 0;
		int maximum_height = 0;
		for (Iterator it = rowheights.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			int cellHeight = ((Integer) entry.getValue()).intValue();
			maximum_height = Math.max(maximum_height, cellHeight);
		}
		return maximum_height;
	}

}
