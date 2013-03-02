package hr.fer.zemris.ktlab.sap.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hr.fer.zemris.ktlab.sap.util.DataModel;

/**
 * Klasa koja sluzi za pohranu u datoteke. Moguci formati izlaznih datoteka su
 * .tmx, .xml i TEI. Datoteka u .tmx formatu zadovoljava TMX 1.4b specifikacije.
 * Ispis datoteka u .xml format sluzi za pohranu jedne strane iz modela.
 */
public class FileExporter {
	public static final String XML = "xml";
	public static final String TMX = "tmx";
	public static final String ALL = "all";

	/**
	 * model koji sadrzi elemente koji se zapisuju u datoteku
	 */
	private DataModel dataModel;

	/**
	 * string za privremenu pohranu
	 */
	private String temp;

	/**
	 * lista kljuceva elemenata koji se nalaze na lijevoj strani
	 */
	private List<Integer> idList1;

	/**
	 * lista kljuceva elemenata koji se nalaze na desnoj strani
	 */
	private List<Integer> idList2;

	/**
	 * skup elemenata koji su posjeceni. Ako se kljuc elementa nalazi u skupu to
	 * znaci da je vrijednost elementa ispisana u datoteku
	 */
	private Set<Integer> visited = new HashSet<Integer>();

	/**
	 * lista koja sadrzi sve elemente koji su medusobno povezani
	 */
	private List<Integer> tuvPair = new ArrayList<Integer>();

	/**
	 * skup imena atributa koji ce se ispisati
	 */
	private Set<String> outAtts = new HashSet<String>();

	/**
	 * Javni konstruktor klase
	 * 
	 * @param dataModel
	 *            model iz kojeg se dohvacaju elementi koji se zapisuju u
	 *            datoteku
	 * @param outAtts
	 *            skup imena atributa koji ce se ispisati
	 */
	public FileExporter(DataModel dataModel, HashSet<String> outAtts) {
		this.dataModel = dataModel;
		this.idList1 = dataModel.getKeys1();
		this.idList2 = dataModel.getKeys2();
		this.outAtts = outAtts;
	}

	/**
	 * Jednostavniji konstruktor. Zadrzan iz povijesnih razloga :)
	 * 
	 * @param dataModel
	 */
	public FileExporter(DataModel dataModel) {
		this.dataModel = dataModel;
		this.idList1 = dataModel.getKeys1();
		this.idList2 = dataModel.getKeys2();
		this.outAtts = new HashSet<String>();
	}

	// /**
	// * Postavlja novu listu atributa za ispis
	// *
	// * @param outAtts
	// * atributi koji ce se ispisati, ako postoje
	// */
	// public void setOutAtts(HashSet<String> outAtts) {
	// this.outAtts = outAtts;
	// }

	/**
	 * Metoda sluzi za ispis xml datoteke. Ispis je well-formed
	 * 
	 * @param out
	 * @param idList
	 *            lista elemenata koje treba ispisati. Uobicajeno svi elementi
	 *            na lijevoj ili desnoj strani
	 * @throws IOException 
	 */
	private void writeToFile(OutputStreamWriter out, List<Integer> idList) throws IOException {
		String attributes;

		try {
			// ispis zaglavlja
			temp = "<?xml version=" + "\"" + "1.0" + "\"" + " encoding=" + "\""
					+ "UTF-8" + "\"" + "?>\r\n";
			out.write(temp, 0, temp.length());

			temp = "<doc type=" + "\"" + "article" + "\"" + ">\r\n";
			out.write(temp, 0, temp.length());

			// ispisuje redom sve elemente odabrane liste
			for (int i = 0; i < idList.size(); i++) {
				// System.out.println(dataModel.getAttributesForElement(idList
				// .get(i))); // maknuti
				// priprema atribute za ispis
				attributes = constructAttributesOutput(idList.get(i));

				temp = "<s id=" + "\"" + i + "\"" + attributes + ">"
						+ dataModel.getElement(idList.get(i)) + "</s>\r\n";
				out.write(temp, 0, temp.length());
			}

			// ispisuje zatvoreni tag
			temp = "</doc>";
			out.write(temp, 0, temp.length());
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Selected encoding is not supported.");
		} catch (IOException e) {
			throw new IOException("I/O exception has occurred.");
		}
	}

