package LTChess.ChessBoard;
import LTChess.GUI.PromotionDialog;
import LTChess.Pieces.Pawn;
import LTChess.Pieces.Piece;
import LTChess.Pieces.Rook;


/*
 * This class demonstrates 2 kinds of moves (normal move and attack move)
 */


public abstract class Move implements java.io.Serializable
{
	protected Piece movedPiece; //The piece which is moved
	public ChessBoard board; //The ches board
	private int destinationCoordinate; //The destination of the move
	private boolean isFirstMove;
	public static final Move NULL_MOVE = new NullMove();
	
	public Move(Piece movedPiece, ChessBoard board, int destionationCo)
	{
		this.movedPiece = movedPiece;
		this.board = board;
		this.destinationCoordinate = destionationCo;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	private Move(ChessBoard board, int destionationCo)
	{
		this.board = board;
		this.destinationCoordinate = destionationCo;
		this.movedPiece = null;
		this.isFirstMove = false;
	}
	
	public int getCurrentCoordinate()
	{
		return this.movedPiece.getPiecePosition();
	}
	public int getDestinationCoordinate()
	{
		return destinationCoordinate;
	}
	
	public Piece getMovePiece()
	{
		return this.movedPiece;
	}
	
	public boolean isAttackMove()
	{
		return false;
	}
	
	public boolean isCastlingMove()
	{
		return false;
	}
	
	//Just for subclass to implement
	public Piece getAttackedPiece()
	{
		return null;
	}
	
	
	public static class NormalMove extends Move
	{
		public NormalMove(Piece movedPiece, ChessBoard board, int destionationCo) {
			super(movedPiece, board, destionationCo);
		}	
	}
	
	
	public static class AttackMove extends Move
	{
		private Piece attackedPiece;
		public AttackMove(Piece movedPiece, ChessBoard board, int destionationCo, Piece attackedPiece) {
			super(movedPiece, board, destionationCo);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public ChessBoard execute() {
			ChessBoard.Builder builder = new ChessBoard.Builder();
			for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece)) //If the pieces on the board are not the moved piece, keep its position the same
				{
					builder.locatePiece(piece);
				}
			}
			
			for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
			{
				if (!attackedPiece.equals(piece))
					builder.locatePiece(piece);
			}
			//Move the moved piece here, movedPiece methods take in the move and give a new piece with updated position
			builder.locatePiece(movedPiece.movedPiece(this));
			//Set the next mover
			builder.setMover(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
		@Override
		public boolean isAttackMove()
		{
			return true;
		}
		
		@Override
		public Piece getAttackedPiece()
		{
			return this.attackedPiece;
		}
		
	}
	
	public static class PawnAttackMove extends AttackMove //Attack move of the pawn
	{
		public PawnAttackMove(Piece movedPiece, ChessBoard board, int destionationCo, Piece attackPiece) {
			super(movedPiece, board, destionationCo, attackPiece);
		}
	}
	
	public static class PawnPromotionNormalMove extends NormalMove
	{
		private Piece promoteTo;
		public PawnPromotionNormalMove(Piece movedPiece, ChessBoard board, int destionationCo) {
			super(movedPiece, board, destionationCo);
		}
		
		public void setPromoteTo(Piece piece)
		{
			this.promoteTo = piece;
		}
		
		@Override
		public ChessBoard execute() {
			ChessBoard.Builder builder = new ChessBoard.Builder();
			for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece)) //If the pieces on the board are not the moved piece, keep its position the same
				{
					builder.locatePiece(piece);
				}
			}
			
			for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
			{
				builder.locatePiece(piece);
			}

			if (promoteTo != null)
			{
				builder.locatePiece(promoteTo);
			}
			else 
			{
				builder.locatePiece(movedPiece.movedPiece(this));
			}
			//Set the next mover
			builder.setMover(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
	}
	
	public static class PawnPromotionAttackMove extends AttackMove
	{
		private Piece promoteTo;
		public PawnPromotionAttackMove(Piece movedPiece, ChessBoard board, int destionationCo, Piece attackedPiece) {
			super(movedPiece, board, destionationCo, attackedPiece);
		}
		

		public void setPromoteTo(Piece piece)
		{
			this.promoteTo = piece;
		}
		
