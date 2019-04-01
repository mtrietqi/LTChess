package LTChess.GUI;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import LTChess.ChessBoard.ChessBoard;
import LTChess.UCI.NewGameUModeDialog;
import LTChess.UCI.UCI;
import LTChess.UCI.UCILevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveGame implements java.io.Serializable
{
	private ChessBoard board;
	private MoveHistoryPanel moveHistory;
	private String clockPanelData;
	private DefeatedPiecePanel defeatedPiecesPanel;
	private String fileName;
	private boolean UCIMode;
	private UCILevel UCILevel;
	private String UCIPath;
	private String UCISitutation = "";
	
	public SaveGame()
	{
		
	}
	
	public SaveGame(ChessBoard board, ClockPanel clockPanel,MoveHistoryPanel moveHistory, DefeatedPiecePanel defeatedPanel, boolean UCI)
	{
		this.board = board;
		clockPanelData = clockPanel.exportSaveData();
		this.defeatedPiecesPanel = defeatedPanel;
		this.moveHistory = moveHistory;
		this.UCIMode= UCI;
		if (UCIMode)
		{
			UCILevel = NewGameUModeDialog.uci.getUCILevel();
			UCIPath = NewGameUModeDialog.uci.getUCIPath();
			UCISitutation =  BoardPanel.inputCode1;
		}
	}
	
	public void saveToFile()
	{
		JFileChooser saveIt = new JFileChooser("C:\\");
		saveIt.setDialogTitle("Save");
		saveIt.setFileFilter(new fileFilter()); 
		if (saveIt.showSaveDialog(null)== JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File chosenFile = saveIt.getSelectedFile();
				FileOutputStream file;
				ObjectOutputStream outputFile;
				
				// If the chosen file has the extension .ltchess already, we don't
				// need to add it any more
				if (chosenFile.getName().endsWith(".ltchess")) 
				{
					file = new FileOutputStream(chosenFile.getAbsolutePath());
					outputFile = new ObjectOutputStream(file);
				} 
				else 
				{
					file = new FileOutputStream(chosenFile.getAbsolutePath()+".ltchess");
					outputFile = new ObjectOutputStream(file);
				}

				// If the file the user wants to save exists already, ask him
				// whether
				// he want to overwrite it
				if (chosenFile.exists()) {
					int result = JOptionPane.showConfirmDialog(null,
							"The file aldready exists. Do you want to overwrite it?");
					if (result == JOptionPane.YES_OPTION)
					{
						outputFile.writeObject(this);
						outputFile.close();
						JOptionPane.showMessageDialog(null, "Your game was saved successfully");
					}
				}
				else
				{
					outputFile.writeObject(this);
					outputFile.close();
					JOptionPane.showMessageDialog(null, "Your game was saved successfully");
					fileName = chosenFile.getName()+".ltchess";
				}

			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null, "An error occured during saving");
				e.printStackTrace();
			}
		}
	}
	
	private class fileFilter extends FileFilter 
	{
		@Override
		public boolean accept(File f) {
			String fn = f.getName();
			return f.isDirectory() || fn.endsWith(".ltchess");
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return ".ltchess File";
		}
	}

	public ChessBoard getBoard() {
		return board;
	}

	public MoveHistoryPanel getMoveHistory() {
		return moveHistory;
	}

	public String getClockPanelData() {
		return clockPanelData;
	}

	public DefeatedPiecePanel getDefeatedPiecesPanel() {
		return defeatedPiecesPanel;
	}
	
	
	public String getFileName()
	{
		return fileName;
	}
	
	public boolean isUCIMode()
	{
		return UCIMode;
	}

	public String getUCIPath() {
		return UCIPath;
	}
	
	public UCILevel getUCILevel()
	{
		return UCILevel;
	}
	
	public String getUCISituation()
	{
		return UCISitutation;
	}
	
}
