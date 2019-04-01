package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import LTChess.ChessBoard.ChessBoard;
import LTChess.Group.Group;
import LTChess.Player.Player;

public class EditInitialBoard extends JFrame
{
	private BoardPanel boardPanel;
	private static StockCustomBoard stock;
	private static JPanel controlPanel;
	private JButton okBtn;
	private JButton discardBtn;
	private boolean discard; //Is user chose to discard the custom board
	
	public EditInitialBoard(MainFrame parent, NewGameDialog newGame)
	{
		newGame.setVisible(false);
		parent.gameFrame.setVisible(false);
		setSize(new Dimension(780,690));
		setResizable(false);
		setTitle("Edit initial board");
		MainFrame.board = ChessBoard.createEmptyChessboard(newGame.getFirstMover());
		if (stock == null)
		{
			stock = new StockCustomBoard();
		}
		
		discard = false;
		boardPanel = new BoardPanel(parent);
		controlPanel = new JPanel();
		okBtn = new JButton("OK");
		discardBtn = new JButton("Discard");
		controlPanel.setLayout(new FlowLayout());
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player firstMover = MainFrame.board.getPlayer(newGame.getFirstMover());
				if (firstMover.getOpponent().isInCheck())
				{
					JOptionPane.showMessageDialog(EditInitialBoard.this,
							"Your custom board is not valid because the king of "+firstMover.getOpponent().getAlliance()+" might be killed"
									+ " right on the first move on the board",
							"Error", JOptionPane.ERROR_MESSAGE);
					return ;
				}
				else if (firstMover.isInCheckMate() || firstMover.isInStaleMate())
				{
					JOptionPane.showMessageDialog(EditInitialBoard.this,
							"Your custom board is not valid because "+firstMover.getAlliance()+" has no valid move when he starts the first move on the board",
							"Error", JOptionPane.ERROR_MESSAGE);
					return ;
				}
				else
				{
					MainFrame.board.customBoardMode = false;
					EditInitialBoard.this.setVisible(false);
//					parent.gameFrame.setVisible(true);
//					newGame.setVisible(true);
					newGame.startAnotherGame();
					//Set custom board mode to false (allow user to play)
				}
			}
		});
		
		discardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.board = null;
				EditInitialBoard.this.dispose();
				if (parent.gameFrame == null)
				{
					parent.gameFrame.setVisible(true);
					parent.gameFrame.setAlwaysOnTop(true);
				}
				newGame.setVisible(true);
			}
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		controlPanel.add(okBtn);
		controlPanel.add(discardBtn);
		add(boardPanel,BorderLayout.CENTER);
		add(stock,BorderLayout.WEST);
		add(controlPanel,BorderLayout.SOUTH);
		setVisible(true);
	}
	
	public static StockCustomBoard getStockCustomBoard()
	{
		return stock;
	}
	
	public boolean isDiscard()
	{
		return this.discard;
	}
	
}
