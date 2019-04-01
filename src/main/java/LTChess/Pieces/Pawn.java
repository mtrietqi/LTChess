package LTChess.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.BoardCheck;
import LTChess.ChessBoard.Move;
import LTChess.Group.Group;
import LTChess.Pieces.Piece.PieceType;
//9:40 #37
public class Pawn extends Piece implements java.io.Serializable
{
	private final static int[] CANDIDATE_MOVE_COORDINATE = {8,16,7,9};
	public Pawn(int piecePosition, Group pieceAlliance) {
		super(PieceType.PAWN,piecePosition, pieceAlliance, true);
		// TODO Auto-generated constructor stub
	}
	
	public Pawn(int piecePosition, Group pieceAlliance, boolean isFirstMove)
	{
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateValidMove(ChessBoard board) {
		List<Move> legalMoves = new ArrayList<>();
		for (int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE)
		{
			int candidateDestinationCoordinate = this.piecePosition + this.getPieceGroup().getDirection()*currentCandidateOffset;
			if (!BoardCheck.isValidTileCoordinate(candidateDestinationCoordinate)) //If it is not a valid move
			{
				continue;
			}
			
			if (currentCandidateOffset ==8 && !board.getSquare(candidateDestinationCoordinate).isSquareOccupied()) //A normal forward move and the
				//destination is not occupied
			{
				//Deal with pawn promotion, if that pawn reached the eighth row (for black) and first row (for white)
				if ((this.getPieceGroup() == Group.WHITE && BoardCheck.FIRST_ROW[candidateDestinationCoordinate] == true)
						|| (this.getPieceGroup() == Group.BLACK && BoardCheck.EIGHTH_ROW[candidateDestinationCoordinate] == true)) 
				{
					legalMoves.add(new Move.PawnPromotionNormalMove(this, board, candidateDestinationCoordinate));
				}
				else
				{
					legalMoves.add(new Move.NormalMove(this, board, candidateDestinationCoordinate));
				}
			}
			else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardCheck.SECOND_ROW[this.piecePosition]&& this.pieceAlliance.isBlack()) ||
                     (BoardCheck.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite())))
			{
				// If the pawn CAN move 2 times (jump) at its first move, that variable WILL check if the destination of
				// the first move is occupied or not
				int behindCandidateDestionationCo = this.piecePosition + (this.getPieceGroup().getDirection()*8);
				if (!board.getSquare(candidateDestinationCoordinate).isSquareOccupied() &&
	                    !board.getSquare(behindCandidateDestionationCo).isSquareOccupied()) {
	                    legalMoves.add(new Move.PawnJump(this,board, candidateDestinationCoordinate));
				}
			}
			//Check if we are in offset 7 and we don't fall into 2 exceptions (in eighth column and first column)
			else if (currentCandidateOffset == 7 &&
                    !((BoardCheck.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardCheck.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))
			{
				if (board.getSquare(candidateDestinationCoordinate).isSquareOccupied())
				{
					Piece pieceatDestionation = board.getSquare(candidateDestinationCoordinate).getPiece();
					//If current piece alliance is different from the alliance of the piece on the destination => Can attack
					if (this.pieceAlliance != pieceatDestionation.getPieceGroup())
					{
						//Deal with pawn promotion, if that pawn reached the eighth row (for black) and first row (for white)
						if (((this.getPieceGroup() == Group.WHITE) && (BoardCheck.FIRST_ROW[candidateDestinationCoordinate] == true))
								|| ((this.getPieceGroup() == Group.BLACK) && (BoardCheck.EIGHTH_ROW[candidateDestinationCoordinate] == true))) 
						{
							legalMoves.add(new Move.PawnPromotionAttackMove(this, board, candidateDestinationCoordinate, pieceatDestionation));
						}
						else
						{
							legalMoves.add(new Move.PawnAttackMove(this, board, candidateDestinationCoordinate,pieceatDestionation));
						}
		
					}
				}
				else if (board.getEnpassantPawn() != null) //Can do enpassant 
				{
					if (board.getEnpassantPawn().getPiecePosition() == (this.getPiecePosition()+ this.pieceAlliance.getOppositeMoving()))
					{
						Piece pieceatDestionation = board.getEnpassantPawn();
						// Check if the alliance of the which has just jump is equal with the current player alliance or not
						if (this.pieceAlliance != pieceatDestionation.getPieceGroup()) 
						{
							legalMoves.add(new Move.PawnEnPassantAttackMove(this, board, candidateDestinationCoordinate, pieceatDestionation));
						}
					}
				}
			}
			else if (currentCandidateOffset == 9 &&
					!((BoardCheck.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardCheck.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))
			{
				if (board.getSquare(candidateDestinationCoordinate).isSquareOccupied())
				{
					Piece pieceatDestionation = board.getSquare(candidateDestinationCoordinate).getPiece();
					//If current piece alliance is different from the alliance of the piece on the destination tile
					if (this.pieceAlliance != pieceatDestionation.getPieceGroup())
					{
						if (((this.getPieceGroup() == Group.WHITE) && (BoardCheck.FIRST_ROW[candidateDestinationCoordinate] == true))
								|| ((this.getPieceGroup() == Group.BLACK) && (BoardCheck.EIGHTH_ROW[candidateDestinationCoordinate] == true))) 
						{
							legalMoves.add(new Move.PawnPromotionAttackMove(this, board, candidateDestinationCoordinate, pieceatDestionation));
						}
						else
						{
							legalMoves.add(new Move.PawnAttackMove(this, board, candidateDestinationCoordinate,pieceatDestionation));
						}
		
					}
				}
				else if (board.getEnpassantPawn() != null) //Can do enpassant 
				{
					if (board.getEnpassantPawn().getPiecePosition() == (this.getPiecePosition()+ this.pieceAlliance.getDirection()))
					{
						Piece pieceatDestionation = board.getEnpassantPawn();
						// Check if the alliance of the which has just jump is equal with the current player alliance or not
						if (this.pieceAlliance != pieceatDestionation.getPieceGroup()) 
						{
							legalMoves.add(new Move.PawnEnPassantAttackMove(this, board, candidateDestinationCoordinate, pieceatDestionation));
						}
					}
				}
			}
			
		}
		return legalMoves;
	}
	
	@Override
	public String toString()
	{
		return PieceType.PAWN.toString();
	}

	@Override
	public Pawn movedPiece(Move move) {
		// TODO Auto-generated method stub
		return new Pawn(move.getDestinationCoordinate(), move.getMovePiece().getPieceGroup(),false);
	}

}
