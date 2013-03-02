package hr.fer.zemris.ktlab.sap.algorithms.connect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.ktlab.sap.util.DataModel;

/**
 * Klasa KeyManipulator zadužena je za obrađujivanje dostupnih ključeva. Dohvaća
 * liste (lijevih i desnih)ključeva koji se trebaju u sljedećem ciklusu
 * obraditi.
 * 
 */

public class KeyManipulator {

	/**
	 * Lista u kojoj se spremaju liste lijevih i desnih ključeva koji se trebaju
	 * poslati na analizu
	 */

	public List<List<Integer>> list;

	/**
	 * Objekt tipa DataModel
	 */
	private DataModel dataModel;

	/**
	 * Lista kljuceva koja počinje kao lista ključeva u jeziku L1 a zatim se
	 * smanjuje što se više elemenata iz L1 spoji. Iz ove se liste šalju
	 * ključevi na spajanje.
	 */
	private List<Integer> localKeys1;

	/**
	 * Lista kljuceva koja počinje kao lista ključeva u jeziku L2 a zatim se
	 * smanjuje što se više elemenata iz L1 spoji.Iz ove se liste šalju ključevi
	 * na spajanje.
	 */
	private List<Integer> localKeys2;

	/** Privremena lista ključeva za jezik L1 */
	public List<Integer> tempKeys1;

	/** Privremena lista ključeva za jezik L2 */
	public List<Integer> tempKeys2;

	/**
	 * Javni konstruktor razreda <code>KeyManipulator</code>. Kao argument
	 * prima referencu na objekt tipa DataModel na kojem ce se provesti proces
	 * sravnjivanja elemenata.
	 * 
	 * @param x
	 *            referenca na objekt tipa DataModel
	 */
	public KeyManipulator(DataModel x) {
		this.dataModel = x;
		this.localKeys1 = new ArrayList<Integer>();
		this.localKeys1.addAll(dataModel.getKeys1());
		this.localKeys2 = new ArrayList<Integer>();
		this.localKeys2.addAll(dataModel.getKeys2());
		this.list = new ArrayList<List<Integer>>();
		this.tempKeys1 = new LinkedList<Integer>();
		this.tempKeys2 = new LinkedList<Integer>();
	}

	/**
	 * Javna metoda koja prvo obriše sve ključeve rečenica koje su povezane, a
	 * zatim dohvaća (maksimalno) 3 ključa sa lijeve i desne strane i sprema ih
	 * u listu listi i šalje ju MasterControlleru
	 * 
	 * @return list Lista u kojoj su spremljene dvije liste ključeva
	 * 
	 */
	public List<List<Integer>> getKeys() {

		if (!(list.isEmpty())) {
			// System.out.println("Brišem listu");
			list.clear();
		}

		tempKeys1.clear();
		tempKeys2.clear();

		while (localKeys1.size() != 0 && localKeys2.size() != 0) {

			Iterator<Integer> it = localKeys1.iterator();
			int done = 0;

			while (done < 3 && it.hasNext()) {
				Integer key = it.next();

				done++;

				if (hasConnections(key)) {
					it.remove();
					// informativno
					// System.out.println("Obrisali smo ključ iz PRVE liste,
					// kljuc: "+ key);
				}
			}

			it = localKeys2.iterator();
			done = 0;

			while (done < 3 && it.hasNext()) {

				Integer key = it.next();

				done++;

				if (hasConnections(key)) {
					it.remove();
					// informativno
					// System.out.println("Obrisali smo ključ iz DRUGE liste,
					// kljuc: "+ key);
				}
			}

			// drugi način brisanja ključeva koji su povezani. Potrebno
			// dodati if uvjete za moguće veličine lista i napraviti za
			// svaki uvjet posebno remove. ideja: obrisati i ključ(eve)
			// iznad ključa koji ima vezu.tako će se očuvati postojanost.
			/*
			 * for(int i=0;i<3;++i){ if(hasConnections(localKeys1.get(i))){
			 * localKeys1.remove(i); } } for(int i=0;i<3;++i){
			 * if(hasConnections(localKeys2.get(i))){ localKeys2.remove(i); } }
			 */

			// dodavanje do max 3 ključa u tempKeys1 i tempKeys2
			if (localKeys1.size() >= 3) {
				for (int k = 0; k < 3; ++k) {
					Integer tempKey1 = localKeys1.get(k);
					tempKeys1.add(tempKey1);
				}

			}
			if (localKeys1.size() == 2) {
				for (int k = 0; k < 2; ++k) {
					Integer tempKey1 = localKeys1.get(k);
					tempKeys1.add(tempKey1);
				}
			}
			if (localKeys1.size() == 1) {
				for (int k = 0; k < 1; ++k) {
					Integer tempKey1 = localKeys1.get(k);
					tempKeys1.add(tempKey1);
				}
			}
			if (localKeys2.size() >= 3) {
				for (int k = 0; k < 3; ++k) {
					Integer tempKey2 = localKeys2.get(k);
					tempKeys2.add(tempKey2);
				}

			}
			if (localKeys2.size() == 2) {
				for (int k = 0; k < 2; ++k) {
					Integer tempKey2 = localKeys2.get(k);
					tempKeys2.add(tempKey2);
				}
			}
			if (localKeys2.size() == 1) {
				for (int k = 0; k < 1; ++k) {
					Integer tempKey2 = localKeys2.get(k);
					tempKeys2.add(tempKey2);
				}
			}

			list.add(tempKeys1);
			list.add(tempKeys2);
			return list;

		}// ovdje je kraj while uvjeta

		return list;

	}

	/**
	 * Privatna metoda koja za dani kljuc elementa provjerava je li element s
	 * tim kljucem vec povezan s nekim elementima u drugom jeziku.
	 * 
	 * @param key
	 *            Integer vrijednost kljuca za kojeg se vrsi provjera
	 * @return Boolean povratna vrijednost: <code>0</code> ako element nije ni
	 *         s kim spojen <code>0</code> ako element vec jest s nekim spojen
	 */
	public boolean hasConnections(int key) {
		Set<Integer> currentConnections;
		currentConnections = dataModel.getConnections(key);
		if (currentConnections == null) {
			return false;
		}
		if (currentConnections.isEmpty() == true) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * informativno @SuppressWarnings("unused") private void printAll() {
	 * System.out.println("LocalKeys1:"); for (Integer key : localKeys1) {
	 * System.out.println(key + ", connections: " +
	 * dataModel.getConnections(key)); }
	 * 
	 * System.out.println("LocalKeys2:"); for (Integer key : localKeys2) {
	 * System.out.println(key + ", connections: " +
	 * dataModel.getConnections(key)); } }// kraj metode printAll
	 */
}// kraj klase
