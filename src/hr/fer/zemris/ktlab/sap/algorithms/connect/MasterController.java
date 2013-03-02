package hr.fer.zemris.ktlab.sap.algorithms.connect;

import java.util.List;

import hr.fer.zemris.ktlab.sap.util.DataModel;

/**
 * Klasa MasterController upravlja radom cijelog algoritma. U beskonačnoj petlji
 * pozivaju se metode iz drugih klasa koje vrše dohvat i analizu rečenice. Prvo
 * se poziva KeyManipulator koji vraća MasterControlleru ključeve koji se moraju
 * obraditi (max.3 za lijevu i desnu stranu) u obliku liste listi. Zatim se
 * poziva SentenceProperties klasa u kojoj se obavlja analiza i spajanje
 * rečenice. Trenutno se spajaju samo 1 na 1 ključevi. Ako algoritam zaključi da
 * je došlo do previše pogreški, javlja MasterControlleru. MasterController
 * nasilno prekida izvođenje programa. U tom slučaju, zbog prirode algoritma,
 * nekoliko (2-3) rečenica na kraju teksta zbog tog prekida ostane nepovezano.
 * Algoritam iz beskonačne petlje na prirodan način izlazi kad se sve rečenice
 * spoje, tj. kad više nema rečenica za obradu.
 * 
 * @author Marin Japec
 */

public class MasterController {

	public List<Integer> list1;
	public List<Integer> list2;
	public List<List<Integer>> list;

	/**
	 * Objekt tipa DataModel
	 */
	private DataModel dataModel;

	/**
	 * Javni konstruktor klase <code>MasterController</code>
	 * 
	 * @param dataModel
	 *            referenca na objekt tipa DataModel
	 */
	public MasterController(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * Metoda kojom započinje rad algoritma. U beskonačnoj
	 * petlji nalaze se pozivi metoda koje dohvaćaju i analiziraju rečenice.
	 */
	public void start() {
		SentenceProperties properties = new SentenceProperties(dataModel);
		KeyManipulator manipulator = new KeyManipulator(dataModel);
		
		//brojač ciklusa 
		int numberOfCall = 0;
		
		while (true) {

			// poziva se metoda koja dohvaća ključeve
			list = manipulator.getKeys();

			// prirodan kraj algoritma
			if (list.isEmpty() || numberOfCall > 10000) {
				break;
			}

			list1 = list.get(0);
			list2 = list.get(1);

			// poziva se metoda koja analizira rečenice
			int errorControl = properties.analyzer(list1, list2);

			// nasilni kraj algoritma
			if (errorControl != 0) {
				// System.out.println("Algorithm error! Exiting...");
				break;
			}

			// informativno
			// broj ciklusa potrebnih algoritmu za obradu teksta
			numberOfCall++;
			// System.out.println("Number of call:" + numberOfCall);
		}

	}

}
