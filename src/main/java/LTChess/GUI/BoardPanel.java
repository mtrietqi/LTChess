package LTChess.GUI;

import java.awt.Color;
//import java.awt.Dialog.ModalityType;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.plaf.LayerUI;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;
import javax.xml.bind.attachment.AttachmentUnmarshaller;

import com.google.common.collect.Lists;

import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.ChessBoard.Builder;
import LTChess.ChessBoard.BoardCheck;
import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Square;
import LTChess.ChessBoard.Move.AttackMove;
import LTChess.ChessBoard.Move.CastleMove;
import LTChess.ChessBoard.Move.KingSideCastleMove;
import LTChess.ChessBoard.Move.PawnAttackMove;
import LTChess.ChessBoard.Move.PawnPromotionAttackMove;
import LTChess.ChessBoard.Move.PawnPromotionNormalMove;
import LTChess.ChessBoard.Move.QueenSideCastleMove;
import LTChess.Group.Group;
import LTChess.MoveAnimation.GlassPane;
import LTChess.Network.OppoThinking;
import LTChess.Pieces.Knight;
import LTChess.Pieces.Piece;
import LTChess.Pieces.Queen;
import LTChess.Pieces.Rook;
import LTChess.Player.MoveTransition;
import LTChess.UCI.EngineThinking;
import LTChess.UCI.NewGameUModeDialog;
import LTChess.UCI.UCI;
import LTChess.UCI.UCIPromotion;
import LTChess.UCI.UndoData;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.LayerUI;


public class BoardPanel extends JPanel
{
	private static Color lightSquare = new Color(255,206,158);
	private static Color darkSquare = new Color(209,139,71);
	public List<SquarePanel> boardSquarePanels = new ArrayList<SquarePanel>();
	public static boolean showLegalMoves;
	
	private static Color darkSquareCopy = darkSquare;
	private static Color lightSquareCopy = lightSquare;
	private static GridBagConstraints gc;
	protected static BoardDirection boardDirection;
	
	private MainFrame parent;
	private Square sourceSquare; //Move a piece from square
	private Square destinationSquare; //To square
	private Piece humanMovedPiece; //Piece which is choosen to moved
	
	//Variables used for custom board
	private StockCustomBoard customBoard;
	private JPopupMenu popupMenu;
	private JMenuItem changePos;
	private JMenuItem removePiece;
	private Piece changedPiece; //This variable is used when user clicks on changePos menu item only
	
	//Variables used in UCI mode
	public static boolean blockMouseSelection = false;
	public static String inputCode1 = "position startpos moves";
	public static String inputCode2 = "";
	private UCI uci;
	private Thread UCIMakeMoveThread;
	private boolean UCIcanMove = false;
	private EngineThinking eThinking;
	private String promoteToChar = ""; 
	
	//Variables use for making move animation of pieces
	private Piece tempRemovedPiece;
	private int tempRemovePosition;
	private int oldPosition;
	
	//Variables used for online mode
	private OppoThinking oppoThinking; 
	public static String messageSend = null;
	private String candidateMoveText = "";
	
	public BoardPanel(MainFrame parent)
	{
		this.parent = parent;

		for (int i = 0; i < 64; i++)
		{
            final SquarePanel squarePanel = new SquarePanel(this,i);
            this.boardSquarePanels.add(squarePanel);
            add(squarePanel);
        }
		if (MainFrame.board.customBoardMode)
		{
			customBoard = EditInitialBoard.getStockCustomBoard();
			popupMenu = new JPopupMenu();
			constructPopupMenu(popupMenu);
		}
		
		this.boardDirection = BoardDirection.NORMAL;
		drawBoard(MainFrame.board);
		showLegalMoves = true;
		setPreferredSize(MainFrame.BOARD_PANEL_DIMENSION);
		validate();
		if (MainFrame.board.UCIMode)
		{
			uci = NewGameUModeDialog.uci;

			if (uci.startEngine()) {
				System.out.println("Engine has started..");
			} else {
				System.out.println("Oops! Something went wrong..");
			}
			uci.sendCommand("uci");
			uci.sendCommand("setoption name Skill Level value "+uci.getUCILevel().getEngineLevel());
			System.out.println("setoption name Skill Level value "+uci.getUCILevel().getEngineLevel());
//			System.out.println("Level: "+uci.getUCILevel());
			uci.sendCommand("ucinewgame");
		}
		else if (MainFrame.board.onlineMode)
		{
			oppoThinking = new OppoThinking(parent);
			oppoThinking.setVisible(false);
		}
	}

