package LTChess.ChessBoard;
/*
 * This class represents each tile (square) in the chessboard 
 */

import java.util.HashMap;
import java.util.Map;

import LTChess.Pieces.*;

public abstract class Square implements java.io.Serializable{
	protected final int squareCoordinate;
	private static final Map<Integer, EmptySquare> EMPTY_SQUARE = createEmptySquares();

	// This method is used to create all empty tiles on the board
	private static Map<Integer, EmptySquare> createEmptySquares() {
		Map<Integer, EmptySquare> emptySquareMap = new HashMap<Integer, EmptySquare>();
		for (int i = 0; i < 64; i++) {
			emptySquareMap.put(i, new EmptySquare(i));
		}
		return emptySquareMap;
	}

	// This method is used to create a single tile on the board
	public static Square createSquare(int coordinate, Piece piece) {
		if (piece == null)
			return new EmptySquare(coordinate);
		else
			return new OccupiedSquare(coordinate, piece);
	}

	public Square(int tileCoordinate) {
		this.squareCoordinate = tileCoordinate;
	}
	
	public int getSquareCoordinate()
	{
		return squareCoordinate;
	}

	public abstract boolean isSquareOccupied(); //Method to check whether tile is occupied by any piece

	public abstract Piece getPiece();
	
	//Class Occupied is a tile but it is NOT occupied by any piece
	public static class EmptySquare extends Square {
		public EmptySquare(int tileCoordinate) {
			super(tileCoordinate);
		}

		@Override
		public boolean isSquareOccupied() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}
		
		@Override
		public String toString()
		{
			return "-";
		}

	}
	
	//Class Occupied is a tile but it is occupied by a piece
	public static class OccupiedSquare extends Square {
		private Piece currentPiece;

		public OccupiedSquare(int coordinate, Piece currentPiece) {
			super(coordinate);
			this.currentPiece = currentPiece;
		}

		@Override
		public boolean isSquareOccupied() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Piece getPiece() {
			// TODO Auto-generated method stub
			return currentPiece;
		}
		
		@Override
		public String toString()
		{
			if (getPiece().getPieceGroup().isBlack())
			{
				return getPiece().toString().toLowerCase();
			}
			else 
			{
				return getPiece().toString();
			}
		}
	}

}
