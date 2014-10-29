package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	// controller has control over board
	private Board board;
	private int test;
	
	/**
	 * Instantiates the view
	 */
	public DataController() {
		System.out.println("[DataController] DataController has started..");
		view = new GuiView();
		System.out.println("[DataController] Booted up the view");
		// add the actionListeners on view
		addActionListeners(this);
		listenerBooted = false;
		listener = new DataListener();
		listener.register(this);
		test = 0;
	}
	
	/** 
	 * Triggers the addActionListener function on view
	 * @param ae - the current dataController
	 */
	private void addActionListeners(DataController ae) {
		view.addActionListeners(ae);
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
		} else if (clickedButton.getName().equals("simulator")) {
			view.addMessage("[DataController] Simulating a move.\n");
			
			if (test == 0) {
			
			listener.sampleData(
					new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
							new int[] { 1, 1, 0, 0, 0, 0, 2, 2 } }
					);
			
			
			} else if (test == 1) {
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
			
			}
			test++;
		}
	}
	
	/**
	 * Gets triggered when datalistener has received all data for a matrix
	 * and build a matrix with the data
	 * @param boardData the int[][] matrix
	 */
	public void onDataReceived(int[][] boardData) {
		view.addMessage("[DataController] Data has been received by the listener");
		
		// if the boad has not been initiliazed yet
		if (board == null) {
			board = new Board(boardData);
		} else {
			board.newTurn(boardData);
			Piece[][] pieces = board.getNewBoard();
			for (int x = 7; x >= 0; x--) {
				for (int y = 0; y < 8; y++) {
					view.addMessage("[" + pieces[y][x] + "]");
				}
				view.addMessage("\n");
			} 
			addHistory();
		}
		
		// print out the matrix for now
		for (int x = 7; x >= 0; x--) {
			for (int y = 0; y < 8; y++) {
				view.addMessage("[" + boardData[y][x] + "]");
			}
			view.addMessage("\n");
		} 
		
		
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
	public void addHistory() {
		if (board.getMove().getMoveType() == MoveType.SLAYING) {
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

}
