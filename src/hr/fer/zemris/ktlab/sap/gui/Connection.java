package hr.fer.zemris.ktlab.sap.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * Veza predstavlja liniju koja povezuje dvije ikone.
 * <p>
 * Veza detektira kada se pokazivač miša nalazi iznad nje i drugačije se
 * iscrtava, nego kada pokazivač miša nije iznad nje. Također se detektiraju
 * lijevi i desni klik miša iznad veze.
 * <p>
 * Ako se dogodi lijevi klik miša iznad veze, veza mijenja stanje iz
 * selektiranog u deselektirano i šalje se događaj pod imenom
 * "ConnectionSelected" sa <code>Boolean</code> vrijednošću koja označava da i
 * je veza označena. Veza se različito prikazuje, ovisno stanju u kojem se
 * nalazi.
 * <p>
 * U slučaju da se detektira desni klik iznad veze, veza šalje događaj pod
 * imenom "DeleteConnection" na koji bi klasa u kojoj se veza nalazi trebala
 * reagirati tako da izbriše vezu.
 * <p>
 * Područje na kojem veza detektira micanje i klikove miša nije pravokutnik kao
 * kod drugih komponenti, već paralelogram kojemu su dvije stranice paralelne sa
 * y-osi, a druge dvije su paralelne sa linijom koja predstavlja vezu.
 * Udaljenost od linije koja predstavlja vezu se može mijenjati i na taj način
 * može se povećati ili smanjiti područje na kojem veza detektira klik i pomake
 * miša.
 * 
 * @author Željko Rumenjak
 * 
 */
public class Connection extends JComponent implements MouseListener {

	private static final long serialVersionUID = 6749707570849850606L;

	/** Lijeva ikona veze (mora se nalaziti na lijevoj strani komponente) */
	Icon leftIcon;

	/** Desna ikona veze (mora se nalaziti na desnoj strani komponente) */
	Icon rightIcon;

	/**
	 * Poligon koji označava područje u kojem veza detektira klikove i pomake
	 * miša. Taj poligon ima oblik paralelograma.
	 */
	protected Polygon selectionArea;

	/**
	 * Broj piksela koji definira udaljenost od linije koja predstavlja vezu do
	 * stranice paralelograma (<code>selectionArea</code>) koja je paralelna
	 * sa njom.
	 */
	private short selectionThreshold;

	/**
	 * Primjenjuje se na <code>Graphics</code> objekt prije crtanja veze iznad
	 * koje se ne nalazi pokazivač miša. Definira prozirnost veze.
	 */
	static private AlphaComposite normal;

	/**
	 * Primjenjuje se na <code>Graphics</code> objekt prije crtanja veze iznad
	 * koje se nalazi pokazivač miša. Definira prozirnost veze.
	 */
	static private AlphaComposite mouseOver;

	/**
	 * Primjenjuje se na <code>Graphics</code> objekt prije crtanja veze koja
	 * nije označena. Definira boju kojem se crta veza.
	 */
	static private Color colorNormal;

	/**
	 * Primjenjuje se na <code>Graphics</code> objekt prije crtanja veze koja
	 * je označena. Definira boju kojem se crta veza.
	 */
	static private Color colorSelected;

	/** Označava da li je veza označena */
	private boolean selected = false;

	/** Označava da li je pokazivač miša unutar <code>selectionArea</code> veze */
	private boolean mouseInside = false;

	/**
	 * Javni konstruktor veze. Veza je linija koja povezuje dvije ikone koje se
	 * nalaze na različitim stranama.
	 * 
	 * @param leftIcon
	 *            ikona lijeve strane
	 * @param rightIcon
	 *            ikona desne strane
	 * @param selectionTreshold
	 *            definira veličinu područja unutar kojeg veza detektira klik i
	 *            micanje miša
	 */
	public Connection(Icon leftIcon, Icon rightIcon, short selectionTreshold) {
		super();
		if (leftIcon == null || rightIcon == null) {
			throw new IllegalArgumentException("Icon cannot be null");
		}

		if (leftIcon.getSide() != Icon.LEFT_SIDE) {
			throw new IllegalArgumentException(
					"First icon must be on the left side, but was on the right");
		}

		if (rightIcon.getSide() != Icon.RIGHT_SIDE) {
			throw new IllegalArgumentException(
					"Cannot connect two icons that are on the same side");
		}

		ComponentAdapter componentAdapter = new ComponentAdapter() {

			@Override
			public void componentMoved(ComponentEvent e) {
				updateLocation();
			}
		};

		this.leftIcon = leftIcon;
		this.rightIcon = rightIcon;
		this.leftIcon.addComponentListener(componentAdapter);
		this.rightIcon.addComponentListener(componentAdapter);
		this.addMouseListener(this);

		normal = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		mouseOver = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);

