package model;

import java.util.LinkedList;

import utils.Piece;
import utils.TempPiece;

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
	private final Piece[][] INIT_BLACK = new Piece[][] {
			new Piece[] { Piece.BLACK_ROOK, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_ROOK },
			new Piece[] { Piece.BLACK_KNIGHT, Piece.BLACK_PAWN, null, null,
					null, null, Piece.WHITE_PAWN, Piece.WHITE_KNIGHT },
			new Piece[] { Piece.BLACK_BISHOP, Piece.BLACK_PAWN, null, null,
					null, null, Piece.WHITE_PAWN, Piece.WHITE_BISHOP },
			new Piece[] { Piece.BLACK_KING, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_KING },
			new Piece[] { Piece.BLACK_QUEEN, Piece.BLACK_PAWN, null, null,
					null, null, Piece.WHITE_PAWN, Piece.WHITE_QUEEN },
			new Piece[] { Piece.BLACK_BISHOP, Piece.BLACK_PAWN, null, null,
					null, null, Piece.WHITE_PAWN, Piece.WHITE_BISHOP },
			new Piece[] { Piece.BLACK_KNIGHT, Piece.BLACK_PAWN, null, null,
					null, null, Piece.WHITE_PAWN, Piece.WHITE_KNIGHT },
			new Piece[] { Piece.BLACK_ROOK, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_ROOK } };
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
	private boolean leftBlackRookMoved;
	/**
	 * False until the right black rook moves. Used for castling validation.
	 */
	private boolean rightBlackRookMoved;
	/**
	 * False until the black king moves. Used for castling validation.
	 */
	private boolean blackKingMoved;
	/**
	 * False until the left white rook moves. Used for castling validation.
	 */
	private boolean leftWhiteRookMoved;
	/**
	 * False until the right white rook moves. Used for castling validation.
	 */
	private boolean rightWhiteRookMoved;
	/**
	 * False until the white king moves. Used for castling validation.
	 */
	private boolean whiteKingMoved;
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
		} else if (tempBoard[0][0] == TempPiece.BLACK) {
			newBoard = INIT_BLACK;
		} else {
			System.err
					.println("No pieces found in tempBoard[0][0] @Board.init");
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

}
