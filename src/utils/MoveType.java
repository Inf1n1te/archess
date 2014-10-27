package utils;

/**
 * Different types of moves.
 * 
 * @author inf1n1te
 */
public enum MoveType {
	/**
	 * Just moving a piece.
	 */
	REGULAR, 
	/**
	 * Slaying a piece.
	 */
	SLAYING, 
	/**
	 * Performing a castling move.
	 */
	CASTLING, 
	/**
	 * Move a pawn and promote that pawn to a queen, bishop, knight or rook.
	 */
	PROMOTION
};