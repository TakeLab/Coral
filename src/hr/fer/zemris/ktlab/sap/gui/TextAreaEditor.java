package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;
import hr.fer.zemris.ktlab.sap.util.DataModel;
import hr.fer.zemris.ktlab.sap.util.Sentence;

import javax.swing.*;

/**
 * Klasa nasljeđuje klasu <code>DefaultCellEditor</code>. Načinjene su
 * modifikacije kako editor, nakon editiranja, ne bi u tablicu spremao običan
 * String nego objekt klase Sentence.
 * 
 * @author Igor Šoš
 * 
 */
public class TextAreaEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public TextAreaEditor(final DataModel model) {
		super(new JTextField());
		final JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setFont(new java.awt.Font("Tahoma", 0, Integer.parseInt(MainWindow.config.getProperty("fontSize"))));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		editorComponent = textArea;

		delegate = new DefaultCellEditor.EditorDelegate() {
			Sentence sentence = null;

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {
				textArea.setText((value != null) ? value.toString() : "");
				sentence = (Sentence) value;
			}

			public Object getCellEditorValue() {
				sentence.setSentence(textArea.getText());
				model.setElement(sentence.getId(), sentence.getSentence());
				return sentence;
			}

		};
	}
}