package hr.fer.zemris.ktlab.sap.util;

import java.io.Serializable;
import java.util.Date;

/**
 * Klasa koja sadrži informacije o jednoj knjižnoj oznaci. Klasa je napravljena
 * za knjižne oznake nad instancama klase <code>Element</code>.
 * <p>
 * Za svaku knjižnu oznaku sprema se ključ elementa nad kojim je stvorena,
 * njezino ime, opis i skup u kojem se element nalazi.
 * <p>
 * Za knjižnu oznaku se također bilježi datum i vrijeme kada je stvorena.
 * 
 * @author Željko Rumenjak
 */
public class Bookmark implements Serializable {

	private static final long serialVersionUID = 4997086445856113711L;

	/**
	 * Konstanta koja označava da element nad kojim se stvara knjižna oznaka
	 * pripada prvom skupu
	 */
	public static final short SET1 = 1;

	/**
	 * Konstanta koja označava da element nad kojim se stvara knjižna oznaka
	 * pripada drugom skupu
	 */
	public static final short SET2 = 2;

	/** Jedinstveni identifikator elementa nad kojim se stvara knjižna oznaka */

	private int key;

	/** Naziv knjižne oznake */
	private String name;

	/** Opis knjižne oznake */
	private String description;

	/**
	 * Sadrži oznaku skupa u kojem se nalazi element nad kojim se stvara knjižna
	 * oznaka
	 */
	private short setNumber;

	/** Datum i vrijeme kada je knjižna oznaka stvorena */
	private Date date;

	/**
	 * Konstruktor koji prima podatke o elementu nad kojim se stvara knjižna
	 * oznaka, te naziv i opis knjižne oznake.
	 * <p>
	 * Datum i vrijeme se automatski bilježi kod stvaranja knjižne oznake.
	 * 
	 * @param key
	 *            jedinstveni identifikator elementa
	 * @param name
	 *            naziv knjižne oznake
	 * @param description
	 *            opis knjižne oznake
	 * @param setNumber
	 *            oznaka skupa u kojem se nalazi element
	 */
	public Bookmark(int key, String name, String description, short setNumber) {
		if (key < 0) {
			throw new IllegalArgumentException(
					"Key must be a positive number, but was: " + key);
		}
		this.key = key;

		if (setNumber < 1 || setNumber > 2) {
			throw new IllegalArgumentException(
					"Set number must be from interval [1, 2], but was: "
							+ setNumber);
		}
		this.setNumber = setNumber;

		this.name = name;
		this.description = description;

		date = new Date();
	}

	/**
	 * Dohvaća jedinstveni identifikator elementa
	 * 
	 * @return jedinstveni identifikator elementa
	 */
	public int getKey() {
		return key;
	}

	/**
	 * Dohvaća naziv knjižne oznake.
	 * 
	 * @return naziv knjižne oznake.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Dohvaća opis knjižne oznake.
	 * 
	 * @return opis knjižne oznake.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Dohvaća oznaku skupa u kojem se nalazi element nad kojim je stvorena
	 * knjižna oznaka.
	 * 
	 * @return oznaka skupa u kojem se nalazi element nad kojim je stvorena
	 *         knjižna oznaka.
	 * 
	 */
	public short getSetNumber() {
		return setNumber;
	}

	/**
	 * Postavlja ime knjižne oznake
	 * 
	 * @param name
	 *            ime knjižne oznake
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Postavlja opis knjižne oznake
	 * 
	 * @param description
	 *            opis knjižne oznake
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Dohvaća datum i vrijeme kada je knjižna oznaka stvorena.
	 * 
	 * @return datum i vrijeme kada je knjižna oznaka stvorena.
	 */
	public Date getCreationDate() {
		return date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + key;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + setNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Bookmark other = (Bookmark) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (key != other.key)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (setNumber != other.setNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
