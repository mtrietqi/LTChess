package LTChess.UCI;

import java.util.List;

import LTChess.ChessBoard.ChessBoard;
import LTChess.GUI.DefeatedPiecePanel;
import LTChess.GUI.MoveHistoryPanel;
import LTChess.Pieces.Piece;

public class UndoData 
{
	private ChessBoard board;
	private List<Piece> defeatedWhitePieces;
	private List<Piece> defeatedBlackPieces;
	private int whiteTime;
	private int blackTime;
	private String[][] moveHistory;
	
	public UndoData(ChessBoard board, List<Piece> defeatedWhitePieces,List<Piece> defeatedBlackPieces, int whiteTime, int blackTime,
			String[][] moveHistory) {
		super();
		this.board = board;
		this.whiteTime = whiteTime;
		this.blackTime = blackTime;
		this.moveHistory = moveHistory;
		this.defeatedWhitePieces = defeatedWhitePieces;
		this.defeatedBlackPieces = defeatedBlackPieces;
	}

	public ChessBoard getBoard() {
		return board;
	}

	public List<Piece> getDefeatedWhitePieces() {
		return defeatedWhitePieces;
	}

	public List<Piece> getDefeatedBlackPieces() {
		return defeatedBlackPieces;
	}

	public int getWhiteTime() {
		return whiteTime;
	}

	public int getBlackTime() {
		return blackTime;
	}

	public String[][] getMoveHistory() {
		return moveHistory;
	}
	
	public String toString()
	{
		String str = "";
		for (int j = 0; j < board.getWhitePieces().size(); j++)
		{
			Piece current = (Piece)board.getWhitePieces().toArray()[j];
			str += "Your "+current+" is in pos " +current.getPiecePosition()+"\n";
		}
		return str;
	}
	
}
