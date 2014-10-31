package model;

import java.util.LinkedList;

import utils.MoveType;
import utils.Movement;
import utils.Piece;
import utils.Rules;
import utils.TempPiece;
import utils.Utils;

/**
 * Class that defines the board. It contains the state of the board, i.e. the
 * position of all the pieces on the board and which pieces have been slain.
 * 
 * @author inf1n1te
 * 
 */
public class Board {

	/**
	 * Initial state of the board with white pieces in the rows y=0 and y=1.
	 */
	private final Piece[][] INIT_WHITE = new Piece[][] {
			new Piece[] { Piece.WHITE_ROOK, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_ROOK },
			new Piece[] { Piece.WHITE_KNIGHT, Piece.WHITE_PAWN, null, null,
					null, null, Piece.BLACK_PAWN, Piece.BLACK_KNIGHT },
			new Piece[] { Piece.WHITE_BISHOP, Piece.WHITE_PAWN, null, null,
					null, null, Piece.BLACK_PAWN, Piece.BLACK_BISHOP },
			new Piece[] { Piece.WHITE_QUEEN, Piece.WHITE_PAWN, null, null,
					null, null, Piece.BLACK_PAWN, Piece.BLACK_QUEEN },
			new Piece[] { Piece.WHITE_KING, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_KING },
			new Piece[] { Piece.WHITE_BISHOP, Piece.WHITE_PAWN, null, null,
					null, null, Piece.BLACK_PAWN, Piece.BLACK_BISHOP },
			new Piece[] { Piece.WHITE_KNIGHT, Piece.WHITE_PAWN, null, null,
					null, null, Piece.BLACK_PAWN, Piece.BLACK_KNIGHT },
			new Piece[] { Piece.WHITE_ROOK, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_ROOK } };
	/**
	 * Initial state of the board with black pieces in the rows y=0 and y=1.
	 */
	/*
	 * private final Piece[][] INIT_BLACK = new Piece[][] { new Piece[] {
	 * Piece.BLACK_ROOK, Piece.BLACK_PAWN, null, null, null, null,
	 * Piece.WHITE_PAWN, Piece.WHITE_ROOK }, new Piece[] { Piece.BLACK_KNIGHT,
	 * Piece.BLACK_PAWN, null, null, null, null, Piece.WHITE_PAWN,
	 * Piece.WHITE_KNIGHT }, new Piece[] { Piece.BLACK_BISHOP, Piece.BLACK_PAWN,
	 * null, null, null, null, Piece.WHITE_PAWN, Piece.WHITE_BISHOP }, new
	 * Piece[] { Piece.BLACK_KING, Piece.BLACK_PAWN, null, null, null, null,
	 * Piece.WHITE_PAWN, Piece.WHITE_KING }, new Piece[] { Piece.BLACK_QUEEN,
	 * Piece.BLACK_PAWN, null, null, null, null, Piece.WHITE_PAWN,
	 * Piece.WHITE_QUEEN }, new Piece[] { Piece.BLACK_BISHOP, Piece.BLACK_PAWN,
	 * null, null, null, null, Piece.WHITE_PAWN, Piece.WHITE_BISHOP }, new
	 * Piece[] { Piece.BLACK_KNIGHT, Piece.BLACK_PAWN, null, null, null, null,
	 * Piece.WHITE_PAWN, Piece.WHITE_KNIGHT }, new Piece[] { Piece.BLACK_ROOK,
	 * Piece.BLACK_PAWN, null, null, null, null, Piece.WHITE_PAWN,
	 * Piece.WHITE_ROOK } };
	 */
	/**
	 * Temporary board used as a bridge between the old and the new board.
	 * Required for figuring out what actually happened before pushing those
	 * changes.
	 */
	private TempPiece[][] tempBoard = new TempPiece[8][8];
	/**
	 * The position of all the pieces on the old board. Format: board[0][0] is
	 * the lower-left corner.
	 */
	private Piece[][] oldBoard = new Piece[8][8];
	/**
	 * The position of all the pieces on the new board. Format: board[0][0] is
	 * the lower-left corner.
	 */
	private Piece[][] newBoard = new Piece[8][8];
	/**
	 * An LinkedList containing all the slain pieces;
	 */
	private LinkedList<Piece> slain = new LinkedList<Piece>();
	/**
	 * False until the left black rook moves. Used for castling validation.
	 */
	private boolean leftBlackRookMoved = false;
	/**
	 * False until the right black rook moves. Used for castling validation.
	 */
	private boolean rightBlackRookMoved = false;
	/**
	 * False until the black king moves. Used for castling validation.
	 */
	private boolean blackKingMoved = false;
	/**
	 * False until the left white rook moves. Used for castling validation.
	 */
	private boolean leftWhiteRookMoved = false;
	/**
	 * False until the right white rook moves. Used for castling validation.
	 */
	private boolean rightWhiteRookMoved = false;
	/**
	 * False until the white king moves. Used for castling validation.
	 */
	private boolean whiteKingMoved = false;
	/**
	 * The last move made.
	 */
	private Move lastMove;

