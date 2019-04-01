package LTChess.ChessBoard;

import LTChess.GUI.MainFrame;
import LTChess.Group.Group;

/*
 * This class is used to check whether a coordinate is valid (less than 0 or greater than 64), 
 * is it the first column, second column, seventh column, eighth column
 */

public class BoardCheck implements java.io.Serializable
{
	public static final boolean[] FIRST_COLUMN = startColumn(0);
	public static final boolean[] SECOND_COLUMN = startColumn(1);
	public static final boolean[] SEVENTH_COLUMN = startColumn(6);
	public static final boolean[] EIGHTH_COLUMN = startColumn(7);
	
	public static final boolean[] FIRST_ROW = startRow(0); 
	public static final boolean[] SECOND_ROW = startRow(8); 
	public static final boolean[] SEVENTH_ROW = startRow(48); 
	public static final boolean[] EIGHTH_ROW = startRow(56);
	//the parameter is the tile which begins the row
	
	public static String[] horizontalStringCoordinate = {"a","b","c","d","e","f","g","h"};
	public static String[] verticalStringCoordinate = {"8","7","6","5","4","3","2","1"};
	
	public static boolean isValidTileCoordinate(int coordinate) //Check the if the destination is out of the board or not
	{
		return coordinate >= 0 && coordinate < 64;
	}

	public static boolean[] startColumn(int columnNumber) {
		boolean[] column = new boolean[64];
		do
		{
			column[columnNumber] = true;
			columnNumber += 8;
		}
		while (columnNumber < 64);
		return column;
	}
	
	private static boolean[] startRow(int rowNumber)
	{
		boolean[] row = new boolean[64];
		do{
			row[rowNumber] = true;
			rowNumber++;
		}
		while(rowNumber % 8 != 0);
		return row;
	}
	
	public static String convertToChessCoordinate(int coordinate)
	{
		return horizontalStringCoordinate[coordinate%8]+verticalStringCoordinate[(int)coordinate/8];
	}
	
	public static boolean isValidChessCoordinate(String chessCo)
	{
		try
		{
			if (chessCo.length() == 3)
				return false;
			else if (Integer.parseInt(chessCo.substring(1)) > 8 || Integer.parseInt(chessCo.substring(1)) < 1)
				return false;
			else
			{
				for (String str: horizontalStringCoordinate)
				{
					if (chessCo.substring(0,1).equals(str))
						return true;
				}
				return false;
			}
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	public static int convertChessCoordinateToInt(String chessCo)
	{
		int result = 0;
		for (int i = 0; i < horizontalStringCoordinate.length; i++)
		{
			if (chessCo.substring(0,1).equals(horizontalStringCoordinate[i]))
			{
				result += i;
			}
		}
		for (int i = 0; i < verticalStringCoordinate.length; i++)
		{
			if (chessCo.substring(1).equals(verticalStringCoordinate[i]))
			{
				result += 8*i;
			}
		}
		return result;
	}
	
	public static String convertToUCISituation(Move move, String promotTo)
	{
		String result = " ";
		result += convertToChessCoordinate(move.getCurrentCoordinate())+convertToChessCoordinate(move.getDestinationCoordinate())+promotTo;
		return result;
	}
	
	public static String covertToFenCode(ChessBoard board)
	{
		int emptyNum = 0;
		String fen = "";
		for (int i = 0; i < 64; i++)
		{
			Square square = board.getSquare(i);
			if (i % 8 == 0)
			{
				if (i != 0)
				{
					if (emptyNum != 0)
						fen+=emptyNum;
					fen+= "/";
				}
				emptyNum = 0;
			}
			if (square.isSquareOccupied())
			{
				if (emptyNum != 0)
				{
					fen+=emptyNum;
					emptyNum = 0;
				}
				if (square.getPiece().getPieceGroup() == Group.BLACK)
				{
					fen+=square.getPiece().toString().toLowerCase();
				}
				else
				{
					fen+=square.getPiece().toString();
				}
			}
			else
			{
				emptyNum++;
			}
			if (i == 63 && !square.isSquareOccupied())
			{
				fen+= emptyNum;
			}
		}
		fen += " "+MainFrame.board.getCurrentPlayer().getAlliance().toString().substring(0,1).toLowerCase()+" ";
		if (MainFrame.board.whiteCanCastle)
		{
			if (!MainFrame.board.getWhitePlayer().findKing().isFirstMove())
			{
				MainFrame.board.whiteCanCastle = false;
			}
			else {
				if (MainFrame.board.getSquare(63).getPiece() != null && MainFrame.board.getSquare(63).getPiece().getPieceType().isRook()
						&& MainFrame.board.getSquare(63).getPiece().isFirstMove()) {
					fen += "K";
				}
				if (MainFrame.board.getSquare(56).getPiece() != null &&  MainFrame.board.getSquare(56).getPiece().getPieceType().isRook()
						&& MainFrame.board.getSquare(56).getPiece().isFirstMove()) {
					fen += "Q";
				}
			}
		}
		if (MainFrame.board.blackCanCastle)
		{
			if (!MainFrame.board.getBlackPlayer().findKing().isFirstMove())
			{
				MainFrame.board.blackCanCastle = false;
			}
			else {
				if (MainFrame.board.getSquare(7).getPiece() != null && MainFrame.board.getSquare(7).getPiece().getPieceType().isRook()
						&& MainFrame.board.getSquare(7).getPiece().isFirstMove()) {
					fen += "k";
				}
				if (MainFrame.board.getSquare(0).getPiece() != null && MainFrame.board.getSquare(0).getPiece().getPieceType().isRook()
						&& MainFrame.board.getSquare(0).getPiece().isFirstMove()) {
					fen += "q";
				}
			}
		}
		if (!MainFrame.board.blackCanCastle && !MainFrame.board.whiteCanCastle)
		{
			fen +="-";
		}
		
		if (MainFrame.board.getEnpassantPawn() != null)
		{
			if (MainFrame.board.getEnpassantPawn().getPieceGroup() == Group.WHITE)
			{
				fen += " "+MainFrame.board.getEnpassantPawn().toString();
			}
			else
			{
				fen += " "+MainFrame.board.getEnpassantPawn().toString().toLowerCase();
			}
		}
		else
		{
			fen += " -";
		}
		fen +=" 0 1";
		return fen;
	}
	
}
