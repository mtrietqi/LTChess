package LTChess.UCI;

import java.util.ArrayList;
import java.util.List;

import LTChess.GUI.DefeatedPiecePanel;
import LTChess.Pieces.Knight;
import LTChess.Pieces.Pawn;
import LTChess.Pieces.Piece;

public class UndoStack 
{
	public  List<UndoData> undoStack;
	public int undoPoint;
	public int initialPoint;
	public int redoPoint;
	
	public UndoStack()
	{
		undoStack = new ArrayList<UndoData>();
		undoPoint = -1;
		initialPoint = -1;
		redoPoint = -1;
	}
	
	public void add(UndoData data)
	{
		if (undoPoint == undoStack.size()-1)
		{
			undoStack.add(data);
		}
		else
		{
			//Remove all undo data which is after the undo point
			int stackSize = undoStack.size()-1;
			for (int i = stackSize; i > undoPoint; i--)
			{
				undoStack.remove(undoStack.get(i));
			}
			undoStack.add(data);
		}
		undoPoint = undoStack.size() -1;
		initialPoint = undoPoint;
		redoPoint = undoPoint+1;
	}
	
	public UndoData undo()
	{
		redoPoint = undoPoint+1;
		return undoStack.get(undoPoint--);
	}
	
	public UndoData redo()
	{
		undoPoint = redoPoint-1;
		return undoStack.get(redoPoint++);
	}
	
	public boolean canUndo()
	{
		return undoPoint >= 0;
	}
	
	public boolean canRedo()
	{
		return redoPoint != -1 && redoPoint < undoStack.size();
	}
	
	public void addMostUpdatedData(UndoData data)
	{
		if (undoStack.size()  < initialPoint+2) //Not add updated data yet
		{
			undoStack.add(initialPoint+1, data);
		}
	}
	
	public String toString()
	{
		String str = "";
		str +="Undo point: "+undoPoint+"\n";
		str +="Redo point: "+redoPoint+"\n";
		str+= "Initial point: "+initialPoint+"\n";
		for (int i = 0; i < undoStack.size();i++)
		{
			str+= "Board "+i+":\n"+undoStack.get(i).getBoard();
		}
		return "Current:\n"+str;
	}
	
}
