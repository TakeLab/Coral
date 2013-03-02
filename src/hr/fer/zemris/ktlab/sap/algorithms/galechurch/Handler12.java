package hr.fer.zemris.ktlab.sap.algorithms.galechurch;

import hr.fer.zemris.ktlab.sap.util.DataModel;

import java.util.List;


/**
 * Razred sluzi pronalasku potencijalnih spojeva tipa ( 1:2 ) za dani element
 * jezika L1. Pri tome se koristi jedna od dvije metode: 
 * 		<code>engageIgnoringParagraphs</code> koja sravnjuje ne uzimajuci u 
 * 											  obzir granice odlomaka
 * 		 <code>engageInsideParagraphs</code>  koja sravnjuje samo unutar
 * 											  odgovarajucih odlomaka.
 * Takodjer, moze se namjestiti i sirina pretrage za potencijalnim spojevima u
 * jeziku L2 pomocu <code>searchWidth</code> argumenta u konstruktoru. 
 * 
 */
public class Handler12 {
	/**
	 * Privatno polje koje sadrzi objekt tipa PossibleConnection. U njemu se
	 * nalazi onaj PossibleConnection objekt koji trenutacno ima najvecu 
	 * vjerojatnost ispravnosti spoja
	 */
	private PossibleConnection mostProbable12;
	
	/** Sadrzi referencu na objekt tipa DataModel cije se elemente sravnjuje */
	private DataModel dataModel;
	
	/** 
	 * Privatna referenca na listu kljuceva elemenata u dataModelu u jeziku L1 
	 */
	private List<Integer> keys1;
	
	/** 
	 * Privatna referenca na listu kljuceva elemenata u dataModelu u jeziku L2 
	 */
	private List<Integer> keys2;
	
	/** Uzevsi element jezika L1 kao fiksnu tocku, u jeziku L2 se ispituju 
	 * spojevi fiksiranog elementa u L1 s elementima udaljenim maksimalno 
	 * <code>searchWidth</code> od ekvivalentnog elementa u jeziku L2.
	 * Postavlja se iskljucivo u konstruktoru.
	 */
	private int searchWidth;
	
	/**
	 * Javni konstruktor koji alocira memoriju za jedan objekt tipa
	 * PossibleConnection i uspostavlja vezu s objektom tipa DataModel s kojim
	 * ovaj Handler11 treba komunicirati. Takodjer postavlja sirinu potrage za
	 * spojevima
	 * @param x  objekt tipa DataModel nad kojim se izvrsava sravnjivanje.
	 * @param y  sirina pretrage za spojevima u jeziku L2
	 */
	public Handler12(DataModel dataModel, 
					 int searchWidth,
					 List<Integer> keys1,
					 List<Integer> keys2) {
		this.mostProbable12 = new PossibleConnection();
		this.dataModel = dataModel;
		this.searchWidth = searchWidth;
		this.keys1 = keys1;
		this.keys2 = keys2;
	}
	
	/**
	 * Metoda pronalazi najvjerojatniji spoj za dani element jezika L1. 
	 * Pri tome nikada ne spaja recenice koje ne pripadaju istom odlomku.
	 * 
	 * @param i
	 * 			Pozicija kljuca elementa u listi kljuceva elemenata jezika L1
	 * @param j
	 * 			Redni broj odlomka koji se trenutno obradjuje
	 * @param firstInParagraph
	 * 			Pozicija prvog elementa u odlomku <code>j</code> u jeziku L2
	 * @param positionInParagraph
	 * 			Redni broj elementa unutar odlomka <code>j</code> koji se
	 * 			trenutno obradjuje
	 * @return
	 * 			Najvjerojatniji spoj (unutar ovog slucaja)
	 */
	public PossibleConnection engageInsideParagraphs(
			int i, int j, int firstInParagraph, int positionInParagraph) {
			/* Inicijalizacija */
		// Slucaj spoja 
		mostProbable12.setconnectionCase(12);      
		// Pretpostavimo da je to nemoguc spoj
		mostProbable12.setProbability(0.0);	       	
		// Ocistimo listu elemenata koji bi se ovim spojem spojili
		mostProbable12.getkeysInConnection().clear(); 
		// Ubacimo element(e) s lijeve strane koji bi se ovim spojem spojio
		// i element(e) s desne strane (inicijalno nepostojeci elementi)
		mostProbable12.getkeysInConnection().add( dataModel.getKeys1().get(i) );
		mostProbable12.getkeysInConnection().add( -1 );
		mostProbable12.getkeysInConnection().add( -1 );
		
		// Ako je element u jeziku L1 vec povezan, onda ne mozemo raditi ovu
		// iteraciju.
		if ( dataModel.getConnections( dataModel.getKeys1().get(i) ).isEmpty()==false ) return mostProbable12;
		
		// Pomocna varijabla za spremanje vjerojatnosti
		double p;
		// "Vadjenje" duljine segmenta u jeziku L1
		int len1 = dataModel.getElement( dataModel.getKeys1().get(i) ).length();
		int len2=0;
		
		// Ova petlja skenira moguce spojeve s elementima udaljenim maksimalno
		// searchWidth mjesta
		for (int b = (-searchWidth); b <= (searchWidth-1); ++b) {
			int i2 = firstInParagraph+positionInParagraph+b;
			// Pokusavamo li adresirati izvan raspona liste jezika L2...
			if ( ( i2<0 ) 
				 | 
				 ( i2+1>=dataModel.getKeys2().size() )
			    )  continue;
			// ...ili izvan odlomka koji se trenutno obradjuje.
			if (
				 ( dataModel.getParagraphForElement( keys2.get(i2  ) )!=j )
				 |
				 ( dataModel.getParagraphForElement( keys2.get(i2+1) )!=j )
				 ) continue;
			
			// Ako je ijedan element s desne strane vec povezan, ne uzimamo ga 
			// u obzir.
			if ( 
				 ( dataModel.getConnections( dataModel.getKeys2().get(i2  ) ).isEmpty()==false )
				 |
				 ( dataModel.getConnections( dataModel.getKeys2().get(i2+1) ).isEmpty()==false )
				) continue;
			
			// "Vadjenje" duljine segmenta koji se promatra u jeziku L2
			// len1 je popunjen jos prije ove petlje.
			len2 =  dataModel.getElement( dataModel.getKeys2().get(i2  ) ).length()
				   +dataModel.getElement( dataModel.getKeys2().get(i2+1) ).length();
			// Racunanje vjerojatnosti tog spoja
			p = ProbabilityComputation.computeP(len1, len2, 12);
			
			if ( p > mostProbable12.getProbability() ) {
				mostProbable12.setProbability(p);
				
				// Izbacuju se stari, manje vjerojatni kljucevi iz L2 s kojima
				// spaja element iz L1...
				mostProbable12.getkeysInConnection().removeLast();
				mostProbable12.getkeysInConnection().removeLast();
				// ... i na njegova mjesta se stave novi, vjerojatniji
				mostProbable12.getkeysInConnection().add( dataModel.getKeys2().get(i2  ) );	
				mostProbable12.getkeysInConnection().add( dataModel.getKeys2().get(i2+1) );
			}
		}
		return mostProbable12;
	}
	
