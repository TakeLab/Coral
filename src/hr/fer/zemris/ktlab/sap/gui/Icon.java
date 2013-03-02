package hr.fer.zemris.ktlab.sap.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * Predstavlja jednu ikonu. Sve instance ove klase dijele sliku koja se
 * prikazuje kao ikona. Svaka ikona ima jedinstveni identifikator koji joj se
 * predaje kod stvaranja nove instance ove klase.
 * <p>
 * Ikona može pripadati lijevoj ili desnoj strani komponente u koju se stavlja i
 * njezina strana se ne može naknadno promijeniti. Ikona može biti povezana sa
 * drugima ikonama koje se ne nalaze na istoj strani.
 * <p>
 * U nekim situacijama se ne crtaju neke ili sve veze od ikone prema drugim
 * ikonama. Kako bi se to prikazalo korisniku pokraj ikone se može nacrtati broj
 * koji označava koliko veza nije nacrtano.
 * <p>
 * Ikona detektira kada se pokazivač miša nalazi iznad nje i drugačije se
 * iscrtava nego kada pokazivač miša nije iznad nje. Također se detektiraju
 * klikovi miša iznad ikone. Na lijevi klik miša ikona mijenja stanje iz
 * deselektiranog u selektirano, ili obratno. To stanje se također uzima u obzir
 * kod iscrtavanja ikone te se ona drugačije iscrtava ako je selektirana ili ne.
 * Ako je na ikoni postavljen kontekstni izbornik tada se on prikazuje na desni
 * klik miša, inače se ne događa ništa.
 * <p>
 * Kod promjene stanje ikone u selektirano ili deselektirano šalje se događaj sa
 * imenom "IconSelected" i <code>Boolean</code> vrijednost koja označava da li
 * je ikona selektirana.
 * 
 * @author Željko Rumenjak
 * 
 */
public class Icon extends JComponent implements MouseListener {

	private static final long serialVersionUID = 1L;

	/** Globalna konstanta koja označava ikonu lijeve strane */
	public static final short LEFT_SIDE = 0;

	/** Globalna konstanta koja označava ikonu desne strane */
	public static final short RIGHT_SIDE = 1;

	/** Jedinstveni identifikator ikone */
	private int id;

	/** Strana na kojoj se ikona nalazi */
	private short side;

	/** Slika koja se prikazuje kao ikona */
	private static Image image = null;

	/** Označava da li je ikona označena ili ne */
	private boolean selected = false;

	/** Označava da li je pokazivač miša unutar područja ikone */
	private boolean mouseInside = false;

	/**
	 * Broj veza ikone koje nisu nacrtane, ako je veći od 0, pojaviti će se
	 * pokraj ikone
	 * 
	 */
	private int invisibleConnections = 0;

	/** Boja kojom se iscrtava područje iza broja nevidljivih veza */
	private static Color invisibleCounterBackground = Color.WHITE;

	/** Boja kojom se iscrtava okvir oko broja nevidljivih veza */
	private static Color invisibleCounterBorder = Color.BLACK;

	/** Boja kojom se iscrtava broj nevidljivih veza */
	private static Color invisibleCounterForeground = Color.RED;

	/**
	 * Boja čijim nijansama se iscrtava područje ikone kada je označena ili kada
	 * se pokazivač nalazi iznad nje
	 */
	private static Color iconSelectionColor = Color.BLUE;

	/**
	 * Javni konstruktor koji uzima pretpostavljene vrijednosti za visinu i
	 * širinu ikone (30, 30).
	 * 
	 * @param x
	 *            x koordinata ikone
	 * @param y
	 *            y koordinata ikone
	 * @param side
	 *            strana na kojoj se ikona nalazi
	 * @param id
	 *            jedinstveni identifikator ikone
	 */
	public Icon(int x, int y, short side, int id) {
		super();
		if (side == LEFT_SIDE || side == RIGHT_SIDE) {
			this.side = side;
		} else {
			throw new IllegalArgumentException("Side must be " + LEFT_SIDE
					+ " or " + RIGHT_SIDE + ", but was: " + side);
		}

		super.setLocation(x, y);
		super.setSize(30, 30);
		this.id = id;
		this.addMouseListener(this);
	}

