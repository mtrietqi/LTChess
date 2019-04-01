package LTChess.Pieces;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import LTChess.ChessBoard.*;
import LTChess.ChessBoard.Move.AttackMove;
import LTChess.ChessBoard.Move.NormalMove;
import LTChess.Group.Group;
/*
 * This class represents the Knight on the chess board 
 */
import LTChess.Pieces.Piece.PieceType;

public class Knight extends Piece implements java.io.Serializable
{
	private final int[] CANDIDATE_MOVE_COORDINATE = {-17,-15,-10,-6,6,10,15,17}; //Possible or legal moves 
	
	public Knight(int piecePosition, Group pieceAlliance) {
		super(PieceType.KNIGHT,piecePosition, pieceAlliance, true);
	}
	
	public Knight(int piecePosition, Group pieceAlliance, boolean isFirstMove)
	{
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateValidMove(ChessBoard board) 
	{
		List<Move> legalMoves = new ArrayList<Move>(); //List of legal moves
		
		for (int currrentCandiateOffset:CANDIDATE_MOVE_COORDINATE)
		{
			int candidateDestionationCoordinate = this.piecePosition + currrentCandiateOffset;  
			if (BoardCheck.isValidTileCoordinate(candidateDestionationCoordinate)) //It is a valid move (minus number, out of 63,...)
			{
				
				if (isFirstColumnExclusion(this.piecePosition, currrentCandiateOffset) || isSecondColumnExclusion(this.piecePosition, currrentCandiateOffset)
						|| isSeventhColumnExclusion(this.piecePosition, currrentCandiateOffset)|| isEighthColumnExclusion(this.piecePosition, currrentCandiateOffset))
				{
					continue;
				}
				
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
		return PieceType.KNIGHT.toString();
	}
	
	//These methods are used to check whether the Knight is in the first, second, seventh, eighth 
	// column (which might make the rule break down)
	private boolean isFirstColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10
				|| candidateOffset == 6 || candidateOffset == 15);
	}
	
	private boolean isSecondColumnExclusion(int currentPosition, int candidateOffset)
	{
		
		return BoardCheck.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
	}
	
	private boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == -6)|| (candidateOffset == 10));
	}
	
	private boolean isEighthColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6
				||candidateOffset == 10 || candidateOffset == 17);
	}

	@Override
	public Knight movedPiece(Move move) {
		return new Knight(move.getDestinationCoordinate(), move.getMovePiece().getPieceGroup(), false);
	}
	
	
}