	/**
	 * Metoda kao fiksnu tocku uzima element na rednom broju <code>i</code> u
	 * jeziku L1. Tada usporedjuje vjerojatnosti spoja fiksnog elementa u L1 s
	 * elementima u L2, koji su udaljeni maksimalno <code>searchWidth</code> 
	 * mjesta od elementa jezika L2 ekvivalentnog fiksnom elementu u L1.
	 * @param i	 
	 * 			Redni broj elementa u L1.
	 * @return
	 * 			<code>PossibleConnection</code> objekt koji sadrzi informacije
	 * 			o najvjerojatnijem spoju koji je pronasla metoda.
	 */
	public PossibleConnection engageIgnoringParagraphs(int i) {
			/* Inicijalizacija */
		// Slucaj spoja 
		mostProbable12.setconnectionCase(12);
		// Pretpostavimo da je to nemoguc spoj
		mostProbable12.setProbability(0.0);
		// Ocistimo listu elemenata koji bi se ovim spojem spojili
		mostProbable12.getkeysInConnection().clear();
		// Ubacimo element(e) s lijeve strane koji bi se ovim spojem spojio
		// i element(e) s desne strane (inicijalno nepostojeci elementi)
		mostProbable12.getkeysInConnection().add( dataModel.getKeys1().get(i) );
		mostProbable12.getkeysInConnection().add( -1 );
		mostProbable12.getkeysInConnection().add( -1 );
		
		// Ako je element u jeziku L1 vec povezan, onda ne mozemo raditi ovu
		// iteraciju.
		if ( dataModel.getConnections( dataModel.getKeys1().get(i) ).isEmpty()==false ) return mostProbable12;
		
		
		// Pomocna varijabla za spremanje vjerojatnosti
		double p;
		// "Vadjenje" duljine segmenta u jeziku L1
		int len1 = dataModel.getElement( dataModel.getKeys1().get(i  ) ).length();
		int len2=0;
		
		// Ova petlja skenira moguce spojeve s elementima udaljenim maksimalno
		// searchWidth mjesta
		for (int b = (-searchWidth); b <= (searchWidth-1); ++b) {
			
			// Provjera pokušavamo li adresirati izvan raspona liste
			if ( ((i+b)<0) | ((i+b+1)>=dataModel.getKeys2().size()) ) continue;
			
			// Ako je ijedan element s u jeziku L2 vec povezan, ne uzimamo u 
			// obzir ovaj spoj
			if ( 
				( dataModel.getConnections( dataModel.getKeys2().get(i+b  ) ).isEmpty()==false )
				|
				( dataModel.getConnections( dataModel.getKeys2().get(i+b+1) ).isEmpty()==false )
			) continue;
			
			// "Vadjenje" duljine segmenta koji se promatra u jeziku L2
			// len1 je popunjen jos prije ove petlje
			len2 =   dataModel.getElement( dataModel.getKeys2().get(i+b  ) ).length()
				   + dataModel.getElement( dataModel.getKeys2().get(i+b+1) ).length();
			// Racunanje vjerojatnosti tog spoja
			p = ProbabilityComputation.computeP(len1, len2, 12);
			if ( p > mostProbable12.getProbability() ) {
				mostProbable12.setProbability(p);
				
				// Izbacuju se stari, manje vjerojatni kljucevi iz L2 s kojima
				// spaja element iz L1...
				mostProbable12.getkeysInConnection().removeLast();
				mostProbable12.getkeysInConnection().removeLast();
				
				// ... i na njihova mjesta se stavljaju novi, vjerojatniji
				mostProbable12.getkeysInConnection().add( dataModel.getKeys2().get(i+b  ) );
				mostProbable12.getkeysInConnection().add( dataModel.getKeys2().get(i+b+1) );
			}
		}
		// Provjera kolika je vjerojatnost 11 spoja za svaki od elemenata iz L1
//		System.out.println("Max P od #"+i+"# je "+mostProbable12.getProbability()+", a treba spojiti  "
//						   +mostProbable12.getkeysInConnection() );
		// Ako u analizi spojeva s elementima u jeziku L2 ne nadje nijedan s
		// p>0.0, onda se vraca mostProbable s p==0.0, a to uzima u obzir 
		// Controller
		return mostProbable12;
	}
}
