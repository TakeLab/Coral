package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.util.Sentence;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

/**
 * Ova klasa je implementacija KeyListener-a koja se koristi za pretraživanje
 * lijeve, odnosno desne tablice. Pretraživanje se vrši odmah pri unosu svakog
 * novog slova, a ne čeka se na potvrdu na kraju unosa.
 * 
 * @author Igor Šoš
 * 
 */
public class KeyListener implements java.awt.event.KeyListener {

	/** Tekstualno polje s tekstom koji se traži u lijevoj ili desnoj tablici. */
	private JTextField textField;

	/** Tablica koja se pretražuje. */
	private JTable2 table;

	/** Model tablice koja se pretražuje. */
	private TableModel tableModel;

	/** Indeks početka podstringa u elementu u kojemu je pronađen traženi tekst. */
	private int findPosition = 0;

	/**
	 * Redak u kojemu je pronađen traženi podstring. Od ovog retka se nastavlja
	 * pretraživanje
	 */
	private int lastSelectedRow = 0;

	/**
	 * Konstruktor klase.
	 * 
	 * @param textField
	 *            Tekstualno polje s tekstom koji se traži u lijevoj ili desnoj
	 *            tablici.
	 * @param table
	 *            Tablica koja se pretražuje.
	 * @param tableModel
	 *            Model tablice koja se pretražuje.
	 */
	public KeyListener(JTextField textField, JTable2 table, TableModel tableModel) {
		this.textField = textField;
		this.table = table;
		this.tableModel = tableModel;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja vrši pretraživanje. Pretražuje se cijela tablica sve dok se
	 * ne nađe traženi podstring u nekome od redaka, ili dok se ne dođe do kraja
	 * tablice.
	 * 
	 * @param startCell
	 *            Indeks retka s kojime počinje pretraživanje.
	 * @param startChar
	 *            Indeks znaka u elementu od kojega počinje pretraživanje.
	 */
	private void search(int startCell, int startChar) {

		int i = startCell;
		while (true) {
			lastSelectedRow = i;
			if (i >= tableModel.getRowCount()) {
				textField.setBackground(Color.RED);
				break;
			}

			Sentence sentence = (Sentence) tableModel.getValueAt(i, 0);
			String element = sentence.getSentence().toLowerCase();
			String searchText = textField.getText().toLowerCase();
			findPosition = element.indexOf(searchText, startChar);

			if (findPosition >= 0) {
				textField.setBackground(Color.WHITE);
				table.editCellAt(i, 0);
				JTextArea textArea = (JTextArea) table.getEditorComponent();
				textArea.select(findPosition, findPosition + textField.getText().length());
				textArea.getCaret().setSelectionVisible(true);
				break;
			}

			System.out.println(findPosition + textField.getText() + "\n");

			i++;
			startChar = 0;
		}
	}

	/**
	 * Metoda koja se poziva pritiskom na tipku next. Započinje se traženje
	 * sljedećeg pojavljivanja traženog teksta u tablici.
	 */
	public void next() {
		search(lastSelectedRow, findPosition + 1);
	}

	/**
	 * Metoda koja se automatski poziva nakon što se se pusti neka tipka koja je
	 * bila pritistnuta. Ukoliko je ta tipka ENTER, poziva se metoda
	 * <code>void next()</code> i započinje se pretraživanje od mjesta na
	 * kojemu se prethodno stalo. Za sve ostale tipke započinje se pretraživanje
	 * od početka tablice.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			next();
		} else {
			search(0, 0);
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public void setTable(JTable2 table) {
		this.table = table;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

}
