package model;

import java.util.LinkedList;

import utils.MoveType;
import utils.Piece;
import utils.TempPiece;

/**
 * Class that defines a move. Requires a board as input and generates all
 * additional values.
 * 
 * @author inf1n1te
 * 
 */
public class Move {

	/**
	 * The board used by this move.
	 */
	private Board board;
	/**
	 * Old coordinates of the moved piece.
	 */
	private int[] oldCoords;
	/**
	 * Old coordinates of the second moved piece.
	 */
	private int[] oldCoordsCastling;
	/**
	 * New coordinates of the moved piece.
	 */
	private int[] newCoords;
	/**
	 * New coordinates of the second moved piece.
	 */
	private int[] newCoordsCastling;
	/**
	 * The piece that moved.
	 */
	private Piece movedPiece;
	/**
	 * The second piece that moved.
	 */
	private Piece movedPieceCastling;
	/**
	 * The piece that was slain.
	 */
	private Piece slainPiece;
	/**
	 * The type of move.
	 */
	private MoveType moveType;
	/**
	 * True if the move is valid.
	 */
	private boolean isValid;

	/**
	 * Constructor for the move which sets the board and runs the methods that
	 * determine the rest of the fields.
	 * 
	 * @param board
	 *            The board
	 */
	public Move(Board board) {
		// Set Boards
		this.board = board;

		// Get the move
		determineMoveType();
		determineMove();

		// Initial validation
		determineValidity();

		// Building the new board for screen output
		updateBoard();

		// Post-move validation (is the active players king now under attack?)
		determinePostValidity();
	}

	/**
	 * Update the newBoard field of the board with the updated values from this
	 * move.
	 */
	private void updateBoard() {
		board.setField(oldCoords, null);
		board.setField(newCoords, movedPiece);
		System.out.println("moveType: " + moveType);
		switch (moveType) {
		case REGULAR:
			break;
		case SLAYING:
			board.addSlain(slainPiece);
			break;
		case CASTLING:
			board.setField(oldCoordsCastling, null);
			board.setField(newCoordsCastling, movedPieceCastling);
			break;
		case PROMOTION:
			// TODO PROMOTION
			break;
		default:
			System.err
					.println("Something went wrong with the updating of the board @Move.updateBoard");
			break;
		}
	}

	// TODO Implement promotion validity
	/**
	 * Determine if the move is valid and set the validity.
	 */
	private void determineValidity() {
		// Checking if move is possible
		if (moveType == MoveType.REGULAR) {
			if (board.hasLOS(oldCoords, newCoords, movedPiece, moveType)
					&& board.canMove(oldCoords, newCoords, movedPiece, moveType)) {
				isValid = true;
			}
		} else if (moveType == MoveType.SLAYING) {
			if (board.hasLOS(oldCoords, newCoords, movedPiece, moveType)
					&& board.canMove(oldCoords, newCoords, movedPiece, moveType)
					&& !slainPiece.toString().split("_")[0].equals(movedPiece
							.toString().split("_")[0])) {
				isValid = true;
			}
		} else if (moveType == MoveType.CASTLING && castlingChecks()) {
			isValid = true;
		} else if (moveType == MoveType.PROMOTION) {
			System.err.println("Not yet implented @Move.determineValidity");
			// TODO PROMOTION
		} else {
			isValid = false;
		}
	}

	/**
	 * Determine validity of the move after moving the actual pieces on the
	 * board because of the king now being in check.
	 */
	private void determinePostValidity() {
		// Checking if king is under attack after this move
		if (movedPiece.toString().contains("WHITE")
				&& board.isFieldUnderAttack(board.getKingField("WHITE"),
						"BLACK")) {
			isValid = false;
		} else if (movedPiece.toString().contains("BLACK")
				&& board.isFieldUnderAttack(board.getKingField("BLACK"),
						"WHITE")) {
			isValid = false;
		}
	}

	/**
	 * Checks for the castling validation.
	 * 
	 * @return boolean
	 */
	private boolean castlingChecks() {
		// Movement checks
		boolean value0 = false;
		if (board.hasLOS(oldCoords, newCoords, movedPiece, moveType)
				&& board.hasLOS(oldCoordsCastling, newCoordsCastling,
						movedPieceCastling, moveType)
				&& board.canMove(oldCoords, newCoords, movedPiece, moveType)
				&& board.canMove(oldCoordsCastling, newCoordsCastling,
						movedPieceCastling, moveType)) {
			value0 = true;
		}

		// Have the pieces not moved? checks
		boolean value1 = false;
		if (movedPiece.toString().contains("WHITE")
				&& !board.isWhiteKingMoved()
				&& ((oldCoords[0] == 0 && !board.isLeftWhiteRookMoved()) || (oldCoords[0] == 7 && !board
						.isRightWhiteRookMoved()))) {
			value1 = true;
		} else if (movedPiece.toString().contains("BLACK")
				&& !board.isBlackKingMoved()
				&& ((oldCoords[0] == 0 && !board.isLeftBlackRookMoved()) || (oldCoords[0] == 7 && !board
						.isRightBlackRookMoved()))) {
			value1 = true;
		}

		// Is the king not in check?
		boolean value2 = false;
		if (movedPiece.toString().contains("WHITE")
				&& !board.isFieldUnderAttack(board.getKingField("WHITE"),
						"BLACK")) {
			value2 = true;
		} else if (movedPiece.toString().contains("BLACK")
				&& !board.isFieldUnderAttack(board.getKingField("BLACK"),
						"WHITE")) {
			value2 = true;
		}

		// Is the field through which the king passes not under attack?
		boolean value3 = false;
		int[] coords;
		if (newCoords[0] <= 3) {
			coords = new int[] { 3, newCoords[1] };
		} else {
			coords = new int[] { 5, newCoords[1] };
		}
		if ((!board.isFieldUnderAttack(coords, "BLACK") && movedPiece
				.toString().contains("WHITE"))
				|| (!board.isFieldUnderAttack(coords, "WHITE") && movedPiece
						.toString().contains("BLACK"))) {
			value3 = false;
		}

		return value0 && value1 && value2 && value3;
	}

