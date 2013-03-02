package hr.fer.zemris.ktlab.sap.gui;

import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/**
 * Ova klasa naljeđuje klasu <code>DefaultCellEditor</code>. Napravljene su
 * modifikacije kako bi se onemogučilo editiranje čelija tablice. Koristi se za
 * u tablici za prikaz knjižnih oznaka.
 * 
 * @author Igor Šoš
 * 
 */
public class TableUneditableEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public TableUneditableEditor() {
		super(new JTextField());
	}

	public boolean isCellEditable(EventObject e) {
		return false;
	}

	public boolean shouldSelectCell() {
		return true;
	}

}
