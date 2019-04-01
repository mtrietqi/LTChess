package LTChess.ChessBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import LTChess.ChessBoard.ChessBoard.Builder;
import LTChess.GUI.MainFrame;
import LTChess.GUI.UndoStack2;
import LTChess.Group.Group;
import LTChess.Pieces.*;
import LTChess.Pieces.Piece.PieceType;
import LTChess.Player.*;
import LTChess.UCI.UndoStack;

public class ChessBoard implements java.io.Serializable
{
	private List<Square> chessBoard;
	private Collection<Piece> whitePieces;
	private Collection<Piece> blackPieces;
	private Pawn enPassantPawn;
	private static Group firstMover;
	private WhitePlayer whitePlayer;
	private BlackPlayer blackPlayer;
	private Player currentPlayer;
	public static boolean UCIMode = false;
	public static boolean onlineMode = false; 
	public static boolean customBoardMode = false;
	public static UndoStack2 undoStack = new UndoStack2();
	public static boolean whiteCanCastle;
	public static boolean blackCanCastle;
	
	public ChessBoard(Builder builder)
	{
		this.chessBoard= new ArrayList<>();
		this.chessBoard = createChessboard(builder);
		whitePieces = new ArrayList<>();
		blackPieces = new ArrayList<>();
		
		this.whitePieces = ImmutableList.copyOf(calculateAlivePieces(this.chessBoard, Group.WHITE));
		this.blackPieces = ImmutableList.copyOf(calculateAlivePieces(this.chessBoard, Group.BLACK));
		this.enPassantPawn = builder.enPassantPawn;
		Collection<Move> whiteLegalMove = calculateValidMove(this.whitePieces);
		Collection<Move> blackLegalMove = calculateValidMove(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this, whiteLegalMove, blackLegalMove);
		this.blackPlayer = new BlackPlayer(this, blackLegalMove, whiteLegalMove);
		this.currentPlayer = Builder.nextMover.chooseaPlayer(whitePlayer,blackPlayer);
	}
	
	
	public ChessBoard(List<Square> chessBoard, Collection<Piece> whitePieces, Collection<Piece> blackPieces,
			Pawn enPassantPawn, WhitePlayer whitePlayer, BlackPlayer blackPlayer, Player currentPlayer) {
		super();
		this.chessBoard = chessBoard;
		this.whitePieces = whitePieces;
		this.blackPieces = blackPieces;
		this.enPassantPawn = enPassantPawn;
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.currentPlayer = currentPlayer;
	}



	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}


	//This method is used to calculate the legal moves for pieces which are still alive on the board
	private Collection<Move> calculateValidMove(Collection<Piece> pieces) {
		List<Move> legalMove = new ArrayList<Move>();
		for (Piece piece:pieces)
		{
			legalMove.addAll(piece.calculateValidMove(this));
		}
		return legalMove;
	}

	//This method is used to calculate pieces which are still alive on the board
	private Collection<Piece> calculateAlivePieces(List<Square> chessBoard, Group alliance) {
		// TODO Auto-generated method stub
		List<Piece> activePieces = new ArrayList<>();
		for (Square tile:chessBoard)
		{
			if (tile.isSquareOccupied())
			{
				Piece piece = tile.getPiece();
				if (piece.getPieceGroup() == alliance)
				{
					activePieces.add(piece);
				}
			}
		}
		return activePieces;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 64; i++)
		{
			String tileText = this.chessBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if ((i+1) % 8 ==0)
			{
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	//This method is used to create chess board from builder
	private List<Square> createChessboard(Builder builder) {
		ArrayList<Square> squares = new ArrayList<Square>();
		for (int i = 0; i < 64; i++)
		{
			squares.add(i, Square.createSquare(i,builder.getBoardSetting().get(i)));
		}
		return squares;
	}
	
	//This method is used to create an empty chess board
	public static ChessBoard createEmptyChessboard(Group firstMoverGroup) {
		Builder builder = new Builder();
		customBoardMode = true;
        builder.locatePiece(new King(4, Group.BLACK));
        builder.locatePiece(new King(60, Group.WHITE));
        builder.setMover(firstMoverGroup);
        firstMover = firstMoverGroup;
        //build the board
        return builder.build();
	}
	
	//This method is used to get tile at a specific coordinate
	public Square getSquare(int tileCoordinate)
	{
		return chessBoard.get(tileCoordinate);
	}
	
	//Create a standard board
	public static ChessBoard createStandardBoard(Group firstMoverAlliance)
	{
		Builder builder = new Builder();
       //Black pieces
        builder.locatePiece(new Rook(0, Group.BLACK));
        builder.locatePiece(new Knight(1, Group.BLACK));
        builder.locatePiece(new Bishop(2, Group.BLACK));
        builder.locatePiece(new Queen(3, Group.BLACK));
        builder.locatePiece(new King(4, Group.BLACK));
        builder.locatePiece(new Bishop(5, Group.BLACK));
        builder.locatePiece(new Knight(6, Group.BLACK));
        builder.locatePiece(new Rook(7, Group.BLACK));
        builder.locatePiece(new Pawn(8, Group.BLACK));
        builder.locatePiece(new Pawn(9, Group.BLACK));
        builder.locatePiece(new Pawn(10, Group.BLACK));
        builder.locatePiece(new Pawn(11, Group.BLACK));
        builder.locatePiece(new Pawn(12, Group.BLACK));
        builder.locatePiece(new Pawn(13, Group.BLACK));
        builder.locatePiece(new Pawn(14, Group.BLACK));
        builder.locatePiece(new Pawn(15, Group.BLACK));
       // White pieces
        builder.locatePiece(new Pawn(48, Group.WHITE));
        builder.locatePiece(new Pawn(49, Group.WHITE));
        builder.locatePiece(new Pawn(50, Group.WHITE));
        builder.locatePiece(new Pawn(51, Group.WHITE));
        builder.locatePiece(new Pawn(52, Group.WHITE));
        builder.locatePiece(new Pawn(53, Group.WHITE));
        builder.locatePiece(new Pawn(54, Group.WHITE));
        builder.locatePiece(new Pawn(55, Group.WHITE));
        builder.locatePiece(new Rook(56, Group.WHITE));
        builder.locatePiece(new Knight(57, Group.WHITE));
        builder.locatePiece(new Bishop(58, Group.WHITE));
        builder.locatePiece(new Queen(59, Group.WHITE));
        builder.locatePiece(new King(60, Group.WHITE));
        builder.locatePiece(new Bishop(61, Group.WHITE));
        builder.locatePiece(new Knight(62, Group.WHITE));
        builder.locatePiece(new Rook(63, Group.WHITE));
//
//        //-----DRAW GAME 1
//		builder.locatePiece(new King(7, Group.BLACK));
//		builder.locatePiece(new King(18, Group.WHITE));
//		builder.locatePiece(new Pawn(9, Group.BLACK));
//		
//		//-----DRAW GAME 2
//		builder.locatePiece(new King(7, Group.BLACK));
//		builder.locatePiece(new King(18, Group.WHITE));
//		builder.locatePiece(new Pawn(9, Group.BLACK));
//		builder.locatePiece(new Knight(32, Group.BLACK));
//		
//		//-----DRAW GAME 3
//		builder.locatePiece(new King(7, Group.BLACK));
//		builder.locatePiece(new King(18, Group.WHITE));
//		builder.locatePiece(new Pawn(9, Group.BLACK));
//		builder.locatePiece(new Bishop(32, Group.BLACK));
//		
//		//-----DRAW GAME 4
//		builder.locatePiece(new King(7, Group.BLACK));
//		builder.locatePiece(new King(18, Group.WHITE));
//		builder.locatePiece(new Knight(50, Group.WHITE));
//		builder.locatePiece(new Pawn(9, Group.BLACK));
//		builder.locatePiece(new Bishop(32, Group.BLACK));
//		
//		//-----PROMOTION
//		builder.locatePiece(new King(5, Group.BLACK));
//		builder.locatePiece(new King(60, Group.WHITE));
//		builder.locatePiece(new Pawn(9, Group.WHITE));
//		builder.locatePiece(new Bishop(45, Group.BLACK));
		
//		//-----CHECKMATE
//		builder.locatePiece(new King(3, Group.BLACK));
//		builder.locatePiece(new Queen(32, Group.WHITE));
//		builder.locatePiece(new Rook(8, Group.WHITE));
//		builder.locatePiece(new King(35, Group.WHITE));
//		builder.locatePiece(new Pawn(11, Group.BLACK));
		
//		//-----STALEMATE
//		builder.locatePiece(new King(2, Group.BLACK));
//		builder.locatePiece(new Pawn(19, Group.BLACK));
//		builder.locatePiece(new King(16, Group.WHITE));
//		builder.locatePiece(new Queen(59, Group.WHITE));
		
        builder.setMover(firstMoverAlliance);
        firstMover = firstMoverAlliance;
        whiteCanCastle = true;
        blackCanCastle = true;
        //build the board
        return builder.build();
	}
	
	public static ChessBoard createStandardBoardForTest(Group firstMoverAlliance)
	{
		Builder builder = new Builder();
       //Black pieces
        builder.locatePiece(new Rook(53, Group.BLACK));
        builder.locatePiece(new Knight(56, Group.BLACK));
//        builder.locatePiece(new Bishop(2, Group.BLACK));
        builder.locatePiece(new Queen(49, Group.BLACK));
        builder.locatePiece(new King(4, Group.BLACK));
//        builder.locatePiece(new Bishop(5, Group.BLACK));
//        builder.locatePiece(new Knight(6, Group.BLACK));
//        builder.locatePiece(new Rook(7, Group.BLACK));
//        builder.locatePiece(new Pawn(8, Group.BLACK));
//        builder.locatePiece(new Pawn(9, Group.BLACK));
//        builder.locatePiece(new Pawn(10, Group.BLACK));
//        builder.locatePiece(new Pawn(11, Group.BLACK));
//        builder.locatePiece(new Pawn(12, Group.BLACK));
//        builder.locatePiece(new Pawn(13, Group.BLACK));
//        builder.locatePiece(new Pawn(14, Group.BLACK));
//        builder.locatePiece(new Pawn(15, Group.BLACK));
       // White pieces
//        builder.locatePiece(new Pawn(48, Group.WHITE));
//        builder.locatePiece(new Pawn(49, Group.WHITE));
//        builder.locatePiece(new Pawn(50, Group.WHITE));
//        builder.locatePiece(new Pawn(51, Group.WHITE));
//        builder.locatePiece(new Pawn(52, Group.WHITE));
//        builder.locatePiece(new Pawn(53, Group.WHITE));
//        builder.locatePiece(new Pawn(54, Group.WHITE));
//        builder.locatePiece(new Pawn(55, Group.WHITE));
//        builder.locatePiece(new Rook(56, Group.WHITE));
//        builder.locatePiece(new Knight(57, Group.WHITE));
//        builder.locatePiece(new Bishop(58, Group.WHITE));
//        builder.locatePiece(new Queen(59, Group.WHITE));
        builder.locatePiece(new King(61, Group.WHITE));
//        builder.locatePiece(new Bishop(61, Group.WHITE));
//        builder.locatePiece(new Knight(62, Group.WHITE));
//        builder.locatePiece(new Rook(63, Group.WHITE));

        builder.setMover(firstMoverAlliance);
        firstMover = firstMoverAlliance;
        whiteCanCastle = true;
        blackCanCastle = true;
        //build the board
        return builder.build();
	}
	
	public Collection<Piece> getBlackPieces()
	{
		return blackPieces;
	}
	
	public Collection<Piece> getWhitePieces()
	{
		return whitePieces;
	}
	
	public Player getBlackPlayer()
	{
		return this.blackPlayer;
	}
	
	public Player getWhitePlayer()
	{
		return this.whitePlayer;
	}
	
	
	//Builder design pattern
	public static class Builder
	{
		public static Map<Integer,Piece> builderChessBoard; //The board
		public static Group nextMover;
		Pawn enPassantPawn;
		
		public Builder()
		{
			this.builderChessBoard = new HashMap<>();
		}
		
		// Set piece at a specific coordinate
		public Builder locatePiece(Piece piece)
		{
			builderChessBoard.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		//Set the next mover 
		public Builder setMover(Group nextMover)
		{
			this.nextMover = nextMover;
			return this;
		}
		
		public Map<Integer,Piece> getBoardSetting()
		{
			return this.builderChessBoard;
		}
		
		public ChessBoard build()
		{
			return new ChessBoard(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
		}
	}
	
	public Move[] getAllValidMoves() {

		
		List<Move> allLegalMoves = new ArrayList<Move>();
		for (Move move:this.whitePlayer.getLegalMoves())
		{
			allLegalMoves.add(move);
		}
		
		for (Move move:this.blackPlayer.getLegalMoves())
		{
			allLegalMoves.add(move);
		}
		Move[] moveCopy = new Move[allLegalMoves.size()];
		for (int i = 0; i < allLegalMoves.size(); i++)
		{
			moveCopy[i] = allLegalMoves.get(i);
		}
		return moveCopy;
	}

	public Pawn getEnpassantPawn() {
		return this.enPassantPawn;
	}

	public Group getFirstMoverAlliance()
	{
		return this.firstMover;
	}
	
	public boolean isDrawGame()
	{
		int numOfWPawn = this.getWhitePlayer().getNumberofPiece(PieceType.PAWN); 
		int numOfBPawn = this.getBlackPlayer().getNumberofPiece(PieceType.PAWN); 
		int numOfWRook = this.getWhitePlayer().getNumberofPiece(PieceType.ROOK); 
		int numOfBRook = this.getBlackPlayer().getNumberofPiece(PieceType.ROOK);
		int numOfWBishop = this.getWhitePlayer().getNumberofPiece(PieceType.BISHOP); 
		int numOfBBishop = this.getBlackPlayer().getNumberofPiece(PieceType.BISHOP); 
		int numOfWKnight = this.getWhitePlayer().getNumberofPiece(PieceType.KNIGHT); 
		int numOfBKnight = this.getBlackPlayer().getNumberofPiece(PieceType.KNIGHT);
		int numOfWQueen = this.getWhitePlayer().getNumberofPiece(PieceType.QUEEN);
		int numOfBQueen = this.getBlackPlayer().getNumberofPiece(PieceType.QUEEN);
		
		
		if (numOfWPawn != 0 || numOfBPawn != 0)
			return false;
		
		if (numOfWRook == 0 && numOfBRook == 0 && numOfWBishop == 0 && numOfBBishop == 0 && numOfWKnight == 0 && numOfBKnight == 0
				&& numOfWQueen == 0 && numOfBQueen == 0) //King VS King
		{
			return true;
		}
		else if (numOfWRook == 0 && numOfBRook == 0 && numOfWQueen == 0  && numOfBQueen == 0)
		{
			//Black has king only
			if (numOfBKnight == 0 && numOfBBishop == 0)
			{
				//White has 1 knight or bishop
				if (numOfWBishop == 1 || numOfWKnight == 1)
				{
					return true;
				}
			}
			//White has king only
			else if (numOfWKnight == 0 && numOfWBishop == 0) {
				// Black has 1 knight or bishop
				if (numOfBBishop == 1 || numOfBKnight == 1) {
					return true;
				}
			}
			//Black has king and 1 bishop or 1 knight
			else if (numOfBKnight == 1 || numOfBBishop ==1)
			{
				//White also has 1 bishop or 1 knight
				if (numOfWKnight == 1 || numOfWBishop == 1)
				{
					return true;
				}
			}
			//White has king and 1 bishop or 1 knight
			else if (numOfWKnight == 1 || numOfWBishop ==1)
			{
				//Black also has 1 bishop or 1 knight
				if (numOfBKnight == 1 || numOfBBishop == 1)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//This method is used for custom board mode
	public static ChessBoard updateAPiecePosition(Piece movedPiece, int newPosition)
	{
		Builder builder = new Builder();
		for (Piece piece : MainFrame.board.getCurrentPlayer().getActivePieces()) {
			if (!piece.equals(movedPiece))
			{
				builder.locatePiece(piece);
			}
		}

		for (Piece piece : MainFrame.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			if (!piece.equals(movedPiece))
			{
				builder.locatePiece(piece);
			}
		}
		
		movedPiece.setPosition(newPosition);
		builder.locatePiece(movedPiece);
		return builder.build();
	}
	
	public static ChessBoard removeAPiece(Piece removePiece)
	{
		Builder builder = new Builder();
		for (Piece piece : MainFrame.board.getCurrentPlayer().getActivePieces()) {
			if (!piece.equals(removePiece))
			{
				builder.locatePiece(piece);
			}
		}

		for (Piece piece : MainFrame.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			if (!piece.equals(removePiece))
			{
				builder.locatePiece(piece);
			}
		}
		return builder.build();
	}
	
	public Player getPlayer(Group group)
	{
		if (group == Group.WHITE)
		{
			return whitePlayer;
		}
		else
		{
			return blackPlayer;
		}
	}
	
	public ChessBoard copy()
	{
		Collection<Piece> whitePiecesCopy = new ArrayList<>();
		List<Square>chess= new ArrayList<>();
		for (Square square: chessBoard)
		{
			chess.add(square);
		}
		
		for (Piece piece:whitePieces)
		{
			whitePiecesCopy.add(piece);
		}
		
		Collection<Piece> blackPiecesCopy = new ArrayList<>();
		for (Piece piece:blackPieces)
		{
			blackPiecesCopy.add(piece);
		}
		ChessBoard copyChessBoard = new ChessBoard(chess, 
				whitePiecesCopy, blackPiecesCopy, this.enPassantPawn, this.whitePlayer, this.blackPlayer, this.currentPlayer);
		return copyChessBoard;
	}
	
}
