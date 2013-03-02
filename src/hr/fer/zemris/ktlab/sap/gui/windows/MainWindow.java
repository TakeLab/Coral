package hr.fer.zemris.ktlab.sap.gui.windows;

import hr.fer.zemris.ktlab.sap.algorithms.SentenceSegmentation;
import hr.fer.zemris.ktlab.sap.gui.Icon;
import hr.fer.zemris.ktlab.sap.gui.JBookmarksTable;
import hr.fer.zemris.ktlab.sap.gui.JScrollBar2;
import hr.fer.zemris.ktlab.sap.gui.JTable2;
import hr.fer.zemris.ktlab.sap.gui.KeyListener;
import hr.fer.zemris.ktlab.sap.gui.MainLayout;
import hr.fer.zemris.ktlab.sap.gui.MouseButtonListener;
import hr.fer.zemris.ktlab.sap.gui.MyAbstractAction;
import hr.fer.zemris.ktlab.sap.gui.SboardMouseWheelListener;
import hr.fer.zemris.ktlab.sap.gui.Statistics;
import hr.fer.zemris.ktlab.sap.gui.Switchboard;
import hr.fer.zemris.ktlab.sap.gui.TableMouseListener;
import hr.fer.zemris.ktlab.sap.gui.TextAreaEditor;
import hr.fer.zemris.ktlab.sap.gui.TextAreaRenderer;
import hr.fer.zemris.ktlab.sap.gui.Worker;
import hr.fer.zemris.ktlab.sap.io.FileExporter;
import hr.fer.zemris.ktlab.sap.io.SaveLoadProject;
import hr.fer.zemris.ktlab.sap.util.Bookmark;
import hr.fer.zemris.ktlab.sap.util.DataModel;
import hr.fer.zemris.ktlab.sap.util.Sentence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;

