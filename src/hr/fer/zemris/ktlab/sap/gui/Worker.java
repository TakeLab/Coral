package hr.fer.zemris.ktlab.sap.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumnModel;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import hr.fer.zemris.ktlab.sap.algorithms.gc.Aligner;
import hr.fer.zemris.ktlab.sap.gui.windows.MainWindow;
import hr.fer.zemris.ktlab.sap.gui.windows.WorkingWindow;
import hr.fer.zemris.ktlab.sap.io.AdvancedFileLoader;
import hr.fer.zemris.ktlab.sap.io.SaveLoadProject;

/**
 * Pomoću ove klase pripremaju se instance SwingWorker-a koje služe za
 * pokretanje dugotrajnih akcija. <code>Worker</code> funkcionira na taj način
 * da se neka dugotrajna akcija pokrene u zasebnoj dretvi, dok se glavna dretva
 * brine za iscrtavanje prozora koji obavještava korisnika da program još radi i
 * onemogućuje mu daljnju interakciju s programom dok prethodni zadatak ne
 * završi.
 * 
 * @author Igor Šoš
 * 
 */
public class Worker {

	/** Glavni prozor programa. */
	private MainWindow mainWindow;

	/**
	 * Javni konstruktor razreda.
	 * 
	 * @param mainWindow
	 *            Glavni prozor programa.
	 */
	public Worker(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 * Metoda koja služi za pokretanje segmentacije na rečenice lijevih
	 * elemenata.
	 */
	public void startAutoSplitLeft() {
		SwingWorker<Void, Void> splitWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				mainWindow.autoSplitLeft();
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};

		new WorkingWindow(mainWindow, splitWorker);
	}

	/**
	 * Metoda koja služi za pokretanje segmentacije na rečenice desnih
	 * elemenata.
	 */
	public void startAutoSplitRight() {
		SwingWorker<Void, Void> splitWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				mainWindow.autoSplitRight();
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};

