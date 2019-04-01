package LTChess.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.BoardCheck;
import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Square;
import LTChess.ChessBoard.Move.AttackMove;
import LTChess.ChessBoard.Move.NormalMove;
import LTChess.Group.Group;
import LTChess.Pieces.Piece.PieceType;

public class King extends Piece implements java.io.Serializable
{
	private final static int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};
	public King(int piecePosition, Group pieceAlliance) {
		super(PieceType.KING,piecePosition, pieceAlliance, true);
		// TODO Auto-generated constructor stub
	}
	
	public King(int piecePosition, Group pieceAlliance, boolean isFirstMove)
	{
		super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateValidMove(ChessBoard board) {
		List<Move> legalMoves = new ArrayList<>();
		
		for (int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE)
		{
			int candidateDestionationCoordinate = this.piecePosition + currentCandidateOffset;
			
			if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
					|| isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
			{
				continue;
			}
			if (BoardCheck.isValidTileCoordinate(candidateDestionationCoordinate))
			{
				Square candidateDestionationTile = board.getSquare(candidateDestionationCoordinate);
				if (!(candidateDestionationTile.isSquareOccupied()))
				{
					legalMoves.add(new NormalMove(this,board,candidateDestionationCoordinate));
				}
				else //It is NOT a valid move
				{
					Piece pieceAtDestionation = candidateDestionationTile.getPiece();
					Group pieceAlliance = pieceAtDestionation.getPieceGroup();
					
					if (this.pieceAlliance != pieceAlliance) //Is it an enemy piece?
					{
						legalMoves.add(new AttackMove(this,board,candidateDestionationCoordinate, pieceAtDestionation));
					}
				}
				
			}
		}
		return legalMoves;
	}
	
	@Override
	public String toString()
	{
		return PieceType.KING.toString();
	}
	
	//These methods are used to check whether the King is in the first, second, seventh, eighth 
	// column (which might make the rule break down)
	private boolean isFirstColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1
				|| candidateOffset == 7);
	}
	
	private boolean isEighthColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1
				|| candidateOffset == 9);
	}

	@Override
	public King movedPiece(Move move) {
		// TODO Auto-generated method stub
		return new King(move.getDestinationCoordinate(), move.getMovePiece().getPieceGroup(),false);
	}
	

}