	/**
	 * Javni konstruktor koji uzima u obzir visinu i širinu.
	 * 
	 * @param x
	 *            x koordinata ikone
	 * @param y
	 *            y koordinata ikone
	 * @param height
	 *            visina ikona
	 * @param width
	 *            širina ikone
	 * @param side
	 *            strana na kojoj se ikona nalazi
	 * @param id
	 *            jedinstveni identifikator ikone
	 */
	public Icon(int x, int y, int height, int width, short side, int id) {
		super();
		if (side == LEFT_SIDE || side == RIGHT_SIDE) {
			this.side = side;
		} else {
			throw new IllegalArgumentException("Side must be " + LEFT_SIDE
					+ " or " + RIGHT_SIDE + ", but was: " + side);
		}

		super.setLocation(x, y);
		super.setSize(width, height);
		this.id = id;
		this.addMouseListener(this);
	}

	/**
	 * Crta ikonu uzimajući u obzir da li je pokazivač miša iznad nje, te da li
	 * je označena ili ne.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (image != null) {
			AlphaComposite alpha = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1);
			g2d.setComposite(alpha);
			g2d.drawImage(image, getLocation().x + 5, getLocation().y + 5,
					getWidth() - 10, getHeight() - 10, this);
		}

		if (invisibleConnections > 0) {
			String number = String.valueOf(invisibleConnections);
			int x = getLocation().x + 1;
			int y = getLocation().y + getHeight() - 2;
			Rectangle2D stringBouds = g2d.getFontMetrics().getStringBounds(
					number, g2d);

			g2d.setColor(invisibleCounterBackground);
			g2d.fillRect(x, y - (int) stringBouds.getHeight() + 2,
					(int) stringBouds.getWidth() + 4, (int) stringBouds
							.getHeight());
			g2d.setColor(invisibleCounterBorder);
			g2d.drawRect(x, y - (int) stringBouds.getHeight() + 2,
					(int) stringBouds.getWidth() + 4, (int) stringBouds
							.getHeight());

			g2d.setColor(invisibleCounterForeground);
			g2d.drawString(String.valueOf(invisibleConnections), x + 3, y);
		}

		if (selected || mouseInside || image == null) {
			AlphaComposite alpha = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(alpha);
			g2d.setColor(iconSelectionColor);
			g2d.drawRoundRect(getLocation().x, getLocation().y, getWidth() - 1,
					getHeight() - 1, 5, 5);
		}

		if (selected) {
			AlphaComposite alpha = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.2f);
			g2d.setComposite(alpha);
			g2d.setColor(iconSelectionColor);
			g2d.fillRoundRect(getLocation().x, getLocation().y, getWidth(),
					getHeight(), 5, 5);
		}

		if (mouseInside) {
			AlphaComposite alpha = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.1f);
			g2d.setComposite(alpha);
			g2d.setColor(iconSelectionColor);
			g2d.fillRoundRect(getLocation().x, getLocation().y, getWidth(),
					getHeight(), 5, 5);
		}

	}

	// GETTERI I SETTERI

	/**
	 * Vraća točku na koju se može spojiti veza. Ta točke će biti početna ili
	 * krajnja točka veze koja ide iz ikone ili u nju.
	 * 
	 * @return početna ili krajnja točka veze
	 */
	public Point getConnectionPoint() {
		if (side == LEFT_SIDE) {
			return new Point(getLocation().x + getWidth(), getLocation().y
					+ getHeight() / 2);
		} else {
			return new Point(getLocation().x, getLocation().y + getHeight() / 2);
		}
	}

	/**
	 * Dohvaća jedinstveni identifikator ikone.
	 * 
	 * @return jedinstveni identifikator
	 */
	public int getId() {
		return id;
	}

	/**
	 * Dohvaća stranu na kojoj se ikona nalazi.
	 */
	public short getSide() {
		return side;
	}

	/**
	 * Provjerava je li ikona označena
	 * 
	 * @return <code>true</code> ako ikona je označena, <code>false</code>
	 *         ako nije
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Mijenja stanje ikone iz selektiranog u deselektirano ili obratno te
	 * obavještava sve slušatelje da je došlo do promjene stanja.
	 */
	private void toggleSelected() {
		selected = !selected;
		this.repaint();
		firePropertyChange("IconSelected", !selected, selected);
	}

	/**
	 * Selektira ikonu, ako ona već nije selektirana. Ako je ikona već
	 * selektirana ne događa se ništa.
	 */
	public void select() {
		if (isSelected()) {
			return;
		} else {
			toggleSelected();
		}
	}

