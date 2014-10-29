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

		// TODO Validation

		// Building the new board for screen output
		updateBoard();
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
			// TODO
			break;
		default:
			System.err
					.println("Something went wrong with the updating of the board @Move.updateBoard");
			break;
		}
	}

	/**
	 * Determine if the move is valid and set the validity.
	 */
	private void determineValidity() {
		// TODO
	}

	/**
	 * Determine and set the values for the fields needed for the current
	 * moveType.
	 */
	private void determineMove() {
		// Loading the old board
		Piece[][] oldBoard = board.getOldBoard();

		// Determining the Move field
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
					// TODO implement promotion movetype (MoveType.PROMOTION)
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
		} else if (tempPiece == TempPiece.BLACK && piece != null
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
