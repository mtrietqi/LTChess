package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Square;
import LTChess.GUI.BoardPanel.SquarePanel;
import LTChess.Group.Group;
import LTChess.MoveAnimation.GlassPane;
import LTChess.Network.NetworkChoice;
import LTChess.Network.NetworkClient;
import LTChess.Network.NetworkClient.ClientHandler;
import LTChess.Network.NetworkServer;
import LTChess.Network.NetworkServer.HandlerServer;
import LTChess.Pieces.Piece;
import LTChess.UCI.NewGameUModeDialog;
import LTChess.UCI.UCI;
import LTChess.UCI.UCILevel;
import LTChess.UCI.UndoData;

public class MainFrame
{
	protected static JFrame gameFrame = new JFrame();
	private static BoardPanel boardPanel;
	private DefeatedPiecePanel defeatedPanel;
	private JPanel rightPanel;
	private MoveHistoryPanel moveHistory;
	private ClockPanel clockPanel;
	private NewGameDialog newgameDialog;
	private PromotionDialog promotionDialog;
	private GlassPane glass; 
	
	private JMenuItem saveGameMenuItem;
	private JMenuItem loadGameMenuItem;
	private JMenuItem flipBoard;
	private JCheckBoxMenuItem showLegalMoves;
	
	public static ChessBoard board;
	static final Dimension OUTER_FRAME_DIMENSION = new Dimension(950,715);
	static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500,700);
	static final Dimension SQUARE_PANEL_DIMENSION = new Dimension(10,10);
	
	//Variables used in UCI mode
	public static NewGameUModeDialog newCmode;
	private JMenuItem undo;
	private JMenuItem redo;
	
	//Variables used in Online mode
	private NetworkChoice choice;
	public ClientHandler clientHandler;
	
	public MainFrame()
	{
		gameFrame.setTitle("LTChess");
		
		//Add elements to JFrame
		JMenuBar menuBar = new JMenuBar();
//		board = Board.createStandardBoard();
//		createRightPanel();
		createMenuBar(menuBar);
		gameFrame.setJMenuBar(menuBar);
		gameFrame.setTitle("LTChess");
//		boardPanel = new BoardPanel(this);
//		defeatedPanel = new DefeatedPiecesPanel();
		
//		gameFrame.add(boardPanel,BorderLayout.CENTER);
//		gameFrame.add(defeatedPanel,BorderLayout.WEST);
//		gameFrame.add(rightPanel,BorderLayout.EAST);
		gameFrame.setSize(OUTER_FRAME_DIMENSION);
		gameFrame.setMinimumSize(new Dimension(720,550));
		gameFrame.setResizable(false);
		setCloseWindowListener(gameFrame);
		gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	
		gameFrame.setVisible(true);
	}


	public void addDefeatedPiece(Piece piece)
	{
		defeatedPanel.addDefeatedPiece(piece);
	}
	
	private void createMenuBar(JMenuBar menuBar) {
		menuBar.add(createFileMenu());
		menuBar.add(createOptionMenu());
		menuBar.add(createHelpMenu());
	}
	
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenu newGame = new JMenu("New Game");
		JMenuItem newGameE = new JMenuItem("New Game (E mode)",new ImageIcon("resource/MenuIcons/EModeIcon.png"));
		newGameE.setAccelerator(KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		newGameE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

					newgameDialog = new NewGameDialog(MainFrame.this);
			}
		});
		
		JMenuItem newGameC = new JMenuItem("New Game (U Mode)",new ImageIcon("resource/MenuIcons/UModeIcon.png"));
		newGameC.setAccelerator(KeyStroke.getKeyStroke('U', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		newGameC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
//						gameFrame.dispose();
						newCmode = new NewGameUModeDialog(MainFrame.this);	
			}
		});
		
		JMenuItem newGameO = new JMenuItem("New Game (O mode)",new ImageIcon("resource/MenuIcons/OModeIcon.png"));
		newGameO.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		newGameO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = new NetworkChoice(MainFrame.this);
			}
		});
		newGame.add(newGameE);
		newGame.add(newGameC);
		newGame.add(newGameO);
		
		loadGameMenuItem = new JMenuItem("Load game",new ImageIcon(new String("resource/MenuIcons/LoadIcon.png")));
		loadGameMenuItem.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		loadGameMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainFrame.board != null && boardPanel.isVisible() == true)
				{
					int answer = JOptionPane.showConfirmDialog(null,"A game is currently running. Do you really want to open a new game?", "Exit", 
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer == JOptionPane.YES_OPTION)
					{
						loadGameFromFile();
					}
				}
				else
				{
					loadGameFromFile();
				}
			}
		});
		
		saveGameMenuItem = new JMenuItem("Save Game",new ImageIcon(new String("resource/MenuIcons/SaveIcon.png")));
		saveGameMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		saveGameMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveGame saveGame = new SaveGame(board, clockPanel, moveHistory, defeatedPanel, MainFrame.board.UCIMode);
				saveGame.saveToFile();
				gameFrame.setTitle("LTChess - "+saveGame.getFileName());
			}
		});
		
		saveGameMenuItem.setEnabled(false);
		JMenuItem exitMenuItem = new JMenuItem("Exit",new ImageIcon("resource/MenuIcons/QuitIcon.png"));
		
		fileMenu.add(newGame);
		fileMenu.add(loadGameMenuItem);
		fileMenu.add(saveGameMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainFrame.board != null && boardPanel.isVisible() == true)
				{
					int answer = JOptionPane.showConfirmDialog(null,"A game is currently running. Do you really want to exit?", "Exit", 
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer == JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
				}
				else
				{
					System.exit(0);
				}
			}
		});
		return fileMenu;
	}
	
	
	private JMenu createOptionMenu()
	{
		JMenu optionMenu = new JMenu("Option");
		undo = new JMenuItem("Undo",new ImageIcon("resource/MenuIcons/UndoIcon.png"));
		undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				UndoData mostupdatedData = boardPanel.createUndoData();
//				ChessBoard.undoStack.addMostUpdatedData(mostupdatedData);
				UndoData data = MainFrame.board.undoStack.undo();
				applyUndoRedo(data);
				System.out.println(MainFrame.board.undoStack);
				if (!ChessBoard.undoStack.canUndo())
				{
					undo.setEnabled(false);
				}
				if (ChessBoard.undoStack.canRedo())
				{
					redo.setEnabled(true);
				}
//				System.out.println(MainFrame.board.undoStack);
			}
		});
		redo = new JMenuItem("Redo",new ImageIcon("resource/MenuIcons/RedoIcon.png"));
		redo.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UndoData data = MainFrame.board.undoStack.redo();
				applyUndoRedo(data);
				if (!ChessBoard.undoStack.canRedo())
				{
					redo.setEnabled(false);
				}
				if (ChessBoard.undoStack.canUndo())
				{
					undo.setEnabled(true);
				}
			}
		});
		undo.setEnabled(false);
		redo.setEnabled(false);
		optionMenu.add(undo);
		optionMenu.add(redo);
		flipBoard = new JMenuItem("Flip board",new ImageIcon("resource/MenuIcons/FlipBoardIcon.png"));
		flipBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				BoardPanel.boardDirection = BoardPanel.boardDirection.opposite();
				boardPanel.drawBoard(MainFrame.board);
			}
		});
		optionMenu.add(flipBoard);
		optionMenu.addSeparator();
		
		showLegalMoves = new JCheckBoxMenuItem("Show legal moves");
		showLegalMoves.setState(true);
		showLegalMoves.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boardPanel.showLegalMoves = showLegalMoves.isSelected();
			}
		});
		showLegalMoves.setEnabled(false);
		flipBoard.setEnabled(false);
		optionMenu.add(showLegalMoves);
		return optionMenu;
	}
	
	private JMenu createHelpMenu()
	{
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About",new ImageIcon(new String("resource/MenuIcons/AboutIcon.png")));
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog aboutDialog = new AboutDialog();
				aboutDialog.setVisible(true);
			}
		});
		helpMenu.add(aboutItem);
		return helpMenu;
	}
	
	public void createRightPanel(int minTime,int secondTime)
	{
		moveHistory = new MoveHistoryPanel();
		moveHistory.setFirstMoverAlliance(board.getFirstMoverAlliance()); 
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(190,40));
		rightPanel.setBorder(BorderFactory.createEtchedBorder());
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints rightgc = new GridBagConstraints();
		
		if (minTime != -1)
		{
			clockPanel = new ClockPanel(this,minTime,secondTime);
			clockPanel.startClock(board.getFirstMoverAlliance());
		}
		else
		{
			clockPanel = new ClockPanel(this,0,0);
			clockPanel.startClock(board.getFirstMoverAlliance());
		}
		
		rightgc.gridx = 0;
		rightgc.gridy = 0;
		rightgc.weighty = 0; //Will delete it 
		rightgc.fill = GridBagConstraints.BOTH;
		rightPanel.add(clockPanel, rightgc);
		
		rightgc.gridx = 0;
		rightgc.gridy = 1;
		rightgc.weighty = 10; //Will delete it 
		rightgc.fill = GridBagConstraints.BOTH;
		rightPanel.add(moveHistory, rightgc);
		
	}
	
	private void createRightPanelFromSaveGame(SaveGame saveGame)
	{
		moveHistory = saveGame.getMoveHistory();
//		moveHistory.setFirstMoverAlliance(saveGame.getBoard().getFirstMoverAlliance()); 
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(190,40));
		rightPanel.setBorder(BorderFactory.createEtchedBorder());
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints rightgc = new GridBagConstraints();
		
		clockPanel = new ClockPanel(this,-1,-1);
		clockPanel.startClockFromSaveGame(saveGame.getClockPanelData());
		
		rightgc.gridx = 0;
		rightgc.gridy = 0;
		rightgc.weighty = 0; //Will delete it 
		rightgc.fill = GridBagConstraints.BOTH;
		rightPanel.add(clockPanel, rightgc);
		
		rightgc.gridx = 0;
		rightgc.gridy = 1;
		rightgc.weighty = 10; //Will delete it 
		rightgc.fill = GridBagConstraints.BOTH;
		rightPanel.add(moveHistory, rightgc);
	}

	public void showNewGameDialog()
	{
//		gameFrame.setVisible(false);
		newgameDialog = new NewGameDialog(MainFrame.this);
	}
	
	public void showNewGameUCIDialog()
	{
		newCmode = new NewGameUModeDialog(MainFrame.this);	
	}
	
	public void showNewOnlineDialog()
	{
		choice = new NetworkChoice(MainFrame.this);
	}
	
	public void addMoveHistory(Move move)
	{
		moveHistory.addMoveHistory(move);
		rightPanel.revalidate();
	}

	public void showPromotionDialog(Move move) 
	{
		promotionDialog = new PromotionDialog(move, this);

	}

	public Piece getPromoteToPiece() 
	{
		return promotionDialog.getPromoteToPiece();
	}
	
	public void updateClockMover()
	{
		clockPanel.updateClockMover();
	}
	
	public void stopClockPanel()
	{
		clockPanel.stopClock();
	}
	
	public static void drawBoard()
	{
		boardPanel.drawBoard(MainFrame.board);
	}

	public void clearAll() 
	{
		boardPanel.removeAll();
		boardPanel.repaint();
		defeatedPanel.removeAll();
		defeatedPanel.repaint();
		moveHistory.removeAll();
		moveHistory.repaint();
		clockPanel.stopClock();
		clockPanel.removeAll();
		clockPanel.repaint();
		gameFrame.setVisible(false);
	}

	public void updateClockMoverPromotion() {
		clockPanel.updateClockMover();
	}
	
	public void startNewGame(boolean createStandardBoard, Group firstMover, int minTime, int secondTime, boolean isUCIMode, ClientHandler clientHandler)
	{
		
		new MainFrame();
		gameFrame.dispose();
		gameFrame = new JFrame();
		gameFrame.setMinimumSize(new Dimension(720,550));
		gameFrame.setTitle("LTChess");
		gameFrame.setSize(OUTER_FRAME_DIMENSION);
		gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		createMenuBar(menuBar);
		gameFrame.setJMenuBar(menuBar);
		setCloseWindowListener(gameFrame);
		//If the board is null (has not started any game), create a new board
		if (createStandardBoard)
		{
			board = ChessBoard.createStandardBoard(firstMover);
		}
		//Does player use UCI mode?
		board.UCIMode = isUCIMode;
		//Does player use Online Mode?
		board.onlineMode = (clientHandler != null);
		boardPanel = new BoardPanel(this);
		drawBoard();
		createRightPanel(minTime,secondTime);
		defeatedPanel = new DefeatedPiecePanel();
		gameFrame.add(boardPanel,BorderLayout.CENTER);
		gameFrame.add(defeatedPanel,BorderLayout.WEST);
		gameFrame.add(rightPanel,BorderLayout.EAST);
		gameFrame.setVisible(true);
		gameFrame.validate();
		gameFrame.repaint();
		
		showLegalMoves.setEnabled(true);
		flipBoard.setEnabled(true);
		gameFrame.requestFocusInWindow();
		saveGameMenuItem.setEnabled(true);
		if (MainFrame.board.UCIMode && MainFrame.board.getFirstMoverAlliance() == Group.BLACK)
		{
			boardPanel.UCIMoveFirstFromMainFrame();
		}
		if (board.onlineMode && (board.getFirstMoverAlliance() != clientHandler.getSideGroup()))
		{
			this.clientHandler = clientHandler;
			boardPanel.waitForOpponent();
		}
		if (MainFrame.board.UCIMode)
		{
			undo.setEnabled(false);
			redo.setEnabled(false);
		}
		if (MainFrame.board.onlineMode)
		{
			saveGameMenuItem.setEnabled(false);
		}
		//At initial undo data
//		boardPanel.addUndoData();
		UndoData mostupdatedData = boardPanel.createUndoData();
		ChessBoard.undoStack.add(mostupdatedData);
//		System.out.println(ChessBoard.undoStack);
		appGlassPanel();
		

	}
	
	public void startAnotherGame(boolean standardBoard, Group firstMover, int minTime, int secondMin, boolean isUCIMode, ClientHandler networkThread)
	{
		if (clockPanel != null)
		{
			clockPanel.stopClock();
		}
		startNewGame(standardBoard,firstMover,minTime,secondMin, isUCIMode,networkThread);
	}
	
	private void loadGameFromFile() {
		JFileChooser fileChooser = new JFileChooser("C:\\");
		fileChooser.setDialogTitle("Open");
		fileChooser.setFileFilter(new fileFilter());
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String path = fileChooser.getSelectedFile().getPath();
			try {
				SaveGame saveGame = new SaveGame();
				FileInputStream file = new FileInputStream(path);
				ObjectInputStream inputFile = new ObjectInputStream(file);
				saveGame = (SaveGame) inputFile.readObject();
				if (clockPanel != null)
				{
					clockPanel.stopClock();
				}
				
				gameFrame.dispose();
				gameFrame = new JFrame();
				gameFrame.setMinimumSize(new Dimension(720,550));
				gameFrame.setTitle("LTChess - "+fileChooser.getSelectedFile().getName());
				gameFrame.setSize(OUTER_FRAME_DIMENSION);
				setCloseWindowListener(gameFrame);
				gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				JMenuBar menuBar = new JMenuBar();
				createMenuBar(menuBar);
				gameFrame.setJMenuBar(menuBar);
				//TODO Check opponent
				board = saveGame.getBoard();
				board.UCIMode = saveGame.isUCIMode();
				if (board.UCIMode)
				{
					NewGameUModeDialog.uci = new UCI(saveGame.getUCIPath(),saveGame.getUCILevel());
				}
				boardPanel = new BoardPanel(this);
				if (board.UCIMode)
				{
					boardPanel.inputCode1 = saveGame.getUCISituation();
				}
//				drawBoard();
				createRightPanelFromSaveGame(saveGame);
				defeatedPanel = saveGame.getDefeatedPiecesPanel();
				gameFrame.add(boardPanel,BorderLayout.CENTER);
				gameFrame.add(defeatedPanel,BorderLayout.WEST);
				gameFrame.add(rightPanel,BorderLayout.EAST);
				gameFrame.setVisible(true);
				gameFrame.validate();
				gameFrame.repaint();
				
				showLegalMoves.setEnabled(true);
				flipBoard.setEnabled(true);
				saveGameMenuItem.setEnabled(true);
				appGlassPanel();
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "An error occured during loading. Please check your file");
				e.printStackTrace();
			}
		}
	}
	
	
	private void setCloseWindowListener(JFrame gameFrame) 
	{
		gameFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				if (MainFrame.board != null && boardPanel.isVisible())
				{
					int answer = JOptionPane.showConfirmDialog(null,"A game is currently running. Do you really want to quit?", "Exit", 
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer == JOptionPane.YES_OPTION)
					{
						if (MainFrame.board.onlineMode)
						{
							clientHandler.setCloseMe(true);
						}
						System.exit(0);
					}
				}
				else
				{
					System.exit(0);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private class fileFilter extends FileFilter {

		@Override
		public String getDescription() {
			String str = ".ltchess File";
			return str;
		}

		@Override
		public boolean accept(File f) {
			String fn = f.getName();
			return f.isDirectory() || fn.endsWith(".ltchess");
		}
	}
	
	//------------------------START METHODS FOR UNDO AND REDO---------------------//
	//This method is used in UCI mode to free menu items when UCI is thinking 
	public void freezeMenuItems() {
		undo.setEnabled(false);
		redo.setEnabled(false);
		saveGameMenuItem.setEnabled(false);
	}
	
	public void unfreezeMenuItems() {
		if (ChessBoard.undoStack.canUndo())
		{
			undo.setEnabled(true);
		}
		if (ChessBoard.undoStack.canRedo())
		{
			redo.setEnabled(true);
		}
		saveGameMenuItem.setEnabled(true);
	}
	
	
	public ClockPanel getClockPanel()
	{
		return this.clockPanel;
	}
	
	public BoardPanel getBoardPanel()
	{
		return this.boardPanel;
	}
	
	public MoveHistoryPanel getMoveHistoryPanel()
	{
		return this.moveHistory;
	}
	
	public DefeatedPiecePanel getDefeatedPiecePanel()
	{
		return this.defeatedPanel;
	}
	
	public JMenuItem getUndoMenuItem()
	{
		return undo;
	}
	
	public JMenuItem getRedoMenuItem()
	{
		return redo;
	}
	
	private void applyUndoRedo(UndoData data)
	{
		MainFrame.board = data.getBoard();
		boardPanel.drawBoard(MainFrame.board);
		defeatedPanel.setDefeatedWhitePieces(data.getDefeatedWhitePieces());
		defeatedPanel.setDefeatedBlackPieces(data.getDefeatedBlackPieces());
		defeatedPanel.drawDefeatedPanel();
		defeatedPanel.repaint();
		moveHistory.setHistoryData(data.getMoveHistory());
		clockPanel.undoClock(data.getWhiteTime(), data.getBlackTime());
//		System.out.println("Current stack: ");
//		System.out.println(ChessBoard.undoStack);
	}
	//------------------------END METHODS FOR UNDO AND REDO---------------------//
	
	//--------------------START METHODS FOR GLASSPANE (MOVE ANIMTION OF PIECES)-----------------------//
	private void appGlassPanel()
	{
		glass = new GlassPane();
	    glass.setVisible(true);
//	    glass.setLayout(new GridBagLayout()); 
	    gameFrame.setGlassPane(glass);
	    glass.setOpaque(false);
	    glass.setVisible(true);
	}
	
	public void doAnimation(Piece movedPiece, Square source, Square destination, SquarePanel sourceSquarePanel, SquarePanel desSquarePanel) 
	{
		glass.doAnimation(movedPiece,source,destination,sourceSquarePanel, desSquarePanel);
	}
	
	//--------------------END METHODS FOR GLASSPANE (MOVE ANIMTION OF PIECES)-----------------------//

	//--------------------METHODS FOR ONLINE MODE-----------------------//
	public void oppoMakeMove(String move) {
		boardPanel.oppoMakeMove(move);
	}
	
	public void oppoPromote(int current, int des, String promoteToChar)
	{
		boardPanel.oppoPromote(current, des, promoteToChar);
	}
	
	public void waitForOpponent()
	{
		boardPanel.waitForOpponent();
	}
	
	public void setClientHandler(ClientHandler handler)
	{
		clientHandler = handler;
	}
	
	public void lostConnection()
	{

		boardPanel.lostConnection();
		nearlyOpenNew();
	}
	
	//--------------------END METHODS FOR ONLINE MODE-----------------------//
	public void nearlyOpenNew()
	{
		boardPanel.setVisible(false);
		clockPanel.setVisible(false);
		clockPanel.stopClock();
		moveHistory.setVisible(false);
		defeatedPanel.setVisible(false);
		rightPanel.setVisible(false);
	}
	
	public void prepareToStartNewGame()
	{
		clockPanel.stopClock();
		if (MainFrame.board.onlineMode)
		{
			clientHandler.setCloseMe(true);
			clockPanel.stopClock();
			boardPanel.lostConnection();
			if (HandlerServer.socket != null)
			{
				try {
					HandlerServer.in.close();
					HandlerServer.out.close();
					HandlerServer.socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
