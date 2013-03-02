package hr.fer.zemris.ktlab.sap.algorithms.galechurch;

import hr.fer.zemris.ktlab.sap.util.DataModel;
import java.util.List;
import java.util.Set;


/**
 * Upravljacki razred u procesu sravnjivanja. Pocevsi od prvog elementa u 
 * jeziku L1, za svaki element poziva metode razredâ Handler11, Handler12,
 * itd. te stvara veze elemenata jezika L1 s odgovarajucim elementima 
 * jezika L2.
 *
 */
public class Controller {
	
	/**
	 * Objekt tipa DataModel
	 */
	private DataModel dataModel;
	
	/**
	 * Broj odlomaka u tekstu na jeziku L1
	 */
	private int numberOfParagraphs1;
	
	/**
	 * Broj odlomaka u tekstu na jeziku L2
	 */
	private int numberOfParagraphs2;
		
	/**
	 * Privatni podatkovni clan koji sadrzi broj elemenata (recenica) u 
	 * tekstu na jeziku L1
	 */
	private int numberOfElements1;
	
	/**
	 * Privatni podatkovni clan koji sadrzi broj elemenata (recenica) u 
	 * tekstu na jeziku L2
	 */
	private int numberOfElements2;
	
	/**
	 * Privatni podatkovni clan koji pokazuje na listu kljuceva elemenata
	 * u jeziku L1
	 */
	private List<Integer> keys1;
	
	/**
	 * Privatni podatkovni clan koji pokazuje na listu kljuceva elemenata
	 * u jeziku L2
	 */
	private List<Integer> keys2;

	
	
	
	/** 
	 * Javni konstruktor razreda <code>Controller</code>
	 * Kao argument prima referencu na objekt tipa DataModel na kojem ce se
	 * provesti proces sravnjivanja elemenata (recenica).
	 * @param x
	 * 			referenca na objekt tipa DataModel
	 */
	public Controller(DataModel x) {
		this.dataModel = x;
		// Dohvacanje kljuceva iz DataModel objekta
		this.keys1 = dataModel.getKeys1();
		this.keys2 = dataModel.getKeys2();
		// Dohvacanje broja elemenata u tekstovima na jezicima L1 i L2
		this.numberOfElements1 = keys1.size();
		this.numberOfElements2 = keys2.size();
		// Dohvacanje broja odlomaka u tekstovima na jezicima L1 i L2
		// (Pretpostavka da brojevi odlomaka pocinju od 0)
		this.numberOfParagraphs1 = x.getParagraphForElement( 
										keys1.get(numberOfElements1-1)) +1;
		this.numberOfParagraphs2 = x.getParagraphForElement(
										keys2.get(numberOfElements2-1)) +1;
		
	}
	
	
	
	/**
	 * Privatna metoda koja za dani kljuc elementa provjerava je li element s
	 * tim kljucem vec povezan s nekim elementima u drugom jeziku.
	 * @param key
	 * 			Integer vrijednost kljuca za kojeg se vrsi provjera
	 * @return
	 * 			Boolean povratna vrijednost:
	 * 				<code>0</code> ako element nije ni s kim spojen
	 * 				<code>0</code> ako element vec jest s nekim spojen
	 */
	private boolean hasConnections(int key) {
		Set<Integer> currentConnections;
		currentConnections = dataModel.getConnections(key);
		if (currentConnections.isEmpty()==true) {
			return false;
		} else {
			return true;
		} 
	}
	
