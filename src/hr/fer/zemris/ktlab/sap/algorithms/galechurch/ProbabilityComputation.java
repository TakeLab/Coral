package hr.fer.zemris.ktlab.sap.algorithms.galechurch;

import java.lang.Math;

/**
 * Razred sadrzi 3 privatne metode i 1 javnu. Javna metoda koristi
 * preostale 3 u izracunu vjerojatnosti da se istovremeno dogodio 
 * odredjeni slucaj ( 1:1, 1:2, 2:1, 2:2 ) te da su duljine
 * segmenata u jezicima L1 i L2 bile u odnosu definiranom u metodi 
 * delta(int, int)
 * Razred ne sadrzi niti jedan podatkovni clan i sve su metode 
 * staticke.
 */

public class ProbabilityComputation {
	
	
	
	/**
	 * Metoda sadrzi vjerojatnosti pojavljivanja odredjenog slucaja u 
	 * sravnjivanju.
	 * Slucajevi koji se mogu dogoditi su:
	 * 		1) Jedna recenica jezika L1 se prevodi u jednu recenicu 
	 * 		   jezika L2 ( 1:1 )
	 * 		2) Jedna recenica jezika L1 se prevodi u dvije recenice 
	 * 		   jezika L2 ( 1:2 ), ili obratno ( 2:1 )
	 * 		3) Dvije recenice jezika L1 se prevode u dvije recenice 
	 * 		   jezika L2 ( 2:2 )
	 * 
	 * Navedene empirijski dobivene vrijednosti su preuzete iz 
	 * Gale, W.A., Church K.W. (1993)
	 *  
	 *  @param slucaj
	 *  				Metoda kao argument uzima zapis slucaja za 
	 *  				koji se trazi vjerojatnost i to u formatu
	 *  				11 za slucaj ( 1:1 ), 12 za slucajeve ( 1:2 ) i
	 *  				( 2:1 ) itd.
	 *  @return
	 *  				Vraca vjerojatnost zadanog slucaja
	 * 
	 */
	
	private static double pmatch(int slucaj) {
		if (slucaj==11) {
			return 0.89;
		} else if ( (slucaj==12)|(slucaj==21) ) {
			return 0.089;
		} else if (slucaj==22) {
			return 0.011;
		} else return 0.0;
	}
	
	/**
	 * Metoda racuna vjerojatnost da je slucajna varijabla poprimili vrijednost
	 * manju od x. No, umjesto integriranja funkcije gustoce od normalne 
	 * razdiobe, koristi se aproksimacija od Abramowitza i Steguna iz 1964.,
	 * a koja se koristi i u Gale, W.A., Church K.W. (1993)
	 * @param x
	 * 			Metodi kao argument treba uputiti parametar delta koji se 
	 * 			dobiva pomocu istoimene metode u ovom razredu 
	 * @return
	 * 			Vjerojatnost da je slucajna varijabla DELTA poprimila 
	 * 			vrijednost manju od delta
	 */
	private static double pnorm(double delta) {
		double x, t, pd;
		if (delta<0) {
			x = -delta;
		} else {
			x = delta;
		}
		// Slijedeca dva retka sadrze spomenuti aproksimativni izracun
		t = 1/(1 + 0.2316419*x);
		pd = 1 - 0.3989423*Math.exp(-x*x/2)* ((((1.330274429*t-1.821255978)*t+1.781477937)*t-0.356563782)*t+0.319381530)*t;
		
		return pd;
	}
	
	/** 
	 * Izracunava parametar delta koji za odgovarajuce len1 i len2 poprima
	 * vrijednosti prema normalnoj razdiobi N(0,1)
	 * Takodjer koristi empirijski dobivene parametre c i s^2 preuzete iz
	 *  Gale, W.A., Church K.W. (1993)
	 * @param len1
	 * 				Duljina segmenta u jeziku L1
	 * @param len2
	 * 				Duljina segmenta u jeziku L2
	 * @return
	 * 				Parametar delta koji poprima vrijednosti prema N(0,1)
	 */				
	private static double delta( int len1, int len2 ) {
		final double c = 1;
		final double s2 = 6.8;
		
		return ( 
				 ( (double)len2 - ((double)len1)*c )
				 /
				 Math.sqrt(s2*((double)len1)) 
				);
		
	}
	
	/**
	 * Centralna metoda razreda ProbabilityComputation. Jedina javna metoda.
	 * Koristi ostale metode razreda u svom radu.
	 * @param len1
	 * 				Broj znakova u segmentu jezika L1
	 * @param len2
	 * 				Broj znakova u segmentu jezika L2
	 * @param slucaj
	 * 				Zapis slucaja za koji se trazi vjerojatnost i to u 
	 * 				formatu11 za slucaj [1-1], 12 za slucajeve [1-2] i
	 *  			[2-1] itd. 
	 * @return
	 * 				Vjerojatnost da su duljine segmenata len1 i len2 u 
	 * 				odnosu delta, te da se, uz to, dogodio zadani slucaj
	 * 				( [1-1] ili [1-2] tj. [2-1] ili [2-2] )
	 */
	public static double computeP ( int len1, int len2, int slucaj) {
		 double x;
		 x = 2 * ( 1-pnorm(delta(len1, len2))) * pmatch(slucaj);
		 return x;
	}

}
