/**
 * 
 */
package utils;

/**
 * Wrapper class to store a single movement.
 * 
 * @author inf1n1te
 * 
 */
public class Movement {

	private int x;
	private int y;

	/**
	 * 
	 */
	public Movement(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] getXY() {
		int[] value = new int[] { x, y };
		return value;
	}

	/**
	 * @return Movement
	 */
	public Movement stepBack() {
		Movement value = null;
		if ((x != 0 || x != 1) && (y != 0 || y != 1)) {
			int newX;
			if (x != 0) {
				newX = (Math.abs(x) - 1) * (int) Math.signum(x);
			} else {
				newX = x;
			}
			int newY;
			if (y != 0) {
				newY = (Math.abs(y) - 1) * (int) Math.signum(y);
			} else {
				newY = y;
			}
			value = new Movement(newX, newY);
		}
		return value;
	}
	
	public boolean equals(Movement comp) {
		if (x == comp.getX() && y == comp.getY()) {
			return true;
		}
		return false;
	}

}