	/**
	 * Privatna metoda koja izvrsava spajanje elemenata u DataModel objektu
	 * prema argumentu koji je dobila u obliku objekta tipa PossibleConnection.
	 * @param x
	 * 			Argument je objekt tipa PossibleConnection. Ovisno o vrijednosti
	 * 			njegovog polja <code>connectionCase</code> izvrsava se spajanje
	 * 			elemenata u njegovoj listi <code>elementsInConnection</code>. U
	 * 			toj listi su na prvim mjestima element(i) iz jezika L1, a nakon
	 * 			njega (ili njih) oni elementi koji su iz jezika L2
	 */
	private void createConnection(PossibleConnection x) {
		
		List<Integer> toBeConnected = x.getkeysInConnection();
		switch (x.getconnectionCase()) {
			case 11: {// Spaja jedan element iz L1 s jednim elementom iz L2
				dataModel.addConnection( toBeConnected.get(0), toBeConnected.get(1) );
			} break;
		
			case 12: { // Spaja 1 element iz jezika L1 s dvama elementima iz jezika L2
				dataModel.addConnection( toBeConnected.get(0), toBeConnected.get(1) );
				dataModel.addConnection( toBeConnected.get(0), toBeConnected.get(2) );
			} break;
			
			case 21: { // Spaja 2 elementa iz jezika L1 s jednim, istim elementom iz jezika L2
				dataModel.addConnection( toBeConnected.get(0), toBeConnected.get(2) );
				dataModel.addConnection( toBeConnected.get(1), toBeConnected.get(2) );
			} break;
			
			case 22: { // Spaja 2 elementa iz jezika L1 s dvama elementima iz jezika L2
				dataModel.addConnection( toBeConnected.get(0), toBeConnected.get(2) );
				dataModel.addConnection( toBeConnected.get(0), toBeConnected.get(3) );
				dataModel.addConnection( toBeConnected.get(1), toBeConnected.get(2) );
				dataModel.addConnection( toBeConnected.get(1), toBeConnected.get(3) );
			} break;
		}
	}
	
	public int findfirstInParagraph(int i, int j) {
		int i2 = i;
		// Trazenje pocetka odgovarajuceg odlomka u L2
		if (dataModel.getParagraphForElement( keys2.get(i2) )>=j) {
			while (dataModel.getParagraphForElement( keys2.get(i2) )>=j) {
				if ( (i2-1)>=0 ) { --i2; }
				else { return i2; }
			}
			++i2;
		} else {
			while (dataModel.getParagraphForElement( keys2.get(i2) )<j) {
				if ( (i2+1)<keys2.size() ) { ++i2; }
				else { return i2; }
			}
		}  // U i2 se sad nalazi redni broj u listi keys2 od
		   // prvog elementa u odgovarajucem odlomku 
		return i2;
		
	}
	
	
	/**
	 * Javna metoda koja pokrece postupak sravnjivanja prema Gale&Church
	 * algoritmu. Ovisno o primljenim parametrima pokrece odgovarajuce 
	 * metode koje rade sam posao.
	 * @param width
	 * 				Sirina prozora koji ce se pretrazivati u jeziku L2
	 * @param ignoreParagraphs
	 * 				Boolean vrijednost koja govori treba li ignorirati granice
	 *  			odlomaka ili treba izvoditi sravnjivanje iskljucivo unutar
	 *  			odlomaka. 
	 * 
	 */	
	public void startAlignment(int width, boolean ignoreParagraphs) {
		if (ignoreParagraphs == true) {
			alignIgnoringParagraphs(width);
		} else {
			alignInsideParagraphs(width);
		}
	}

	/**
	 * Privatna metoda koja pokrece postupak sravnjivanja svih elemenata u 
	 * objektu tipa DataModel. Moguci su spojevi iskljucivo unutar 
	 * odgovarajucih odlomaka u jezicima L1 i L2.
	 * @param width
	 * 				Sirina prozora koji ce se pretrazivati u jeziku L2
	 */
	private void alignInsideParagraphs(int width) {		
		// Brojac elemenata
		int i = 0;
		// Brojac odlomaka
		int j = 0;
		
		// Niz od 4 objekta tipa PossibleConnection. Svaki ce sadrzavati 
		// najvjerojatniji spoj unutar svog slucaja. 
		// 0. clan niza odgovara slucaju [1-1], 1. clan slucaju [1-2]
		// 2. clan slucaju [2-1], 3. clan slucaju [2-2]
		PossibleConnection caseSeparatedPossibleConnection[] = new PossibleConnection[4];
		// Stvaranje Handler objekata. Drugi argument je sirina njihove pretrage
		Handler11 handler11 = new Handler11(dataModel, width, keys1, keys2);
		Handler12 handler12 = new Handler12(dataModel, width, keys1, keys2);
		Handler21 handler21 = new Handler21(dataModel, width, keys1, keys2);
		Handler22 handler22 = new Handler22(dataModel, width, keys1, keys2); 
		
		for (j=0; j<numberOfParagraphs1; ++j) {
			// Redni broj elementa u odlomku koji se trenutno obradjuje
			int positionInParagraph = 0; 
			// Nadjemo pocetak ovog odlomka u L2
			int firstInParagraph = findfirstInParagraph(i, j);
			
			while ( dataModel.getParagraphForElement( keys1.get(i) )==j ) {
				
				caseSeparatedPossibleConnection[0] = handler11.engageInsideParagraphs(
													 i, j, firstInParagraph, positionInParagraph);
				caseSeparatedPossibleConnection[1] = handler12.engageInsideParagraphs(
													 i, j, firstInParagraph, positionInParagraph);
				caseSeparatedPossibleConnection[2] = handler21.engageInsideParagraphs(
													 i, j, firstInParagraph, positionInParagraph);
				caseSeparatedPossibleConnection[3] = handler22.engageInsideParagraphs(
													 i, j, firstInParagraph, positionInParagraph);
				
				
				// Trazenje najvjerojatnijeg PossibleConnection-a
				PossibleConnection mostProbable = caseSeparatedPossibleConnection[0];
				// for petlja ide od 1 do 3, preskace 0. u nizu jer je on vec u mostProbable-u
				for (int k=1; k<4; ++k) {
					if (caseSeparatedPossibleConnection[k].getProbability()>mostProbable.getProbability()) {
						mostProbable = caseSeparatedPossibleConnection[k];
					}
				}
				// Ako su vjerojatnosti spajanja u svim slucajevima jednake nuli,
				// tada se ne izvrsava nikakvo spajanje.
				if (mostProbable.getProbability() > 0) { 	
					createConnection(mostProbable);
				}	
				// Uvecavanje brojaca za elemente L1
				if ( (i+1)>=keys1.size() ) break;
				++i;
				// Uvecavanje brojaca elemenata unutar odlomka u jeziku L1
				++positionInParagraph;
			}
		}
	}