		new WorkingWindow(mainWindow, splitWorker);
	}

	/**
	 * Metoda koja služi za pokretanje Gale-Church algoritma za povezivanje
	 * rečenica.
	 */
	public void alignSentencesGaleChurch() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				Aligner aligner = new Aligner(mainWindow.dataModel);
				mainWindow.dataModel.startCompoundAction();
				// Izbaciti kad se ispis trajanja doda u gui
				System.out.println( "Recenicno sravnjenje je trajalo: "+aligner.alignSentences() );
				mainWindow.dataModel.stopCompoudAction();
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};

		new WorkingWindow(mainWindow, worker);
	}
	
	/**
	 * Metoda koja služi za pokretanje Gale-Church algoritma za sravnjivanje
	 * odlomaka.
	 */
	public void alignParagraphsGaleChurch() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				Aligner aligner = new Aligner(mainWindow.dataModel);
				mainWindow.dataModel.startCompoundAction();
				System.out.println( "Odlomacko sravnjenje je trajalo: "+aligner.alignParagraphs() );
				mainWindow.dataModel.stopCompoudAction();
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};

		new WorkingWindow(mainWindow, worker);
	}

	/**
	 * Metoda se koristi za pokretanje učitavanja izvornih datoteka novog
	 * projekta.
	 * 
	 * @param handler
	 *            Handler za učitavanje prve datoteke.
	 * @param handler2
	 *            Handler za učitavanje druge datoteke.
	 * @param path
	 *            Lokacija prve datoteke.
	 * @param path2
	 *            Lokacija druge datoteke.
	 * @param enc1
	 *            Encoding prve datoteke.
	 * @param enc2
	 *            Encoding druge datoteke.
	 */
	public void startCreatingNewProject(final AdvancedFileLoader handler,
			final AdvancedFileLoader handler2, final String path,
			final String path2, final String enc1, final String enc2,
			final String projectName, final String lang1, final String lang2) {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				mainWindow.clear();
				mainWindow.sboard.clear();
				
				mainWindow.undoOff = true;

				TableColumnModel cmodel = mainWindow.jTable1.getColumnModel();
				TextAreaRenderer textAreaRenderer = new TextAreaRenderer(
						mainWindow, TextAreaRenderer.LEFT);
				cmodel.getColumn(0).setCellRenderer(textAreaRenderer);
				TextAreaEditor textEditor = new TextAreaEditor(
						mainWindow.dataModel);
				cmodel.getColumn(0).setCellEditor(textEditor);

				TableColumnModel cmodel2 = mainWindow.jTable2.getColumnModel();
				TextAreaRenderer textAreaRenderer2 = new TextAreaRenderer(
						mainWindow, TextAreaRenderer.RIGHT);
				cmodel2.getColumn(0).setCellRenderer(textAreaRenderer2);
				TextAreaEditor textEditor2 = new TextAreaEditor(
						mainWindow.dataModel);
				cmodel2.getColumn(0).setCellEditor(textEditor2);

				String[] splited = path.split("\\.");
				String extension = "";
				if (splited.length > 1) {
					extension = splited[splited.length - 1];
				}

				XMLReader xr = null;
				FileInputStream fis = null;
				InputStreamReader isr = null;
				if (extension.equals("xml")) {

					try {
						xr = XMLReaderFactory.createXMLReader();
						xr.setContentHandler(handler);
						// xr.setErrorHandler(handler);
						fis = new FileInputStream(path);
						isr = new InputStreamReader(fis, enc1);
						xr.parse(new InputSource(isr));

					} catch (SAXException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Parsing error",
								JOptionPane.ERROR_MESSAGE);
					} catch (FileNotFoundException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					} catch (UnsupportedEncodingException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(),
								"Encoding not supported",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					try {
						handler.loadTXT(path, enc1);
					} catch (FileNotFoundException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

				splited = path2.split("\\.");
				extension = "";
				if (splited.length > 1) {
					extension = splited[splited.length - 1];
				}

				if (extension.equals("xml")) {
					try {
						xr = XMLReaderFactory.createXMLReader();
						xr.setContentHandler(handler2);
						// xr.setErrorHandler(handler2);
						fis = new FileInputStream(path2);
						isr = new InputStreamReader(fis, enc2);
						xr.parse(new InputSource(isr));
					} catch (SAXException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Parsing error",
								JOptionPane.ERROR_MESSAGE);
					} catch (FileNotFoundException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					} catch (UnsupportedEncodingException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(),
								"Encoding not supported",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					try {
						handler2.loadTXT(path2, enc2);
					} catch (FileNotFoundException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						cancel(true);
						JOptionPane.showMessageDialog(null, e
								.getLocalizedMessage(), "Open error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

				mainWindow.reloadKeyListener();
				mainWindow.lastSaveLocation = null;
				mainWindow.setProjectProperties(projectName, lang1, lang2, path, path2);
				mainWindow.loadProjectProperties();
				
				Thread.sleep(500);
				mainWindow.reDrawVisibleIcons();
				mainWindow.undoOff = false;
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};

		new WorkingWindow(mainWindow, worker);
	}

	/**
	 * Metoda se koristi za otvaranje postojećeg projekta.
	 * 
	 * @param path
	 *            Lokacija datoteke sa spremljenim projektom.
	 */
	public void startOpeningProject(final String path) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {

				try {
					mainWindow.clear();
					mainWindow.undoOff = true;

					TableColumnModel cmodel = mainWindow.jTable1
							.getColumnModel();
					TextAreaRenderer textAreaRenderer = new TextAreaRenderer(
							mainWindow, TextAreaRenderer.LEFT);
					cmodel.getColumn(0).setCellRenderer(textAreaRenderer);
					TextAreaEditor textEditor = new TextAreaEditor(
							mainWindow.dataModel);
					cmodel.getColumn(0).setCellEditor(textEditor);

					TableColumnModel cmodel2 = mainWindow.jTable2
							.getColumnModel();
					TextAreaRenderer textAreaRenderer2 = new TextAreaRenderer(
							mainWindow, TextAreaRenderer.RIGHT);
					cmodel2.getColumn(0).setCellRenderer(textAreaRenderer2);
					TextAreaEditor textEditor2 = new TextAreaEditor(
							mainWindow.dataModel);
					cmodel2.getColumn(0).setCellEditor(textEditor2);

					mainWindow.dataModel.addAll(SaveLoadProject
							.loadProject(path));
					// dataModel.initializeTransients();
					// dataModel.addUndoableEditListener(this);
					// dataModel.addPropertyChangeListener(this);

					mainWindow.enableComponents(true);
					mainWindow.loadProjectProperties();
					mainWindow.reloadKeyListener();
					mainWindow.stats = mainWindow.dataModel.getStatistics();

				} catch (Exception e) {
					cancel(true);
					JOptionPane.showMessageDialog(mainWindow, e
							.getLocalizedMessage(), "Open error",
							JOptionPane.ERROR_MESSAGE);
				}

				mainWindow.lastSaveLocation = path;
				Thread.sleep(500);
				mainWindow.reDrawVisibleIcons();
				mainWindow.undoOff = false;
				return null;
			}

			@Override
			protected void done() {
				firePropertyChange("DONE", "Done", null);
				super.done();
			}

		};
		new WorkingWindow(mainWindow, worker);
	}

}