		colorNormal = Color.BLUE;
		colorSelected = Color.BLACK;

		setSelectionThreshold(selectionTreshold);
	}

	/**
	 * Crta veza uzimajući u obzir da li je ona označena ili ne, te da li je
	 * pokazivač miša unutar nje ili ne.
	 */
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setComposite(normal);
		if (selected) {
			g2d.setColor(colorSelected);
		} else {
			g2d.setColor(colorNormal);
		}

		if (mouseInside) {
			g2d.setComposite(mouseOver);
		}

		if (leftIcon.isShowing() && rightIcon.isShowing()) {
			g.drawLine(leftIcon.getConnectionPoint().x, leftIcon
					.getConnectionPoint().y, rightIcon.getConnectionPoint().x,
					rightIcon.getConnectionPoint().y);
		}
	}

	/**
	 * Dohvaća <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 * veza iznad kojih se ne nalazi pokazivač miša.
	 * 
	 * @return <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 *         veza iznad kojih se ne nalazi pokazivač miša
	 */
	public static AlphaComposite getNormalAlpha() {
		return normal;
	}

	/**
	 * Postavlja <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 * veza iznad kojih se ne nalazi pokazivač miša.
	 * 
	 * @param alpha
	 *            <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 *            veza iznad kojih se ne nalazi pokazivač miša
	 */
	public static void setNormalAlpha(AlphaComposite alpha) {
		if (alpha == null) {
			throw new IllegalArgumentException("Alpha cannot be null");
		}
		normal = alpha;
	}

	/**
	 * Dohvaća <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 * veza iznad kojih se nalazi pokazivač miša.
	 * 
	 * @return <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 *         veza iznad kojih se nalazi pokazivač miša
	 */
	public static AlphaComposite getMouseOverAlpha() {
		return mouseOver;
	}

	/**
	 * Postavlja <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 * veza iznad kojih se nalazi pokazivač miša.
	 * 
	 * @param alpha
	 *            <code>AlphaComposite</code> koji se koristi kod iscrtavanja
	 *            veza iznad kojih se nalazi pokazivač miša
	 */
	public static void setMouseOverAlpha(AlphaComposite alpha) {
		if (alpha == null) {
			throw new IllegalArgumentException("Alpha cannot be null");
		}
		mouseOver = alpha;
	}

	/**
	 * Dohvaća boju koja se koristi kod iscrtavanja veza koje nisu selektirane.
	 * 
	 * @return boja koja se koristi kod iscrtavanja veza koje nisu selektirane
	 */
	public static Color getConnectionColor() {
		return colorNormal;
	}

	/**
	 * Postavlja boju koja se koristi kod iscrtavanja veza koje nisu
	 * selektirane.
	 * 
	 * @param color
	 *            boja koja se koristi kod iscrtavanja veza koje nisu
	 *            selektirane
	 */
	public static void setConnectionColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		colorNormal = color;
	}

	/**
	 * Dohvaća boju koja se koristi kod iscrtavanja veza koje su selektirane.
	 * 
	 * @return boja koja se koristi kod iscrtavanja veza koje su selektirane
	 */
	public static Color getSelectedConnectionColor() {
		return colorSelected;
	}

	/**
	 * Postavlja boju koja se koristi kod iscrtavanja veza koje su selektirane.
	 * 
	 * @param color
	 *            boja koja se koristi kod iscrtavanja veza koje su selektirane
	 */
	public static void setSelectedConnectionColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		colorSelected = color;
	}

	/**
	 * Vraća broj koji definira područje unutar kojega veza detektira klik i
	 * micanje pokazivača miša.
	 * 
	 * @return broj piksela za koji je moguće pomaknuti pokazivač od veze, a da
	 *         bi ona još mogla detektirati pomak ili klik miša
	 */
	public short getSelectionThreshold() {
		return selectionThreshold;
	}

	/**
	 * Postavlja broj koji definira područje unutar kojega veza detektira klik i
	 * micanje pokazivača miša.
	 * 
	 * @param threshold
	 *            označava broj piksela za koji je moguće pomaknuti pokazivač od
	 *            veze, a da bi ona još mogla detektirati pomak ili klik miša
	 */
	public void setSelectionThreshold(short threshold) {
		if (threshold < 0) {
			throw new IllegalArgumentException(
					"Threshold cannot be negative, but was: " + threshold);
		}

		this.selectionThreshold = threshold;
		updateLocation();
	}

	/**
	 * Izračunava područje unutar kojega veza detektira klik ili pomak miša, te
	 * osvježava položaj veze u ovisnosti o položaju ikona koje ona povezuje.
	 */
	private void updateLocation() {
		int leftX = leftIcon.getConnectionPoint().x;
		int leftY = leftIcon.getConnectionPoint().y;
		int rightX = rightIcon.getConnectionPoint().x;
		int rightY = rightIcon.getConnectionPoint().y;
		selectionArea = new Polygon();

		super.setLocation(leftX, Math.min(leftY, rightY) - 5);
		super.setSize(Math.abs(leftX - rightX), Math.abs(leftY - rightY) + 10);

		int threshold = calculateTreshold(Math.abs(leftX - rightX), Math
				.abs(leftY - rightY));

		selectionArea.addPoint(leftX, leftY - threshold);
		selectionArea.addPoint(leftX, leftY + threshold);
		selectionArea.addPoint(rightX, rightY + threshold);
		selectionArea.addPoint(rightX, rightY - threshold);
	}

	/**
	 * Izračunava udaljenost od početne točke veze do točke presjeka pravca koji
	 * se nalazi na udaljenost <code>selectionThreshold</code> od veze i
	 * paralelan je s njom i pravca koji je paralelan sa y-osi koordinatnog
	 * sustava i prolazi početnom točkom veze.
	 * 
	 * @param deltaX
	 *            udaljenost između lijeve i desne ikone po x-osi
	 * @param deltaY
	 *            udaljenost između lijeve i desne ikone po y-osi
	 * @return udaljenost točke presjeka i početne točke veze
	 */
	private int calculateTreshold(int deltaX, int deltaY) {
		double tan = (double) deltaY / deltaX;
		double alpha = Math.atan(tan);
		alpha = Math.PI / 2 - alpha;
		double result = selectionThreshold / Math.sin(alpha);
		return (int) Math.round(result);
	}

	@Override
	public boolean contains(int x, int y) {
		return contains(new Point(x, y));
	}

	@Override
	public boolean contains(Point p) {
		if (!leftIcon.isVisible() || !rightIcon.isVisible()) {
			return false;
		}
		p.translate(getX(), getY());
		return selectionArea.contains(p);
	}

	/**
	 * Provjerava je li veza označena
	 * 
	 * @return <code>true</code> ako veza je označena, <code>false</code>
	 *         ako nije
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Mijenja stanje veze iz selektiranog u deselektirano ili obratno te
	 * obavještava sve slušatelje da je došlo do promjene stanja.
	 */
	private void toggleSelected() {
		selected = !selected;
		repaint();
		firePropertyChange("ConnectionSelected", !selected, selected);
	}

	/**
	 * Selektira vezu, ako ona već nije selektirana. Ako je veza već selektirana
	 * ne događa se ništa.
	 */
	public void select() {
		if (isSelected()) {
			return;
		} else {
			toggleSelected();
		}
	}

	/**
	 * Deselektira vezu, ako je ona selektirana. Ako veza nije selektirana ne
	 * događa se ništa.
	 */
	public void deselect() {
		if (!isSelected()) {
			return;
		} else {
			toggleSelected();
		}
	}

	public void mouseClicked(MouseEvent e) {
		//Ignored
	}

	public void mouseEntered(MouseEvent e) {
		mouseInside = true;
		repaint();

	}

	public void mouseExited(MouseEvent e) {
		mouseInside = false;
		repaint();

	}

	public void mousePressed(MouseEvent e) {
		// Ignored

	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && mouseInside) {
			toggleSelected();
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			firePropertyChange("DeleteConnection", null, null);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((leftIcon == null) ? 0 : leftIcon.hashCode());
		result = PRIME * result
				+ ((rightIcon == null) ? 0 : rightIcon.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Connection other = (Connection) obj;
		if (leftIcon == null) {
			if (other.leftIcon != null)
				return false;
		} else if (!leftIcon.equals(other.leftIcon))
			return false;
		if (rightIcon == null) {
			if (other.rightIcon != null)
				return false;
		} else if (!rightIcon.equals(other.rightIcon))
			return false;
		return true;
	}
}
