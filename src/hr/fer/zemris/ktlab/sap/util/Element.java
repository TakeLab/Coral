package hr.fer.zemris.ktlab.sap.util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Klasa koja opisuje jedan tekstualni element. Tekstualni element može biti
 * cijeli tekst, odlomak teksta, rečenica, riječ...
 * 
 * Svaki tekstualni element može pripadati jednom od dva skupa i može biti
 * povezan sa drugim elementima.
 * 
 * @author Željko Rumenjak
 */
public class Element implements Serializable {

	private static final long serialVersionUID = -1187567170791834580L;

	/** Konstanta koja označava da element pripada prvom skupu */
	public static final short SET1 = 1;

	/** Konstanta koja označava da element pripada drugom skupu */
	public static final short SET2 = 2;
	
	/** Jedinstveni identifikator */
	private int id;

	/** Tekst koji je pohranjen u elementu */
	private String text;

	/**
	 * Varijabla poprima vrijednost <code>true</code> ako element pripada
	 * prvom skupu, odnosno vrijednost <code>false</code> ako element pripada
	 * drugom skupu
	 */
	private boolean set1;

	/** Skup ključeva elemenata s kojima je element povezan */
	private Set<Integer> connections;

	/** Označava broj odlomka u kojem se element nalazi */
	private int paragraph = -1;

	/** Atributi elementa pročitani iz ulazne datoteke */
	private Map<String, String> attributes = null;

	/**
	 * Označava da je element najvjerojatnije podijeljen točno, ako je
	 * <code>true</code> ili netočno ako je <code>false</code>
	 */
	private boolean splitSafely = true;

	/**
	 * Javni konstruktor.
	 * 
	 * @param id
	 *            jedinstveni identifikator koji određuje element, ako dva
	 *            elementa imaju jednake id-ove, tada su i oni sami jednaki
	 * @param text
	 *            tekst koji se pohranjuje u element
	 * @param setNumber
	 *            oznaka skupa kojem element pripada
	 */
	public Element(int id, String text, short setNumber) {
		this.id = id;
		this.text = text;
		if (setNumber == SET1) {
			this.set1 = true;
		} else if (setNumber == SET2) {
			this.set1 = false;
		} else {
			throw new IllegalArgumentException("setNumber can be " + SET1
					+ " or " + SET2 + ", but was: " + setNumber);
		}
		connections = new HashSet<Integer>();
	}

	/**
	 * Javni konstruktor koji se koristi kod stvaranja elemenata nastalih
	 * podjelom jednog elementa na dva.
	 * 
	 * @param id
	 *            jedinstveni identifikator koji određuje element, ako dva
	 *            elementa imaju jednake id-ove, tada su i oni sami jednaki
	 * @param text
	 *            tekst koji se pohranjuje u element
	 * @param setNumber
	 *            oznaka skupa kojem element pripada
	 * @param paragraph
	 *            broj odlomka u kojem se element nalazi
	 * @param splitSafely
	 *            označava da li je element najvjerojatnije podijeljen točno ili
	 *            ne
	 */
	public Element(int id, String text, short setNumber, int paragraph,
			boolean splitSafely) {
		this.id = id;
		this.text = text;
		if (setNumber == SET1) {
			this.set1 = true;
		} else if (setNumber == SET2) {
			this.set1 = false;
		} else {
			throw new IllegalArgumentException("setNumber can be " + SET1
					+ " or " + SET2 + ", but was: " + setNumber);
		}
		connections = new HashSet<Integer>();

		setParagraph(paragraph);

		this.splitSafely = splitSafely;
	}

	/**
	 * Javni konstruktor sa dodatnim opcijama.
	 * 
	 * @param id
	 *            jedinstveni identifikator koji određuje element, ako dva
	 *            elementa imaju jednake id-ove, tada su i oni sami jednaki
	 * @param text
	 *            tekst koji se pohranjuje u element
	 * @param setNumber
	 *            oznaka skupa kojem element pripada
	 * @param paragraph
	 *            broj odlomka u kojem se element nalazi
	 */
	public Element(int id, String text, short setNumber, int paragraph) {
		this.id = id;
		this.text = text;
		if (setNumber == SET1) {
			this.set1 = true;
		} else if (setNumber == SET2) {
			this.set1 = false;
		} else {
			throw new IllegalArgumentException("setNumber can be " + SET1
					+ " or " + SET2 + ", but was: " + setNumber);
		}
		connections = new HashSet<Integer>();

		setParagraph(paragraph);
	}

