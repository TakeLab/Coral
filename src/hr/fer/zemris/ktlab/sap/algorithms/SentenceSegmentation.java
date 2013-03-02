package hr.fer.zemris.ktlab.sap.algorithms;

import hr.fer.zemris.ktlab.sap.util.DataModel;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa koja će se brinuti za segmentaciju određenog elementa na rečenice.
 * Segmentacija se odvija tako da prolazi kroz tekst slovo po slovo i u
 * varijablu <code>predecessor</code> sprema riječ po riječ, sve dok ne naiđe
 * na neki interpunkcijski znak koji označava kraj rečenice, ili točku. Ako
 * naiđe na točku, traži prvu riječ koja nailazi nakon točke i sprema je u
 * varijablu <code>follower</code>, te uspoređuje s varijablom
 * <code>predecessor</code> i na temelju usporedbe te dvije varijable obavlja
 * se segmentacija.
 *
 *@author Renato Dragišić, Marin Japec
 */

public class SentenceSegmentation {

	/**
	 * Globalna varijabla koja označava preciznost segmentacije
	 */
	public boolean safe = true;
	
	/**
	 * Globalna varijabla kojom se provjerava parnost pojavljivanja navodnika u elementu
	 */
	static int parity=0;

	/**
	 * Skup u koji ćemo učitavati kratice
	 */
	private Set<String> abbs = new HashSet<String>();

	/**
	 * Metoda koja učitava kratice iz txt datoteke u skup kratica.
	 * 
	 * @param file
	 *            datoteka u kojoj se nalaze kratice
	 * @throws IOException 
	 */
	public void loadAbbreviations(String file)
			throws IOException {

		BufferedReader abbreviation = null;

		abbreviation = new BufferedReader(new InputStreamReader(
				new FileInputStream("./acronyms/" + file), "UTF8"));

		try {
			while (true) {

				String line = abbreviation.readLine();

				if (line == null) {
					break;
				}
				abbs.add(line);

			}
		} finally {
			try {
				if (abbreviation != null) {
					abbreviation.close();
				}
			} catch (IOException ignorable) {
			}
		}

	}

	/**
	 * Metoda koja pronalazi sljedeću riječ u slučaju da algoritam segmentacije
	 * naiđe na točku
	 * 
	 * @param counter
	 *            brojač slova u tekstu
	 * 
	 * @param a
	 *            komad teksta na kojem se trenutno primjenjuje algoritam
	 * 
	 * @return riječ(ili znak) koja slijedi nakon točke
	 */

	public String findFollower(int counter, char[] a) {
		
		int k = counter + 1;
		int j = 0;
		String follower;
		if (k >= a.length) {
			return "";
		}

		char[] array1 = new char[100];

		while ((Character.isSpaceChar(a[k])) || (a[k] == '\r')
				|| (a[k] == '\n')) {

			k++;
			if (k >= a.length)
				return "";
		}

		while (Character.isLetterOrDigit(a[k]) || a[k] == '-' || a[k] == ','
				|| a[k] == ';') {
			array1[j] = a[k];
			if (k+1 >= a.length) break;
			k++;
			j++;
		}
		if (a[k] == '(' || a[k] == ')' || a[k] == '[' || a[k] == ']' ) {
			array1[j] = a[k];
			j++;
		}
		if (a[k] == '"') {
			array1[j] = a[k];
			j++;
			parity++;
		}
		
		char[] b = new char[j];
		for (k = 0; k < j; k++)
			b[k] = array1[k];

		follower = String.copyValueOf(b);
		return follower;
	}

