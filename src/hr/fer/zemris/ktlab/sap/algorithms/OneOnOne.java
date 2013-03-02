package hr.fer.zemris.ktlab.sap.algorithms;

import java.util.List;

import hr.fer.zemris.ktlab.sap.util.DataModel;

/**
 * Klasa predstavlja jednostavan algoritam za sravnjivanje. Ovaj algoritam nije
 * razvijen s namjerom da nešto uspješno sravni. Njegova glavna namjena je bilo
 * testiranje grafičkog sučelja u ranijim fazama razvoja.
 * 
 * @author Igor Šoš
 * 
 */
public class OneOnOne {

	/** Podatkovni model. */
	DataModel dataModel;

	/**
	 * Javni konstruktor.
	 * @param dataModel Podatkovni model.
	 */
	public OneOnOne(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * Metoda obavlja sravnjivanje.
	 */
	public void Connect() {
		List<Integer> list1 = dataModel.getKeys1();
		List<Integer> list2 = dataModel.getKeys2();

		int min = (list1.size() < list2.size()) ? list1.size() : list2.size();
		for (int i = 0; i < min; i++) {
			dataModel.addConnection(list1.get(i), list2.get(i));
		}
	}

}
