package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.gui.windows.WorkingWindow;
import hr.fer.zemris.ktlab.sap.util.DataModel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

/**
 * Klasa <code>Switchboard</code> je pogled na model čija je glavna zadaća da
 * pregledno prikaže veze između elemenata iz modela te da korisniku omogući
 * jednostavno stvaranje novih veza te uklanjanje već postojećih.
 * <p>
 * Elementi iz modela su prikazani kao ikone, a veze su prikazane kao linije
 * između njih. Ikone su podijeljene na ikone lijeve strane i ikone desne
 * strane. Veza se može uspostaviti samo između ikone lijeve strane i ikone
 * desne strane, nije moguće spojiti dvije ili više ikona jedne strane.
 * <p>
 * Klasa dopušta odabir više ikona i više veze, no u bilo kojem trenutku mogu
 * biti odabrane samo veze ili samo ikone koje se nalaze na jednoj strani. Ikone
 * se označavaju sve dok korisnik odabire ikone koje se nalaze na istoj strani.
 * Prvi put kada korisnik odabere ikonu koja nije na istoj strani stvaraju se
 * veze između svih prethodno odabranih ikona i odabrane ikone koja se nalazi na
 * suprotnoj strani.
 * <p>
 * Klasa <code>Switcboard</code> poziva odgovarajuću metodu klase
 * <code>DataModel</code> kod bilo kakve promjene koju napravi korisnik i ne
 * mijenja svoj prikaz. Sve promjene u prikazu nastaju kao rezultat poziva
 * određene metode od strane klase u kojoj se ova klasa nalazi uslijed reakcije
 * na poruku o određenoj promjeni iz modela. To znači da ova klasa ne može
 * djelovati kao samostalni pogled na model, nego samo kao dio nekog drugog
 * pogleda.
 * <p>
 * Za mijenjanje izgleda veza i ikona mogu se koristiti statičke metode tih
 * klasa.
 * 
 * @see Icon
 * @see Connection
 * 
 * @author Željko Rumenjak
 * 
 */
