package LTChess.Pieces;
/*
 * This class represents the Bishop on the chess board 
 */

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

//5:44
public class Bishop extends Piece implements java.io.Serializable
{
	private static int[] CANDIDATE_MOVE_COORDINATE ={-9,-7,7,9};
	
	public Bishop(int piecePosition, Group pieceAlliance) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance, true);
		// TODO Auto-generated constructor stub
	}
	
	public Bishop(int piecePosition, Group pieceAlliance, boolean isFirstMove)
	{
		super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateValidMove(ChessBoard board) {
		// TODO Auto-generated method stub
		List<Move> legalMoves = new ArrayList<>();
		for (int candidateCoordinateOffset:CANDIDATE_MOVE_COORDINATE)
		{
			int candidateDestionationCoordinate = this.piecePosition;
			while(BoardCheck.isValidTileCoordinate(candidateDestionationCoordinate))
			{
				if(isFirstColumnExclusion(candidateDestionationCoordinate, candidateCoordinateOffset)
						|| isEighthColumnExclusion(candidateDestionationCoordinate, candidateCoordinateOffset))
				{
					break;
				}
				
				candidateDestionationCoordinate += candidateCoordinateOffset;
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
						break;
					}
				}
			}
		}
		return legalMoves;
	}
	
	@Override
	public String toString()
	{
		return PieceType.BISHOP.toString();
	}
	
	//These methods are used to check whether the Bishop is in the first and eighth 
	// column (which might make the rule break down)
	private boolean isFirstColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
	}
	
	private boolean isEighthColumnExclusion(int currentPosition, int candidateOffset)
	{
		return BoardCheck.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 ||candidateOffset == 1 || candidateOffset == 9);
	}

	@Override
	public Piece movedPiece(Move move) {
		return new Bishop(move.getDestinationCoordinate(), move.getMovePiece().getPieceGroup(), false);
	}
	
	

}