	/**
	 * Javni konstruktor sa dodatnim opcijama.
	 * 
	 * @param id
	 *            jedinstveni identifikator koji određuje element, ako dva
	 *            elementa imaju jednake id-ove, tada su i oni sami jednaki
	 * @param text
	 *            tekst koji se pohranjuje u element
	 * @param setNumber
	 *            oznaka skupa kojem element pripada
	 * @param paragraph
	 *            broj odlomka u kojem se element nalazi
	 * @param attributes
	 *            atributi pročitani iz ulazne datoteke
	 */
	public Element(int id, String text, short setNumber, int paragraph,
			Map<String, String> attributes) {
		this.id = id;
		this.text = text;
		if (setNumber == SET1) {
			this.set1 = true;
		} else if (setNumber == SET2) {
			this.set1 = false;
		} else {
			throw new IllegalArgumentException("setNumber can be " + SET1
					+ " or " + SET2 + ", but was: " + setNumber);
		}
		connections = new HashSet<Integer>();

		setParagraph(paragraph);

		this.attributes = attributes;
	}

	/**
	 * Dohvaća jedinstveni identifikator elementa.
	 * 
	 * @return jedinstveni identifikator elementa
	 */
	public int getId() {
		return id;
	}

	/**
	 * Dodaje novi ključ u skup ključeva elemenata s kojima je element povezan
	 * 
	 * @param destinationKey
	 *            ključ elementa s kojim se element povezuje
	 * 
	 * @return <code>true</code> ako je ključ dodan u skup ključeva s kojima
	 *         je element povezan, <code>false</code> ako se ključ već nalazi
	 *         u tom skupu
	 */
	public boolean addConnection(int destinationKey) {
		return connections.add(destinationKey);
	}

	/**
	 * Uklanja ključ iz skupa ključeva elemenata s kojima je element povezan.
	 * 
	 * @param destinationKey
	 *            ključ koji se uklanja
	 * @return <code>true</code> ako je veza uklonjena ili <code>false</code>
	 *         ako veza niti nije postojala
	 */
	public boolean removeConnection(int destinationKey) {
		return connections.remove(destinationKey);
	}

	/**
	 * Dohvaća tekst koji je pohranjen u elementu
	 * 
	 * @return tekst koji je pohranjen u elementu
	 */
	public String getText() {
		return text;
	}

	/**
	 * Dohvaća skup ključeva elemenata s kojima je element povezan.
	 * 
	 * @return skup ključeva elemenata s kojima je element povezan
	 */
	public Set<Integer> getConnections() {
		return connections;
	}

	/**
	 * Dohvaća broj odlomka u kojem se element nalazi.
	 * 
	 * @return broj odlomka
	 */
	public int getParagraph() {
		return paragraph;
	}

	/**
	 * Dohvaća atribute elementa.
	 * 
	 * @return atributi elementa
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * Provjerava skup kojemu element pripada.
	 * 
	 * @return <code>true</code> ako element pripada prvom skupu,
	 *         <code>false</code> inače
	 */
	public boolean isInSet1() {
		return set1;
	}

	/**
	 * Provjerava da li je element najvjerojatnije podijeljen točno ili ne
	 * 
	 * @return <code>true</code> ako je element najvjerojatnije podijeljen
	 *         točno, <code>false</code> inače
	 */
	public boolean isSplitSafely() {
		return splitSafely;
	}

	/**
	 * Postavlja tekst koji je pohranjen u elementu.
	 * 
	 * @param text
	 *            novi tekst
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Postavlja broj odlomka u kojem se nalazi element. Broj odlomka ne smije
	 * biti negativan.
	 * 
	 * @param paragraph
	 *            broj odlomka
	 */
	public void setParagraph(int paragraph) {
		if (paragraph < 0) {
			throw new IllegalArgumentException(
					"Paragraph cannot be negative, but was: " + paragraph);
		}
		this.paragraph = paragraph;
	}

	/**
	 * Postavlja atribute elementa.
	 * 
	 * @param attributes
	 *            atributi pročitani iz datoteke
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Postavlja varijablu <code>splitSafely</code> koja označava da li je
	 * element najvjerojatnije podijeljen točno ili ne
	 * 
	 * @param splitSafely
	 *            <code>true</code> ako je element najvjerojatnije podijeljen
	 *            tocno, <code>false</code> inače
	 */
	public void setSplitSafely(boolean splitSafely) {
		this.splitSafely = splitSafely;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Element other = (Element) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
