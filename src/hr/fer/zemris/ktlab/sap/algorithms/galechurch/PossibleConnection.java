package hr.fer.zemris.ktlab.sap.algorithms.galechurch;

import java.util.*;

/**
 * Razred sadrzi privatne podatkovne clanove <code>probability, connectionCase</code>
 * i <code>elementsInConnection</code>.
 * <code>probability</code> 	
 * 				sadrzi vjerojatnost da je potencijalni spoj ispravan  
 * <code>connectionCase</code>	
 * 				sadrzi int vrijednost kojom se oznacava slucaj spoja
 * <code>elementsInConnection</code>
 * 				lista objekata ElementInfo koji opisuju element koji 
 * 				bi se postavio u potencijalni spoj
 * Za <code>probability</code> i <code>connectionCase</code> su 
 * definirane <code>get</code> i <code>set</code> metode, a za 
 * <code>elementsInConnection</code> je definirana metoda za dodavanje 
 * novog objekta tipa ElementInfo i za dohvacanje cijele liste <code>elementsInConnection</code> 
 *
 */
public class PossibleConnection {
	
	/**
	 * Sadrzi vjerojatnost spoja
	 */
	private double probability;
	
	/**
	 * Sadrzi tip spoja u zapisu s formatom: 
	 * 		11 za ( 1:1 )
	 * 		12 za ( 1:2 ) i ( 2:1 )
	 * 		22 za ( 2:2 )
	 */
	private int    connectionCase;
	
	/** 
	 * Sadrzi reference na kljuceve elemenata koji bi se nasli u spoju
	 * s gore navedenom vjerojatnoscu i u gore navedenom slucaju.
	 */
	private LinkedList<Integer> keysInConnection;
	
	/**
	 * Javni konstruktor koji ne prima nikakav argument i inicijalizira
	 * polja <code>possibility</code> i <code>connectionCase</code> na nulu, i
	 * stvori novu, praznu listu <code>elementsInConnection</code>
	 */
	public PossibleConnection() {
		this.probability = 0;
		this.connectionCase = 0;
		this.keysInConnection = new LinkedList<Integer>();
	}
	
	/**
	 * Postavlja podatkovni clan probability na zadanu vrijednost
	 * @param x
	 * 			Vrijednost na koju se postavlja probability
	 */
	public void setProbability (double x) {
		this.probability = x;
	}
	
	/**
	 * Javna metoda koja cita iz objekta njegov privatni clan probability
	 * @return
	 * 			probability zadanog PossibleConnectiona
	 */			
	public double getProbability () {
		return this.probability;
	}
	
	/**
	 * Postavlja slucaj na vrijednost argumenta. 
	 * @param x
	 * 			Argument koji poprima vrijednosti 11, 12, 22 za slucajeve
	 *			( 1:1 ), ( 1:2 )&( 2:1 ), te ( 2:2 )			
	 */
	public void setconnectionCase(int x) {
		this.connectionCase= x;
	}
	
	/**
	 * Javna metoda koja cita iz objekta njegov privatni clan slucaj
	 * @return
	 * 			Slucaj pod koji potpada ovaj PossibleConnection
	 */			
	public int getconnectionCase() {
		return this.connectionCase;
	}

	/**
	 * Javna metoda koja predaje cijelu listu elemenata koji su potencijalno
	 * spojivi.
	 * @return
	 * 			Vezana lista koja sadrzi kljuceve elemenata u potencijalnom 
	 * 			spoju.
	 */
	public LinkedList<Integer> getkeysInConnection() {
		return keysInConnection;
	}
	

}