	/**
	 * Constructor for the board. Requires a raw board as input. It will then
	 * generate a temporary board. Used for initialization.
	 * 
	 * @param rawBoard
	 *            A int[][] containing the raw data received from the FPGA.
	 */
	public Board(int[][] rawBoard) {
		createTempBoard(rawBoard);
		init();
	}

	/**
	 * Initialize the board with starting positions.
	 */
	private void init() {
		if (tempBoard[0][0] == TempPiece.WHITE) {
			newBoard = INIT_WHITE;
		} /*
		 * else if (tempBoard[0][0] == TempPiece.BLACK) { newBoard = INIT_BLACK;
		 * }
		 */else {
			System.err.println("Switch board around! @Board.init");
		}
	}

	/**
	 * Creates a temporary board with limited information.
	 * 
	 * @param raw
	 *            The raw input data
	 */
	private void createTempBoard(int[][] raw) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tempBoard[i][j] = getTempPiece(raw[i][j]);
			}
		}
	}

	/**
	 * Determine which temporary piece belongs to which value.
	 * 
	 * @param value
	 *            The value to find a temporary piece for
	 * @return The piece that belongs to the input value
	 */
	private TempPiece getTempPiece(int value) {
		switch (value) {
		case 0:
			return TempPiece.EMPTY;
		case 1:
			return TempPiece.WHITE;
		case 2:
			return TempPiece.BLACK;
		default:
			System.err.println("Wrong intvalue @Board.getTempPiece");
			return null;
		}
	}

	/**
	 * Function that handles the board and move of a new turn.
	 * 
	 * @param rawBoard
	 *            The raw input for a board
	 */
	public void newTurn(int[][] rawBoard) {
		createTempBoard(rawBoard);
		oldBoard = newBoard;
		lastMove = new Move(this);
		newBoard = lastMove.getNewBoard();
		slain = lastMove.getSlainPieces();
		hasMovedCastling(lastMove);
	}

	/**
	 * Checks if any of the pieces involved in castling have moved.
	 * 
	 * @param lastMove
	 */
	private void hasMovedCastling(Move lastMove) {
		if (!blackKingMoved
				&& (lastMove.getMovedPieces()[0] == Piece.BLACK_KING || lastMove
						.getMovedPieces()[1] == Piece.BLACK_KING)) {
			blackKingMoved = true;
		}
		if (!whiteKingMoved
				&& (lastMove.getMovedPieces()[0] == Piece.WHITE_KING || lastMove
						.getMovedPieces()[1] == Piece.WHITE_KING)) {
			whiteKingMoved = true;
		}
		int coords00 = lastMove.getOldCoords()[0][0];
		int coords01 = lastMove.getOldCoords()[0][1];
		if (lastMove.getOldCoords()[0] != null) {
			int coords10 = lastMove.getOldCoords()[1][0];
			int coords11 = lastMove.getOldCoords()[1][1];
			if (!leftBlackRookMoved
					&& ((coords00 == 0 && coords01 == 7) || (coords10 == 0
							&& coords11 == 7))) {
				leftBlackRookMoved = true;
			}
			if (!rightBlackRookMoved
					&& ((coords00 == 7 && coords01 == 7) || (coords10 == 7
							&& coords11 == 7))) {
				rightBlackRookMoved = true;
			}
			if (!leftWhiteRookMoved
					&& ((coords00 == 0 && coords01 == 0) || (coords10 == 0
							&& coords11 == 0))) {
				leftWhiteRookMoved = true;
			}
			if (!rightWhiteRookMoved
					&& ((coords00 == 7 && coords01 == 0) || (coords10 == 7
							&& coords11 == 0))) {
				rightWhiteRookMoved = true;
			}
		}

	}

	/**
	 * Sets a specific field to a specific piece.
	 * 
	 * @param coords
	 *            The coordinates of the field to be set
	 * @param piece
	 *            The piece which the field will be set to
	 */
	public void setField(int[] coords, Piece piece) {
		newBoard[coords[0]][coords[1]] = piece;
	}

	/**
	 * Gets the piece that is occupying the field on the new board corresponding
	 * to the input coordinates.
	 * 
	 * @param coords
	 *            coordinates of the field
	 * @return Piece
	 */
	public Piece getField(int[] coords) {
		if (coords[0] < 0 || coords[0] > 7 || coords[1] < 0 || coords[1] > 7) {
			return null;
		}
		return newBoard[coords[0]][coords[1]];
	}

	/**
	 * Adds a piece to the list of slain pieces.
	 * 
	 * @param slainPiece
	 *            The piece that will be added
	 */
	public void addSlain(Piece slainPiece) {
		slain.add(slainPiece);
	}

	/**
	 * Gets the temporary board.
	 * 
	 * @return The temporary board
	 */
	public TempPiece[][] getTempBoard() {
		return tempBoard;
	}

	/**
	 * Sets the temporary board.
	 * 
	 * @param tempBoard
	 *            The TempPiece[] to which the temporary board will be set
	 */
	public void setTempBoard(TempPiece[][] tempBoard) {
		this.tempBoard = tempBoard;
	}

	/**
	 * Gets the new board.
	 * 
	 * @return The new board
	 */
	public Piece[][] getNewBoard() {
		return newBoard;
	}

	/**
	 * Sets the new board.
	 * 
	 * @param newBoard
	 *            The new board
	 */
	public void setNewBoard(Piece[][] newBoard) {
		this.newBoard = newBoard;
	}

	/**
	 * Gets the old board.
	 * 
	 * @return The old board
	 */
	public Piece[][] getOldBoard() {
		return oldBoard;
	}

	/**
	 * Sets the old board.
	 * 
	 * @param oldBoard
	 *            The old board
	 */
	public void setOldBoard(Piece[][] oldBoard) {
		this.oldBoard = oldBoard;
	}

	/**
	 * Gets the slain pieces.
	 * 
	 * @return The slain pieces.
	 */
	public LinkedList<Piece> getSlain() {
		return slain;
	}

	/**
	 * Sets the slain pieces.
	 * 
	 * @param slain
	 *            The LinkedList to which the slain pieces will be set
	 */
	public void setSlain(LinkedList<Piece> slain) {
		this.slain = slain;
	}

	/**
	 * Gets the last move.
	 * 
	 * @return Move lastMove
	 */
	public Move getMove() {
		return lastMove;
	}

	/**
	 * @return the leftBlackRookMoved
	 */
	public boolean isLeftBlackRookMoved() {
		return leftBlackRookMoved;
	}

	/**
	 * @param leftBlackRookMoved
	 *            the leftBlackRookMoved to set
	 */
	public void setLeftBlackRookMoved(boolean leftBlackRookMoved) {
		this.leftBlackRookMoved = leftBlackRookMoved;
	}

	/**
	 * @return the rightBlackRookMoved
	 */
	public boolean isRightBlackRookMoved() {
		return rightBlackRookMoved;
	}

	/**
	 * @param rightBlackRookMoved
	 *            the rightBlackRookMoved to set
	 */
	public void setRightBlackRookMoved(boolean rightBlackRookMoved) {
		this.rightBlackRookMoved = rightBlackRookMoved;
	}

	/**
	 * @return the blackKingMoved
	 */
	public boolean isBlackKingMoved() {
		return blackKingMoved;
	}

	/**
	 * @param blackKingMoved
	 *            the blackKingMoved to set
	 */
	public void setBlackKingMoved(boolean blackKingMoved) {
		this.blackKingMoved = blackKingMoved;
	}

	/**
	 * @return the leftWhiteRookMoved
	 */
	public boolean isLeftWhiteRookMoved() {
		return leftWhiteRookMoved;
	}

	/**
	 * @param leftWhiteRookMoved
	 *            the leftWhiteRookMoved to set
	 */
	public void setLeftWhiteRookMoved(boolean leftWhiteRookMoved) {
		this.leftWhiteRookMoved = leftWhiteRookMoved;
	}

	/**
	 * @return the rightWhiteRookMoved
	 */
	public boolean isRightWhiteRookMoved() {
		return rightWhiteRookMoved;
	}

	/**
	 * @param rightWhiteRookMoved
	 *            the rightWhiteRookMoved to set
	 */
	public void setRightWhiteRookMoved(boolean rightWhiteRookMoved) {
		this.rightWhiteRookMoved = rightWhiteRookMoved;
	}

	/**
	 * @return the whiteKingMoved
	 */
	public boolean isWhiteKingMoved() {
		return whiteKingMoved;
	}

	/**
	 * @param whiteKingMoved
	 *            the whiteKingMoved to set
	 */
	public void setWhiteKingMoved(boolean whiteKingMoved) {
		this.whiteKingMoved = whiteKingMoved;
	}

	/**
	 * Determines whether the origin field has a clear line of sight to a
	 * destination field. Does not check movement sets for anything but a pawn.
	 * Use canMove in conjunction with hasLOS to know whether a piece can
	 * actually move to the field it has LOS of. Uses the new board.
	 * 
	 * @param origin
	 *            the field from which a piece has LOS
	 * @param destination
	 *            the field on which a piece has LOS
	 * @param piece
	 *            the piece on the origin field
	 * @param moveType
	 *            the type of move made by the piece, can be <code>null</code>
	 *            when piece is not <code>Piece.BLACK_PAWN</code> or
	 *            <code>Piece.WHITE_PAWN</code>
	 * @return boolean
	 */
	public boolean hasLOS(int[] origin, int[] destination, Piece piece,
			MoveType moveType) {
		// The knight jumps over pieces, LOS isn't required.
		// The king can only move a single square, it is impossible to have a
		// piece in between.
		if (piece.toString().contains("KNIGHT")
				|| piece.toString().contains("KING")) {
			return true;
		}

		// The movement is used for the other pieces.
		Movement movement = new Movement(destination[0] - origin[0],
				destination[1] - origin[1]);

		// Special pawn rules
		if (piece.toString().contains("PAWN") && moveType == MoveType.REGULAR) {
			if ((piece.toString().contains("WHITE") && origin[1] == 1)
					|| (piece.toString().contains("BLACK") && origin[1] == 6)) {
				movement = movement.stepBack();
				if (movement != null
						&& getField(new int[] { origin[0] + movement.getX(),
								origin[1] + movement.getY() }) == null) {
					return true;
				}
			} else {
				// A single square move cannot be blocked
				return true;
			}
		} else if (piece.toString().contains("PAWN")) {
			return true;
		}

		// Stepping back once before the while loop
		movement = movement.stepBack();
		// Determining LOS for the rest of the pieces
		while (movement != null) {
			if (getField(new int[] { origin[0] + movement.getX(),
					origin[1] + movement.getY() }) != null) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines if a piece could move from origin square to destination square
	 * if the board were empty. Use in conjunction with hasLOS shows if the move
	 * is actually possible.
	 * 
	 * It checks whether a Movement is in a LinkedList with Movements.
	 * 
	 * @param origin
	 *            the origin field
	 * @param destination
	 *            the destination field
	 * @param piece
	 *            the moving piece
	 * @param moveType
	 *            the type of move you want to make
	 * @return boolean
	 */
	public boolean canMove(int[] origin, int[] destination, Piece piece,
			MoveType moveType) {
		// Determine the movement
		Movement movement = new Movement(destination[0] - origin[0],
				destination[1] - origin[1]);

		// Special pawn rules on regular move, because of the special
		// "double-forward" rule
		if (piece.toString().contains("PAWN") && moveType == MoveType.REGULAR) {
			if (piece.toString().contains("WHITE")) {
				if (origin[1] == 1) {
					if (Utils.containsMovement(Rules.WHITE_PAWN.getSpecial(),
							movement)) {
						return true;
					}
				} else if (Utils.containsMovement(
						Rules.WHITE_PAWN.getRegular(), movement)) {
					return true;
				}
			} else if (piece.toString().contains("BLACK")) {
				if (origin[1] == 6) {
					if (Utils.containsMovement(Rules.BLACK_PAWN.getSpecial(),
							movement)) {
						return true;
					}
				} else if (Utils.containsMovement(
						Rules.BLACK_PAWN.getRegular(), movement)) {
					return true;
				}
			}
		}
		// Every other case
		else if (Utils.containsMovement(
				Rules.getMoveSet(piece).getSmart(moveType), movement)) {
			return true;
		}
		// Or return false
		return false;

	}

	/**
	 * Determines if a field is under attack by a certain color.
	 * 
	 * @param coords
	 *            the coordinates of the field
	 * @param color
	 *            the color of the attacker
	 * @return boolean
	 */
	public boolean isFieldUnderAttack(int[] coords, String color) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int[] origin = new int[] { i, j };
				Piece field = getField(origin);
				if (field != null
						&& field.toString().contains(color.toUpperCase())) {
					return (canMove(origin, coords, field, MoveType.SLAYING) && hasLOS(
							origin, coords, field, MoveType.SLAYING));
				}
			}
		}
		return false;
	}

	/**
	 * The field of a king.
	 * 
	 * @return boolean
	 * @requires <code>color.equals("BLACK") || color.equals("WHITE");</code>
	 */
	public int[] getKingField(String color) {
		int[] coords = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece field = getField(new int[] { i, j });
				if (field != null
						&& field.toString().equals(
								"" + color.toUpperCase() + "_KING")) {
					coords = new int[] { i, j };
				}
			}
		}
		return coords;
	}

}
