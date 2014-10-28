package model;

import java.util.ArrayList;

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
			new Piece[] { Piece.WHITE_KNIGHT, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_KNIGHT },
			new Piece[] { Piece.WHITE_BISHOP, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_BISHOP },
			new Piece[] { Piece.WHITE_QUEEN, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_QUEEN },
			new Piece[] { Piece.WHITE_KING, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_KING },
			new Piece[] { Piece.WHITE_BISHOP, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_BISHOP },
			new Piece[] { Piece.WHITE_KNIGHT, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_KNIGHT },
			new Piece[] { Piece.WHITE_ROOK, Piece.WHITE_PAWN, null, null, null,
					null, Piece.BLACK_PAWN, Piece.BLACK_ROOK } };
	/**
	 * Initial state of the board with black pieces in the rows y=0 and y=1.
	 */
	private final Piece[][] INIT_BLACK = new Piece[][] {
			new Piece[] { Piece.BLACK_ROOK, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_ROOK },
			new Piece[] { Piece.BLACK_KNIGHT, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_KNIGHT },
			new Piece[] { Piece.BLACK_BISHOP, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_BISHOP },
			new Piece[] { Piece.BLACK_KING, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_KING },
			new Piece[] { Piece.BLACK_QUEEN, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_QUEEN },
			new Piece[] { Piece.BLACK_BISHOP, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_BISHOP },
			new Piece[] { Piece.BLACK_KNIGHT, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_KNIGHT },
			new Piece[] { Piece.BLACK_ROOK, Piece.BLACK_PAWN, null, null, null,
					null, Piece.WHITE_PAWN, Piece.WHITE_ROOK } };
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
	 * An ArrayList containing all the slain pieces;
	 */
	private ArrayList<Piece> slain = new ArrayList<Piece>();
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
	 * Constructor for the board. Requires a raw board as input. It will then
	 * generate a temporary board. Used for initialization.
	 * 
	 * @param rawBoard
	 *            A Byte[] containing the raw data received from the FPGA.
	 */
	public Board(Byte[] rawBoard) {
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
	private void createTempBoard(Byte[] raw) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tempBoard[i][j] = getTempPiece(raw[i * 8 + j]);
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

	public void newTurn(Byte[] rawBoard) {
		createTempBoard(rawBoard);
		oldBoard = newBoard;
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
	private void setTempBoard(TempPiece[][] tempBoard) {
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
	private void setNewBoard(Piece[][] newBoard) {
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
	private void setOldBoard(Piece[][] oldBoard) {
		this.oldBoard = oldBoard;
	}

	/**
	 * Gets the slain pieces.
	 * 
	 * @return The slain pieces.
	 */
	public ArrayList<Piece> getSlain() {
		return slain;
	}

	/**
	 * Sets the slain pieces.
	 * 
	 * @param slain
	 *            The ArrayList to which the slain pieces will be set
	 */
	private void setSlain(ArrayList<Piece> slain) {
		this.slain = slain;
	}

}
