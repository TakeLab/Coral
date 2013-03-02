package hr.fer.zemris.ktlab.sap.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Klasa koja služi za demonstraciju područja unutar kojega veza reagira na klik
 * i micanje pokazivača miša.
 * <p>
 * Klasa prikazuje dvije ikone i vezu koja ih povezuje, no oko veze se dodatno
 * prikazuje područje unutar kojega ona reagira na klik i pomicanje pokazivača
 * miša.
 * <p>
 * Promjenom područje na koje veza reagira, ono se mijenja i u prikazu.
 * 
 * @author Željko Rumenjak
 * 
 */
public class DemoBoard extends JComponent {

	private static final long serialVersionUID = 1L;

	/** Lijeva ikona na koju se spaja veza */
	private Icon leftIcon;

	/** Desna ikona na koju se spaja veza */
	private Icon rightIcon;

	/** Veza koja povezuje ikone */
	private DemoConnection connection;

	/** X koordinata lijeve ikone */
	private int leftX = 5;

	/** Y koordinata lijeve ikone */
	private int leftY = 5;

	/** X koordinata desne ikone */
	private int rightX = 120;

	/** Y koordinata desne ikone */
	private int rightY = 20;

	/**
	 * Javni konstruktor. Prima broj koji definira područje unutar kojega veza
	 * detektira klik i micanje pokazivača miša.
	 * 
	 * @param selectionThreshold
	 *            označava broj piksela za koji je moguće pomaknuti pokazivač od
	 *            veze, a da bi ona još mogla detektirati pomak ili klik miša
	 */
	public DemoBoard(short selectionThreshold) {
		super();

		if (selectionThreshold < 0) {
			throw new IllegalArgumentException(
					"Selection threshold must be positive, but was: "
							+ selectionThreshold);
		}

		createChildren(selectionThreshold);
		setSizes();
	}

	/**
	 * Postavlja prefferedSize i minimumSize, ovisno o području koje zauzimaju
	 * ikone i veza.
	 */
	private void setSizes() {
		Dimension size = new Dimension(
				Math.max(leftIcon.getX() + leftIcon.getWidth(), rightIcon
						.getX()
						+ rightIcon.getWidth()) + 10, Math.max(leftIcon.getY()
						+ leftIcon.getHeight(), rightIcon.getY()
						+ rightIcon.getHeight()) + 10);

		setPreferredSize(size);
		setMinimumSize(size);
	}

	/**
	 * Stvara dvije ikone i vezu koja ih povezuje.
	 * 
	 * @param selectionThreshold
	 *            označava broj piksela za koji je moguće pomaknuti pokazivač od
	 *            veze, a da bi ona još mogla detektirati pomak ili klik miša
	 */
	private void createChildren(short selectionThreshold) {
		leftIcon = new Icon(leftX, leftY, Icon.LEFT_SIDE, 0);
		rightIcon = new Icon(rightX, rightY, Icon.RIGHT_SIDE, 1);
		connection = new DemoConnection(leftIcon, rightIcon, selectionThreshold);

		add(leftIcon);
		add(rightIcon);
		add(connection);
	}

	@Override
	protected void paintChildren(Graphics g) {
		leftIcon.paint(g);
		rightIcon.paint(g);
		connection.paint(g);
	}

	/**
	 * Postavlja broj koji definira područje unutar kojega veza detektira klik i
	 * micanje pokazivača miša.
	 * 
	 * @param threshold
	 *            označava broj piksela za koji je moguće pomaknuti pokazivač od
	 *            veze, a da bi ona još mogla detektirati pomak ili klik miša
	 */
	public void setSelectionTreshold(short threshold) {
		connection.setSelectionThreshold(threshold);
	}

	/**
	 * Vraća broj koji definira područje unutar kojega veza detektira klik i
	 * micanje pokazivača miša.
	 * 
	 * @return broj piksela za koji je moguće pomaknuti pokazivač od veze, a da
	 *         bi ona još mogla detektirati pomak ili klik miša
	 */
	public short getSelectionThreshold() {
		return connection.getSelectionThreshold();
	}

	/**
	 * Privatna klasa koje nasljeđuje vezu, no dodatno iscrtava područje unutar
	 * kojega veza reagira na klik i micanje pokazivača miša.
	 */
	private class DemoConnection extends Connection {

		private static final long serialVersionUID = 1L;

		public DemoConnection(Icon leftIcon, Icon rightIcon,
				short selectionThreshold) {
			super(leftIcon, rightIcon, selectionThreshold);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawPolygon(selectionArea);
		}
	}
}
