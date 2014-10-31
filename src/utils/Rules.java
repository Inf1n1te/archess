package utils;

import java.util.LinkedList;

/**
 * Defines all the movement sets for pieces.
 * 
 * @author inf1n1te
 * 
 */
public final class Rules {
	public static final MoveSet WHITE_PAWN = whitePawnInit();
	public static final MoveSet BLACK_PAWN = blackPawnInit();
	public static final MoveSet ROOK = rookInit();
	public static final MoveSet KNIGHT = knightInit();
	public static final MoveSet BISHOP = bishopInit();
	public static final MoveSet QUEEN = queenInit();
	public static final MoveSet KING = kingInit();

	private static final MoveSet whitePawnInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		regularMoves.add(new Movement(0, 1));
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		slayingMoves.add(new Movement(-1, 1));
		slayingMoves.add(new Movement(1, 1));
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		promotionMoves.add(new Movement(0, 1));
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		specialMoves.add(new Movement(0, 1));
		specialMoves.add(new Movement(0, 2));
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	private static final MoveSet blackPawnInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		regularMoves.add(new Movement(0, -1));
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		slayingMoves.add(new Movement(-1, -1));
		slayingMoves.add(new Movement(1, -1));
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		promotionMoves.add(new Movement(0, -1));
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		specialMoves.add(new Movement(0, -1));
		specialMoves.add(new Movement(0, -2));
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	private static final MoveSet rookInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		for (int i = 1; i < 9; i++) {
			regularMoves.add(new Movement(i, 0));
			regularMoves.add(new Movement(-i, 0));
			regularMoves.add(new Movement(0, i));
			regularMoves.add(new Movement(0, -i));
		}
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		for (int i = 1; i < 9; i++) {
			slayingMoves.add(new Movement(i, 0));
			slayingMoves.add(new Movement(-i, 0));
			slayingMoves.add(new Movement(0, i));
			slayingMoves.add(new Movement(0, -i));
		}
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		castlingMoves.add(new Movement(3, 0));
		castlingMoves.add(new Movement(-2, 0));
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	private static final MoveSet knightInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		regularMoves.add(new Movement(1, 2));
		regularMoves.add(new Movement(1, -2));
		regularMoves.add(new Movement(-1, 2));
		regularMoves.add(new Movement(-1, -2));
		regularMoves.add(new Movement(2, 1));
		regularMoves.add(new Movement(2, -1));
		regularMoves.add(new Movement(-2, 1));
		regularMoves.add(new Movement(-2, -1));
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		slayingMoves.add(new Movement(1, 2));
		slayingMoves.add(new Movement(1, -2));
		slayingMoves.add(new Movement(-1, 2));
		slayingMoves.add(new Movement(-1, -2));
		slayingMoves.add(new Movement(2, 1));
		slayingMoves.add(new Movement(2, -1));
		slayingMoves.add(new Movement(-2, 1));
		slayingMoves.add(new Movement(-2, -1));
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	private static final MoveSet bishopInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		for (int i = 1; i < 9; i++) {
			regularMoves.add(new Movement(i, i));
			regularMoves.add(new Movement(i, -i));
			regularMoves.add(new Movement(-i, i));
			regularMoves.add(new Movement(-i, -i));
		}
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		for (int i = 1; i < 9; i++) {
			regularMoves.add(new Movement(i, i));
			regularMoves.add(new Movement(i, -i));
			regularMoves.add(new Movement(-i, i));
			regularMoves.add(new Movement(-i, -i));
		}
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	private static final MoveSet queenInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		for (int i = 1; i < 9; i++) {
			regularMoves.add(new Movement(i, i));
			regularMoves.add(new Movement(i, -i));
			regularMoves.add(new Movement(-i, i));
			regularMoves.add(new Movement(-i, -i));
			regularMoves.add(new Movement(i, 0));
			regularMoves.add(new Movement(-i, 0));
			regularMoves.add(new Movement(0, i));
			regularMoves.add(new Movement(0, -i));
		}
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		for (int i = 1; i < 9; i++) {
			regularMoves.add(new Movement(i, i));
			regularMoves.add(new Movement(i, -i));
			regularMoves.add(new Movement(-i, i));
			regularMoves.add(new Movement(-i, -i));
			regularMoves.add(new Movement(i, 0));
			regularMoves.add(new Movement(-i, 0));
			regularMoves.add(new Movement(0, i));
			regularMoves.add(new Movement(0, -i));
		}
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	private static final MoveSet kingInit() {
		LinkedList<Movement> regularMoves = new LinkedList<Movement>();
		regularMoves.add(new Movement(1, 1));
		regularMoves.add(new Movement(1, -1));
		regularMoves.add(new Movement(-1, 1));
		regularMoves.add(new Movement(-1, -1));
		regularMoves.add(new Movement(1, 0));
		regularMoves.add(new Movement(-1, 0));
		regularMoves.add(new Movement(0, 1));
		regularMoves.add(new Movement(0, -1));
		LinkedList<Movement> slayingMoves = new LinkedList<Movement>();
		regularMoves.add(new Movement(1, 1));
		regularMoves.add(new Movement(1, -1));
		regularMoves.add(new Movement(-1, 1));
		regularMoves.add(new Movement(-1, -1));
		regularMoves.add(new Movement(1, 0));
		regularMoves.add(new Movement(-1, 0));
		regularMoves.add(new Movement(0, 1));
		regularMoves.add(new Movement(0, -1));
		LinkedList<Movement> castlingMoves = new LinkedList<Movement>();
		regularMoves.add(new Movement(2, 0));
		regularMoves.add(new Movement(-2, 0));
		LinkedList<Movement> promotionMoves = new LinkedList<Movement>();
		LinkedList<Movement> specialMoves = new LinkedList<Movement>();
		return new MoveSet(regularMoves, slayingMoves, castlingMoves,
				promotionMoves, specialMoves);
	}

	public static MoveSet getMoveSet(Piece piece) {
		String pieceString = piece.toString();
		if (pieceString.contains("PAWN")) {
			if (pieceString.contains("WHITE")) {
				return BLACK_PAWN;
			} else {
				return WHITE_PAWN;
			}
		} else if (pieceString.contains("ROOK")) {
			return ROOK;
		} else if (pieceString.contains("KNIGHT")) {
			return KNIGHT;
		} else if (pieceString.contains("BISHOP")) {
			return BISHOP;
		} else if (pieceString.contains("QUEEN")) {
			return QUEEN;
		} else {
			return KING;
		}
	}

}