	/**
	 * Metoda stvara izlaznu datoteku ovisno o parametrima. Nakon toga poziva
	 * metodu writeToFile koja obavlja zapisivanje.
	 * 
	 * @param path
	 *            putanja do datoteke
	 * @param side
	 *            strana koja se pohranjuje
	 * @param extention
	 *            ekstenzija
	 * @throws IOException 
	 */
	public void xmlExporter(String path, short side, String extention) throws IOException {
		OutputStream fout = null;
		OutputStream bout = null;
		OutputStreamWriter output = null;

		String newPath = checkPath(path, extention);

		try {
			// ovisno o odabranoj strani stvara se datoteka i poziva metoda koja
			// vrsi ispis
			if (side == (short) 1) {
				fout = new FileOutputStream(newPath);
				bout = new BufferedOutputStream(fout);
				output = new OutputStreamWriter(bout, "UTF-8");

				// FileOutputStream output = new FileOutputStream("left.xml");
				this.writeToFile(output, idList1);
				output.close();
			} else {
				fout = new FileOutputStream(newPath);
				bout = new BufferedOutputStream(fout);
				output = new OutputStreamWriter(bout, "UTF-8");

				// output = new FileOutputStream("right.xml");
				this.writeToFile(output, idList2);
				output.close();
			}

		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(
					"File could not be found!\r\n"
							+ "- Verify that the file exists in the specified location.\r\n"
							+ "- Check the spelling of the name of the document.\r\n\r\n"
							+ path);
		} catch (IOException e) {
			throw new IOException("I/O exception has occurred.");
		}

	}

	/**
	 * Metoda ispisuje sravnjene recenice u .tmx datoteku. Zadovoljena su sva
	 * pravila tmx formata. Sve veze koje nisu 1:1 pretvaraju se u veze 1:1 na
	 * nacin da se spajaju elementi.
	 * 
	 * @param path
	 * @param extention
	 * @throws IOException 
	 */
	public void exportTMX(String path, String extention) throws IOException {
		StringBuilder seg1 = new StringBuilder();
		StringBuilder seg2 = new StringBuilder();

		path = checkPath(path, extention);

		try {
			OutputStream fout = new FileOutputStream(path);
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter output = new OutputStreamWriter(bout, "UTF-8");

			output.write("<tmx version=\"1.4\">\r\n\r\n");

			// ispis headera
			temp = "<header\t" + "creationtool=" + "\"" + "Coral" + "\"\r\n"
					+ "\t" + "creationtoolversion=" + "\"" + "v1.0" + "\"\r\n"
					+ "\t" + "datatype=" + "\"" + "plaintext" + "\"\r\n" + "\t"
					+ "segtype=" + "\"" + "block" + "\"\r\n" + "\t"
					+ "adminlang=" + "\"" + System.getProperty("user.language")
					+ "\"\r\n" + "\t" + "srclang=" + "\""
					+ dataModel.getProperty("language1") + "\"\r\n" + "\t"
					+ "o-tmf=" + "\"" + "xml" + "\">\r\n" + "</header>\r\n\r\n";
			output.write(temp);

			// body
			output.write("<body>\r\n");

			for (int i = 0; i < idList1.size(); i++) {

				if (dataModel.getConnections(idList1.get(i)).isEmpty()) {
					continue;
				}

				// prolazi se kroz lijevu listu
				if (!visited.contains(idList1.get(i))) {
					// traze se parovi od trenutnog elementa iz lijeve liste
					goDeep(idList1.get(i));

					// vrsi se sortiranje dohvacenih parova ovisno o tome kojoj
					// listi izvorno pripadaju
					// takoder, ako se radi o visestrukim vezama elementi se
					// spajaju
					for (int j = 0; j < tuvPair.size(); j++) {
						if (idList1.contains(tuvPair.get(j))) {
							seg1.append(dataModel.getElement(tuvPair.get(j))
									+ " ");
						} else if (idList2.contains(tuvPair.get(j))) {
							seg2.append(dataModel.getElement(tuvPair.get(j))
									+ " ");
						}

					}

					// stvara se novi translation unit
					output.write("\t" + "<tu>\r\n");

					// prethodno spojeni elementi ispisuju se u translation unit
					// variant
					output.write("\t" + "<tuv xml:lang=" + "\""
							+ dataModel.getProperty("language1") + "\""
							+ "><seg>" + seg1.toString().trim()
							+ "</seg></tuv>\r\n");
					output.write("\t" + "<tuv xml:lang=" + "\""
							+ dataModel.getProperty("language2") + "\""
							+ "><seg>" + seg2.toString().trim()
							+ "</seg></tuv>\r\n");

					output.write("\t" + "</tu>\r\n\r\n");

					tuvPair.clear();
					seg1.delete(0, seg1.length());
					seg2.delete(0, seg2.length());
				}
			}

			output.write("</body>");
			output.write("</tmx>");

			output.close();
			visited.clear();

		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(
					"File could not be found!\r\n"
							+ "- Verify that the file exists in the specified location.\r\n"
							+ "- Check the spelling of the name of the document.\r\n\r\n"
							+ path);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Selected encoding is not supported.");
		} catch (IOException e) {
			throw new IOException("I/O exception has occurred.");
		}
	}

