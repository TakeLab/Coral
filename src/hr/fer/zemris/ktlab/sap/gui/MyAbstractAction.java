package hr.fer.zemris.ktlab.sap.gui;

import hr.fer.zemris.ktlab.sap.algorithms.OneOnOne;
import hr.fer.zemris.ktlab.sap.algorithms.connect.MasterController;
import hr.fer.zemris.ktlab.sap.gui.windows.AboutWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.CreateProjectWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.EditBookmarkWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.EditProjectWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.GaleChurchWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.SettingsWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.StatisticsWindow;
import hr.fer.zemris.ktlab.sap.util.Bookmark;
import hr.fer.zemris.ktlab.sap.util.Sentence;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JSplitPane;

/**
 * Ova klasa nasljeđuje klasu <code>AbstractAction</code>. Ovdje su
 * definirane sve akcije koje se koriste u grafičkom sučelju programa.
 * 
 * @author Igor Šoš
 * 
 */
public class MyAbstractAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/** Konstanta koja reprezentira akciju za otvaranje About prozora. */
	public static final int ABOUT_WINDOW = 0;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje prozora za otvaranje
	 * projekta.
	 */
	public static final int OPEN_WINDOW = 1;

	/** Konstanta koja reprezentira akciju za otvaranje prozora sa statistikom. */
	public static final int STATS_WINDOW = 2;

	/**
	 * Konstanta koja reprezentira akciju za otvaranje prozora za kreiranje
	 * novog projekta.
	 */
	public static final int NEW_PROJ_WINDOW = 3;

	/** Konstanta koja reprezentira otvaranje prozora za editiranje komentara. */
	public static final int EDIT_COMMENT = 4;

	/** Konstanta koja reprezentira akciju za otvaranje prozora s postavkama. */
	public static final int SETTINGS_WINDOW = 5;

	/**
	 * Konstanta koja reprezentira akciju za otvaranje prozora za editiranje
	 * projekta.
	 */
	public static final int EDIT_PROJECT = 6;

	/** Konstanta koja reprezentira akciju za spremanje projekta. */
	public static final int SAVE_PROJECT = 7;

	/** Konstanta koja reprezentira akciju za editiranje lijeve knjižne oznake. */
	public static final int EDIT_BOOKMARK_LEFT = 8;

	/** Konstanta koja reprezentira akciju za dodavanje lijeve knjižne oznake. */
	public static final int ADD_BOOKMARK_LEFT = 9;

	/**
	 * Konstanta koja reprezentira akciju za brisanje lijeve knjižne oznake iz
	 * padajučeg izbornika tablice.
	 */
	public static final int REMOVE_BOOKMARK_LEFT = 10;

	/**
	 * Konstanta koja reprezentira akciju za brisanje knjižne oznake iz panela s
	 * knjižnim oznakama.
	 */
	public static final int REMOVE_BOOKMARK2 = 11;

	/**
	 * Konstanta koja reprezentira akciju za editiranje knjižne oznake iz panela
	 * s knjižnim oznakama.
	 */
	public static final int EDIT_BOOKMARK2 = 12;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje eksportanja lijeve
	 * tablice u XML datoteku.
	 */
	public static final int EXPORT_XML1 = 13;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje eksportanja desne
	 * tablice u XML datoteku.
	 */
	public static final int EXPORT_XML2 = 14;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje eksportanja u TMX
	 * datoteku.
	 */
	public static final int EXPORT_TMX = 15;

	/** Konstanta koja reprezentira akciju za otvaranje projekta. */
	public static final int OPEN_PROJECT = 16;

	/** Konstanta koja reprezentira undo akciju. */
	public static final int UNDO = 17;

	/** Konstanta koja reprezentira redo akciju. */
	public static final int REDO = 18;

	/** Konstanta koja reprezentira akciju za uklanjanje svih veza. */
	public static final int REMOVE_ALL_CONNECTIONS = 19;

	/**
	 * Konstanta koja reprezentira akciju za editiranje desne knjižne oznake iz
	 * padajućeg izbornika tablice.
	 */
	public static final int EDIT_BOOKMARK_RIGHT = 20;

	/**
	 * Konstanta koja reprezentira akciju za dodavanje knjižne oznake desnom
	 * elementu.
	 */
	public static final int ADD_BOOKMARK_RIGHT = 21;

	/**
	 * Konstanta koja reprezentira akciju za brisanje knjižne oznake desnog
	 * elementa iz padajućeg izbornika tablice.
	 */
	public static final int REMOVE_BOOKMARK_RIGHT = 22;

	/** Konstanta koja reprezentira akciju za brisanje elementa lijeve tablice. */
	public static final int DELETE_ELEMENT1 = 23;

	/** Konstanta koja reprezentira akciju za brisanje elementa desne tablice. */
	public static final int DELETE_ELEMENT2 = 24;

	/**
	 * Konstanta koja reprezentira akciju za umetanje elementa u lijevu tablicu
	 * iznad trenutno označenog elementa.
	 */
	public static final int INSERT_ELEMENT_ABOVE1 = 25;

	/**
	 * Konstanta koja reprezentira akciju za umetanje elementa u lijevu tablicu
	 * ispod trenutno označenog elementa.
	 */
	public static final int INSERT_ELEMENT_BELOW1 = 26;

	/**
	 * Konstanta koja reprezentira akciju za umetanje elementa u desnu tablicu
	 * iznad trenutno označenog elementa.
	 */
	public static final int INSERT_ELEMENT_ABOVE2 = 27;

	/**
	 * Konstanta koja reprezentira akciju za umetanje elementa u desnu tablicu
	 * ispod trenutno označenog elementa.
	 */
	public static final int INSERT_ELEMENT_BELOW2 = 28;

	/**
	 * Konstanta koja reprezentira akciju za spremanje projekta uz odabir
	 * lokacije spremanja.
	 */
	public static final int SAVE_AS_PROJECT = 29;

	/** Konstanta koja reprezentira akciju za eksportanje u TEI format. */
	public static final int EXPORT_TEI = 30;

	/**
	 * Konstanta koja reprezentira akciju za označavanje elementa lijeve tablice
	 * kao ispravno podijeljenog.
	 */
	public static final int MARK_AS_OK1 = 31;

	/**
	 * Konstanta koja reprezentira akciju za označavanje elementa desne tablice
	 * kao ispravno podijeljenog.
	 */
	public static final int MARK_AS_OK2 = 32;

	/**
	 * Konstanta koja reprezentira akciju za osvježavanje grafičkog sučelja.
	 */
	public static final int REFRESH = 33;

	/** Konstanta koja reprezentira akciju za pokretanje algoritma 1on1. */
	public static final int ALG_1ON1 = 100;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje automatske segmentacije
	 * lijevih elemenata.
	 */
	public static final int AUTO_SPLIT_LEFT = 101;

	/** Konstanta koja reprezentira akciju za pokretanje Gale-Church algoritma. */
	public static final int GALE_CHURCH = 102;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje automatske segmentacije
	 * desnih elemenata.
	 */
	public static final int AUTO_SPLIT_RIGHT = 103;

	/**
	 * Konstanta koja reprezentira akciju za pokretanje Japecovog algoritma za
	 * segmentaciju.
	 */
	public static final int JAPEC_ALGORITHM = 104;

	/** Konstanta koja reprezentira akciju za gašenje programa. */
	public static final int DO_EXIT = 200;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na
	 * lijevi gumb za dijeljenje elemenata.
	 */
	public static final int BUTTON_LEFT_SPLIT = 300;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na desni
	 * gumb za dijeljenje elemenata.
	 */
	public static final int BUTTON_RIGHT_SPLIT = 301;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na gumb
	 * za spajanje lijevih elemenata.
	 */
	public static final int BUTTON_LEFT_MERGE = 302;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na gumb
	 * za spajanje desnih elemenata.
	 */
	public static final int BUTTON_RIGHT_MERGE = 303;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na gumb
	 * za automatsku segmentaciju lijevih elemenata.
	 */
	public static final int BUTTON_AUTO_SPLIT_LEFT = 304;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na gumb
	 * za automatsku segmentaciju desnih elemenata.
	 */
	public static final int BUTTON_AUTO_SPLIT_RIGHT = 305;

	// public static final int BUTTON_UNSAFE_SPLIT_COLOR = 306;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na
	 * lijevi gumb za pretraživanje.
	 */
	public static final int TGLBTN_LEFT_FIND = 350;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na desni
	 * gumb za pretraživanje.
	 */
	public static final int TGLBTN_RIGHT_FIND = 351;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na gumb
	 * za prikazivanje knjižnih oznaka.
	 */
	public static final int TGLBTN_BOOKMARKS = 352;

	/**
	 * Konstanta koja reprezentira akciju koja se izvršava pri pritisku na gumb
	 * za prikazivanje detalja elemenata.
	 */
	public static final int TGLBTN_DETAILS = 353;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Vrijednost konstante koja reprezentira neku akciju. */
	private int what;

	/**
	 * Konstruktor klase.
	 * 
	 * @param name
	 *            Ime akcije.
	 * @param parent
	 *            Glavni prozor programa.
	 * @param what
	 *            Vrijednost konstante koja reprezentira neku akciju.
	 */
	public MyAbstractAction(String name, MainWindow parent, int what) {
		super(name);
		this.mainWindow = parent;
		this.what = what;
	}

	/**
	 * Metoda koja se automatski poziva pri izvršavanju neke akcije.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		int row;
		int id;
		Bookmark tmpBookmark;

		switch (what) {
		case ABOUT_WINDOW:
			new AboutWindow(mainWindow, true);
			break;

		case STATS_WINDOW:
			new StatisticsWindow(mainWindow);
			break;

		case NEW_PROJ_WINDOW:
			int check = mainWindow.saveIfModified();
			if (check == 1)
				new CreateProjectWindow(mainWindow);
			break;

		case EDIT_COMMENT:
			row = mainWindow.jTable1.getSelectedRow();
			id = mainWindow.leftRowWithId.get(row);
			// new CommentWindow(mainWindow, id);
			break;

		case SETTINGS_WINDOW:
			new SettingsWindow(mainWindow);
			break;

		case EDIT_PROJECT:
			new EditProjectWindow(mainWindow);
			break;

		case EXPORT_XML1:
			mainWindow.exportXML((short) 1);
			break;

		case EXPORT_XML2:
			mainWindow.exportXML((short) 2);
			break;

		case EXPORT_TMX:
			mainWindow.exportTMX();
			break;

		case EXPORT_TEI:
			mainWindow.exportTEI();
			break;

		case MARK_AS_OK1:
			int[] rows = mainWindow.jTable1.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				if (mainWindow.leftNotSure.contains(rows[i])) {
					mainWindow.leftNotSure.remove(rows[i]);
				}
			}
			break;

		case MARK_AS_OK2:
			rows = mainWindow.jTable2.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				if (mainWindow.rightNotSure.contains(rows[i])) {
					mainWindow.rightNotSure.remove(rows[i]);
				}
			}
			break;

		case REFRESH:
			mainWindow.reDrawVisibleIcons();
			mainWindow.repaint();
			break;

		case ADD_BOOKMARK_LEFT:
			row = mainWindow.jTable1.getSelectedRow();
			id = ((Sentence) mainWindow.jTable1Model.getValueAt(row, 0)).getId();

			new EditBookmarkWindow(mainWindow, Bookmark.SET1, id, false);
			break;

		case EDIT_BOOKMARK_LEFT:
			row = mainWindow.jTable1.getSelectedRow();

			if (row >= 0) {
				id = ((Sentence) mainWindow.jTable1Model.getValueAt(row, 0)).getId();

				new EditBookmarkWindow(mainWindow, Bookmark.SET1, id, true);
			}
			break;

		case EDIT_BOOKMARK2:
			row = mainWindow.jTableBookmarks.getSelectedRow();

			if (row >= 0) {
				tmpBookmark = ((Bookmark) mainWindow.jTableBookmarksModel.getValueAt(row, 0));

				new EditBookmarkWindow(mainWindow, tmpBookmark.getSetNumber(), tmpBookmark.getKey(), true);
			}
			break;

		case REMOVE_BOOKMARK_LEFT:
			row = mainWindow.jTable1.getSelectedRow();
			if (row >= 0) {
				id = ((Sentence) mainWindow.jTable1Model.getValueAt(row, 0)).getId();
				tmpBookmark = (Bookmark) mainWindow.bookmarksById.get(id);

				mainWindow.dataModel.removeBookmark(tmpBookmark);
			}
			break;

		case REMOVE_BOOKMARK2:
			row = mainWindow.jTableBookmarks.getSelectedRow();
			if (row >= 0) {
				Bookmark bookmark = (Bookmark) mainWindow.jTableBookmarksModel.getValueAt(row, 0);
				mainWindow.dataModel.removeBookmark(bookmark);
			}
			break;

		case ADD_BOOKMARK_RIGHT:
			row = mainWindow.jTable2.getSelectedRow();
			id = ((Sentence) mainWindow.jTable2Model.getValueAt(row, 0)).getId();

			new EditBookmarkWindow(mainWindow, Bookmark.SET2, id, false);
			break;

		case EDIT_BOOKMARK_RIGHT:
			row = mainWindow.jTable2.getSelectedRow();

			if (row >= 0) {
				id = ((Sentence) mainWindow.jTable2Model.getValueAt(row, 0)).getId();

				new EditBookmarkWindow(mainWindow, Bookmark.SET2, id, true);
			}
			break;

		case REMOVE_BOOKMARK_RIGHT:
			row = mainWindow.jTable2.getSelectedRow();
			if (row >= 0) {
				id = ((Sentence) mainWindow.jTable2Model.getValueAt(row, 0)).getId();
				tmpBookmark = (Bookmark) mainWindow.bookmarksById.get(id);

				mainWindow.dataModel.removeBookmark(tmpBookmark);
			}
			break;

		case DELETE_ELEMENT1:
			int[] selectedRows = mainWindow.jTable1.getSelectedRows();
			Set<Sentence> toDelete = new HashSet<Sentence>();
			for (int i = 0; i < selectedRows.length; i++) {
				Sentence tmpSentence = (Sentence) mainWindow.jTable1Model.getValueAt(selectedRows[i], 0);
				toDelete.add(tmpSentence);
			}
			for (Sentence sentence : toDelete) {
				mainWindow.dataModel.remove(sentence.getId());
			}
			break;

		case DELETE_ELEMENT2:
			toDelete = new HashSet<Sentence>();
			selectedRows = mainWindow.jTable2.getSelectedRows();
			for (int i = 0; i < selectedRows.length; i++) {
				Sentence tmpSentence = (Sentence) mainWindow.jTable2Model.getValueAt(selectedRows[i], 0);
				toDelete.add(tmpSentence);
			}
			for (Sentence sentence : toDelete) {
				mainWindow.dataModel.remove(sentence.getId());
			}
			break;

		case INSERT_ELEMENT_ABOVE1:
			row = mainWindow.jTable1.getSelectedRow();
			id = mainWindow.dataModel.insertElement1(row, "");
			mainWindow.reDrawVisibleIcons();
			break;

		case INSERT_ELEMENT_BELOW1:
			row = mainWindow.jTable1.getSelectedRow();
			if (mainWindow.jTable1.getRowCount() > row + 1) {
				id = mainWindow.dataModel.insertElement1(row + 1, "");
			} else {
				mainWindow.dataModel.add1("");
			}

			mainWindow.reDrawVisibleIcons();
			break;

		case INSERT_ELEMENT_ABOVE2:
			row = mainWindow.jTable2.getSelectedRow();
			if (row != -1) {
				id = mainWindow.dataModel.insertElement2(row, "");
				mainWindow.setStatus("Element inserted");
			}

			mainWindow.reDrawVisibleIcons();
			break;

		case INSERT_ELEMENT_BELOW2:
			row = mainWindow.jTable2.getSelectedRow();
			if (mainWindow.jTable2.getRowCount() > row + 1) {
				id = mainWindow.dataModel.insertElement2(row + 1, "");
			} else {
				mainWindow.dataModel.add2("");
			}

			mainWindow.reDrawVisibleIcons();
			break;

		case SAVE_PROJECT:
			mainWindow.saveProject();
			break;

		case SAVE_AS_PROJECT:
			mainWindow.saveAsProject();
			break;

		case OPEN_PROJECT:
			mainWindow.openProject();
			break;

		case UNDO:
			mainWindow.undoManager.undo();
			mainWindow.jBtnUndo.setEnabled(mainWindow.undoManager.canUndo());
			mainWindow.jBtnRedo.setEnabled(mainWindow.undoManager.canRedo());
			break;

		case REDO:
			mainWindow.undoManager.redo();
			mainWindow.jBtnUndo.setEnabled(mainWindow.undoManager.canUndo());
			mainWindow.jBtnRedo.setEnabled(mainWindow.undoManager.canRedo());
			break;

		case REMOVE_ALL_CONNECTIONS:
			mainWindow.removeAllConnections();
			break;

		case ALG_1ON1:
			OneOnOne alg1on1 = new OneOnOne(mainWindow.dataModel);
			mainWindow.dataModel.startCompoundAction();
			alg1on1.Connect();
			mainWindow.dataModel.stopCompoudAction();
			break;

		case DO_EXIT:
			mainWindow.dispose();
			break;

		case BUTTON_LEFT_SPLIT:
			mainWindow.splitLeftSentence();
			mainWindow.stats.splitedElements1++;
			break;

		case BUTTON_RIGHT_SPLIT:
			mainWindow.splitRightSentence();
			break;

		case BUTTON_LEFT_MERGE:
			mainWindow.mergeLeftSentences();
			break;

		case BUTTON_RIGHT_MERGE:
			mainWindow.mergeRightSentences();
			break;

		case BUTTON_AUTO_SPLIT_LEFT:
			if (mainWindow.jTglBtnAutoSplitAllLeft.isSelected()) {
				mainWindow.hideLeftSubPanes("split");
				mainWindow.jPanel8.add(mainWindow.jToolBar7, BorderLayout.NORTH);
				mainWindow.validate();
				mainWindow.jToolBar7.setVisible(true);
			} else {
				mainWindow.jToolBar7.setVisible(false);
				mainWindow.jPanel8.remove(mainWindow.jToolBar7);
			}
			break;

		case BUTTON_AUTO_SPLIT_RIGHT:
			if (mainWindow.jTglBtnAutoSplitAllRight.isSelected()) {
				mainWindow.hideRightSubPanes("split");
				mainWindow.jPanelRightFind.add(mainWindow.jToolBar8, BorderLayout.NORTH);
				mainWindow.validate();
				mainWindow.jToolBar8.setVisible(true);
			} else {
				mainWindow.jToolBar8.setVisible(false);
				mainWindow.jPanelRightFind.remove(mainWindow.jToolBar8);
			}
			break;

		case AUTO_SPLIT_LEFT:
			if (mainWindow.jTglBtnAutoSplitAllLeft.isSelected()) {
				mainWindow.jToolBar7.setVisible(false);
				mainWindow.jPanel8.remove(mainWindow.jToolBar7);
				mainWindow.jTglBtnAutoSplitAllLeft.setSelected(false);
			}
			mainWindow.dataModel.startCompoundAction();
			mainWindow.worker.startAutoSplitLeft();
			mainWindow.dataModel.stopCompoudAction();
			break;

		case AUTO_SPLIT_RIGHT:
			if (mainWindow.jTglBtnAutoSplitAllRight.isSelected()) {
				mainWindow.jToolBar8.setVisible(false);
				mainWindow.jPanelRightFind.remove(mainWindow.jToolBar8);
				mainWindow.jTglBtnAutoSplitAllRight.setSelected(false);
			}
			mainWindow.dataModel.startCompoundAction();
			mainWindow.worker.startAutoSplitRight();
			mainWindow.dataModel.stopCompoudAction();
			break;

		case JAPEC_ALGORITHM:
			MasterController mController = new MasterController(mainWindow.dataModel);
			mController.start();

			// MasterController master = new MasterController();
			// SentenceProperties properties = new SentenceProperties(
			// mainWindow.dataModel);
			// KeyManipulator manipulator = new KeyManipulator(
			// mainWindow.dataModel);
			//
			// while (true) {
			// master.list = manipulator.getKeys();
			//
			// // kraj algoritma
			// if (master.list.isEmpty()) {
			// System.out.println("THE END");
			// break;
			// }
			//
			// MasterController.list1 = master.list.get(0);
			// MasterController.list2 = master.list.get(1);
			// properties.analyzer(MasterController.list1,
			// MasterController.list2);
			// }
			break;

		case GALE_CHURCH:
			new GaleChurchWindow(mainWindow);
			// mainWindow.worker.startGaleChurch();
			break;

		case TGLBTN_LEFT_FIND:
			if (mainWindow.jTglBtnLeftFind.isSelected()) {
				mainWindow.hideLeftSubPanes("find");
				mainWindow.jPanel8.add(mainWindow.jToolBar5, BorderLayout.NORTH);
				mainWindow.validate();
				mainWindow.jToolBar5.setVisible(true);
			} else {
				mainWindow.jToolBar5.setVisible(false);
				mainWindow.jPanel8.remove(mainWindow.jToolBar5);
			}
			break;

		case TGLBTN_RIGHT_FIND:
			if (mainWindow.jTglBtnRightFind.isSelected()) {
				mainWindow.hideRightSubPanes("find");
				mainWindow.jPanelRightFind.add(mainWindow.jToolBar6, BorderLayout.NORTH);
				mainWindow.validate();
				mainWindow.jToolBar6.setVisible(true);
			} else {
				mainWindow.jToolBar6.setVisible(false);
				mainWindow.jPanelRightFind.remove(mainWindow.jToolBar6);
			}
			break;

		case TGLBTN_DETAILS:
			if (mainWindow.jTglBtnDetails.isSelected()) {
				mainWindow.hideLeftSubPanes("details");
				mainWindow.hideRightSubPanes("details");

				mainWindow.jPanel8.add(mainWindow.jToolBar9, BorderLayout.NORTH);
				mainWindow.jToolBar9.setVisible(true);
				mainWindow.validate();

				mainWindow.jPanelRightFind.add(mainWindow.jToolBar10, BorderLayout.NORTH);
				mainWindow.jToolBar10.setVisible(true);
				mainWindow.validate();
			} else {
				mainWindow.jToolBar9.setVisible(false);
				mainWindow.jPanel8.remove(mainWindow.jToolBar9);

				mainWindow.jToolBar10.setVisible(false);
				mainWindow.jPanelRightFind.remove(mainWindow.jToolBar10);
			}

			break;

		case TGLBTN_BOOKMARKS:
			if (mainWindow.jTglBtnBookmarks.isSelected()) {
				mainWindow.jPanelBookmarks.setVisible(true);

				mainWindow.getContentPane().remove(mainWindow.jPanelMain);

				mainWindow.jSplitPane1.add(mainWindow.jPanelMain, JSplitPane.RIGHT);
				mainWindow.jSplitPane1.add(mainWindow.jPanelBookmarks, JSplitPane.LEFT);

				mainWindow.getContentPane().add(mainWindow.jSplitPane1);

			} else {
				mainWindow.jPanelBookmarks.setVisible(false);

				mainWindow.getContentPane().remove(mainWindow.jSplitPane1);
				mainWindow.getContentPane().add(mainWindow.jPanelMain);
			}
			mainWindow.validate();
			break;

		default:
			break;
		}

	}

}