	public class SquarePanel extends JPanel
	{
		private int squareId;
		public SquarePanel(BoardPanel boardPanel, int squareId)
		{
			this.setLayout(new GridBagLayout());
			this.squareId = squareId;
			assignSquareIcon(MainFrame.board);
			setPreferredSize(MainFrame.SQUARE_PANEL_DIMENSION);
			
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					//--------------START UCI MODE----------------//
					if (blockMouseSelection)
						return ;
					//--------------END UCI MODE----------------//
					//--------------START CUSTOM BOARD MODE-------------------
					if (MainFrame.board.customBoardMode) {
						if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) {
							if (customBoard.getCurrentFocusPiece() != null) {
								if (MainFrame.board.getSquare(squareId).isSquareOccupied()) {
									JOptionPane.showMessageDialog(BoardPanel.this,
											"The selected square is occupied. Please remove the piece on this square"
											+ " or change its position first",
											"Error", JOptionPane.ERROR_MESSAGE);
									return;
								}
								// Add the selected piece to the board
								Builder builder = new Builder();
								for (Piece piece : MainFrame.board.getCurrentPlayer().getActivePieces()) {
									builder.locatePiece(piece);
								}

								for (Piece piece : MainFrame.board.getCurrentPlayer().getOpponent().getActivePieces()) {
									builder.locatePiece(piece);
								}

								customBoard.removeCurrentPiece();
								customBoard.getCurrentFocusPiece().setPosition(squareId);
								builder.locatePiece(customBoard.getCurrentFocusPiece());
								customBoard.resetCurrentFocusPiece();
								MainFrame.board = new ChessBoard(builder);
								drawBoard(MainFrame.board);
							}
						}
						else if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
							if (MainFrame.board.getSquare(squareId).isSquareOccupied())
							{
								if (MainFrame.board.getSquare(squareId).getPiece().getPieceType().isKing())
								{
									removePiece.setEnabled(false);
								}
								else
								{
									removePiece.setEnabled(true);
								}
								popupMenu.show(BoardPanel.this, (int) SquarePanel.this.getLocation().getX()+45,(int) SquarePanel.this.getLocation().getY()+45);
								changedPiece = MainFrame.board.getSquare(squareId).getPiece();
							}
						}
					}
					//--------------END CUSTOM BOARD MODE-------------------
					else if((e.getModifiersEx() &
							InputEvent.BUTTON3_DOWN_MASK) != 0)
					{
						sourceSquare = null;
						destinationSquare = null;
						humanMovedPiece = null;
						boardPanel.drawBoard(MainFrame.board);
					}
					else if((e.getModifiersEx() &
							InputEvent.BUTTON1_DOWN_MASK) != 0)
					{
						if (sourceSquare == null)
						{
							//It is the first click
							sourceSquare = MainFrame.board.getSquare(squareId);
							humanMovedPiece = sourceSquare.getPiece();
							if (humanMovedPiece == null) //If player selects an empty square
							{
								sourceSquare = null;
							}
						}
						else 
						{
							makeAMove(squareId);

						}
						updateBoardPanel(boardPanel);

					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					
				}
			});
			validate();
		}
		
		private void assignSquareIcon(ChessBoard board)
		{
			this.removeAll();
			if (board.getSquare(this.squareId).isSquareOccupied())
			{
				String pieceIconPath = "resource/";
				try {
					BufferedImage image = ImageIO.read(new File(pieceIconPath 
							+ board.getSquare(this.squareId).getPiece().getPieceGroup().toString().substring(0,1)+
							board.getSquare(this.squareId).getPiece().toString()+".png"));
		
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void highlightLegalMoves(ChessBoard board)
		{
			if (showLegalMoves)
			{
				for (Move move : calculateSelectedPieceLegalMoves(board)) 
				{
					if (move.getDestinationCoordinate() == this.squareId) {
						this.setBackground(new Color(122, 244, 0));
						this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
						this.repaint();
					}
				}
			}
		}

		
		private Collection<Move> calculateSelectedPieceLegalMoves(ChessBoard board)
		{
			if (humanMovedPiece != null && humanMovedPiece.getPieceGroup() == board.getCurrentPlayer().getAlliance())
				//If selected piece is not null and it has to be the same with the alliance of the current player
			{
				List<Move> moveList = new ArrayList<Move>();
				for (Move move: MainFrame.board.getCurrentPlayer().getLegalMoves())
				{
					if (move.getMovePiece() == humanMovedPiece)
					{
						moveList.add(move);
					}
				}
				return moveList;
			}
			return Collections.emptyList();
		}

	}
	
	
	private static class HorizontalSpecialSquare extends JLabel
	{
		private static String[] textArrayN = {"a","b","c","d","e","f","g","h"};
		private static String[] textArrayR = {"h","g","f","e","d","c","b","a"};
		private static int index = 0;
		public HorizontalSpecialSquare() {
			if (boardDirection == BoardDirection.NORMAL)
				this.setText(textArrayN[index]);
			else
				this.setText(textArrayR[index]);
			this.setOpaque(true);
			this.setBackground(Color.WHITE);
			index++;
		}
		
		public void resetIndex()
		{
			index = 0;
		}
	}
	
	private static class VerticalSpecialSquare extends JLabel
	{
		private static String[] textArrayN = {"8","8","7","7","6","6","5","5","4","4","3","3","2","2","1","1"};
		private static String[] textArrayR = {"1","1","2","2","3","3","4","4","5","5","6","6","7","7","8","8"};
		private static int index = 0;
		public VerticalSpecialSquare() {
			if (boardDirection == BoardDirection.NORMAL)
				this.setText(textArrayN[index]);
			else
				this.setText(textArrayR[index]);
			this.setOpaque(true);
			this.setBackground(Color.WHITE);
			index++;
		}
		
		public void resetIndex()
		{
				index = 0;
		}
	}
	
	private void addSquares(ChessBoard board)
	{
		gc = new GridBagConstraints();
		HorizontalSpecialSquare horiSquare = null;
		VerticalSpecialSquare vertiSquare = null;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.BOTH;
		for (int i  = 0; i < 10; i++)
		{
			if (i==0 || i == 9)
			{
				JPanel corner = new JPanel();
				corner.setBackground(Color.WHITE);
				this.add(corner, gc);
				gc.gridx++;
				continue;
			}
			horiSquare = new HorizontalSpecialSquare();
			horiSquare.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(horiSquare,gc);
			gc.gridx++;
		}
		gc.gridy = 9;
		gc.gridx = 0;
		horiSquare.resetIndex();
		for (int i  = 0; i < 10; i++)
		{
			if (i==0 || i == 9)
			{
				JPanel corner = new JPanel();
				corner.setBackground(Color.WHITE);
				this.add(corner, gc);
				gc.gridx++;
				continue;
			}
			horiSquare = new HorizontalSpecialSquare();
			horiSquare.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(horiSquare,gc);
			gc.gridx++;
		}
		
		gc.gridy = 1;
		gc.gridx = 0;
		for (int i  = 0; i < 8; i++)
		{
			vertiSquare = new VerticalSpecialSquare();
			vertiSquare.setVerticalAlignment(SwingConstants.CENTER);
			vertiSquare.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(vertiSquare, gc);
			
			gc.gridx = 9;
			vertiSquare = new VerticalSpecialSquare();
			vertiSquare.setVerticalAlignment(SwingConstants.CENTER);
			vertiSquare.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(vertiSquare, gc);
			gc.gridx = 0;
			gc.gridy++;
		}
		
		gc.gridy = 1;
		gc.gridx = 1;
		
		int i = 0;
		for (SquarePanel square:boardDirection.traverse(boardSquarePanels))
		{
			if (i % 8 == 0 && i!= 0)
			{
				Color backup = darkSquareCopy;
				darkSquareCopy = lightSquareCopy;
				lightSquareCopy = backup;
				gc.gridx = 1;
				gc.gridy++;
			}
			if (i % 2 == 0)
				square.setBackground(lightSquareCopy);
			else
				square.setBackground(darkSquareCopy);
			square.assignSquareIcon(board);
			square.setBorder(BorderFactory.createEmptyBorder());
			square.highlightLegalMoves(MainFrame.board);
			this.add(square,gc);	
			gc.gridx++;
			i++;
		}
		horiSquare.resetIndex();
		vertiSquare.resetIndex();
		darkSquareCopy = darkSquare;
		lightSquareCopy = lightSquare;
	}

	public void drawBoard(ChessBoard board) 
	{
		this.removeAll();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{30,25,25,25,25,25,25,25,25,30};
		gridBagLayout.rowHeights = new int[]{25,25,25,25,25,25,25,25,25,25};
		gridBagLayout.columnWeights = new double[]{0,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0};
		gridBagLayout.rowWeights = new double[]{0,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0};
		setLayout(gridBagLayout);
		addSquares(board);
		setPreferredSize(MainFrame.BOARD_PANEL_DIMENSION);
		validate();
		repaint();
	}
	
	public enum BoardDirection {
        NORMAL {
            @Override
            List<SquarePanel> traverse(final List<SquarePanel> boardSquare) {
                return boardSquare;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }

			@Override
			String[] traverse(String[] stringText) {
				return stringText;
			}
        },
        FLIPPED {
            @Override
            List<SquarePanel> traverse(final List<SquarePanel> boardSquare) {
                return Lists.reverse(boardSquare);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }

			@Override
			String[] traverse(String[] stringText) {
				List<String> stringList = new ArrayList<>();
				for (String str:stringText)
					stringList.add(str);	
				return Lists.reverse(stringList).toArray(stringText);
			}
        };

        abstract List<SquarePanel> traverse(final List<SquarePanel> boardSquare);
        abstract String[] traverse(String[] stringText);
        abstract BoardDirection opposite();
    }
	
	private void constructPopupMenu(JPopupMenu popupMenu) 
	{
		changePos = new JMenuItem("Change position");
		removePiece = new JMenuItem("Remove");
		changePos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newPosition = JOptionPane.showInputDialog(BoardPanel.this, "Please enter the coodinate you want to change:");
				if (newPosition == null)
				{
					return ;
				}
				else if (newPosition.equals("") || !BoardCheck.isValidChessCoordinate(newPosition))
				{
					JOptionPane.showMessageDialog(BoardPanel.this, "You input is invalid. Please check it again", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if (changedPiece != null)
					{
						if (MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(newPosition)).isSquareOccupied())
						{
							JOptionPane.showMessageDialog(BoardPanel.this, "The coordinate of the square you enter is being occupied by "
									+ "a piece. Please select a different coordinate", "Error", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							MainFrame.board = ChessBoard.updateAPiecePosition(changedPiece, BoardCheck.convertChessCoordinateToInt(newPosition));
							BoardPanel.this.drawBoard(MainFrame.board);
							changedPiece = null;
						}
					}
				}
			}
		});
		
		removePiece.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (changedPiece != null)
				{
					MainFrame.board = ChessBoard.removeAPiece(changedPiece);
					customBoard.addPiece(changedPiece);
					BoardPanel.this.drawBoard(MainFrame.board);
					changedPiece= null;
				}
			}
		});
		popupMenu.add(changePos);
		popupMenu.add(removePiece);
	}
	
	private void makeAMove(int squareId)
	{
		//It is the second click
		if (destinationSquare == null)
		{
			destinationSquare = MainFrame.board.getSquare(squareId);
		}
		Move move = Move.getMoves(MainFrame.board, sourceSquare.getSquareCoordinate(), destinationSquare.getSquareCoordinate());
		MoveTransition transition = MainFrame.board.getCurrentPlayer().executeMove(move);

		if (transition.getMoveResult().isDone())
		{
			//Do move animation
			parent.doAnimation(humanMovedPiece, sourceSquare, destinationSquare,
					boardSquarePanels.get(sourceSquare.getSquareCoordinate()),boardSquarePanels.get(destinationSquare.getSquareCoordinate()));
			//Save information for undo
			
			
			//For online mode
			if (MainFrame.board.onlineMode && !blockMouseSelection)
			{
				candidateMoveText = BoardCheck.convertToChessCoordinate(sourceSquare.getSquareCoordinate())+
						BoardCheck.convertToChessCoordinate(destinationSquare.getSquareCoordinate());
			}
			

			MainFrame.board = transition.getTransitionBoard();
			if (MainFrame.board.getCurrentPlayer().getAlliance() == Group.WHITE && MainFrame.board.UCIMode)
			{
				MainFrame.board.undoStack.add(createUndoData());	
			}
			if (MainFrame.board.undoStack.canUndo())
			{
				parent.getUndoMenuItem().setEnabled(true);
			}
//			System.out.println(MainFrame.board.undoStack);
			//Check for fen code
			if (move instanceof CastleMove)
			{
					if (MainFrame.board.getCurrentPlayer().getAlliance() == Group.BLACK)
					{
						MainFrame.board.whiteCanCastle = false;
					}
					else if (MainFrame.board.getCurrentPlayer().getAlliance() == Group.WHITE)
					{
						MainFrame.board.blackCanCastle = false;
					}
				
			}
			//If the move has made is an attack move, add the defeated move to DefeatedPiecesPanel
			if (move instanceof AttackMove)
			{
				parent.addDefeatedPiece(move.getAttackedPiece());
			}
			if (move instanceof PawnPromotionNormalMove || move instanceof PawnPromotionAttackMove)
			{
				if (MainFrame.board.getCurrentPlayer().getAlliance() == Group.WHITE &&  MainFrame.board.UCIMode)
				{
					if (!promoteToChar.equals(""))
					{
						UCIPromotion uciPro = new UCIPromotion(promoteToChar, move);
						MainFrame.board = uciPro.getUCIPromotionMove().execute();
						promoteToChar = "";
						parent.updateClockMover();	
					}
				}
				else if (MainFrame.board.onlineMode) //It is an online mode
				{
					if (!blockMouseSelection) //Can show promotion dialog for this user
					{
						parent.showPromotionDialog(move);
						candidateMoveText = "";
					}
					else //Borrow UCIPromotion class to do the promotion in online mode
					{
						if (!promoteToChar.equals(""))
						{
							UCIPromotion uciPro = new UCIPromotion(promoteToChar, move);
							MainFrame.board = uciPro.getUCIPromotionMove().execute();
							promoteToChar = "";
							parent.updateClockMover();	
						}
					}
				}
				else
				{
					parent.showPromotionDialog(move);
				}
			}
			//Write data to Move History
			parent.addMoveHistory(move);
			//Check if the user play with UCI and now it's turn of UCI
			if (MainFrame.board.getCurrentPlayer().getAlliance() == Group.BLACK && MainFrame.board.UCIMode)
			{
				UCIcanMove = true;
			}
			
			tempRemovedPiece = sourceSquare.getPiece();
			oldPosition = sourceSquare.getPiece().getPiecePosition();
			tempRemovePosition = destinationSquare.getSquareCoordinate();
		}
		else if (transition.getMoveResult().isInChecK())
		{
			 JOptionPane.showMessageDialog(BoardPanel.this, "You cannot make this move because your king might be killed after executing it", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		sourceSquare = null;
		destinationSquare = null;
		humanMovedPiece = null;
		if (!(move instanceof PawnPromotionNormalMove) && !(move instanceof PawnPromotionAttackMove))
		{
			parent.updateClockMover();	
		}
	}
	
	private void updateBoardPanel(BoardPanel boardPanel)
	{
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				
				if (tempRemovedPiece != null && tempRemovePosition != -1)
				{
					tempRemovedPiece.setPosition(tempRemovePosition);
					boardPanel.drawBoard(MainFrame.board.removeAPiece(tempRemovedPiece));
					tempRemovedPiece.setPosition(oldPosition);
					tempRemovedPiece = null;
					tempRemovePosition = -1;
				}
				else
				{
					boardPanel.drawBoard(MainFrame.board);
				}
				//Check if the game can continue or not
				if (MainFrame.board.getCurrentPlayer().isInCheckMate())
				{
					parent.stopClockPanel();
					if (MainFrame.board.onlineMode)
					{
						if (!blockMouseSelection && !candidateMoveText.equals(""))
						{
							BoardPanel.messageSend = candidateMoveText;
//							waitForOpponent();
							candidateMoveText = "";
						}
					}
					int answer = JOptionPane.showConfirmDialog(BoardPanel.this, MainFrame.board.getCurrentPlayer().getOpponent().getAlliance()+" wins because his opponent is in checkmate now. Congratulations!"
							+ " Do you want to play again?", "Checkmate", 
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer != JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
					else
					{
						parent.nearlyOpenNew();
						if (MainFrame.board.UCIMode)
						{
							parent.showNewGameUCIDialog();
						}
						else if (MainFrame.board.onlineMode)
						{
							parent.showNewOnlineDialog();
						}
						else
						{
							parent.showNewGameDialog();
						}
					}
				}
				else if (MainFrame.board.getCurrentPlayer().isInStaleMate())
				{
					parent.stopClockPanel();
					if (MainFrame.board.onlineMode)
					{
						if (!blockMouseSelection && !candidateMoveText.equals(""))
						{
							BoardPanel.messageSend = candidateMoveText;
//							waitForOpponent();
							candidateMoveText = "";
						}
					}
					int answer = JOptionPane.showConfirmDialog(BoardPanel.this, MainFrame.board.getCurrentPlayer().getOpponent().getAlliance()+" wins because his opponent is in stalemate now. Congratulations!"
							+ " Do you want to play again?", "Stalemate", 
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer != JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
					else
					{
						parent.nearlyOpenNew();
						if (MainFrame.board.UCIMode)
						{
							parent.showNewGameUCIDialog();
						}
						else if (MainFrame.board.onlineMode)
						{
							parent.showNewOnlineDialog();
						}
						else
						{
							parent.showNewGameDialog();
						}
					}
				}
				else if (MainFrame.board.isDrawGame())
				{
					parent.stopClockPanel();
					if (MainFrame.board.onlineMode)
					{
						if (!blockMouseSelection && !candidateMoveText.equals(""))
						{
							BoardPanel.messageSend = candidateMoveText;
//							waitForOpponent();
							candidateMoveText = "";
						}
					}
					int answer = JOptionPane.showConfirmDialog(BoardPanel.this, "It is a draw game."
							+ " Do you want to play again?", "Draw", 
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (answer != JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
					else
					{
						parent.nearlyOpenNew();
						if (MainFrame.board.UCIMode)
						{
							parent.showNewGameUCIDialog();
						}
						else if (MainFrame.board.onlineMode)
						{
							parent.showNewOnlineDialog();
						}
						else
						{
							parent.showNewGameDialog();
						}
					}
				} 
				else if (MainFrame.board.UCIMode) {
					if (UCIcanMove)
					{
						UCIcanMove = false;
						blockMouseSelection = true;
						eThinking = new EngineThinking();
						eThinking.setLocationRelativeTo(parent.getMoveHistoryPanel());
						UCIMakeMoveThread = new Thread(new Runnable() {
							@Override
							public void run() {
								UCIMakeMove();
							}
						});
						UCIMakeMoveThread.start();
					}
				}
				else if (MainFrame.board.onlineMode)
				{
					if (!blockMouseSelection && !candidateMoveText.equals(""))
					{
						BoardPanel.messageSend = candidateMoveText;
						waitForOpponent();
						candidateMoveText = "";
					}
				}
			}
		});
	}
	
	private void UCIMakeMove() {
		parent.freezeMenuItems();
		inputCode2 = "go wtime "+parent.getClockPanel().getWhiteTime()+" btime "+parent.getClockPanel().getBlackTime()+" winc 0 binc 0";
		String fenCode = BoardCheck.covertToFenCode(MainFrame.board);
		String command1 = "position fen "+fenCode;
//		System.out.println(inputCode2);
//		System.out.println(command1);
//		System.out.println(inputCode2);
		uci.sendCommand(command1);
		uci.sendCommand(inputCode2);
		try {
			String originalMove = uci.readIt();
//			System.out.println("Original code: "+originalMove);
			String bestmove = originalMove.substring(originalMove.indexOf(" ")+1, originalMove.indexOf(" ")+5);
//			System.out.println("Best move: "+bestmove);
			try
			{
				bestmove = originalMove.substring(originalMove.indexOf(" ")+1, originalMove.indexOf(" ", originalMove.indexOf(" ")+1));
			}
			catch (StringIndexOutOfBoundsException e)
			{
				bestmove = originalMove.substring(originalMove.indexOf(" ")+1);
			}
			//If this is a normal move
			if (bestmove.length() == 4)
			{
				sourceSquare = MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(bestmove.substring(0,2)));
				destinationSquare = MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(bestmove.substring(2)));
			}
			else if (bestmove.length() == 5)
			{
				sourceSquare = MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(bestmove.substring(0,2)));
				destinationSquare = MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(bestmove.substring(2,4)));
				if (!bestmove.endsWith("\n"))
				{
					promoteToChar = bestmove.substring(4);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		eThinking.setVisible(false);
		makeAMove(sourceSquare.getSquareCoordinate());
		updateBoardPanel(BoardPanel.this);
		blockMouseSelection = false;
		parent.unfreezeMenuItems();
//		uci.drawBoard("");
	}
	
	//This method is used to start the engine from MainFrame
	public void UCIMoveFirstFromMainFrame()
	{
		UCIcanMove = false;
		blockMouseSelection = true;
		eThinking = new EngineThinking();
		eThinking.setLocationRelativeTo(parent.getMoveHistoryPanel());
		UCIMakeMoveThread = new Thread(new Runnable() {
			@Override
			public void run() {
				UCIMakeMove();
			}
		});
		UCIMakeMoveThread.start();
	}
	
	//This method is used to add undo data
	public UndoData createUndoData() {

		List<Piece> defeatedWhitePieces = new ArrayList<Piece>();
		for (Piece piece : parent.getDefeatedPiecePanel().getDefeatedWhitePieces()) {
			defeatedWhitePieces.add(piece);
		}
		List<Piece> defeatedBlackPieces = new ArrayList<Piece>();
		for (Piece piece : parent.getDefeatedPiecePanel().getDefeatedBlackPieces()) {
			defeatedBlackPieces.add(piece);
		}
		UndoData undoData = new UndoData(MainFrame.board.copy(), defeatedWhitePieces, defeatedBlackPieces,
				parent.getClockPanel().getWhiteTime(), parent.getClockPanel().getBlackTime(),
				parent.getMoveHistoryPanel().getHistoryData());
		return undoData;
	}
	
	//------------------ONLINE MODE METHODS------------------------------//
	public void waitForOpponent()
	{
		oppoThinking.setLocationRelativeTo(parent.getMoveHistoryPanel());
		blockMouseSelection = true;
		oppoThinking.setVisible(true);
	}
	
	public void oppoMakeMove(String move)
	{
		oppoThinking.setVisible(false);
		sourceSquare = MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(move.substring(0,2)));
		destinationSquare = MainFrame.board.getSquare(BoardCheck.convertChessCoordinateToInt(move.substring(2)));
		makeAMove(sourceSquare.getSquareCoordinate());
		updateBoardPanel(BoardPanel.this);
	}
	
	public void oppoPromote(int current, int des, String promoteToChar)
	{
		this.promoteToChar = promoteToChar;
		oppoThinking.setVisible(false);
		sourceSquare = MainFrame.board.getSquare(current);
		destinationSquare = MainFrame.board.getSquare(des);
		makeAMove(sourceSquare.getSquareCoordinate());
		updateBoardPanel(BoardPanel.this);
	}
	
	public void lostConnection()
	{
		oppoThinking.setVisible(false);
		blockMouseSelection = false;
	}
	
	//------------------END ONLINE MODE METHODS------------------------------//
}

