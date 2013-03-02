package hr.fer.zemris.ktlab.sap.algorithms.galechurch;

/**
 * Razred koji sadrzi neke osnovne informacije o elementu (segmentu) teksta
 * koji se u danom trenutku obradjuje u procesu sravnjivanja.
 */

public class ElementInfo {
	
	/**
	 * Sadrzi kljuc elementa
	 */
	private int elementKey;
	
	/**
	 * Sadrzi pripadnost elementa skupu 1 (jezik L1) ili skupu 2 (jezik L2)
	 * 		<code>true</code> ako je Element iz jezika L1
	 * 		<code>false</code> ako je Element iz jezika L2
	 */
	private boolean elementSet;
	
	/**
	 * Sadrzi broj znakova od kojih se sastoji element.
	 */
	private int elementLength;
	
	/** 
	 * Javna metoda koja postavlja podatkovni clan elementKey na zadanu vrijednost.
	 * @param x
	 * 			Argument je novi kljuc tipa int
	 */
	public void setelementKey(int x) {
		this.elementKey = x;
	}
	
	/**
	 * Javna metoda koja vraca vrijednost podatkovnog clana elementKey.
	 * @return
	 * 			Vrijednost koja se vraca je tipa int
	 */
	public int getelementKey() {
		return this.elementKey;
	}
	
	/** 
	 * Javna metoda koja postavlja podatkovni clan elementSet na zadanu 
	 * vrijednost.
	 * @param x
	 * 			Argument je nova pripadnost skupu (jeziku) u obliku 
	 * 			boolean vrijednosti
	 */
	public void setelementSet(boolean x) {
		this.elementSet = x;
	}
	
	/**
	 * Javna metoda koja vraca vrijednost podatkovnog clana elementSet.
	 * @return
	 * 			Vrijednost koja se vraca je pripadnost skupu (jeziku) 
	 * 			u obliku boolean vrijednosti 
	 */
	public boolean getelementSet() {
		return this.elementSet;
	}
	
	/**
	 * Postavlja duljinu elementa <code>elementLength</code> na vrijednost 
	 * argumenta.
	 * @param x
	 * 			Argument (int)
	 */
	public void setelementLength(int x) {
		this.elementLength = x;
	}
	
	/**
	 * Vraca duljinu ovog elementa
	 * @return
	 * 		Integer vrijednost duljine elementa
	 */
	public int getelementLength() {
		return this.elementLength;
	}
	
	/** 
	 * Javni konstruktor razreda ElementInfo
	 * @param x
	 * 			Vrijednost na koju se inicijalizira clan elementKey
	 * @param y
	 * 			Vrijednost na koju se inicijalizira clan elementSet
	 */
	public ElementInfo(int key, boolean set, int length) {
		this.elementKey = key;
		this.elementSet = set;
		this.elementLength = length;
	}

}
