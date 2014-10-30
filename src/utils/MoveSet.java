package utils;

import java.util.LinkedList;

/**
 * Wrapper class to store a certain set of movements.
 * 
 * @author inf1n1te
 * 
 */
public class MoveSet {
	/**
	 * The
	 */
	private LinkedList<Movement> regular = new LinkedList<Movement>();
	private LinkedList<Movement> slaying = new LinkedList<Movement>();
	private LinkedList<Movement> castling = new LinkedList<Movement>();
	private LinkedList<Movement> promotion = new LinkedList<Movement>();
	private LinkedList<Movement> special = new LinkedList<Movement>();

	public MoveSet(LinkedList<Movement> r, LinkedList<Movement> s,
			LinkedList<Movement> c, LinkedList<Movement> p, LinkedList<Movement> sp) {
		regular = r;
		slaying = s;
		castling = c;
		promotion = p;
		special = sp;
	}

	/**
	 * Gets the regular movement.
	 * 
	 * @return the regular
	 */
	public LinkedList<Movement> getRegular() {
		return regular;
	}

	/**
	 * Sets the regular movement.
	 * 
	 * @param regular
	 *            the regular to set
	 */
	public void setRegular(LinkedList<Movement> regular) {
		this.regular = regular;
	}

	/**
	 * Gets the slaying movement.
	 * 
	 * @return the slaying
	 */
	public LinkedList<Movement> getSlaying() {
		return slaying;
	}

	/**
	 * Sets the slaying movement.
	 * 
	 * @param slaying
	 *            the slaying to set
	 */
	public void setSlaying(LinkedList<Movement> slaying) {
		this.slaying = slaying;
	}

	/**
	 * Gets the castling movement.
	 * 
	 * @return the castling
	 */
	public LinkedList<Movement> getCastling() {
		return castling;
	}

	/**
	 * Sets the castling movement.
	 * 
	 * @param castling
	 *            the castling to set
	 */
	public void setCastling(LinkedList<Movement> castling) {
		this.castling = castling;
	}

	/**
	 * Gets the promotion movement.
	 * 
	 * @return the promotion
	 */
	public LinkedList<Movement> getPromotion() {
		return promotion;
	}

	/**
	 * Sets the promotion movement.
	 * 
	 * @param promotion
	 *            the promotion to set
	 */
	public void setPromotion(LinkedList<Movement> promotion) {
		this.promotion = promotion;
	}

	/**
	 * Gets the special movement.
	 * 
	 * @return the special
	 */
	public LinkedList<Movement> getSpecial() {
		return special;
	}

	/**
	 * Sets the special movement.
	 * 
	 * @param special
	 *            the special to set
	 */
	public void setSpecial(LinkedList<Movement> special) {
		this.special = special;
	}
	
	public LinkedList<Movement> getSmart(MoveType mt) {
		LinkedList<Movement> value = null;
		switch (mt) {
		case REGULAR:
			value = regular;
			break;
		case SLAYING:
			value = slaying;
			break;
		case CASTLING:
			value = castling;
			break;
		case PROMOTION:
			value = promotion;
			break;
		default:
			break;
		}
		return value;
	}
	
}
