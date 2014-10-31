package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import utils.GradientPanel;
import utils.JBackgroundPanel;
import utils.JBackgroundTextArea;
import controllers.DataController;
import net.miginfocom.swing.MigLayout;

public class GuiView {

	// all the necessary components
	private JPanel history, board;
	private JButton startListener, simulator;
	private JTextArea textArea, historyArea, blackSlainArea, whiteSlainArea;
	private JFrame window;
	private JLabel[][] squares;
	private JLabel[] piecesImg;
	private JPanel boardPieces;
	
	/**
	 * Create a new window with components [history], [board]
	 * Managed with a [miglayout]
	 */
	public GuiView() {
		squares = new JLabel[8][8];
		piecesImg = new JLabel[36];
		
		window = new JFrame("ARChess");
		window.setSize(new Dimension(1024, 786));
		window.setResizable(false);
		
		// miglayout
		MigLayout layout = new MigLayout("insets 0");
		window.setLayout(layout);
		
		JBackgroundPanel mainPanel = new JBackgroundPanel(new MigLayout(), "bg.png");
		
		// get history panel
		try {
			history = createHistoryView();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get board panel
		try {
			board = createBoardView();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// start button
		startListener = new JButton("Run DataListener (thread)");
		startListener.setName("startListener");
		
		// simulate button
		simulator = new JButton("Simulate move");
		simulator.setName("simulator");
		
		// console
		textArea = new JTextArea(5, 20);
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		
		textArea.append("[Console] Hello i'm your visual assist.\n");
		
		BufferedImage header = null;
		try {
			header = ImageIO.read(getClass().getResource("/images/header.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel headerLabel = new JLabel(new ImageIcon(header));
		
		mainPanel.add(headerLabel, "width 1024px, height 70px, wrap");
		mainPanel.add(history, "width 350px, height 600px, split 2");
		mainPanel.add(board, "width 640px, height 600px, wrap");
		//mainPanel.add(scrollPane, "width 100%, height 100px, wrap");
		mainPanel.add(startListener, "width 50%, height 30px, split 2");
		mainPanel.add(simulator, "width 50%, height 30px, wrap");
		window.add(mainPanel, "width 100%, height 100%");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	/**
	 * Redraws the board
	 * @param oldcords
	 * @param newcords
	 */
	public void redrawBoard(int[] oldcords, int[] newcords) {
		boardPieces.removeAll();
		boardPieces.repaint();
		squares[newcords[1]][newcords[0]] = squares[oldcords[1]][oldcords[0]];
		squares[oldcords[1]][oldcords[0]] = new JLabel();
		
		boardPieces.revalidate();
		for (int x = 7; x >= 0; x--) {
			//System.out.println(x);
			for (int y = 0; y < 8; y++) {
				if (y == 0) {
					boardPieces.add(squares[x][y], "width 63px, height 63px, split 8");
				} else if (y == 7) {
					boardPieces.add(squares[x][y], "width 63px, height 63px, gapx 0, wrap");
				} else {
					boardPieces.add(squares[x][y], "width 63px, height 63px, gapx 0");
				}
			}
		}
		boardPieces.repaint();
	}
	
	/** 
	 * repaint the window (this is a test class
	 */
	public void repaintWindow() {
		System.out.println("moving piece");
		boardPieces.removeAll();
		boardPieces.repaint();
		squares[3][0] = squares[0][0];
		squares[0][0] = new JLabel();
		boardPieces.revalidate();
		for (int x = 7; x >= 0; x--) {
			//System.out.println(x);
			for (int y = 0; y < 8; y++) {
				if (y == 0) {
					boardPieces.add(squares[x][y], "width 63px, height 63px, split 8");
				} else if (y == 7) {
					boardPieces.add(squares[x][y], "width 63px, height 63px, gapx 0, wrap");
				} else {
					boardPieces.add(squares[x][y], "width 63px, height 63px, gapx 0");
				}
			}
		}
		boardPieces.repaint();
	}
	
	/**
	 * Adds a message to the console
	 */
	public void addMessage(String message) {
		textArea.append(message);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	/** 
	 * Adds a history to the history field
	 */
	public void addHistory(String historyText) {
		historyArea.append(historyText);
		historyArea.setCaretPosition(historyArea.getDocument().getLength());
	}
	
	/**
	 * Creates a panel and initializes everything needed to display history
	 * @return A history panel, with all the needed components.
	 * @throws IOException 
	 */
	private JPanel createHistoryView() throws IOException {
		JBackgroundPanel history = new JBackgroundPanel(new MigLayout(), "historybg.png");
		history.setOpaque(false);
		// elements in history panel
		
		// history textarea
		historyArea = new JBackgroundTextArea("areabg.png");
		historyArea.setBackground(Color.decode("#916335"));
		JScrollPane historyPane = new JScrollPane(historyArea); 
		historyPane.setBorder(null);
		historyPane.setOpaque(false);
		historyArea.setForeground(Color.decode("#f4ede7"));
		historyArea.setMargin(new Insets(10,10,10,10)); 
		Font newFont = new Font("Verdana", Font.PLAIN, 16);
		historyArea.setFont(newFont);
		historyArea.setEditable(false);
		
		blackSlainArea = new JBackgroundTextArea("areabg.png");
		blackSlainArea.setBackground(Color.decode("#916335"));
		JScrollPane blackPane = new JScrollPane(blackSlainArea); 
		blackPane.setBorder(null);
		blackSlainArea.setEditable(false);
		
		whiteSlainArea = new JBackgroundTextArea("areabg.png");
		whiteSlainArea.setBackground(Color.decode("#916335"));
		JScrollPane whitePane = new JScrollPane(whiteSlainArea); 
		whitePane.setBorder(null);
		whitePane.setBackground(Color.blue);
		whiteSlainArea.setEditable(false);
		
		history.add(historyPane, "width 315px, height 125px, gapx 10px, gapy 40px, wrap");
		history.add(blackPane, "width 315px, height 125px, gapy 70px, gapx 10px, wrap");
		history.add(whitePane, "width 315px, height 125px, gapy 70px, gapx 10px, wrap");
		return history;
	}
	
	/**
	 * Creates a panel and initializes everything needed to display the board
	 * @return A board panel, with all the needed components.
	 * @throws IOException 
	 */
	private JPanel createBoardView() throws IOException {
		JPanel boardWrapper = new JBackgroundPanel(new MigLayout("ins 0"), "boardbg.png");
		boardPieces = new JPanel(new MigLayout("ins 0, gapx 0, gapy 0"));
		boardPieces.setOpaque(false);
		
		// images
		BufferedImage rookWhite = ImageIO.read(getClass().getResource("/images/rookwhite.png"));
		BufferedImage horseWhite = ImageIO.read(getClass().getResource("/images/horsewhite.png"));
		BufferedImage bishopWhite = ImageIO.read(getClass().getResource("/images/bishopwhite.png"));
		BufferedImage queenWhite = ImageIO.read(getClass().getResource("/images/queenwhite.png"));
		BufferedImage kingWhite = ImageIO.read(getClass().getResource("/images/kingwhite.png"));
		BufferedImage pawnWhite = ImageIO.read(getClass().getResource("/images/pawnwhite.png"));
		BufferedImage rookBlack = ImageIO.read(getClass().getResource("/images/rookblack.png"));
		BufferedImage horseBlack = ImageIO.read(getClass().getResource("/images/horseblack.png"));
		BufferedImage bishopBlack = ImageIO.read(getClass().getResource("/images/bishopblack.png"));
		BufferedImage queenBlack = ImageIO.read(getClass().getResource("/images/queenblack.png"));
		BufferedImage kingBlack = ImageIO.read(getClass().getResource("/images/kingblack.png"));
		BufferedImage pawnBlack = ImageIO.read(getClass().getResource("/images/pawnblack.png"));
		
		// labels
		piecesImg[0] = new JLabel(new ImageIcon(rookWhite));
		piecesImg[1] = new JLabel(new ImageIcon(rookWhite));
		piecesImg[2] = new JLabel(new ImageIcon(horseWhite));
		piecesImg[3] = new JLabel(new ImageIcon(horseWhite));
		piecesImg[4] = new JLabel(new ImageIcon(bishopWhite));
		piecesImg[5] = new JLabel(new ImageIcon(bishopWhite));
		piecesImg[6] = new JLabel(new ImageIcon(queenWhite));
		piecesImg[7] = new JLabel(new ImageIcon(kingWhite));
		piecesImg[8] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[9] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[10] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[11] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[12] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[13] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[14] = new JLabel(new ImageIcon(pawnWhite));
		piecesImg[15] = new JLabel(new ImageIcon(pawnWhite));
		
		piecesImg[16] = new JLabel(new ImageIcon(rookBlack));
		piecesImg[17] = new JLabel(new ImageIcon(rookBlack));
		piecesImg[18] = new JLabel(new ImageIcon(horseBlack));
		piecesImg[19] = new JLabel(new ImageIcon(horseBlack));
		piecesImg[20] = new JLabel(new ImageIcon(bishopBlack));
		piecesImg[21] = new JLabel(new ImageIcon(bishopBlack));
		piecesImg[22] = new JLabel(new ImageIcon(queenBlack));
		piecesImg[23] = new JLabel(new ImageIcon(kingBlack));
		piecesImg[24] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[25] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[26] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[27] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[28] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[29] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[30] = new JLabel(new ImageIcon(pawnBlack));
		piecesImg[31] = new JLabel(new ImageIcon(pawnBlack));
		
		for (int x = 7; x >= 0; x--) {
			//System.out.println(x);
			for (int y = 0; y < 8; y++) {
				squares[x][y] = new JLabel();
			}
		}
		
		// squares
		squares[0][0] = piecesImg[0];
		squares[0][1] = piecesImg[2];
		squares[0][2] = piecesImg[4];
		squares[0][3] = piecesImg[6];
		squares[0][4] = piecesImg[7];
		squares[0][5] = piecesImg[5];
		squares[0][6] = piecesImg[3];
		squares[0][7] = piecesImg[1];
		squares[1][0] = piecesImg[8];
		squares[1][1] = piecesImg[9];
		squares[1][2] = piecesImg[10];
		squares[1][3] = piecesImg[11];
		squares[1][4] = piecesImg[12];
		squares[1][5] = piecesImg[13];
		squares[1][6] = piecesImg[14];
		squares[1][7] = piecesImg[15];
		
		squares[7][0] = piecesImg[16];
		squares[7][1] = piecesImg[18];
		squares[7][2] = piecesImg[20];
		squares[7][3] = piecesImg[22];
		squares[7][4] = piecesImg[23];
		squares[7][5] = piecesImg[21];
		squares[7][6] = piecesImg[19];
		squares[7][7] = piecesImg[17];
		squares[6][0] = piecesImg[24];
		squares[6][1] = piecesImg[25];
		squares[6][2] = piecesImg[26];
		squares[6][3] = piecesImg[27];
		squares[6][4] = piecesImg[28];
		squares[6][5] = piecesImg[29];
		squares[6][6] = piecesImg[30];
		squares[6][7] = piecesImg[31];
		
		for (int x = 7; x >= 0; x--) {
			//System.out.println(x);
			for (int y = 0; y < 8; y++) {
				if (y == 0) {
					boardPieces.add(squares[x][y], "width 63px, height 63px, split 8");
				} else if (y == 7) {
					boardPieces.add(squares[x][y], "width 63px, height 63px, gapx 0, wrap");
				} else {
					boardPieces.add(squares[x][y], "width 63px, height 63px, gapx 0");
				}
			}
		}
		
		boardWrapper.add(boardPieces, "width 504px, height 504px, gapx 68px, gapy 48px");
		boardWrapper.setOpaque(false);
		return boardWrapper;
	}
	
	/**
	 * Adds the actionListeners on the buttons.
	 */
	public void addActionListeners(DataController ae) {
		startListener.addActionListener(ae);
		simulator.addActionListener(ae);
	}
}