	/**
	 * Metoda koja traži mjesto na kojem treba podijeliti određeni dio teksta.
	 * Ukoliko naiđe na sigurne oznake kraja rečenice, odmah će vratiti mjesto
	 * na kojem se one nalaze, a ako naiđe na točku vratit će mjesto podijele u
	 * odnosu na riječi(ili znakove) koje se nalaze prije i nakon točke.
	 * 
	 * @param textPiece
	 *            dio teksta na kojem tražimo mjesto podjele
	 * 
	 * @return mjesto podjele ili -1 u slučaju da rečenicu ne treba dijeliti
	 */
	public int splitSearch(String textPiece) {
		
		int i = 0, k, j = 0, indicator = 2;
		char[] textCharArray;
		char[] array0;

		textCharArray = textPiece.toCharArray();
		array0 = new char[1000];

		for (i = 0; i < textCharArray.length; ++i) {
			
			if (i + 1 >= textCharArray.length) {
				return -1;
			}
			
			if ((textCharArray[i] == '\r' || textCharArray[i] == '\n')
					&& !(textCharArray[i + 1] == '\r' || textCharArray[i + 1] == '\n')
					&& !(textCharArray[i - 1] == '\r' || textCharArray[i - 1] == '\n'))
				return i;

			if (Character.isLetterOrDigit(textCharArray[i])
					|| textCharArray[i] == '(' || textCharArray[i] == ')'
					|| textCharArray[i] == '[' || textCharArray[i] == ']'	
					//|| textCharArray[i] == '–' || textCharArray[i] == ','
					|| textCharArray[i] == ';' || textCharArray[i] == '"'
					//|| textCharArray[i] == '\''
						)
					{
			
				array0[j] = textCharArray[i];
				j++;

				continue;
			}

			if (i >= textCharArray.length) {
				return -1;
			}
			char[] b = new char[j];
			for (k = 0; k < j; k++)
				b[k] = array0[k];

			array0 = new char[1000];
			j = 0;

			String predecessor = String.copyValueOf(b);

			if ((textCharArray[i] == '!') || (textCharArray[i] == '?')) {
				if (i + 1 >= textCharArray.length){
					if (textCharArray[i + 1] == '"' || textCharArray[i + 1] == '\''
					|| textCharArray[i + 1] == ')' || textCharArray[i + 1] == ']') continue;
					else return i;
				}
			}

			if ((textCharArray[i] == ')') || (textCharArray[i] == '(')
					|| (textCharArray[i] == '-')) {
				i++;
				if (i >= textCharArray.length)
					continue;
			}

			if (textCharArray[i] == '.') {

				// Ako iza jedne točke dođe druga točka onda gledamo da li
				// postoji
				// još jedna točka iza nje ( tri točke ukupno). Ako ne, onda
				// javljamo safe=false i vraćamo i+1

/*dodano				if (((i + 1) < textCharArray.length)
						&& (textCharArray[i + 1] == '.')) {
					if (textCharArray[i + 2] != '.') {
						safe = false;
						return i + 1;
					}
					return i + 2;
				}
dodano */
				if (i + 1 >= textCharArray.length) {
					return -1;
				}
				if (textCharArray[i - 1] == ')') {
					return i;
				}
				
				String follower = findFollower(i, textCharArray);

				if (follower.equals("") || predecessor.equals(""))
					continue;
				
				if (follower.equals("\"")) {
					if (parity % 2 == 1) return i+1;
					else continue;
				}
				
				char firstCharOfPredecessor = predecessor.charAt(0);
				char firstCharOfFollower = follower.charAt(0);
				int followerLength = follower.length();
				int predecessorLength = predecessor.length();
				if ((firstCharOfFollower == '\n')
						|| (firstCharOfFollower == '\n'))
					continue;
				// ako je prvi znak prethodnika broj onda indicator= 0,
				// ako je prvi znak prethodnika slovo onda indicator=1.
				if (Character.isDigit(firstCharOfPredecessor))
					indicator = 0;
				else if (Character.isLetter(firstCharOfPredecessor))
					indicator = 1;

				if (predecessor.equals(")") || follower.equals("(") 
					|| predecessor.equals("]") || follower.equals("["))
					return i;

				if (firstCharOfPredecessor == '('
						&& Character.isUpperCase(firstCharOfFollower)
						&& (followerLength < 2)) {
					safe = false;
					return -1;
				}

				if (follower.equals(")")) continue;
					if (abbs.contains(predecessor)) continue; 
						switch (indicator) {
						case 0: {
							if (Character.isDigit(firstCharOfFollower))
								break;

							if ((Character.isUpperCase(firstCharOfFollower))
									&& (followerLength < 2)) {
								safe = false;
								break;
							}
							if (Character.isLowerCase(firstCharOfFollower))
								break;
							if (firstCharOfFollower == ','
									|| firstCharOfFollower == '-')
								break;

						}
						case 1:
							if (Character.isUpperCase(firstCharOfFollower) && (followerLength < 2)
									&& predecessorLength<2) {
								safe = false;
								break;
							}
							if ((Character.isLowerCase(firstCharOfFollower))){
								safe = false;
								break;
							}
							if (firstCharOfFollower == ','
									|| firstCharOfFollower == '-')
								break;
						default:
							return i;
						}
				}
		}
		safe = false;
		return -1;

	}

	/**
	 * Glavni dio programa koji preuzima listu ključeva iz modela elemenata i
	 * dohvaća tekst za svaki ključ u listi. Za svaki komad teksta poziva
	 * algoritam koji traži mjesto podjele i na temelju povratne vrijednosti iz
	 * tog algoritma dijeli tekst na dva dijela ili prelazi na novi komad teksta
	 */

	public static void main(String[] args) {

		DataModel model = new DataModel();
		SentenceSegmentation segmentation = new SentenceSegmentation();
		List<Integer> list1 = model.getKeys1();
		for (int i = 0; i < list1.size(); i++) {
			// dohvati tekst ključa koji se nalazi na i-toj poziciji liste
			int key = list1.get(i);
			String text = model.getElement(key);
			try {
				segmentation.loadAbbreviations("Hrvatski.txt");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int splitPosition = segmentation.splitSearch(text);
			if (splitPosition == -1) {
				continue;
			}
			model.splitElement(key, splitPosition + 1, segmentation.safe);
			segmentation.safe = true;
			}

	}

}
