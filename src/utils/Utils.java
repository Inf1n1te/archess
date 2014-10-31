package utils;

import java.util.LinkedList;

public class Utils {

		public static boolean containsMovement(LinkedList<Movement> list, Movement movement) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getX() == movement.getX() && list.get(i).getY() == movement.getY()) {
					return true;
				}
			}
			return false;
		}

}
