package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Ova klasa nasljeđuje klasu <code>JScrollBar2</code> te predstavlja malo
 * modificiranu kliznu traku koja se koristi na lijevoj i desnoj tablici za
 * prikaz elemenata. Modifikacije su napravljene kako bi se omogučila opcija
 * zajedničkog klizanja ("skrolanja") obje tablice i pomicanja ikona pri
 * klizanju neke od tablica.
 * 
 * @author Igor Šoš
 * 
 */
public class JScrollBar2 extends JScrollBar {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/**
	 * Podatak o strani na kojoj se nalazi klizna traka (1 == lijevo, 2 ==
	 * desno).
	 */
	private int side;

	/** Klizna traka suprotne strane. */
	private JScrollPane scrollPane2;

	/**
	 * Konstruktor klase.
	 * 
	 * @param sboard
	 *            Središnja komponenta glavnog prozora.
	 * @param side
	 *            Podatak o strani na kojoj se nalazi klizna traka (1 == lijevo,
	 *            2 == desno).
	 * @param scrollPane2
	 *            Klizna traka suprotne strane.
	 * @param mainWindow
	 *            Glavni prozor.
	 */
	public JScrollBar2(Switchboard sboard, int side, JScrollPane scrollPane2,
			MainWindow mainWindow) {
		super();
		this.side = side;
		this.scrollPane2 = scrollPane2;
		this.mainWindow = mainWindow;
	}

	/**
	 * Metoda koja se automatski poziva pri promijeni vrijednosti klizne trake.
	 */
	@Override
	public void setValue(int value) {
		super.setValue(value);
		if (MainWindow.config.get("scrollType").equals("normal")) {
			if (side == 1) {
				// sboard.scrollLeftSide(-getValue());
				mainWindow.reDrawVisibleIcons();
			} else {
				// sboard.scrollRightSide(-getValue());
				mainWindow.reDrawVisibleIcons();
			}
		} else if (MainWindow.config.get("scrollType").equals("together")) {
			if (side == 1) {
				// sboard.scrollLeftSide(-getValue());
				JScrollBar2 sb = (JScrollBar2) scrollPane2
						.getVerticalScrollBar();
				sb.setValueTogether(value, 2);
				mainWindow.reDrawVisibleIcons();
			} else {
				// sboard.scrollRightSide(-getValue());
				JScrollBar2 sb = (JScrollBar2) scrollPane2
						.getVerticalScrollBar();
				sb.setValueTogether(value, 1);
				mainWindow.reDrawVisibleIcons();
			}
		}

	}

	/**
	 * Metoda koja se koristi za zajedničko klizanje lijeve i desne klizne
	 * trake.
	 * 
	 * @param value
	 *            Vrijednost na koju se klizna traka postavlja.
	 * @param side
	 *            Strana klizne trake kojoj se mijenja vrijednost.
	 */
	public void setValueTogether(int value, int side) {
		super.setValue(value);
		if (side == 1) {
			// sboard.scrollLeftSide(-getValue());
		} else {
			// sboard.scrollRightSide(-getValue());
		}

	}

}