import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Klasa <code>MainWindow</code> predstavlja glavni prozor programa. Prozor je
 * podijeljen na 6 glavnih dijelova:
 * <ul>
 * <li>traka sa 5 padajućih izbornika,
 * <li>glavna alatna traka,
 * <li>lijevi i desni dio, od kojih svaki sadrži tablicu u koju se pohranjuju
 * elementi jednog jezika te odgovarajuću alatnu traku,
 * <li>središnji dio u kojemu se nalazi <code>Switchboard</code> komponenta
 * za vizualno stvaranje veza između elemenata i
 * <li>statusna traka na dnu prozora.
 * </ul>
 * <p>
 * Izvorni kôd <code>MainWindow</code> klase je podijeljen u dvije osnovne
 * cjeline:
 * <ul>
 * <li>inicijalizacija grafičkog sučelja i
 * <li>metode koje omogućuju rad u programu.
 * </ul>
 * <p>
 * Većina komponenti grafičkog sučelja su deklarirane kao globalne varijable jer
 * se kontroliraju iz više metoda. Također, veliki broj varijabli ima public
 * prava pristupa jer ih koristi veliki broj drugih klasa, a getteri i setteri
 * nisu pisani jer bi samo stvarali zbrku kod ovako velikog broja varijabli.
 * <p>
 * Inicijalizacija grafičkog sučelja (metoda <code>void initGUI()</code> je
 * podijeljena kroz 6 podmetoda. Svaka za određenu cjelinu prozora. Sučelje je
 * razvijeno kao višejezično; sav tekst sučelja je pohranjen u XML datoteci koja
 * se učitava i koristi pomoću <Properties> klase. Glavna svrha ove klase je
 * vizualna reprezentacija podataka nad kojima se radit i omogućiti osnovnu
 * funkcionalnost programa, dok se podaci pohranjuju pomoću klase
 * <code>DataModel</code>. Za svaku promjenu u sadržaju podataka, podatkovni
 * model obavještava ovu klasu, na što ona reagira odgovarajućom promjenom u
 * prikazu.
 * 
 * @author Igor Šoš
 */
public class MainWindow extends JFrame implements ComponentListener, PropertyChangeListener, UndoableEditListener {

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// Ignore
		}
	}

	// POCETAK DEKLARACIJE VARIJABLI ***********************************

	private static final long serialVersionUID = 1L;

	/** Podatkovni model u koji se pohranjuju svi korisnički podatci. */
	public DataModel dataModel = new DataModel();

	/** Glavni panel na koji se postavlja većina ostalih elemenata sučelja. */
	public JPanel jPanelMain;

	/**
	 * Panel koji se pojavljuje nakon pritiska na tipku Show Bookmarks. Sadrži
	 * tablicu s knjižnim oznakama.
	 */
	public JPanel jPanelBookmarks;

	/** Scroll pane od lijeve tablice. */
	public JScrollPane jScrollPane1 = new JScrollPane();

	/** Scroll pane od desne tablice. */
	public JScrollPane jScrollPane2 = new JScrollPane();

	/** Gumb za ručno dijeljenje lijevih elemenata. */
	private JButton jBtnLeftSplit;

	/** Glavna alatna traka. */
	private JToolBar jToolBar1;

	/** Gumb za kreiranje novog projekta. */
	private JButton jBtnNewProject;

	/** Gumb za otvaranje projekta. */
	private JButton jBtnOpenProject;

	/** Desni next gumb. */
	private JButton jBtnRightNext;

	/** Lijevi gumb za automatsku segmentaciju na rečenice. */
	private JButton jButton14;

	/** Desni gumb za automatsku segmentaciju na rečenice. */
	private JButton jButton18;

	/** Gumb za spremanje projekta na glavnoj alatnoj traci. */
	private JButton jBtnSaveProject;

	/** Gumb za redo akcije. Nalazi se na glavnoj alatnoj traci. */
	public JButton jBtnRedo;

	/** Gumb za osvježavanje grafičkog sučelja. */
	public JButton jBtnRefresh;

	/** Save As gumb. Nalazi se na glavnoj alatnoj traci. */
	private JButton jButton17;

	/**
	 * Podijeljeni panel koji se koristi za podijelu prozora na dva dijela kod
	 * prikazivanja knjižnih oznaka.
	 */
	public JSplitPane jSplitPane1;

	/** Tablica u kojoj se prikazuju knjižne oznake. */
	public JTable jTableBookmarks;

	/** Tablica koja se koristi za prikazivanje desnih elemenata. */
	public JTable2 jTable2;

	/** Tablica koja se koristi za prikazivanje lijevih elemenata. */
	public JTable2 jTable1;

	/** Tablica u kojoj se prikazuju detalji lijevog elementa. */
	public JTable leftDetailsTable;

	/** Tablica u kojoj se prikazuju detalji desnog elementa. */
	public JTable rightDetailsTable;

	/** Model tablice u kojoj se prikazuju detalji lijevog elementa. */
	public DefaultTableModel leftDetailsTableModel;

	/** Model tablice u kojoj se prikazuju detalji desnog elementa. */
	public DefaultTableModel rightDetailsTableModel;

	/** Glavni desni panel. */
	private JPanel jPanelRight;

	/** Središnji panel na kojemu se nalazi <code>Switchboard</code>. */
	public JPanel jPanel3;

	/** Lijevi glavni panel. */
	private JPanel jPanel2;

	/** Podatkovni model prve (lijeve) tablice. */
	public DefaultTableModel jTable1Model;

	/** Podatkovni model tablice s knjižnim oznakama. */
	public DefaultTableModel jTableBookmarksModel;

	/**
	 * Gumb kojime se veze označenih ikona pomiču prema dolje. Nalazi se na
	 * alatnoj traci Switchboarda.
	 */
	private JButton jBtnShiftConDown;

	/** Gumb za lijevo ručno spajanje elemenata. */
	private JButton jBtnLeftMerge;

	/** Panel na dnu prozora na kojemu se nalazi statusna traka. */
	private JPanel jPanelStatus;

	/** Podatkovni model druge (desne) tablice. */
	public DefaultTableModel jTable2Model;

	/** Mapa u kojoj su pohranjene neke, najčešće korištene, akcije. */
	private Map<String, Action> allActions;

	/**
	 * Središnja komponenta koja omogućuje grafičko prikazivanje i crtanje veza
	 * među elementima.
	 */
	public Switchboard sboard;

	/**
	 * Gumb kojime se aktivira zajedničko klizanje (scroll) lijeve i desne
	 * tablice.
	 */
	private JToggleButton jTglBtnScrollTogether;

	/**
	 * Gumb kojime se aktivira samostalno klizanje (scroll) lijeve i desne
	 * tablice.
	 */
	private JToggleButton jTglBtnNormalScroll;

	/** Gumb kojime se aktivira desni panel za pretraživanje. */
	public JToggleButton jTglBtnRightFind;

	/** Desni gumb za spajanje (merge) elemenata. */
	private JButton jBtnMerge;

	/** Desni gumb za ručno razdvajanje (split) elemenata. */
	private JButton jButton4;

	/** Sadrži sve postavke promijenjene od strane korisnika. */
	public static Properties config = new Properties();

	/** Sadrži tekst sučelja na jeziku kojeg je korisnik odabrao. */
	public static Properties language = new Properties();

	/** Mapa u kojoj su pohranjene lijeve ikone prema ID-evima. */
	public Map<Integer, Icon> leftIconsByID = new HashMap<Integer, Icon>();

	/** Mapa u kojoj su pohranjene desne ikone prema ID-evima. */
	public Map<Integer, Icon> rightIconsByID = new HashMap<Integer, Icon>();

	/** Panel na kojemu se nalaze komponente za pretraživanje desne tablice. */
	public JPanel jPanelRightFind;

	/**
	 * Gumb koji se koristi za prikazivanje/uklanjanje lijevog panela za
	 * pretraživanje.
	 */
	public JToggleButton jTglBtnLeftFind;

	/** Lijevi gumb za pronalazak sljedećeg traženog podniza. */
	private JButton jButton8;

	/**
	 * Gumb koji se koristi za prikazivanje/uklanjanje lijevog panela za
	 * automatsku segmentaciju na rečenice.
	 */
	public JToggleButton jTglBtnAutoSplitAllLeft;

	/**
	 * Gumb koji se koristi za prikazivanje/uklanjanje desnog panela za
	 * automatsku segmentaciju na rečenice.
	 */
	public JToggleButton jTglBtnAutoSplitAllRight;

	/**
	 * Gumb koji se koristi za prikazivanje/uklanjanje panela s knjižnim
	 * oznakama.
	 */
	public JToggleButton jTglBtnBookmarks;

	/**
	 * Gumb koji se koristi za prikazivanje/uklanjanje panela s detaljima
	 * lijevih i desnih elemenata.
	 */
	public JToggleButton jTglBtnDetails;

	/** Tekstualno polje za unos teksta kod pretraživanja lijeve tablice. */
	public JTextField jTextField1;

	/** Tekstualno polje za unos teksta kod pretraživanja desne tablice. */
	public JTextField jTextField2;

	/**
	 * Alatna traka na kojoj se nalaze komponente za pretraživanje lijeve
	 * tablice.
	 */
	public JToolBar jToolBar5;

	/**
	 * Alatna traka na kojoj se nalaze komponente za pretraživanje desne
	 * tablice.
	 */
	public JToolBar jToolBar6;

	/**
	 * Alatna traka na kojoj se nalaze komponente za pokretanje automatske
	 * segmentacije na rečenice lijevih elemenata.
	 */
	public JToolBar jToolBar7;

	/**
	 * Alatna traka na kojoj se nalaze komponente za pokretanje automatske
	 * segmentacije na rečenice desnih elemenata.
	 */
	public JToolBar jToolBar8;

	/**
	 * Alatna traka na kojoj se nalaze komponente za prikaz atributa koji su
	 * pridruzeni uz trenutno označeni lijevi element.
	 */
	public JToolBar jToolBar9;

	/**
	 * Alatna traka na kojoj se nalaze komponente za prikaz atributa koji su
	 * pridruzeni uz trenutno označeni desni element.
	 */
	public JToolBar jToolBar10;

	/** Panel na kojemu se nalazi alatna traka za upravljanje lijevim elementima. */
	public JPanel jPanel8;

	/**
	 * Varijabla koja sadrži podatak o postojanju/nepostojanju ikona na
	 * središnjoj komponenti.
	 */
	boolean iconsExist = false;

	/** Labela na kojoj se ispisuje podatak o desnom jeziku. */
	public JLabel langLabel2;

	/** Labela na kojoj se ispisuje podatak o lijevom jeziku. */
	public JLabel langLabel1;

	/** Labela na kojoj se ispisuje podatak o desno učitanoj datoteci. */
	public JLabel fileLabel2;

	/** Labela na kojoj se ispisuje podatak o lijevo učitanoj datoteci. */
	public JLabel fileLabel1;

	/** Opcija za spremanje u padajućem izborniku. */
	private JMenuItem mItemSave;

	/**
	 * Opcija za spremanje uz specifikaciju imena datoteke u padajućem
	 * izborniku.
	 */
	private JMenuItem mItemSaveAs;

	/** Podizbornik File izbornika. Sadrži opcije za export podataka. */
	private JMenu menuExport;

	/**
	 * Desni panel na kojemu se nalaze labele s podatkom o jeziku i imenom
	 * datoteke.
	 */
	private JPanel jPanel7;

	/**
	 * Lijevi panel na kojemu se nalaze labele s podatkom o jeziku i imenom
	 * datoteke.
	 */
	private JPanel jPanel5;

	/**
	 * Alatna traka na kojoj se nalaze gumbi za pokretanje akcija nad desnom
	 * tablicom.
	 */
	private JToolBar jToolBar4;

	/**
	 * Alatna traka na kojoj se nalazi gumbi za pokretanje akcija nad središnjom
	 * komponentom.
	 */
	private JToolBar jToolBar3;

	/**
	 * Alatna traka na kojoj se nalaze gumbi za pokretanje akcija nad lijevom
	 * tablicom.
	 */
	private JToolBar jToolBar2;

	/** Gumb pomoću kojega se veze svih označenih ikona pomiču prema gore. */
	private JButton jButton7;

	/** Gumb za uklanjanje svih veza između ikona središnje komponente. */
	private JButton jButton9;

	/** Labela na kojoj se ispisuje trenutni status programa. */
	private JLabel statusLabel;

	/** Lijevi padajući izbornik s listom datoteka s kraticama. */
	public JComboBox jComboBox1;

	/** Desni padajući izbornik s listom datoteka s kraticama. */
	public JComboBox jComboBox2;

	/** Gumb za undo akcije. Nalazi se na glavnoj alatnoj traci. */
	public JButton jBtnUndo;

	/** Undo opcija Edit izbornika. */
	public JMenuItem mItemUndo;

	/** Redo opcija Edit izbornika. */
	public JMenuItem mItemRedo;

	/**
	 * Mapa u kojoj su pohranjeni indeksi lijevih redaka prema IDu elementa koji
	 * se u njemu nalazi.
	 */
	public Map<Integer, Integer> leftRowWithId = new HashMap<Integer, Integer>();

	/**
	 * Mapa u kojoj su pohranjeni indeksi desnih redaka prema IDu elementa koji
	 * se u njemu nalazi.
	 */
	public Map<Integer, Integer> rightRowWithId = new HashMap<Integer, Integer>();

	/** Sadrži širinu središnje komponente. */
	private final int switchBoardWidth = 200;

	/** KeyListener koji reagira na unos teksta kod pretraživanja lijeve tablice. */
	public KeyListener keyListener;

	/** KeyListener koji reagira na unos teksta kod pretraživanja desne tablice. */
	public KeyListener keyListener2;

	/**
	 * Padajući izbornik koji se aktivira na pritisak desne tipke miša nad
	 * lijevom tablicom ako odabrani element nema pridružen bookmark.
	 */
	public JPopupMenu popupMenuLeft1;

	/**
	 * Padajući izbornik koji se aktivira na pritisak desne tipke miša nad
	 * lijevom tablicom ako odabrani element ima pridružen bookmark.
	 */
	public JPopupMenu popupMenuLeft2;

	/**
	 * Padajući izbornik koji se aktivira na pritisak desne tipke miša nad
	 * desnom tablicom ako odabrani element nema pridružen bookmark.
	 */
	public JPopupMenu popupMenuRight1;

	/**
	 * Padajući izbornik koji se aktivira na pritisak desne tipke miša nad
	 * desnom tablicom ako odabrani element ima pridružen bookmark.
	 */
	public JPopupMenu popupMenuRight2;

	/** Definira oblik i veličinu bordera na gumbima. */
	public Border buttonBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	/** Padajući izbornik File. */
	private JMenu menuFile;

	/** Padajući izbornik Edit. */
	private JMenu menuEdit;

	/** Padajući izbornik Project. */
	private JMenu menuProject;
	// private Object monitor = new Object();

	/**
	 * Polje koje sadrži listu kratica koje su iznimka kod segmentacije na
	 * rečenice.
	 */
	public String[] acronymsFilesList;

	/**
	 * Skup u kojemu su sadržani indeksi redaka lijevih elemenata koji pri
	 * segmentaciji možda nisu dobro podijeljeni.
	 */
	public Set<Integer> leftNotSure = new HashSet<Integer>();

	/**
	 * Skup u kojemu su sadržani indeksi redaka desnih elemenata koji pri
	 * segmentaciji možda nisu dobro podijeljeni.
	 */
	public Set<Integer> rightNotSure = new HashSet<Integer>();

	/**
	 * Skup u kojemu su sadržani indeksi lijevih redaka koji sadrže knjižnu
	 * oznaku.
	 */
	public Set<Integer> rowsWithBookmarksLeft = new HashSet<Integer>();

	/**
	 * Skup u kojemu su sadržani indeksi desnih redaka koji sadrže knjižnu
	 * oznaku.
	 */
	public Set<Integer> rowsWithBookmarksRight = new HashSet<Integer>();

	/** Mapa u kojoj su pohranjene knjižne oznake prema ID-u elemenata. */
	public Map<Integer, Bookmark> bookmarksById = new HashMap<Integer, Bookmark>();

	/** Listener koji reagira na pritiske tipaka miša. */
	public MouseButtonListener mouseButtonListener = new MouseButtonListener(this);

	/** Objekt koji sadrži metode za exportanje podataka. */
	public FileExporter fileExporter = new FileExporter(dataModel);
	// public String[] languages = new String[] { "en-US", "hr" };
	// public String[] encodings = new String[] { "UTF-8", "Cp1250", "ISO8859_2"
	// };

	/** Polje koje sadrži listu jezika ponuđenih pri otvaranju novog projekta. */
	public String[] languages;

	/** Polje koje sadrži listu encodinga ponuđenih pri otvaranju novog projekta. */
	public String[] encodings;

	/**
	 * Varijabla koja sadrži true ako su napravljene neke promjene u projektu
	 * nakon posljednjeg spremanja.
	 */
	public boolean askForSave;

	/** Posljednja lokacija spremanja. */
	public String lastSaveLocation = null;

	/**
	 * Objekt pomoću kojega se podiže nova dretva unutar koje se obavlja neki
	 * dugotrajan posao.
	 */
	public Worker worker = new Worker(this);

	/** Objekt koji omogućuje praćenje akcija te njihovo brisanje. */
	public UndoManager undoManager = new UndoManager();

	/** Statistika rada na projektu. */
	public Statistics stats = new Statistics();;

	/** Skup koji sadrži lijeve ikone koje su trenutno označene. */
	public Set<Icon> selectedIconsLeft = new HashSet<Icon>();

	/**
	 * Opcija u padajućem izborniku lijeve tablice pomoću koje se neka ćelija
	 * može označiti kao ispravno podijeljena.
	 */
	public JMenuItem markAsOk1 = new JMenuItem(new MyAbstractAction("Mark as OK", this, MyAbstractAction.MARK_AS_OK1));

	/**
	 * Opcija u padajućem izborniku desne tablice pomoću koje se neka ćelija
	 * može označiti kao ispravno podijeljena.
	 */
	public JMenuItem markAsOk2 = new JMenuItem(new MyAbstractAction("Mark as OK", this, MyAbstractAction.MARK_AS_OK2));

	/** Sadrži <code>true</code> kada se ne žele bilježiti undo akcije. */
	public boolean undoOff = true;

	/**
	 * Sadrži <code>true</code> ako se želi označiti čeliju kao ispravno
	 * podijeljenu čim ju se označi.
	 */
	public boolean markOKOnSelect = false;

	/** Skup svih ikona koje su trenutno vidljive. */
	private Set<Icon> visibleIcons = new HashSet<Icon>();

	/** Skup svih ikona koje bi trenutno trebale biti vidljive. */
	private Set<Icon> shouldBeVisibleIcons = new HashSet<Icon>();

	/** Verzija programa. */
	public String version = "v0.310 beta";

	// KRAJ DEKLARACIJE VARIJABLI **********************************

	/**
	 * Konstruktor klase.
	 */
	public MainWindow() {
		super();
		loadPropertiesXML("language/english.xml", language);
		loadPropertiesXML("config.xml", config);
		loadAcronymsFilesList();
		createActions();
		initGUI();
		createMenus();
		addListeners();
		enableComponents(false);
		loadData();
		sboard.setConnectionPopupMenu(Boolean.parseBoolean(config.getProperty("sboardShowMenu")));
		dataModel.setStatistics(stats);
	}

	/**
	 * Metoda koja briše sve podatke iz podatkovnog modela i elemente ove klase
	 * koji sadrže podatke specifične za neki projekt. Ova metoda se poziva
	 * uvijek prije učitavanja novog projekta kako ne bi došlo do miješanja
	 * podatka prethodnog projekta s novoučitanim.
	 */
	public void clear() {
		dataModel.clear();
		leftIconsByID.clear();
		rightIconsByID.clear();
		leftRowWithId.clear();
		rightRowWithId.clear();
		leftNotSure.clear();
		rightNotSure.clear();
		rowsWithBookmarksLeft.clear();
		rowsWithBookmarksRight.clear();
		bookmarksById.clear();
		// fileExporter = new FileExporter(dataModel);
		undoManager.discardAllEdits();

		jTable1Model = new DefaultTableModel(0, 1);
		jTable1.setModel(jTable1Model);

		jTable2Model = new DefaultTableModel(0, 1);
		jTable2.setModel(jTable2Model);
		
		stats = new Statistics();
		dataModel.setStatistics(stats);

		reloadBookmarks();
	}

	/**
	 * Metoda koja registrira sve potrebne listenere. Poziva se nakon
	 * inicijalizacije grafičkog sučelja i kada se pogube reference na određene
	 * objekte.
	 */
	public void addListeners() {
		dataModel.addUndoableEditListener(this);
		dataModel.addPropertyChangeListener(this);

		jTable1.addMouseListener(new TableMouseListener(this, 1));
		jTable1.addComponentListener(this);
		jTable2.addMouseListener(new TableMouseListener(this, 2));

		jTextField1.addKeyListener(keyListener);
		jTextField2.addKeyListener(keyListener2);

		sboard.addMouseWheelListener(new SboardMouseWheelListener(this));
	}

	/**
	 * Metoda puni globalno polje <code>String[] acronymsFilesList</code> s
	 * listom datoteka koje se nalaze u direktoriju s kraticama.
	 */
	public void loadAcronymsFilesList() {
		File file = new File("./acronyms");
		File[] files = file.listFiles();
		acronymsFilesList = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			acronymsFilesList[i] = files[i].getName();
		}
	}

	/**
	 * Glavna metoda koja vrši inicijalizaciju grafičkog sučelja.
	 * Inicijalizacija se vrši kroz 6 podmetoda od kojih svaka inicijalizira
	 * određeni dio grafičkog sučelja. Na dno prozora (južni položaj
	 * BorderLayout-a glavnog frame-a) postavlja se zaseban panel na kojemu se
	 * nalazi labela koja predstavlja statusnu traku.
	 */
	private void initGUI() {

		sboard = new Switchboard(dataModel, language);
		sboard.setSelectionTreshold(Short.parseShort((String) config.get("sboardTreshold")));

		initGUIBasics();
		initGUILeftSide();
		initGUICenter();
		initGUIRightSide();
		initGUIMainToolbar();
		initGUIBookmarksPane();

		{
			jPanelStatus = new JPanel();
			getContentPane().add(jPanelStatus, BorderLayout.SOUTH);
			jPanelStatus.setLayout(null);
			jPanelStatus.setPreferredSize(new java.awt.Dimension(592, 20));
			jPanelStatus.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			{
				statusLabel = new JLabel();
				jPanelStatus.add(statusLabel);
				statusLabel.setBounds(7, 2, 783, 14);
			}
		}

		Icon.setImage(new ImageIcon("icons/icon.gif").getImage());

		jTable1.setColumnSelectionAllowed(true);
		jTable1.setRowSelectionAllowed(true);

		jTable2.setColumnSelectionAllowed(true);
		jTable2.setRowSelectionAllowed(true);

		pack();
		setVisible(true);
	}

	/**
	 * Metoda u kojoj se inicijaliziraju osnovni elementi grafičkog sučelja.
	 */
	private void initGUIBasics() {
		setTitle("CORAL " + version);
		setMinimumSize(new Dimension(800, 500));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		BorderLayout thisLayout = new BorderLayout();
		getContentPane().setLayout(thisLayout);

		jPanelMain = new JPanel();
		MainLayout jPanel1Layout = new MainLayout();
		getContentPane().add(jPanelMain, BorderLayout.CENTER);
		jPanelMain.setLayout(jPanel1Layout);
		jPanelMain.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		jSplitPane1 = new JSplitPane();
		jSplitPane1.setBorder(null);
		jSplitPane1.setDividerLocation(100);
		jSplitPane1.setDividerSize(7);
		jSplitPane1.setOneTouchExpandable(true);
	}

	/**
	 * Metoda koja služi za inicijalizaciju panela za prikazivanje knjižnih
	 * oznaka. Sadrži tablicu unutar koje se prikazuje lista knjižih oznaka te
	 * alatnu traku s gumbima za uklanjanje i editiranje knjižnih oznaka.
	 */
	private void initGUIBookmarksPane() {
		jPanelBookmarks = new JPanel();
		jPanelBookmarks.setLayout(new BorderLayout());
		jPanelBookmarks.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		getContentPane().add(jPanelBookmarks, BorderLayout.WEST);
		jPanelBookmarks.setVisible(false);
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JLabel labelBookmarks = new JLabel();
		labelBookmarks.setText("Bookmarks:");
		toolBar.add(labelBookmarks);
		jPanelBookmarks.add(toolBar, BorderLayout.NORTH);

		JToolBar toolBar2 = new JToolBar();
		toolBar2.setFloatable(false);
		JButton removeButton = new JButton(new MyAbstractAction("Remove bookmark", this,
				MyAbstractAction.REMOVE_BOOKMARK2));
		removeButton.setText("Remove");
		removeButton.setBorder(buttonBorder);
		removeButton.addMouseListener(mouseButtonListener);
		toolBar2.add(removeButton);

		JButton editButton = new JButton(new MyAbstractAction("Edit bookmark", this, MyAbstractAction.EDIT_BOOKMARK2));
		editButton.setText("Edit");
		editButton.setBorder(buttonBorder);
		editButton.addMouseListener(mouseButtonListener);
		toolBar2.add(editButton);

		jPanelBookmarks.add(toolBar2, BorderLayout.SOUTH);

		JScrollPane jScrlPnBookmarks = new JScrollPane();
		jPanelBookmarks.add(jScrlPnBookmarks, BorderLayout.CENTER);

		jTableBookmarksModel = new DefaultTableModel(0, 1);
		jTableBookmarks = new JBookmarksTable(this, jTableBookmarksModel) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; // Disallow the editing of any cell
			}
		};
		jTableBookmarks.setTableHeader(null);

		jTableBookmarks.setSize(80, 0);
		jTableBookmarks.setMinimumSize(new Dimension(100, 0));
		jTableBookmarks.setMaximumSize(new Dimension(100, 0));

		jScrlPnBookmarks.setViewportView(jTableBookmarks);
	}

	/**
	 * Pomoću ove metode se inicijalizira lijeva strana prozora. Ona sadrži
	 * jedan panel s BorderLayoutom. Na sjevernom položaju tog panela nalazi se
	 * panel s labelama. Na centralnom položaju je jScrollPane unutar kojega se
	 * nalazi tablica, a na južnom položaju je panel s alatnom trakom za
	 * upravljanje tablicom (i panelima za pretraživanje i aktiviranje
	 * automatske segmentacije, no, oni se dodaju i uklanjaju s panela na akcije
	 * korisnika).
	 */
	private void initGUILeftSide() {
		{
			jPanel2 = new JPanel();
			BorderLayout jPanel2Layout = new BorderLayout();
			jPanelMain.add(jPanel2, MainLayout.WEST);
			jPanel2.setLayout(jPanel2Layout);
			jPanel2.setPreferredSize(new java.awt.Dimension(110, 473));
			{
				jScrollPane1.setVerticalScrollBar(new JScrollBar2(sboard, 1, jScrollPane2, this));
				jPanel2.add(jScrollPane1, BorderLayout.CENTER);
				jScrollPane1.getVerticalScrollBar().setUnitIncrement(50);
				{
					jTable1Model = new DefaultTableModel(0, 1);
					jTable1 = new JTable2(this, JTable2.LEFT);
					jScrollPane1.setViewportView(jTable1);
					jTable1.setModel(jTable1Model);
					jTable1.setTableHeader(null);

					TableColumnModel cmodel = jTable1.getColumnModel();
					// columnModel1 = new XTableColumnModel();
					jTable1.setColumnModel(cmodel);

					// jTable1Model.addColumn("Element");
					TextAreaRenderer textAreaRenderer = new TextAreaRenderer(this, TextAreaRenderer.LEFT);
					cmodel.getColumn(0).setCellRenderer(textAreaRenderer);
					TextAreaEditor textEditor = new TextAreaEditor(dataModel);
					cmodel.getColumn(0).setCellEditor(textEditor);

				}
			}

			{
				jPanel5 = new JPanel();
				jPanel2.add(jPanel5, BorderLayout.NORTH);
				jPanel5.setLayout(new BorderLayout());
				{
					langLabel1 = new JLabel();
					jPanel5.add(langLabel1, BorderLayout.NORTH);
					String labelText = language.getProperty("labelLanguage");
					langLabel1.setText(labelText + " -");
				}
				{
					fileLabel1 = new JLabel();
					jPanel5.add(fileLabel1, BorderLayout.SOUTH);
					String labelText = language.getProperty("labelFile");
					fileLabel1.setText(labelText + " -");
				}
			}

			{
				jPanel8 = new JPanel();
				BorderLayout jPanel8Layout = new BorderLayout();
				jPanel2.add(jPanel8, BorderLayout.SOUTH);
				jPanel8.setLayout(jPanel8Layout);
				{
					jToolBar2 = new JToolBar();
					jPanel8.add(jToolBar2, BorderLayout.SOUTH);
					jToolBar2.setBorder(BorderFactory.createTitledBorder("Left List Controls"));
					jToolBar2.setSize(200, 27);
					jToolBar2.setFloatable(false);
					{
						jBtnLeftSplit = new JButton(new MyAbstractAction("Left split", this,
								MyAbstractAction.BUTTON_LEFT_SPLIT));
						jToolBar2.add(jBtnLeftSplit);
						jBtnLeftSplit.setBounds(5, 7, 32, 32);
						jBtnLeftSplit.setToolTipText("Split");
						jBtnLeftSplit.setText("Split");
						jBtnLeftSplit.addMouseListener(mouseButtonListener);
						jBtnLeftSplit.setIcon(new ImageIcon("icons/disconnect.png"));
						jBtnLeftSplit.setBorder(buttonBorder);
					}
					{
						jBtnLeftMerge = new JButton(new MyAbstractAction("Left merge", this,
								MyAbstractAction.BUTTON_LEFT_MERGE));
						jToolBar2.add(jBtnLeftMerge);
						jBtnLeftMerge.setBounds(84, 7, 63, 28);
						jBtnLeftMerge.setToolTipText("Merge");
						jBtnLeftMerge.addMouseListener(mouseButtonListener);
						jBtnLeftMerge.setIcon(new ImageIcon("icons/connect-32x32.png"));
						jBtnLeftMerge.setSize(32, 32);
						jBtnLeftMerge.setBorder(buttonBorder);
						jBtnLeftMerge.setText("Merge");
					}

					jToolBar2.addSeparator();
					{
						jTglBtnAutoSplitAllLeft = new JToggleButton(new MyAbstractAction("Auto segmentation", this,
								MyAbstractAction.BUTTON_AUTO_SPLIT_LEFT));
						jToolBar2.add(jTglBtnAutoSplitAllLeft);
						jTglBtnAutoSplitAllLeft.setBounds(84, 7, 63, 28);
						jTglBtnAutoSplitAllLeft.setToolTipText("Split all to sentences");
						jTglBtnAutoSplitAllLeft.setIcon(new ImageIcon("icons/split_16.gif"));
						jTglBtnAutoSplitAllLeft.addMouseListener(mouseButtonListener);
						jTglBtnAutoSplitAllLeft.setText("Auto segmentation");
						jTglBtnAutoSplitAllLeft.setSize(32, 32);
						jTglBtnAutoSplitAllLeft.setBorder(buttonBorder);
					}
					jToolBar2.addSeparator();
					{
						jTglBtnLeftFind = new JToggleButton(new MyAbstractAction("Left find", this,
								MyAbstractAction.TGLBTN_LEFT_FIND));
						jTglBtnLeftFind.setText("Find");
						jTglBtnLeftFind.addMouseListener(mouseButtonListener);
						jTglBtnLeftFind.setBorder(buttonBorder);
						jTglBtnLeftFind.setIcon(new ImageIcon("icons/find.gif"));
						jToolBar2.add(jTglBtnLeftFind);

					}
				}
				{
					jToolBar9 = new JToolBar();
					jToolBar9.setVisible(false);
					jToolBar9.setFloatable(false);
					{
						JScrollPane sp = new JScrollPane();
						sp.setPreferredSize(new Dimension(50, 50));
						jToolBar9.add(sp);
						leftDetailsTable = new JTable();
						leftDetailsTable.setTableHeader(null);
						sp.setViewportView(leftDetailsTable);
						leftDetailsTableModel = new DefaultTableModel(0, 2);
						leftDetailsTable.setModel(leftDetailsTableModel);
					}

				}
				{
					jToolBar5 = new JToolBar();
					// jPanel8.add(jToolBar5, BorderLayout.NORTH);
					jToolBar5.setVisible(false);
					jToolBar5.setFloatable(false);
					{
						jTextField1 = new JTextField();
						jToolBar5.add(jTextField1);
						jTextField1.setText("");
						keyListener = new KeyListener(jTextField1, jTable1, jTable1Model);

					}
					{
						jButton8 = new JButton();
						jToolBar5.add(jButton8);
						jButton8.setText("Next");
						jButton8.addMouseListener(mouseButtonListener);
						jButton8.setBorder(buttonBorder);
						jButton8.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButton8ActionPerformed(evt);
							}
						});
					}
				}
				{
					jToolBar7 = new JToolBar();
					// jPanel8.add(jToolBar7, BorderLayout.NORTH);
					jToolBar7.setVisible(false);
					jToolBar7.setFloatable(false);
					{
						JLabel label = new JLabel();
						label.setText("Abbreviations:");
						jToolBar7.add(label);
					}
					{
						ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(acronymsFilesList);
						jComboBox1 = new JComboBox();
						jToolBar7.add(jComboBox1);
						jComboBox1.setModel(jComboBox1Model);
						jComboBox1.setBounds(12, 37, 157, 21);
					}
					{
						jButton14 = new JButton(new MyAbstractAction("Auto split", this,
								MyAbstractAction.AUTO_SPLIT_LEFT));
						jToolBar7.add(jButton14);
						jButton14.setText("Start segmentation");
						jButton14.setIcon(new ImageIcon("./icons/split_16.gif"));
						jButton14.setBorder(buttonBorder);
						jButton14.addMouseListener(mouseButtonListener);
					}

				}
			}
		}
	}

	/**
	 * Metoda pomoću koje se inicijalizira središnji dio prozora (središnja
	 * komponenta). Sastoji se od panela s BorderLayout-om u čijem centralnom
	 * dijelu se nalazi komponenta <code>Switchboard</code> koja omogućuje
	 * vizualno prikazivanje i crtanje veza između elemenata. U južnom dijelu
	 * nalazi se panel s alatnom trakom za upravljanje Switchboard-om.
	 */
	private void initGUICenter() {
		{
			jPanel3 = new JPanel();
			BorderLayout jPanel3Layout = new BorderLayout();
			jPanel3.setLayout(jPanel3Layout);
			jPanelMain.add(jPanel3, MainLayout.CENTER);
			jPanel3.setPreferredSize(new java.awt.Dimension(switchBoardWidth, 59));
			jPanel3.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
			jPanel3.add(sboard, BorderLayout.CENTER);
			{
				jToolBar3 = new JToolBar();
				jPanel3.add(jToolBar3, BorderLayout.SOUTH);
				jToolBar3.setSize(150, 25);
				jToolBar3.setBorder(BorderFactory.createTitledBorder("Switchboard Controls"));
				jToolBar3.setFloatable(false);
				{
					jBtnShiftConDown = new JButton();
					jToolBar3.add(jBtnShiftConDown);
					// jButton3.setText("v");
					jBtnShiftConDown.setIcon(new ImageIcon("icons/icon-arrow-down.gif"));
					jBtnShiftConDown.setBounds(0, 0, 48, 28);
					jBtnShiftConDown.setBorder(buttonBorder);
					jBtnShiftConDown.addMouseListener(mouseButtonListener);
					jBtnShiftConDown.setToolTipText("Shift connections down");
					jBtnShiftConDown.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButton3ActionPerformed(evt);
						}
					});
				}
				{
					jButton7 = new JButton();
					jToolBar3.add(jButton7);
					jButton7.setIcon(new ImageIcon("icons/icon-arrow-up.gif"));
					jButton7.setBounds(89, 0, 51, 28);
					jButton7.setBorder(buttonBorder);
					jButton7.addMouseListener(mouseButtonListener);
					jButton7.setToolTipText("Shift connections up");
					jButton7.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButton7ActionPerformed(evt);
						}
					});
				}
				jToolBar3.addSeparator();
				{
					jButton9 = new JButton(new MyAbstractAction("Remove All Connections", this,
							MyAbstractAction.REMOVE_ALL_CONNECTIONS));
					jToolBar3.add(jButton9);
					// jButton8.setIcon(new ImageIcon(
					// "icons/icon-arrow-up.gif"));
					jButton9.setBounds(89, 0, 51, 28);
					jButton9.setText("");
					jButton9.setIcon(new ImageIcon("icons/remove_all_connections.gif"));
					jButton9.addMouseListener(mouseButtonListener);
					jButton9.setBorder(buttonBorder);
					jButton9.setToolTipText("Remove all connections");

				}
			}

		}
	}

	/**
	 * Pomoću ove metode se inicijalizira desna strana prozora. Ona sadrži jedan
	 * panel s BorderLayoutom. Na sjevernom položaju tog panela nalazi se panel
	 * s labelama. Na centralnom položaju je jScrollPane unutar kojega se nalazi
	 * tablica, a na južnom položaju je panel s alatnom trakom za upravljanje
	 * tablicom (i panelima za pretraživanje i aktiviranje automatske
	 * segmentacije, no, oni se dodaju i uklanjaju s panela na akcije
	 * korisnika).
	 */
	private void initGUIRightSide() {
		{
			jPanelRight = new JPanel();
			BorderLayout jPanel4Layout = new BorderLayout();
			jPanelMain.add(jPanelRight, MainLayout.EAST);
			jPanelRight.setLayout(jPanel4Layout);
			jPanelRight.setPreferredSize(new java.awt.Dimension(188, 431));
			{
				jScrollPane2.setVerticalScrollBar(new JScrollBar2(sboard, 2, jScrollPane1, this));
				jScrollPane2.getVerticalScrollBar().setUnitIncrement(50);
				jPanelRight.add(jScrollPane2, BorderLayout.CENTER);
				{
					jTable2Model = new DefaultTableModel(0, 1);
					jTable2 = new JTable2(this, JTable2.RIGHT);
					jScrollPane2.setViewportView(jTable2);
					jTable2.setModel(jTable2Model);
					jTable2.setTableHeader(null);

					TableColumnModel cmodel = jTable2.getColumnModel();
					TextAreaRenderer textAreaRenderer = new TextAreaRenderer(this, TextAreaRenderer.RIGHT);
					cmodel.getColumn(0).setCellRenderer(textAreaRenderer);
					TextAreaEditor textEditor = new TextAreaEditor(dataModel);
					cmodel.getColumn(0).setCellEditor(textEditor);

				}
			}

			{
				jPanel7 = new JPanel();
				jPanelRight.add(jPanel7, BorderLayout.NORTH);
				jPanel7.setLayout(new BorderLayout());
				{
					langLabel2 = new JLabel();
					jPanel7.add(langLabel2, BorderLayout.NORTH);
					String labelText = language.getProperty("labelLanguage");
					langLabel2.setText(labelText + " -");
				}
				{
					fileLabel2 = new JLabel();
					jPanel7.add(fileLabel2, BorderLayout.SOUTH);
					String labelText = language.getProperty("labelFile");
					fileLabel2.setText(labelText + " -");
				}
			}
			{
				jPanelRightFind = new JPanel();
				BorderLayout jPanel9Layout = new BorderLayout();
				jPanelRight.add(jPanelRightFind, BorderLayout.SOUTH);
				jPanelRightFind.setLayout(jPanel9Layout);
				{
					jToolBar6 = new JToolBar();
					jPanelRightFind.add(jToolBar6, BorderLayout.NORTH);
					jToolBar6.setVisible(false);
					jToolBar6.setFloatable(false);
					{
						jTextField2 = new JTextField();
						jToolBar6.add(jTextField2);
						jTextField2.setText("");
						keyListener2 = new KeyListener(jTextField2, jTable2, jTable2Model);

					}
					{
						jBtnRightNext = new JButton();
						jToolBar6.add(jBtnRightNext);
						jBtnRightNext.setText("Next");
						jBtnRightNext.setBorder(buttonBorder);
						jBtnRightNext.addMouseListener(mouseButtonListener);
						jBtnRightNext.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButton13ActionPerformed(evt);
							}
						});
					}
				}
				{
					jToolBar4 = new JToolBar();
					jPanelRightFind.add(jToolBar4, BorderLayout.SOUTH);
					jToolBar4.setBorder(BorderFactory.createTitledBorder("Right List Controls"));
					jToolBar4.setFloatable(false);
					{
						jButton4 = new JButton();
						jToolBar4.add(jButton4);
						jButton4.setIcon(new ImageIcon("icons/disconnect.png"));
						jButton4.setBounds(21, 7, 63, 28);
						jButton4.setToolTipText("Split");
						jButton4.setText("Split");
						jButton4.addMouseListener(mouseButtonListener);
						jButton4.setBorder(buttonBorder);
						jButton4.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButton4ActionPerformed(evt);
							}
						});
					}
					{
						jBtnMerge = new JButton();
						jToolBar4.add(jBtnMerge);
						jBtnMerge.setBounds(105, 7, 63, 28);
						jBtnMerge.setToolTipText("Merge");
						jBtnMerge.setText("Merge");
						jBtnMerge.addMouseListener(mouseButtonListener);
						jBtnMerge.setIcon(new ImageIcon("icons/connect-32x32.png"));
						jBtnMerge.setBorder(buttonBorder);
						jBtnMerge.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButton5ActionPerformed(evt);
							}
						});
					}
					jToolBar4.addSeparator();
					{
						jTglBtnAutoSplitAllRight = new JToggleButton(new MyAbstractAction("Auto segmentation", this,
								MyAbstractAction.BUTTON_AUTO_SPLIT_RIGHT));
						jToolBar4.add(jTglBtnAutoSplitAllRight);
						jTglBtnAutoSplitAllRight.setBounds(84, 7, 63, 28);
						jTglBtnAutoSplitAllRight.setToolTipText("Split all to sentences");
						jTglBtnAutoSplitAllRight.setIcon(new ImageIcon("icons/split_16.gif"));
						jTglBtnAutoSplitAllRight.addMouseListener(mouseButtonListener);
						jTglBtnAutoSplitAllRight.setText("Auto segmentation");
						jTglBtnAutoSplitAllRight.setSize(32, 32);
						jTglBtnAutoSplitAllRight.setBorder(buttonBorder);
					}
					jToolBar4.addSeparator();
					{
						jTglBtnRightFind = new JToggleButton(new MyAbstractAction("Find", this,
								MyAbstractAction.TGLBTN_RIGHT_FIND));
						jTglBtnRightFind.addMouseListener(mouseButtonListener);
						jTglBtnRightFind.setIcon(new ImageIcon("icons/find.gif"));
						jToolBar4.add(jTglBtnRightFind);
						jTglBtnRightFind.setText("Find");
						jTglBtnRightFind.setBorder(buttonBorder);
					}
					{
						jToolBar10 = new JToolBar();
						jToolBar10.setVisible(false);
						jToolBar10.setFloatable(false);
						{
							JScrollPane sp = new JScrollPane();
							sp.setPreferredSize(new Dimension(50, 50));
							jToolBar10.add(sp);
							rightDetailsTable = new JTable();
							rightDetailsTable.setTableHeader(null);
							sp.setViewportView(rightDetailsTable);
							rightDetailsTableModel = new DefaultTableModel(0, 2);
							rightDetailsTable.setModel(rightDetailsTableModel);
						}

					}
					{
						jToolBar8 = new JToolBar();
						// jPanel8.add(jToolBar7, BorderLayout.NORTH);
						jToolBar8.setVisible(false);
						jToolBar8.setFloatable(false);
						{
							JLabel label = new JLabel();
							label.setText("Abbreviations:");
							jToolBar8.add(label);
						}
						{
							ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(acronymsFilesList);
							jComboBox2 = new JComboBox();
							jToolBar8.add(jComboBox2);
							jComboBox2.setModel(jComboBox1Model);
							jComboBox2.setBounds(12, 37, 157, 21);
						}
						{
							jButton18 = new JButton(new MyAbstractAction("Start segmentation", this,
									MyAbstractAction.AUTO_SPLIT_RIGHT));
							jToolBar8.add(jButton18);
							jButton18.setText("Start segmentation");
							jButton18.setIcon(new ImageIcon("icons/split_16.gif"));
							jButton18.setBorder(buttonBorder);
							jButton18.addMouseListener(mouseButtonListener);
						}

					}
				}
			}
		}
	}

	/**
	 * Metoda za inicijalizaciju glavne alatne trake. Alatna traka se sastoji od
	 * kontrola za otvaranje novog projekta, otvaranje postojećeg projekta,
	 * spremanje projekta, spremanje projekta uz specifikaciju naziva datoteke,
	 * undo i redo opcija, gumba za prikazivanje/uklanjanje panela s knjižnim
	 * oznakama i dva gumba za odabir načina klizanja (scroll) tablica.
	 */
	private void initGUIMainToolbar() {
		{
			jToolBar1 = new JToolBar();
			getContentPane().add(jToolBar1, BorderLayout.NORTH);
			jToolBar1.setPreferredSize(new java.awt.Dimension(592, 35));
			jToolBar1.setFloatable(false);

			{
				jBtnNewProject = new JButton(allActions.get("CreateProject"));
				jToolBar1.add(jBtnNewProject);
				jBtnNewProject.setBounds(105, 7, 63, 28);
				jBtnNewProject.setToolTipText("Create New Project");
				jBtnNewProject.setText(null);
				jBtnNewProject.addMouseListener(mouseButtonListener);
				jBtnNewProject.setIcon(new ImageIcon("icons/new.gif"));
				jBtnNewProject.setBorder(buttonBorder);
				jBtnNewProject.addMouseListener(mouseButtonListener);
			}
			{
				jBtnOpenProject = new JButton(allActions.get("Open"));
				jToolBar1.add(jBtnOpenProject);
				jBtnOpenProject.addMouseListener(mouseButtonListener);
				jBtnOpenProject.setBounds(105, 7, 63, 28);
				jBtnOpenProject.setToolTipText("Open Project");
				jBtnOpenProject.setText(null);
				jBtnOpenProject.setIcon(new ImageIcon("icons/open.gif"));
				jBtnOpenProject.setBorder(buttonBorder);
			}
			{
				jBtnSaveProject = new JButton(allActions.get("Save"));
				jToolBar1.add(jBtnSaveProject);
				jBtnSaveProject.addMouseListener(mouseButtonListener);
				jBtnSaveProject.setBounds(105, 7, 63, 28);
				jBtnSaveProject.setToolTipText("Save Project");
				jBtnSaveProject.setText(null);
				jBtnSaveProject.setIcon(new ImageIcon("icons/save.gif"));
				jBtnSaveProject.setBorder(buttonBorder);
			}
			{
				jButton17 = new JButton(new MyAbstractAction("Save As", this, MyAbstractAction.SAVE_AS_PROJECT));
				jToolBar1.add(jButton17);
				jButton17.addMouseListener(mouseButtonListener);
				jButton17.setBounds(105, 7, 63, 28);
				jButton17.setToolTipText("Save Project As");
				jButton17.setText(null);
				jButton17.setIcon(new ImageIcon("icons/save_as.gif"));
				jButton17.setBorder(buttonBorder);
			}
			jToolBar1.addSeparator();
			{
				jBtnUndo = new JButton(new MyAbstractAction("Undo", this, MyAbstractAction.UNDO));
				jToolBar1.add(jBtnUndo);
				jBtnUndo.addMouseListener(mouseButtonListener);
				jBtnUndo.setText(null);
				jBtnUndo.setToolTipText("Undo action");
				jBtnUndo.setBorder(buttonBorder);
				jBtnUndo.setIcon(new ImageIcon("icons/undo.gif"));
				jBtnUndo.setEnabled(false);

			}
			{
				jBtnRedo = new JButton(new MyAbstractAction("Redo", this, MyAbstractAction.REDO));
				jToolBar1.add(jBtnRedo);
				jBtnRedo.addMouseListener(mouseButtonListener);
				jBtnRedo.setText(null);
				jBtnRedo.setToolTipText("Redo action");
				jBtnRedo.setBorder(buttonBorder);
				jBtnRedo.setIcon(new ImageIcon("icons/redo.gif"));
				jBtnRedo.setEnabled(false);

			}
			jToolBar1.addSeparator();
			{
				jBtnRefresh = new JButton(new MyAbstractAction("Refresh", this, MyAbstractAction.REFRESH));
				jToolBar1.add(jBtnRefresh);
				jBtnRefresh.addMouseListener(mouseButtonListener);
				jBtnRefresh.setText(null);
				jBtnRefresh.setToolTipText("Refresh");
				jBtnRefresh.setBorder(buttonBorder);
				jBtnRefresh.setIcon(new ImageIcon("icons/refresh.gif"));
			}
			jToolBar1.addSeparator();
			{
				jTglBtnBookmarks = new JToggleButton(allActions.get("ShowBookmarks"));
				jTglBtnBookmarks.addMouseListener(mouseButtonListener);
				 jTglBtnBookmarks.setIcon(new
				 ImageIcon("icons/icon_bookmark.gif"));
				jToolBar1.add(jTglBtnBookmarks);
				jTglBtnBookmarks.setText("");
				jTglBtnBookmarks.setToolTipText("Bookmarks");
				jTglBtnBookmarks.setBorder(buttonBorder);
			}
			{
				jTglBtnDetails = new JToggleButton(new MyAbstractAction("Details", this,
						MyAbstractAction.TGLBTN_DETAILS));
				jTglBtnDetails.addMouseListener(mouseButtonListener);
				jTglBtnDetails.setIcon(new
				 ImageIcon("icons/details.gif"));
				jToolBar1.add(jTglBtnDetails);
				jTglBtnDetails.setText("");
				jTglBtnDetails.setToolTipText("Element details");
				jTglBtnDetails.setBorder(buttonBorder);
			}
		}
			jToolBar1.addSeparator();
			{
				jTglBtnNormalScroll = new JToggleButton();
				jTglBtnNormalScroll.addMouseListener(mouseButtonListener);
				jToolBar1.add(jTglBtnNormalScroll);
				jTglBtnNormalScroll.setText("Normal Scroll");
				jTglBtnNormalScroll.setBorder(buttonBorder);
				if (config.get("scrollType").equals("normal")) {
					jTglBtnNormalScroll.setSelected(true);
				}
				jTglBtnNormalScroll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jToggleButton1ActionPerformed(evt);
					}
				});
			}
			{
				jTglBtnScrollTogether = new JToggleButton();
				jTglBtnScrollTogether.addMouseListener(mouseButtonListener);
				jToolBar1.add(jTglBtnScrollTogether);
				jTglBtnScrollTogether.setText("Scroll Together");
				jTglBtnScrollTogether.setBorder(buttonBorder);
				if (config.get("scrollType").equals("together")) {
					jTglBtnScrollTogether.setSelected(true);
				}
				jTglBtnScrollTogether.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jToggleButton2ActionPerformed(evt);
					}
				});
			}

	}

	/**
	 * Metoda u kojoj se kreira gornja traka s padajućim izbornicima i padajući
	 * izbornici koji se aktiviraju na pritisak desne tipke nad lijevom ili
	 * desnom tablicom.
	 */
	private void createMenus() {
		JMenuItem mItem;
		JMenuBar menuBar = new JMenuBar();

		menuFile = new JMenu(language.getProperty("menuFile"));
		menuFile.setMnemonic(KeyEvent.VK_F);
		{
			mItem = new JMenuItem(allActions.get("CreateProject"));
			mItem.setText(language.getProperty("menuCreateProject"));
			mItem.setMnemonic(KeyEvent.VK_N);
			mItem.setIcon(new ImageIcon("icons/small/new.gif"));
			mItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
			menuFile.add(mItem);

			mItem = new JMenuItem(allActions.get("Open"));
			mItem.setText(language.getProperty("menuOpen"));
			mItem.setMnemonic(KeyEvent.VK_O);
			mItem.setIcon(new ImageIcon("icons/small/open.gif"));
			mItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
			menuFile.add(mItem);

			mItemSave = new JMenuItem(allActions.get("Save"));
			mItemSave.setText(language.getProperty("menuSave"));
			mItemSave.setMnemonic(KeyEvent.VK_S);
			mItemSave.setIcon(new ImageIcon("icons/small/save.gif"));
			mItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
			menuFile.add(mItemSave);

			mItemSaveAs = new JMenuItem(new MyAbstractAction("Save Project As", this, MyAbstractAction.SAVE_AS_PROJECT));
			mItemSaveAs.setText(language.getProperty("menuSaveAs"));
			mItemSaveAs.setIcon(new ImageIcon("icons/small/save_as.gif"));
			menuFile.add(mItemSaveAs);

			menuFile.addSeparator();

			menuExport = new JMenu(language.getProperty("menuExport"));
			mItem = new JMenuItem(new MyAbstractAction("Export left XML", this, MyAbstractAction.EXPORT_XML1));
			menuExport.add(mItem);
			mItem = new JMenuItem(new MyAbstractAction("Export right XML", this, MyAbstractAction.EXPORT_XML2));
			menuExport.add(mItem);
			mItem = new JMenuItem(new MyAbstractAction("Export to TMX", this, MyAbstractAction.EXPORT_TMX));
			menuExport.add(mItem);
			mItem = new JMenuItem(new MyAbstractAction("Export translation unit", this, MyAbstractAction.EXPORT_TEI));
			menuExport.add(mItem);
			menuFile.add(menuExport);

			menuFile.addSeparator();

			mItem = new JMenuItem(new MyAbstractAction("Settings", this, MyAbstractAction.SETTINGS_WINDOW));
			mItem.setIcon(new ImageIcon("icons/small/settings.gif"));
			menuFile.add(mItem);

			menuFile.addSeparator();

			mItem = new JMenuItem(allActions.get("Exit"));
			mItem.setText(language.getProperty("menuExit"));
			mItem.setMnemonic(KeyEvent.VK_X);
			mItem.setIcon(new ImageIcon("icons/small/exit.gif"));
			mItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
			menuFile.add(mItem);
		}

		menuBar.add(menuFile);

		menuEdit = new JMenu(language.getProperty("menuEdit"));
		menuEdit.setMnemonic(KeyEvent.VK_E);

		mItemUndo = new JMenuItem(new MyAbstractAction("Undo", this, MyAbstractAction.UNDO));
		mItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		mItemUndo.setMnemonic(KeyEvent.VK_Z);
		mItemUndo.setIcon(new ImageIcon("icons/small/undo.gif"));
		mItemUndo.setEnabled(false);
		menuEdit.add(mItemUndo);

		mItemRedo = new JMenuItem(new MyAbstractAction("Redo", this, MyAbstractAction.REDO));
		mItemRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		mItemRedo.setMnemonic(KeyEvent.VK_Y);
		mItemRedo.setIcon(new ImageIcon("icons/small/redo.gif"));
		mItemRedo.setEnabled(false);
		menuEdit.add(mItemRedo);

		menuEdit.addSeparator();

		menuEdit.add(new JMenuItem(new MyAbstractAction("Delete selected elements (left)", this,
				MyAbstractAction.DELETE_ELEMENT1)));
		menuEdit.add(new JMenuItem(new MyAbstractAction("Insert element above selection (left)", this,
				MyAbstractAction.INSERT_ELEMENT_ABOVE1)));
		menuEdit.add(new JMenuItem(new MyAbstractAction("Insert element below selection (left)", this,
				MyAbstractAction.INSERT_ELEMENT_BELOW1)));
		menuEdit.addSeparator();
		menuEdit.add(new JMenuItem(new MyAbstractAction("Delete selected elements (right)", this,
				MyAbstractAction.DELETE_ELEMENT2)));
		menuEdit.add(new JMenuItem(new MyAbstractAction("Insert element above selection (right)", this,
				MyAbstractAction.INSERT_ELEMENT_ABOVE2)));
		menuEdit.add(new JMenuItem(new MyAbstractAction("Insert element below selection (right)", this,
				MyAbstractAction.INSERT_ELEMENT_BELOW2)));
		menuBar.add(menuEdit);

		menuProject = new JMenu(language.getProperty("menuProject"));
		menuProject.setMnemonic(KeyEvent.VK_P);
		mItem = new JMenuItem(allActions.get("Alg1on1"));
		mItem.setText("Connect 1on1");
		menuProject.add(mItem);
		menuProject.add(new JMenuItem(new MyAbstractAction("Gale-Church", this, MyAbstractAction.GALE_CHURCH)));
		menuProject.add(new JMenuItem(new MyAbstractAction("Coral Algorithm beta", this, MyAbstractAction.JAPEC_ALGORITHM)));
		menuProject.addSeparator();
		menuProject.add(new JMenuItem(allActions.get("Statistics")));
		menuProject.add(new JMenuItem(new MyAbstractAction("Edit Project", this, MyAbstractAction.EDIT_PROJECT)));
		menuBar.add(menuProject);

		// JMenu menuSettings = new JMenu(language.getProperty("menuSettings"));

		// mItem = new JMenuItem(new MyAbstractAction("Settings", this,
		// MyAbstractAction.SETTINGS_WINDOW));
		// menuSettings.add(mItem);
		// menuSettings.setMnemonic(KeyEvent.VK_S);
		// menuBar.add(menuSettings);

		JMenu menuHelp = new JMenu(language.getProperty("menuHelp"));
		menuHelp.setMnemonic(KeyEvent.VK_H);
		mItem = new JMenuItem(allActions.get("About"));
		mItem.setText(language.getProperty("menuAbout"));
		menuHelp.add(mItem);

		menuBar.add(menuHelp);
		this.setJMenuBar(menuBar);

		popupMenuLeft1 = new JPopupMenu();
		popupMenuLeft1.add(new JMenuItem(new MyAbstractAction("Insert element above", this,
				MyAbstractAction.INSERT_ELEMENT_ABOVE1)));
		popupMenuLeft1.add(new JMenuItem(new MyAbstractAction("Insert element below", this,
				MyAbstractAction.INSERT_ELEMENT_BELOW1)));
		popupMenuLeft1
				.add(new JMenuItem(new MyAbstractAction("Delete Element", this, MyAbstractAction.DELETE_ELEMENT1)));
		popupMenuLeft1.add(new JPopupMenu.Separator());
		popupMenuLeft1
				.add(new JMenuItem(new MyAbstractAction("Add bookmark", this, MyAbstractAction.ADD_BOOKMARK_LEFT)));

		popupMenuLeft2 = new JPopupMenu();
		popupMenuLeft2.add(new JMenuItem(new MyAbstractAction("Insert element above", this,
				MyAbstractAction.INSERT_ELEMENT_ABOVE1)));
		popupMenuLeft2.add(new JMenuItem(new MyAbstractAction("Insert element below", this,
				MyAbstractAction.INSERT_ELEMENT_BELOW1)));
		popupMenuLeft2
				.add(new JMenuItem(new MyAbstractAction("Delete Element", this, MyAbstractAction.DELETE_ELEMENT1)));
		popupMenuLeft2.add(new JPopupMenu.Separator());
		popupMenuLeft2.add(new JMenuItem(new MyAbstractAction("Edit bookmark", this,
				MyAbstractAction.EDIT_BOOKMARK_LEFT)));
		popupMenuLeft2.add(new JMenuItem(new MyAbstractAction("Remove bookmark", this,
				MyAbstractAction.REMOVE_BOOKMARK_LEFT)));

		popupMenuRight1 = new JPopupMenu();
		popupMenuRight1.add(new JMenuItem(new MyAbstractAction("Insert element above", this,
				MyAbstractAction.INSERT_ELEMENT_ABOVE2)));
		popupMenuRight1.add(new JMenuItem(new MyAbstractAction("Insert element below", this,
				MyAbstractAction.INSERT_ELEMENT_BELOW2)));
		popupMenuRight1.add(new JMenuItem(
				new MyAbstractAction("Delete Element", this, MyAbstractAction.DELETE_ELEMENT2)));
		popupMenuRight1.addSeparator();
		popupMenuRight1.add(new JMenuItem(new MyAbstractAction("Add bookmark", this,
				MyAbstractAction.ADD_BOOKMARK_RIGHT)));

		popupMenuRight2 = new JPopupMenu();
		popupMenuRight2.add(new JMenuItem(new MyAbstractAction("Insert element above", this,
				MyAbstractAction.INSERT_ELEMENT_ABOVE2)));
		popupMenuRight2.add(new JMenuItem(new MyAbstractAction("Insert element below", this,
				MyAbstractAction.INSERT_ELEMENT_BELOW2)));
		popupMenuRight2.add(new JMenuItem(
				new MyAbstractAction("Delete Element", this, MyAbstractAction.DELETE_ELEMENT2)));
		popupMenuRight2.addSeparator();
		popupMenuRight2.add(new JMenuItem(new MyAbstractAction("Edit bookmark", this,
				MyAbstractAction.EDIT_BOOKMARK_RIGHT)));
		popupMenuRight2.add(new JMenuItem(new MyAbstractAction("Remove bookmark", this,
				MyAbstractAction.REMOVE_BOOKMARK_RIGHT)));
	}

	/**
	 * Pomoću ove metode se kreiraju i pohranjuju u mapu one akcije koje se
	 * pozivaju s više mjesta u programu kako bi se izbjeglo višestruko
	 * stvaranje istih akcija i nepotrebno trošenje memorije.
	 */
	private void createActions() {
		allActions = new HashMap<String, Action>();

		Action actionOpen = new MyAbstractAction("Open", this, MyAbstractAction.OPEN_PROJECT);
		allActions.put("Open", actionOpen);

		Action actionSave = new MyAbstractAction("Save", this, MyAbstractAction.SAVE_PROJECT);
		allActions.put("Save", actionSave);

		Action actionStats = new MyAbstractAction("Statistics", this, MyAbstractAction.STATS_WINDOW);
		allActions.put("Statistics", actionStats);

		Action actionAlg1on1 = new MyAbstractAction("Alg1on1", this, MyAbstractAction.ALG_1ON1);
		allActions.put("Alg1on1", actionAlg1on1);

		Action actionCreateProject = new MyAbstractAction("CreateProject", this, MyAbstractAction.NEW_PROJ_WINDOW);
		allActions.put("CreateProject", actionCreateProject);

		Action actionAbout = new MyAbstractAction("About", this, MyAbstractAction.ABOUT_WINDOW);
		allActions.put("About", actionAbout);

		Action actionExit = new MyAbstractAction("Exit", this, MyAbstractAction.DO_EXIT);
		allActions.put("Exit", actionExit);

		Action actionEditComment = new MyAbstractAction("EditComment", this, MyAbstractAction.EDIT_COMMENT);
		allActions.put("EditComment", actionEditComment);

		Action actionShowBookmarks = new MyAbstractAction("ShowBookmarks", this, MyAbstractAction.TGLBTN_BOOKMARKS);
		allActions.put("ShowBookmarks", actionShowBookmarks);
	}

	/**
	 * Metoda pomoću koje se aktiviraju/deaktiviraju sve komponente koje se
	 * smiju koristiti tek nakon učitavanja projekta.
	 * 
	 * @param state
	 *            <code>true</code> ako se želi aktivirati komponente, a
	 *            <code>false</code> ako ih se želi deaktivirati.
	 */
	public void enableComponents(boolean state) {
		jTglBtnNormalScroll.setEnabled(state);
		jTglBtnScrollTogether.setEnabled(state);
		jTglBtnLeftFind.setEnabled(state);
		jTglBtnRightFind.setEnabled(state);

		jBtnLeftSplit.setEnabled(state);
		jBtnLeftMerge.setEnabled(state);
		jBtnRefresh.setEnabled(state);
		jBtnShiftConDown.setEnabled(state);
		jButton4.setEnabled(state);
		jBtnMerge.setEnabled(state);
		// jBtnUndo.setEnabled(state);
		jButton7.setEnabled(state);
		jButton8.setEnabled(state);
		jButton9.setEnabled(state);
		jBtnSaveProject.setEnabled(state);
		jButton17.setEnabled(state);
		// jBtnRedo.setEnabled(state);
		jTglBtnAutoSplitAllLeft.setEnabled(state);
		jTglBtnAutoSplitAllRight.setEnabled(state);
		jTglBtnBookmarks.setEnabled(state);
		jTglBtnDetails.setEnabled(state);

		menuProject.setEnabled(state);
		menuEdit.setEnabled(state);
		menuExport.setEnabled(state);
		mItemSave.setEnabled(state);
		mItemSaveAs.setEnabled(state);
	}

	/** 
	 * Metoda koja sakriva sve panele ispod lijeve tablice.
	 * @param except Panel koji se ne sakriva.
	 */
	public void hideLeftSubPanes(String except) {

		if (!except.equals("find")) {
			jToolBar5.setVisible(false);
			jPanel8.remove(jToolBar5);
			jTglBtnLeftFind.setSelected(false);
		}

		if (!except.equals("split")) {
			jToolBar7.setVisible(false);
			jPanel8.remove(jToolBar7);
			jTglBtnAutoSplitAllLeft.setSelected(false);
		}

		if (!except.equals("details")) {
			jToolBar9.setVisible(false);
			jPanel8.remove(jToolBar9);
			jTglBtnDetails.setSelected(false);
		}
	}

	/** 
	 * Metoda koja sakriva sve panele ispod desne tablice.
	 * @param except Panel koji se ne sakriva.
	 */
	public void hideRightSubPanes(String except) {
		if (!except.equals("find")) {
			jToolBar6.setVisible(false);
			jPanelRightFind.remove(jToolBar6);
			jTglBtnRightFind.setSelected(false);
		}

		if (!except.equals("split")) {
			jToolBar8.setVisible(false);
			jPanelRightFind.remove(jToolBar8);
			jTglBtnAutoSplitAllRight.setSelected(false);
		}

		if (!except.equals("details")) {
			jToolBar10.setVisible(false);
			jPanelRightFind.remove(jToolBar10);
			jTglBtnDetails.setSelected(false);
		}
	}

	/**
	 * Metoda koja se poziva kada se promijeni vrijednost lijevoj tablici.
	 * @param e Event promijene vrijednosti.
	 */
	public void leftDetailsTableValueChanged(ListSelectionEvent e) {

		int row = jTable1.getSelectedRow();
		if (row != -1) {
			int id = ((Sentence) jTable1Model.getValueAt(row, 0)).getId();
			Map<String, String> attributes = dataModel.getAttributesForElement(id);
			
			while (leftDetailsTableModel.getRowCount() > 0) {
				leftDetailsTableModel.removeRow(0);
			}
			
			leftDetailsTableModel.addRow(new String[] { "Paragraph", String.valueOf(dataModel.getParagraphForElement(id)) });
			
			if (attributes != null) {
				Set<String> keys = attributes.keySet();

				for (String key : keys) {
					leftDetailsTableModel.addRow(new String[] { key, attributes.get(key) });
				}
			}

		}
		leftDetailsTable.validate();
		leftDetailsTable.repaint();

	}

	/**
	 * Metoda koja se poziva kada se promijeni vrijednost desnoj tablici.
	 * @param e Event promijene vrijednosti.
	 */
	public void rightDetailsTableValueChanged(ListSelectionEvent e) {

		int row = jTable2.getSelectedRow();
		if (row != -1) {

			int id = ((Sentence) jTable2Model.getValueAt(row, 0)).getId();
			Map<String, String> attributes = dataModel.getAttributesForElement(id);
			
			while (rightDetailsTableModel.getRowCount() > 0) {
				rightDetailsTableModel.removeRow(0);
			}
			
			rightDetailsTableModel.addRow(new String[] { "Paragraph", String.valueOf(dataModel.getParagraphForElement(id)) });
			
			if (attributes != null) {
				Set<String> keys = attributes.keySet();

				for (String key : keys) {
					rightDetailsTableModel.addRow(new String[] { key, attributes.get(key) });
				}
			}
		}
		// leftDetailsTable.validate();
		// leftDetailsTable.repaint();

	}

	/**
	 * Metoda pomoću koje se postavlja ime projekta te podaci o učitanim
	 * datotekama i njihovim jezicima.
	 * 
	 * @param projectName
	 *            Ime projekta.
	 * @param lang1
	 *            Jezik prve datoteke.
	 * @param lang2
	 *            Jezik druge datoteke.
	 * @param file1
	 *            Lokacija prve datoteke na datotečnom sustavu računala.
	 * @param file2
	 *            Lokacija druge datoteke na datotečnom sustavu računala.
	 */
	public void setProjectProperties(String projectName, String lang1, String lang2, String file1, String file2) {
		dataModel.setProperty("projectName", projectName);
		dataModel.setProperty("language1", lang1);
		dataModel.setProperty("language2", lang2);
		dataModel.setProperty("file1", file1);
		dataModel.setProperty("file2", file2);
	}

	/**
	 * Ova metoda čita iz podatkovnog modela podatke o imenu projekta te
	 * učitanim datotekama i njihovim jezicima te ih ispisuje na grafičko
	 * sučelje.
	 */
	public void loadProjectProperties() {
		setTitle(dataModel.getProperty("projectName") + " - CORAL " + version);
		langLabel1.setText(MainWindow.language.getProperty("labelLanguage") + dataModel.getProperty("language1"));
		langLabel2.setText(MainWindow.language.getProperty("labelLanguage") + dataModel.getProperty("language2"));
		fileLabel1.setText(dataModel.getProperty("file1"));
		fileLabel1.setToolTipText(dataModel.getProperty("file1"));
		fileLabel2.setText(dataModel.getProperty("file2"));
		fileLabel2.setToolTipText(dataModel.getProperty("file2"));
	}

	protected void jButton13ActionPerformed(ActionEvent evt) {
		keyListener2.next();

	}

	protected void jToggleButton4ActionPerformed(ActionEvent evt) {
		if (jTglBtnRightFind.isSelected()) {
			jToolBar6.setVisible(true);
		} else {
			jToolBar6.setVisible(false);
		}

	}

	/**
	 * Metoda koja repozicionira ikone u središnjoj komponenti prema trenutno
	 * vidljivim elementima lijeve i desne tablice. Ova metoda se poziva pri
	 * svakom klizanju (scroll) neke od tablice, dodavanja/brisanja elemenata
	 * ili mijenjanja veličine prozora.
	 */
	public void reDrawVisibleIcons() {

		Sentence tmpSentence = null;

		shouldBeVisibleIcons.clear();

		// left side icons
		JScrollBar2 sb = (JScrollBar2) jScrollPane1.getVerticalScrollBar();
		JViewport vp = jScrollPane1.getViewport();

		Point p = jTable1.getVisibleRect().getLocation();
		int firstVisibleRow = jTable1.rowAtPoint(p);
		int lastVisibleRow = jTable1.rowAtPoint(new Point(0, sb.getValue() + vp.getExtentSize().height)) - 1;
		if (lastVisibleRow < 0) {
			lastVisibleRow = jTable1.getRowCount() - 1;
		}

		for (int i = firstVisibleRow - 2; i <= lastVisibleRow + 1; i++) {
			int y = jTable1.getCellRect(i, 0, false).getLocation().y - sb.getValue();

			int rowHeight = jTable1.getRowHeight(i);

			if ((i >= 0) && (i <= jTable1Model.getRowCount() - 1)) {
				tmpSentence = (Sentence) jTable1Model.getValueAt(i, 0);
				tmpSentence.getIcon().setLocation(10, y + rowHeight / 2 - 5 - 5);
				// tmpSentence.getIcon().setOffset(-sb.getValue());
				tmpSentence.getIcon().setVisible(true);
				shouldBeVisibleIcons.add(tmpSentence.getIcon());
				visibleIcons.add(tmpSentence.getIcon());
			}
		}

		// Iterator<Icon> it = visibleIcons.iterator();
		// while (it.hasNext()) {
		// Icon tmpIcon = it.next();
		// if (!shouldBeVisibleIcons.contains(tmpIcon)) {
		// tmpIcon.setVisible(false);
		// it.remove();
		// }
		// }
		//
		// shouldBeVisibleIcons.clear();

		// right side icons
		sb = (JScrollBar2) jScrollPane2.getVerticalScrollBar();
		vp = jScrollPane2.getViewport();

		p = jTable2.getVisibleRect().getLocation();
		firstVisibleRow = jTable2.rowAtPoint(p);
		lastVisibleRow = jTable2.rowAtPoint(new Point(0, sb.getValue() + vp.getExtentSize().height)) - 1;
		if (lastVisibleRow < 0) {
			lastVisibleRow = jTable2.getRowCount() - 1;
		}

		for (int i = firstVisibleRow - 2; i <= lastVisibleRow + 1; i++) {
			int y = jTable2.getCellRect(i, 0, false).getLocation().y - sb.getValue();

			int rowHeight = jTable2.getRowHeight(i);

			if ((i >= 0) && (i <= jTable2Model.getRowCount() - 1)) {
				tmpSentence = (Sentence) jTable2Model.getValueAt(i, 0);
				tmpSentence.getIcon().setLocation(switchBoardWidth - 35, y + rowHeight / 2 - 5 - 5);
				// tmpSentence.getIcon().setOffset(-sb.getValue());
				tmpSentence.getIcon().setVisible(true);
				shouldBeVisibleIcons.add(tmpSentence.getIcon());
				visibleIcons.add(tmpSentence.getIcon());
			}
		}

		Iterator<Icon> it2 = visibleIcons.iterator();
		while (it2.hasNext()) {
			Icon tmpIcon = it2.next();
			if (!shouldBeVisibleIcons.contains(tmpIcon)) {
				tmpIcon.setVisible(false);
				it2.remove();
			}
		}
		sboard.repaint();
	}

	/**
	 * Metoda koja pri mijenjanju veličine prozora pozicionira ikone koje
	 * trenutno ne bi trebale biti vidljive izvan vidljivih granica središnje
	 * komponente. To je potrebno raditi jer je moguće da se prilikom
	 * povećavanja prozora na središnjoj komponenti pojave neke ikne obližnjih
	 * elemenata koje ne bi trebale biti vidljive.
	 */
	public void hideIcons() {

		Sentence tmpSentence = null;

		// left side icons
		for (int i = 0; i < jTable1.getRowCount(); i++) {
			tmpSentence = (Sentence) jTable1Model.getValueAt(i, 0);
			tmpSentence.getIcon().setLocation(10, -50);
		}

		// right side icons
		for (int i = 0; i < jTable2.getRowCount(); i++) {
			tmpSentence = (Sentence) jTable2Model.getValueAt(i, 0);
			tmpSentence.getIcon().setLocation(125, -50);
		}
	}

	/**
	 * Metoda pomoću koje se u grafičko sučelje (na kraj lijeve tablice) dodaje
	 * element sadržan u podatkovnom modelu. Ova metoda se poziva nakon što je u
	 * podatkovni model dodan element.
	 * 
	 * @param id
	 *            ID elementa koji se dodaje.
	 */
	public void addSentenceLeft(int id) {
		iconsExist = true;
		Icon icon = sboard.addLeftSideIcon(10, 99999999, id);
		icon.addPropertyChangeListener(this);
		icon.setVisible(false);
		Sentence tmpSentence = new Sentence(dataModel.getElement(id), id, icon);
		jTable1Model.addRow(new Object[] { tmpSentence });
		// leftSentences.add(id, tmpSentence);
		leftRowWithId.put(id, jTable1Model.getRowCount() - 1);
		leftIconsByID.put(id, icon);
	}


	/**
	 * Metoda pomoću koje se u lijevu tablicu umeće element sadržan u
	 * podatkovnom modelu. Ova metoda se poziva nakon što je u podatkovnom
	 * modelu u skup lijevih elemenata umetnut novi element.
	 * 
	 * @param id
	 *            ID elementa koji se dodaje.
	 * @param row
	 *            Index retka tablice u koji se dodaje element.
	 */
	public void addSentenceLeft(int id, int row) {
		iconsExist = true;
		Sentence sentenceBefore = (Sentence) jTable1Model.getValueAt(row, 0);
		Icon iconBefore = sentenceBefore.getIcon();
		Icon icon = sboard.insertLeftSideIcon(10, 99999999, iconBefore, id);
		icon.setVisible(false);
		icon.addPropertyChangeListener(this);
		Sentence tmpSentence = new Sentence(dataModel.getElement(id), id, icon);
		jTable1Model.insertRow(row, new Object[] { tmpSentence });
		leftRowWithId.put(id, row);
		leftIconsByID.put(id, icon);
		renewLeftRowMap();
	}

	/**
	 * Metoda pomoću koje se u lijevu tablicu umeće element sadržan u
	 * podatkovnom modelu.
	 * 
	 * @param id
	 *            ID elementa koji se dodaje.
	 * @param row
	 *            Index retka tablice nakon kojega se dodaje element.
	 */
	public void addSentenceAfter1(int id, int row) {
		iconsExist = true;
		Sentence sentenceBefore = (Sentence) jTable1Model.getValueAt(row, 0);
		Icon iconBefore = sentenceBefore.getIcon();
		// Icon icon = sboard.addLeftSideIcon(10, 99999999, id);
		Icon icon = sboard.insertLeftSideIcon(10, 99999999, iconBefore, id);
		icon.addPropertyChangeListener(this);
		Sentence tmpSentence = new Sentence(dataModel.getElement(id), id, icon);
		jTable1Model.insertRow(row + 1, new Object[] { tmpSentence });
		leftRowWithId.put(id, row + 1);
		leftIconsByID.put(id, icon);
		renewLeftRowMap();
	}

	/**
	 * Metoda pomoću koje se iz lijeve tablice uklanja element. Ova metoda se
	 * poziva nakon što se određeni element ukloni iz podatkovnog modela.
	 * 
	 * @param id
	 *            ID rečenice koja se uklanja.
	 */
	public void removeLeftSentence(int id) {
		int row = leftRowWithId.get(id);
		Sentence sentence = (Sentence) jTable1Model.getValueAt(row, 0);
		Icon icon = sentence.getIcon();
		icon.removePropertyChangeListener(this);
		sboard.removeLeftSideIcon(icon);
		jTable1Model.removeRow(row);
		leftRowWithId.remove(id);
		leftIconsByID.remove(icon);
		renewLeftRowMap();
		reDrawVisibleIcons();
	}

	/**
	 * Metoda pomoću koje se iz desne tablice uklanja element. Ova metoda se
	 * poziva nakon što se određeni element ukloni iz podatkovnog modela.
	 * 
	 * @param id
	 *            ID rečenice koja se uklanja.
	 */
	public void removeRightSentence(int id) {
		int row = rightRowWithId.get(id);
		Sentence sentence = (Sentence) jTable2Model.getValueAt(row, 0);
		Icon icon = sentence.getIcon();
		icon.removePropertyChangeListener(this);
		sboard.removeRightSideIcon(icon);
		jTable2Model.removeRow(row);
		rightRowWithId.remove(id);
		rightIconsByID.remove(icon);
		renewRightRowMap();
		reDrawVisibleIcons();
	}

	/**
	 * Metoda pomoću koje se mijenja sadržaj određenog elementa lijeve tablice.
	 * Ova metoda se poziva nakon što je u podatkovnom modelu izmijenjen sadržaj
	 * nekog elementa.
	 * 
	 * @param id
	 *            ID elementa koji je izmijenjen.
	 */
	public void updateLeftSentence(int id) {
		int row = leftRowWithId.get(id);
		Sentence sent = (Sentence) jTable1Model.getValueAt(row, 0);
		sent.setSentence(dataModel.getElement(id));
		jTable1.repaint();
	}

	/**
	 * Metoda pomoću koje se mijenja sadržaj određenog elementa desne tablice.
	 * Ova metoda se poziva nakon što je u podatkovnom modelu izmijenjen sadržaj
	 * nekog elementa.
	 * 
	 * @param id
	 *            ID elementa koji je izmijenjen.
	 */
	public void updateRightSentence(int id) {
		int row = rightRowWithId.get(id);
		Sentence sent = (Sentence) jTable2Model.getValueAt(row, 0);
		sent.setSentence(dataModel.getElement(id));
		jTable2.repaint();
	}

	/**
	 * Metoda pomoću koje se u grafičko sučelje (na kraj desne tablice) dodaje
	 * element sadržan u podatkovnom modelu. Ova metoda se poziva nakon što je u
	 * podatkovni model dodan element.
	 * 
	 * @param id
	 *            ID elementa koji se dodaje.
	 */
	public void addSentenceRight(int id) {
		iconsExist = true;
		Icon icon = sboard.addRightSideIcon(500, 99999999, id);
		icon.addPropertyChangeListener(this);
		icon.setVisible(false);
		Sentence tmpSentence = new Sentence(dataModel.getElement(id), id, icon);
		jTable2Model.addRow(new Object[] { tmpSentence });
		rightRowWithId.put(id, jTable2Model.getRowCount() - 1);
		rightIconsByID.put(id, icon);
	}

	/**
	 * Metoda pomoću koje se u desno tablicu umeće element sadržan u podatkovnom
	 * modelu. Ova metoda se poziva nakon što je u podatkovnom modelu u skup
	 * desnih elemenata umetnut novi element.
	 * 
	 * @param id
	 *            ID elementa koji se dodaje.
	 * @param row
	 *            Index retka tablice u koji se dodaje element.
	 */
	public void addSentenceRight(int id, int row) {
		iconsExist = true;
		Sentence sentenceBefore = (Sentence) jTable2Model.getValueAt(row, 0);
		Icon iconBefore = sentenceBefore.getIcon();
		// Icon icon = sboard.addRightSideIcon(500, 99999999, id);
		Icon icon = sboard.insertRightSideIcon(500, 99999999, iconBefore, id);
		icon.addPropertyChangeListener(this);
		icon.setVisible(false);
		Sentence tmpSentence = new Sentence(dataModel.getElement(id), id, icon);
		jTable2Model.insertRow(row, new Object[] { tmpSentence });
		rightRowWithId.put(id, row);
		rightIconsByID.put(id, icon);
		renewRightRowMap();
	}

	/**
	 * Metoda pomoću koje se u desnu tablicu umeće element sadržan u podatkovnom
	 * modelu.
	 * 
	 * @param id
	 *            ID elementa koji se dodaje.
	 * @param row
	 *            Index retka tablice nakon kojega se dodaje element.
	 */
	public void addSentenceAfter2(int id, int row) {
		iconsExist = true;
		Sentence sentenceBefore = (Sentence) jTable2Model.getValueAt(row, 0);
		Icon iconBefore = sentenceBefore.getIcon();
		// Icon icon = sboard.addRightSideIcon(500, 99999999, id);
		Icon icon = sboard.insertRightSideIcon(500, 99999999, iconBefore, id);
		icon.addPropertyChangeListener(this);
		Sentence tmpSentence = new Sentence(dataModel.getElement(id), id, icon);
		jTable2Model.insertRow(row + 1, new Object[] { tmpSentence });
		rightRowWithId.put(id, row + 1);
		rightIconsByID.put(id, icon);
		renewRightRowMap();
	}

	/**
	 * Metoda koja osvježava mapu s podatcima o povezanosti ID-eva desnih
	 * elemenata i retcima u kojima se nalaze. Poziva se nakon umetanja
	 * elemenata u lijevu tablicu.
	 */
	private void renewLeftRowMap() {
		for (int i = 0; i < jTable1.getRowCount(); i++) {
			Sentence tmpSentence = (Sentence) jTable1Model.getValueAt(i, 0);
			leftRowWithId.put(tmpSentence.getId(), i);
		}
	}

	/**
	 * Metoda koja osvježava mapu s podatcima o povezanosti ID-eva desnih
	 * elemenata i retcima u kojima se nalaze. Poziva se nakon umetanja
	 * elemenata u desnu tablicu.
	 */
	private void renewRightRowMap() {
		for (int i = 0; i < jTable2.getRowCount(); i++) {
			Sentence tmpSentence = (Sentence) jTable2Model.getValueAt(i, 0);
			rightRowWithId.put(tmpSentence.getId(), i);
		}
	}

	/**
	 * Metoda koja omogućuje ručno dijeljenje lijevih elemenata. Metoda podijeli
	 * na dva dijela onaj element koji se pri pozivu metode nalazi u modu za
	 * editiranje. Djeljenje se vrši na lokaciji kursora. Ukoliko niti jedna
	 * čelija tablice nije u edit modeu, ova metoda ne radi ništa.
	 */
	public void splitLeftSentence() {
		int selectedRow = jTable1.getSelectedRow();
		Sentence tmpSentence = null;
		JTextArea textArea = (JTextArea) jTable1.getEditorComponent();

		if (textArea != null) {
			jTable1.getCellEditor(selectedRow, 0).stopCellEditing();

			tmpSentence = (Sentence) jTable1Model.getValueAt(selectedRow, 0);
			int newId = dataModel.splitElement(tmpSentence.getId(), textArea.getCaretPosition(), true);

			// jTable1Model.setValueAt(new Sentence(dataModel
			// .getElement(tmpSentence.getId()), tmpSentence.getId(),
			// tmpSentence.getIcon()), selectedRow, 0);
			// addSentenceLeft(newId, selectedRow + 1);
			statusLabel.setText("Splitted sentence #" + tmpSentence.getId() + " into sentences #" + tmpSentence.getId()
					+ " and #" + newId);

		} else {
			statusLabel.setText("Nothing to split...");
		}
	}

	/**
	 * Metoda koja omogućuje ručno dijeljenje desnih elemenata. Metoda podijeli
	 * na dva dijela onaj element koji se pri pozivu metode nalazi u modu za
	 * editiranje. Djeljenje se vrši na lokaciji kursora. Ukoliko niti jedna
	 * čelija tablice nije u edit modeu, ova metoda ne radi ništa.
	 */
	public void splitRightSentence() {
		int selectedRow = jTable2.getSelectedRow();
		Sentence tmpSentence = null;
		JTextArea textArea = (JTextArea) jTable2.getEditorComponent();

		if (textArea != null) {
			jTable2.getCellEditor(selectedRow, 0).stopCellEditing();

			tmpSentence = (Sentence) jTable2Model.getValueAt(selectedRow, 0);
			int newId = dataModel.splitElement(tmpSentence.getId(), textArea.getCaretPosition(), true);

			statusLabel.setText("Splitted sentence #" + tmpSentence.getId() + " into sentences #" + tmpSentence.getId()
					+ " and #" + newId);

		} else {
			statusLabel.setText("Nothing to split...");
		}
		reDrawVisibleIcons();
	}

	/**
	 * Metoda koja sve označene elemente lijeve tablice spaja u jedan element.
	 * Ukoliko nije označen niti jedan element ili je označen samo jedan
	 * element, ova metoda ne radi ništa.
	 */
	public void mergeLeftSentences() {
		int[] selectedRows = jTable1.getSelectedRows();

		if (selectedRows.length > 1) {
			Sentence tmpSentence = (Sentence) jTable1Model.getValueAt(selectedRows[0], 0);

			while (jTable1.getSelectedRowCount() > 1) {
				Sentence tmpSentence1 = (Sentence) jTable1Model.getValueAt(selectedRows[1], 0);
				dataModel.combineElements(tmpSentence.getId(), tmpSentence1.getId());
			}
			statusLabel.setText("Merged sentences");
		} else {
			statusLabel.setText("Nothing to merge...");
		}
		jTable1.repaint();
	}

	/**
	 * Metoda koja sve označene elemente desne tablice spaja u jedan element.
	 * Ukoliko nije označen niti jedan element ili je označen samo jedan
	 * element, ova metoda ne radi ništa.
	 */
	public void mergeRightSentences() {
		int[] selectedRows = jTable2.getSelectedRows();

		if (selectedRows.length > 1) {
			Sentence tmpSentence = (Sentence) jTable2Model.getValueAt(selectedRows[0], 0);

			while (jTable2.getSelectedRowCount() > 1) {
				Sentence tmpSentence1 = (Sentence) jTable2Model.getValueAt(selectedRows[1], 0);
				dataModel.combineElements(tmpSentence.getId(), tmpSentence1.getId());
			}
			statusLabel.setText("Merged sentences");
		} else {
			statusLabel.setText("Nothing to merge...");
		}
	}

	/**
	 * Metoda koja uklanja sve postojeće veze među elementima.
	 */
	public void removeAllConnections() {
		sboard.removeAllConnections();
	}

	/**
	 * Pomoću ove metode se obavlja automatska segmentacija na rečenice svih
	 * lijevih elemenata. Pri segmentaciji se koristi datoteka s popisom kratica
	 * za određeni jezik koje se ne mogu naći na kraju rečenice. Koristi se ona
	 * datoteka koju je korisnik odabrao u grafičkom sučelju prilikom pokretanja
	 * segmentacije.
	 */
	public void autoSplitLeft() {
		SentenceSegmentation segmentation = new SentenceSegmentation();
		List<Integer> list1 = dataModel.getKeys1();
		try {
			segmentation.loadAbbreviations((String) jComboBox1.getSelectedItem());
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Encoding not supported",
					JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "File error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
		}

		for (int i = 0; i < list1.size(); i++) {
			// dohvati tekst ključa koji se nalazi na i-toj poziciji liste
			int key = list1.get(i);
			String text = dataModel.getElement(key);
			int splitPosition = segmentation.splitSearch(text);
			if (splitPosition == -1) {
				continue;
			}
			dataModel.splitElement(key, splitPosition + 1, segmentation.safe);
			//System.out.println(segmentation.safe + " " + i);
			if (segmentation.safe == false) {
				leftNotSure.add(leftRowWithId.get(key));
			}

			segmentation.safe = true;
		}
	}

	/**
	 * Pomoću ove metode se obavlja automatska segmentacija na rečenice svih
	 * desnih elemenata. Pri segmentaciji se koristi datoteka s popisom kratica
	 * za određeni jezik koje se ne mogu naći na kraju rečenice. Koristi se ona
	 * datoteka koju je korisnik odabrao u grafičkom sučelju prilikom pokretanja
	 * segmentacije.
	 */
	public void autoSplitRight() {
		SentenceSegmentation segmentation = new SentenceSegmentation();
		List<Integer> list2 = dataModel.getKeys2();
		try {
			segmentation.loadAbbreviations((String) jComboBox2.getSelectedItem());
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Encoding not supported",
					JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "File error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
		}

		for (int i = 0; i < list2.size(); i++) {
			// dohvati tekst ključa koji se nalazi na i-toj poziciji liste
			int key = list2.get(i);
			String text = dataModel.getElement(key);
			int splitPosition = segmentation.splitSearch(text);
			if (splitPosition == -1) {
				continue;
			}
			dataModel.splitElement(key, splitPosition + 1, segmentation.safe);
			//System.out.println(segmentation.safe + " " + i);
			if (segmentation.safe == false) {
				rightNotSure.add(rightRowWithId.get(key));
			}

			segmentation.safe = true;
		}
	}

	/**
	 * Metoda pomoću koje se otvara, prije spremljeni, projekt.
	 */
	public void openProject() {

		if (saveIfModified() == 1) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CORAL files (.coral)", "coral");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File(MainWindow.config.getProperty("lastOpenDir")));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				worker.startOpeningProject(chooser.getSelectedFile().getAbsolutePath());

				// try {
				// clear();
				//
				// TableColumnModel cmodel = jTable1.getColumnModel();
				// TextAreaRenderer textAreaRenderer = new
				// TextAreaRenderer(this, TextAreaRenderer.LEFT);
				// cmodel.getColumn(0).setCellRenderer(textAreaRenderer);
				// TextAreaEditor textEditor = new TextAreaEditor(dataModel);
				// cmodel.getColumn(0).setCellEditor(textEditor);
				//
				// TableColumnModel cmodel2 = jTable2.getColumnModel();
				// TextAreaRenderer textAreaRenderer2 = new
				// TextAreaRenderer(this, TextAreaRenderer.RIGHT);
				// cmodel2.getColumn(0).setCellRenderer(textAreaRenderer2);
				// TextAreaEditor textEditor2 = new TextAreaEditor(dataModel);
				// cmodel2.getColumn(0).setCellEditor(textEditor2);
				//
				// dataModel.addAll(SaveLoadProject.loadProject(chooser.getSelectedFile().getAbsolutePath()));
				// // dataModel.initializeTransients();
				// // dataModel.addUndoableEditListener(this);
				// // dataModel.addPropertyChangeListener(this);
				//
				// enableComponents(true);
				// loadProjectProperties();
				// reloadKeyListener();
				// stats = dataModel.getStatistics();
				//
				// } catch (Exception e) {
				// JOptionPane.showMessageDialog(this, "Unable to open selected
				// file.", "Open error",
				// JOptionPane.ERROR_MESSAGE);
				// // e.printStackTrace();
				// }
				// reloadElements();
				MainWindow.config.setProperty("lastOpenDir", chooser.getSelectedFile().getAbsolutePath());
				undoOff = false;
				lastSaveLocation = null;
			}
		}
	}

	/**
	 * Pomoću ove metode se učitava XML datoteka u kojoj su pohranjene
	 * vrijednosti mape nekog objekta <code>Properties</code> tipa.
	 * 
	 * @param file
	 *            Lokacija XML datoteke.
	 * @param props
	 *            varijabla tipa <code>Properties</code> u koju se učitavaju
	 *            vrijednosti iz XML datoteke.
	 */
	protected void loadPropertiesXML(String file, Properties props) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			props.loadFromXML(is);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Config or language file was not found.\n" + e.getLocalizedMessage(), "Load error",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		} catch (InvalidPropertiesFormatException e) {
			JOptionPane.showMessageDialog(this, "Config or language file is damaged.\n" + e.getLocalizedMessage(), "Load error",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Unable to load config or language file.\n" + e.getLocalizedMessage(), "Load error",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}

	/**
	 * Pomoću ove metode se sprema XML datoteka u kojoj su pohranjene
	 * vrijednosti mape nekog objekta <code>Properties</code> tipa.
	 * 
	 * @param file
	 *            Lokacija XML datoteke.
	 * @param props
	 *            varijabla tipa <code>Properties</code> iz koje se čitaju
	 *            vrijednosti i zapisuju u XML datoteku.
	 */
	private void savePropertiesXML(String file, Properties props) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			props.storeToXML(os, file);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Unable to save configuration data.\n" + e.getLocalizedMessage(), "Save error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Unable to save configuration data.\n" + e.getLocalizedMessage(), "Save error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Metoda koja u neki niz String-ova dodaje novi String. Ukoliko taj String
	 * več postoji u nizu, onda se ništa ne dodaje. Ako zadani niz već ima 10
	 * elemenata, onda se zadnji element briše, a novi String se dodaje na nulto
	 * mjesto.
	 * 
	 * @param array
	 *            Niz u koji se dodaje novi String,
	 * @param string
	 *            String koji se dodaje u niz.
	 * @return Novi niz s promijenjenim sadržajem.
	 */
	public String[] addToStringArray(String[] array, String string) {
		string = string.trim();
		String[] newArray = null;
		boolean found = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(string))
				found = true;
		}

		if (!found) {
			if (array.length < 10) {
				newArray = new String[array.length + 1];
				newArray[0] = string;
				for (int i = 0; i < array.length; i++) {
					newArray[i + 1] = array[i];
				}
			} else {
				newArray = new String[10];
				newArray[0] = string;
				for (int i = 0; i < 9; i++) {
					newArray[i + 1] = array[i];
				}
			}
		} else {
			newArray = array;
		}

		return newArray;
	}

	/**
	 * Metoda pomoću koje se pri gašenju programa pohranjuju vrijednosti o
	 * jezicima i encoding-ima koje je korisnik zadnje upotrebljavao.
	 */
	public void saveData() {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("userData.dat"));
			oos.writeObject(languages);
			oos.writeObject(encodings);
			oos.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Unable to save configuration data.\n" + e.getLocalizedMessage(), "Save error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Unable to save configuration data.\n" + e.getLocalizedMessage(), "Save error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Metoda pomoću koje se pri paljenju programa čitaju pohranjene vrijednosti
	 * o jezicima i encoding-ima koje je korisnik zadnje upotrebljavao.
	 */
	public void loadData() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream("userData.dat"));
			languages = (String[]) ois.readObject();
			encodings = (String[]) ois.readObject();
			ois.close();
		} catch (Exception ignorable) {
		}

	}

	/**
	 * Metoda koja, ukoliko su na projektu napravljene neke promjene, prikazuje
	 * dijalog s pitanjem za korisnika da li želi spremiti projekt.
	 * 
	 * @return Vrijednost koja reprezentira korisnikov odgovor.
	 */
	public int saveIfModified() {
		int ret = -1;
		if (askForSave) {

			Object[] options = { "Yes", "No", "Cancel" };
			int n = JOptionPane.showOptionDialog(this, "Save current project?", "Save project?",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

			switch (n) {
			case 0:
				int check = saveProject();
				if (check == JFileChooser.APPROVE_OPTION) {
					ret = 1;
				}
				break;

			case 1:
				ret = 1;
				break;

			default:
				break;
			}
		}
		return askForSave ? ret : 1;
	}

	/**
	 * Metoda koja se automatski poziva pri gašenju prozora. Pohranjuju se
	 * trenutne vrijednosti u configa u XML datoteku i pita se korisnika da li
	 * želi pohraniti promjene koje je napravio na projektu.
	 */
	@Override
	public void dispose() {
		savePropertiesXML("config.xml", config);

		if (askForSave) {

			Object[] options = { "Yes", "No", "Cancel" };
			int n = JOptionPane.showOptionDialog(this, "Save project before close?", "Close program",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

			switch (n) {
			case 0:
				int check = saveProject();
				if (check == JFileChooser.APPROVE_OPTION) {
					saveData();
					super.dispose();
				}
				break;

			case 1:
				saveData();
				super.dispose();
				break;

			default:
				break;
			}
		} else {
			saveData();
			super.dispose();
		}

	}

	private void jButton4ActionPerformed(ActionEvent evt) {
		splitRightSentence();
	}

	private void jToggleButton1ActionPerformed(ActionEvent evt) {
		if (jTglBtnNormalScroll.isSelected()) {
			config.put("scrollType", "normal");
			jTglBtnScrollTogether.setSelected(false);
		} else {
			config.put("scrollType", "together");
			jTglBtnScrollTogether.setSelected(true);
		}
	}

	private void jToggleButton2ActionPerformed(ActionEvent evt) {
		if (jTglBtnScrollTogether.isSelected()) {
			config.put("scrollType", "together");
			jTglBtnNormalScroll.setSelected(false);
		} else {
			config.put("scrollType", "normal");
			jTglBtnNormalScroll.setSelected(true);
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Metoda koja se automatski poziva kod promijene veličine prozora.
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		// this.paintAll(this.getGraphics());
		hideIcons();
		reDrawVisibleIcons();
		sboard.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	private void jButton3ActionPerformed(ActionEvent evt) {
		sboard.shiftSelectedConnectionsDown();
	}

	private void jButton7ActionPerformed(ActionEvent evt) {
		sboard.shiftSelectedConnectionsUp();
	}

	private void jButton5ActionPerformed(ActionEvent evt) {
		mergeRightSentences();
	}

	private void jButton8ActionPerformed(ActionEvent evt) {
		keyListener.next();
	}

	/**
	 * Metoda koja se automatski poziva kada neki od objekata pošalje obavijest
	 * o promijeni vrijednosti. U ovoj metodi se prate sve promjene u
	 * vrijednostima podatkovnog modela te se za svaku promjenu poduzimaju
	 * određene akcije kako bi prikaz u grafičkom sučelju ostao konzistentan s
	 * trenutnim stanjem podatkovnog modela.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		// System.out.println(evt.getPropertyName());

		if (evt.getSource().equals(dataModel)) {
			stats.addToLog(evt.getPropertyName());
			askForSave = true;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_UPDATED_IN_SET1)) {
			updateLeftSentence((Integer) evt.getNewValue());
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_UPDATED_IN_SET2)) {
			updateRightSentence((Integer) evt.getNewValue());
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_ADDED_IN_SET1)) {
			addSentenceLeft((Integer) evt.getNewValue());
			stats.elementsAdded1++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_ADDED_IN_SET2)) {
			addSentenceRight((Integer) evt.getNewValue());
			stats.elementsAdded2++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_INSERTED_IN_SET1)) {
			List<Integer> list = (ArrayList<Integer>) evt.getNewValue();
			int row = list.get(0);
			int id = list.get(1);
			addSentenceLeft(id, row);
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_INSERTED_IN_SET2)) {
			List<Integer> list = (ArrayList<Integer>) evt.getNewValue();
			int row = list.get(0);
			int id = list.get(1);
			addSentenceRight(id, row);
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_REMOVED_FROM_SET1)) {
			removeLeftSentence((Integer) evt.getOldValue());
			stats.elementsRemoved1++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_REMOVED_FROM_SET2)) {
			removeRightSentence((Integer) evt.getOldValue());
			stats.elementsRemoved2++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_SPLIT_IN_SET1)) {
			updateLeftSentence((Integer) evt.getOldValue());
			int row = leftRowWithId.get((Integer) evt.getOldValue());
			addSentenceAfter1((Integer) evt.getNewValue(), row);
			stats.splitedElements1++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_SPLIT_IN_SET2)) {
			updateRightSentence((Integer) evt.getOldValue());
			int row = rightRowWithId.get((Integer) evt.getOldValue());
			addSentenceAfter2((Integer) evt.getNewValue(), row);
			stats.splitedElements2++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENTS_COMBINED_IN_SET1)) {
			removeLeftSentence((Integer) evt.getOldValue());
			updateLeftSentence((Integer) evt.getNewValue());
			stats.mergedElements1++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENTS_COMBINED_IN_SET2)) {
			removeRightSentence((Integer) evt.getOldValue());
			updateRightSentence((Integer) evt.getNewValue());
			stats.mergedElements2++;
		}

		if (evt.getPropertyName().equals(DataModel.BOOKMARK_ADDED)) {
			Bookmark bookmark = (Bookmark) evt.getNewValue();
			int id = bookmark.getKey();

			if (bookmark.getSetNumber() == Bookmark.SET1) {
				rowsWithBookmarksLeft.add(leftRowWithId.get(id));
			} else {
				rowsWithBookmarksRight.add(rightRowWithId.get(id));
			}
			bookmarksById.put(id, bookmark);
			reloadBookmarks();
			stats.bookmarksAdded++;
		}

		if (evt.getPropertyName().equals(DataModel.BOOKMARK_REMOVED)) {
			Bookmark bookmark = (Bookmark) evt.getOldValue();
			int id = bookmark.getKey();

			if (bookmark.getSetNumber() == Bookmark.SET1) {
				if (rowsWithBookmarksLeft.contains(bookmark.getKey())) {
					rowsWithBookmarksLeft.remove(bookmark.getKey());
				}
			}
			if (bookmark.getSetNumber() == Bookmark.SET2) {
				if (rowsWithBookmarksRight.contains(bookmark.getKey())) {
					rowsWithBookmarksRight.remove(bookmark.getKey());
				}
			}

			if (bookmarksById.containsKey(id)) {
				bookmarksById.remove(id);
			}
			reloadBookmarks();
			stats.bookmarksRemoved++;
		}

		if (evt.getPropertyName().equals(DataModel.ELEMENT_REMOVED_FROM_SET1)) {
			sboard.remove(leftIconsByID.get(evt.getOldValue()));
			leftIconsByID.remove(evt.getOldValue());
			stats.elementsRemoved1++;
		}

		if (evt.getPropertyName().equals(DataModel.CONNECTION_ADDED)) {
			int id1 = (Integer) evt.getOldValue();
			int id2 = (Integer) evt.getNewValue();
			int row1 = leftRowWithId.get(id1);
			int row2 = rightRowWithId.get(id2);

			Sentence tmpSentence1 = (Sentence) jTable1Model.getValueAt(row1, 0);
			Sentence tmpSentence2 = (Sentence) jTable2Model.getValueAt(row2, 0);

			sboard.addConnection(tmpSentence1.getIcon(), tmpSentence2.getIcon());
			sboard.repaint();
			stats.createdConnections++;

		} else if (evt.getPropertyName().equals(DataModel.CONNECTION_REMOVED)) {
			int id1 = (Integer) evt.getOldValue();
			int id2 = (Integer) evt.getNewValue();
			int row1 = leftRowWithId.get(id1);
			int row2 = rightRowWithId.get(id2);

			Sentence tmpSentence1 = (Sentence) jTable1Model.getValueAt(row1, 0);
			Sentence tmpSentence2 = (Sentence) jTable2Model.getValueAt(row2, 0);

			sboard.removeConnection(tmpSentence1.getIcon(), tmpSentence2.getIcon());
			sboard.repaint();
			stats.removedConnections++;

		} else if (evt.getPropertyName().equals("ClosedCreateProjectWindow")) {
			// reloadElements();
			enableComponents(true);
			undoOff = false;
			this.repaint();
			reDrawVisibleIcons();

		} else if (evt.getPropertyName().equals("IconSelected")) {
			// Icon icon = (Icon) evt.getSource();
			// boolean selected = (Boolean) evt.getNewValue();
			// short side = icon.getSide();
			//			
			// if (selected) {
			// if (side == Icon.LEFT_SIDE) {
			// selectedIconsLeft.add(icon);
			// }
			// } else {
			// if (side == Icon.LEFT_SIDE) {
			// selectedIconsLeft.remove(icon);
			// }
			// }
			//			
			//			
			// jTable1.clearSelection();
			// for (Icon tmpIcon : selectedIconsLeft) {
			// int id = tmpIcon.getId();
			// int row = leftRowWithId.get(id);
			// jTable1.changeSelection(row, 0, true, false);
			// }

		} else {
			// ignore
		}
	}

	/**
	 * Metoda koja prazni cijelo grafičko sučelje i ponovo ga puni podacima iz
	 * podatkovnog modela.
	 */
	public void reloadElements() {
		// List<Integer> keys = dataModel.getKeys1();
		// for (int i = 0; i < keys.size(); i++) {
		// sboard.remove(leftIconsByID.get(keys.get(i)));
		// leftIconsByID.remove(keys.get(i));
		// }

		Collection<Icon> icons = leftIconsByID.values();
		for (Icon icon : icons) {
			sboard.remove(icon);
		}
		icons.removeAll(icons);
		icons = rightIconsByID.values();
		for (Icon icon : icons) {
			sboard.remove(icon);
		}
		icons.removeAll(icons);
		sboard.repaint();

		while (jTable1Model.getRowCount() > 0) {
			jTable1Model.removeRow(0);
		}
		while (jTable2Model.getRowCount() > 0) {
			jTable2Model.removeRow(0);
		}

		List<Integer> ids = dataModel.getKeys1();
		for (int i = 0; i < ids.size(); i++) {
			addSentenceLeft(ids.get(i));
		}

		ids = dataModel.getKeys2();
		for (int i = 0; i < ids.size(); i++) {
			addSentenceRight(ids.get(i));
		}

	}

	/**
	 * Metoda koja prazni popis knjižnih oznaka iz grafičkog sučelja i ponovo ga
	 * puni prema sadržaju podatkovnog modela.
	 */
	public void reloadBookmarks() {
		bookmarksById.clear();
		rowsWithBookmarksLeft.clear();
		rowsWithBookmarksRight.clear();

		List<Bookmark> bookmarks = dataModel.getBookmarks();

		while (jTableBookmarksModel.getRowCount() > 0) {
			jTableBookmarksModel.removeRow(0);
		}

		for (Bookmark bookmark : bookmarks) {
			jTableBookmarksModel.addRow(new Bookmark[] { bookmark });
			bookmarksById.put(bookmark.getKey(), bookmark);
			if (bookmark.getSetNumber() == Bookmark.SET1) {
				rowsWithBookmarksLeft.add(leftRowWithId.get(bookmark.getKey()));
			}
			if (bookmark.getSetNumber() == Bookmark.SET2) {
				rowsWithBookmarksRight.add(rightRowWithId.get(bookmark.getKey()));
			}
		}
	}

	/**
	 * Metoda koja pokreće eksport elemenata lijeve ili desne tablice u XML
	 * datoteku.
	 * 
	 * @param side
	 *            Oznaka lijeve ili desne tablice.
	 */
	public void exportXML(short side) {
		new AttributesWindow(this, side, AttributesWindow.XML);

		// JFileChooser chooser = new JFileChooser();
		// FileNameExtensionFilter filter = new FileNameExtensionFilter("XML
		// files (.xml)", "xml");
		// chooser.setFileFilter(filter);
		// chooser.setCurrentDirectory(new
		// File(MainWindow.config.getProperty("lastOpenDir")));
		// int returnVal = chooser.showSaveDialog(this);
		// if (returnVal == JFileChooser.APPROVE_OPTION) {
		// if (chooser.getFileFilter().getDescription().equals("XML files
		// (.xml)"))
		// fileExporter.xmlExporter(chooser.getSelectedFile().getAbsolutePath(),
		// side, FileExporter.XML);
		// else
		// fileExporter.xmlExporter(chooser.getSelectedFile().getAbsolutePath(),
		// side, FileExporter.ALL);
		// MainWindow.config.setProperty("lastOpenDir",
		// chooser.getSelectedFile().getAbsolutePath());
		// }
	}

	/**
	 * Metoda koja trenutno stanje projekta zapisuje u datoteku.
	 * 
	 * @return Odabir korisnika u prozoru za spremanje projekta.
	 */
	public int saveProject() {
		if (lastSaveLocation == null) {

			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CORAL files (.coral)", "coral");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File(MainWindow.config.getProperty("lastOpenDir")));
//			int returnVal = chooser.showSaveDialog(this);
			int returnVal;
			while (true) {
				returnVal = chooser.showSaveDialog(this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					if (chooser.getSelectedFile().exists()) {
						String[] options = { "Yes", "No" };
						int response = JOptionPane.showOptionDialog(this, chooser
								.getSelectedFile().getAbsolutePath()
								+ " already exists.\nDo you want to replace it?",
								"Save As", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);

						if (response == 0) {
							break;
						} else if (response == 1) {
							continue;
						}
					} else {
						break;
					}
				} else {
					return returnVal;
				}
			}
			
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					lastSaveLocation = chooser.getSelectedFile().getAbsolutePath();
					if (chooser.getFileFilter().getDescription().equals("CORAL files (.coral)"))
						SaveLoadProject.saveProject(lastSaveLocation, this.dataModel, SaveLoadProject.CORAL);
					else
						SaveLoadProject.saveProject(lastSaveLocation, this.dataModel, SaveLoadProject.ALL);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
				}
				askForSave = false;
				MainWindow.config.setProperty("lastOpenDir", chooser.getSelectedFile().getAbsolutePath());
			}
			return returnVal;
		} else {
			try {
				SaveLoadProject.saveProject(lastSaveLocation, this.dataModel, SaveLoadProject.CORAL);
				askForSave = false;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
			}
			return JFileChooser.APPROVE_OPTION;
		}
	}

	/**
	 * Metoda koja trenutno stanje projekta zapisuje u datoteku. Ova metoda
	 * uvijek prikazuje dijelog za odabir lokacije i imena datoteke u koju se
	 * sprema projekt.
	 * 
	 * @return Odabir korisnika u prozoru za spremanje projekta.
	 */
	public int saveAsProject() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CORAL files (.coral)", "coral");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File(MainWindow.config.getProperty("lastOpenDir")));
		//int returnVal = chooser.showSaveDialog(this);
		int returnVal;
		while (true) {
			returnVal = chooser.showSaveDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				if (chooser.getSelectedFile().exists()) {
					String[] options = { "Yes", "No" };
					int response = JOptionPane.showOptionDialog(this, chooser
							.getSelectedFile().getAbsolutePath()
							+ " already exists.\nDo you want to replace it?",
							"Save As", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);

					if (response == 0) {
						break;
					} else if (response == 1) {
						continue;
					}
				} else {
					break;
				}
			} else {
				return returnVal;
			}
		}
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				lastSaveLocation = chooser.getSelectedFile().getAbsolutePath();
				SaveLoadProject.saveProject(lastSaveLocation, this.dataModel, SaveLoadProject.CORAL);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
			}
			MainWindow.config.setProperty("lastOpenDir", chooser.getSelectedFile().getAbsolutePath());
			askForSave = false;
		}
		return returnVal;
	}

	/**
	 * Metoda koja pokreće eksport svih povezanih elemenata u TMX v1.4 format.
	 */
	public void exportTMX() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TMX files (.tmx)", "tmx");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File(MainWindow.config.getProperty("lastOpenDir")));
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (chooser.getFileFilter().getDescription().equals("TMX files (.tmx)"))
				try {
					fileExporter.exportTMX(chooser.getSelectedFile().getAbsolutePath(), FileExporter.TMX);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
				}
			else
				try {
					fileExporter.exportTMX(chooser.getSelectedFile().getAbsolutePath(), FileExporter.ALL);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
				}

			MainWindow.config.setProperty("lastOpenDir", chooser.getSelectedFile().getAbsolutePath());
		}

	}

	/**
	 * Metoda koja pokrece eksport svih povezanih elemenata u TEI format.
	 */
	public void exportTEI() {
		new AttributesWindow(this, (short) -1, AttributesWindow.TEI);

		// JFileChooser chooser = new JFileChooser();
		// FileNameExtensionFilter filter = new FileNameExtensionFilter("XML
		// files (.xml)", "xml");
		// chooser.setFileFilter(filter);
		// chooser.setCurrentDirectory(new
		// File(MainWindow.config.getProperty("lastOpenDir")));
		// int returnVal = chooser.showSaveDialog(this);
		// if (returnVal == JFileChooser.APPROVE_OPTION) {
		// if (chooser.getFileFilter().getDescription().equals("XML files
		// (.xml)"))
		// fileExporter.exportTEI(chooser.getSelectedFile().getAbsolutePath(),
		// FileExporter.XML);
		// else
		// fileExporter.exportTEI(chooser.getSelectedFile().getAbsolutePath(),
		// FileExporter.ALL);
		//
		// MainWindow.config.setProperty("lastOpenDir",
		// chooser.getSelectedFile().getAbsolutePath());
		// }
	}

	/**
	 * Metoda koja se automatski poziva kada se desi neka akcija koju je moguće
	 * poništiti.
	 */
	@Override
	public void undoableEditHappened(UndoableEditEvent evt) {
		UndoableEdit edit = evt.getEdit();

		if (!undoOff) {
			undoManager.addEdit(edit);
		}

		jBtnUndo.setEnabled(undoManager.canUndo());
		jBtnRedo.setEnabled(undoManager.canRedo());
		mItemUndo.setEnabled(undoManager.canUndo());
		mItemRedo.setEnabled(undoManager.canRedo());

	}

	/**
	 * Metoda koja key listenerima za pretraživanje lijeve i desne tablice
	 * predaje reference novih elemenata nakon što se one promijene pri
	 * otvaranju novog projekta.
	 */
	public void reloadKeyListener() {
		keyListener.setTable(jTable1);
		keyListener.setTableModel(jTable1Model);
		keyListener.setTextField(jTextField1);
		keyListener2.setTable(jTable2);
		keyListener2.setTableModel(jTable2Model);
		keyListener2.setTextField(jTextField2);

	}

	public void setStatus(String status) {
		statusLabel.setText(status);
	}

}
