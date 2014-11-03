package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

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
import controllers.ReplayController;
import net.miginfocom.swing.MigLayout;

public class GuiView {

	// all the necessary components
	private JPanel history, board, headerPanel;
	private JButton startListener, simulator, replay, newGameButton;
	private JTextArea textArea, historyArea, blackSlainArea, whiteSlainArea;
	private JFrame window;
	private JLabel[][] squares, backupSquares;
	private JLabel[] piecesImg;
	private JPanel boardPieces;
	private HashMap<Integer, JLabel> oldMoveSet;
	private int oldmove;
	private DataController controller;
	private JLabel isValid, isTurn, blackTurnLabel, whiteTurnLabel, validLabel, invalidLabel;
	
	/**
	 * Create a new window with components [history], [board]
	 * Managed with a [miglayout]
	 */
	public GuiView() {
		squares = new JLabel[8][8];
		backupSquares = new JLabel[8][8];
		piecesImg = new JLabel[36];
		
		window = new JFrame("ARChess");
		window.setSize(new Dimension(1024, 786));
		window.setResizable(false);
		
		oldMoveSet = new HashMap();
		oldmove = 0;
		
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
		
		headerPanel = new JPanel(new MigLayout("ins 0"));
		
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
		
		replay = new JButton("Play recorded game");
		replay.setName("replay");
		
		BufferedImage header = null;
		BufferedImage turnBlack = null;
		BufferedImage turnWhite = null;
		BufferedImage valid = null;
		BufferedImage invalid = null;
		BufferedImage newGame = null;
		try {
			header = ImageIO.read(getClass().getResource("/images/header.png"));
			turnBlack = ImageIO.read(getClass().getResource("/images/turnblack.png"));
			turnWhite = ImageIO.read(getClass().getResource("/images/turnwhite.png"));
			valid = ImageIO.read(getClass().getResource("/images/valid.png"));
			newGame = ImageIO.read(getClass().getResource("/images/newGame.png"));
			invalid = ImageIO.read(getClass().getResource("/images/invalid.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel headerLabel = new JLabel(new ImageIcon(header));
		blackTurnLabel = new JLabel(new ImageIcon(turnBlack));
		whiteTurnLabel = new JLabel(new ImageIcon(turnWhite));
		validLabel = new JLabel(new ImageIcon(valid));
		invalidLabel = new JLabel(new ImageIcon(invalid));
		isValid = new JLabel();
		isTurn = new JLabel();
		
		// white starts
		isTurn = whiteTurnLabel;
		isValid = validLabel;
		
		newGameButton = new JButton(new ImageIcon(newGame));
		newGameButton.setBorder(BorderFactory.createEmptyBorder());
		newGameButton.setContentAreaFilled(false);
		newGameButton.setName("newgame");
		
		headerPanel.add(headerLabel, "width 430px, height 50px, split 4");
		headerPanel.add(newGameButton, "width 170px, height 50px");
		headerPanel.add(isValid, "width 170px, height 50px");
		headerPanel.add(isTurn, "width 170px, height 50px, wrap");
		headerPanel.setOpaque(false);
		
		mainPanel.add(new JLabel(), "width 600px, height 10px, wrap");
		mainPanel.add(headerPanel, "width 940px, height 50px, wrap");
		mainPanel.add(history, "width 350px, height 600px, split 2");
		mainPanel.add(board, "width 640px, height 600px, wrap");
		//mainPanel.add(scrollPane, "width 100%, height 100px, wrap");
		mainPanel.add(startListener, "width 30%, height 30px, split 3");
		mainPanel.add(replay, "width 30%, height 30px");
		mainPanel.add(simulator, "width 30%, height 30px, wrap");
		window.add(mainPanel, "width 100%, height 100%");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	/**
	 * Reverts the board to init state by reverting moves one by one
	 * @param moves list with the moves done
	 * @param reset flag whether to replay moves after reverting
	 */
	public void revertBoard(HashMap moves, boolean reset) {
		boardPieces.removeAll();
		boardPieces.repaint();
		
		int[] moveCords;
		
		for (int i = (moves.size() - 1); i >= 0; i--) {
		
			moveCords = (int[]) moves.get(i);
			squares[moveCords[1]][moveCords[0]] = squares[moveCords[3]][moveCords[2]];
			squares[moveCords[3]][moveCords[2]] = oldMoveSet.get(i);
		
		}
		
		//oldMoveSet.clear();
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
		
		if (!reset) {
		
			ReplayController replayHandler = new ReplayController();
			replayHandler.setView(this);
			replayHandler.setMoves(moves);
			replayHandler.start();
		}
	}
	
	/**
	 * Redraws the board
	 * @param oldcords the old position of the piece
	 * @param newcords the new position of the piece
	 */
	public void redrawBoard(int[] oldcords, int[] newcords, boolean replay) {
		boardPieces.removeAll();
		boardPieces.repaint();
		
		if (!replay) {
			oldMoveSet.put(oldmove, squares[newcords[1]][newcords[0]]);
			oldmove++;
		}
		
		squares[newcords[1]][newcords[0]] = squares[oldcords[1]][oldcords[0]];
		
		JLabel empty = new JLabel();
		empty.setName("empty");
		squares[oldcords[1]][oldcords[0]] = empty; 
		
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
	 * Sets the valid label to valid or invalid
	 * @param valid the validity of the move
	 */
	public void setValid(boolean valid) {
		headerPanel.remove(isValid);
		headerPanel.remove(isTurn);
		if (valid) {
			isValid = validLabel;
		} else {
			isValid = invalidLabel;
		}
		headerPanel.add(isValid);
		
		// set moves
		if (isTurn == whiteTurnLabel) {
			isTurn = blackTurnLabel;
		} else {
			isTurn = whiteTurnLabel;
		}
		
		headerPanel.add(isTurn, "wrap");
		
		headerPanel.revalidate();
		headerPanel.repaint();
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
	 * Adds a black slain piece
	 */
	public void addBlackSlain(String piece) {
		blackSlainArea.append(piece);
	}
	
	/** 
	 * Adds a white slain piece
	 */
	public void addWhiteSlain(String piece) {
		whiteSlainArea.append(piece);
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
		historyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		blackSlainArea = new JBackgroundTextArea("areabg.png");
		blackSlainArea.setBackground(Color.decode("#916335"));
		JScrollPane blackPane = new JScrollPane(blackSlainArea); 
		blackSlainArea.setMargin(new Insets(10,10,10,10)); 
		blackPane.setBorder(null);
		blackSlainArea.setFont(newFont);
		blackSlainArea.setEditable(false);
		blackSlainArea.setForeground(Color.decode("#f4ede7"));
		blackPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		whiteSlainArea = new JBackgroundTextArea("areabg.png");
		whiteSlainArea.setBackground(Color.decode("#916335"));
		JScrollPane whitePane = new JScrollPane(whiteSlainArea);
		whiteSlainArea.setMargin(new Insets(10,10,10,10)); 
		whiteSlainArea.setForeground(Color.decode("#f4ede7"));
		whiteSlainArea.setFont(newFont);
		whitePane.setBorder(null);
		whitePane.setBackground(Color.blue);
		whiteSlainArea.setEditable(false);
		whitePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
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
				JLabel empty = new JLabel();
				empty.setName("empty");
				squares[x][y] = empty;
			}
		}
		
		// create squares
		createSquares();
		
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
	
	public void createSquares() {
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
	}
	
	/**
	 * Resets the board view
	 */
	public void reset(HashMap moves) {
		revertBoard(moves, true);
		oldmove = 0;
		oldMoveSet.clear();
		headerPanel.remove(isValid);
		headerPanel.remove(isTurn);
		isValid = validLabel;
		headerPanel.add(isValid);
		isTurn = whiteTurnLabel;
		headerPanel.add(isTurn, "wrap");
		headerPanel.revalidate();
		headerPanel.repaint();
		
		historyArea.setText(null);
		blackSlainArea.setText(null);
		whiteSlainArea.setText(null);
	}
	
	/**
	 * Adds the actionListeners on the buttons.
	 */
	public void addActionListeners(DataController ae) {
		controller = ae;
		startListener.addActionListener(ae);
		simulator.addActionListener(ae);
		replay.addActionListener(ae);
		newGameButton.addActionListener(ae);
	}
}
