package hr.fer.zemris.ktlab.sap.util;

import java.io.Serializable;

import hr.fer.zemris.ktlab.sap.gui.Icon;

/**
 * Klasa predstavlja jedan element koji se stavlja u model lijeve i desne
 * tablice.
 * 
 * @author Igor Šoš
 * 
 */
public class Sentence implements Serializable {

	private static final long serialVersionUID = 643139892092053102L;

	/**
	 * Tekst elementa.
	 */
	private String sentence;

	/** ID elementa. */
	private int id;

	/** Ikona elementa. */
	private Icon icon;

	/**
	 * Javni konstruktor.
	 * @param sentence Tekst elementa.
	 * @param id ID elementa.
	 * @param icon Ikona elementa.
	 */
	public Sentence(String sentence, int id, Icon icon) {
		this.sentence = sentence;
		this.id = id;
		this.icon = icon;
	}

	/** 
	 * Metoda za postavljanje teksta.
	 * @param sentence Tekst koji se postavlja.
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	/**
	 * Metoda za dohvaćanje ID-a.
	 * @return ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Metoda za dohvaćanje teksta elementa.
	 * @return Tekst.
	 */
	public String getSentence() {
		return sentence;
	}

	/**
	 * Metoda za dohvaćanje ikone elementa.
	 * @return Ikona.
	 */
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public String toString() {
		return sentence;
	}

}
