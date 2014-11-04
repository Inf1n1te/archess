package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;

import model.Board;
import utils.MoveType;
import utils.Piece;
import views.GuiView;

public class DataController implements ActionListener {

	// data controller has an GuiView and DataListener
	private GuiView view;
	private DataListener listener;
	private boolean listenerBooted;
	private HashMap moves;
	
	// controller has control over board
	private Board board;
	private int test, moveNumber, slainBlack, slainWhite;
	
	/**
	 * Instantiates the view
	 */
	public DataController() {
		view = new GuiView();
		// add the actionListeners on view
		addActionListeners(this);
		
		listenerBooted = false;
		listener = new DataListener();
		listener.register(this);
		test = 1;
		
		slainBlack = 0;
		slainWhite = 0;
		
		moves = new HashMap();
		moveNumber = 0;
		
		initBoard();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		view.addMessage("[DataController] An action has been performed.\n");
		JButton clickedButton = (JButton) ae.getSource();
		if (clickedButton.getName().equals("startListener")) {
			if (!listenerBooted) {
				view.addMessage("[DataController] startListener has been pushed, running DataListener.\n");
				listener.start();
				listenerBooted = true;
			}
		} else if (clickedButton.getName().equals("newgame")) {
			reset();
		} else if (clickedButton.getName().equals("replay")) {
			System.out.println("Replaying the game");
			replayGame();
		} else if (clickedButton.getName().equals("simulator")) {
			simulate();
		}
	}
	
	/** 
	 * Resets the controller side of the board, and calls the reset function
	 * on the view side
	 */
	private void reset() {
		System.out.println("Starting new game");
		HashMap movesCopy = (HashMap) moves.clone();
		view.reset(movesCopy);
		moves.clear();
		
		// reinit the board
		initBoard();
		
		// reinit variables
		moveNumber = 0;
		test = 1;
		slainBlack = 0;
		slainWhite = 0;
	}
	
	/** 
	 * Initialize board
	 */
	private void initBoard() {
		// create board
		board = new Board(new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 } }
		);
	}
	
	/** 
	 * Triggers the addActionListener function on view
	 * @param ae - the current dataController
	 */
	private void addActionListeners(DataController ae) {
		view.addActionListeners(ae);
	}
	
	/** 
	 * Calls the revertboard function on the view with flag reset false
	 * meaning all the moves will get executed after reverting
	 */
	private void replayGame() {
		view.revertBoard(moves, false);
	}
	
	/**
	 * Gets triggered when datalistener has received all data for a matrix
	 * and build a matrix with the data
	 * @param boardData the int[][] matrix
	 */
	public void onDataReceived(int[][] boardData) {
		view.addMessage("[DataController] Data has been received by the listener\n");
		
		// if the boad has not been initiliazed yet
		if (board == null) {
			initBoard();
		} else {
			board.newTurn(boardData);
			Piece[][] pieces = board.getNewBoard();
			for (int x = 7; x >= 0; x--) {
				for (int y = 0; y < 8; y++) {
					view.addMessage("[" + pieces[y][x] + "]");
				}
				view.addMessage("\n");
			} 
			redrawBoard();
			addInformation();
		}
		
		/* print out the matrix for now
		for (int x = 7; x >= 0; x--) {
			for (int y = 0; y < 8; y++) {
				view.addMessage("[" + boardData[y][x] + "]");
			}
			view.addMessage("\n");
		} 
		*/
		
	}
	
	/** gives the coordinates to redraw the board
	 */
	public void redrawBoard() {
		int[] oldcords = board.getMove().getOldCoords()[0];
		int[] newcords = board.getMove().getNewCoords()[0];
		
		int[] moveCords = new int[4];
		moveCords[0] = oldcords[0];
		moveCords[1] = oldcords[1];
		moveCords[2] = newcords[0];
		moveCords[3] = newcords[1];
		
		moves.put(moveNumber, moveCords);
		moveNumber++;
		
		view.redrawBoard(oldcords, newcords, false);
	}
	
	/** 
	 * Commands the view to add a message
	 * @param message message you want to append
	 */
	public void addMessage(String message) {
		view.addMessage(message);
	}
	
	/** gives information to the history view to add
	 */
	public void addInformation() {
		System.out.println(board.getMove().isValid());
		view.setValid(board.getMove().isValid());
		if (board.getMove().getMoveType() == MoveType.SLAYING) {
			
			String slainPiece = "" + board.getMove().getSlainPieces().peekLast();
			if (slainPiece.contains("BLACK")) {
				slainBlack++;
				if (slainBlack % 2 == 0) {
					view.addBlackSlain(slainPiece + "\n");
				} else {
					view.addBlackSlain(slainPiece + " ");
				}
			} else {
				slainWhite++;
				if (slainWhite % 2 == 0) {
					view.addWhiteSlain(slainPiece + "\n");
				} else {
					view.addWhiteSlain(slainPiece + " ");
				}
			}
			
			view.addHistory("[" + board.getMove().getMovedPieces()[0] + "] (" +
					board.getMove().getOldCoords()[0][0] + "," + board.getMove().getOldCoords()[0][1] + ") > (" +
					board.getMove().getNewCoords()[0][0] + "," + board.getMove().getNewCoords()[0][1] + ") \n has slain [" + 
					 board.getMove().getSlainPieces().peekLast() + "]\n");
		} else {
			view.addHistory("[" + board.getMove().getMovedPieces()[0] + "] (" +
					board.getMove().getOldCoords()[0][0] + "," + board.getMove().getOldCoords()[0][1] + ") > (" +
					board.getMove().getNewCoords()[0][0] + "," + board.getMove().getNewCoords()[0][1] + ")\n");
		}
	} 
	
	private void simulate() {
		if (test == 1) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 1, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 } }
					);
		
		} else if (test == 2) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 1, 0, 0, 2, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 } }
					);
		
		} else if (test == 3) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 1, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 } }
					);
		
		} else if (test == 4) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 0, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 1 } }
					);
		
		} else if (test == 5) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 1, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 0 } }
					);
		
		} else if (test == 6) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 1, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 0, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 0 } }
					);
		
		} else if (test == 7) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 1 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 0, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 0 } }
					);
		
		} else if (test == 8) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 1 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 0, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 0, 1, 0, 0, 0, 2, 0 } }
					);
		
		} else if (test == 9) {
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 0, 1, 0, 0, 0, 0, 2, 0 },
							new int[] { 1, 1, 0, 0, 0, 2, 2, 1 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 0, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 0, 0, 1, 0, 0, 2, 0 } }
					);
		
		}
		test++; 
	}

}
