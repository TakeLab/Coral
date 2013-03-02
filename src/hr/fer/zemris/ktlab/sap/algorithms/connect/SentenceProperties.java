package hr.fer.zemris.ktlab.sap.algorithms.connect;

import hr.fer.zemris.ktlab.sap.util.DataModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.List;

/**
 * Klasa SentenceProperties sastoji se od nekoliko metoda koje analiziraju
 * rečenice. Te metode su: izračunavanje duljine rečenice, traženje identičnih
 * riječi koje počinju velikim slovom i brojeva, usporedba duljine dvije
 * rečenice. U klasi se nalazi i metoda koja obavlja 1-1 sravnjivanje.
 * Sravnjivanje možemo podijeliti na sigurno sravnjivanje(broj istih
 * tokena+najbolji omjer duljina)i nesigurno sravnjivanje(najbolji omjer
 * duljina).
 * 
 */

public class SentenceProperties {

	/**
	 * Lista u kojoj se nalaze dvije liste stringova. Koristi se kod traženja
	 * velikih riječi u rečenici i brojeva.
	 */
	private List<List<String>> tokenList;

	/**
	 * Objekt tipa DataModel
	 */
	private DataModel dataModel;

	/**
	 * Javni konstruktor klase <code>SentenceProperties</code>
	 * 
	 * @param dataModel
	 *            referenca na objekt tipa DataModel
	 */
	public SentenceProperties(DataModel x) {
		this.dataModel = x;
		this.tokenList = new ArrayList<List<String>>();
	}

	/**
	 * Računa dužinu zadanog teksta.
	 * 
	 * @param text
	 *            tekst koji se prima(string)
	 * @return dužina teksta
	 */
	private int sentenceLength(String text) {
		int length = text.length();
		return length;
	}

	/**
	 * Pronalazi riječi koje počinju velikim slovom i brojeve(znamenke). Koristi
	 * StringTokenizer i kao delimiter sljedeće znakove: ". * ! ? - ( )[ ] , ;
	 * \t\n\r\f"
	 * 
	 * @param text
	 *            tekst koji se prima(string)
	 * @return Lista u kojoj se nalaze dvije liste: lista svih riječi koje
	 *         počinju velikim slovom i lista svih brojeva zapisanih kao string
	 * 
	 */
	private List<List<String>> huntForTokens(String text) {

		// OVO BI TREBALO ISKORISTITI.Broj riječi u rečenici
		// int numberOfTokens = 0;

		int numberTokenCount = 0;
		int wordTokenCount = 0;

		List<String> wordTokens;
		List<String> numberTokens;
		StringTokenizer tokenizer = new StringTokenizer(text,
				". * ! ? - ( )[ ] , ; \t\n\r\f");
		wordTokens = new LinkedList<String>();
		numberTokens = new LinkedList<String>();

		if (!(tokenList.isEmpty())) {
			tokenList.clear();
		}

		if (!(wordTokens.isEmpty())) {
			wordTokens.clear();
		}

		if (!(numberTokens.isEmpty())) {
			numberTokens.clear();
		}

		// numberOfTokens=tokenizer.countTokens();

		while (tokenizer.hasMoreTokens()) {
			String tempToken = tokenizer.nextToken();
			// System.out.println(tempToken);

			// broji brojeve
			if (tempToken.charAt(0) > 0x29 && tempToken.charAt(0) < 0x3a) {
				numberTokenCount++;
				numberTokens.add(tempToken);
			}
			// Potrebno još podržati HR slova! Vidi
			// http://mindprod.com/jgloss/unicode.html
			if (tempToken.charAt(0) > 0x40 && tempToken.charAt(0) < 0x5b) {
				wordTokenCount++;
				// dodajem riječ u listu, a pri tome stavljam slova da budu mala
				wordTokens.add(tempToken.toLowerCase());
			}
		}
		/*
		 * Spremanje dvije string liste u listu lista. Na nultom mjestu liste
		 * nalazi se lista svih riječi koje počinju sa velikim slovom, a na
		 * prvom mjestu nalazi se lista u kojoj su spremljeni svi brojevi koji
		 * se nalaze u tekstu.
		 */
		tokenList.add(wordTokens);
		tokenList.add(numberTokens);

		return tokenList;
	}

	/**
	 * Traži iste tokene(stringove)između dvije liste stringova
	 * 
	 * @param listaTokena1
	 *            prva lista stringova
	 * @param listaTokena2
	 *            druga lista stringova
	 * @return vraća broj istih tokena(stringova)
	 */
	private int compare(List<String> listaTokena1, List<String> listaTokena2) {
		int identicalTokens = 0;

		// System.out.println("prije poziva prva lista ima:" + listaTokena1);
		// System.out.println("prije poziva druga lista ima:" + listaTokena2);
		listaTokena1.retainAll(listaTokena2);
		identicalTokens = listaTokena1.size();
		// System.out.println("ISTI TOKENI SU:" + listaTokena1);
		return identicalTokens;
	}

	/**
	 * Uspoređuje duljinu dvije rečenice pomoću omjera: duljina manje
	 * rečenice/duljina veće rečenice. Rečenice sa istom duljinom imat će omjer
	 * 1 (to je ujedno i maksimum)
	 * 
	 * @param length1
	 *            duljina prve rečenice
	 * @param length2
	 *            duljina druge rečenice
	 * @return vraća omjer tih rečenica
	 */
	private float compareLength(float length1, float length2) {

		float magicRatio = 0;
		if ((length1 / length2) > 1)
			magicRatio = length2 / length1;
		else
			magicRatio = length1 / length2;
		return magicRatio;
	}

