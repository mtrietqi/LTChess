import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.Test;
import LTChess.ChessBoard.BoardCheck;
import LTChess.ChessBoard.ChessBoard;
import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Move.NormalMove;
import LTChess.Group.Group;
import LTChess.Pieces.Knight;
import LTChess.Player.Player;

public class TestUnits {
	
	// Test if a coordinate is in the first column
	@Test
	public void testcase1() {
		// Check if coodinate 8 is in the first column
		boolean first = BoardCheck.startColumn(0)[8];
		assertEquals(true, first);

		// Check if coodinate 15 is in the first column
		boolean second = BoardCheck.startColumn(0)[18];
		assertEquals(false, second);
	}

	// Test conversation from an int to chess coordinate
	@Test
	public void testcase2() {
		// Convert 25 to chess coordinate
		String result1 = BoardCheck.convertToChessCoordinate(25);
		assertEquals("b5", result1);

		// Convert 45 to chess coordinate
		String result2 = BoardCheck.convertToChessCoordinate(40);
		assertEquals("a3", result2);
	}

	// Test if the opponent player is in check
	@Test
	public void testcase3() {
		ChessBoard newBoard = ChessBoard.createStandardBoardForTest(Group.BLACK);
//		System.out.println(newBoard);
		Collection<Move> kingAttacks = Player.calculateAttackOnSquare(
				newBoard.getCurrentPlayer().getOpponent().getKing().getPiecePosition(),
				newBoard.getCurrentPlayer().getLegalMoves());
		assertEquals(true, !kingAttacks.isEmpty());
	}
	
	// Test the valid move for the white Knight in the standard board
	@Test
	public void testcase4() {
		ChessBoard newBoard = ChessBoard.createStandardBoard(Group.WHITE);
		System.out.println(newBoard);
		Knight whiteKnight = new Knight(62,Group.WHITE);
		Collection<Move> moveCollection = whiteKnight.calculateValidMove(newBoard);
		assertEquals(true, moveCollection.size() == 2);
		
		boolean success = true;
		int order = 0;
		for (Move move:moveCollection)
		{
			if (order == 0)
			{
				if (move.getMovePiece() != whiteKnight || move.getDestinationCoordinate() != 45)
				{
					success = false;
//					System.out.println("here1");
//					System.out.println(move.getDestinationCoordinate());
				}
			}
			else if (order == 1)
			{
				if (move.getMovePiece() != whiteKnight || move.getDestinationCoordinate() != 47)
				{
					success = false;
//					System.out.println("here2");
				}
			}
			order++;
		}
		assertEquals(true, success);
	}
}