	/**
	 * Ispisuje model u datoteku u tei formatu. Uz <code>id</code>, kao
	 * atribut uz pojedini element moguce je ispisati i odabrane atribute koji
	 * su se nalazili u originalnoj datoteci. Kopiranjem atributa sprijecava se
	 * gubitak mozebitno korisnih informacija, koje nas program ne koristi, a
	 * korisnik zeli zadrzati.
	 * 
	 * @param path
	 *            putanja do datoteke
	 * @param extention
	 *            ekstenzija
	 * @throws IOException 
	 */
	public void exportTEI(String path, String extention) throws IOException {
		List<Integer> seg1Atts = new ArrayList<Integer>();
		List<Integer> seg2Atts = new ArrayList<Integer>();

		int idCounter1 = 0, idCounter2 = 0;

		String[] tokens;
		String filename1 = new String(), filename2 = new String();

		tokens = dataModel.getProperty("file1").split("\\\\");
		tokens = tokens[tokens.length - 1].split("\\.");
		for (int i = 0; i < tokens.length - 1; i++) {
			filename1 = filename1.concat(tokens[i]);
			filename1 = filename1.concat(".");
		}
		if (filename1.endsWith(".")) {
			filename1 = filename1.subSequence(0, filename1.length() - 1)
					.toString();
		}

		tokens = dataModel.getProperty("file2").split("\\\\");
		tokens = tokens[tokens.length - 1].split("\\.");
		for (int i = 0; i < tokens.length - 1; i++) {
			filename2 = filename2.concat(tokens[i]);
			filename2 = filename2.concat(".");
		}
		if (filename2.endsWith(".")) {
			filename2 = filename2.subSequence(0, filename2.length() - 1)
					.toString();
		}

		//System.out.println(extention);
		path = checkPath(path, extention);

		try {
			OutputStream fout = new FileOutputStream(path);
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter output = new OutputStreamWriter(bout, "UTF-8");

			output.write("<doc type=" + "\"" + "article_aligned"
							+ "\" projectName=\""
							+ dataModel.getProperty("projectName") + "\""
							+ ">\r\n\r\n");

			// body
			// output.write("<doc>\r\n\r\n");

			for (int i = 0; i < idList1.size(); i++) {

				if (dataModel.getConnections(idList1.get(i)).isEmpty()) {
					continue;
				}

				if (!visited.contains(idList1.get(i))) {
					goDeep(idList1.get(i));

					for (int j = 0; j < tuvPair.size(); j++) {
						if (idList1.contains(tuvPair.get(j))) {
							// seg1.add(dataModel.getElement(tuvPair.get(j)));
							seg1Atts.add(tuvPair.get(j));
							// pair1++;
						} else if (idList2.contains(tuvPair.get(j))) {
							// seg2.add(dataModel.getElement(tuvPair.get(j)));
							seg2Atts.add(tuvPair.get(j));
							// pair2++;
						}

					}

					output.write("<tu type=" + "\"" + seg1Atts.size() + ":"
							+ seg2Atts.size() + "\"" + ">\r\n");

					output.write("\t" + "<seg lang=" + "\""
							+ dataModel.getProperty("language1") + "\">\r\n");

					for (int j = 0; j < seg1Atts.size(); j++) {
						temp = constructAttributesOutput(seg1Atts.get(j));

						output.write("\t\t" + "<s id=" + "\"" + filename1
								+ ".S" + idCounter1++ + "\"" + temp + ">"
								+ dataModel.getElement(seg1Atts.get(j))
								+ "</s>\r\n");

					}

					output.write("\t" + "</seg>\r\n");
					output.write("\t" + "<seg lang=" + "\""
							+ dataModel.getProperty("language2") + "\">\r\n");

					for (int j = 0; j < seg2Atts.size(); j++) {
						temp = constructAttributesOutput(seg2Atts.get(j));

						output.write("\t\t" + "<s id=" + "\"" + filename2
								+ ".S" + idCounter2++ + "\"" + temp + ">"
								+ dataModel.getElement(seg2Atts.get(j))
								+ "</s>\r\n");

					}

					output.write("\t" + "</seg>\r\n");

					output.write("</tu>\r\n\r\n");
					seg1Atts.clear();
					seg2Atts.clear();
					tuvPair.clear();

				}
			}

			output.write("</doc>");

			output.close();
			visited.clear();

		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(
					"File could not be found!\r\n"
							+ "- Verify that the file exists in the specified location.\r\n"
							+ "- Check the spelling of the name of the document.\r\n\r\n"
							+ path);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Selected encoding is not supported.");
		} catch (IOException e) {
			throw new IOException("I/O exception has occurred.");
		}
	}