	/**
	 * Javna metoda koja poziva razne analize rečenica i na temelju tih analiza
	 * sravnjuje dva elementa.
	 * 
	 * @param keyList1
	 *            lista ključeva elemanata lijeve strane koji će se analizirati
	 * @param keyList2
	 *            lista ključeva elemenata desne strane koji će se analizirati
	 * @return vraća 1 ako je došlo do pogreške u sravnjivanju, inače 0
	 */
	public int analyzer(List<Integer> keyList1, List<Integer> keyList2) {
		int i, j;
		int identicalOld = 0;
		int key1 = 0;
		int key2 = 0;
		int key3 = 0;
		int newKey1 = 0;
		int newKey2 = 0;
		int mostSimilarKeys1 = 0;
		int mostSimilarKeys2 = 0;
		// najveći trenutni omjer u jednom ciklusu, vrijednosti poprimaju raspon
		// <0,1]
		float maxLength = 0;
		float lengthCompare = 0;
		float lengthCompareOld = 0;
		boolean connectionMade = false;

		// ako lijeva strana ima samo još jedan element za sravniti, a desna dva
		if ((keyList1.size() == 1) && (keyList2.size() == 2)) {

			key1 = keyList1.get(0);
			key2 = keyList2.get(0);
			key3 = keyList2.get(1);

			dataModel.addConnection(key1, key2);
			dataModel.addConnection(key1, key3);

		}
		// ako lijeva strana ima još samo dva elementa za sravniti, a lijeva
		// jedan
		else if ((keyList1.size() == 2) && (keyList2.size() == 1)) {

			key1 = keyList1.get(0);
			key2 = keyList1.get(1);
			key3 = keyList2.get(0);

			dataModel.addConnection(key1, key3);
			dataModel.addConnection(key2, key3);
		}

		// ako lijeva(ili desna) strana imaju samo još jedan element za
		// sravniti, a ona suprotna strana barem još 3 elementa
		else if (((keyList1.size() == 1) && (keyList2.size() == 3))
				|| ((keyList1.size() == 3) && (keyList2.size() == 1))) {

			return 1;
		}

		// ako obje strane imaju dva ili više elementa za sravniti.
		else if ((keyList1.size() >= 2) && (keyList2.size() >= 2)) {

			for (i = 0; i < keyList1.size(); i++) {
				for (j = 0; j < keyList2.size(); j++) {

					key1 = keyList1.get(i);
					key2 = keyList2.get(j);

					String text1 = dataModel.getElement(key1);
					String text2 = dataModel.getElement(key2);

					float length1 = sentenceLength(text1);
					float length2 = sentenceLength(text2);

					/*
					 * ovdje se traži po jednoj rečenici sa lijeve strane riječi
					 * koje počinju sa velikim slovom i brojevi, a zatim se taj
					 * rezultat prebacuje u dvije odvojene liste
					 */
					List<List<String>> tempList1 = huntForTokens(text1);
					List<String> wordTokenList1 = tempList1.get(0);
					List<String> numberTokenList1 = tempList1.get(1);

					/*
					 * ovdje se traži po jednoj rečenici sa desne strane riječi
					 * koje počinju sa velikim slovom i brojevi, a zatim se taj
					 * rezultat prebacuje u dvije odvojene liste
					 */
					List<List<String>> tempList2 = huntForTokens(text2);
					List<String> wordTokenList2 = tempList2.get(0);
					List<String> numberTokenList2 = tempList2.get(1);

					int identicalTokens1 = compare(wordTokenList1,
							wordTokenList2);
					int identicalTokens2 = compare(numberTokenList1,
							numberTokenList2);
					int identicalTokens = identicalTokens1 + identicalTokens2;

					lengthCompare = compareLength(length1, length2);

					// najsličniji ključevi po duljini (gledaju se samo
					// parovi kada je i==j. Služi za nesigurno spajanje.
					if ((i == j) && (lengthCompareOld < lengthCompare)) {
						lengthCompareOld = lengthCompare;
						mostSimilarKeys1 = key1;
						mostSimilarKeys2 = key2;
					}

					/*
					 * Ovdje se gleda da li je trenutni omjer duljina najveći
					 * omjer u trenutnom ciklusu i da li su pronađene iste
					 * riječi(ili brojevi) između dvije rečenice koje se
					 * (trenutno)promatraju.Tu se još nalazi konstanta koja je
					 * uzeta kao granica najmanjeg mogućeg omjera koji smije
					 * biti između dvije rečenice.Tu je konstantu potrebno
					 * kasnije dodatno testirati i finije odrediti. Ako su svi
					 * ti uvjeti zadovoljeni, trenutni omjer postaje najveći
					 * omjer u ciklusu, i zapamte se ti ključevi.
					 * 
					 */
					if ((identicalOld < identicalTokens)
							&& (lengthCompare > 0.423689)
							&& (lengthCompare >= maxLength)) {
						identicalOld = identicalTokens;
						maxLength = lengthCompare;
						newKey1 = key1;
						newKey2 = key2;
					}

				}// ovo je zatvorena zagrada za drugu for petlju

				// ako su pronađene isti tokeni u elementima sa lijeve i desne
				// strane
				if (identicalOld != 0) {
					dataModel.addConnection(newKey1, newKey2);
					connectionMade = true;
				}
				// spojili smo i idemo van
				if (connectionMade)
					break;
			}// ovo je zatvorena zagrada za prvu for petlju

			// ako se u iteraciji ništa nije spojilo
			if (identicalOld == 0) {
				// System.out.println("SPAJAM NAJSLIČNIJE PO DULJINI");
				dataModel.addConnection(mostSimilarKeys1, mostSimilarKeys2);
			}

		}
		return 0;
	}// ovo je zatvorena zagrada za metodu
}// ovo je zatvorena zagrada za klasu