		@Override
		public ChessBoard execute() {
			ChessBoard.Builder builder = new ChessBoard.Builder();
			for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece)) //If the pieces on the board are not the moved piece, keep its position the same
				{
					builder.locatePiece(piece);
				}
			}
			
			for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
			{
				builder.locatePiece(piece);
			}

			if (promoteTo != null)
			{
				builder.locatePiece(promoteTo);
			}
			else 
			{
				builder.locatePiece(movedPiece.movedPiece(this));
				builder.setMover(this.board.getCurrentPlayer().getOpponent().getAlliance());
			}
			//Set the next mover
			return builder.build();
		}
	}
	
	public static class PawnEnPassantAttackMove extends PawnAttackMove //Special attack move of the pawn
	{
		public PawnEnPassantAttackMove(Piece movedPiece, ChessBoard board, int destionationCo, Piece attackPiece) {
			super(movedPiece, board, destionationCo, attackPiece);
		}
		
		public ChessBoard execute()
		{
			ChessBoard.Builder builder = new ChessBoard.Builder();
			for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece)) //If the pieces on the board are not the moved piece
				{
					builder.locatePiece(piece);
				}
			}
			
			for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
			{
				if (!piece.equals(this.getAttackedPiece()))
				{
					builder.locatePiece(piece);
				}
			}
			builder.locatePiece(movedPiece.movedPiece(this));
			builder.setMover(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}
	
	public static class PawnJump extends NormalMove //The jump of the pawn
	{
		public PawnJump(Piece movedPiece, ChessBoard board, int destionationCo) {
			super(movedPiece, board, destionationCo);
		}
		
		@Override
		public ChessBoard execute()
		{
			ChessBoard.Builder builder = new ChessBoard.Builder();
			for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece))
				{
					builder.locatePiece(piece);
				}
			}
			
			for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
			{
				builder.locatePiece(piece);
			}
			
			Pawn movedPawn = (Pawn) movedPiece.movedPiece(this);
			builder.locatePiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setMover(this.board.getCurrentPlayer().getOpponent().getAlliance());		
			return builder.build();
		}
	}
	
	public static class NullMove extends Move //The jump of the pawn
	{
		public NullMove() {
			super(null,-1);
		}	
	}
	
	public static abstract class CastleMove extends Move
	{
		protected Rook castledRook;
		protected int castledRookStart;
		protected int castledRookEnd;
		
		public CastleMove(Piece movedPiece, ChessBoard board, int destionationCo, Rook castledRook, int castleRookStart, int castleRookEnd) {
			super(movedPiece, board, destionationCo);
			this.castledRook = castledRook;
			this.castledRookStart = castleRookStart;
			this.castledRookEnd = castleRookEnd;
		}
		
		public Rook getcastledRook()
		{
			return castledRook;
		}
		
		@Override
		public boolean isCastlingMove()
		{
			return true;
		}
		
		@Override
		public ChessBoard execute()
		{
			ChessBoard.Builder builder = new ChessBoard.Builder();
			for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece) && !castledRook.equals(piece))
				{
					builder.locatePiece(piece);
				}
			}
			
			for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
			{
				builder.locatePiece(piece);
			}
			builder.locatePiece(movedPiece.movedPiece(this));
			//TODO first move on normal piece
			builder.locatePiece(new Rook(castledRookEnd, castledRook.getPieceGroup()));
			builder.setMover(board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		};
		
	}
	
	public static class KingSideCastleMove extends CastleMove
	{
		public KingSideCastleMove(Piece movedPiece, ChessBoard board, int destionationCo,
				Rook castledRook, int castleRookStart, int castleRookEnd) {
			super(movedPiece, board, destionationCo,castledRook,castleRookStart,castleRookEnd);
		}
		
		@Override
		public String toString()
		{
			return "kingside";
		}
	}
	
	public static class QueenSideCastleMove extends CastleMove
	{
		public QueenSideCastleMove(Piece movedPiece, ChessBoard board, int destionationCo,
				Rook castledRook, int castleRookStart, int castleRookEnd) {
			super(movedPiece, board, destionationCo,castledRook,castleRookStart,castleRookEnd);
		}
		
		@Override
		public String toString()
		{
			return "queenside";
		}
	}
	
	public static Move getMoves(ChessBoard board, int currentCoordinate, int destinationCoordinate)
	{
		for (Move move:board.getAllValidMoves())
		{
			if (move.getCurrentCoordinate() == currentCoordinate &&
					move.getDestinationCoordinate() == destinationCoordinate)
			{
				return move;
			}
		}
		return NULL_MOVE;
	}
	

	public ChessBoard execute() {
		//If the player made the move, it will make another board, not change the original board
		ChessBoard.Builder builder = new ChessBoard.Builder();
		for (Piece piece:this.board.getCurrentPlayer().getActivePieces())
		{
			if (!this.movedPiece.equals(piece)) //If the pieces on the board are not the moved piece
			{
				builder.locatePiece(piece);
			}
		}
		
		for (Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces())
		{
			builder.locatePiece(piece);
		}
		//Move the moved piece here
		builder.locatePiece(this.movedPiece.movedPiece(this));
		//Set the next mover
		builder.setMover(this.board.getCurrentPlayer().getOpponent().getAlliance());
		return builder.build();
	}
	
}