	/**
	 * Deselektira ikonu, ako je ona selektirana. Ako ikona nije selektirana ne
	 * događa se ništa.
	 */
	public void deselect() {
		if (!isSelected()) {
			return;
		} else {
			toggleSelected();
		}
	}

	/**
	 * Povećava broj veza ikone koje se neće iscrtavati. Broj neiscrtanih veza
	 * će se pojaviti pokraj ikone, ako je veći od 0.
	 */
	public void addInvisibleConnection() {
		invisibleConnections++;
	}

	/**
	 * Poništava broj neiscrtanih veza ikone na 0.
	 */
	public void resetInvisibleConnections() {
		invisibleConnections = 0;
	}

	// METODE ZA PROMJENU IZGLEDA IKONE

	/**
	 * Dohvaća sliku koju prikazuju sve instance ove klase.
	 * 
	 * @return slika koja se prikazuje na ikoni
	 */
	public static Image getImage() {
		return image;
	}

	/**
	 * Postavlja sliku koju će prikazivati sve instance ove klase.
	 * 
	 * @param image
	 *            slika koje će se prikazivati na svim ikonama
	 */
	public static void setImage(Image image) {
		Icon.image = image;
	}

	/**
	 * Dohvaća boju kojom se iscrtava područje iza broja nevidljivih veza
	 * 
	 * @return boja kojom se iscrtava područje iza broja nevidljivih veza
	 */
	public static Color getInvisibleCounterBackground() {
		return invisibleCounterBackground;
	}

	/**
	 * Postavlja boju kojom se iscrtava područje iza broja nevidljivih veza
	 * 
	 * @param color
	 *            boja kojom se iscrtava područje iza broja nevidljivih veza
	 */
	public static void setInvisibleCounterBackground(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		invisibleCounterBackground = color;
	}

	/**
	 * Dohvaća boju kojom se iscrtava okvir oko broja nevidljivih veza
	 * 
	 * @return boja kojom se iscrtava okvir oko broja nevidljivih veza
	 */
	public static Color getInvisibleCounterBorder() {
		return invisibleCounterBorder;
	}

	/**
	 * Postavlja boju kojom se iscrtava okvir oko broja nevidljivih veza
	 * 
	 * @param color
	 *            boja kojom se iscrtava okvir oko broja nevidljivih veza
	 */
	public static void setInvisibleCounterBorder(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		invisibleCounterBorder = color;
	}

	/**
	 * Dohvaća boju kojom se iscrtava broj nevidljivih veza
	 * 
	 * @return boja kojom se iscrtava broj nevidljivih veza
	 */
	public static Color getInvisibleCounterForeground() {
		return invisibleCounterForeground;
	}

	/**
	 * Postavlja boju kojom se iscrtava broj nevidljivih veza
	 * 
	 * @param color
	 *            boja kojom se iscrtava broj nevidljivih veza
	 */
	public static void setInvisibleCounterForeground(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		invisibleCounterForeground = color;
	}

	/**
	 * Dohvaća boju čijim nijansama se iscrtava područje ikone kada je označena
	 * ili kada se pokazivač nalazi iznad nje
	 * 
	 * @return boja čijim nijansama se iscrtava područje ikone kada je označena
	 *         ili kada se pokazivač nalazi iznad nje
	 */
	public static Color getIconSelectionColor() {
		return iconSelectionColor;
	}

	/**
	 * Postavlja boju čijim nijansama se iscrtava područje ikone kada je
	 * označena ili kada se pokazivač nalazi iznad nje
	 * 
	 * @param color
	 *            boja čijim nijansama se iscrtava područje ikone kada je
	 *            označena ili kada se pokazivač nalazi iznad nje
	 */
	public static void setIconSelectionColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		iconSelectionColor = color;
	}

	// METODE ZA MOUSE LISTENER

	public void mouseClicked(MouseEvent e) {
		//Ignored
	}

	public void mouseEntered(MouseEvent e) {
		mouseInside = true;
		this.repaint();
		firePropertyChange("MouseEnteredIcon", null, null);
	}

	public void mouseExited(MouseEvent e) {
		mouseInside = false;
		this.repaint();
		firePropertyChange("MouseExitedIcon", null, null);
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			firePropertyChange("MousePressed", null, null);
		}

	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			firePropertyChange("MouseReleased", null, null);
			if (mouseInside) {
				toggleSelected();
			}
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + side;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Icon other = (Icon) obj;
		if (id != other.id)
			return false;
		if (side != other.side)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Icon, id=" + id + ", side=" + side;
	}

}
