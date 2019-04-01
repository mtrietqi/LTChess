package LTChess.Network;

import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Move.PawnPromotionAttackMove;
import LTChess.ChessBoard.Move.PawnPromotionNormalMove;
import LTChess.GUI.MainFrame;
import LTChess.Pieces.Bishop;
import LTChess.Pieces.Knight;
import LTChess.Pieces.Piece;
import LTChess.Pieces.Queen;
import LTChess.Pieces.Rook;

public class NetworkPromotion
{
	private String promoteToChar = "";
	private Move move;
	public NetworkPromotion(String promotoChar, Move move)
	{
		this.promoteToChar = promotoChar;
		this.move = move;
	}
	
	public Move getNetworkPromotionMove()
	{
		Piece promoteTo;
		if (move instanceof PawnPromotionNormalMove)
		{
			PawnPromotionNormalMove normalPromotion = (PawnPromotionNormalMove) move; 
			if (promoteToChar.equalsIgnoreCase("q"))
			{
				promoteTo = new Queen(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				normalPromotion.setPromoteTo(promoteTo);
				return normalPromotion;
			}
			else if (promoteToChar.equalsIgnoreCase("n"))
			{
				promoteTo = new Knight(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				normalPromotion.setPromoteTo(promoteTo);
				return normalPromotion;
			}
			else if (promoteToChar.equalsIgnoreCase("r"))
			{
				promoteTo = new Rook(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				normalPromotion.setPromoteTo(promoteTo);
				return normalPromotion;
			}
			else if (promoteToChar.equalsIgnoreCase("b"))
			{
				promoteTo = new Bishop(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				normalPromotion.setPromoteTo(promoteTo);
				return normalPromotion;
			}
		}
		else
		{
			PawnPromotionAttackMove attackPromotion = (PawnPromotionAttackMove) move; 
			if (promoteToChar.equalsIgnoreCase("q"))
			{
				promoteTo = new Queen(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				attackPromotion.setPromoteTo(promoteTo);
				return attackPromotion;
			}
			else if (promoteToChar.equalsIgnoreCase("k"))
			{
				promoteTo = new Knight(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				attackPromotion.setPromoteTo(promoteTo);
				return attackPromotion;
			}
			else if (promoteToChar.equalsIgnoreCase("b"))
			{
				promoteTo = new Bishop(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				attackPromotion.setPromoteTo(promoteTo);
				return attackPromotion;
			}
			else if (promoteToChar.equalsIgnoreCase("r"))
			{
				promoteTo = new Rook(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
				attackPromotion.setPromoteTo(promoteTo);
				return attackPromotion;
			}
		}
		return null;
	}
}
