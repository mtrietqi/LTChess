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

public class WhitePlayer extends Player implements java.io.Serializable
{

	public WhitePlayer(ChessBoard board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
		super(board, legalMoves, opponentMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return board.getWhitePieces();
	}

	@Override
	public Group getAlliance() {
		return Group.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.getBlackPlayer();
	}

	@Override
	public Collection<Move> calculateKingCastle(Collection<Move> playerLegalMoves, Collection<Move> oppoLegalMoves) {
		
		List<Move> kingCastleMoves = new ArrayList<Move>();
		if (this.king.isFirstMove() && !(this.isInCheck()))
		{
			//King side castle
			if (!(this.board.getSquare(61).isSquareOccupied()) && !(this.board.getSquare(62).isSquareOccupied())) //2 tiles need to be not occupied in king side castle
			{
				Square rookTile = this.board.getSquare(63);
				if (rookTile.isSquareOccupied() && rookTile.getPiece().isFirstMove()) // We have a piece there and it is its first move
				{
					if (Player.calculateAttackOnSquare(61, oppoLegalMoves).isEmpty()
							&& Player.calculateAttackOnSquare(62, oppoLegalMoves).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook())
					{
						kingCastleMoves.add(new Move.KingSideCastleMove(king, board, 62, (Rook) rookTile.getPiece(),63, 61));
					}
				}
			}
			
			//Queen side castle
			if (!(this.board.getSquare(59).isSquareOccupied()) && !(this.board.getSquare(58).isSquareOccupied()) && 
					!(this.board.getSquare(57).isSquareOccupied())) //2 tiles need to be not occupied in king side castle
			{
				Square rookTile2 = this.board.getSquare(56);
				if (rookTile2.isSquareOccupied() && rookTile2.getPiece().isFirstMove()) // The rook is still there and it is its first move
				{
					if (Player.calculateAttackOnSquare(57, oppoLegalMoves).isEmpty()
							&& Player.calculateAttackOnSquare(58, oppoLegalMoves).isEmpty()
							&& Player.calculateAttackOnSquare(59, oppoLegalMoves).isEmpty()
							&& rookTile2.getPiece().getPieceType().isRook())
					{
						kingCastleMoves.add(new Move.QueenSideCastleMove(king, board, 58,(Rook) rookTile2.getPiece(), 56, 59));
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
