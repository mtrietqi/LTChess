package LTChess.Player;

import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.Move;

public class MoveTransition implements java.io.Serializable
{
	private ChessBoard transitionBoard;
	private Move move;
	private MoveResult moveResult;
	
	public MoveTransition(ChessBoard transitionBoard, Move move, MoveResult moveResult)
	{
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveResult = moveResult;
	}

	public MoveResult getMoveResult() {
		return moveResult;
	}
	
	public ChessBoard getTransitionBoard()
	{
		return transitionBoard;
	}
}
