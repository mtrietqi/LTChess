package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import LTChess.ChessBoard.Move;
import LTChess.ChessBoard.Move.PawnPromotionAttackMove;
import LTChess.ChessBoard.Move.PawnPromotionNormalMove;
import LTChess.Group.Group;
import LTChess.Pieces.Bishop;
import LTChess.Pieces.Knight;
import LTChess.Pieces.Piece;
import LTChess.Pieces.Queen;
import LTChess.Pieces.Rook;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class PromotionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnB;
	private JButton btnQ;
	private JButton btnR;
	private JButton btnN;
	private Piece promoteTo;
	private PawnPromotionNormalMove normalPromotion;
	private PawnPromotionAttackMove attackPromotion;
	private MainFrame parent;
	
	/**
	 * Create the dialog.
	 */
	public PromotionDialog(Move move, MainFrame parent) {
		setResizable(false);
		this.parent = parent;
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		if (move instanceof PawnPromotionNormalMove)
		{
			this.normalPromotion = (PawnPromotionNormalMove)move;
		}
		else if (move instanceof PawnPromotionAttackMove)
		{
			this.attackPromotion = (PawnPromotionAttackMove) move;
		}
		promoteTo = null;
		setTitle("Promotion Dialog");
		setBounds(100, 100, 436, 149);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPleaseChooseA = new JLabel("Please choose a piece that you want to promote your pawn into:");
		lblPleaseChooseA.setBounds(10, 11, 448, 14);
		contentPanel.add(lblPleaseChooseA);
		
		btnQ = new JButton("");
		btnQ.addActionListener(new btnListener());
		btnQ.setBounds(10, 36, 89, 67);
		
		btnB = new JButton("");
		btnB.addActionListener(new btnListener());
		btnB.setBounds(115, 36, 89, 67);
		
		btnR = new JButton("");
		btnR.addActionListener(new btnListener());
		btnR.setBounds(324, 36, 89, 67);
		
		btnN = new JButton("");
		btnN.addActionListener(new btnListener());
		btnN.setBounds(219, 36, 89, 67);
		
		String pieceIconPath = "resource/";
		try {
			BufferedImage image = ImageIO.read(new File(pieceIconPath 
					+MainFrame.board.getCurrentPlayer().getOpponent().getAlliance().toString().substring(0,1)+"Q.png"));
			ImageIcon ic = new ImageIcon(image);
			btnQ.setIcon(ic);

			image = ImageIO.read(new File(pieceIconPath 
					+MainFrame.board.getCurrentPlayer().getOpponent().getAlliance().toString().substring(0,1)+"B.png"));
			btnB.setIcon(new ImageIcon(image));

			image = ImageIO.read(new File(pieceIconPath 
					+MainFrame.board.getCurrentPlayer().getOpponent().getAlliance().toString().substring(0,1)+"N.png"));
			btnN.setIcon(new ImageIcon(image));

			image = ImageIO.read(new File(pieceIconPath 
					+MainFrame.board.getCurrentPlayer().getOpponent().getAlliance().toString().substring(0,1)+"R.png"));
			btnR.setIcon(new ImageIcon(image));
		} catch (IOException e) {
			e.printStackTrace();
		}

		contentPanel.add(btnN);
		contentPanel.add(btnB);
		contentPanel.add(btnR);
		contentPanel.add(btnQ);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (promoteTo == null && normalPromotion != null)
				{
					promoteTo = new Queen(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					normalPromotion.setPromoteTo(promoteTo);
					MainFrame.board = normalPromotion.execute();
					MainFrame.drawBoard();
				}
				else if (promoteTo == null && attackPromotion != null)
				{
					promoteTo = new Queen(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					attackPromotion.setPromoteTo(promoteTo);
					MainFrame.board = attackPromotion.execute();
					MainFrame.drawBoard();
				}
				parent.updateClockMover();
			}
		});
		this.setVisible(true);
	}
	
	private class btnListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btnB)
			{
				if (normalPromotion != null)
				{
					promoteTo = new Bishop(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					normalPromotion.setPromoteTo(promoteTo);
					MainFrame.board = normalPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+normalPromotion.getCurrentCoordinate()
													+" "+normalPromotion.getDestinationCoordinate()+" "+"b";
						parent.waitForOpponent();
					}
				}
				else if (attackPromotion != null)
				{
					promoteTo = new Bishop(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					attackPromotion.setPromoteTo(promoteTo);
					MainFrame.board = attackPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+attackPromotion.getCurrentCoordinate()
													+" "+attackPromotion.getDestinationCoordinate()+" "+"b";
						parent.waitForOpponent();
					}
				}
				PromotionDialog.this.dispose();
			}
			else if (e.getSource() == btnN)
			{
				if (normalPromotion != null)
				{
					promoteTo = new Knight(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					normalPromotion.setPromoteTo(promoteTo);
					MainFrame.board = normalPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+normalPromotion.getCurrentCoordinate()
													+" "+normalPromotion.getDestinationCoordinate()+" "+"n";
						parent.waitForOpponent();
					}
				}
				else if (attackPromotion != null)
				{
					promoteTo = new Knight(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					attackPromotion.setPromoteTo(promoteTo);
					MainFrame.board = attackPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+attackPromotion.getCurrentCoordinate()
													+" "+attackPromotion.getDestinationCoordinate()+" "+"n";
						parent.waitForOpponent();
					}
				}
				PromotionDialog.this.dispose();
			}
			else if (e.getSource() == btnR)
			{
				if (normalPromotion != null)
				{
					promoteTo = new Rook(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					normalPromotion.setPromoteTo(promoteTo);
					MainFrame.board = normalPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+normalPromotion.getCurrentCoordinate()
													+" "+normalPromotion.getDestinationCoordinate()+" "+"r";
						parent.waitForOpponent();
					}
				}
				else if (attackPromotion != null)
				{
					promoteTo = new Rook(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					attackPromotion.setPromoteTo(promoteTo);
					MainFrame.board = attackPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+attackPromotion.getCurrentCoordinate()
													+" "+attackPromotion.getDestinationCoordinate()+" "+"r";
						parent.waitForOpponent();
					}
				}
				PromotionDialog.this.dispose();
			}
			else if (e.getSource() == btnQ)
			{
				if (normalPromotion != null)
				{
					promoteTo = new Queen(normalPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					normalPromotion.setPromoteTo(promoteTo);
					MainFrame.board = normalPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+normalPromotion.getCurrentCoordinate()
													+" "+normalPromotion.getDestinationCoordinate()+" "+"q";
						parent.waitForOpponent();
					}
				}
				else if (attackPromotion != null)
				{
					promoteTo = new Queen(attackPromotion.getDestinationCoordinate(), MainFrame.board.getCurrentPlayer().getOpponent().getAlliance());
					attackPromotion.setPromoteTo(promoteTo);
					MainFrame.board = attackPromotion.execute();
					MainFrame.drawBoard();
					if (MainFrame.board.onlineMode)
					{
						BoardPanel.messageSend = "promo "+attackPromotion.getCurrentCoordinate()
													+" "+attackPromotion.getDestinationCoordinate()+" "+"q";
						parent.waitForOpponent();
					}
				}
				PromotionDialog.this.dispose();
			}
			parent.updateClockMover();
		}
		
	}

	public Piece getPromoteToPiece()
	{
		return this.promoteTo;
	}
}