	/**
	 * Rekurzivna metoda koja puni <code>visited</code> sa elementima koje je
	 * posjetila i <code>tuvPair</code> sa elementima koji su medusobno
	 * povezani.
	 * 
	 * @param key
	 *            element koji se trenutno obraduje
	 */
	private void goDeep(int key) {
		Set<Integer> connections = dataModel.getConnections(key);
		//System.out.println(key);
		visited.add(key);
		tuvPair.add(key);

		for (Integer tempKey : connections) {
			if (!tuvPair.contains(tempKey)) {
				goDeep(tempKey);
			}

		}
	}

	/**
	 * Metoda za provjeru ispravnosti putanje. Provjerava se zavrsava li
	 * primljana putanja sa ekstenzijom extention. Ako da metoda vraca primljenu
	 * putanju, u suprotnome na primljenu putanju dodaje se ektenzija
	 * <code>extention</code>.
	 * 
	 * @param pathToCheck
	 *            putanja
	 * @param extention
	 *            ekstenzija sa kojom putanja treba zavrsiti ili "all" ako se
	 *            korisniku prepusta izbor proizvoljne ekstenzije
	 */
	private String checkPath(String pathToCheck, String extention) {
		String[] tokens = null;
		StringBuilder retval = new StringBuilder();

		// ako primljena eksenzija ima tocku na pocetku ona se brise
		if (extention.substring(0, 1).equals(".")) {
			extention = extention.substring(1);
		}

		// ako je primljena "ekstenzija" all documents path se ne mijenja
		if (extention.equals("all")) {
			return pathToCheck;
		} else {

			tokens = pathToCheck.split("\\.");

			if (tokens == null) {
				return pathToCheck + "." + extention;
			} else {
				// ako primljeni path zavrsava sa dobrom ekstenzijom nista se ne
				// dodaje
				if (tokens[tokens.length - 1].equals(extention)) {
					return pathToCheck;
				} else {
					// u suprotnome na primljeni path se ljepi ekstenzija
					for (String token : tokens) {
						retval.append(token + ".");
					}
					return (retval.append(extention).toString());
				}
			}

		}

	}

	/**
	 * Metoda konstruira String koji sadrzi odabrane atribute pripremljene za
	 * ispis.
	 * 
	 * @param key
	 *            kljuc elementa ciji se atributi pripremaju za ispis
	 * @return String koji sadrzi atribute spremne za ispis u datoteku
	 */
	private String constructAttributesOutput(int key) {
		StringBuilder temp = new StringBuilder();
		Map<String, String> elementAttributes = dataModel
				.getAttributesForElement(key);
		Iterator<String> iterator = outAtts.iterator();
		String current = new String();

		//System.out.println(outAtts.toString());

		if (elementAttributes == null) {
			return "";
		}

		while (iterator.hasNext()) {
			current = iterator.next();
			//System.out.println(current);

			if (elementAttributes.containsKey(current)) {
				temp.append(" " + current + "=\""
						+ elementAttributes.get(current) + "\"");
			}
		}
		return temp.toString();
	}

	public Set<String> getOutAtts() {
		return outAtts;
	}

	public void setOutAtts(Set<String> outAtts) {
		this.outAtts = outAtts;
	}

}
