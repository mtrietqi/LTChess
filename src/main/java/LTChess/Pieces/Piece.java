package LTChess.Pieces;
import java.util.Collection;

import LTChess.ChessBoard.*;
import LTChess.Group.*;

public abstract class Piece implements java.io.Serializable
{
	protected int piecePosition; //current position of the piece
	protected Group pieceAlliance; //the alliance (black or white) of the piece
	protected boolean isFirstMove; //is it the first move?
	protected PieceType pieceType; //what is the type of piece?
	
	public Piece(PieceType pieceType, int piecePosition, Group pieceAlliance, boolean isFirstMove)
	{
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.pieceType = pieceType;
		this.isFirstMove  = isFirstMove;
//		this.hashCodeMemory = calculateHashcode();
	}
	

	@Override
	public boolean equals(Object otherObject) // equals method is used to compare two pieces
	{
		Piece otherPiece = (Piece) otherObject;
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
				pieceAlliance == otherPiece.getPieceGroup();
	}
	
	public boolean isFirstMove()
	{
		return this.isFirstMove;
	}
	
	public Group getPieceGroup()
	{
		return pieceAlliance;
	}
	
	public int getPiecePosition()
	{
		return piecePosition;
	}
	
	public PieceType getPieceType()
	{
		return this.pieceType;
	}
	
	
	public abstract Collection<Move> calculateValidMove(ChessBoard board);
	public abstract Piece movedPiece(Move move); // That method takes in a Move and give another piece with updated position
	public void setPosition(int position)
	{
		this.piecePosition = position;
	}
	
	public Piece PieceFactory(PieceType pieceType, int position, Group group)
	{
		if (pieceType == PieceType.KING)
		{
			return new King(position,group);
		}
		else if (pieceType == PieceType.QUEEN)
		{
			return new Queen(position,group);
		}
		else if (pieceType == PieceType.ROOK)
		{
			return new Rook(position,group);
		}
		else if (pieceType == PieceType.BISHOP)
		{
			return new Bishop(position,group);
		}
		else if (pieceType == PieceType.KNIGHT)
		{
			return new Knight(position,group);
		}
		return null;
	}
	
	//This method identifies piece types for toString() method in each piece
	public enum PieceType
	{
		PAWN("P",5) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}

			@Override
			public int getValue() {
				return 5;
			}
		},
		KNIGHT("N",10) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}

			@Override
			public int getValue() {
				return 10;
			}
		},
		BISHOP("B",10) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}

			@Override
			public int getValue() {
				return 10;
			}
		},
		ROOK("R",15) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}

			@Override
			public int getValue() {
				return 15;
			}
		},
		QUEEN("Q",20) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}

			@Override
			public int getValue() {
				return 20;
			}
		},
		KING("K",100) {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}

			@Override
			public int getValue() {
				return 100;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		
		PieceType(String pieceName, int pieceValue)
		{
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		};
		
		public String toString()
		{
			return this.pieceName;
		}
		
		public abstract boolean isKing();
		public abstract boolean isRook();
		public abstract int getValue();
	
	}

}
