package hr.fer.zemris.ktlab.sap.io;

import hr.fer.zemris.ktlab.sap.util.DataModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Klasa sluzi za ucitavanje datoteka. Ucitavati se mogu .xml i .txt datoteke.
 * 
 * Kod ucitavanja .txt datoteka tekst se lomi po oznakama za novi red. Tocnije,
 * sav tekst koji se nalazi izmedu dvije oznake za novi red biti ce predan u
 * model kao jedan element i biti ce oznacen kao jedan paragraf.
 * 
 * Kod ucitavanja .xml datoteka ponudena je opcija ignoriranja tagova i
 * proizvoljnog odredivanja koji tagovi oznacavaju novi paragraf. Tekst koji se
 * nalazi unutar tag-a biti ce predan kao jedan element. Taj ce element takoder
 * imati oznaku trenutnog paragrafa.
 */
public class AdvancedFileLoader extends DefaultHandler {
	public static final int LEFT = 1;
	public static final int RIGHT = 2;

	/**
	 * Model koji se puni sa podacima iz ulaznih datoteka
	 */
	private DataModel dataModel;

	/**
	 * StringBuilder u kojeg se pohranjuju ucitani dijelovi dokumenta koji jos
	 * nisu predani u model
	 */
	private StringBuilder temp = new StringBuilder();

	/**
	 * Varijabla koja oznacava "stranu" koja se puni
	 */
	private int type;

	/**
	 * Lista koja sadrzi imena tagova koji ce se ignorirati. Ignorirati tag
	 * znaci da se zanemaruju atributi koji su u njemu pohranjeni i da se tekst
	 * koji je tim tagom obuhvacen ne predaje u model kao zasebni element nego
	 * se spaja sa ostalim tagovima na istoj razini.
	 */
	private List<String> ignoreList;

	/**
	 * Lista koja sadrzi imena tagova na ciju ce se pojavu povecati brojac
	 * paragrafa. Element koji se predaje u model uvijek se predaje zajedno sa
	 * trenutnom vrijednosti brojaca.
	 */
	private List<String> paragraphList;

	/**
	 * Sluzi za privremenu pohranu ucitanih atributa
	 */
	private Attributes lastAttributes;

	/**
	 * Brojac paragrafa
	 */
	private int paragraphCounter;

	/**
	 * Mapa u koju su pohranjeni atributi. Predaje se u <code>dataModel</code>.
	 */
	private Map<String, String> last = new HashMap<String, String>();

	/**
	 * Javni konstruktor klase
	 * 
	 * @param dataModel
	 *            model u kojeg ce se ucitavati datoteke
	 * @param type
	 *            "strana" na koju ce se ucitavati
	 * @param ignoreList
	 *            lista tagove koji ce se ignorirati
	 * @param paragraphList
	 *            lista paragrafa koji oznacavaju novi paragraf
	 * 
	 * @see AdvancedFileLoader
	 */
	public AdvancedFileLoader(DataModel dataModel, int type,
			List<String> ignoreList, List<String> paragraphList) {
		super();
		this.dataModel = dataModel;
		this.type = type;
		this.ignoreList = ignoreList;
		this.paragraphList = paragraphList;
		this.paragraphCounter = 0;
	}

	/**
	 * Mijenja listu tagova koji se ignoriraju
	 * 
	 * @param ignoreList
	 *            nova lista tagova koji ce se ignorirati
	 */
	public void setIgnoreList(List<String> ignoreList) {
		this.ignoreList = ignoreList;
	}

	/**
	 * Mijenja listu tagova koji oznacavaju novi paragraf
	 * 
	 * @param paragraphList
	 *            nova lista tagova koji ce povecati brojac paragrafa
	 */
	public void setParagraphList(List<String> paragraphList) {
		this.paragraphList = paragraphList;
	}

	/**
	 * Resetira brojac paragrafa
	 */
	public void resetParagraphCounter() {
		paragraphCounter = 0;
	}