	/**
	 * Determine and set the values for the fields needed for the current
	 * moveType.
	 */
	private void determineMove() {
		// Loading the old board
		Piece[][] oldBoard = board.getOldBoard();

		// Determining the Move fields
		// Regular movement
		movedPiece = oldBoard[oldCoords[0]][oldCoords[1]];
		// Addition for slaying
		if (moveType == MoveType.SLAYING) {
			slainPiece = oldBoard[newCoords[0]][newCoords[1]];
		}
		// Addition for castling
		if (moveType == MoveType.CASTLING) {
			movedPieceCastling = oldBoard[oldCoordsCastling[0]][oldCoordsCastling[1]];
		}
		// TODO Addition for promotion
	}

	/**
	 * Determines the type of move (defined by the enum MoveType). And sets the
	 * coords used for determining the exact move.
	 */
	private void determineMoveType() {
		// Loading the boards that will be compared
		TempPiece[][] tempBoard = board.getTempBoard();
		Piece[][] oldBoard = board.getOldBoard();

		// Comparing the boards and setting the coords
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				// Check for changes
				if (!tempFieldEquals(tempBoard[i][j], oldBoard[i][j])) {

					// Set the coords and movetype
					// TODO implement promotion movetype (MoveType.PROMOTION).
					// Requires more detailed raw board input.
					if (tempBoard[i][j] == TempPiece.EMPTY && oldCoords == null) {
						oldCoords = new int[] { i, j };
					} else if (tempBoard[i][j] == TempPiece.EMPTY
							&& oldCoords != null) {
						oldCoordsCastling = new int[] { i, j };
					} else if (oldBoard[i][j] == null && newCoords == null) {
						newCoords = new int[] { i, j };
						moveType = MoveType.REGULAR;
					} else if (oldBoard[i][j] == null && newCoords != null) {
						newCoordsCastling = new int[] { i, j };
						moveType = MoveType.CASTLING;
					} else if (oldBoard[i][j] != null) {
						newCoords = new int[] { i, j };
						moveType = MoveType.SLAYING;
					}
				}
			}
		}
	}

	/**
	 * Checks whether a field on two boards is either empty on both, white on
	 * both, or black on both.
	 * 
	 * @param tempPiece
	 *            The field of the temporary board
	 * @param piece
	 *            The field of the old board
	 * @return True if on of the conditions is true
	 */
	private boolean tempFieldEquals(TempPiece tempPiece, Piece piece) {
		if (tempPiece == TempPiece.EMPTY && piece == null) {
			return true;
		} else if (tempPiece == TempPiece.WHITE && piece != null
				&& piece.toString().contains("WHITE")) {
			return true;
		} else if (tempPiece == TempPiece.BLACK
				&& piece.toString().contains("BLACK")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the newBoard field from board.
	 * 
	 * @return A Piece[][] of the board
	 */
	public Piece[][] getNewBoard() {
		return board.getNewBoard();
	}

	/**
	 * Returns the validity of the current move.
	 * 
	 * @return Boolean with the validity
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Gets the slain pieces array list from the board.
	 * 
	 * @return LinkedList of Piece with the slain pieces
	 */
	public LinkedList<Piece> getSlainPieces() {
		return board.getSlain();
	}

	/**
	 * Gets the old coords of all the moved pieces.
	 * 
	 * @return int[][], with the int[][0] being the x and int[][y] being the y.
	 *         int[1][] is only used for castling
	 */
	public int[][] getNewCoords() {
		int[][] value = new int[][] { newCoords, newCoordsCastling };
		return value;
	}

	/**
	 * Gets the new coords of all the moved pieces.
	 * 
	 * @return int[][], with the int[][0] being the x and int[][y] being the y.
	 *         int[1][] is only used for castling
	 */
	public int[][] getOldCoords() {
		int[][] value = new int[][] { oldCoords, oldCoordsCastling };
		return value;
	}

	/**
	 * Gets the move type of the this move.
	 * 
	 * @return MoveType
	 */
	public MoveType getMoveType() {
		return moveType;
	}

	/**
	 * Gets all the moved pieces.
	 * 
	 * @return Piece[], with Piece[1] only being used for castling
	 */
	public Piece[] getMovedPieces() {
		Piece[] value = new Piece[] { movedPiece, movedPieceCastling };
		return value;
	}

}
