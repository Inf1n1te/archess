package views;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class GuiView {

	// all the necessary components
	JPanel history, board;
	
	/**
	 * Create a new window with components [history], [board]
	 * Managed with a [miglayout]
	 */
	public GuiView() {
		JFrame window = new JFrame("ARChess");
		window.setSize(new Dimension(800, 600));
		
		// miglayout
		MigLayout layout = new MigLayout();
		window.setLayout(layout);
		
		// get history panel
		history = createHistoryView();
		// get board panel
		board = createBoardView();
		
		window.add(history, "width 25%");
		window.add(board, "width 75%");
		window.setVisible(true);
	}
	
	/**
	 * Creates a panel and initializes everything needed to display history
	 * @return A history panel, with all the needed components.
	 */
	private JPanel createHistoryView() {
		JPanel history = new JPanel(new MigLayout());
		history.setBackground(Color.GRAY);
		
		// elements in history panel
		JLabel historyLabel = new JLabel("History");
		history.add(historyLabel);
		
		return history;
	}
	
	/**
	 * Creates a panel and initializes everything needed to display the board
	 * @return A board panel, with all the needed components.
	 */
	private JPanel createBoardView() {
		JPanel board = new JPanel(new MigLayout());
		board.setBackground(Color.GRAY);
		
		// elements in the board panel
		JLabel boardLabel = new JLabel("Board");
		board.add(boardLabel);
		
		return board;
	}
}
