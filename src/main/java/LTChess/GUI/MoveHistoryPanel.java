package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import LTChess.ChessBoard.BoardCheck;
import LTChess.ChessBoard.Move;
import LTChess.Group.Group;

public class MoveHistoryPanel extends JPanel implements java.io.Serializable
{
	private String[] columnNames = {"White Moves","Black Moves"};
	private String[][] tableData = new String[0][2];
	private List<String> mainData = new ArrayList<String>();
	private JScrollPane scrollPane;
	private Group firstMover;
	private JTable table;
	
	public MoveHistoryPanel()
	{
		this.setLayout(new BorderLayout());
		table = new JTable(tableData,columnNames);
		scrollPane = new JScrollPane();
		table.setForeground(new Color(0, 0, 0));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		scrollPane.setViewportView(table);
		add(scrollPane);
		setPreferredSize(new Dimension(180,300));
		setVisible(true);
	}

	public void addMoveHistory(Move move) 
	{
		String[][] backup = new String[0][2];
		this.removeAll();
		String history = move.getMovePiece()+":"+BoardCheck.convertToChessCoordinate(move.getDestinationCoordinate());
		if (MainFrame.board.getCurrentPlayer().getOpponent().getAlliance() == Group.WHITE)
		{
			backup = tableData.clone();
			if (MainFrame.board.getCurrentPlayer().getOpponent().getAlliance() == firstMover)
			{
				int currentSize = tableData.length;
				tableData = new String[currentSize+1][2];
			}
			if (backup.length != 0) //At first, the length of backup = 0, so we need that condition
			{
				for (int i = 0; i < backup.length; i++)
				{
					for (int j = 0; j < backup[0].length; j++)
					{
						tableData[i][j] = backup[i][j];
					}
				}
			}
			if (move instanceof Move.KingSideCastleMove)
			{
				history = "K:g1, R:f1";
				tableData[tableData.length-1][0] = history;
			}
			else if (move instanceof Move.QueenSideCastleMove)
			{
				history = "K:c1,R:d1";
				tableData[tableData.length-1][0] = history;
			}
			else
			{
				tableData[tableData.length-1][0] = history; 
			}
		}
		else if (MainFrame.board.getCurrentPlayer().getOpponent().getAlliance() == Group.BLACK)
		{
			backup = tableData.clone();
			if (MainFrame.board.getCurrentPlayer().getOpponent().getAlliance() == firstMover)
			{
				int currentSize = tableData.length;
				tableData = new String[currentSize+1][2];
			}
			if (backup.length != 0)
			{
				for (int i = 0; i < backup.length; i++)
				{
					for (int j = 0; j < backup[0].length; j++)
					{
						tableData[i][j] = backup[i][j];
					}
				}
			}
			if (move instanceof Move.KingSideCastleMove)
			{
				history = "K:g8, R:f8";
				tableData[tableData.length-1][1] = history;
			}
			else if (move instanceof Move.QueenSideCastleMove)
			{
				history = "K:c8, R:d8";
				tableData[tableData.length-1][1] = history;
			}
			else
			{
				if (tableData.length-1 >= 0)
				{
					tableData[tableData.length-1][1] = history;
				}
				else
				{
					tableData[tableData.length-1][1] = history;
				}
				
			}
		}
		
		table = new JTable(tableData,columnNames);
		scrollPane = new JScrollPane();
		table.setForeground(new Color(0, 0, 0));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		scrollPane.setViewportView(table);
		add(scrollPane);
	}
	
	public void setFirstMoverAlliance(Group alliance) 
	{
		this.firstMover = alliance;
	}
	
	public void reset()
	{
		this.removeAll();
		mainData = new ArrayList<String>();
		this.setLayout(new BorderLayout());
		table = new JTable(tableData,columnNames);
		scrollPane = new JScrollPane();
		table.setForeground(new Color(0, 0, 0));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		scrollPane.setViewportView(table);
		add(scrollPane);
		setPreferredSize(new Dimension(180,300));
		setVisible(true);
		this.repaint();
		this.revalidate();
	}
	
	public String[][] getHistoryData()
	{
		return tableData;
	}
	
	public void setHistoryData(String[][] data)
	{
		this.tableData = data;
		reset();
	}
}