	/**
	 * Metoda ucitava sadrzaj .txt datoteke u model. Enter oznacava kraj
	 * elementa i kraj paragrafa.
	 * 
	 * @param path
	 *            putanja do datoteke
	 * @param encoding
	 *            encoding u kojem je datoteka zapisana
	 * @throws IOException 
	 */
	public void loadTXT(String path, String encoding)
			throws IOException {
		String[] tokens = null;

		int ch;
		StringBuilder buf = new StringBuilder();

		try {
			FileInputStream fin = new FileInputStream(path);
			InputStreamReader input = new InputStreamReader(fin, encoding);
			BufferedReader bin = new BufferedReader(input);

			while ((ch = bin.read()) != -1) {
				buf.append((char) ch);
			}

			tokens = buf.toString().split("\n");
			buf.delete(0, buf.length());

			if (type == LEFT) {
				for (int i = 0; i < tokens.length; i++) {
					if (!tokens[i].trim().equals("")) {
						if (tokens[i].endsWith("\r"))
							tokens[i] = tokens[i].substring(0, tokens[i]
									.length() - 1);
						dataModel.add1(tokens[i], i);
					}
				}

			} else if (type == RIGHT) {
				for (int i = 0; i < tokens.length; i++) {
					if (!tokens[i].trim().equals("")) {
						if (tokens[i].endsWith("\r"))
							tokens[i] = tokens[i].substring(0, tokens[i]
									.length() - 1);
						dataModel.add2(tokens[i], i);
					}
				}
			}

			resetParagraphCounter();

		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Encoding " + encoding
					+ "is not supported.");
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

	// //////////////////////////////////////////////////////////////////
	// Event handlers.
	// //////////////////////////////////////////////////////////////////

	/**
	 * Prazna metoda koju SAX parser poziva na pocetku parsiranja dokumenta.
	 */
	public void startDocument() {

	}

	/**
	 * Prazna metoda koju SAX parser poziva na kraju parsiranja dokumenta.
	 */
	public void endDocument() {

	}

	/**
	 * Metoda koju poziva SAX parser. Metoda se poziva nailaskom na otvoreni
	 * tag. Svi atributi koje tag sadrzi predaju se u <code>dataModel</code>.
	 * Ako je ime taga sadrzano u <code>paragraphList</code> povecava se
	 * oznaka paragrafa. Ako se tag ne ignorira (ne nalazi se u
	 * <code>ignoreList</code>) njegovi atributi se pohranjuju.
	 * 
	 * @param uri
	 *            Universal Resource Identifier
	 * @param name
	 *            ime taga
	 * @param qName
	 *            kvalificirano ime taga
	 * @param atts
	 *            objekt tipa Attributes koji sadrzi atribute
	 */
	public void startElement(String uri, String name, String qName,
			Attributes atts) {

		// napuni dataModel sa svim vrstama atributa na koje naide tjekom
		// parsiranja
		for (int i = 0; i < atts.getLength(); i++) {
			dataModel.addXmlAttributeName(atts.getQName(i));
		}

		if (paragraphList.contains(qName)) {
			paragraphCounter++;
		}

		if (!ignoreList.contains(qName)) {
			lastAttributes = new AttributesImpl(atts);
		}

	}

	/**
	 * Metoda koju poziva SAX parser nailaskom na zatvoreni tag. Ako
	 * <code>ignoreList</code> sadrzi ime taga metoda ne radi nista. U
	 * suprotnome u <code>dataModel</code> se predaju do tada ucitani tekst,
	 * broj paragrafa i atributi koji su pohranjeni u zadnji tag koji se nije
	 * ignorirao. U <code>dataModel</code> se nikada nece predati prazan
	 * string.
	 * 
	 * @param uri
	 *            Universal Resource Identifier
	 * @param name
	 *            ime taga
	 * @param qName
	 *            kvalificirano ime taga
	 */
	public void endElement(String uri, String name, String qName) {

		if (!ignoreList.contains(qName)) {
			for (int i = 0; i < lastAttributes.getLength(); i++) {
				last
						.put(lastAttributes.getQName(i), lastAttributes
								.getValue(i));
			}

			if (!(temp.toString().trim().equals(""))) {
				if (type == LEFT) {
					// System.out.println(last.toString());
					dataModel.add1(temp.toString().trim(), paragraphCounter,
							last);
				} else if (type == RIGHT) {
					dataModel.add2(temp.toString().trim(), paragraphCounter,
							last);
				}
			}
			temp.delete(0, temp.length());
			last = new HashMap<String, String>();
		}
	}

	/**
	 * Metoda koju poziva SAX parser kad naide na skup znakova. Kako bi se
	 * izbjeglo razlamanje teksta nailaskom na znakove vece od dva byte-a metoda
	 * samo lijepi procitane znakove na varijablu temp.
	 * 
	 * @param ch[]
	 *            polje znakove ucitanih iz dokumenta
	 * @param start
	 *            oznaka prvog elementa u polju koji predstavlja ucitane znakove
	 * @param length
	 *            broj ucitanih znakova
	 */
	public void characters(char ch[], int start, int length) {
		String s = new String(ch, start, length);

		// s = s.trim();
		// if ( s.equals("\n") || s.equals("\r\n") ) {
		// (temp.length() - 2, temp.length() -1).equals(" ")

		if (s.endsWith("\r\n") || s.endsWith("\n")) {
			if (!temp.toString().endsWith(" "))
				temp.append(" ");
		} else {
			temp.append(s);
		}
	}
}