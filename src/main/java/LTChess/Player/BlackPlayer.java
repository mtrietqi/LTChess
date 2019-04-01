package LTChess.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Square;
import LTChess.GUI.MainFrame;
import LTChess.Group.Group;
import LTChess.Pieces.Piece;
import LTChess.Pieces.Piece.PieceType;
import LTChess.Pieces.Rook;

public class BlackPlayer extends Player implements java.io.Serializable
{
	public BlackPlayer(ChessBoard board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
		super(board, legalMoves, opponentMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Group getAlliance() {
		// TODO Auto-generated method stub
		return Group.BLACK;
	}

	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.getWhitePlayer();
	}

	@Override
	public Collection<Move> calculateKingCastle(Collection<Move> playerLegalMoves, Collection<Move> oppoLegalMoves) {
		List<Move> kingCastleMoves = new ArrayList<Move>();
		if (this.king.isFirstMove() && !(this.isInCheck()))
		{
			//King side castle
			if (!(this.board.getSquare(5).isSquareOccupied()) && !(this.board.getSquare(6).isSquareOccupied())) //2 tiles need to be not occupied in king side castle
			{
				Square rookTile = this.board.getSquare(7);
				if (rookTile.isSquareOccupied() && rookTile.getPiece().isFirstMove()) // We have a piece there and it is its first move
				{
					if (Player.calculateAttackOnSquare(5, oppoLegalMoves).isEmpty()
							&& Player.calculateAttackOnSquare(6, oppoLegalMoves).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook())
					{

						kingCastleMoves.add(new Move.KingSideCastleMove(king, board,6 , (Rook) rookTile.getPiece(),7, 5));
					}
				}
			}
			
			//Queen side castle
			if (!(this.board.getSquare(1).isSquareOccupied()) && !(this.board.getSquare(2).isSquareOccupied()) && 
					!(this.board.getSquare(3).isSquareOccupied())) //3 tiles need to be not occupied in queen side castle
			{
				Square rookTile2 = this.board.getSquare(0);
				if (rookTile2.isSquareOccupied() && rookTile2.getPiece().isFirstMove()) // The rook is still there and it is its first move
				{
					if (Player.calculateAttackOnSquare(1, oppoLegalMoves).isEmpty()
							&& Player.calculateAttackOnSquare(2, oppoLegalMoves).isEmpty()
							&& Player.calculateAttackOnSquare(3, oppoLegalMoves).isEmpty()
							&& rookTile2.getPiece().getPieceType().isRook())
					{
						kingCastleMoves.add(new Move.QueenSideCastleMove(king, board,2,(Rook) rookTile2.getPiece(),0, 3));
					}
				}
			}
		}
		return kingCastleMoves;
	}

	@Override
	public int getNumberofPiece(PieceType pieceType) {
		int count = 0;
		for (Piece piece: getActivePieces())
		{
			if (piece.getPieceType() == pieceType)
				count++;
		}
		return count;
	}



}