	/**
	 * Privatna metoda koja pokrece postupak sravnjivanja svih elemenata u 
	 * objektu tipa DataModel ignorirajuci granice odlomaka. Moguci su spojevi
	 * recenica koje pripadaju razlicitim odlomcima.
	 * @param width
	 * 				Sirina prozora koji ce se pretrazivati u jeziku L2
	 */
	private void alignIgnoringParagraphs(int width) {
		// Brojac koji se koristi za prolaz po lijevoj listi elemenata
		int i;
		
		// Niz od 4 objekta tipa PossibleConnection. Svaki ce sadrzavati 
		// najvjerojatniji spoj unutar svog slucaja. 
		// 0. clan niza odgovara slucaju [1-1], 1. clan slucaju [1-2]
		// 2. clan slucaju [2-1], 3. clan slucaju [2-2]
		PossibleConnection caseSeparatedPossibleConnection[] = new PossibleConnection[4];
		
		// Stvaranje Handler objekata. Drugi argument je sirina njihove pretrage
		Handler11 handler11 = new Handler11(dataModel, width, keys1, keys2);
		Handler12 handler12 = new Handler12(dataModel, width, keys1, keys2);
		Handler21 handler21 = new Handler21(dataModel, width, keys1, keys2);
		Handler22 handler22 = new Handler22(dataModel, width, keys1, keys2); 
		
		for (i=0; i<numberOfElements1; ++i) {
			
        	caseSeparatedPossibleConnection[0] = handler11.engageIgnoringParagraphs(i);
			caseSeparatedPossibleConnection[1] = handler12.engageIgnoringParagraphs(i);
			caseSeparatedPossibleConnection[2] = handler21.engageIgnoringParagraphs(i);
			caseSeparatedPossibleConnection[3] = handler22.engageIgnoringParagraphs(i);
			
			
			// Trazenje najvjerojatnijeg PossibleConnection-a
			PossibleConnection mostProbable = caseSeparatedPossibleConnection[0];
			
			for (int k=1; k<4; ++k) { // for petlja ide od 1 do 3, preskace 0. u nizu jer je on vec u mostProbable-u
				if (caseSeparatedPossibleConnection[k].getProbability()>mostProbable.getProbability()) {
					mostProbable = caseSeparatedPossibleConnection[k];
				}
			} 
			
			// Ako su vjerojatnosti spajanja u svim slucajevima jednake nuli,
			// tada se ne izvrsava nikakvo spajanje.
			if (mostProbable.getProbability() > 0) { 	
				
				// Spajanje odgovarajucih elemenata u najvjerojatnijem 
				// PossibleConnection-u, tj. u mostProbable-u	
				createConnection(mostProbable);
			}
		}		
	}
	
	
}