package hr.fer.zemris.ktlab.sap.gui.windows;

import hr.fer.zemris.ktlab.sap.util.Bookmark;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * Klasa EditBookmarkWindow predstavlja jednostavan prozor koji omogućuje unos
 * nove ili editiranje postojeće knjižne oznake. Prozor se sastoji samo od jedne
 * instance klase JTextField (unos naziva knjižne oznake), jedne instance klase
 * JTextArea (unos opisa knjižne oznake) i dvije instance klase JButton (gumbi
 * "OK" i "Cancel"). Ukoliko se dodaje nova knjižna oznaka, komponente ovog
 * prozora neće sadržavati nikakav tekst. No, kod editiranja knjižne oznake, u
 * komponente se učitava odgovarajući sadržaj knjižne oznake (naziv i opis).
 * 
 * @author Igor Šoš
 * 
 */
public class EditBookmarkWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/** Varijabla koja predstavlja stranu prozora. */
	private short side;

	/** Gumb OK. */
	private JButton jButton1;

	/** Gumb Cancel. */
	private JButton jButton2;
	private JTextPane jTextPane1;
	private JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JTextField jTextField1;
	private JLabel jLabel1;

	/** ID elementa kojemu se postavlja knjižna oznaka. */
	private int id;

	/** Varijabla koja označava da li se dodaje ili editira knjižna oznaka. */
	private boolean edit;

	/**
	 * Javni konstruktor klase.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 * @param side
	 *            Varijabla koja predstavlja stranu prozora.
	 * @param id
	 *            ID elementa kojemu se postavlja knjižna oznaka.
	 * @param edit
	 *            true ako se editira postojeca knjižna oznaka, inače false.
	 */
	public EditBookmarkWindow(MainWindow mainWindow, short side, int id,
			boolean edit) {
		super(mainWindow, true);
		this.mainWindow = mainWindow;
		this.side = side;
		this.id = id;
		this.edit = edit;
		initGUI();
	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		{
			this.setTitle("Edit bookmark");
			getContentPane().setLayout(null);
			this.setResizable(false);
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText("Bookmark title:");
				jLabel1.setBounds(12, 12, 214, 14);
			}
			{
				jTextField1 = new JTextField();
				getContentPane().add(jTextField1);
				jTextField1.setBounds(12, 32, 214, 21);
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2);
				jLabel2.setText("Bookmark description:");
				jLabel2.setBounds(12, 65, 214, 14);
			}
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(12, 85, 214, 79);
				{
					jTextPane1 = new JTextPane();
					jScrollPane1.setViewportView(jTextPane1);
					jTextPane1
							.setPreferredSize(new java.awt.Dimension(211, 78));
				}
			}
			if (edit) {
				System.out.println("Editing");
				jTextField1.setText(mainWindow.bookmarksById.get(id).getName());
				jTextPane1.setText(mainWindow.bookmarksById.get(id)
						.getDescription());
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1);
				jButton1.setText("OK");
				jButton1.setBounds(35, 176, 80, 21);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2);
				jButton2.setText("Cancel");
				jButton2.setBounds(126, 176, 80, 21);
				jButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2ActionPerformed(evt);
					}
				});
			}
		}
		{
			this.setSize(246, 235);
			this.setLocationRelativeTo(mainWindow);
			this.setVisible(true);
		}

	}

	/**
	 * Metoda koja se izvršava nakon pritiska na gumb OK.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton1ActionPerformed(ActionEvent evt) {

		Bookmark newBookmark = new Bookmark(id, jTextField1.getText(),
				jTextPane1.getText(), side);

		if (!edit) {
			mainWindow.dataModel.addBookmark(newBookmark);
		} else {
			Bookmark oldBookmark = mainWindow.bookmarksById.get(id);
			List<Bookmark> bookmarks = mainWindow.dataModel.getBookmarks();
			int index = bookmarks.indexOf(oldBookmark);
			bookmarks.set(index, newBookmark);
		}

		mainWindow.reloadBookmarks();

		// if (side == Bookmark.SET1) {
		// mainWindow.rowsWithBookmarksLeft.add(mainWindow.leftRowWithId.get(id));
		// mainWindow.bookmarksById.put(id, bookmark);
		// mainWindow.reloadBookmarks();
		// } else {
		// mainWindow.rowsWithBookmarksRight.add(mainWindow.rightRowWithId.get(id));
		// mainWindow.bookmarksById.put(id, bookmark);
		// mainWindow.reloadBookmarks();
		// }

		this.dispose();
	}

	/**
	 * Metoda koja se izvršava nakon pritiska na gumb Cancel.
	 * 
	 * @param evt
	 *            Event pritiska na gumb.
	 */
	private void jButton2ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

}
