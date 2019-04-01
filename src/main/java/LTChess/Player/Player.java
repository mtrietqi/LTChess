package LTChess.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import LTChess.ChessBoard.*;
import LTChess.GUI.BoardPanel;
import LTChess.GUI.MainFrame;
import LTChess.Group.Group;
import LTChess.Pieces.*;
import LTChess.Pieces.Piece.PieceType;

public abstract class Player implements java.io.Serializable
{
	protected ChessBoard board;
	protected King king;
	protected Collection<Move> legalMoves;
	private boolean isInCheck;
	
	public Player(ChessBoard board, Collection<Move> legalMoves, Collection<Move> opponentMoves)
	{
		this.board = board;
		this.king = findKing();
		this.legalMoves = new ArrayList<Move>();
		
		for (Move move:legalMoves)
		{
			this.legalMoves.add(move);
		}
		for (Move move:calculateKingCastle(legalMoves, opponentMoves))
		{
			this.legalMoves.add(move);
		}
		this.isInCheck = !Player.calculateAttackOnSquare(this.king.getPiecePosition(), opponentMoves).isEmpty(); 
	}

	public King findKing() {
		for (Piece piece:getActivePieces())
		{
			if (piece.getPieceType().isKing())
			{
				return (King) piece;
			}
		}
//		throw new RuntimeException("The king doesn't exist on the board, something's wrong there!");
		return new King(-1,MainFrame.board.getCurrentPlayer().getAlliance());
	};
	
	public static Collection<Move> calculateAttackOnSquare(int piecePosition, Collection<Move> opponentMoves) {
		List<Move> attackMove = new ArrayList<>();
		for (Move opponentMove:opponentMoves)
		{
			if (piecePosition == opponentMove.getDestinationCoordinate())
			{
				attackMove.add(opponentMove);
			}
		}
		return attackMove;
	}
	
	public boolean isMoveLegal(Move move)
	{
		return this.legalMoves.contains(move);
	}
	
	public boolean isInCheck() //Bị chiếu 
	{
		return this.isInCheck;
	}
	
	public boolean isInCheckMate() //Bị chiếu bí
	{
		return this.isInCheck && !hasEscapeMove();
	}
	
	public boolean isInStaleMate() //Hết nước
	{
		return !this.isInCheck && !hasEscapeMove();
	}
	
	private boolean hasEscapeMove() //That method means if the king has any way to escape
	{
		for (Move move:legalMoves)
		{
			MoveTransition transition = executeMove(move);
			if(transition.getMoveResult().isDone())
			{
				return true;
			}
		}
		return false;
	}
	
	public King getKing()
	{
		return king;
	}
	
	public Collection<Move> getLegalMoves()
	{
		return legalMoves;
	}
	
	public MoveTransition executeMove(Move move)
	{
		if (!isMoveLegal(move))
		{
			return new MoveTransition(this.board, move,MoveResult.ILLEGAL); //Return on the same board because the move is invalid
		}
		ChessBoard newBoard = move.execute();
		Collection<Move> kingAttacks = Player.calculateAttackOnSquare(newBoard.getCurrentPlayer().getOpponent().getKing().getPiecePosition(), 
				newBoard.getCurrentPlayer().getLegalMoves()); //Calculate the attacks on the king from the current player to his opponent king
		if (!kingAttacks.isEmpty()) //Current player puts his opponent in check
		{
			return new MoveTransition(board, move, MoveResult.IN_CHECK);
		}
		return new MoveTransition(newBoard, move, MoveResult.DONE);
	}
	
	public abstract Collection<Piece> getActivePieces();
	public abstract Group getAlliance();
	public abstract Player getOpponent();
	public abstract Collection<Move> calculateKingCastle(Collection<Move> playerLegalMoves, Collection<Move> oppoLegalMoves);
	public abstract int getNumberofPiece(PieceType pieceType);
}
