package hr.fer.zemris.ktlab.sap.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import hr.fer.zemris.ktlab.sap.util.DataModel;

/**
 * Klasa koja sluzi za spremanje modela. Model se sprema u datoteku cija je
 * pretpostavljena ekstenzija .razor, ali moze biti i proizvoljna. 
 * 
 */
public class SaveLoadProject {
	public static final String CORAL = "coral";
	public static final String ALL = "all";

	/**
	 * Metoda sprema cijeli model u datoteku.
	 * 
	 * @param path
	 *            putanja do dokumenta
	 * @param dataModel
	 *            model koji se sprema
	 * @param extention
	 * @throws Exception
	 */
	public static void saveProject(String path, DataModel dataModel,
			String extention) throws Exception {

		path = checkPath(path, extention);

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				path));

		oos.writeObject(dataModel);
		oos.close();
	}

	/**
	 * Metoda koja ucitava prethodno spremljeni projekt. Ucitava se cijeli
	 * model.
	 * 
	 * @param path
	 *            putanja do dokumenta
	 * @return
	 * @throws Exception
	 */
	public static DataModel loadProject(String path) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		DataModel dataModel = (DataModel) ois.readObject();
		ois.close();

		return dataModel;
	}

	// dodao static da ne bude error. to treba provjerit!!

	/**
	 * Metoda za provjeru ispravnosti putanje. Provjerava se zavrsava li
	 * primljana putanja sa ekstenzijom extention. Ako da metoda vraca primljenu
	 * putanju, u suprotnome na primljenu putanju dodaje se ektenzija extention.
	 * 
	 * @param pathToCheck
	 *            putanja
	 * @param extention
	 *            ekstenzija sa kojom putanja treba zavrsiti ili "all" ako se
	 *            korisniku prepusta izbor proizvojlen ekstenzije
	 */
	private static String checkPath(String pathToCheck, String extention) {
		String[] tokens = null;
		StringBuilder retval = new StringBuilder();

		// ako primljena eksenzija ima tocku na pocetku ona se brise
		if (extention.substring(0, 1).equals(".")) {
			extention = extention.substring(1);
		}

		// ako je primljena "ekstenzija" all documents path se ne mijenja
		if (extention.equals("all")) {
			return pathToCheck;
		} else {

			tokens = pathToCheck.split("\\.");

			if (tokens == null) {
				return pathToCheck + "." + extention;
			} else {
				// ako primljeni path zavrsava sa dobrom ekstenzijom nista se ne
				// dodaje
				if (tokens[tokens.length - 1].equals(extention)) {
					return pathToCheck;
				} else {
					// u suprotnome na primljeni path se ljepi ekstenzija
					for (String token : tokens) {
						retval.append(token + ".");
					}
					return (retval.append(extention).toString());
				}
			}

		}

	}
}
