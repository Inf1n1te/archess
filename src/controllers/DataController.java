package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Board;
import utils.Piece;
import views.GuiView;

public class DataController implements ActionListener {

	// data controller has an GuiView and DataListener
	private GuiView view;
	private DataListener listener;
	private boolean listenerBooted;
	
	// controller has control over board
	private Board board;
	private boolean boardInit;
	
	/**
	 * Instantiates the view
	 */
	public DataController() {
		System.out.println("[DataController] DataController has started..");
		view = new GuiView();
		System.out.println("[DataController] Booted up the view");
		boardInit = false;
		// add the actionListeners on view
		addActionListeners(this);
		listenerBooted = false;
		listener = new DataListener();
		listener.register(this);
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
		System.out.println("[DataController] Action has been performed..");
		JButton clickedButton = (JButton) ae.getSource();
		if (clickedButton.getName().equals("startListener")) {
			if (!listenerBooted) {
				System.out.println("[DataController] startListener has been pushed, running DataListener..");
				listener.start();
				listenerBooted = true;
			}
		} else if (clickedButton.getName().equals("simulator")) {
			System.out.println("[DataController] Simulating a move");
		}
	}
	
	/**
	 * Gets triggered when datalistener has received all data for a matrix
	 * and build a matrix with the data
	 * @param boardData the int[][] matrix
	 */
	public void onDataReceived(int[][] boardData) {
		System.out.println("[DataController] Data has been received by the listener");
		
		// if the boad has not been initiliazed yet
		if (!boardInit) {
			board = new Board(boardData);
		} else {
			board.newTurn(boardData);
			Piece[][] pieces = board.getNewBoard();
			System.out.println(pieces[2][2]);
		}
		
		// print out the matrix for now
		for (int x = 0; x < 8; x++) {
			System.out.print("[");
			for (int y = 0; y < 8; y++) {
				System.out.print("[" + boardData[x][y] + "]");
			}
			System.out.print("]\n");
		} 
		
		
	}

}
