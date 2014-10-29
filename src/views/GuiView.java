package views;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import controllers.DataController;
import net.miginfocom.swing.MigLayout;

public class GuiView {

	// all the necessary components
	JPanel history, board;
	private JButton startListener, simulator;
	
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
		
		// start button
		startListener = new JButton("Run DataListener (thread)");
		startListener.setName("startListener");
		
		// simulate button
		simulator = new JButton("Simulate move");
		simulator.setName("simulator");
		
		window.add(startListener, "width 50%, height 30px");
		window.add(simulator, "width 50%, height 30px");
		window.add(history, "width 25%");
		window.add(board, "width 75%");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
	
	/**
	 * Adds the actionListeners on the buttons.
	 */
	public void addActionListeners(DataController ae) {
		startListener.addActionListener(ae);
		simulator.addActionListener(ae);
	}
}