public class Switchboard extends JComponent implements MouseListener,
		MouseMotionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	/** Ikone koje se nalaze na lijevoj strani */
	private List<Icon> leftSideIcons;

	/** Ikone koje se nalaze na desnoj strani */
	private List<Icon> rightSideIcons;

	/** Sve veze između ikona */
	private List<Connection> connections;

	/** Sadrži sve ikone koje su označene */
	private List<Icon> selectedIcons;

	/** Sadrži sve veze koje su označene */
	private List<Connection> selectedConnections;

	/** Model u kojem se nalaze svi korisnički podaci */
	private DataModel model;

	/** Označava da li se treba pojaviti izbornik na desni klik iznad veze */
	private boolean connectionPopupMenu = false;

	/** Ikona od koje je korisnik počeo crtati vezu */
	private Icon dragIcon = null;

	/** Točka do koje je korisnik trenutno povukao vezu */
	private Point dragLineEnd = null;

	/** Ikona iznad koje se nalazi pokazivač miša */
	private Icon mouseOverIcon = null;

	/** Boja kojom se iscrtava veza koju korisnik crta kada spaja dvije ikone */
	private Color dragConnectionColor;

	/**
	 * označava broj piksela za koji je moguće pomaknuti pokazivač od veze, a da
	 * bi ona još mogla detektirati pomak ili klik miša
	 */
	private short selectionTreshold = 5;

	/** Sadrži tekst sučelja na jeziku kojeg je korisnik odabrao */
	private Properties language;

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param model
	 *            model u kojem se nalaze svi korisnički podaci
	 * @param language
	 *            sadrži tekst sučelja
	 */
	public Switchboard(DataModel model, Properties language) {
		super();

		if (model == null) {
			throw new IllegalArgumentException("Model cannot be null");
		}

		if (language == null) {
			throw new IllegalArgumentException("Language cannot be null");
		}

		this.model = model;
		this.language = language;

		dragConnectionColor = Color.BLACK;

		registerListeners();
		leftSideIcons = new LinkedList<Icon>();
		rightSideIcons = new LinkedList<Icon>();
		connections = new LinkedList<Connection>();
		selectedIcons = new LinkedList<Icon>();
		selectedConnections = new LinkedList<Connection>();
		this.addMouseListener(this);
		createPopupMenu();
	}

	/**
	 * Stvara kontekstni izbornik za klasu. Preko izbornika se mogu ukloniti sve
	 * veze, ili samo označene veze.
	 */
	private void createPopupMenu() {
		JPopupMenu sboardPopupMenu = new JPopupMenu();

		Action removeSelected = new RemoveSelectedConnections();

		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("DELETE"), "deleteSelectedConnections");
		getActionMap().put("deleteSelectedConnections", removeSelected);

		sboardPopupMenu.add(removeSelected);
		sboardPopupMenu.add(new RemoveAllConnections());
		this.setComponentPopupMenu(sboardPopupMenu);
	}

	/**
	 * Crta sve vidljive ikone, te veze čije su obje ikone vidljive. Pokraj
	 * ikona čije sve veze nisu nacrtane se pojavljuje broj veza koje nisu
	 * nacrtane.
	 */
	@Override
	public void paintChildren(Graphics g) {
		synchronized (getTreeLock()) {
			for (Icon leftIcon : leftSideIcons) {
				if (leftIcon.isVisible()) {
					leftIcon.resetInvisibleConnections();
				}
			}

			for (Icon rightIcon : rightSideIcons) {
				if (rightIcon.isVisible()) {
					rightIcon.resetInvisibleConnections();
				}
			}

			for (Connection connection : connections) {
				if (connection.leftIcon.isVisible()
						&& connection.rightIcon.isVisible()) {
					connection.paint(g);
				} else if (connection.leftIcon.isVisible()) {
					connection.leftIcon.addInvisibleConnection();
				} else if (connection.rightIcon.isVisible()) {
					connection.rightIcon.addInvisibleConnection();
				}
			}

			for (Icon leftIcon : leftSideIcons) {
				if (leftIcon.isVisible()) {
					leftIcon.paint(g);
				}
			}

			for (Icon rightIcon : rightSideIcons) {
				if (rightIcon.isVisible()) {
					rightIcon.paint(g);
				}
			}

			if (dragIcon != null && dragLineEnd != null) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setColor(dragConnectionColor);
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));

				int x = dragIcon.getConnectionPoint().x;
				int y = dragIcon.getConnectionPoint().y;

				g2d.drawLine(x, y, dragLineEnd.x, dragLineEnd.y);
			}
		}
	}

	/**
	 * Dodaje novu ikonu na lijevu stranu.
	 * 
	 * @param x
	 *            x koordinata ikone
	 * @param y
	 *            y koordinata ikone
	 * @param id
	 *            jedinstveni identifikator ikone
	 * @return stvorena ikona
	 */
	public Icon addLeftSideIcon(int x, int y, int id) {
		Icon icon;

		synchronized (getTreeLock()) {
			icon = new Icon(x, y, Icon.LEFT_SIDE, id);
			icon.addPropertyChangeListener(this);
			icon.addMouseMotionListener(this);

			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(new RemoveLeftIconsConnections(icon));
			icon.setComponentPopupMenu(popupMenu);

			leftSideIcons.add(icon);
		}

		add(icon);
		return icon;
	}

	/**
	 * Stvara novu ikonu lijeve strane i stavlja ju ispred ikone
	 * <code>beforeIcon</code>. Ikona <code>beforeIcon</code> mora
	 * postojati u ovoj klasi i mora biti na lijevoj strani.
	 * 
	 * @param x
	 *            x koordinata ikone
	 * @param y
	 *            y koordinata ikone
	 * @param beforeIcon
	 *            ikona ispred koje se umeće nova ikona
	 * @param id
	 *            jedinstveni identifikator ikone
	 * @return stvorena ikona
	 */
	public Icon insertLeftSideIcon(int x, int y, Icon beforeIcon, int id) {
		Icon icon;

		synchronized (getTreeLock()) {
			int index = leftSideIcons.indexOf(beforeIcon);
			if (index == -1) {
				throw new IllegalArgumentException(
						"Icon after which this icon should be inserted does not exist");
			}

			icon = new Icon(x, y, Icon.LEFT_SIDE, id);
			icon.addPropertyChangeListener(this);
			icon.addMouseMotionListener(this);

			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(new RemoveLeftIconsConnections(icon));
			icon.setComponentPopupMenu(popupMenu);

			leftSideIcons.add(index, icon);
		}

		add(icon);
		return icon;
	}

	/**
	 * Uklanja ikonu sa lijeve strane.
	 * 
	 * @param icon
	 *            ikona koja se uklanja
	 * @return <code>true</code> ako je ikona uklonjena, <code>false</code>
	 *         inače
	 */
	public boolean removeLeftSideIcon(Icon icon) {
		boolean result = false;

		synchronized (getTreeLock()) {
			Iterator<Connection> it = connections.iterator();

			while (it.hasNext()) {
				Connection connection = it.next();
				if (connection.leftIcon.equals(icon)) {
					it.remove();
				}
			}

			selectedIcons.remove(icon);
			result = leftSideIcons.remove(icon);
		}

		remove(icon);
		return result;
	}

	/**
	 * Dodaje novu ikonu na desnu stranu.
	 * 
	 * @param x
	 *            x koordinata ikone
	 * @param y
	 *            y koordinata ikone
	 * @param id
	 *            jedinstveni identifikator ikone
	 * @return stvorena ikona
	 */
	public Icon addRightSideIcon(int x, int y, int id) {

		Icon icon;
		synchronized (getTreeLock()) {
			icon = new Icon(x, y, Icon.RIGHT_SIDE, id);
			icon.addPropertyChangeListener(this);
			icon.addMouseMotionListener(this);

			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(new RemoveRightIconsConnections(icon));
			icon.setComponentPopupMenu(popupMenu);

			rightSideIcons.add(icon);
		}
		add(icon);

		return icon;
	}

	/**
	 * Stvara novu ikonu desne strane i stavlja ju ispred ikone
	 * <code>beforeIcon</code>. Ikona <code>beforeIcon</code> mora
	 * postojati u ovoj klasi i mora biti na desnoj strani.
	 * 
	 * @param x
	 *            x koordinata ikone
	 * @param y
	 *            y koordinata ikone
	 * @param beforeIcon
	 *            ikona ispred koje se umeće nova ikona
	 * @param id
	 *            jedinstveni identifikator ikone
	 * @return stvorena ikona
	 */
	public Icon insertRightSideIcon(int x, int y, Icon beforeIcon, int id) {
		Icon icon;

		synchronized (getTreeLock()) {
			int index = rightSideIcons.indexOf(beforeIcon);
			if (index == -1) {
				throw new IllegalArgumentException(
						"Icon after which this icon should be inserted does not exist");
			}

			icon = new Icon(x, y, Icon.RIGHT_SIDE, id);
			icon.addPropertyChangeListener(this);
			icon.addMouseMotionListener(this);

			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(new RemoveRightIconsConnections(icon));
			icon.setComponentPopupMenu(popupMenu);

			rightSideIcons.add(index, icon);
		}

		add(icon);
		return icon;
	}

	/**
	 * Uklanja ikonu sa desne strane.
	 * 
	 * @param icon
	 *            ikona koja se uklanja
	 * @return <code>true</code> ako je ikona uklonjena, <code>false</code>
	 *         inače
	 */
	public boolean removeRightSideIcon(Icon icon) {
		boolean result;

		synchronized (getTreeLock()) {
			Iterator<Connection> it = connections.iterator();

			while (it.hasNext()) {
				Connection connection = it.next();
				if (connection.rightIcon.equals(icon)) {
					it.remove();
				}
			}
			selectedIcons.remove(icon);
			result = rightSideIcons.remove(icon);
		}

		remove(icon);
		return result;
	}

	/**
	 * Dodaje novu vezu koja spaja lijevu i desnu ikonu.
	 * 
	 * @param leftIcon
	 *            lijeva ikona veze
	 * @param rightIcon
	 *            desna ikona veze
	 */
	public void addConnection(Icon leftIcon, Icon rightIcon) {
		Connection connection = new Connection(leftIcon, rightIcon,
				selectionTreshold);
		addConnection(connection);
	}

	/**
	 * Dodaje novu vezu.
	 * 
	 * @param connection
	 *            veza koja se dodaje
	 */
	public void addConnection(Connection connection) {

		synchronized (getTreeLock()) {
			if (connection == null) {
				throw new IllegalArgumentException("Connection cannot be null");
			}

			connection.addPropertyChangeListener(this);

			if (connectionPopupMenu) {
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(new RemoveConnection(connection));
				connection.setComponentPopupMenu(popupMenu);
			}
			connections.add(connection);
		}

		add(connection);
	}

	/**
	 * Uklanja vezu koja spaja ikone koje se predaju kao parametri. Ako veza ne
	 * postoji, ništa se ne događa. Niti jedna ikona ne smije biti
	 * <code>null</code>.
	 * 
	 * @param leftIcon
	 *            lijeva ikona veze
	 * @param rightIcon
	 *            desna ikona veze
	 * @return <code>true</code> ako je veza uklonjena, <code>false</code>
	 *         inače
	 */
	public boolean removeConnection(Icon leftIcon, Icon rightIcon) {
		Connection connection = new Connection(leftIcon, rightIcon,
				selectionTreshold);
		return removeConnection(connection);
	}

	/**
	 * Uklanja vezu. Ako veza ne postoji ništa se ne događa. Veza ne smije biti
	 * <code>null</code>.
	 * 
	 * @param connection
	 *            veza koja se uklanja
	 * @return <code>true</code> ako je veza uklonjena, <code>false</code>
	 *         inače
	 */
	public boolean removeConnection(Connection connection) {
		boolean result;

		synchronized (getTreeLock()) {
			if (connection == null) {
				throw new IllegalArgumentException("Connection cannot be null");
			}
			connection = connections.get(connections.indexOf(connection));
			selectedConnections.remove(connection);
			result = connections.remove(connection);
		}

		remove(connection);
		return result;
	}

	/**
	 * Dodaje vezu u model, veza se ne dodaje u <code>Switchboard</code>,
	 * nego samo u model.
	 * 
	 * @param connection
	 *            veza koja se dodaje u model.
	 */
	private void addConnectionModel(Connection connection) {
		model.addConnection(connection.leftIcon.getId(), connection.rightIcon
				.getId());
	}

	/**
	 * Uklanja vezu iz modela, veza se ne uklanja iz klase
	 * <code>Switchboard</code>, nego samo iz modele.
	 * 
	 * @param connection
	 *            veza koja se uklanja iz modela.
	 */
	private void removeConnectionModel(Connection connection) {
		model.removeConnection(connection.leftIcon.getId(),
				connection.rightIcon.getId());
	}

	/**
	 * Deselektira sve selektirane ikone.
	 */
	private void clearSelectedIcons() {
		List<Icon> tempIcons = new ArrayList<Icon>(selectedIcons.size());
		tempIcons.addAll(selectedIcons);

		for (Icon icon : tempIcons) {
			icon.deselect();
		}
	}

	/**
	 * Deselektira sve selektirane veze.
	 */
	private void clearSelectedConnections() {
		List<Connection> tempConnections = new ArrayList<Connection>(
				selectedConnections.size());

		tempConnections.addAll(selectedConnections);

		for (Connection connection : tempConnections) {
			connection.deselect();
		}
	}

	/**
	 * Uklanja sve veze iz kolekcije koja se predaje metodi. Ako će se kao
	 * rezultat poziva ove metode ukloniti više od jedne veze pojavljuje se
	 * poruka koja upozorava korisnika koliko veza će se ukloniti i on mora
	 * potvrditi da želi ukloniti te veze. Sve akcije u modelu koje rezultiraju
	 * pozivom ove metode se grupiraju u jednu akciju (<code>compoundAction</code>)
	 * kako bi se mogle vratiti jednim pozivom <code>undo()</code> metode.
	 * 
	 * @param connectionsToRemove
	 *            veze koje se uklanjaju
	 */
	public void removeConnections(final Collection<Connection> connectionsToRemove) {
		int result = 0;
		if (connectionsToRemove.size() > 1) {
			String[] options = { language.getProperty("buttonYes"),
					language.getProperty("buttonNo") };
			result = JOptionPane.showOptionDialog(null, language
					.getProperty("messageConfDelConnText")
					+ connectionsToRemove.size(), language
					.getProperty("msgRemoveConfDelConnTitle"),
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);
		}

		if (result != 0) {
			return;
		}

		SwingWorker<Void, Void> removeWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				model.startCompoundAction();
				for (Connection connection : connectionsToRemove) {
					removeConnectionModel(connection);
				}
				model.stopCompoudAction();
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};

		new WorkingWindow(null, removeWorker);

	}

	/**
	 * Uklanja sve selektirane veze. Ako će se kao rezultat poziva ove metode
	 * ukloniti više od jedne veze pojavljuje se poruka koja upozorava korisnika
	 * koliko veza će se ukloniti i on mora potvrditi da želi ukloniti te veze.
	 * Sve akcije u modelu koje rezultiraju pozivom ove metode se grupiraju u
	 * jednu akciju (<code>compoundAction</code>) kako bi se mogle vratiti
	 * jednim pozivom <code>undo()</code> metode.
	 */
	public void removeSelectedConnections() {
		Set<Connection> connectionsToRemove = new HashSet<Connection>();
		connectionsToRemove.addAll(selectedConnections);
		removeConnections(connectionsToRemove);
	}

	/**
	 * Uklanja sve veze. Ako će se kao rezultat poziva ove metode ukloniti više
	 * od jedne veze pojavljuje se poruka koja upozorava korisnika koliko veza
	 * će se ukloniti i on mora potvrditi da želi ukloniti te veze. Sve akcije u
	 * modelu koje rezultiraju pozivom ove metode se grupiraju u jednu akciju
	 * kako bi se mogle vratiti jednim pozivom <code>undo()</code> metode.
	 */
	public void removeAllConnections() {
		Set<Connection> connectionsToRemove = new HashSet<Connection>();
		connectionsToRemove.addAll(connections);
		removeConnections(connectionsToRemove);
	}

	/**
	 * Deselektira sve selektirane ikone i sve selektirane veze.
	 */
	public void clearSelection() {
		clearSelectedIcons();
		clearSelectedConnections();
	}

	/**
	 * Pomiče svaku vezu selektiranih ikona za jednu vezu prema gore, tj. na
	 * prethodnu ikonu strane na kojoj su ikone selektirane. Ako takva ikona ne
	 * postoji veze koje bi joj se trebale pridružiti se brišu. Sve akcije u
	 * modelu koje rezultiraju pozivom ove metode se grupiraju u jednu akciju
	 * kako bi se mogle vratiti jednim pozivom <code>undo()</code> metode.
	 */
	public void shiftSelectedConnectionsUp() {
		Set<Icon> newSelection = new HashSet<Icon>();
		Set<Connection> connectionsToRemove = new HashSet<Connection>();
		Set<Connection> connectionsToAdd = new HashSet<Connection>();

		if (selectedIcons.isEmpty()) {
			return;
		}

		if (selectedIcons.get(0).getSide() == Icon.LEFT_SIDE) {
			for (Connection connection : connections) {
				if (selectedIcons.contains(connection.leftIcon)) {
					int index = leftSideIcons.indexOf(connection.leftIcon);
					if (index > 0) {
						Connection newConnection = new Connection(leftSideIcons
								.get(index - 1), connection.rightIcon,
								selectionTreshold);
						connectionsToAdd.add(newConnection);
						newSelection.add(newConnection.leftIcon);
					}
					connectionsToRemove.add(connection);
				}
			}
		} else {
			for (Connection connection : connections) {
				if (selectedIcons.contains(connection.rightIcon)) {
					int index = rightSideIcons.indexOf(connection.rightIcon);
					if (index > 0) {
						Connection newConnection = new Connection(
								connection.leftIcon, rightSideIcons
										.get(index - 1), selectionTreshold);
						connectionsToAdd.add(newConnection);
						newSelection.add(newConnection.rightIcon);
					}
					connectionsToRemove.add(connection);

				}
			}
		}
		model.startCompoundAction();
		for (Connection connection : connectionsToRemove) {
			removeConnectionModel(connection);
		}

		for (Connection connection : connectionsToAdd) {
			addConnectionModel(connection);
		}
		model.stopCompoudAction();

		clearSelectedIcons();
		for (Icon icon : newSelection) {
			icon.select();
		}
	}

	/**
	 * Pomiče svaku vezu selektiranih ikona za jednu vezu prema dolje, tj. na
	 * sljedeću ikonu strane na kojoj su ikone selektirane. Ako takva ikona ne
	 * postoji veze koje bi joj se trebale pridružiti se brišu. Sve akcije u
	 * modelu koje rezultiraju pozivom ove metode se grupiraju u jednu akciju
	 * kako bi se mogle vratiti jednim pozivom <code>undo()</code> metode.
	 */
	public void shiftSelectedConnectionsDown() {
		Set<Icon> newSelection = new HashSet<Icon>();
		Set<Connection> connectionsToRemove = new HashSet<Connection>();
		Set<Connection> connectionsToAdd = new HashSet<Connection>();

		if (selectedIcons.isEmpty()) {
			return;
		}

		if (selectedIcons.get(0).getSide() == Icon.LEFT_SIDE) {
			for (Connection connection : connections) {
				if (selectedIcons.contains(connection.leftIcon)) {
					int index = leftSideIcons.indexOf(connection.leftIcon);
					if (index < leftSideIcons.size() - 1) {
						Connection newConnection = new Connection(leftSideIcons
								.get(index + 1), connection.rightIcon,
								selectionTreshold);
						connectionsToAdd.add(newConnection);
						newSelection.add(newConnection.leftIcon);
					}
					connectionsToRemove.add(connection);
				}
			}
		} else {
			for (Connection connection : connections) {
				if (selectedIcons.contains(connection.rightIcon)) {
					int index = rightSideIcons.indexOf(connection.rightIcon);
					if (index < rightSideIcons.size() - 1) {
						Connection newConnection = new Connection(
								connection.leftIcon, rightSideIcons
										.get(index + 1), selectionTreshold);
						connectionsToAdd.add(newConnection);
						newSelection.add(newConnection.rightIcon);
					}
					connectionsToRemove.add(connection);
				}
			}
		}

		model.startCompoundAction();
		for (Connection connection : connectionsToRemove) {
			removeConnectionModel(connection);
		}

		for (Connection connection : connectionsToAdd) {
			addConnectionModel(connection);
		}
		model.stopCompoudAction();

		clearSelectedIcons();
		for (Icon icon : newSelection) {
			icon.select();
		}
	}

	public void mouseClicked(MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON1) {
			clearSelection();
		}
	}

	public void mouseEntered(MouseEvent evt) {
		// Ignored
	}

	public void mouseExited(MouseEvent evt) {
		// Ignored
	}

	public void mousePressed(MouseEvent evt) {
		// Ignored
	}

	public void mouseReleased(MouseEvent evt) {
		// Ignored
	}

	public void mouseDragged(MouseEvent e) {
		if (dragIcon == null) {
			return;
		}
		dragLineEnd = e.getPoint();
		if (e.getSource() instanceof Icon) {
			dragLineEnd.translate(e.getComponent().getX(), e.getComponent()
					.getY());
		}

		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		// Ignored
	}

	/**
	 * Dodaje novu ikonu u listu selektiranih ikona.
	 * <p>
	 * Ako lista selektiranih ikona nije prazna nova ikona se dodaje samo ako se
	 * nalazi na istoj strani na kojoj se nalaze i ikone u listi, ako se ne
	 * nalazi na istoj strani između svih ikona u listi i te ikone stvaraju se
	 * nove veze.
	 * <p>
	 * Prije dodavanja nove ikone u listu selektiranih ikona provjerava se da li
	 * je lista selektiranih veza prazna. Ako lista selektiranih veza nije
	 * prazna, prvo se sve selektirane veze deselektiraju, a tek onda se dodaje
	 * nova ikona u listu selektiranih ikona.
	 * 
	 * @param icon
	 *            ikona koja se dodaje u listu selektiranih ikona
	 */
	private void selectIcon(Icon icon) {
		if (!selectedConnections.isEmpty()) {
			clearSelectedConnections();
		}
		if (selectedIcons.isEmpty()) {
			synchronized (getTreeLock()) {
				selectedIcons.add(icon);
			}
		} else {
			if (selectedIcons.get(0).getSide() == icon.getSide()) {
				synchronized (getTreeLock()) {
					selectedIcons.add(icon);
				}
			} else {
				updateConnections(icon);
			}
		}

	}

	/**
	 * Uklanja ikonu iz liste selektiranih ikona. Ako se ikona ne nalazi u
	 * listi, ništa se ne događa.
	 * 
	 * @param icon
	 *            ikona koja se uklanja iz liste selektiranih ikona
	 */
	private void deselectIcon(Icon icon) {
		synchronized (getTreeLock()) {
			selectedIcons.remove(icon);
		}
	}

	/**
	 * Dodaje vezu u listu selektiranih veza.
	 * <p>
	 * Prije dodavanja veze u listu selektiranih veza, provjerava se da li je
	 * lista selektiranih ikona prazna. Ako lista selektiranih ikona nije
	 * prazna, sve selektirane ikone se deselektiraju, a tek onda se dodaje veza
	 * u listu selektiranih veza.
	 * 
	 * @param connection
	 *            veza koja se dodaje u listu selektiranih veza
	 */
	private void selectConnection(Connection connection) {
		if (!selectedIcons.isEmpty()) {
			clearSelectedIcons();
		}

		synchronized (getTreeLock()) {
			selectedConnections.add(connection);
		}
	}

	private void deselectConnection(Connection connection) {
		synchronized (getTreeLock()) {
			selectedConnections.remove(connection);
		}
	}

	/**
	 * Metoda stvara nove veze ili uklanja već postojeće između ikone koja joj
	 * se predaje kao parametar i svih ikona iz liste selektiranih ikona.
	 * <p>
	 * Ako veza između ikone koja se predaje kao parametar i neke od
	 * selektiranih ikona ne postoji, tada se ona stvara, no ako veza između tih
	 * ikona već postoji, ona se uklanja.
	 * 
	 * @param icon
	 *            ikona između koje i selektiranih ikona se stvaraju ili
	 *            uklanjaju veze
	 */
	private void updateConnections(Icon icon) {
		Connection connection = null;

		model.startCompoundAction();
		if (icon.getSide() == Icon.LEFT_SIDE) {
			for (Icon selectedIcon : selectedIcons) {
				connection = new Connection(icon, selectedIcon,
						selectionTreshold);
				if (connections.contains(connection)) {
					removeConnectionModel(connection);
				} else {
					addConnectionModel(connection);
				}
			}
		} else {
			for (Icon selectedIcon : selectedIcons) {
				connection = new Connection(selectedIcon, icon,
						selectionTreshold);
				if (connections.contains(connection)) {
					removeConnectionModel(connection);
				} else {
					addConnectionModel(connection);
				}
			}
		}
		model.stopCompoudAction();

		clearSelectedIcons();
		icon.deselect();
	}

	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getSource() instanceof Icon) {
			processIconPropertyChange(evt);
			
		} else if (evt.getSource() instanceof Connection) {
			if (evt.getPropertyName().equals("ConnectionSelected")) {
				if ((Boolean) evt.getNewValue()) {
					selectConnection((Connection) evt.getSource());
				} else {
					deselectConnection((Connection) evt.getSource());
				}
			} else if (evt.getPropertyName().equals("DeleteConnection")) {
				removeConnectionModel((Connection) evt.getSource());
			}
		} else if (evt.getSource() == model
				&& evt.getPropertyName().equals(DataModel.GLOBAL_CLEAR)) {
			clear();
		}

	}

	/**
	 * Obrađuje događaj koji je stigao od instance klase <code>Icon</code>.
	 * 
	 * @param evt
	 *            događaj koji je stigao od ikone
	 */
	private void processIconPropertyChange(PropertyChangeEvent evt) {
		Icon icon = (Icon) evt.getSource();

		if (evt.getPropertyName().equals("IconSelected")) {
			if ((Boolean) evt.getNewValue()) {
				selectIcon(icon);
			} else {
				deselectIcon(icon);
			}
		} else if (evt.getPropertyName().equals("MousePressed")) {
			dragIcon = icon;
			dragLineEnd = null;
		} else if (evt.getPropertyName().equals("MouseReleased")) {

			if (dragIcon != null && mouseOverIcon != null) {
				if (dragIcon.getSide() == Icon.LEFT_SIDE
						&& mouseOverIcon.getSide() == Icon.RIGHT_SIDE) {

					Connection connection = new Connection(dragIcon,
							mouseOverIcon, selectionTreshold);

					if (connections.contains(connection)) {
						removeConnectionModel(connection);
					} else {
						addConnectionModel(connection);
					}
				} else if (dragIcon.getSide() == Icon.RIGHT_SIDE
						&& mouseOverIcon.getSide() == Icon.LEFT_SIDE) {

					Connection connection = new Connection(mouseOverIcon,
							dragIcon, selectionTreshold);

					if (connections.contains(connection)) {
						removeConnectionModel(connection);
					} else {
						addConnectionModel(connection);
					}

				}
			}

			clearDragAndDrop();

		} else if (evt.getPropertyName().equals("MouseEnteredIcon")) {
			mouseOverIcon = icon;
		} else if (evt.getPropertyName().equals("MouseExitedIcon")) {
			if (icon == mouseOverIcon) {
				mouseOverIcon = null;
			}
		}
	}

	/**
	 * Poništava sve varijable koje se koriste kada korisnik ručno crta vezu
	 * između dvije ikone i ponovno iscrtava područje koje se promijenilo.
	 */
	private void clearDragAndDrop() {
		
		if (dragIcon != null && dragLineEnd != null) {

			int x = Math.min(dragIcon.getConnectionPoint().x, dragLineEnd.x);
			int y = Math.min(dragIcon.getConnectionPoint().y, dragLineEnd.y);
			int width = Math.abs(dragIcon.getConnectionPoint().x
					- dragLineEnd.x);
			int height = Math.abs(dragIcon.getConnectionPoint().y
					- dragLineEnd.y);

			repaint(x - 1, y - 1, width + 2, height + 2);
		}

		dragIcon = null;
		dragLineEnd = null;
	}

	/**
	 * Uklanja sve podatke iz ove klase. Nakon što se ova metoda pozove klasa će
	 * biti prazna.
	 */
	public void clear() {
		leftSideIcons.clear();
		rightSideIcons.clear();
		connections.clear();
		selectedIcons.clear();
		removeAll();
		this.repaint();
	}

	/**
	 * Registrira sve slušatelje koji se koriste u ovoj klasi.
	 */
	public void registerListeners() {
		model.addPropertyChangeListener(this);
	}

	// GETTERI I SETTERI

	/**
	 * Vraća da li se prikazuje kontekstni izbornik na desni klik iznad veza.
	 * 
	 * @return <code>true</code> ako se izbornik prikazuje, <code>false</code>
	 *         ako se ne prikazuje
	 */
	public boolean connectionPopupMenu() {
		return connectionPopupMenu;
	}

	/**
	 * Uključuje ili isključuje kontekstni izbornik nad vezama.
	 * 
	 * @param enabled
	 *            određuje da li će se kontekstni izbornik iznad veza
	 *            prikazivati (ako je <code>true</code>) ili ne (ako je
	 *            <code>false</code>)
	 */
	public void setConnectionPopupMenu(boolean enabled) {
		if (connectionPopupMenu != enabled) {
			for (Connection connection : connections) {
				if (enabled) {
					JPopupMenu popupMenu = new JPopupMenu();
					popupMenu.add(new RemoveConnection(connection));
					connection.setComponentPopupMenu(popupMenu);
				} else {
					connection.setComponentPopupMenu(null);
				}
			}
			this.connectionPopupMenu = enabled;
		}
	}

	/**
	 * Vraća broj koji definira područje unutar kojeg veze detektiraju klik i
	 * micanje pokazivača miša.
	 * 
	 * @return broj piksela za koji je moguće pomaknuti pokazivač od veze, a da
	 *         bi ona još mogla detektirati pomak ili klik miša
	 */
	public short getSelectionTreshold() {
		return selectionTreshold;
	}

	/**
	 * Postavlja broj koji definira područje unutar kojeg veze detektiraju klik
	 * i micanje pokazivača miša.
	 * 
	 * @param threshold
	 *            označava broj piksela za koji je moguće pomaknuti pokazivač od
	 *            veze, a da bi ona još mogla detektirati pomak ili klik miša
	 */
	public void setSelectionTreshold(short selectionTreshold) {
		for (Connection connection : connections) {
			connection.setSelectionThreshold(selectionTreshold);
		}
		this.selectionTreshold = selectionTreshold;
	}

	/**
	 * Dohvaća boju kojom se iscrtava veza koju korisnik crta kada spaja dvije
	 * ikone.
	 * 
	 * @return boja kojom se iscrtava veza koju korisnik crta kada spaja dvije
	 *         ikone.
	 */
	public Color getDragConnectionColor() {
		return dragConnectionColor;
	}

	/**
	 * Postavlja boju kojom se iscrtava veza koju korisnik crta kada spaja dvije
	 * ikone.
	 * 
	 * @param color
	 *            boja kojom se iscrtava veza koju korisnik crta kada spaja
	 *            dvije ikone.
	 */
	public void setDragConnectionColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}

		dragConnectionColor = color;
	}

	// AKCIJE ZA KONTEKSTNE IZBORNIKE

	/**
	 * Privatna klasa koja implementira akciju koja se izvršava kada korisnik
	 * odabere opciju za uklanjanje selektiranih veza u kontekstnom izborniku
	 * klase <code>Switchboard</code>.
	 */
	private class RemoveSelectedConnections extends AbstractAction {

		private static final long serialVersionUID = -5342062471535320979L;

		public RemoveSelectedConnections() {
			super(language.getProperty("popupRemoveSelectedConn"));
		}

		public void actionPerformed(ActionEvent e) {
			removeSelectedConnections();
		}

	}

	/**
	 * Privatna klasa koja implementira akciju koja se izvršava kada korisnik
	 * odabere opciju za uklanjanje svih veza u kontekstnom izborniku klase
	 * <code>Switchboard</code>.
	 */
	private class RemoveAllConnections extends AbstractAction {

		private static final long serialVersionUID = 8749685928214021374L;

		public RemoveAllConnections() {
			super(language.getProperty("popupRemoveAllConn"));
		}

		public void actionPerformed(ActionEvent e) {
			removeAllConnections();
		}

	}

	/**
	 * Privatna klasa koja implementira akciju koja se izvršava kada korisnik
	 * odabere opciju za uklanjanje veze u kontekstnom izborniku klase
	 * <code>Connection/code>.
	 */
	private class RemoveConnection extends AbstractAction {

		private static final long serialVersionUID = -5258903217787046947L;

		private Connection connection;

		public RemoveConnection(Connection connection) {
			super(language.getProperty("popupRemoveConnection"));
			this.connection = connection;
		}

		public void actionPerformed(ActionEvent evt) {
			removeConnectionModel(connection);
		}

	}

	/**
	 * Privatna klasa koja implementira akciju koja se izvršava kada korisnik
	 * odabere opciju za uklanjanje svih veza ikone lijeve strane u kontekstnom
	 * izborniku klase <code>Icon</code>.
	 */
	private class RemoveLeftIconsConnections extends AbstractAction {

		private static final long serialVersionUID = -7086730151097136923L;

		private Icon icon;

		public RemoveLeftIconsConnections(Icon icon) {
			super(language.getProperty("popupRemoveIconsConn"));
			this.icon = icon;
		}

		public void actionPerformed(ActionEvent e) {
			Set<Connection> connectionsToRemove = new HashSet<Connection>();

			for (Connection connection : connections) {
				if (connection.leftIcon.equals(icon)) {
					connectionsToRemove.add(connection);
				}
			}

			removeConnections(connectionsToRemove);
		}
	}

	/**
	 * Privatna klasa koja implementira akciju koja se izvršava kada korisnik
	 * odabere opciju za uklanjanje svih veza ikone desne strane u kontekstnom
	 * izborniku klase <code>Icon</code>.
	 */
	private class RemoveRightIconsConnections extends AbstractAction {

		private static final long serialVersionUID = 297090881470611956L;

		private Icon icon;

		public RemoveRightIconsConnections(Icon icon) {
			super(language.getProperty("popupRemoveIconsConn"));
			this.icon = icon;
		}

		public void actionPerformed(ActionEvent e) {
			Set<Connection> connectionsToRemove = new HashSet<Connection>();

			for (Connection connection : connections) {
				if (connection.rightIcon.equals(icon)) {
					connectionsToRemove.add(connection);
				}
			}

			removeConnections(connectionsToRemove);
		}
	}
}
